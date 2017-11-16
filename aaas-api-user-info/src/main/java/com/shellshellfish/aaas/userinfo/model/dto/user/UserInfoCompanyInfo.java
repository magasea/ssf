package com.shellshellfish.aaas.userinfo.model.dto.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserInfoCompanyInfo {
    String companyInfo;

    String serviceNum;

    public String getCompanyInfo() {
        return companyInfo;
    }

    public void setCompanyInfo(String companyInfo) {
        this.companyInfo = companyInfo;
    }

    public String getServiceNum() {
        return serviceNum;
    }

    public void setServiceNum(String serviceNum) {
        this.serviceNum = serviceNum;
    }

}
