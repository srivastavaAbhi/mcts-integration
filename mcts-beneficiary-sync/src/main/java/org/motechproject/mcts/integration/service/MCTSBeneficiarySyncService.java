/**
 * Class to send <code>Get</code> Updates request to MCTS, <code>Add</code> the received updates to database and <code>Notify</code> Hub
 */
package org.motechproject.mcts.integration.service;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import org.apache.commons.io.FileUtils;
import org.joda.time.DateTime;
import org.motechproject.mcts.integration.exception.ApplicationErrors;
import org.motechproject.mcts.integration.exception.BeneficiaryException;
import org.motechproject.mcts.integration.hibernate.model.HubTransaction;
import org.motechproject.mcts.integration.hibernate.model.MctsDistrict;
import org.motechproject.mcts.integration.hibernate.model.MctsHealthblock;
import org.motechproject.mcts.integration.hibernate.model.MctsHealthworker;
import org.motechproject.mcts.integration.hibernate.model.MctsPhc;
import org.motechproject.mcts.integration.hibernate.model.MctsPregnantMother;
import org.motechproject.mcts.integration.hibernate.model.MctsPregnantMotherMaster;
import org.motechproject.mcts.integration.hibernate.model.MctsState;
import org.motechproject.mcts.integration.hibernate.model.MctsSubcenter;
import org.motechproject.mcts.integration.hibernate.model.MctsTaluk;
import org.motechproject.mcts.integration.hibernate.model.MctsVillage;
import org.motechproject.mcts.integration.model.Location;
import org.motechproject.mcts.integration.model.LocationDataCSV;
import org.motechproject.mcts.integration.model.NewDataSet;
import org.motechproject.mcts.integration.model.Record;
import org.motechproject.mcts.utils.PropertyReader;
import org.motechproject.transliteration.service.TransliterationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Service
@Transactional
public class MCTSBeneficiarySyncService {
	private final static Logger LOGGER = LoggerFactory
			.getLogger(MCTSHttpClientService.class);
	private static final String DATE_FORMAT = "dd-MM-yyyy";

	@Autowired
	private MCTSHttpClientService mctsHttpClientService;
	@Autowired
	private PropertyReader propertyReader;
	@Autowired
	private Publisher publisher;
	@Autowired
	private CareDataService careDataService;
	@Autowired
	private TransliterationService transliterationService;
	@Autowired
	private LocationDataPopulator locationDataPopulator;

	/**
	 * Main Method to send <code>Get</code> Updates request to MCTS,
	 * <code>Add</code> the received updates to database and <code>Notify</code>
	 * Hub
	 * @param startDate
	 * @param endDate
	 * @throws BeneficiaryException 
	 */
	public void syncBeneficiaryData(DateTime startDate, DateTime endDate) throws BeneficiaryException{
		NewDataSet newDataSet = syncFrom(startDate, endDate);
		if (newDataSet == null) {
			LOGGER.info("No New Updates Received. Exiting");
			return;
		}
		addToDbData(newDataSet); // adds updates received to db
		// writeToFile(beneficiaryData); //Writes the Updates received to a file
		// TODO to be added hubNotification for 0.24
		// notifyHub(); //Notify the hub about the Updates received
	}

	/**
	 * Send the sync request to <code>MCTS</code>
	 * @param startDate
	 * @param endDate
	 * @return String of XML of the updates received from MCTS
	 */
	protected NewDataSet syncFrom(DateTime startDate, DateTime endDate) {
		LOGGER.info("Creating Request Body To Be Sent To MCTS");
		MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
		requestBody.putAll(propertyReader
				.getDefaultBeneficiaryListQueryParams());
		requestBody.add("FromDate", startDate.toString(DATE_FORMAT));
		requestBody.add("ToDate", endDate.toString(DATE_FORMAT));
		return mctsHttpClientService.syncFrom(requestBody);
	}

