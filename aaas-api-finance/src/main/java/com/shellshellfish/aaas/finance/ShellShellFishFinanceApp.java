package com.shellshellfish.aaas.finance;


import com.shellshellfish.aaas.finance.configuration.StaticResourceConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;


//@Import(com.shellshellfish.aaas.configuration.JpaConfiguration.class)
@Import(StaticResourceConfiguration.class)
@SpringBootApplication(scanBasePackages={"com.shellshellfish.aaas.finance"})// same as @Configuration @EnableAutoConfiguration @ComponentScan
public class ShellShellFishFinanceApp {

	public static void main(String[] args) {
		SpringApplication.run(ShellShellFishFinanceApp.class, args);
	}
}
