package com.shellshellfish.aaas.common.enums;

/**
 * @Author pierre
 * 18-1-19
 * TODO 货币基金太多，需要建立一个表存储
 */
public enum MonetaryFundEnum {


    ONE("001987.OF", "东方金元宝货币市场基金"),

    TWO("400005.OF", "东方金账簿货币市场证券投资基金"),

    THREE("000366.OF", "汇添富添富通货币市场基金"),

    FOUR("003474.OF", "南方天天利货币B"),

    FIVE("004568.OF", "长城工资宝货币B"),

    SIX("004399.OF", "融通汇财宝货币E");

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
        for (MonetaryFundEnum monetaryFundEnum : MonetaryFundEnum.values()) {
            if (monetaryFundEnum.code.equalsIgnoreCase(code))
                return true;
        }
        return false;
    }

}
