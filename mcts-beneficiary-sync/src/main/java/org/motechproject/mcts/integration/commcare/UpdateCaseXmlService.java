package org.motechproject.mcts.integration.commcare;

import java.util.Date;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.motechproject.mcts.integration.exception.BeneficiaryException;
import org.motechproject.mcts.integration.hibernate.model.MctsPregnantMother;
import org.motechproject.mcts.integration.repository.CareDataRepository;
import org.motechproject.mcts.integration.service.FixtureDataService;
import org.motechproject.mcts.integration.service.MCTSHttpClientService;
import org.motechproject.mcts.utils.CommcareConstants;
import org.motechproject.mcts.utils.ObjectToXMLConverter;
import org.motechproject.mcts.utils.PropertyReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Class to create xml for update cases.
 * 
 * @author aman
 * 
 */
@Transactional
@Service
public class UpdateCaseXmlService {

    private final static Logger LOGGER = LoggerFactory
            .getLogger(UpdateCaseXmlService.class);

    @Autowired
    PropertyReader propertyReader;

    @Autowired
    FixtureDataService fixtureDataService;

    @Autowired
    CareDataRepository careDataRepository;

    @Autowired
    MCTSHttpClientService mCTSHttpClientService;

    public CareDataRepository getCareDataRepository() {
        return careDataRepository;
    }

    public void setCareDataRepository(CareDataRepository careDataRepository) {
        this.careDataRepository = careDataRepository;
    }

    public void updateXml(Integer mctsPregnantMotherId)
            throws BeneficiaryException {
        MctsPregnantMother mctsPregnantMother = careDataRepository
                .getMotherFromPrimaryId(mctsPregnantMotherId);
        String caseGuid = mctsPregnantMother.getMctsPersonaCaseUId();
        if (caseGuid != null && !StringUtils.isEmpty(caseGuid)) {
            updateXml(mctsPregnantMother);
        }
    }

    public void updateXml(MctsPregnantMother mctsPregnantMother)
            throws BeneficiaryException {
        UpdateData data = new UpdateData();
        String userId = propertyReader.getUserIdforCommcare();
        Meta meta = createMetaandReturn(userId);
        meta.setTimeEnd(new DateTime().toString());
        data.setMeta(meta);
        int workerId;

        if (mctsPregnantMother.getMctsHealthworkerByAshaId() != null) {
            workerId = mctsPregnantMother.getMctsHealthworkerByAshaId()
                    .getHealthworkerId();
        } else {
            workerId = -1;
        }
        Case caseTask = createCaseForBeneficiary(mctsPregnantMother, userId,
                workerId);
        if ((caseTask.getUpdateTask().getMctsFullname() != null)
                && (caseTask.getUpdateTask().getMctsHusbandName() != null)
                && (caseTask.getUpdateTask().getMctsHusbandName_en() != null)
                && (caseTask.getUpdateTask().getMctsFullname_en() != null)) {
            data.setCaseTask(caseTask);
        }

        data.setXmlns(CommcareConstants.UPDATEDATAXMLNS);
        String returnvalue = ObjectToXMLConverter.converObjectToXml(data,
                UpdateData.class);
        LOGGER.debug("returned : " + returnvalue);
        // POST the xml to the url
        HttpStatus status = mCTSHttpClientService.syncToCommcareUpdate(data);

        // Accepted status code is 2xx
        if (status.value() / 100 == 2) {
            LOGGER.debug("valid response received");
        }

    }

    private Case createCaseForBeneficiary(
            MctsPregnantMother mctsPregnantMother, String userId, int workerId)
            throws BeneficiaryException {

        String ownerId = mctsPregnantMother.getOwnerId();
        String caseId = mctsPregnantMother.getMctsPersonaCaseUId();

        Case caseTask = new Case();
        DateTime date = new DateTime();

        String dateModified = date.toString();

        UpdateTask updatedTask = updateTaskandReturn(mctsPregnantMother,
                workerId, ownerId);
        caseTask.setUpdateTask(updatedTask);
        caseTask.setXmlns(CommcareConstants.XMLNS);
        caseTask.setDateModified(dateModified);
        caseTask.setCaseId(caseId);
        caseTask.setUserId(userId);

        return caseTask;
    }

    /**
     * Mehtod to create Meta Object and return it.
     * 
     * @param userId
     * @return
     */
    private Meta createMetaandReturn(String userId) {

        Meta meta = new Meta();
        meta.setXmlns(CommcareConstants.METAXMLNS);
        meta.setInstanceID(UUID.randomUUID().toString());
        meta.setTimeStart(new DateTime().toString());
        meta.setUserID(userId);

        return meta;
    }

    /**
     * Method to create UpdateTask Object and return it.
     * 
     * @param mctsPregnantMother
     * @param userId
     * @param workerId
     * @return
     * @throws BeneficiaryException
     */
    public UpdateTask updateTaskandReturn(
            MctsPregnantMother mctsPregnantMother, int workerId, String ownerId)
            throws BeneficiaryException {
        UpdateTask updateTask = new UpdateTask();

        String mctsName = mctsPregnantMother.getHindiName();
        String mctsName_en = mctsPregnantMother.getName();
        String husbandName = mctsPregnantMother.getHindiFatherHusbandName();
        String husbandName_en = mctsPregnantMother.getFatherHusbandName();
        String phone = mctsPregnantMother.getMobileNo();
        Date birth = mctsPregnantMother.getBirthDate();
        DateTime birthDate = new DateTime(mctsPregnantMother.getBirthDate());
        DateTime date = new DateTime();
        String age = "";
        String dob = "";
        DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd");

        if (birth != null) {
            dob = fmt.print(birthDate);
            age = Integer.toString(Days.daysBetween(
                    birthDate.withTimeAtStartOfDay(),
                    date.withTimeAtStartOfDay()).getDays() / 365);
        }

        if (mctsPregnantMother.getLmpDate() != null) {
            DateTime lmpDate = new DateTime(mctsPregnantMother.getLmpDate());
            DateTime edd = lmpDate.plusDays(280);
            String eddDate = fmt.print(edd);
            updateTask.setMctsEdd(eddDate);
        }

        updateTask.setCaseName(mctsPregnantMother.getName());
        updateTask.setCaseType(CommcareConstants.CASETYPE);
        updateTask.setOwnerId(ownerId);
        updateTask.setMctsHusbandName(husbandName);
        updateTask.setMctsHusbandName_en(husbandName_en);
        updateTask.setMctsFullname(mctsName);
        updateTask.setMctsFullname_en(mctsName_en);
        updateTask.setMctsAge(age);
        updateTask.setMctsDob(dob);
        updateTask.setMctsPhoneNumber(phone);

        return updateTask;
    }

}
