package com.shellshellfish.datamanager.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shellshellfish.datamanager.model.DailyFunds;
import com.shellshellfish.datamanager.model.FundCodes;
import com.shellshellfish.datamanager.repositories.MongoDailyFundsRepository;
import com.shellshellfish.datamanager.repositories.MongoFundCodesRepository;

@Service
public class DataServiceImpl implements DataService {
	
	@Autowired
	MongoFundCodesRepository mongoFundCodesRepository;
	@Autowired
	MongoDailyFundsRepository mongoDailyFundsRepository;
	
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
	
	public List<DailyFunds>  getDailyFunds(String[] codelist,String date){
		//String[] abc= new String[] {"000001.OF","000003.OF"};
		return mongoDailyFundsRepository.findByCodeAndDate(codelist, date);
	}
	
}