	/**
	 * Add the updates received from MCTS to database table
	 * <code>mctsPregnantMother</code>
	 * @param newDataSet
	 * @throws BeneficiaryException 
	 */
	private void addToDbData(NewDataSet newDataSet) throws BeneficiaryException{
		LOGGER.info(String.format("Started writing to db for %s records",
				newDataSet.getRecords().size()));
		int count = 0;
		Date startDate = new Date(); // Sets the startDate when data started to
										// add to db to be send to Hub to query
										// db
		for (Record record : newDataSet.getRecords()) {
			MctsPregnantMother mctsPregnantMother = new MctsPregnantMother();
			mctsPregnantMother = mapRecordToMctsPregnantMother(record,
					startDate);
			if (mctsPregnantMother != null) {
				careDataService.saveOrUpdate(mctsPregnantMother);
				count++;
				LOGGER.info(String.format(
						"MctsPregnantMother [%s] added to db",
						mctsPregnantMother.getMctsId()));
			} else
				addToMctsPregnantMotherMaster(record, startDate);
			LOGGER.error("SKIPPED Adding this record to Database");
		}
		Date endDate = new Date(); // Sets the endDate when data ended to be
									// added to db to be send to Hub to query db
		setHubTransactionDates(startDate, endDate);
		LOGGER.info(String.format("Added %s records to db of %s records.",
				count, newDataSet.getRecords().size()));
	}

	/**
	 * Map the <code>Record</code> object received from MCTS to
	 * <code>MctsPregnatMother</code> object to be added to db
	 * @param record
	 * @return MctsPregnantMother
	 * @throws BeneficiaryException 
	 */
	private MctsPregnantMother mapRecordToMctsPregnantMother(Record record,
			Date startDate) throws BeneficiaryException{
		String beneficiaryId = record.getBeneficiaryID();
		MctsPregnantMother mctsPregnantMother = careDataService.findEntityByField(
				MctsPregnantMother.class, "mctsId",
				beneficiaryId);
		LOGGER.info(record.toString());
		if (beneficiaryId != null
				&& beneficiaryId.length() == 18) {// checks that Beneficiary Id should be 18 char long
			if (mctsPregnantMother == null) {// checks if beneficiary already present
															// in db with same mctsId
				mctsPregnantMother = new MctsPregnantMother();
				String gender = record.getGender();
				String beneficiaryName = record.getBeneficiaryName();
				addLocationToDbIfNotPresent(record);
				Location location = getUniqueLocationMap(record);
				mctsPregnantMother.setMctsVillage(location.getMctsVillage());
				mctsPregnantMother
						.setMctsSubcenter(location.getMctsSubcenter());
				mctsPregnantMother
						.setMctsHealthworkerByAshaId(getHealthWorkerId(
								record.getASHAID(), location, "ASHA"));
				mctsPregnantMother
						.setMctsHealthworkerByAnmId(getHealthWorkerId(
								record.getANMID(), location, "ANM"));
				mctsPregnantMother.setBeneficiaryAddress(record
						.getBeneficiaryAddress());
				mctsPregnantMother.setCategory(record.getCategory());
				mctsPregnantMother.setCreationTime(startDate);
				mctsPregnantMother
						.setEconomicStatus(record.getEconomicStatus());
				mctsPregnantMother.setEidNumber(record.getEIDNumber());
				mctsPregnantMother.setEmail(record.getEmail());
				mctsPregnantMother.setFatherHusbandName(record
						.getFatherHusbandName());
				mctsPregnantMother
						.setHindiFatherHusbandName(transliterate(record
								.getFatherHusbandName()));
				if (gender != null
						&& gender.length() > 0) {
					mctsPregnantMother.setGender(gender.charAt(0));
				}
				mctsPregnantMother.setMctsId(beneficiaryId);
				mctsPregnantMother.setMobileNo(record.getMobileno());
				if (beneficiaryName != null
						&& beneficiaryName.length() > 0) {// checks that beneficiary name cannot be null
					mctsPregnantMother.setName(beneficiaryName);
					mctsPregnantMother
							.setHindiName(transliterate(beneficiaryName));
				} else {
					LOGGER.error(String.format(
							"Beneficiary Name Cannot be null for MctsId[%s]",
							beneficiaryId));
					return null;
				}
				mctsPregnantMother.setPincode(record.getPinCode());
				mctsPregnantMother.setTown(record.getTown());
				mctsPregnantMother.setType(record.getBeneficiaryType());
				mctsPregnantMother.setUidNumber(record.getUIDNumber());
				mctsPregnantMother.setWard(record.getWard());
				// Parse the LmpDate to dd-mm-YYYY format and logs an error if  not
				// in correct format
				try {
					mctsPregnantMother.setLmpDate(new SimpleDateFormat(
							"yyyy-MM-dd", Locale.ENGLISH).parse(record
							.getLMPDate()));
					LOGGER.debug("LMP Date is: "
							+ mctsPregnantMother.getLmpDate().toString());
				} catch (ParseException e) {
					LOGGER.error(String
							.format("Invalid LMP Date[%s] for Beneficiary Record: %s. Correct format is dd-mm-yyyy",
									record.getLMPDate(),
									beneficiaryId));
				} catch (NullPointerException e) {
					LOGGER.error(String
							.format("Null value received in LMP Date Field"));
				}
				// Parse the BirthDate to dd-mm-YYYY format and logs an error if not
				// in correct format
				try {
					mctsPregnantMother.setBirthDate(new SimpleDateFormat(
							"yyyy-MM-dd", Locale.ENGLISH).parse(record
							.getBirthdate()));
					LOGGER.debug("Birth Date is: "
							+ mctsPregnantMother.getBirthDate().toString());
				} catch (ParseException e) {
					LOGGER.error(String
							.format("Invalid Birth Date[%s] for Beneficiary Record: %s. Correct format is dd-mm-yyyy",
									record.getBirthdate(),
									beneficiaryId));
				} catch (NullPointerException e) {
					LOGGER.error(String
							.format("Null value received in BirthDate Date Field"));
				}
				return mctsPregnantMother;
			} else {
				LOGGER.error(String
						.format("Beneficiary with [MctsId:%s] already present in Database. Hence skipping adding this record.",
								beneficiaryId));
				return null;
			}
		} else {
			LOGGER.error("Beneficiary Id cannot be Null. Data not added to Db");
			return null;
		}
	}

