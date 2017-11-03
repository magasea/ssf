package com.shellshellfish.aaas.userinfo.model.dao.userinfo;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.validator.constraints.NotEmpty;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="USER_USERINFO")
public class UserInfoDao implements Serializable {

  @Id
  @GeneratedValue(strategy= GenerationType.IDENTITY)
  private Long id;

  @NotEmpty
  @Column(name="NAME", nullable=false)
  private String name;

  @Column(name="AGE", nullable=false)
  private Integer age;

  @Column(name="OCCUPATION", nullable=false)
  private String occupation;

  public String getOccupation() {
    return occupation;
  }

  public void setOccupation(String occupation) {
    this.occupation = occupation;
  }

  public double getPhoneNum() {
    return phoneNum;
  }

  public void setPhoneNum(double phoneNum) {
    this.phoneNum = phoneNum;
  }

  @Column(name="PHONENUM", nullable=false)
  private double phoneNum;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Integer getAge() {
    return age;
  }

  public void setAge(Integer age) {
    this.age = age;
  }



  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    com.shellshellfish.aaas.userinfo.model.dao.userinfo.UserInfoDao user =  (com.shellshellfish
        .aaas.userinfo.model.dao.userinfo.UserInfoDao) o;

    if (!user.occupation.equals(occupation) ) return false;
    if (id != null ? !id.equals(user.id) : user.id != null) return false;
    if (name != null ? !name.equals(user.name) : user.name != null) return false;
    return age != null ? age.equals(user.age) : user.age == null;
  }

  @Override
  public int hashCode() {
    int result;
    long temp;
    result = id != null ? id.hashCode() : 0;
    result = 31 * result + (name != null ? name.hashCode() : 0);
    result = 31 * result + (age != null ? age.hashCode() : 0);
    result = 31 * result + (name != null ? name.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "User [id=" + id + ", name=" + name + ", age=" + age
        + ", occupation=" + occupation + "]";
  }


}

