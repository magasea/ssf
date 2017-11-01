package com.shellshellfish.aaas.util;

public class FishChart {
    private String title;
    private String href;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public FishChart(String title, String href, String describedBy) {
        this.title = title;
        this.href = href;
        this.describedBy = describedBy;
    }

    public String getDescribedBy() {

        return describedBy;
    }

    public void setDescribedBy(String describedBy) {
        this.describedBy = describedBy;
    }

    private String describedBy;

}
