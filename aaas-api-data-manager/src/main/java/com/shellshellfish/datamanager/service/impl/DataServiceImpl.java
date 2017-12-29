package com.shellshellfish.datamanager.service.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import com.shellshellfish.datamanager.service.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shellshellfish.datamanager.commons.DataConvertUtils;
import com.shellshellfish.datamanager.commons.DateUtil;
import com.shellshellfish.datamanager.model.DailyFunds;
import com.shellshellfish.datamanager.model.FundCodes;
import com.shellshellfish.datamanager.model.FundCompanys;
import com.shellshellfish.datamanager.model.FundManagers;
import com.shellshellfish.datamanager.model.FundYearIndicator;
import com.shellshellfish.datamanager.model.FundYeildRate;
import com.shellshellfish.datamanager.model.IndicatorPoint;



import com.shellshellfish.datamanager.repositories.MongoDailyFundsRepository;
import com.shellshellfish.datamanager.repositories.MongoFundCodesRepository;
import com.shellshellfish.datamanager.repositories.MongoFundCompanysRepository;
import com.shellshellfish.datamanager.repositories.MongoFundManagersRepository;
import com.shellshellfish.datamanager.repositories.MongoFundYearIndicatorRepository;
import com.shellshellfish.datamanager.repositories.MongoListedFundCodesRepository;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
@Service
public class DataServiceImpl implements DataService {
	
	@Autowired
	MongoFundCodesRepository mongoFundCodesRepository;
	
	@Autowired
	MongoListedFundCodesRepository mongoListedFundCodesRepository;
	
	@Autowired
	MongoDailyFundsRepository mongoDailyFundsRepository;
	
	@Autowired
	MongoFundManagersRepository mongoFundManagersRepository;
	
	@Autowired
	MongoFundCompanysRepository mongoFundCompanysRepository;
	
	@Autowired
	MongoFundYearIndicatorRepository mongoFundYearIndicatorRepository;
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
	/*
	public boolean saveDailytoDBforday(String codelist,String querydate) {
		return callpython("./getdaily.sh"+" "+codelist+" "+querydate);
	}
	
	public boolean saveDailytoDBfordays(String codelist,String fromdate,String todate) {
	    return 	callpython("./getdailybatch.sh"+" "+codelist+" "+fromdate+" "+todate);
	}
	
	public boolean saveAllfundCodestoDB() {
		return callpython("./getallfundcodes.sh");
	}*/
	/*
	public boolean callpython (String command){
		BufferedReader bf;
        bf= null;  
        Process proc = null;  
        try{  
            
        	proc =Runtime.getRuntime().exec(command,null,new File("/work/wtwong/workspace/fundsimportdata/api"));
        	proc.waitFor();//如果 pyhton app有大量print输出，将会导致程序wait_time_out
        	int exitcode=proc.exitValue();
        	
        	if (exitcode!=0)
        	   bf = new BufferedReader(new InputStreamReader(proc.getErrorStream()));
        	//p = run.exec(command,null,new File("/work/wtwong/workspace/choice/api"));  
        	else
        		bf = new BufferedReader(new InputStreamReader(proc.getInputStream()));  
            
        	String msg = null;  
            
            while((msg = bf.readLine()) != null){  
                System.out.println(msg);  
            }  
            System.out.println("exit code:"+exitcode);
            if (exitcode==0)
            	return true;
            
        }catch(Exception e){  
            e.printStackTrace();  
        }finally{  
            if(bf != null){  
                  
                try {  
                    bf.close();  
                    proc.destroy();  
                } catch (IOException e) {  
                    // TODO Auto-generated catch block  
                    e.printStackTrace();  
                }  
            }
            
        }  
        return false;

	}*/
	
	public List<FundCodes>  getAllFundCodes(){
		//List<FundCodes> ftl=mongoFundCodesRepository.findByCodeAndDate("000001.OF","2017-11-12");
		return mongoFundCodesRepository.findAll();
	}
	
	//基金概况
	public List<DailyFunds>  getDailyFundsBycode(String[] codelist){
		//String[] abc= new String[] {"000001.OF","000003.OF"};
		for (int i=0;i<codelist.length;i++) {
			if (!codelist[i].contains("OF") && !codelist[i].contains("SH") && !codelist[i].contains("SZ"))
				codelist[i]=codelist[i]+".OF";
		}
		
		Criteria criteria = Criteria.where("code").in(codelist);
		Query query = new Query(criteria);
		query.with(new Sort(Sort.DEFAULT_DIRECTION.DESC,"querydate")); 
		List<DailyFunds> list = mongoTemplate.find(query, DailyFunds.class);
		
		if (list==null || list.size()==0)
			return new ArrayList<DailyFunds>();
		
		List<DailyFunds> toplst= new ArrayList<DailyFunds>();
		toplst.add(list.get(0));
		
		return toplst;
	}
	
