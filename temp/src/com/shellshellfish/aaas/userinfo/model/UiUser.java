package com.shellshellfish.aaas.userinfo.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by chenwei on 2017- 十二月 - 21
 */

@Entity
@Table(name = "ui_user", schema = "ssfuser", catalog = "")
public class UiUser {

  private long id;
  private String uuid;
  private String cellPhone;
  private String birthAge;
  private String occupation;
  private boolean activated;
  private String createdBy;
  private long createdDate;
  private Long lastResetDate;
  private String lastModifiedBy;
  private Long lastModifiedDate;
  private String passwordHash;
  private Integer isTestFlag;
  private Integer riskLevel;

  @Id
  @Column(name = "id")
  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  @Basic
  @Column(name = "uuid")
  public String getUuid() {
    return uuid;
  }

  public void setUuid(String uuid) {
    this.uuid = uuid;
  }

  @Basic
  @Column(name = "cell_phone")
  public String getCellPhone() {
    return cellPhone;
  }

  public void setCellPhone(String cellPhone) {
    this.cellPhone = cellPhone;
  }

  @Basic
  @Column(name = "birth_age")
  public String getBirthAge() {
    return birthAge;
  }

  public void setBirthAge(String birthAge) {
    this.birthAge = birthAge;
  }

  @Basic
  @Column(name = "occupation")
  public String getOccupation() {
    return occupation;
  }

  public void setOccupation(String occupation) {
    this.occupation = occupation;
  }

  @Basic
  @Column(name = "activated")
  public boolean isActivated() {
    return activated;
  }

  public void setActivated(boolean activated) {
    this.activated = activated;
  }

  @Basic
  @Column(name = "created_by")
  public String getCreatedBy() {
    return createdBy;
  }

  public void setCreatedBy(String createdBy) {
    this.createdBy = createdBy;
  }

  @Basic
  @Column(name = "created_date")
  public long getCreatedDate() {
    return createdDate;
  }

  public void setCreatedDate(long createdDate) {
    this.createdDate = createdDate;
  }

  @Basic
  @Column(name = "last_reset_date")
  public Long getLastResetDate() {
    return lastResetDate;
  }

  public void setLastResetDate(Long lastResetDate) {
    this.lastResetDate = lastResetDate;
  }

  @Basic
  @Column(name = "last_modified_by")
  public String getLastModifiedBy() {
    return lastModifiedBy;
  }

  public void setLastModifiedBy(String lastModifiedBy) {
    this.lastModifiedBy = lastModifiedBy;
  }

  @Basic
  @Column(name = "last_modified_date")
  public Long getLastModifiedDate() {
    return lastModifiedDate;
  }

  public void setLastModifiedDate(Long lastModifiedDate) {
    this.lastModifiedDate = lastModifiedDate;
  }

  @Basic
  @Column(name = "password_hash")
  public String getPasswordHash() {
    return passwordHash;
  }

  public void setPasswordHash(String passwordHash) {
    this.passwordHash = passwordHash;
  }

  @Basic
  @Column(name = "is_test_flag")
  public Integer getIsTestFlag() {
    return isTestFlag;
  }

  public void setIsTestFlag(Integer isTestFlag) {
    this.isTestFlag = isTestFlag;
  }

  @Basic
  @Column(name = "risk_level")
  public Integer getRiskLevel() {
    return riskLevel;
  }

  public void setRiskLevel(Integer riskLevel) {
    this.riskLevel = riskLevel;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    UiUser uiUser = (UiUser) o;

    if (id != uiUser.id) {
      return false;
    }
    if (activated != uiUser.activated) {
      return false;
    }
    if (createdDate != uiUser.createdDate) {
      return false;
    }
    if (uuid != null ? !uuid.equals(uiUser.uuid) : uiUser.uuid != null) {
      return false;
    }
    if (cellPhone != null ? !cellPhone.equals(uiUser.cellPhone) : uiUser.cellPhone != null) {
      return false;
    }
    if (birthAge != null ? !birthAge.equals(uiUser.birthAge) : uiUser.birthAge != null) {
      return false;
    }
    if (occupation != null ? !occupation.equals(uiUser.occupation) : uiUser.occupation != null) {
      return false;
    }
    if (createdBy != null ? !createdBy.equals(uiUser.createdBy) : uiUser.createdBy != null) {
      return false;
    }
    if (lastResetDate != null ? !lastResetDate.equals(uiUser.lastResetDate)
        : uiUser.lastResetDate != null) {
      return false;
    }
    if (lastModifiedBy != null ? !lastModifiedBy.equals(uiUser.lastModifiedBy)
        : uiUser.lastModifiedBy != null) {
      return false;
    }
    if (lastModifiedDate != null ? !lastModifiedDate.equals(uiUser.lastModifiedDate)
        : uiUser.lastModifiedDate != null) {
      return false;
    }
    if (passwordHash != null ? !passwordHash.equals(uiUser.passwordHash)
        : uiUser.passwordHash != null) {
      return false;
    }
    if (isTestFlag != null ? !isTestFlag.equals(uiUser.isTestFlag) : uiUser.isTestFlag != null) {
      return false;
    }
    if (riskLevel != null ? !riskLevel.equals(uiUser.riskLevel) : uiUser.riskLevel != null) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int result = (int) (id ^ (id >>> 32));
    result = 31 * result + (uuid != null ? uuid.hashCode() : 0);
    result = 31 * result + (cellPhone != null ? cellPhone.hashCode() : 0);
    result = 31 * result + (birthAge != null ? birthAge.hashCode() : 0);
    result = 31 * result + (occupation != null ? occupation.hashCode() : 0);
    result = 31 * result + (activated ? 1 : 0);
    result = 31 * result + (createdBy != null ? createdBy.hashCode() : 0);
    result = 31 * result + (int) (createdDate ^ (createdDate >>> 32));
    result = 31 * result + (lastResetDate != null ? lastResetDate.hashCode() : 0);
    result = 31 * result + (lastModifiedBy != null ? lastModifiedBy.hashCode() : 0);
    result = 31 * result + (lastModifiedDate != null ? lastModifiedDate.hashCode() : 0);
    result = 31 * result + (passwordHash != null ? passwordHash.hashCode() : 0);
    result = 31 * result + (isTestFlag != null ? isTestFlag.hashCode() : 0);
    result = 31 * result + (riskLevel != null ? riskLevel.hashCode() : 0);
    return result;
  }
}
