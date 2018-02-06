package com.shellshellfish.aaas.common.enums;

/**
 * created by pierre on 2018-02-06
 */
public enum FundRiskLevelEnum {

	R1("R1", "低风险"), R2("R2", "中低风险"), R3("R3", "中风险"), R4("R4", "中高风险"), R5("R5", "高风险");

	private String level;
	private String name;

	FundRiskLevelEnum(String level, String name) {
		this.level = level;
		this.name = name;
	}

	public static String getName(String level) {
		level = level.toUpperCase();

		switch (level) {
			case "R1":
				return R1.name;
			case "R2":
				return R2.name;
			case "R3":
				return R3.name;
			case "R4":
				return R4.name;
			case "R5":
				return R5.name;
			default:
				return "--";
		}
	}
}
