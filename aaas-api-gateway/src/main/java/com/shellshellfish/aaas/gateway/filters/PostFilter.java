package com.shellshellfish.aaas.gateway.filters;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.netflix.zuul.ZuulFilter;

@Component
public class PostFilter extends ZuulFilter {

	Logger logger = LoggerFactory.getLogger(PostFilter.class);

	@Override
	public Object run() {
	    logger.info("Inside post filter");
	    return null;
	}

	@Override
	public boolean shouldFilter() {
		return true;
	}

	@Override
	public int filterOrder() {
		return 1;
	}

	@Override
	public String filterType() {
		return "post";
	}

}