	/**
	 * This is to save the data as it is received from mcts
	 * maps record to MctsPregnatMotherMaster and add to db
	 * @param record
	 * @param startDate
	 * @throws BeneficiaryException
	 */
	private void addToMctsPregnantMotherMaster(Record record, Date startDate)
			throws BeneficiaryException {
		MctsPregnantMotherMaster mctsPregnantMotherMaster = new MctsPregnantMotherMaster();
		mctsPregnantMotherMaster.setaNMID(record.getANMID());
		mctsPregnantMotherMaster.setaSHAID(record.getASHAID());
		mctsPregnantMotherMaster.setBeneficiaryAddress(record
				.getBeneficiaryAddress());
		mctsPregnantMotherMaster.setBeneficiaryID(record.getBeneficiaryID());
		mctsPregnantMotherMaster
				.setBeneficiaryName(record.getBeneficiaryName());
		mctsPregnantMotherMaster
				.setBeneficiaryType(record.getBeneficiaryType());
		mctsPregnantMotherMaster.setBirthdate(record.getBirthdate());
		mctsPregnantMotherMaster.setBlockID(record.getBlockID());
		mctsPregnantMotherMaster.setBlockName(record.getBlockName());
		mctsPregnantMotherMaster.setCategory(record.getCategory());
		mctsPregnantMotherMaster.setDistrictID(record.getDistrictID());
		mctsPregnantMotherMaster.setDistrictName(record.getDistrictName());
		mctsPregnantMotherMaster.setEconomicStatus(record.getEconomicStatus());
		mctsPregnantMotherMaster.seteIDNumber(record.getEIDNumber());
		mctsPregnantMotherMaster.setEmail(record.getEmail());
		mctsPregnantMotherMaster.setFacilityID(record.getFacilityID());
		mctsPregnantMotherMaster.setFacilityName(record.getFacilityName());
		mctsPregnantMotherMaster.setFatherHusbandName(record
				.getFatherHusbandName());
		mctsPregnantMotherMaster.setGender(record.getGender());
		mctsPregnantMotherMaster.setlMPDate(record.getLMPDate());
		mctsPregnantMotherMaster.setlMPDate(record.getMobileno());
		mctsPregnantMotherMaster.setPinCode(record.getPinCode());
		mctsPregnantMotherMaster.setStateID(record.getStateID());
		mctsPregnantMotherMaster.setStateName(record.getStateName());
		mctsPregnantMotherMaster.setSubCentreID(record.getSubCentreID());
		mctsPregnantMotherMaster.setSubCentreName(record.getSubCentreName());
		mctsPregnantMotherMaster.setTehsilID(record.getTehsilID());
		mctsPregnantMotherMaster.setTehsilName(record.getTehsilName());
		mctsPregnantMotherMaster.setTown(record.getTown());
		mctsPregnantMotherMaster.setuIDNumber(record.getUIDNumber());
		mctsPregnantMotherMaster.setVillageID(record.getVillageID());
		mctsPregnantMotherMaster.setVillageName(record.getVillageName());
		mctsPregnantMotherMaster.setWard(record.getWard());
		mctsPregnantMotherMaster.setCreationTime(startDate);
		careDataService.saveOrUpdate(mctsPregnantMotherMaster);
	}

