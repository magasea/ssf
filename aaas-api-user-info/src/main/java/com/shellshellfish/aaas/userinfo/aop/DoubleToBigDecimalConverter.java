package com.shellshellfish.aaas.userinfo.aop;


import java.math.BigDecimal;
import org.springframework.core.convert.converter.Converter;

/**
 * @Author pierre 18-1-25
 */


public class DoubleToBigDecimalConverter implements Converter<Double, BigDecimal> {

	@Override
	public BigDecimal convert(Double source) {
		return new BigDecimal(source);
	}

}
