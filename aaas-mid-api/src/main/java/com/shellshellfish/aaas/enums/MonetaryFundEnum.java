package com.shellshellfish.aaas.enums;

/**
 * @Author pierre
 * 18-1-19
 */
public enum MonetaryFundEnum {


	ONE("001987.OF", "东方金元宝货币市场基金"),

	TWO("400005.OF", "东方金账簿货币市场证券投资基金"),

	THREE("000366.OF", "汇添富添富通货币市场基金");

	private String code;
	private String name;

	MonetaryFundEnum(String code, String name) {
		this.code = code;
		this.name = name;
	}


	public String getCode() {
		return code;
	}


	public String getName() {
		return name;
	}

	public static boolean containsCode(String code) {
		if (ONE.code.equalsIgnoreCase(code) || TWO.code.equalsIgnoreCase(code) || THREE.code.equalsIgnoreCase(code))
			return true;

		return false;
	}

}
