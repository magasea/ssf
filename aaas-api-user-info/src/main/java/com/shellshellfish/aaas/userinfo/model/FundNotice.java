package com.shellshellfish.aaas.userinfo.model;

public class FundNotice {
    private String fundCode;
    private String title;
    private String txtContent;
    private String declareDate;
    private String acceRoute;

    public String getFundCode() {
        return fundCode;
    }

    public void setFundCode(String fundCode) {
        this.fundCode = fundCode;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTxtContent() {
        return txtContent;
    }

    public void setTxtContent(String txtContent) {
        this.txtContent = txtContent;
    }

    public String getDeclareDate() {
        return declareDate;
    }

    public void setDeclareDate(String declareDate) {
        this.declareDate = declareDate;
    }

    public String getAcceRoute() {
        return acceRoute;
    }

    public void setAcceRoute(String acceRoute) {
        this.acceRoute = acceRoute;
    }
}
