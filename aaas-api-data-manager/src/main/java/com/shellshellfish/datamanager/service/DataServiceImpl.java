package com.shellshellfish.datamanager.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;


import org.hibernate.mapping.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shellshellfish.datamanager.commons.DataConvertUtils;
import com.shellshellfish.datamanager.model.DailyFunds;
import com.shellshellfish.datamanager.model.FundCodes;
import com.shellshellfish.datamanager.model.FundCompanys;
import com.shellshellfish.datamanager.model.FundManagers;
import com.shellshellfish.datamanager.model.IndicatorPoint;
import com.shellshellfish.datamanager.repositories.MongoDailyFundsRepository;
import com.shellshellfish.datamanager.repositories.MongoFundCodesRepository;
import com.shellshellfish.datamanager.repositories.MongoFundCompanysRepository;
import com.shellshellfish.datamanager.repositories.MongoFundManagersRepository;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
@Service
public class DataServiceImpl implements DataService {
	
	@Autowired
	MongoFundCodesRepository mongoFundCodesRepository;
	@Autowired
	MongoDailyFundsRepository mongoDailyFundsRepository;
	
	@Autowired
	MongoFundManagersRepository mongoFundManagersRepository;
	
	@Autowired
	MongoFundCompanysRepository mongoFundCompanysRepository;
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
	public boolean saveDailytoDBforday(String codelist,String querydate) {
		return callpython("./getdaily.sh"+" "+codelist+" "+querydate);
	}
	
	public boolean saveDailytoDBfordays(String codelist,String fromdate,String todate) {
	    return 	callpython("./getdailybatch.sh"+" "+codelist+" "+fromdate+" "+todate);
	}
	
	public boolean saveAllfundCodestoDB() {
		return callpython("./getallfundcodes.sh");
	}
	
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

	}
	
	public List<FundCodes>  getAllFundCodes(){
		//List<FundCodes> ftl=mongoFundCodesRepository.findByCodeAndDate("000001.OF","2017-11-12");
		return mongoFundCodesRepository.findAll();
	}
	
	public List<DailyFunds>  getDailyFundsBycode(String[] codelist){
		//String[] abc= new String[] {"000001.OF","000003.OF"};
		Criteria criteria = Criteria.where("code").in(codelist);
		Query query = new Query(criteria);
		query.with(new Sort(Sort.DEFAULT_DIRECTION.DESC,"querydate")); 
		List<DailyFunds> list = mongoTemplate.find(query, DailyFunds.class);
		
		if (list==null || list.size()==0)
			return null;
		
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
			for (int i=0;i<lst.size();i++) {
				 dmap[i]=new HashMap<String,String>();
				 String jobstr=lst.get(i).getFundname()+"|||"+lst.get(i).getFundtype()+"|||"+getYearscale(lst.get(i).getCode()); //还需要一个年化收益率
				 dmap[i].put("funditem",jobstr);
				 
			}
			fmmap.put("fundlist",dmap);
			
		}
		
		return fmmap;
		
	}
	
	
	//非货币基金:区间复权单位净值增长率
	//货币基金:区间涨跌幅
	public String getYearscale(String code) {
		return "";
	}
		
	
}