package com.shellshellfish.aaas.gateway.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity.IgnoredRequestConfigurer;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableConfigurationProperties
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private IgnoredWhitelistConfig ignoredListConfig;
		
    @Override
    public void init(WebSecurity web) {
    	IgnoredRequestConfigurer iRC = web.ignoring();
    	ignoredListConfig.getIgnoredWhitelist().forEach(ignoredPath -> iRC.antMatchers(ignoredPath));
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.antMatcher("/**").authorizeRequests().anyRequest().authenticated();
    }

}
