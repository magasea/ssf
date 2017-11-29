package com.shellshellfish.aaas.account;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;  

@EnableAutoConfiguration
@EntityScan(basePackages = { "com.shellshellfish.aaas.account" })
@SpringBootApplication(scanBasePackages={"com.shellshellfish.aaas.account"})
public class AccountServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AccountServiceApplication.class, args);
		
		/*
		String msg=AliSms.sendVerificationSms("15026646271");
		System.out.println(msg);
		*/
		
	}
	
	
     	
}
