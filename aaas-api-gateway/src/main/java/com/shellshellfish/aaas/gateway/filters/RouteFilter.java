package com.shellshellfish.aaas.gateway.filters;


import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

@Component
public class RouteFilter extends ZuulFilter {

	Logger logger = LoggerFactory.getLogger(PreFilter.class);

	@Override
	public Object run() {
		RequestContext ctx = RequestContext.getCurrentContext();
	    HttpServletRequest request = ctx.getRequest();

		logger.info("Inside route filter");
		logger.info("Request Method : " + request.getMethod() + " Request URL : " + request.getRequestURL
				().toString());
	    
	    // you can setup the response without really
	    // ctx.setResponseBody("Hello, hijack");
	    // ctx.setRouteHost(null);
	    
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
		return "route";
	}

}
