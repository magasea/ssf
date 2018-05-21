package com.shellshellfish.aaas.common.enums;

/**
 * @Author pierre.chen
 * @Date 18-5-9
 */
public enum QDIIEnum {

    DXA("000614", "华安德国DAX");

    String code;
    String name;

    QDIIEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public static boolean isQDII(String code) {
        if (code.contains("."))
            code = code.substring(0, code.indexOf("."));

        QDIIEnum[] values = QDIIEnum.values();

        for (int i = 0; i < values.length; i++) {
            if (values[i].code.equalsIgnoreCase(code))
                return true;
        }
        return false;
    }


    public static void main(String[] args) {
        System.out.println(isQDII("000514.OF"));
    }

}
