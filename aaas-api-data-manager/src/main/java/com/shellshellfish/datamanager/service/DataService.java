package com.shellshellfish.datamanager.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.shellshellfish.datamanager.model.DailyFunds;
import com.shellshellfish.datamanager.model.FundCodes;


public interface DataService {
	//from choice to mongodb
	boolean saveDailytoDBforday(String codelist,String querydate);
	boolean saveDailytoDBfordays(String codelist,String fromdate,String todate);
    boolean saveAllfundCodestoDB();
	//from choice to mongodb
    
    public List<FundCodes>  getAllFundCodes();
    public List<DailyFunds>  getDailyFunds(String[] codelist,String date);
}
