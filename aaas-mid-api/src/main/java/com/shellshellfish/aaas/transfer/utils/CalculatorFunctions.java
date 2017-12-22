package com.shellshellfish.aaas.transfer.utils;

import java.math.BigDecimal;

/**
 * 放置了一些计算公式
 * @author developer4
 *
 */
public class CalculatorFunctions {
	
	/**
	 * 计算历史收益
	 * @param investTerm 投资期限
	 * @param historicAnnPerformace 模拟历史年化业绩
	 * @return
	 */
	public static String getHistoricReturn(String investAmount,String historicAnnPerformace){
		BigDecimal term=new BigDecimal(investAmount);
		BigDecimal performace=new BigDecimal(historicAnnPerformace);
		Double result=term.multiply(performace).doubleValue();
		return result.toString();
	}

}
