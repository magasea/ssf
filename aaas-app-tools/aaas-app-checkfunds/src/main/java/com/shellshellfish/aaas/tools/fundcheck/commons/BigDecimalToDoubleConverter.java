package com.shellshellfish.aaas.tools.fundcheck.commons;


import java.math.BigDecimal;
import java.math.RoundingMode;
import org.springframework.core.convert.converter.Converter;

/**
 * @Author pierre 18-1-25
 */
public class BigDecimalToDoubleConverter implements Converter<BigDecimal, Double> {

	@Override
	public Double convert(BigDecimal source) {
		return source.setScale(6, RoundingMode.HALF_UP).doubleValue();
	}

}
