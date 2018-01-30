package com.shellshellfish.aaas.userinfo.configuration;

import com.mongodb.MongoClient;
import com.shellshellfish.aaas.userinfo.aop.BigDecimalToDoubleConverter;
import com.shellshellfish.aaas.userinfo.aop.DoubleToBigDecimalConverter;
import java.util.Arrays;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.CustomConversions;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = {"com.shellshellfish.aaas.userinfo.repositories.zhongzheng"}, mongoTemplateRef = "zhongZhengMongoTemplate")
public class ZhongZhengMongoConfiguration {


	@Value("${spring.data.mongodb.host}")
	private String host;

	@Value("${spring.data.mongodb.port}")
	private int port;

	@Value("${spring.data.mongodb.zhongzheng}")
	private String zhongzhengDateBase;

	@Bean(name = "zhongZhengMongoTemplate")
	@PostConstruct
	public MongoTemplate mongoTemplate() {
		MongoTemplate mongoTemplate = new MongoTemplate(new MongoClient(host, port),
				zhongzhengDateBase);

		MappingMongoConverter mongoMapping = (MappingMongoConverter) mongoTemplate.getConverter();
		mongoMapping
				.setCustomConversions(customConversions()); // tell mongodb to use the custom converters
		mongoMapping.afterPropertiesSet();
		return mongoTemplate;
	}

	/**
	 * Returns the list of custom converters that will be used by the MongoDB template
	 **/
	@Bean
	public CustomConversions customConversions() {
		return new CustomConversions(
				Arrays.asList(new DoubleToBigDecimalConverter(), new BigDecimalToDoubleConverter()));
	}
}