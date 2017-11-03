package com.shellshellfish.finance;


import com.shellshellfish.finance.configuration.StaticResourceConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;


//@Import(com.shellshellfish.aaas.configuration.JpaConfiguration.class)
@Import(StaticResourceConfiguration.class)
@SpringBootApplication(scanBasePackages={"com.shellshellfish.finance"})// same as @Configuration @EnableAutoConfiguration @ComponentScan
public class SpringBootCRUDApp {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootCRUDApp.class, args);
	}
}
