package com.shellshellfish.aaas.userinfo.model.dto.user;

import java.util.Date;


public class UserBaseInfo {


    Long id;
    String userName;
    String cellPhone;
    Date birthAge;
    String occupation;
    String password;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCellPhone() {
        return cellPhone;
    }

    public void setCellPhone(String cellPhone) {
        this.cellPhone = cellPhone;
    }

    public Date getBirthAge() {
        return birthAge;
    }

    public void setBirthAge(Date birthDay) {
        this.birthAge = birthDay;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }



}
