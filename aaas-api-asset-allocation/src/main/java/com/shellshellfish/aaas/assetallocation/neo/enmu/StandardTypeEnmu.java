package com.shellshellfish.aaas.assetallocation.neo.enmu;

/**
 * Author: hongming
 * Date: 2018/1/13 0013
 * Desc:
 */
public enum StandardTypeEnmu {

    INCOME_NUM("收益率", "1", "income_num"),
    RISK_NUM("风险率", "2", "risk_num");

    private String name;
    private String standardType;
    private String slidebarType;

    StandardTypeEnmu(String name, String standardType, String slidebarType) {
        this.name = name;
        this.standardType = standardType;
        this.slidebarType = slidebarType;
    }

    public static String getNameByStandardType(String standardType) {
        for (StandardTypeEnmu e : StandardTypeEnmu.values()) {
            if (e.getStandardType().equalsIgnoreCase(standardType)) {
                return e.getName();
            }
        }
        return null;
    }

    public static String getNameBySlidebarType(String slidebarType) {
        for (StandardTypeEnmu e : StandardTypeEnmu.values()) {
            if (e.getSlidebarType().equalsIgnoreCase(slidebarType)) {
                return e.getName();
            }
        }
        return null;
    }

    public static String getStandardTypeBySlidebarType(String slidebarType) {
        for (StandardTypeEnmu e : StandardTypeEnmu.values()) {
            if (e.getSlidebarType().equalsIgnoreCase(slidebarType)) {
                return e.getStandardType();
            }
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public String getStandardType() {
        return standardType;
    }

    public String getSlidebarType() {
        return slidebarType;
    }
}
