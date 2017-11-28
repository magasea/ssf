package com.shellshellfish.aaas.gateway.filters;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

@Component
public class PreFilter extends ZuulFilter {

	@Override
	public Object run() {
		RequestContext ctx = RequestContext.getCurrentContext();
	    HttpServletRequest request = ctx.getRequest();
	 
	    System.out.println("inside pre filter");
	    System.out.println("Request Method : " + request.getMethod() + " Request URL : " + request.getRequestURL().toString());
	    
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