	public List<IndicatorPoint>  getMaxfallPeriod(String code,long fromtime,long totime){
		
		fromtime=fromtime-18000; //diff in python and java
		totime=totime-18000;
		Criteria criteria = Criteria.where("code").is(code).and("querydate").gte(fromtime).lte(totime);
		Query query = new Query(criteria);
		List<DailyFunds> list = mongoTemplate.find(query, DailyFunds.class);
		 
		List<IndicatorPoint> ptlst=DataConvertUtils.getIndFromDailyData(list);
		
		return ptlst;
	}
	
	
	public HashMap<String,Object>  getFundManager(String name){
		HashMap<String,Object> fmmap=null;
		List<FundManagers> lst= mongoFundManagersRepository.findByManagername(name);
		if (lst==null || lst.size()==0)
			return new HashMap<String,Object>();
		else {
			fmmap=new HashMap<String,Object>();
			fmmap.put("manager", lst.get(0).getMnager());
			fmmap.put("avgearningrate", lst.get(0).getAvgearningrate());
			fmmap.put("workingdays", lst.get(0).getWorkingdays());
			fmmap.put("fundnum",lst.size());
			HashMap[] dmap=new HashMap[lst.size()];
			for (int i=0;i<lst.size();i++) {
				 dmap[i]=new HashMap<String,String>();
				 String jobstr=lst.get(i).getFundname()+"|||"+lst.get(i).getStartdate()+"|||"+lst.get(i).getEarningrate();
				 dmap[i].put("jobitem",jobstr);
				 
			}
			fmmap.put("joblist",dmap);
			
		}
		
		return fmmap;
		
	}
		
	//基金公司信息
	public HashMap<String,Object>  getFundCompany(String name){
		HashMap<String,Object> fmmap=null;
		List<FundCompanys> lst= mongoFundCompanysRepository.findByCompanyname(name);
		if (lst==null || lst.size()==0)
			return new HashMap<String,Object>();
		else {
			fmmap=new HashMap<String,Object>();
			fmmap.put("fundcompany", lst.get(0).getCompanyname());
			fmmap.put("scale", lst.get(0).getScale());
			fmmap.put("fundnum",lst.size());
			HashMap[] dmap=new HashMap[lst.size()];
			String[] codes=new String[lst.size()];
			
			for (int i=0;i<lst.size();i++) {
				 dmap[i]=new HashMap<String,String>();
				 String itemstr=lst.get(i).getFundname()+"|||"+lst.get(i).getFundtype();//+"|||"+getYearscale(lst.get(i).getCode()); //还需要一个年化收益率
				 dmap[i].put("funditem",itemstr);
				 dmap[i].put("code", lst.get(i).getCode());
				 codes[i]=lst.get(i).getCode();
			}
			
			List<FundYearIndicator> yearindilst=getYearscale(codes);
			if (yearindilst!=null) {
		       for (int i=0;i<dmap.length;i++) {
		    	   String code=(String)dmap[i].get("code");
		    	   String val=getnavaccumreturnpFromlist(yearindilst,code); //区间累计单位净值增长率
		    	   String funditem=(String)dmap[i].get("funditem");
		    	   funditem=funditem+"|||"+val;
		    	   dmap[i].put("funditem",funditem);
		       }
			}
			fmmap.put("fundlist",dmap);
			
		}
		
		return fmmap;
		
	}
	
	
	public String  getnavaccumreturnpFromlist(List<FundYearIndicator> list,String code ) {
		for (int i=0;i<list.size();i++) {
			if (code.equals(list.get(i).getCode()))
				return 	list.get(i).getNavaccumreturnp();
		}
		
		return "none";  //if here,not found
	}
	
	
	//不分场内和场外基金,使用同一的计算方式：
	
	//unsued:场内基金(SH,SZ):区间涨跌幅
	//unused:场外基金(OF):区间复权单位净值增长率
	
