package com.shellshellfish.aaas;

import com.shellshellfish.aaas.oeminfo.configuration.JpaConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;


@Import(JpaConfiguration.class)
@SpringBootApplication(scanBasePackages={"com.shellshellfish.aaas.oeminfo"})// same as
// @Configuration @EnableAutoConfiguration @ComponentScan
public class AaasOEMInfoApp {

	public static void main(String[] args) {
		SpringApplication.run(AaasOEMInfoApp.class, args);
	}
}
