package com.shellshellfish.aaas.finance.model.dao;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Fundresources {
	@Id
	private String id;
	private String code;
	private String mgrbirthday;
	private String mgrage;
	private String mgrdegree;
	private String mgrresume;
	private String mgrgender;
	private String mgrsdate;
	private String mgrdates;
	private String mgravgyears;
	private String mgrbigestyears;
	private String mgrnation;
	private String mgrfundsnum;
	private String mgrghallreturn;
	private String mgrghannuavgreturn;
	private String mgrtermannuavgreturn;

	public Fundresources() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMgrbirthday() {
		return mgrbirthday;
	}

	public void setMgrbirthday(String mgrbirthday) {
		this.mgrbirthday = mgrbirthday;
	}

	public String getMgrage() {
		return mgrage;
	}

	public void setMgrage(String mgrage) {
		this.mgrage = mgrage;
	}

	public String getMgrdegree() {
		return mgrdegree;
	}

	public void setMgrdegree(String mgrdegree) {
		this.mgrdegree = mgrdegree;
	}

	public String getMgrresume() {
		return mgrresume;
	}

	public void setMgrresume(String mgrresume) {
		this.mgrresume = mgrresume;
	}

	public String getMgrgender() {
		return mgrgender;
	}

	public void setMgrgender(String mgrgender) {
		this.mgrgender = mgrgender;
	}

	public String getMgrsdate() {
		return mgrsdate;
	}

	public void setMgrsdate(String mgrsdate) {
		this.mgrsdate = mgrsdate;
	}

	public String getMgrdates() {
		return mgrdates;
	}

	public void setMgrdates(String mgrdates) {
		this.mgrdates = mgrdates;
	}

	public String getMgravgyears() {
		return mgravgyears;
	}

	public void setMgravgyears(String mgravgyears) {
		this.mgravgyears = mgravgyears;
	}

	public String getMgrbigestyears() {
		return mgrbigestyears;
	}

	public void setMgrbigestyears(String mgrbigestyears) {
		this.mgrbigestyears = mgrbigestyears;
	}

	public String getMgrnation() {
		return mgrnation;
	}

	public void setMgrnation(String mgrnation) {
		this.mgrnation = mgrnation;
	}

	public String getMgrfundsnum() {
		return mgrfundsnum;
	}

	public void setMgrfundsnum(String mgrfundsnum) {
		this.mgrfundsnum = mgrfundsnum;
	}

	public String getMgrghallreturn() {
		return mgrghallreturn;
	}

	public void setMgrghallreturn(String mgrghallreturn) {
		this.mgrghallreturn = mgrghallreturn;
	}

	public String getMgrghannuavgreturn() {
		return mgrghannuavgreturn;
	}

	public void setMgrghannuavgreturn(String mgrghannuavgreturn) {
		this.mgrghannuavgreturn = mgrghannuavgreturn;
	}

	public String getMgrtermannuavgreturn() {
		return mgrtermannuavgreturn;
	}

	public void setMgrtermannuavgreturn(String mgrtermannuavgreturn) {
		this.mgrtermannuavgreturn = mgrtermannuavgreturn;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Override
	public String toString() {
		return "FundResources [id=" + id + ", code=" + code + ", mgrbirthday=" + mgrbirthday + ", mgrage=" + mgrage
				+ ", mgrdegree=" + mgrdegree + ", mgrresume=" + mgrresume + ", mgrgender=" + mgrgender + ", mgrsdate="
				+ mgrsdate + ", mgrdates=" + mgrdates + ", mgravgyears=" + mgravgyears + ", mgrbigestyears="
				+ mgrbigestyears + ", mgrnation=" + mgrnation + ", mgrfundsnum=" + mgrfundsnum + ", mgrghallreturn="
				+ mgrghallreturn + ", mgrghannuavgreturn=" + mgrghannuavgreturn + ", mgrtermannuavgreturn="
				+ mgrtermannuavgreturn + "]";
	}

	public Fundresources(String id, String code, String mgrbirthday, String mgrage, String mgrdegree, String mgrresume,
			String mgrgender, String mgrsdate, String mgrdates, String mgravgyears, String mgrbigestyears,
			String mgrnation, String mgrfundsnum, String mgrghallreturn, String mgrghannuavgreturn,
			String mgrtermannuavgreturn) {
		super();
		this.id = id;
		this.code = code;
		this.mgrbirthday = mgrbirthday;
		this.mgrage = mgrage;
		this.mgrdegree = mgrdegree;
		this.mgrresume = mgrresume;
		this.mgrgender = mgrgender;
		this.mgrsdate = mgrsdate;
		this.mgrdates = mgrdates;
		this.mgravgyears = mgravgyears;
		this.mgrbigestyears = mgrbigestyears;
		this.mgrnation = mgrnation;
		this.mgrfundsnum = mgrfundsnum;
		this.mgrghallreturn = mgrghallreturn;
		this.mgrghannuavgreturn = mgrghannuavgreturn;
		this.mgrtermannuavgreturn = mgrtermannuavgreturn;
	}

}
