package com.shellshellfish.aaas;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;


@Import(com.shellshellfish.aaas.configuration.JpaConfiguration.class)
@SpringBootApplication(scanBasePackages={"com.shellshellfish.aaas"})// same as @Configuration @EnableAutoConfiguration @ComponentScan
public class SpringBootCRUDApp {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootCRUDApp.class, args);
	}
}
