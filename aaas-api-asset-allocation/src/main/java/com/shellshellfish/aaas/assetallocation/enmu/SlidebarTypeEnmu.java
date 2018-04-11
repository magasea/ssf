package com.shellshellfish.aaas.assetallocation.enmu;

/**
 * Author: hongming
 * Date: 2018/1/13 0013
 * Desc:
 */
public enum SlidebarTypeEnmu {

    INCOME_NUM("收益率", "income_num"),
    RISK_NUM("风险率", "risk_num");

    private String name;
    private String slidebarType;

    SlidebarTypeEnmu(String name, String slidebarType) {
        this.name = name;
        this.slidebarType = slidebarType;
    }

    public static String getNameByType(String slidebarType) {
        for (SlidebarTypeEnmu e : SlidebarTypeEnmu.values()) {
            if (e.getSlidebarType().equalsIgnoreCase(slidebarType)) {
                return e.getName();
            }
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public String getSlidebarType() {
        return slidebarType;
    }
}
