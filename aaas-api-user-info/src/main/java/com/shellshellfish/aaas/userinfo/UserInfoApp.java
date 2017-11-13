package com.shellshellfish.aaas.userinfo;



import org.springframework.boot.SpringApplication;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;


@EnableAutoConfiguration
@EntityScan(basePackages = { "com.shellshellfish.aaas.userinfo" })
@SpringBootApplication(scanBasePackages={"com.shellshellfish.aaas.userinfo"})// same as
// @Configuration @EnableAutoConfiguration @ComponentScan
public class UserInfoApp {

	public static void main(String[] args) {
		SpringApplication.run(UserInfoApp.class, args);
	}
}
