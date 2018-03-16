package com.shellshellfish.aaas.tools.fundcheck.commons;


import java.math.BigDecimal;
import java.math.RoundingMode;
import org.springframework.core.convert.converter.Converter;

/**
 * @Author pierre 18-1-25
 */


public class DoubleToBigDecimalConverter implements Converter<Double, BigDecimal> {

	@Override
	public BigDecimal convert(Double source) {
		return new BigDecimal(source).setScale(6, RoundingMode.HALF_UP);
	}

}
