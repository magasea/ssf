package com.shellshellfish.aaas.datamanager.model;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Id;


import org.springframework.data.mongodb.core.mapping.Field;


public class IndicatorPoint {
	
	
	private String   ptdate;  //每日净值对应的日期
	private String unitvalue;  //复权单位净值
	
	
	public String getUnitvalue() {
	    return unitvalue;
	}

	public void setUnitvalue(String unitvalue) {
	    this.unitvalue = unitvalue;
	}
	
	public String getPtdate() {
	    return ptdate;
	}

	public void setPtdate(long datetime) {
		//china timezone ,GMT+8
		Timestamp ts = new Timestamp((datetime+18000)*1000+8*60*60*1000);  //to milliseconds
        //ts=ts.setTime(time);;
		String tsStr = "";  
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
        try {  
            //方法一  
            ptdate = sdf.format(ts);  
        }catch (Exception e) {
        	ptdate="";	
        }
        if (ptdate.length()>=10)
           ptdate=ptdate.substring(0, 10);
        
	}


	
		
}
