package com.shellshellfish.aaas.account.model.dao;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the Verification database table.
 * 
 */
@Entity
@NamedQuery(name="Verification.findAll", query="SELECT v FROM Verification v")
public class Verification implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="Id")
	private int id;

	private int name;

	public Verification() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getName() {
		return this.name;
	}

	public void setName(int name) {
		this.name = name;
	}

}