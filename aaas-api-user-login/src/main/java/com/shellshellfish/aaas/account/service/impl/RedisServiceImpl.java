package com.shellshellfish.aaas.account.service.impl;

import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import com.shellshellfish.aaas.account.model.dto.VerificationBodyDTO;
import com.shellshellfish.aaas.account.service.RedisService;
import redis.clients.jedis.Jedis;

public class RedisServiceImpl implements RedisService {

	public static final Logger logger = LoggerFactory.getLogger(RedisServiceImpl.class);

	public static final String LOGIN = "Login";

	@Autowired
	private Environment env;

	// public static String redis_server = "192.168.1.10"; //redis default
	// server ip
	// public static int redis_port=6379 ;
	public static Jedis jedis;
	public static boolean initflag = false;

	public boolean connectServer() {

		jedis = new Jedis(env.getProperty("redis.host"), Integer.parseInt(env.getProperty("redis.port")));
		jedis.auth(env.getProperty("redis.pwd"));

		System.out.println("Connection to redis server sucessfully");
		// check whether server is running or not
		System.out.println("Server is running: " + jedis.ping());
		return true;
	}

	@Override
	public boolean saveVeribody(VerificationBodyDTO vbody){
		if (initflag == false) {
			connectServer();
			initflag = true;
		}

		HashMap<String, String> vmap = new HashMap<String, String>();
		vmap.put("telnum", vbody.getTelnum());
		vmap.put("identifyingcode", vbody.getIdentifyingcode());
		jedis.hmset(vbody.getTelnum(), vmap);
		jedis.expire(vbody.getTelnum(), 60); // expired time :1 min
		return true;

	}

	@Override
	public boolean doSmsVerification(VerificationBodyDTO vbody) {
		if (initflag == false) {
			connectServer();
			initflag = true;
		}

		try {
			String vericode = jedis.hget(vbody.getTelnum(), "identifyingcode");
			if (vericode != null && vericode.equals(vbody.getIdentifyingcode()))
				return true;
		} catch (Exception e) {
			logger.debug(e.getMessage());
			return false;
		}

		return false;
	}

	@Override
	public boolean doPwdSave(String cellphone, String passwordhash) {
		if (initflag == false) {
			connectServer();
			initflag = true;
		}

		Map<String, String> vmap = new HashMap<String, String>();
		vmap.put("telnum", cellphone);
		vmap.put("password", passwordhash);
		jedis.hmset(LOGIN + cellphone, vmap);
		return true;
	}

	@Override
	public String doGetPwd(String cellphone, String passwordhash) {
		if (initflag == false) {
			connectServer();
			initflag = true;
		}
		String password = jedis.hget(LOGIN + cellphone, "password");
		if (password != null && password.equals(passwordhash)) {
			return password;
		} else {
			return null;
		}
	}

	@Override
	public void doLogout(String cellphone, String passwordhash) {
		if (initflag == false) {
			connectServer();
			initflag = true;
		}
		try {
			String telnum = jedis.hget(LOGIN + cellphone, "telnum");
			if (telnum != null) {
				jedis.del(LOGIN + cellphone);
			}
		} catch (Exception e) {
			logger.debug(e.getMessage());
		}
	}

}
