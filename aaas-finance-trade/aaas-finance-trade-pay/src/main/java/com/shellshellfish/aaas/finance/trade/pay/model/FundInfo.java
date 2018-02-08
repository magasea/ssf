package com.shellshellfish.aaas.finance.trade.pay.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class FundInfo {
    @Id
    private String id;
    private String subscribestate;
    private String minshare;
    private String declarestate;
    private String hfincomeratio;
    private String incomeratio;
    private String fundname;
    private String withdrawstate;
    private String manager_company;
    private String manager_id;
    private String totalnetvalue;
    private String trup_company;
    private String sharetype;
    private String establish_date;
    private String trendstate;
    private String fundtype;
    private String fundrisklevel;
    private String fundstate;
    private String fundcode;
    private String pernetvalue;
    private String asset_totol;
    private String valuagrstate;
    private String transflag;
    private String tacode;
    private String navdate;
    private String dayinc;
    private String fund_mananger;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSubscribestate() {
        return subscribestate;
    }

    public void setSubscribestate(String subscribestate) {
        this.subscribestate = subscribestate;
    }

    public String getMinshare() {
        return minshare;
    }

    public void setMinshare(String minshare) {
        this.minshare = minshare;
    }

    public String getDeclarestate() {
        return declarestate;
    }

    public void setDeclarestate(String declarestate) {
        this.declarestate = declarestate;
    }

    public String getHfincomeratio() {
        return hfincomeratio;
    }

    public void setHfincomeratio(String hfincomeratio) {
        this.hfincomeratio = hfincomeratio;
    }

    public String getIncomeratio() {
        return incomeratio;
    }

    public void setIncomeratio(String incomeratio) {
        this.incomeratio = incomeratio;
    }

    public String getFundname() {
        return fundname;
    }

    public void setFundname(String fundname) {
        this.fundname = fundname;
    }

    public String getWithdrawstate() {
        return withdrawstate;
    }

    public void setWithdrawstate(String withdrawstate) {
        this.withdrawstate = withdrawstate;
    }

    public String getManager_company() {
        return manager_company;
    }

    public void setManager_company(String manager_company) {
        this.manager_company = manager_company;
    }

    public String getManager_id() {
        return manager_id;
    }

    public void setManager_id(String manager_id) {
        this.manager_id = manager_id;
    }

    public String getTotalnetvalue() {
        return totalnetvalue;
    }

    public void setTotalnetvalue(String totalnetvalue) {
        this.totalnetvalue = totalnetvalue;
    }

    public String getTrup_company() {
        return trup_company;
    }

    public void setTrup_company(String trup_company) {
        this.trup_company = trup_company;
    }

    public String getSharetype() {
        return sharetype;
    }

    public void setSharetype(String sharetype) {
        this.sharetype = sharetype;
    }

    public String getEstablish_date() {
        return establish_date;
    }

    public void setEstablish_date(String establish_date) {
        this.establish_date = establish_date;
    }

    public String getTrendstate() {
        return trendstate;
    }

    public void setTrendstate(String trendstate) {
        this.trendstate = trendstate;
    }

    public String getFundtype() {
        return fundtype;
    }

    public void setFundtype(String fundtype) {
        this.fundtype = fundtype;
    }

    public String getFundrisklevel() {
        return fundrisklevel;
    }

    public void setFundrisklevel(String fundrisklevel) {
        this.fundrisklevel = fundrisklevel;
    }

    public String getFundstate() {
        return fundstate;
    }

    public void setFundstate(String fundstate) {
        this.fundstate = fundstate;
    }

    public String getFundcode() {
        return fundcode;
    }

    public void setFundcode(String fundcode) {
        this.fundcode = fundcode;
    }

    public String getPernetvalue() {
        return pernetvalue;
    }

    public void setPernetvalue(String pernetvalue) {
        this.pernetvalue = pernetvalue;
    }

    public String getAsset_totol() {
        return asset_totol;
    }

    public void setAsset_totol(String asset_totol) {
        this.asset_totol = asset_totol;
    }

    public String getValuagrstate() {
        return valuagrstate;
    }

    public void setValuagrstate(String valuagrstate) {
        this.valuagrstate = valuagrstate;
    }

    public String getTransflag() {
        return transflag;
    }

    public void setTransflag(String transflag) {
        this.transflag = transflag;
    }

    public String getTacode() {
        return tacode;
    }

    public void setTacode(String tacode) {
        this.tacode = tacode;
    }

    public String getNavdate() {
        return navdate;
    }

    public void setNavdate(String navdate) {
        this.navdate = navdate;
    }

    public String getDayinc() {
        return dayinc;
    }

    public void setDayinc(String dayinc) {
        this.dayinc = dayinc;
    }

    public String getFund_mananger() {
        return fund_mananger;
    }

    public void setFund_mananger(String fund_mananger) {
        this.fund_mananger = fund_mananger;
    }
}