	/**
	 * map record to <code>LocationCSV</code> and adds to db if not present
	 * @param record
	 * @throws BeneficiaryException
	 */
	private void addLocationToDbIfNotPresent(Record record)
			throws BeneficiaryException {
		LocationDataCSV locationDataCSV = new LocationDataCSV();
		locationDataCSV.setSID(record.getStateID());
		locationDataCSV.setState(record.getStateName());
		locationDataCSV.setDCode(record.getDistrictID());
		locationDataCSV.setDistrict(record.getDistrictName());
		locationDataCSV.setVCode(record.getVillageID());
		locationDataCSV.setVillage(record.getVillageName());
		locationDataCSV.setSUBCenter(record.getSubCentreName());
		locationDataCSV.setSID(record.getSubCentreID());
		locationDataCSV.setBID(record.getBlockID());
		locationDataCSV.setBlock(record.getBlockName());
		locationDataCSV.setTCode(record.getTehsilID());
		locationDataCSV.setTaluka_Name(record.getTehsilName());
		locationDataCSV.setPHC(record.getFacilityName());
		locationDataCSV.setPID(record.getFacilityID());
		locationDataPopulator.addLocationToDb(locationDataCSV);
	}

	/**
	 * Maps MctsHealthworkerId to MotechHealthworkerId Creates new HealthWorker
	 * if the healthworker by Id passed doesnot exist and return its Id
	 * @param mctsHealthWorkerId
	 * @return
	 * @throws BeneficiaryException 
	 */
	private MctsHealthworker getHealthWorkerId(String mctsHealthWorkerId,
			Location location, String type) throws BeneficiaryException{
		int id = validateAndReturnAsInt("mctsHealthWorkerId",
				mctsHealthWorkerId);
		MctsHealthworker mctsHealthworker = careDataService.findEntityByField(
				MctsHealthworker.class, "healthworkerId", id);
		// Checks if HealthWorker exist in db...if not then logs an error
		// message and creates a new healthworker and return it
		if (mctsHealthworker == null) {
			LOGGER.error(String
					.format("HealthWorker with HealthworkerId: %s doesNot exist in DataBase. Adding new record in HealthWorker Table",
							mctsHealthWorkerId));
			mctsHealthworker = new MctsHealthworker();
			mctsHealthworker.setGfAddress(mctsHealthWorkerId);
			mctsHealthworker.setMctsPhc(location.getMctsPhc());
			mctsHealthworker.setMctsSubcenter(location.getMctsSubcenter());
			mctsHealthworker.setMctsVillage(location.getMctsVillage());
			mctsHealthworker.setType(type);
			careDataService.saveOrUpdate(mctsHealthworker);
			return careDataService.findEntityByField(MctsHealthworker.class,
					"healthworkerId", id);
		} else {
			return mctsHealthworker;
		}
	}

	/**
	 * Validate if the string is int or not && return by converting it to int
	 * @param field
	 * @param id
	 * @return
	 * @throws BeneficiaryException
	 */
	private int validateAndReturnAsInt(String field, String id) throws BeneficiaryException{
		if(id.isEmpty() || id == null || id.matches("//d+")){
			return Integer.parseInt(id);
		}
		else throw new BeneficiaryException(ApplicationErrors.INVALID_ARGUMENT, String.format("Value received for [%s : %s] is invalid", field, id));
	}

