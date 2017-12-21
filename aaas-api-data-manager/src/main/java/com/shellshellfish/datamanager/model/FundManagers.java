package com.shellshellfish.datamanager.model;
import javax.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
@Document(collection = "fundmanagers")
public class FundManagers {
	
	@Id
	private String id;
	@Field("基金经理")
	private String manager; //基金经理
	@Field("算术平均年化收益率(%)")
	private double avgearningrate; //算术平均年化收益率(%)
	@Field("任职天数")
	private String workingdays; //任职天数
	
	@Field("名称")
	private String fundname; //基金名称
	
	@Field("任职日期")
	private String startdate; //任职日期
	
	@Field("任职以来回报(%)")
	private double earningrate; //任职以来回报
	
	public String getMnager() {
	    return manager;
	}

	public void setManager(String manager) {
	    this.manager = manager;
	}


	public double getAvgearningrate() {
	    return avgearningrate;
	}

	public void setAvgearningrate(double earningrate) {
	    this.avgearningrate = earningrate;
	}


	public String getWorkingdays() {
	    return workingdays;
	}

	public void setWorkingdays(String days) {
	    this.workingdays = days;
	}
	
	public String getFundname() {
	    return fundname;
	}

	public void setFundname(String fundname) {
	    this.fundname = fundname;
	}
	
	public String getStartdate() {
	    return startdate;
	}

	public void setStartdate(String Startdate) {
	    this.startdate = startdate;
	}
	
	public double getEarningrate() {
	    return earningrate;
	}

	public void setEarningrate(double earningrate) {
	    this.earningrate = earningrate;
	}

}
