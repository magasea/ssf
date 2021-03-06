package com.shellshellfish.aaas.finance.trade.pay.config;

/**
 * Created by chenwei on 2018- 二月 - 01
 */

import com.shellshellfish.aaas.finance.trade.pay.model.WorkDayRedis;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
@Configuration
@ComponentScan("com.shellshellfish.aaas.finance.trade.pay")
public class RedisConfig {

  @Value("${spring.redis.host}")
  String redisHost;

  @Value("${spring.redis.port}")
  int port;

  @Value("${spring.redis.password}")
  String password;

  @Bean
  public LettuceConnectionFactory redisConnectionFactory(){
    RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
    redisStandaloneConfiguration.setHostName(redisHost);
    redisStandaloneConfiguration.setPort(port);
    redisStandaloneConfiguration.setPassword(RedisPassword.of(password));
    LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory(redisStandaloneConfiguration);
    return lettuceConnectionFactory;
  }


  //  @Bean
//  public RedisConnectionFactory redisConnectionFactory() {
//    JedisPoolConfig poolConfig = new JedisPoolConfig();
//    poolConfig.setMaxTotal(100);
//    poolConfig.setTestOnBorrow(true);
//    poolConfig.setTestOnReturn(true);
//
//    JedisConnectionFactory connectionFactory = new JedisConnectionFactory(poolConfig);
//    connectionFactory.setUsePool(true);
//    connectionFactory.setHostName(redisHost);
//    connectionFactory.setPort(port);
//    connectionFactory.setPassword(password);
//
//    return connectionFactory;
//  }
  @Bean
  public RedisTemplate<String, WorkDayRedis> redisTemplate() {
    RedisTemplate<String, WorkDayRedis> redisTemplate = new RedisTemplate<>();
    redisTemplate.setConnectionFactory(redisConnectionFactory());
    redisTemplate.setEnableTransactionSupport(true);
    return redisTemplate;
  }
  @Bean
  public StringRedisTemplate stringRedisTemplate() {
    StringRedisTemplate stringRedisTemplate = new StringRedisTemplate(redisConnectionFactory());
    stringRedisTemplate.setEnableTransactionSupport(true);
    return stringRedisTemplate;
  }
}
