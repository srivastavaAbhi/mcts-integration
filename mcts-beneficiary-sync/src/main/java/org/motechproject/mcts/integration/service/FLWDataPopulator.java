package org.motechproject.mcts.integration.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

import org.hibernate.HibernateException;
import org.motechproject.mcts.integration.exception.ApplicationErrors;
import org.motechproject.mcts.integration.exception.BeneficiaryException;
import org.motechproject.mcts.integration.hibernate.model.MctsHealthworkerErrorLog;
import org.motechproject.mcts.integration.hibernate.model.MctsHealthworker;
import org.motechproject.mcts.integration.hibernate.model.MctsPhc;
import org.motechproject.mcts.integration.hibernate.model.MctsSubcenter;
import org.motechproject.mcts.integration.hibernate.model.MctsVillage;
import org.motechproject.mcts.integration.model.FLWDataCSV;
import org.motechproject.mcts.integration.repository.CareDataRepository;
import org.motechproject.mcts.utils.FlwValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.supercsv.exception.SuperCsvReflectionException;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.prefs.CsvPreference;

/**
 * Service method to read flw CSV file and populate the database
 * 
 * @author aman
 * 
 */
@Transactional
@Service
// @TransactionConfiguration(transactionManager = "txManager", defaultRollback =
// true)
public class FLWDataPopulator {

	private final static Logger LOGGER = LoggerFactory
			.getLogger(MCTSHttpClientService.class);

	public CareDataRepository getCareDataRepository() {
		return careDataRepository;
	}

	public void setCareDataRepository(CareDataRepository careDataRepository) {
		this.careDataRepository = careDataRepository;
	}

	@Autowired
	private CareDataRepository careDataRepository;

	/**
	 * Method to populate table mcts_HealthWorker
	 * 
	 * @throws Exception
	 */
	public void populateFLWData(MultipartFile file) throws BeneficiaryException {
		ICsvBeanReader beanReader = null;
		FLWDataCSV flwDataCSV = new FLWDataCSV();
		File newFile = null;
		try {
			byte[] bytes = file.getBytes();
			String path = System.getProperty("java.io.tmpdir");
			newFile = new File(path + "/flw.xml");
			FileOutputStream out = new FileOutputStream(newFile);
			out.write(bytes);
			System.out.println("size" + newFile.getTotalSpace());
			beanReader = new CsvBeanReader(new FileReader(newFile),
					CsvPreference.STANDARD_PREFERENCE);
			final String[] header = beanReader.getHeader(true);

			while ((flwDataCSV = beanReader.read(FLWDataCSV.class, header)) != null) {
				if (FlwValidator.isValidateFlw(flwDataCSV) == true) {
					addFLWToDb(flwDataCSV);
				}
				else {
					flwDataPopulator(flwDataCSV);
				}
			}

		} catch (FileNotFoundException e) {
			throw new BeneficiaryException(ApplicationErrors.FILE_NOT_FOUND, e);
		} catch (IOException e) {
			throw new BeneficiaryException(
					ApplicationErrors.FILE_READING_WRTING_FAILED, e);
		} catch (SuperCsvReflectionException e) {
			throw new BeneficiaryException(
					ApplicationErrors.CSV_FILE_DOES_NOT_MATCH_WITH_HEADERS, e);
		} catch (IllegalArgumentException e) {
			throw new BeneficiaryException(
					ApplicationErrors.NUMBER_OF_ARGUMENTS_DOES_NOT_MATCH, e);
		} catch (HibernateException e) {
			throw new BeneficiaryException(
					ApplicationErrors.DATABASE_OPERATION_FAILED, e);
		} finally {
			if (beanReader != null) {
				try {
					beanReader.close();
				} catch (IOException e) {
					throw new BeneficiaryException(
							ApplicationErrors.FILE_CLOSING_FAILED,
							e.getMessage());
				}
			}
			
		}

	}

	public void addFLWToDb(FLWDataCSV flwDataCSV) throws BeneficiaryException {
		int phcId = flwDataCSV.getPHC_ID();
		MctsPhc mctsPhc = careDataRepository.getMctsPhc(phcId);
		if (mctsPhc != null) {
			int healthworkerId = flwDataCSV.getId();
			String name = flwDataCSV.getName();
			String contact_No = flwDataCSV.getContact_No();
			char sex = flwDataCSV.getSex().charAt(0);
			String type = flwDataCSV.getType();
			int subcentreId = flwDataCSV.getSubCentre_ID();
			int villageId = flwDataCSV.getVillage_ID();
			String husbandName = flwDataCSV.getHusband_Name();
			String aadharNo = flwDataCSV.getAadhar_No();
			String gfAddress = flwDataCSV.getGF_Address();

			MctsSubcenter mctsSubcenre = careDataRepository
					.getMctsSubcentre(subcentreId);
			MctsVillage mctsVillage = careDataRepository
					.getMctsVillage(villageId);

			MctsHealthworker mctsHealthworker = careDataRepository
					.findEntityByField(MctsHealthworker.class,
							"healthworkerId", healthworkerId);
			if (mctsHealthworker == null) {
				mctsHealthworker = new MctsHealthworker();
			}
			mctsHealthworker.setMctsPhc(mctsPhc);
			mctsHealthworker.setHealthworkerId(healthworkerId);
			mctsHealthworker.setName(name);
			mctsHealthworker.setSex(sex);
			mctsHealthworker.setType(type);
			mctsHealthworker.setContactNo(contact_No);
			mctsHealthworker.setHusbandName(husbandName);
			mctsHealthworker.setAadharNo(aadharNo);
			mctsHealthworker.setGfAddress(gfAddress);
			mctsHealthworker.setStatus(true);
			if (mctsSubcenre != null) {
				mctsHealthworker.setMctsSubcenter(mctsSubcenre);
			}
			if (mctsVillage != null) {
				mctsHealthworker.setMctsVillage(mctsVillage);
			}
			careDataRepository.saveOrUpdate(mctsHealthworker);

		}

		else {
			LOGGER.error("invalid phc id in row");
		}

	}

