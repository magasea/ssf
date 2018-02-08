package com.shellshellfish.aaas.userinfo.model.redis;

import java.io.Serializable;

/**
 * Created by chenwei on 2018- 二月 - 02
 */

public class UserBaseInfoRedis implements Serializable {

  private long id;
  private String uuid;
  private String cellPhone;
  private String birthAge;
  private String occupation;
  private int activated;
  private String createdBy;
  private long createdDate;
  private Long lastResetDate;
  private String lastModifiedBy;
  private Long lastModifiedDate;
  private String passwordHash;
  private Integer isTestFlag;
  private Integer riskLevel;

  
  
  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  
  
  public String getUuid() {
    return uuid;
  }

  public void setUuid(String uuid) {
    this.uuid = uuid;
  }

  
  
  public String getCellPhone() {
    return cellPhone;
  }

  public void setCellPhone(String cellPhone) {
    this.cellPhone = cellPhone;
  }

  
  
  public String getBirthAge() {
    return birthAge;
  }

  public void setBirthAge(String birthAge) {
    this.birthAge = birthAge;
  }

  
  
  public String getOccupation() {
    return occupation;
  }

  public void setOccupation(String occupation) {
    this.occupation = occupation;
  }

  
  
  public int getActivated() {
    return activated;
  }

  public void setActivated(int activated) {
    this.activated = activated;
  }

  
  
  public String getCreatedBy() {
    return createdBy;
  }

  public void setCreatedBy(String createdBy) {
    this.createdBy = createdBy;
  }

  
  
  public long getCreatedDate() {
    return createdDate;
  }

  public void setCreatedDate(long createdDate) {
    this.createdDate = createdDate;
  }

  
  
  public Long getLastResetDate() {
    return lastResetDate;
  }

  public void setLastResetDate(Long lastResetDate) {
    this.lastResetDate = lastResetDate;
  }

  
  
  public String getLastModifiedBy() {
    return lastModifiedBy;
  }

  public void setLastModifiedBy(String lastModifiedBy) {
    this.lastModifiedBy = lastModifiedBy;
  }

  
  
  public Long getLastModifiedDate() {
    return lastModifiedDate;
  }

  public void setLastModifiedDate(Long lastModifiedDate) {
    this.lastModifiedDate = lastModifiedDate;
  }

  
  
  public String getPasswordHash() {
    return passwordHash;
  }

  public void setPasswordHash(String passwordHash) {
    this.passwordHash = passwordHash;
  }

  
  
  public Integer getIsTestFlag() {
    return isTestFlag;
  }

  public void setIsTestFlag(Integer isTestFlag) {
    this.isTestFlag = isTestFlag;
  }

  
  
  public Integer getRiskLevel() {
    return riskLevel;
  }

  public void setRiskLevel(Integer riskLevel) {
    this.riskLevel = riskLevel;
  }

  
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    com.shellshellfish.aaas.userinfo.model.redis.UserBaseInfoRedis userBaseInfoRedis =
        (com.shellshellfish.aaas.userinfo.model.redis.UserBaseInfoRedis) o;

    if (id != userBaseInfoRedis.id) {
      return false;
    }
    if (activated != userBaseInfoRedis.activated) {
      return false;
    }
    if (createdDate != userBaseInfoRedis.createdDate) {
      return false;
    }
    if (uuid != null ? !uuid.equals(userBaseInfoRedis.uuid) : userBaseInfoRedis.uuid != null) {
      return false;
    }
    if (cellPhone != null ? !cellPhone.equals(userBaseInfoRedis.cellPhone) : userBaseInfoRedis.cellPhone != null) {
      return false;
    }
    if (birthAge != null ? !birthAge.equals(userBaseInfoRedis.birthAge) : userBaseInfoRedis.birthAge != null) {
      return false;
    }
    if (occupation != null ? !occupation.equals(userBaseInfoRedis.occupation) : userBaseInfoRedis.occupation != null) {
      return false;
    }
    if (createdBy != null ? !createdBy.equals(userBaseInfoRedis.createdBy) : userBaseInfoRedis.createdBy != null) {
      return false;
    }
    if (lastResetDate != null ? !lastResetDate.equals(userBaseInfoRedis.lastResetDate)
        : userBaseInfoRedis.lastResetDate != null) {
      return false;
    }
    if (lastModifiedBy != null ? !lastModifiedBy.equals(userBaseInfoRedis.lastModifiedBy)
        : userBaseInfoRedis.lastModifiedBy != null) {
      return false;
    }
    if (lastModifiedDate != null ? !lastModifiedDate.equals(userBaseInfoRedis.lastModifiedDate)
        : userBaseInfoRedis.lastModifiedDate != null) {
      return false;
    }
    if (passwordHash != null ? !passwordHash.equals(userBaseInfoRedis.passwordHash)
        : userBaseInfoRedis.passwordHash != null) {
      return false;
    }
    if (isTestFlag != null ? !isTestFlag.equals(userBaseInfoRedis.isTestFlag) : userBaseInfoRedis.isTestFlag != null) {
      return false;
    }
    if (riskLevel != null ? !riskLevel.equals(userBaseInfoRedis.riskLevel) : userBaseInfoRedis.riskLevel != null) {
      return false;
    }

    return true;
  }

  
  public int hashCode() {
    int result = (int) (id ^ (id >>> 32));
    result = 31 * result + (uuid != null ? uuid.hashCode() : 0);
    result = 31 * result + (cellPhone != null ? cellPhone.hashCode() : 0);
    result = 31 * result + (birthAge != null ? birthAge.hashCode() : 0);
    result = 31 * result + (occupation != null ? occupation.hashCode() : 0);
    result = 31 * result + activated;
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

