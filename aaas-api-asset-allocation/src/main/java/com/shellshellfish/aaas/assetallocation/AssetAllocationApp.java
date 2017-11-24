package com.shellshellfish.aaas.assetallocation;



import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;

import com.shellshellfish.aaas.assetallocation.configuration.Properties;


@ComponentScan
@SpringBootApplication(scanBasePackages={"com.shellshellfish.aaas.assetallocation"})
@EnableAutoConfiguration(exclude={MongoAutoConfiguration.class})
@EnableConfigurationProperties({ Properties.class })
public class AssetAllocationApp {

	public static void main(String[] args) {
		SpringApplication.run(AssetAllocationApp.class, args);
	}
}
