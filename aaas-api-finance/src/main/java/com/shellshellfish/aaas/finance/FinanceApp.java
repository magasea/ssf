package com.shellshellfish.aaas.finance;


import com.shellshellfish.aaas.finance.configuration.Properties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.client.RestTemplate;


@ComponentScan
@SpringBootApplication(scanBasePackages = {"com.shellshellfish.aaas.finance"})
@EnableAutoConfiguration(exclude = {MongoAutoConfiguration.class})
@EnableConfigurationProperties({Properties.class})
public class FinanceApp {
    public static void main(String[] args) {
        SpringApplication.run(FinanceApp.class, args);
    }
}
