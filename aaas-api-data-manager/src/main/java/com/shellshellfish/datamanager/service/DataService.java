package com.shellshellfish.datamanager.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.shellshellfish.datamanager.model.DailyFunds;
import com.shellshellfish.datamanager.model.FundCodes;
import com.shellshellfish.datamanager.model.FundManagers;
import com.shellshellfish.datamanager.model.IndicatorPoint;


public interface DataService {
	//from choice to mongodb
	boolean saveDailytoDBforday(String codelist,String querydate);
	boolean saveDailytoDBfordays(String codelist,String fromdate,String todate);
    boolean saveAllfundCodestoDB();
	//from choice to mongodb
    
    public List<FundCodes>  getAllFundCodes();
    //日指标
    public List<DailyFunds>  getDailyFundsBycode(String[] codelist);
    
    public List<IndicatorPoint>  getMaxfallPeriod(String code,long fromtime,long totime);
    //基金经理信息
    public HashMap<String,Object> getFundManager(String name);
    //基金公司信息
    public HashMap<String,Object> getFundCompany(String name);
    
}
