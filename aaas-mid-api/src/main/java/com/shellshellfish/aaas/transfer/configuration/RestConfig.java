package com.shellshellfish.aaas.transfer.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Created by chenwei on 2018- 四月 - 09
 */
@Configuration
public class RestConfig {

  @Bean
  RestTemplate restTemplate(){
    return new RestTemplate();
  }
}
