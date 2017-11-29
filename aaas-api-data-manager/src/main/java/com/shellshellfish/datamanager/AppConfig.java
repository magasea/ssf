package com.shellshellfish.datamanager;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.shellshellfish.datamanager.service.DataService;
import com.shellshellfish.datamanager.service.DataServiceImpl;


@Configuration
public class AppConfig {
	
	@Bean
	public DataService DataService() {
		return new DataServiceImpl();
	}
	
}
