package com.shellshellfish.aaas.common;

import com.shellshellfish.aaas.common.configuration.JpaConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;


@Import(JpaConfiguration.class)
@SpringBootApplication(scanBasePackages={"com.shellshellfish.aaas.common"})// same as @Configuration @EnableAutoConfiguration @ComponentScan
public class UserInfoApp {

	public static void main(String[] args) {
		SpringApplication.run(UserInfoApp.class, args);
	}
}
