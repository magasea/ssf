package com.shellshellfish.aaas.finance;


import com.shellshellfish.aaas.finance.configuration.JpaConfiguration;
import com.shellshellfish.aaas.finance.configuration.StaticResourceConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;


@EnableAutoConfiguration
@ComponentScan
@SpringBootApplication(scanBasePackages={"com.shellshellfish.aaas.finance"})// same as @Configuration @EnableAutoConfiguration @ComponentScan
public class FinanceApp {

	public static void main(String[] args) {
		SpringApplication.run(FinanceApp.class, args);
	}
}
