package org.motechproject.mcts.integration.hibernate.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "unapproved_to_discuss_form")
public class UnapprovedToDiscussForm {
    private int id;
    private String mctsId;
    private String fullMctsId;
    private String reasonDisapproved;
    private String showReasonDisapproved;
    private String badMapping;
    private String ashaCanFix;
    private String promptFix;
    private String ashaFixed;
    private String needToRemap;
    private String approvedFix;
    private String unconfirmed;
    private String ashaNeedsToClose;
    private String anmClose;
    private String confirmAnmClose;
    private String dateModified;
    private String dateAuthorized;
    private String dateAuthorizedInt;
    private boolean close;
    private String namespace;
    private String deviceID;
    private String instanceID;
    private String appVersion;
    private String userID;
    private MotherCase motherCase;
    private MctsPregnantMother mctsPregnantMother;
    
    @Id
    @Column(name = "id", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    
    @Column(name = "mcts_id")
    public String getMctsId() {
        return mctsId;
    }
    public void setMctsId(String mctsId) {
        this.mctsId = mctsId;
    }
    
    @Column(name = "full_mcts_id")
    public String getFullMctsId() {
        return fullMctsId;
    }
    public void setFullMctsId(String fullMctsId) {
        this.fullMctsId = fullMctsId;
    }
    
    @Column(name = "reason_disapproved")
    public String getReasonDisapproved() {
        return reasonDisapproved;
    }
    public void setReasonDisapproved(String reasonDisapproved) {
        this.reasonDisapproved = reasonDisapproved;
    }
    
    @Column(name = "show_reason_disapproved")
    public String getShowReasonDisapproved() {
        return showReasonDisapproved;
    }
    public void setShowReasonDisapproved(String showReasonDisapproved) {
        this.showReasonDisapproved = showReasonDisapproved;
    }
    
    @Column(name = "bad_mapping")
    public String getBadMapping() {
        return badMapping;
    }
    public void setBadMapping(String badMapping) {
        this.badMapping = badMapping;
    }
    
    @Column(name = "asha_can_fix")
    public String getAshaCanFix() {
        return ashaCanFix;
    }
    public void setAshaCanFix(String ashaCanFix) {
        this.ashaCanFix = ashaCanFix;
    }
    
    @Column(name = "prompt_fix")
    public String getPromptFix() {
        return promptFix;
    }
    public void setPromptFix(String promptFix) {
        this.promptFix = promptFix;
    }
    
    @Column(name = "asha_fixed")
    public String getAshaFixed() {
        return ashaFixed;
    }
    public void setAshaFixed(String ashaFixed) {
        this.ashaFixed = ashaFixed;
    }
    
    @Column(name = "need_to_remap")
    public String getNeedToRemap() {
        return needToRemap;
    }
    public void setNeedToRemap(String needToRemap) {
        this.needToRemap = needToRemap;
    }
    
    @Column(name = "approved_fix")
    public String getApprovedFix() {
        return approvedFix;
    }
    public void setApprovedFix(String approvedFix) {
        this.approvedFix = approvedFix;
    }
    
    @Column(name = "unconfirmed")
    public String getUnconfirmed() {
        return unconfirmed;
    }
    public void setUnconfirmed(String unconfirmed) {
        this.unconfirmed = unconfirmed;
    }

    @Column(name = "asha_needs_close")
    public String getAshaNeedsToClose() {
        return ashaNeedsToClose;
    }
    public void setAshaNeedsToClose(String ashaNeedsToClose) {
        this.ashaNeedsToClose = ashaNeedsToClose;
    }
    
    @Column(name = "anm_close")
    public String getAnmClose() {
        return anmClose;
    }
    public void setAnmClose(String anmClose) {
        this.anmClose = anmClose;
    }
    
    @Column(name = "confirm_anm_close")
    public String getConfirmAnmClose() {
        return confirmAnmClose;
    }
    public void setConfirmAnmClose(String confirmAnmClose) {
        this.confirmAnmClose = confirmAnmClose;
    }
    
    @Column(name = "date_modified")
    public String getDateModified() {
        return dateModified;
    }
    public void setDateModified(String dateModified) {
        this.dateModified = dateModified;
    }
    
    @Column(name = "close")
    public boolean getClose() {
        return close;
    }
    public void setClose(boolean close) {
        this.close = close;
    }
    
    @Column(name = "date_authorized")
    public String getDateAuthorized() {
        return dateAuthorized;
    }
    public void setDateAuthorized(String dateAuthorized) {
        this.dateAuthorized = dateAuthorized;
    }
    
    @Column(name = "date_authorized_int")
    public String getDateAuthorizedInt() {
        return dateAuthorizedInt;
    }
    public void setDateAuthorizedInt(String dateAuthorizedInt) {
        this.dateAuthorizedInt = dateAuthorizedInt;
    }
    
    @Column(name = "namespace")
    public String getNamespace() {
        return namespace;
    }
    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }
    
    @Column(name = "device_id")
    public String getDeviceID() {
        return deviceID;
    }
    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }
    
    @Column(name = "instance_id")
    public String getInstanceID() {
        return instanceID;
    }
    public void setInstanceID(String instanceID) {
        this.instanceID = instanceID;
    }
    
    @Column(name = "app_version")
    public String getAppVersion() {
        return appVersion;
    }
    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }
    
    @Column(name = "user_id")
    public String getUserID() {
        return userID;
    }
    public void setUserID(String userID) {
        this.userID = userID;
    }
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "case_id")
    public MotherCase getMotherCase() {
        return motherCase;
    }
    public void setMotherCase(MotherCase motherCase) {
        this.motherCase = motherCase;
    }
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mcts_case_id")
    public MctsPregnantMother getMctsPregnantMother() {
        return mctsPregnantMother;
    }
    public void setMctsPregnantMother(MctsPregnantMother mctsPregnantMother) {
        this.mctsPregnantMother = mctsPregnantMother;
    }
   
}
