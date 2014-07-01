package org.motechproject.mcts.integration.hibernate.model;

// Generated May 19, 2014 7:55:47 PM by Hibernate Tools 3.4.0.CR1

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.SEQUENCE;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * MctsPhc generated by hbm2java
 */
@Entity
@Table(name = "mcts_phc", schema = "report", uniqueConstraints = @UniqueConstraint(columnNames = {
		"phc_id", "healthblock_id" }))
public class MctsPhc implements java.io.Serializable {

	private Integer id;
	private MctsHealthblock mctsHealthblock;
	private int phcId;
	private String name;
	private boolean status;
	private Set<MctsSubcenter> mctsSubcenters = new HashSet<MctsSubcenter>(0);
	private Set<MctsHealthworker> mctsHealthworkers = new HashSet<MctsHealthworker>(
			0);

	public MctsPhc() {
	}

	public MctsPhc(MctsHealthblock mctsHealthblock, int phcId, String name) {
		this.mctsHealthblock = mctsHealthblock;
		this.phcId = phcId;
		this.name = name;
	}

	public MctsPhc(MctsHealthblock mctsHealthblock, int phcId, String name,
			Set<MctsSubcenter> mctsSubcenters,
			Set<MctsHealthworker> mctsHealthworkers, boolean status) {
		this.mctsHealthblock = mctsHealthblock;
		this.phcId = phcId;
		this.name = name;
		this.mctsSubcenters = mctsSubcenters;
		this.mctsHealthworkers = mctsHealthworkers;
		this.status = status;
	}

	@SequenceGenerator(name = "generator", sequenceName = "mcts_phc_id_seq")
	@Id
	@GeneratedValue(strategy = SEQUENCE, generator = "generator")
	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "healthblock_id", nullable = false)
	public MctsHealthblock getMctsHealthblock() {
		return this.mctsHealthblock;
	}

	public void setMctsHealthblock(MctsHealthblock mctsHealthblock) {
		this.mctsHealthblock = mctsHealthblock;
	}

	@Column(name = "phc_id", nullable = false)
	public int getPhcId() {
		return this.phcId;
	}

	public void setPhcId(int phcId) {
		this.phcId = phcId;
	}

	@Column(name = "name", nullable = false)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "status")
	public boolean getStatus() {
		return this.status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "mctsPhc")
	public Set<MctsSubcenter> getMctsSubcenters() {
		return this.mctsSubcenters;
	}

	public void setMctsSubcenters(Set<MctsSubcenter> mctsSubcenters) {
		this.mctsSubcenters = mctsSubcenters;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "mctsPhc")
	public Set<MctsHealthworker> getMctsHealthworkers() {
		return this.mctsHealthworkers;
	}

	public void setMctsHealthworkers(Set<MctsHealthworker> mctsHealthworkers) {
		this.mctsHealthworkers = mctsHealthworkers;
	}

}
