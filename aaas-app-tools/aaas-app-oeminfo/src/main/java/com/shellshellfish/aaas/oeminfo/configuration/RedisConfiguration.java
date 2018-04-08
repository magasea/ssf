package com.shellshellfish.aaas.oeminfo.configuration;

import com.fasterxml.jackson.databind.ser.std.MapSerializer;
import com.shellshellfish.aaas.oeminfo.model.User;
import java.util.Map;
import javax.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializationContext.RedisSerializationContextBuilder;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Created by chenwei on 2018- 四月 - 03
 */

public class RedisConfiguration {
  @Autowired
  RedisConnectionFactory factory;

  @Value("${spring.redis.host}")
  String host;


  @Value("${spring.redis.port}")
  int port;

  @Bean
  public LettuceConnectionFactory redisConnectionFactory() {
    LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory(host, port);
    return lettuceConnectionFactory;
  }

  @Bean
  public ReactiveRedisConnectionFactory reactiveRedisConnectionFactory(){
    return redisConnectionFactory();
  }

  /**
   * Configures a {@link ReactiveRedisTemplate} with {@link String} keys and values.
   */
  @Bean
  public ReactiveRedisTemplate<String, String> reactiveRedisTemplate(ReactiveRedisConnectionFactory connectionFactory) {
    return new ReactiveRedisTemplate<>(connectionFactory, RedisSerializationContext.string());
  }

  /**
   * Configures a {@link ReactiveRedisTemplate} with {@link String} keys and a typed
   * {@link Jackson2JsonRedisSerializer}.
   */
//  @Bean
//  public ReactiveRedisTemplate<String, User> reactiveJsonPersonRedisTemplate(
//      ReactiveRedisConnectionFactory connectionFactory) {
//
//    Jackson2JsonRedisSerializer<User> serializer = new Jackson2JsonRedisSerializer<>(User.class);
//    RedisSerializationContextBuilder<String, User> builder = RedisSerializationContext
//        .newSerializationContext(new StringRedisSerializer());
//
//    RedisSerializationContext<String, User> serializationContext = builder.value(serializer).build();
//
//    return new ReactiveRedisTemplate<>(connectionFactory, serializationContext);
//  }

  /**
   * Configures a {@link ReactiveRedisTemplate} with {@link String} keys and a typed
   * {@link Jackson2JsonRedisSerializer}.
   */
//  @Bean
//  public ReactiveRedisTemplate<String, Map<String, String>> reactiveMapRedisTemplate(
//      ReactiveRedisConnectionFactory connectionFactory) {
//
//    Jackson2JsonRedisSerializer<Map> serializer = new Jackson2JsonRedisSerializer<Map>(Map.class);
//    RedisSerializationContextBuilder<String, Map> builder = RedisSerializationContext
//        .newSerializationContext(new MapSerializer());
//
//    RedisSerializationContext<String, Map> serializationContext = builder.value(serializer).build();
//
//    return new ReactiveRedisTemplate<>(connectionFactory, serializationContext);
//  }

  /**
   * Configures a {@link ReactiveRedisTemplate} with {@link String} keys and {@link GenericJackson2JsonRedisSerializer}.
   */
  @Bean
  public ReactiveRedisTemplate<String, Object> reactiveJsonObjectRedisTemplate(
      ReactiveRedisConnectionFactory connectionFactory) {

    RedisSerializationContextBuilder<String, Object> builder = RedisSerializationContext
        .newSerializationContext(new StringRedisSerializer());

    RedisSerializationContext<String, Object> serializationContext = builder
        .value(new GenericJackson2JsonRedisSerializer("_type")).build();

    return new ReactiveRedisTemplate<>(connectionFactory, serializationContext);
  }

  /**
   * Clear database before shut down.
   */
  public @PreDestroy
  void flushTestDb() {
    factory.getConnection().flushDb();
  }
}
