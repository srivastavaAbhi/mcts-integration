package org.motechproject.mcts.integration.hibernate.model;

// Generated May 14, 2014 4:50:50 PM by Hibernate Tools 3.4.0.CR1

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * MctsTaluk generated by hbm2java
 */
@Entity
@Table(name = "mcts_taluk", schema = "report")
public class MctsTaluk implements java.io.Serializable {

	private int id;
	private MctsDistrict mctsDistrict;
	private int talukId;
	private String name;
	private Set<MctsHealthblock> mctsHealthblocks = new HashSet<MctsHealthblock>(
			0);
	private Set<MctsVillage> mctsVillages = new HashSet<MctsVillage>(0);

	public MctsTaluk() {
	}

	public MctsTaluk(int id, MctsDistrict mctsDistrict, int talukId, String name) {
		this.id = id;
		this.mctsDistrict = mctsDistrict;
		this.talukId = talukId;
		this.name = name;
	}

	public MctsTaluk(int id, MctsDistrict mctsDistrict, int talukId,
			String name, Set<MctsHealthblock> mctsHealthblocks,
			Set<MctsVillage> mctsVillages) {
		this.id = id;
		this.mctsDistrict = mctsDistrict;
		this.talukId = talukId;
		this.name = name;
		this.mctsHealthblocks = mctsHealthblocks;
		this.mctsVillages = mctsVillages;
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
	@JoinColumn(name = "district_id", nullable = false)
	public MctsDistrict getMctsDistrict() {
		return this.mctsDistrict;
	}

	public void setMctsDistrict(MctsDistrict mctsDistrict) {
		this.mctsDistrict = mctsDistrict;
	}

	@Column(name = "taluk_id", nullable = false)
	public int getTalukId() {
		return this.talukId;
	}

	public void setTalukId(int talukId) {
		this.talukId = talukId;
	}

	@Column(name = "name", nullable = false)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "mctsTaluk")
	public Set<MctsHealthblock> getMctsHealthblocks() {
		return this.mctsHealthblocks;
	}

	public void setMctsHealthblocks(Set<MctsHealthblock> mctsHealthblocks) {
		this.mctsHealthblocks = mctsHealthblocks;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "mctsTaluk")
	public Set<MctsVillage> getMctsVillages() {
		return this.mctsVillages;
	}

	public void setMctsVillages(Set<MctsVillage> mctsVillages) {
		this.mctsVillages = mctsVillages;
	}

}
