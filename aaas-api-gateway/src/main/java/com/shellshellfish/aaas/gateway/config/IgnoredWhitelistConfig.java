package com.shellshellfish.aaas.gateway.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "shellshellfish")
public class IgnoredWhitelistConfig {
	private List<String> ignoredWhtelist;

	IgnoredWhitelistConfig() {
		this.ignoredWhtelist = new ArrayList<String>();
	}

	public List<String> getIgnoredWhitelist() {
		return this.ignoredWhtelist;
	}
}
