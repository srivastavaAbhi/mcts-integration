package org.motechproject.mcts.integration.hibernate.model;

// Generated May 26, 2014 2:25:24 PM by Hibernate Tools 3.4.0.CR1

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * MotherCaseMctsAuthorizedStatus generated by hbm2java
 */
@Entity
@Table(name = "mother_case_mcts_authorized_status", schema = "report")
public class MotherCaseMctsAuthorizedStatus implements java.io.Serializable {

	private int id;
	private String name;
	private Set<MctsPregnantMother> mctsPregnantMothers = new HashSet<MctsPregnantMother>(
			0);

	public MotherCaseMctsAuthorizedStatus() {
	}

	public MotherCaseMctsAuthorizedStatus(int id) {
		this.id = id;
	}

	public MotherCaseMctsAuthorizedStatus(int id, String name,
			Set<MctsPregnantMother> mctsPregnantMothers) {
		this.id = id;
		this.name = name;
		this.mctsPregnantMothers = mctsPregnantMothers;
	}

	@Id
	@Column(name = "id", unique = true, nullable = false)
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Column(name = "name")
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "motherCaseMctsAuthorizedStatus")
	public Set<MctsPregnantMother> getMctsPregnantMothers() {
		return this.mctsPregnantMothers;
	}

	public void setMctsPregnantMothers(
			Set<MctsPregnantMother> mctsPregnantMothers) {
		this.mctsPregnantMothers = mctsPregnantMothers;
	}

}