	/**
	 * Method to populate table mcts_flw_error
	 * @param flwDataCSV
	 * @throws BeneficiaryException
	 */
	public void flwDataPopulator(FLWDataCSV flwDataCSV) throws BeneficiaryException {
		String districtId = flwDataCSV.getDistrict_ID().toString();
		String talukaId = flwDataCSV.getTaluka_ID().toString();
		String healthBlockId = flwDataCSV.getHealthBlock_ID()
				.toString();
		String subCentreId = flwDataCSV.getSubCentre_ID().toString();
		String villageId = flwDataCSV.getVillage_ID().toString();
		String name = flwDataCSV.getName();
		String sex = flwDataCSV.getSex();
		String type = flwDataCSV.getType();
		String aadharNo = flwDataCSV.getAadhar_No();
		String husbandName = flwDataCSV.getHusband_Name();
		String gfAdress = flwDataCSV.getGF_Address();
		String healthWorkerId = flwDataCSV.getId().toString();
		String contact_No = flwDataCSV.getContact_No();
		String phcId = flwDataCSV.getPHC_ID().toString();

		String status = "1";
		String comments = " ";
		MctsPhc mctsPhc = careDataRepository.getMctsPhc(flwDataCSV
				.getPHC_ID());
		if (mctsPhc == null) {
			status = "0";
			comments = "Invalid phc id";
		}
		MctsFlwData mctsFlwData = new MctsFlwData(districtId, talukaId,
				healthBlockId, subCentreId, villageId, name, sex, type,
				aadharNo, husbandName, gfAdress, healthWorkerId,
				contact_No, phcId, status, comments);
		careDataRepository.saveOrUpdate(mctsFlwData);
	}
	
	
/*	*//**
	 * Method to populate table mcts_flw_master
	 * 
	 * @throws Exception
	 *//*
	public void flwDataPopulator(File file) throws BeneficiaryException {
		ICsvBeanReader beanReader = null;
		FLWDataCSV flwDataCSV = new FLWDataCSV();
		try {
			beanReader = new CsvBeanReader(new FileReader(file),
					CsvPreference.STANDARD_PREFERENCE);
			final String[] header = beanReader.getHeader(true);

			while ((flwDataCSV = beanReader.read(FLWDataCSV.class, header)) != null) {

				String districtId = flwDataCSV.getDistrict_ID().toString();
				String talukaId = flwDataCSV.getTaluka_ID().toString();
				String healthBlockId = flwDataCSV.getHealthBlock_ID()
						.toString();
				String subCentreId = flwDataCSV.getSubCentre_ID().toString();
				String villageId = flwDataCSV.getVillage_ID().toString();
				String name = flwDataCSV.getName();
				String sex = flwDataCSV.getSex();
				String type = flwDataCSV.getType();
				String aadharNo = flwDataCSV.getAadhar_No();
				String husbandName = flwDataCSV.getHusband_Name();
				String gfAdress = flwDataCSV.getGF_Address();
				String healthWorkerId = flwDataCSV.getId().toString();
				String contact_No = flwDataCSV.getContact_No();
				String phcId = flwDataCSV.getPHC_ID().toString();

				String status = "1";
				String comments = " ";
				MctsPhc mctsPhc = careDataRepository.getMctsPhc(flwDataCSV
						.getPHC_ID());
				if (mctsPhc == null) {
					status = "0";
					comments = "Invalid phc id";
				}
				MctsHealthworkerErrorLog mctsFlwData = new MctsHealthworkerErrorLog(districtId, talukaId,
						healthBlockId, subCentreId, villageId, name, sex, type,
						aadharNo, husbandName, gfAdress, healthWorkerId,
						contact_No, phcId, status, comments);
				careDataRepository.saveOrUpdate(mctsFlwData);

			}

		} catch (FileNotFoundException e) {
			throw new BeneficiaryException(ApplicationErrors.FILE_NOT_FOUND,
					e.getMessage());
		} catch (IOException e) {
			throw new BeneficiaryException(
					ApplicationErrors.FILE_READING_WRTING_FAILED,
					e.getMessage());
		} catch (SuperCsvReflectionException e) {
			throw new BeneficiaryException(
					ApplicationErrors.CSV_FILE_DOES_NOT_MATCH_WITH_HEADERS,
					e.getMessage());
		} catch (IllegalArgumentException e) {
			throw new BeneficiaryException(
					ApplicationErrors.NUMBER_OF_ARGUMENTS_DOES_NOT_MATCH,
					e.getMessage());
		} catch (HibernateException e) {
			throw new BeneficiaryException(
					ApplicationErrors.DATABASE_OPERATION_FAILED, e.getMessage());
		}

		finally {
			if (beanReader != null) {
				try {
					beanReader.close();
				} catch (IOException e) {
					throw new BeneficiaryException(
							ApplicationErrors.FILE_CLOSING_FAILED,
							e.getMessage());
				}
			}
		}

	}*/
}
