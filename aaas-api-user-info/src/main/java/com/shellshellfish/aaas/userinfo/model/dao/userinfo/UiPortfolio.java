package com.shellshellfish.aaas.userinfo.model.dao.userinfo;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigInteger;


/**
 * The persistent class for the ui_portfolio database table.
 * 
 */
@Entity
@Table(name="ui_portfolio")
@NamedQuery(name="UiPortfolio.findAll", query="SELECT u FROM UiPortfolio u")
public class UiPortfolio implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="UI_PORTFOLIO_ID_GENERATOR" )
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="UI_PORTFOLIO_ID_GENERATOR")
	private String id;

	private BigInteger investProdId;

	private BigInteger userId;

	public UiPortfolio() {
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public BigInteger getInvestProdId() {
		return this.investProdId;
	}

	public void setInvestProdId(BigInteger investProdId) {
		this.investProdId = investProdId;
	}

	public BigInteger getUserId() {
		return this.userId;
	}

	public void setUserId(BigInteger userId) {
		this.userId = userId;
	}

}