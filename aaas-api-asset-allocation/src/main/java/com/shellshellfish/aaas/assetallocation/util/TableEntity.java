package com.shellshellfish.aaas.assetallocation.util;

import java.util.List;

public class TableEntity {

    private List<String> header;
    private List<List<Object>> values;
    private String caption;

    public List<String> getHeader() {
        return header;
    }

    public void setHeader(List<String> header) {
        this.header = header;
    }

    public List<List<Object>> getValues() {
        return values;
    }

    public void setValues(List<List<Object>> values) {
        this.values = values;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }
}
