package com.shellshellfish.aaas.userinfo;

import com.shellshellfish.aaas.userinfo.configuration.JpaConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;


@Import(JpaConfiguration.class)
@SpringBootApplication(scanBasePackages={"com.shellshellfish.aaas.userinfo"})// same as @Configuration @EnableAutoConfiguration @ComponentScan
public class UserInfoApp {

	public static void main(String[] args) {
		SpringApplication.run(UserInfoApp.class, args);
	}
}