	public List<FundYearIndicator> getYearscale(String[] codes) {
		
		
		Date d=new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String sdate=sdf.format(d);
		String yearstr=sdate.substring(0, 4);
		String stdate=yearstr+"-01-01";
		
		String enddate=yearstr+"-12-31";
		long sttime=0;
		long endtime=0;
		try {
			
			Date stdated = sdf.parse(stdate);
			
	        Calendar cal = Calendar.getInstance();
	        cal.setTime(stdated);//date 换成已经已知的Date对象
	        //cal.add(Calendar.HOUR_OF_DAY, -8);// before 8 hour (GMT 8)
	        Date e=cal.getTime();
	        sttime=e.getTime()/1000;
	        
	        Date enddated= sdf.parse(enddate);
	        
	        cal.setTime(enddated);
	        //cal.add(Calendar.HOUR_OF_DAY, -8);// before 8 hour (GMT 8)
	        e=cal.getTime();
	        endtime=e.getTime()/1000;
	        
	        //sttime=sttime-18000; //diff in python and java
			//endtime=endtime-18000;
		}catch (Exception e)
		{
			System.out.println(e.getMessage());
			return null;
		}		
		List<FundYearIndicator> lcodelst=mongoFundYearIndicatorRepository.findByCodeAndQuerydate(codes,sttime,endtime);
		
		return lcodelst;
	}
		
    
	/*
	public String  getDiffValueBylistedcode(String code){
		
		Criteria criteria = Criteria.where("code").is(code);
		Query query = new Query(criteria);
		query.with(new Sort(Sort.DEFAULT_DIRECTION.DESC,"queryenddate")); 
		
		List<RangeIndicator> list = mongoTemplate.find(query, RangeIndicator.class);
		if (list==null || list.size()==0)
			return "none"; //取不到,bug
		
		return list.get(0).getDifferrangep();
		
	}
	
    public String  getDiffValueByofcode(String code){
		
		Criteria criteria = Criteria.where("code").is(code);
		Query query = new Query(criteria);
		query.with(new Sort(Sort.DEFAULT_DIRECTION.DESC,"queryenddate")); 
		
		List<OffundYeildRate> list = mongoTemplate.find(query, OffundYeildRate.class);
		if (list==null || list.size()==0)
			return "none"; //取不到,bug
		
		return list.get(0).getNavadjreturnp();
		
	}*/
	
    //历史净值
	public HashMap<String,Object> getHistoryNetvalue(String code,String type,String settingdate){
		
		if (!code.contains("OF") && !code.contains("SH") && !code.contains("SZ"))
			code=code+".OF";
		
		HashMap<String,Object> hnmap=new HashMap<String,Object>();
		hnmap.put("code", code);
		hnmap.put("period", type);
		
		//get current date as todate
		
		Date stdate=null;
		Date enddate=null;
		long sttime=0L;
		long endtime=0L;
		
		try
		{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			
			enddate = sdf.parse(settingdate);
			endtime=enddate.getTime()/1000; //seconds
			
			GregorianCalendar gc=new GregorianCalendar();
            gc.setTime(enddate);
            if (type.equals("1"))
               gc.add(2, -Integer.parseInt("3"));//3 month
            else if (type.equals("2"))
               gc.add(2, -Integer.parseInt("6"));//6 month
            else if (type.equals("3"))
                gc.add(2, -Integer.parseInt("12"));//1 year
            else 
                gc.add(2, -Integer.parseInt("36"));//3 year
             
            
            System.out.println(new SimpleDateFormat("yyyy-MM-dd").format(gc.getTime()));
            sttime=gc.getTime().getTime()/1000; //seconds
        
			
		}
		catch (Exception e)
		{
			System.out.println(e.getMessage());
			return new HashMap<String,Object>();
		}	
		
		Criteria criteria = Criteria.where("code").is(code).and("querydate").gte(sttime).lte(endtime);
		Query query = new Query(criteria);
		query.with(new Sort(Sort.DEFAULT_DIRECTION.DESC,"querydate")); 
		List<FundYearIndicator> list = mongoTemplate.find(query, FundYearIndicator.class);
		
		if (list==null || list.size()==0)
			return new HashMap<String,Object>();
		
		HashMap[] dmap=new HashMap[list.size()];
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
		
		
		for (int i=0;i<list.size();i++) {
			 dmap[i]=new HashMap<String,String>();
			 String qd=sdf.format(new Date(list.get(i).getQuerydate()*1000));
			 double navunit=list.get(i).getNavunit();
			 double navaccum=list.get(i).getNavaccum();
			 dmap[i].put("date",qd);
			 dmap[i].put("navunit",navunit);
			 dmap[i].put("navaccum",navaccum);
			 double dayup=0.0;
			 String sdayup="0.00";
			 try {
			      double d1=navunit;
			      
			      if (i!=list.size()-1) {
			    	  double d2=list.get(i+1).getNavunit();
			    	  dayup= (d1-d2)/d2*100; 
			    	  sdayup=String.format("%.2f", dayup);
			      }
			 }catch (NumberFormatException e)
			 {
				 e.getMessage();
				 dayup=0;
			 }
			 dmap[i].put("dayup",sdayup+"%");
		}
		hnmap.put("historylist",dmap);
		return hnmap;
				
	}
	
