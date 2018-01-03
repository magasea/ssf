package transfer.controller;

import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author pierre
 * 18-1-3
 */

public class LogFilter implements Filter{

	private static  final Logger logger = LoggerFactory.getLogger(Filter.class);

	@Override
	public Response filter(FilterableRequestSpecification requestSpec, FilterableResponseSpecification responseSpec, FilterContext ctx) {
		String uri = requestSpec.getURI();


		return ctx.next(requestSpec, responseSpec);
	}
}
