package org.motechproject.mcts.integration.hibernate.model;

// Generated May 26, 2014 2:25:24 PM by Hibernate Tools 3.4.0.CR1

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.SEQUENCE;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * MctsPregnantMother generated by hbm2java
 */
@Entity
@Table(name = "mcts_pregnant_mother", schema = "report")
public class MctsPregnantMother implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2473057427181282155L;
	private Integer id;
	private String mctsPersonaCaseUId;
	private MctsSubcenter mctsSubcenter;
	private MctsHealthworker mctsHealthworkerByAshaId;
	private MctsPregnantMotherMatchStatus mctsPregnantMotherMatchStatus;
	private MctsHealthworker mctsHealthworkerByAnmId;
	private MotherCase motherCase;
	private MctsVillage mctsVillage;
	private MotherCaseMctsAuthorizedStatus motherCaseMctsAuthorizedStatus;
	private String mctsId;
	private String name;
	private String hindiName;
	private String type;
	private Date birthDate;
	private Character gender;
	private String fatherHusbandName;
	private String hindiFatherHusbandName;
	private String email;
	private String mobileNo;
	private String economicStatus;
	private String category;
	private String beneficiaryAddress;
	private String uidNumber;
	private String pincode;
	private Date lmpDate;
	private String eidNumber;
	private String ward;
	private String town;
	private Date creationTime;
	private String hhNumber;
	private String familyNumber;
	

	public MctsPregnantMother() {
	}

	public MctsPregnantMother(MctsSubcenter mctsSubcenter,
			MctsHealthworker mctsHealthworkerByAshaId,
			MctsPregnantMotherMatchStatus mctsPregnantMotherMatchStatus,
			MctsHealthworker mctsHealthworkerByAnmId, MotherCase motherCase,
			MctsVillage mctsVillage,
			MotherCaseMctsAuthorizedStatus motherCaseMctsAuthorizedStatus,
			String mctsId, String name, String hindiName, String type, Date birthDate,
			Character gender, String fatherHusbandName, String hindiFatherHusbandName, String email,
			String mobileNo, String economicStatus, String category,
			String beneficiaryAddress, String uidNumber, String pincode,
			Date lmpDate, String eidNumber, String ward, String town,
			Date creationTime, String familyNumber, String hhNumber) {
		this.mctsSubcenter = mctsSubcenter;
		this.mctsHealthworkerByAshaId = mctsHealthworkerByAshaId;
		this.mctsPregnantMotherMatchStatus = mctsPregnantMotherMatchStatus;
		this.mctsHealthworkerByAnmId = mctsHealthworkerByAnmId;
		this.motherCase = motherCase;
		this.mctsVillage = mctsVillage;
		this.motherCaseMctsAuthorizedStatus = motherCaseMctsAuthorizedStatus;
		this.mctsId = mctsId;
		this.name = name;
		this.hindiName = hindiName;
		this.type = type;
		this.birthDate = birthDate;
		this.gender = gender;
		this.fatherHusbandName = fatherHusbandName;
		this.hindiFatherHusbandName = hindiFatherHusbandName;
		this.email = email;
		this.mobileNo = mobileNo;
		this.economicStatus = economicStatus;
		this.category = category;
		this.beneficiaryAddress = beneficiaryAddress;
		this.uidNumber = uidNumber;
		this.pincode = pincode;
		this.lmpDate = lmpDate;
		this.eidNumber = eidNumber;
		this.ward = ward;
		this.town = town;
		this.creationTime = creationTime;
		this.familyNumber = familyNumber;
		this.hhNumber = hhNumber;
	}

	@SequenceGenerator(name = "generator", sequenceName = "mcts_pregnant_mother_id_seq")
	@Id
	@GeneratedValue(strategy = SEQUENCE, generator = "generator")
	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	@Column(name = "mcts_persona_case_uid", unique = true)
	public String getMctsPersonaCaseUId() {
		return mctsPersonaCaseUId;
	}

	public void setMctsPersonaCaseUId(String mctsPersonaCaseUId) {
		this.mctsPersonaCaseUId = mctsPersonaCaseUId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "subcenter_id")
	public MctsSubcenter getMctsSubcenter() {
		return this.mctsSubcenter;
	}

	public void setMctsSubcenter(MctsSubcenter mctsSubcenter) {
		this.mctsSubcenter = mctsSubcenter;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "asha_id")
	public MctsHealthworker getMctsHealthworkerByAshaId() {
		return this.mctsHealthworkerByAshaId;
	}

	public void setMctsHealthworkerByAshaId(
			MctsHealthworker mctsHealthworkerByAshaId) {
		this.mctsHealthworkerByAshaId = mctsHealthworkerByAshaId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "match_status")
	public MctsPregnantMotherMatchStatus getMctsPregnantMotherMatchStatus() {
		return this.mctsPregnantMotherMatchStatus;
	}

	public void setMctsPregnantMotherMatchStatus(
			MctsPregnantMotherMatchStatus mctsPregnantMotherMatchStatus) {
		this.mctsPregnantMotherMatchStatus = mctsPregnantMotherMatchStatus;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "anm_id")
	public MctsHealthworker getMctsHealthworkerByAnmId() {
		return this.mctsHealthworkerByAnmId;
	}

	public void setMctsHealthworkerByAnmId(
			MctsHealthworker mctsHealthworkerByAnmId) {
		this.mctsHealthworkerByAnmId = mctsHealthworkerByAnmId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "mother_case_id")
	public MotherCase getMotherCase() {
		return this.motherCase;
	}

	public void setMotherCase(MotherCase motherCase) {
		this.motherCase = motherCase;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "village_id")
	public MctsVillage getMctsVillage() {
		return this.mctsVillage;
	}

	public void setMctsVillage(MctsVillage mctsVillage) {
		this.mctsVillage = mctsVillage;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "case_authorized_status")
	public MotherCaseMctsAuthorizedStatus getMotherCaseMctsAuthorizedStatus() {
		return this.motherCaseMctsAuthorizedStatus;
	}

	public void setMotherCaseMctsAuthorizedStatus(
			MotherCaseMctsAuthorizedStatus motherCaseMctsAuthorizedStatus) {
		this.motherCaseMctsAuthorizedStatus = motherCaseMctsAuthorizedStatus;
	}

	@Column(name = "mcts_id")
	public String getMctsId() {
		return this.mctsId;
	}

	public void setMctsId(String mctsId) {
		this.mctsId = mctsId;
	}

	@Column(name = "name")
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "hindi_name")
	public String getHindiName() {
		return this.hindiName;
	}

	public void setHindiName(String hindiName) {
		this.hindiName = hindiName;
	}

	@Column(name = "type", length = 10)
	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "birth_date", length = 13)
	public Date getBirthDate() {
		return this.birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	@Column(name = "gender", length = 1)
	public Character getGender() {
		return this.gender;
	}

	public void setGender(Character gender) {
		this.gender = gender;
	}

	@Column(name = "father_husband_name")
	public String getFatherHusbandName() {
		return this.fatherHusbandName;
	}

	public void setFatherHusbandName(String fatherHusbandName) {
		this.fatherHusbandName = fatherHusbandName;
	}

	@Column(name = "hindi_father_husband_name")
	public String getHindiFatherHusbandName() {
		return this.hindiFatherHusbandName;
	}

	public void setHindiFatherHusbandName(String hindiFatherHusbandName) {
		this.hindiFatherHusbandName = hindiFatherHusbandName;
	}

	@Column(name = "email")
	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(name = "mobile_no", length = 20)
	public String getMobileNo() {
		return this.mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	@Column(name = "economic_status")
	public String getEconomicStatus() {
		return this.economicStatus;
	}

	public void setEconomicStatus(String economicStatus) {
		this.economicStatus = economicStatus;
	}

	@Column(name = "category")
	public String getCategory() {
		return this.category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	@Column(name = "beneficiary_address")
	public String getBeneficiaryAddress() {
		return this.beneficiaryAddress;
	}

	public void setBeneficiaryAddress(String beneficiaryAddress) {
		this.beneficiaryAddress = beneficiaryAddress;
	}

	@Column(name = "uid_number")
	public String getUidNumber() {
		return this.uidNumber;
	}

	public void setUidNumber(String uidNumber) {
		this.uidNumber = uidNumber;
	}

	@Column(name = "pincode", length = 6)
	public String getPincode() {
		return this.pincode;
	}

	public void setPincode(String pincode) {
		this.pincode = pincode;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "lmp_date", length = 13)
	public Date getLmpDate() {
		return this.lmpDate;
	}

	public void setLmpDate(Date lmpDate) {
		this.lmpDate = lmpDate;
	}

	@Column(name = "eid_number")
	public String getEidNumber() {
		return this.eidNumber;
	}

	public void setEidNumber(String eidNumber) {
		this.eidNumber = eidNumber;
	}

	@Column(name = "ward")
	public String getWard() {
		return this.ward;
	}

	public void setWard(String ward) {
		this.ward = ward;
	}

	@Column(name = "town")
	public String getTown() {
		return this.town;
	}

	public void setTown(String town) {
		this.town = town;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "creation_time", length = 29)
	public Date getCreationTime() {
		return this.creationTime;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	@Column(name = "hh_number")
	public String getHhNumber() {
		return this.hhNumber;
	}

	public void setHhNumber(String hhNumber) {
		this.hhNumber = hhNumber;
	}

	@Column(name = "family_number")
	public String getFamilyNumber() {
		return this.familyNumber;
	}

	public void setFamilyNumber(String familyNumber) {
		this.familyNumber = familyNumber;
	}

}