	//日涨幅,近一年涨幅,净值,分级类型,评级
	
	public HashMap<String,Object> getFundValueInfo(String code,String date){
		Date stdate=null;
		Date enddate=null;
		long sttime=0L;
		long endtime=0L;
		
		if (!code.contains("OF") && !code.contains("SH") && !code.contains("SZ"))
			code=code+".OF";
		
		HashMap<String,Object> hnmap=new HashMap<String,Object>();
		hnmap.put("code", code);
		hnmap.put("net", "3.5");//净值
		hnmap.put("classtype", "2");//分级类型
		hnmap.put("rate", "4");//评级
		
		HashMap[] dmap=new HashMap[8];
		
		try
		{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			stdate = sdf.parse(date);
			sttime=stdate.getTime()/1000; //seconds
			
			
		}
		catch (Exception e)
		{
			System.out.println(e.getMessage());
			return null;
		}	
		
		List<FundYearIndicator> lst=mongoFundYearIndicatorRepository.getHistoryNetByCodeAndQuerydate(code,sttime);
		double curdayval=0;
		double yesval=0;
	
		double dayup=0;
		double weekup=0;
		double monthup=0;
		double threemonthup=0;
		double sixmonthup=0;
		double oneyearup=0;
		double threeyearup=0;
		double thisyearup=0;
		if (lst!=null && lst.size()==1) {
		    curdayval=lst.get(0).getNavadj();
			dayup=getUprate(code,curdayval,stdate,1); //a day ago
			weekup=getUprate(code,curdayval,stdate,2); //a week ago
			monthup=getUprate(code,curdayval,stdate,3); //a month ago
			threemonthup=getUprate(code,curdayval,stdate,4); //3 month ago
			sixmonthup=getUprate(code,curdayval,stdate,5); //6 month ago
			oneyearup=getUprate(code,curdayval,stdate,6); //1 year ago
			threeyearup=getUprate(code,curdayval,stdate,7); //3 year ago
			thisyearup=getUprate(code,curdayval,stdate,8); //this year ago
		}
		
		dmap[0].put("time","日涨幅");
		dmap[0].put("val",dayup);
		
		dmap[1].put("time","近一周");
		dmap[1].put("val",weekup);
		
		dmap[2].put("time","近一月");
		dmap[2].put("val",monthup);
		
		dmap[3].put("time","近三月");
		dmap[3].put("val",threemonthup);
		
		dmap[4].put("time","近六月");
		dmap[4].put("val",sixmonthup);
		
		dmap[5].put("time","近一年");
		dmap[5].put("val",oneyearup);
		
		dmap[6].put("time","近三年");
		dmap[6].put("val",threeyearup);
		
		dmap[7].put("time","今年来");
		dmap[7].put("val",thisyearup);
		
		hnmap.put("uplist",dmap);
		
		return hnmap;
	}
	
	public double getUprate(String code,double curval,Date curdate,int type) {
		Date befdate=null;
		if (type==1)
		   befdate=DateUtil.addDays(curdate, -1);//昨天日期
		else if (type==2)
		   befdate=DateUtil.addDays(curdate, -7);//前一周 
		else if (type==3)
			befdate=DateUtil.addDays(curdate, -30);//前一月
		else if (type==4)
			befdate=DateUtil.addDays(curdate, -90);//前三月
		else if (type==5)
			befdate=DateUtil.addDays(curdate, -180);//前六月
		else if (type==6)
			befdate=DateUtil.addDays(curdate, -365);//前一年
		else if (type==7)
			befdate=DateUtil.addDays(curdate, -365*3);//前三年
		else if (type==8) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String thisyear=sdf.format(curdate).substring(0,3);
			try {
			    befdate=sdf.parse(thisyear+"-01-01");
			}catch (ParseException e) {
				e.printStackTrace();
				return 0;
			}
			
		}
		    
		
        long endtime=befdate.getTime()/1000; //seconds
		
		double yesval=0;
		List<FundYearIndicator> yeslst=mongoFundYearIndicatorRepository.getHistoryNetByCodeAndQuerydate(code,endtime);
		if (yeslst!=null && yeslst.size()==1) {
			yesval=yeslst.get(0).getNavadj();
		}
		double up=0;
		if (yesval!=0)
			up=(curval-yesval)/yesval;
		
	    return up;
	}
	
	
	
}