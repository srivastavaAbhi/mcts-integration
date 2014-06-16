package org.motechproject.mcts.integration.hibernate.model;

// Generated May 19, 2014 7:55:47 PM by Hibernate Tools 3.4.0.CR1

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

/**
 * MiForm generated by hbm2java
 */
@Entity
@Table(name = "mi_form", schema = "report", uniqueConstraints = @UniqueConstraint(columnNames = "instance_id"))
public class MiForm implements java.io.Serializable {

	private int id;
	private Flw flw;
	private MotherCase motherCase;
	private String instanceId;
	private Date timeEnd;
	private Date timeStart;
	private Date dateModified;
	private Date serverDateModified;
	private String appVersion;
	private Date dateArrived;
	private Date dateLearned;
	private Date dateOfDelivery;
	private String name;
	private String pregStatus;
	private String referralInfo;
	private String abortionType;
	private Date dateAborted;
	private String migratedStatus;
	private Integer deliveryOffsetDays;
	private String status;
	private Date creationTime;
	private Date dateDelFu;

	public MiForm() {
	}

	public MiForm(int id) {
		this.id = id;
	}

	public MiForm(int id, Flw flw, MotherCase motherCase, String instanceId,
			Date timeEnd, Date timeStart, Date dateModified,
			Date serverDateModified, String appVersion, Date dateArrived,
			Date dateLearned, Date dateOfDelivery, String name,
			String pregStatus, String referralInfo, String abortionType,
			Date dateAborted, String migratedStatus,
			Integer deliveryOffsetDays, String status, Date creationTime,
			Date dateDelFu) {
		this.id = id;
		this.flw = flw;
		this.motherCase = motherCase;
		this.instanceId = instanceId;
		this.timeEnd = timeEnd;
		this.timeStart = timeStart;
		this.dateModified = dateModified;
		this.serverDateModified = serverDateModified;
		this.appVersion = appVersion;
		this.dateArrived = dateArrived;
		this.dateLearned = dateLearned;
		this.dateOfDelivery = dateOfDelivery;
		this.name = name;
		this.pregStatus = pregStatus;
		this.referralInfo = referralInfo;
		this.abortionType = abortionType;
		this.dateAborted = dateAborted;
		this.migratedStatus = migratedStatus;
		this.deliveryOffsetDays = deliveryOffsetDays;
		this.status = status;
		this.creationTime = creationTime;
		this.dateDelFu = dateDelFu;
	}

	@Id
	@Column(name = "id", unique = true, nullable = false)
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	public Flw getFlw() {
		return this.flw;
	}

	public void setFlw(Flw flw) {
		this.flw = flw;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "case_id")
	public MotherCase getMotherCase() {
		return this.motherCase;
	}

	public void setMotherCase(MotherCase motherCase) {
		this.motherCase = motherCase;
	}

	@Column(name = "instance_id", unique = true, length = 50)
	public String getInstanceId() {
		return this.instanceId;
	}

	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "time_end", length = 35)
	public Date getTimeEnd() {
		return this.timeEnd;
	}

	public void setTimeEnd(Date timeEnd) {
		this.timeEnd = timeEnd;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "time_start", length = 35)
	public Date getTimeStart() {
		return this.timeStart;
	}

	public void setTimeStart(Date timeStart) {
		this.timeStart = timeStart;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "date_modified", length = 35)
	public Date getDateModified() {
		return this.dateModified;
	}

	public void setDateModified(Date dateModified) {
		this.dateModified = dateModified;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "server_date_modified", length = 35)
	public Date getServerDateModified() {
		return this.serverDateModified;
	}

	public void setServerDateModified(Date serverDateModified) {
		this.serverDateModified = serverDateModified;
	}

	@Column(name = "app_version")
	public String getAppVersion() {
		return this.appVersion;
	}

	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "date_arrived", length = 13)
	public Date getDateArrived() {
		return this.dateArrived;
	}

	public void setDateArrived(Date dateArrived) {
		this.dateArrived = dateArrived;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "date_learned", length = 13)
	public Date getDateLearned() {
		return this.dateLearned;
	}

	public void setDateLearned(Date dateLearned) {
		this.dateLearned = dateLearned;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "date_of_delivery", length = 13)
	public Date getDateOfDelivery() {
		return this.dateOfDelivery;
	}

	public void setDateOfDelivery(Date dateOfDelivery) {
		this.dateOfDelivery = dateOfDelivery;
	}

	@Column(name = "name")
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "preg_status")
	public String getPregStatus() {
		return this.pregStatus;
	}

	public void setPregStatus(String pregStatus) {
		this.pregStatus = pregStatus;
	}

	@Column(name = "referral_info")
	public String getReferralInfo() {
		return this.referralInfo;
	}

	public void setReferralInfo(String referralInfo) {
		this.referralInfo = referralInfo;
	}

	@Column(name = "abortion_type")
	public String getAbortionType() {
		return this.abortionType;
	}

	public void setAbortionType(String abortionType) {
		this.abortionType = abortionType;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "date_aborted", length = 13)
	public Date getDateAborted() {
		return this.dateAborted;
	}

	public void setDateAborted(Date dateAborted) {
		this.dateAborted = dateAborted;
	}

	@Column(name = "migrated_status")
	public String getMigratedStatus() {
		return this.migratedStatus;
	}

	public void setMigratedStatus(String migratedStatus) {
		this.migratedStatus = migratedStatus;
	}

	@Column(name = "delivery_offset_days")
	public Integer getDeliveryOffsetDays() {
		return this.deliveryOffsetDays;
	}

	public void setDeliveryOffsetDays(Integer deliveryOffsetDays) {
		this.deliveryOffsetDays = deliveryOffsetDays;
	}

	@Column(name = "status")
	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "creation_time", length = 35)
	public Date getCreationTime() {
		return this.creationTime;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "date_del_fu", length = 13)
	public Date getDateDelFu() {
		return this.dateDelFu;
	}

	public void setDateDelFu(Date dateDelFu) {
		this.dateDelFu = dateDelFu;
	}

}