package com.shellshellfish.aaas.datacollection.server.model;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by chenwei on 2017- 十二月 - 25
 */
@Document(collection = "fundresources")
public class FundResources {
  String id;
  String minstockbuyshareoffl;
  String minpurchdiscounts;
  String benchmark;
  String exdiviotcdate;
  String investscope;
  String diviibdate;
  String bnfdisinterestrule;
  String accntfirm;
  String diviotcdate;
  Double custodianfeeratio;
  String name;
  String code;
  String scaleranking;
  Double existingyears;
  String fundmanager;
  String servicefeeratio;
  String divpayout;
  String bnfdismethod;
  String reddmsdate;
  String investidea;
  String investobj;
  String stockbuysdateoffl;
  String divtimes;
  String divaccumdpayout;
  String mgrcomp;
  String redemstatus;
  String maxcashbuyshareonl;
  String maturitydate;
  String mincashbuyshareoffl;
  String cashbuysdateonl;
  String tradecur;
  String etflistdate;
  String subscrfeeratio;
  String fundtype;
  String strucfundornot;
  Integer divperiodtimes;
  String choicetopictype;
  String bnfdisrule;
  String ptm;
  String buysdateoffl;
  String avgsimifundscale;
  String simifundnum;
  String exdividate;
  String dividate;
  String ptmyear;
  String risklevel;
  String fname;
  Double maxpurchfeeratio;
  String custodianbank;
  String portfoliomaxratio;
  String divperiodpayout;
  String morningstarfundtype;
  String maxredemfeeratio;
  String redemconfirmdate;
  String custsdate;
  String purchandredemcode;
  String sharecodate;
  String par;
  String mincashbuyshareonl;
  String guarantfundornot;
  String buyedateoffl;
  String isofixinvestment;
  String bmindexchgpct;
  String stfundcycle;
  String founddate;
  String purchfeeratio;
  String stockbuyedateoffl;
  String custedate;
  Long querystdate;
  String investstrategy;
  String discountfeeornot;
  String etfdealshareonmkt;
  String emcode;
  String bmindexcode;
  String fundsharetransdate;
  String bnfcomethod;
  String firstinvesttype;
  Double minfidiscounts;
  String purchconfirmdate;
  Integer divaccumdtimes;
  Long update;
  String divperunit;
  String predfundmanager;
  Double managfeeratio;
  String portfoliominratio;
  Double fundscale;
  String cashbuyedateonl;
  String redemfeeratio;
  Double diviratio;
  String registerdate;
  String onlcashofferingcode;
  String insidename;
  String fundsharetransratio;
  String secondinvesttype;
  String redempaydate;
  Double divaccumuperunit;
  String listmkt;
  String firstmktfundcode;
  String purchstatus;
  Long queryenddate;
  Double lplimit;
  String investstyle;
  String csrctype;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getMinstockbuyshareoffl() {
    return minstockbuyshareoffl;
  }

  public void setMinstockbuyshareoffl(String minstockbuyshareoffl) {
    this.minstockbuyshareoffl = minstockbuyshareoffl;
  }

  public String getMinpurchdiscounts() {
    return minpurchdiscounts;
  }

  public void setMinpurchdiscounts(String minpurchdiscounts) {
    this.minpurchdiscounts = minpurchdiscounts;
  }

  public String getBenchmark() {
    return benchmark;
  }

  public void setBenchmark(String benchmark) {
    this.benchmark = benchmark;
  }

  public String getExdiviotcdate() {
    return exdiviotcdate;
  }

  public void setExdiviotcdate(String exdiviotcdate) {
    this.exdiviotcdate = exdiviotcdate;
  }

  public String getInvestscope() {
    return investscope;
  }

  public void setInvestscope(String investscope) {
    this.investscope = investscope;
  }

  public String getDiviibdate() {
    return diviibdate;
  }

  public void setDiviibdate(String diviibdate) {
    this.diviibdate = diviibdate;
  }

  public String getBnfdisinterestrule() {
    return bnfdisinterestrule;
  }

  public void setBnfdisinterestrule(String bnfdisinterestrule) {
    this.bnfdisinterestrule = bnfdisinterestrule;
  }

