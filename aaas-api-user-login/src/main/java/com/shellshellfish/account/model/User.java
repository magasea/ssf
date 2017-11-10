package com.shellshellfish.account.model;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Collection;

@Entity
public class User {
    private long id;
    private String uuid;
    private String cellPhone;
    private String birthAge;
    private String occupation;
    private String passwordHash;
    private boolean activated;
    private String createdBy;
    private Timestamp createdDate;
    private Timestamp lastResetDate;
    private String lastModifiedBy;
    private Timestamp lastModifiedDate;
    private Collection<UserRole> userRolesById;

    public void setId(Long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "activated", nullable = false)
    public Boolean getActivated() {
        return activated;
    }

    public void setActivated(Boolean activated) {
        this.activated = activated;
    }

    @Id
    @Column(name = "id", nullable = false)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "uuid", nullable = false, length = 36)
    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Basic
    @Column(name = "cell_phone", nullable = false, length = 50)
    public String getCellPhone() {
        return cellPhone;
    }

    public void setCellPhone(String cellPhone) {
        this.cellPhone = cellPhone;
    }

    @Basic
    @Column(name = "birth_age", nullable = true, length = 50)
    public String getBirthAge() {
        return birthAge;
    }

    public void setBirthAge(String birthAge) {
        this.birthAge = birthAge;
    }

    @Basic
    @Column(name = "occupation", nullable = true, length = 50)
    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    @Basic
    @Column(name = "password_hash", nullable = true, length = 60)
    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    @Basic
    @Column(name = "activated", nullable = false)
    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    @Basic
    @Column(name = "created_by", nullable = false, length = 50)
    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    @Basic
    @Column(name = "created_date", nullable = false)
    public Timestamp getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }

    @Basic
    @Column(name = "last_reset_date", nullable = true)
    public Timestamp getLastResetDate() {
        return lastResetDate;
    }

    public void setLastResetDate(Timestamp lastResetDate) {
        this.lastResetDate = lastResetDate;
    }

    @Basic
    @Column(name = "last_modified_by", nullable = true, length = 50)
    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    @Basic
    @Column(name = "last_modified_date", nullable = true)
    public Timestamp getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Timestamp lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (id != user.id) return false;
        if (activated != user.activated) return false;
        if (uuid != null ? !uuid.equals(user.uuid) : user.uuid != null) return false;
        if (cellPhone != null ? !cellPhone.equals(user.cellPhone) : user.cellPhone != null) return false;
        if (birthAge != null ? !birthAge.equals(user.birthAge) : user.birthAge != null) return false;
        if (occupation != null ? !occupation.equals(user.occupation) : user.occupation != null) return false;
        if (passwordHash != null ? !passwordHash.equals(user.passwordHash) : user.passwordHash != null) return false;
        if (createdBy != null ? !createdBy.equals(user.createdBy) : user.createdBy != null) return false;
        if (createdDate != null ? !createdDate.equals(user.createdDate) : user.createdDate != null) return false;
        if (lastResetDate != null ? !lastResetDate.equals(user.lastResetDate) : user.lastResetDate != null)
            return false;
        if (lastModifiedBy != null ? !lastModifiedBy.equals(user.lastModifiedBy) : user.lastModifiedBy != null)
            return false;
        if (lastModifiedDate != null ? !lastModifiedDate.equals(user.lastModifiedDate) : user.lastModifiedDate != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (uuid != null ? uuid.hashCode() : 0);
        result = 31 * result + (cellPhone != null ? cellPhone.hashCode() : 0);
        result = 31 * result + (birthAge != null ? birthAge.hashCode() : 0);
        result = 31 * result + (occupation != null ? occupation.hashCode() : 0);
        result = 31 * result + (passwordHash != null ? passwordHash.hashCode() : 0);
        result = 31 * result + (activated ? 1 : 0);
        result = 31 * result + (createdBy != null ? createdBy.hashCode() : 0);
        result = 31 * result + (createdDate != null ? createdDate.hashCode() : 0);
        result = 31 * result + (lastResetDate != null ? lastResetDate.hashCode() : 0);
        result = 31 * result + (lastModifiedBy != null ? lastModifiedBy.hashCode() : 0);
        result = 31 * result + (lastModifiedDate != null ? lastModifiedDate.hashCode() : 0);
        return result;
    }

    @OneToMany(mappedBy = "userByUserId")
    public Collection<UserRole> getUserRolesById() {
        return userRolesById;
    }

    public void setUserRolesById(Collection<UserRole> userRolesById) {
        this.userRolesById = userRolesById;
    }
}
