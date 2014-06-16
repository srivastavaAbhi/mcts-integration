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
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * MctsState generated by hbm2java
 */
@Entity
@Table(name = "mcts_state", schema = "report", uniqueConstraints = @UniqueConstraint(columnNames = "state_id"))
public class MctsState implements java.io.Serializable {

	private Integer id;
	private int stateId;
	private String name;
	private Set<MctsDistrict> mctsDistricts = new HashSet<MctsDistrict>(0);

	public MctsState() {
	}

	public MctsState(int stateId, String name) {
		this.stateId = stateId;
		this.name = name;
	}

	public MctsState(int stateId, String name, Set<MctsDistrict> mctsDistricts) {
		this.stateId = stateId;
		this.name = name;
		this.mctsDistricts = mctsDistricts;
	}

	@SequenceGenerator(name = "generator", sequenceName = "mcts_state_id_seq")
	@Id
	@GeneratedValue(strategy = SEQUENCE, generator = "generator")
	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "state_id", unique = true, nullable = false)
	public int getStateId() {
		return this.stateId;
	}

	public void setStateId(int stateId) {
		this.stateId = stateId;
	}

	@Column(name = "name", nullable = false)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "mctsState")
	public Set<MctsDistrict> getMctsDistricts() {
		return this.mctsDistricts;
	}

	public void setMctsDistricts(Set<MctsDistrict> mctsDistricts) {
		this.mctsDistricts = mctsDistricts;
	}

}