  public String getAccntfirm() {
    return accntfirm;
  }

  public void setAccntfirm(String accntfirm) {
    this.accntfirm = accntfirm;
  }

  public String getDiviotcdate() {
    return diviotcdate;
  }

  public void setDiviotcdate(String diviotcdate) {
    this.diviotcdate = diviotcdate;
  }

  public Double getCustodianfeeratio() {
    return custodianfeeratio;
  }

  public void setCustodianfeeratio(Double custodianfeeratio) {
    this.custodianfeeratio = custodianfeeratio;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getScaleranking() {
    return scaleranking;
  }

  public void setScaleranking(String scaleranking) {
    this.scaleranking = scaleranking;
  }

  public Double getExistingyears() {
    return existingyears;
  }

  public void setExistingyears(Double existingyears) {
    this.existingyears = existingyears;
  }

  public String getFundmanager() {
    return fundmanager;
  }

  public void setFundmanager(String fundmanager) {
    this.fundmanager = fundmanager;
  }

  public String getServicefeeratio() {
    return servicefeeratio;
  }

  public void setServicefeeratio(String servicefeeratio) {
    this.servicefeeratio = servicefeeratio;
  }

  public String getDivpayout() {
    return divpayout;
  }

  public void setDivpayout(String divpayout) {
    this.divpayout = divpayout;
  }

  public String getBnfdismethod() {
    return bnfdismethod;
  }

  public void setBnfdismethod(String bnfdismethod) {
    this.bnfdismethod = bnfdismethod;
  }

  public String getReddmsdate() {
    return reddmsdate;
  }

  public void setReddmsdate(String reddmsdate) {
    this.reddmsdate = reddmsdate;
  }

  public String getInvestidea() {
    return investidea;
  }

  public void setInvestidea(String investidea) {
    this.investidea = investidea;
  }

  public String getInvestobj() {
    return investobj;
  }

  public void setInvestobj(String investobj) {
    this.investobj = investobj;
  }

  public String getStockbuysdateoffl() {
    return stockbuysdateoffl;
  }

  public void setStockbuysdateoffl(String stockbuysdateoffl) {
    this.stockbuysdateoffl = stockbuysdateoffl;
  }

  public String getDivtimes() {
    return divtimes;
  }

  public void setDivtimes(String divtimes) {
    this.divtimes = divtimes;
  }

  public String getDivaccumdpayout() {
    return divaccumdpayout;
  }

  public void setDivaccumdpayout(String divaccumdpayout) {
    this.divaccumdpayout = divaccumdpayout;
  }

  public String getMgrcomp() {
    return mgrcomp;
  }

  public void setMgrcomp(String mgrcomp) {
    this.mgrcomp = mgrcomp;
  }

  public String getRedemstatus() {
    return redemstatus;
  }

  public void setRedemstatus(String redemstatus) {
    this.redemstatus = redemstatus;
  }

  public String getMaxcashbuyshareonl() {
    return maxcashbuyshareonl;
  }

  public void setMaxcashbuyshareonl(String maxcashbuyshareonl) {
    this.maxcashbuyshareonl = maxcashbuyshareonl;
  }

  public String getMaturitydate() {
    return maturitydate;
  }

  public void setMaturitydate(String maturitydate) {
    this.maturitydate = maturitydate;
  }

  public String getMincashbuyshareoffl() {
    return mincashbuyshareoffl;
  }

  public void setMincashbuyshareoffl(String mincashbuyshareoffl) {
    this.mincashbuyshareoffl = mincashbuyshareoffl;
  }

  public String getCashbuysdateonl() {
    return cashbuysdateonl;
  }

  public void setCashbuysdateonl(String cashbuysdateonl) {
    this.cashbuysdateonl = cashbuysdateonl;
  }

  public String getTradecur() {
    return tradecur;
  }

  public void setTradecur(String tradecur) {
    this.tradecur = tradecur;
  }

  public String getEtflistdate() {
    return etflistdate;
  }

  public void setEtflistdate(String etflistdate) {
    this.etflistdate = etflistdate;
  }

  public String getSubscrfeeratio() {
    return subscrfeeratio;
  }

  public void setSubscrfeeratio(String subscrfeeratio) {
    this.subscrfeeratio = subscrfeeratio;
  }

  public String getFundtype() {
    return fundtype;
  }

  public void setFundtype(String fundtype) {
    this.fundtype = fundtype;
  }

  public String getStrucfundornot() {
    return strucfundornot;
  }

  public void setStrucfundornot(String strucfundornot) {
    this.strucfundornot = strucfundornot;
  }

  public Integer getDivperiodtimes() {
    return divperiodtimes;
  }

  public void setDivperiodtimes(Integer divperiodtimes) {
    this.divperiodtimes = divperiodtimes;
  }

  public String getChoicetopictype() {
    return choicetopictype;
  }

  public void setChoicetopictype(String choicetopictype) {
    this.choicetopictype = choicetopictype;
  }

  public String getBnfdisrule() {
    return bnfdisrule;
  }

  public void setBnfdisrule(String bnfdisrule) {
    this.bnfdisrule = bnfdisrule;
  }

  public String getPtm() {
    return ptm;
  }

  public void setPtm(String ptm) {
    this.ptm = ptm;
  }

  public String getBuysdateoffl() {
    return buysdateoffl;
  }

  public void setBuysdateoffl(String buysdateoffl) {
    this.buysdateoffl = buysdateoffl;
  }

  public String getAvgsimifundscale() {
    return avgsimifundscale;
  }

  public void setAvgsimifundscale(String avgsimifundscale) {
    this.avgsimifundscale = avgsimifundscale;
  }

  public String getSimifundnum() {
    return simifundnum;
  }

  public void setSimifundnum(String simifundnum) {
    this.simifundnum = simifundnum;
  }

  public String getExdividate() {
    return exdividate;
  }

  public void setExdividate(String exdividate) {
    this.exdividate = exdividate;
  }

  public String getDividate() {
    return dividate;
  }

  public void setDividate(String dividate) {
    this.dividate = dividate;
  }

  public String getPtmyear() {
    return ptmyear;
  }

  public void setPtmyear(String ptmyear) {
    this.ptmyear = ptmyear;
  }

  public String getRisklevel() {
    return risklevel;
  }

  public void setRisklevel(String risklevel) {
    this.risklevel = risklevel;
  }

  public String getFname() {
    return fname;
  }

  public void setFname(String fname) {
    this.fname = fname;
  }

  public Double getMaxpurchfeeratio() {
    return maxpurchfeeratio;
  }

  public void setMaxpurchfeeratio(Double maxpurchfeeratio) {
    this.maxpurchfeeratio = maxpurchfeeratio;
  }

  public String getCustodianbank() {
    return custodianbank;
  }

  public void setCustodianbank(String custodianbank) {
    this.custodianbank = custodianbank;
  }

  public String getPortfoliomaxratio() {
    return portfoliomaxratio;
  }

  public void setPortfoliomaxratio(String portfoliomaxratio) {
    this.portfoliomaxratio = portfoliomaxratio;
  }

  public String getDivperiodpayout() {
    return divperiodpayout;
  }

  public void setDivperiodpayout(String divperiodpayout) {
    this.divperiodpayout = divperiodpayout;
  }

  public String getMorningstarfundtype() {
    return morningstarfundtype;
  }

  public void setMorningstarfundtype(String morningstarfundtype) {
    this.morningstarfundtype = morningstarfundtype;
  }

  public String getMaxredemfeeratio() {
    return maxredemfeeratio;
  }

  public void setMaxredemfeeratio(String maxredemfeeratio) {
    this.maxredemfeeratio = maxredemfeeratio;
  }

  public String getRedemconfirmdate() {
    return redemconfirmdate;
  }

  public void setRedemconfirmdate(String redemconfirmdate) {
    this.redemconfirmdate = redemconfirmdate;
  }

  public String getCustsdate() {
    return custsdate;
  }

  public void setCustsdate(String custsdate) {
    this.custsdate = custsdate;
  }

  public String getPurchandredemcode() {
    return purchandredemcode;
  }

  public void setPurchandredemcode(String purchandredemcode) {
    this.purchandredemcode = purchandredemcode;
  }

  public String getSharecodate() {
    return sharecodate;
  }

  public void setSharecodate(String sharecodate) {
    this.sharecodate = sharecodate;
  }

  public String getPar() {
    return par;
  }

  public void setPar(String par) {
    this.par = par;
  }

  public String getMincashbuyshareonl() {
    return mincashbuyshareonl;
  }

  public void setMincashbuyshareonl(String mincashbuyshareonl) {
    this.mincashbuyshareonl = mincashbuyshareonl;
  }

  public String getGuarantfundornot() {
    return guarantfundornot;
  }

  public void setGuarantfundornot(String guarantfundornot) {
    this.guarantfundornot = guarantfundornot;
  }

  public String getBuyedateoffl() {
    return buyedateoffl;
  }

  public void setBuyedateoffl(String buyedateoffl) {
    this.buyedateoffl = buyedateoffl;
  }

  public String getIsofixinvestment() {
    return isofixinvestment;
  }

  public void setIsofixinvestment(String isofixinvestment) {
    this.isofixinvestment = isofixinvestment;
  }

  public String getBmindexchgpct() {
    return bmindexchgpct;
  }

  public void setBmindexchgpct(String bmindexchgpct) {
    this.bmindexchgpct = bmindexchgpct;
  }

  public String getStfundcycle() {
    return stfundcycle;
  }

  public void setStfundcycle(String stfundcycle) {
    this.stfundcycle = stfundcycle;
  }

  public String getFounddate() {
    return founddate;
  }

  public void setFounddate(String founddate) {
    this.founddate = founddate;
  }

  public String getPurchfeeratio() {
    return purchfeeratio;
  }

  public void setPurchfeeratio(String purchfeeratio) {
    this.purchfeeratio = purchfeeratio;
  }

  public String getStockbuyedateoffl() {
    return stockbuyedateoffl;
  }

  public void setStockbuyedateoffl(String stockbuyedateoffl) {
    this.stockbuyedateoffl = stockbuyedateoffl;
  }

  public String getCustedate() {
    return custedate;
  }

  public void setCustedate(String custedate) {
    this.custedate = custedate;
  }

  public Long getQuerystdate() {
    return querystdate;
  }

  public void setQuerystdate(Long querystdate) {
    this.querystdate = querystdate;
  }

  public String getInveststrategy() {
    return investstrategy;
  }

  public void setInveststrategy(String investstrategy) {
    this.investstrategy = investstrategy;
  }

  public String getDiscountfeeornot() {
    return discountfeeornot;
  }

  public void setDiscountfeeornot(String discountfeeornot) {
    this.discountfeeornot = discountfeeornot;
  }

  public String getEtfdealshareonmkt() {
    return etfdealshareonmkt;
  }

  public void setEtfdealshareonmkt(String etfdealshareonmkt) {
    this.etfdealshareonmkt = etfdealshareonmkt;
  }

  public String getEmcode() {
    return emcode;
  }

  public void setEmcode(String emcode) {
    this.emcode = emcode;
  }

  public String getBmindexcode() {
    return bmindexcode;
  }

  public void setBmindexcode(String bmindexcode) {
    this.bmindexcode = bmindexcode;
  }

  public String getFundsharetransdate() {
    return fundsharetransdate;
  }

  public void setFundsharetransdate(String fundsharetransdate) {
    this.fundsharetransdate = fundsharetransdate;
  }

  public String getBnfcomethod() {
    return bnfcomethod;
  }

  public void setBnfcomethod(String bnfcomethod) {
    this.bnfcomethod = bnfcomethod;
  }

  public String getFirstinvesttype() {
    return firstinvesttype;
  }

  public void setFirstinvesttype(String firstinvesttype) {
    this.firstinvesttype = firstinvesttype;
  }

  public Double getMinfidiscounts() {
    return minfidiscounts;
  }

  public void setMinfidiscounts(Double minfidiscounts) {
    this.minfidiscounts = minfidiscounts;
  }

  public String getPurchconfirmdate() {
    return purchconfirmdate;
  }

  public void setPurchconfirmdate(String purchconfirmdate) {
    this.purchconfirmdate = purchconfirmdate;
  }

  public Integer getDivaccumdtimes() {
    return divaccumdtimes;
  }

  public void setDivaccumdtimes(Integer divaccumdtimes) {
    this.divaccumdtimes = divaccumdtimes;
  }

  public Long getUpdate() {
    return update;
  }

  public void setUpdate(Long update) {
    this.update = update;
  }

  public String getDivperunit() {
    return divperunit;
  }

  public void setDivperunit(String divperunit) {
    this.divperunit = divperunit;
  }

  public String getPredfundmanager() {
    return predfundmanager;
  }

  public void setPredfundmanager(String predfundmanager) {
    this.predfundmanager = predfundmanager;
  }

  public Double getManagfeeratio() {
    return managfeeratio;
  }

  public void setManagfeeratio(Double managfeeratio) {
    this.managfeeratio = managfeeratio;
  }

  public String getPortfoliominratio() {
    return portfoliominratio;
  }

  public void setPortfoliominratio(String portfoliominratio) {
    this.portfoliominratio = portfoliominratio;
  }

  public Double getFundscale() {
    return fundscale;
  }

  public void setFundscale(Double fundscale) {
    this.fundscale = fundscale;
  }

  public String getCashbuyedateonl() {
    return cashbuyedateonl;
  }

  public void setCashbuyedateonl(String cashbuyedateonl) {
    this.cashbuyedateonl = cashbuyedateonl;
  }

  public String getRedemfeeratio() {
    return redemfeeratio;
  }

  public void setRedemfeeratio(String redemfeeratio) {
    this.redemfeeratio = redemfeeratio;
  }

  public Double getDiviratio() {
    return diviratio;
  }

  public void setDiviratio(Double diviratio) {
    this.diviratio = diviratio;
  }

  public String getRegisterdate() {
    return registerdate;
  }

  public void setRegisterdate(String registerdate) {
    this.registerdate = registerdate;
  }

  public String getOnlcashofferingcode() {
    return onlcashofferingcode;
  }

  public void setOnlcashofferingcode(String onlcashofferingcode) {
    this.onlcashofferingcode = onlcashofferingcode;
  }

  public String getInsidename() {
    return insidename;
  }

  public void setInsidename(String insidename) {
    this.insidename = insidename;
  }

  public String getFundsharetransratio() {
    return fundsharetransratio;
  }

  public void setFundsharetransratio(String fundsharetransratio) {
    this.fundsharetransratio = fundsharetransratio;
  }

  public String getSecondinvesttype() {
    return secondinvesttype;
  }

  public void setSecondinvesttype(String secondinvesttype) {
    this.secondinvesttype = secondinvesttype;
  }

  public String getRedempaydate() {
    return redempaydate;
  }

  public void setRedempaydate(String redempaydate) {
    this.redempaydate = redempaydate;
  }

  public Double getDivaccumuperunit() {
    return divaccumuperunit;
  }

  public void setDivaccumuperunit(Double divaccumuperunit) {
    this.divaccumuperunit = divaccumuperunit;
  }

  public String getListmkt() {
    return listmkt;
  }

  public void setListmkt(String listmkt) {
    this.listmkt = listmkt;
  }

  public String getFirstmktfundcode() {
    return firstmktfundcode;
  }

  public void setFirstmktfundcode(String firstmktfundcode) {
    this.firstmktfundcode = firstmktfundcode;
  }

  public String getPurchstatus() {
    return purchstatus;
  }

  public void setPurchstatus(String purchstatus) {
    this.purchstatus = purchstatus;
  }

  public Long getQueryenddate() {
    return queryenddate;
  }

  public void setQueryenddate(Long queryenddate) {
    this.queryenddate = queryenddate;
  }

  public Double getLplimit() {
    return lplimit;
  }

  public void setLplimit(Double lplimit) {
    this.lplimit = lplimit;
  }

  public String getInveststyle() {
    return investstyle;
  }

  public void setInveststyle(String investstyle) {
    this.investstyle = investstyle;
  }

  public String getCsrctype() {
    return csrctype;
  }

  public void setCsrctype(String csrctype) {
    this.csrctype = csrctype;
  }
}
