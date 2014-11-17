package org.motechproject.mcts.integration.commcare;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.motechproject.mcts.care.common.mds.model.MctsHealthworker;
import org.motechproject.mcts.care.common.mds.model.MctsPregnantMother;
import org.motechproject.mcts.integration.exception.BeneficiaryException;
import org.motechproject.mcts.integration.repository.MctsRepository;
import org.motechproject.mcts.integration.service.FixtureDataService;
import org.motechproject.mcts.integration.service.MCTSHttpClientService;
import org.motechproject.mcts.utils.DateValidator;
import org.motechproject.mcts.utils.PropertyReader;
import org.springframework.http.HttpStatus;

@RunWith(MockitoJUnitRunner.class)
public class CreateXmlServiceTest {

    @InjectMocks
    private CreateCaseXmlService createCaseXmlService = new CreateCaseXmlService();

    @Mock
    MctsRepository careDataRepository;

    @Mock
    PropertyReader propertyReader;

    @Mock
    MCTSHttpClientService mCTSHttpClientService;

    @Mock
    FixtureDataService fixtureDataService;

    List<MctsPregnantMother> motherList;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        motherList = new ArrayList<MctsPregnantMother>();

        MctsPregnantMother mother1 = new MctsPregnantMother();
        mother1.setName("soniya devi");
        mother1.setFatherHusbandName("Dharmandra Sada");
        mother1.setHindiName("soniya devi");
        mother1.setHindiFatherHusbandName("Dharmandra Sada");
        MctsHealthworker healthWorker = new MctsHealthworker();
        healthWorker.setHealthworkerId(21);
        mother1.setMctsHealthworkerByAshaId(healthWorker);
        mother1.setBirthDate(DateValidator.checkDateInFormat("12-11-1987",
                "dd-MM-yyyy"));
        mother1.setLmpDate(DateValidator.checkDateInFormat("10-03-2011",
                "dd-MM-yyyy"));

        MctsPregnantMother mother2 = new MctsPregnantMother();
        mother2.setName("Ranju Devi");
        mother2.setFatherHusbandName("Dilkush Kamat");
        mother2.setHindiName("Ranju Devi");
        mother2.setHindiFatherHusbandName("Dilkush Kamat");

        motherList.add(mother1);
        motherList.add(mother2);

        HttpStatus status = HttpStatus.ACCEPTED;

        Mockito.when(careDataRepository.getMctsPregnantMother()).thenReturn(
                motherList);
        Mockito.when(propertyReader.sizeOfXml()).thenReturn(50);
        Mockito.when(mCTSHttpClientService.syncToCommcare((Data) any()))
                .thenReturn(status);
        Mockito.when(propertyReader.getUserIdforCommcare()).thenReturn("1234");
        Mockito.when(
                fixtureDataService.getCaseGroupIdfromAshaId(anyInt(),
                        anyString())).thenReturn("6efbnkfb");
        Mockito.when(careDataRepository.getMotherFromPrimaryId(anyInt()))
                .thenReturn(mother1).thenReturn(mother2);
        Mockito.when(careDataRepository.getDetachedFieldId(anyObject()))
                .thenReturn(1);

    }

    @Test
    public void shouldCreateCaseXml() throws BeneficiaryException {
        createCaseXmlService.createCaseXml();
        verify(careDataRepository).getMctsPregnantMother();
        verify(propertyReader).sizeOfXml();
        verify(propertyReader).getUserIdforCommcare();
        verify(careDataRepository, times(2)).getDetachedFieldId(anyObject());
        verify(mCTSHttpClientService).syncToCommcare((Data) any());
        verify(careDataRepository, times(2)).saveOrUpdate(
                (MctsPregnantMother) any());
    }

    @Test
    public void shouldCreateCaseXml_multiple_times()
            throws BeneficiaryException {
        Mockito.when(propertyReader.sizeOfXml()).thenReturn(2);
        createCaseXmlService.createCaseXml();
        verify(careDataRepository).getMctsPregnantMother();
        verify(propertyReader).sizeOfXml();
        verify(careDataRepository, times(2)).getDetachedFieldId(anyObject());
        verify(careDataRepository, times(2)).saveOrUpdate(
                (MctsPregnantMother) any());
    }
}
