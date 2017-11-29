package com.shellshellfish.aaas.account.service.impl;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import com.shellshellfish.aaas.account.body.VerificationBody;
import com.shellshellfish.aaas.account.service.RedisService;
import redis.clients.jedis.Jedis;

public class RedisServiceimpl implements RedisService{
	
	public static final Logger logger = LoggerFactory.getLogger(RedisServiceimpl.class);
	
	@Autowired  
	private Environment env;  
	
	//public static String redis_server = "192.168.1.10"; //redis default server ip
	//public static int redis_port=6379 ;
	public static Jedis jedis;	
	public static boolean initflag=false;
	
	
	public boolean connectServer() {
		
		
		jedis = new Jedis(env.getProperty("redis.host"),Integer.parseInt(env.getProperty("redis.port"))); 
    	jedis.auth(env.getProperty("redis.pwd"));
        
        System.out.println("Connection to redis server sucessfully"); 
        //check whether server is running or not 
        System.out.println("Server is running: "+jedis.ping());
		return true;
	}
	
	public boolean saveVeribody(VerificationBody vbody) throws RuntimeException {
		if (initflag==false) {
			connectServer();
			initflag=true;
		}
		
		HashMap<String,String> vmap=new HashMap<String,String>();
		vmap.put("telnum", vbody.getTelnum());
		vmap.put("identifyingcode", vbody.getIdentifyingcode());
		jedis.hmset(vbody.getTelnum(), vmap);
		jedis.expire(vbody.getTelnum(), 60); //expired time :1 min
		return true;
		
	}
	
	
	public  boolean doSmsVerification(VerificationBody vbody) throws RuntimeException {
		if (initflag==false) {
			connectServer();
			initflag=true;
		}
		
		try {
	       String vericode=jedis.hget(vbody.getTelnum(), "identifyingcode");
	       if (vericode!=null && vericode.equals(vbody.getIdentifyingcode()))
		       return true;
	   }catch (Exception e) {
		   logger.debug(e.getMessage());
		   return false;
	   }
	   
	   return false;
	}
	
}
