package com.shellshellfish.aaas.tools.fundcheck;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan("com.shellshellfish.aaas.tools.fundcheck")
public class FundCheckServiceApplication {

	public static void main(String[] args) {
	     SpringApplication.run(FundCheckServiceApplication.class, args);
	}
	
	
     	
}
