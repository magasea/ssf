package com.shellshellfish.aaas.gateway.filters;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

@Component
public class PreFilter extends ZuulFilter {
	Logger logger = LoggerFactory.getLogger(PreFilter.class);
	@Override
	public Object run() {
		RequestContext ctx = RequestContext.getCurrentContext();
	    HttpServletRequest request = ctx.getRequest();

		logger.info("inside pre filter");
		logger.info("Request Method : " + request.getMethod() + " Request URL : " + request.getRequestURL
				().toString());
	    
	    // the following two lines will stop the filter chain and deny the routing
	    // ctx.setSendZuulResponse(false);
	    // ctx.setResponseStatusCode(401);
	    
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
		return "pre";
	}

}