	/**
	 * Maps record to a unique location and returns the <code>Location</code>
	 * @param record
	 * @return
	 * @throws BeneficiaryException
	 */
	private Location getUniqueLocationMap(Record record)
			throws BeneficiaryException {
		Location location = new Location();
		location.setMctsState(careDataService.findEntityByField(
				MctsState.class, "stateId", record.getStateID()));
		try {
			// sets District
			HashMap<String, Object> params = new HashMap<String, Object>();
			params.put("mctsState", location.getMctsState().getId());
			params.put("disctrictId", validateAndReturnAsInt("disctrictId", record.getDistrictID()));
			location.setMctsDistrict(careDataService
					.findListOfEntitiesByMultipleField(MctsDistrict.class,
							params).get(0));
			// sets Taluka
			params = new HashMap<String, Object>();
			params.put("mctsDistrict", location.getMctsDistrict());
			params.put("talukId", validateAndReturnAsInt("talukId", record.getTehsilID()));
			location.setMctsTaluk(careDataService
					.findListOfEntitiesByMultipleField(MctsTaluk.class, params)
					.get(0));
			// sets Village
			params = new HashMap<String, Object>();
			params.put("mctsTaluk", location.getMctsTaluk());
			params.put("villageId", validateAndReturnAsInt("villageId", record.getVillageID()));
			location.setMctsVillage(careDataService
					.findListOfEntitiesByMultipleField(MctsVillage.class,
							params).get(0));
			// sets HealthBlock
			params = new HashMap<String, Object>();
			params.put("mctsTaluk", location.getMctsTaluk());
			params.put("healthblockId", validateAndReturnAsInt("healthblockId", record.getBlockID()));
			location.setMctsHealthblock(careDataService
					.findListOfEntitiesByMultipleField(MctsHealthblock.class,
							params).get(0));
			// sets Phc
			params = new HashMap<String, Object>();
			params.put("mctsHealthblock", location.getMctsHealthblock());
			params.put("phcId", validateAndReturnAsInt("phcId", record.getFacilityID()));
			location.setMctsPhc(careDataService
					.findListOfEntitiesByMultipleField(MctsPhc.class, params)
					.get(0));
			// sets SubCenter
			params = new HashMap<String, Object>();
			params.put("mctsPhc", location.getMctsPhc());
			params.put("subcenterId", validateAndReturnAsInt("subcenterId", record.getSubCentreID()));
			location.setMctsSubcenter(careDataService
					.findListOfEntitiesByMultipleField(MctsSubcenter.class,
							params).get(0));
		} catch (NumberFormatException e) {
			LOGGER.error(String.format("Invalid Location Code Received"), e);
			// TODO throw proper Exception
		}
		return location;
	}

	/**
	 * 
	 * @param mctsPregnantMother
	 * @return
	 * @throws BeneficiaryException
	 */
	public String transliterate(String word) throws BeneficiaryException {
		LOGGER.info("Creating new transliteration");
		String hindi = transliterationService.transliterate(word);
		LOGGER.debug(String.format("Tranlation for [%s] received is [%s]",
				word, hindi));
		return hindi;
	}

	/**
	 * Write the Beneficiary Data to a new XML file with timeStamp
	 * @param beneficiaryData
	 * @throws BeneficiaryException 
	 */
	@Deprecated
	protected void writeToFile(String beneficiaryData) throws BeneficiaryException{
		String outputFileLocation = String.format("%s_%s.xml", propertyReader
				.getSyncRequestOutputFileLocation(),
				DateTime.now().toString("yyyy-MM-dd") + "T"
						+ DateTime.now().toString("HH:mm"));
		try {
			FileUtils.writeStringToFile(new File(outputFileLocation),
					beneficiaryData);
			LOGGER.info(String.format(
					"MCTS beneficiary details response is added to file %s",
					outputFileLocation));
		} catch (IOException e) {
			String error = String.format(
					"Cannot write MCTS beneficiary details response to file: %s. Reason %s",
					outputFileLocation, e.getMessage());
			LOGGER.error(error);
			throw new BeneficiaryException(ApplicationErrors.FILE_READING_WRTING_FAILED, error);
		}
	}

	/**
	 * Sets startDate and endDate to be sent to hub
	 * @throws BeneficiaryException
	 */
	protected void setHubTransactionDates(Date startDate, Date endDate)
			throws BeneficiaryException {
		HubTransaction hubTransaction = new HubTransaction();
		hubTransaction.setStartDate(startDate);
		hubTransaction.setEndDate(endDate);
		hubTransaction.setIsNotified(false);
		careDataService.saveOrUpdate(hubTransaction);
	}

	/**
	 * Notifies the Hub when the Updates received from Mcts with Url to call
	 * Back
	 * @throws BeneficiaryException
	 */
	protected void notifyHub() throws BeneficiaryException{
		String updateUrl = propertyReader.getBenificiaryUpdateTopicUrlForHub();
		LOGGER.info("Sending Notification to Hub about Updates at Topic url"
				+ updateUrl);
		publisher.publish(updateUrl);
	}
}
