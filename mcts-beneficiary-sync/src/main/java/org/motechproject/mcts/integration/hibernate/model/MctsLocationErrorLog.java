package org.motechproject.mcts.integration.hibernate.model;

// Generated May 19, 2014 7:55:47 PM by Hibernate Tools 3.4.0.CR1

import static javax.persistence.GenerationType.SEQUENCE;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * MctsLocationMaster generated by hbm2java
 */
@Entity
@Table(name = "mcts_location_error_log", schema = "report")
public class MctsLocationErrorLog implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1135568505844170766L;
    private Integer id;
    private String stateid;
    private String state;
    private String dcode;
    private String district;
    private String tcode;
    private String talukaname;
    private String bid;
    private String block;
    private String pid;
    private String phc;
    private String sid;
    private String subcenter;
    private String vcode;
    private String village;
    private String status;
    private String comments;

    public MctsLocationErrorLog() {
    }

    public MctsLocationErrorLog(String stateid, String state, String dcode,
            String district, String tcode, String talukaname, String bid,
            String block, String pid, String phc, String sid, String subcenter,
            String vcode, String village, String status, String comments) {
        this.stateid = stateid;
        this.state = state;
        this.dcode = dcode;
        this.district = district;
        this.tcode = tcode;
        this.talukaname = talukaname;
        this.bid = bid;
        this.block = block;
        this.pid = pid;
        this.phc = phc;
        this.sid = sid;
        this.subcenter = subcenter;
        this.vcode = vcode;
        this.village = village;
        this.status = status;
        this.comments = comments;
    }

    @SequenceGenerator(name = "generator", sequenceName = "mcts_location_master_seq")
    @Id
    @GeneratedValue(strategy = SEQUENCE, generator = "generator")
    @Column(name = "id", unique = true, nullable = false)
    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "stateid")
    public String getStateid() {
        return this.stateid;
    }

    public void setStateid(String stateid) {
        this.stateid = stateid;
    }

    @Column(name = "state")
    public String getState() {
        return this.state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Column(name = "dcode")
    public String getDcode() {
        return this.dcode;
    }

    public void setDcode(String dcode) {
        this.dcode = dcode;
    }

    @Column(name = "district")
    public String getDistrict() {
        return this.district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    @Column(name = "tcode")
    public String getTcode() {
        return this.tcode;
    }

    public void setTcode(String tcode) {
        this.tcode = tcode;
    }

    @Column(name = "talukaname")
    public String getTalukaname() {
        return this.talukaname;
    }

    public void setTalukaname(String talukaname) {
        this.talukaname = talukaname;
    }

    @Column(name = "bid")
    public String getBid() {
        return this.bid;
    }

    public void setBid(String bid) {
        this.bid = bid;
    }

    @Column(name = "block")
    public String getBlock() {
        return this.block;
    }

    public void setBlock(String block) {
        this.block = block;
    }

    @Column(name = "pid")
    public String getPid() {
        return this.pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    @Column(name = "phc")
    public String getPhc() {
        return this.phc;
    }

    public void setPhc(String phc) {
        this.phc = phc;
    }

    @Column(name = "sid")
    public String getSid() {
        return this.sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    @Column(name = "subcenter")
    public String getSubcenter() {
        return this.subcenter;
    }

    public void setSubcenter(String subcenter) {
        this.subcenter = subcenter;
    }

    @Column(name = "vcode")
    public String getVcode() {
        return this.vcode;
    }

    public void setVcode(String vcode) {
        this.vcode = vcode;
    }

    @Column(name = "village")
    public String getVillage() {
        return this.village;
    }

    public void setVillage(String village) {
        this.village = village;
    }

    @Column(name = "status")
    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Column(name = "comments")
    public String getComments() {
        return this.comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

}
