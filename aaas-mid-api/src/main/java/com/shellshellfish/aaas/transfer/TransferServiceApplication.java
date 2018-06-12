/**
 * 
 */
/**
 * @author chenwei
 *
 */
package com.shellshellfish.aaas.transfer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@EnableAutoConfiguration
@EntityScan(basePackages={"com.shellshellfish.aaas.transfer"})
@SpringBootApplication(scanBasePackages={"com.shellshellfish.aaas.transfer"})

public class TransferServiceApplication{
	
	public static void main(String[] args){
		SpringApplication.run(TransferServiceApplication.class, args);
	}
	
	
}