package com.gx.ca.mapper;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 
 * @TableName certificates
 */
@Data
@TableName("certificates")
public class Certificate implements Serializable {
    public final static int LEGAL = 0;
    public final static int DELETED = 1;
    public final static int EXPIRED = 2;
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 公钥
     */
    private String publicKey;
    /**
     * 私钥
     */
    private String privateKey;

    /**
     * 
     */
    private Date createdAt;

    /**
     * 
     */
    private Date updatedAt;

    /**
     * 
     */
    private Date deletedAt;

    /**
     * 证书拥有者ID
     */
    private String userAccount;

    /**
     * 状态（1 代表在使用中，2代表已撤销或过期）
     */
    private Integer state;

    /**
     * 证书请求ID
     */
    private Integer requestId;

    /**
     * 过期时间戳
     */
    private Long expireTime;

    /**
     * China
     */
    private String country;

    /**
     * ShanXi
     */
    private String province;

    /**
     * TaiYuan
     */
    private String locality;

    /**
     * NorthUnivercityOfChina
     */
    private String organization;

    /**
     * www.example.com
     */
    private String commonName;

    /**
     * xx@xx.com
     */
    private String emailAddress;

    /**
     * 144
     */
    private String organizationUnitName;

    private static final long serialVersionUID = 1L;

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        Certificate other = (Certificate) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getPublicKey() == null ? other.getPublicKey() == null : this.getPublicKey().equals(other.getPublicKey()))
            && (this.getCreatedAt() == null ? other.getCreatedAt() == null : this.getCreatedAt().equals(other.getCreatedAt()))
            && (this.getUpdatedAt() == null ? other.getUpdatedAt() == null : this.getUpdatedAt().equals(other.getUpdatedAt()))
            && (this.getDeletedAt() == null ? other.getDeletedAt() == null : this.getDeletedAt().equals(other.getDeletedAt()))
            && (this.getUserAccount() == null ? other.getUserAccount() == null : this.getUserAccount().equals(other.getUserAccount()))
            && (this.getState() == null ? other.getState() == null : this.getState().equals(other.getState()))
            && (this.getRequestId() == null ? other.getRequestId() == null : this.getRequestId().equals(other.getRequestId()))
            && (this.getExpireTime() == null ? other.getExpireTime() == null : this.getExpireTime().equals(other.getExpireTime()))
            && (this.getCountry() == null ? other.getCountry() == null : this.getCountry().equals(other.getCountry()))
            && (this.getProvince() == null ? other.getProvince() == null : this.getProvince().equals(other.getProvince()))
            && (this.getLocality() == null ? other.getLocality() == null : this.getLocality().equals(other.getLocality()))
            && (this.getOrganization() == null ? other.getOrganization() == null : this.getOrganization().equals(other.getOrganization()))
            && (this.getCommonName() == null ? other.getCommonName() == null : this.getCommonName().equals(other.getCommonName()))
            && (this.getEmailAddress() == null ? other.getEmailAddress() == null : this.getEmailAddress().equals(other.getEmailAddress()))
            && (this.getOrganizationUnitName() == null ? other.getOrganizationUnitName() == null : this.getOrganizationUnitName().equals(other.getOrganizationUnitName()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getPublicKey() == null) ? 0 : getPublicKey().hashCode());
        result = prime * result + ((getCreatedAt() == null) ? 0 : getCreatedAt().hashCode());
        result = prime * result + ((getUpdatedAt() == null) ? 0 : getUpdatedAt().hashCode());
        result = prime * result + ((getDeletedAt() == null) ? 0 : getDeletedAt().hashCode());
        result = prime * result + ((getUserAccount() == null) ? 0 : getUserAccount().hashCode());
        result = prime * result + ((getState() == null) ? 0 : getState().hashCode());
        result = prime * result + ((getRequestId() == null) ? 0 : getRequestId().hashCode());
        result = prime * result + ((getExpireTime() == null) ? 0 : getExpireTime().hashCode());
        result = prime * result + ((getCountry() == null) ? 0 : getCountry().hashCode());
        result = prime * result + ((getProvince() == null) ? 0 : getProvince().hashCode());
        result = prime * result + ((getLocality() == null) ? 0 : getLocality().hashCode());
        result = prime * result + ((getOrganization() == null) ? 0 : getOrganization().hashCode());
        result = prime * result + ((getCommonName() == null) ? 0 : getCommonName().hashCode());
        result = prime * result + ((getEmailAddress() == null) ? 0 : getEmailAddress().hashCode());
        result = prime * result + ((getOrganizationUnitName() == null) ? 0 : getOrganizationUnitName().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", publicKey=").append(publicKey);
        sb.append(", createdAt=").append(createdAt);
        sb.append(", updatedAt=").append(updatedAt);
        sb.append(", deletedAt=").append(deletedAt);
        sb.append(", userAccount=").append(userAccount);
        sb.append(", state=").append(state);
        sb.append(", requestId=").append(requestId);
        sb.append(", expireTime=").append(expireTime);
        sb.append(", country=").append(country);
        sb.append(", province=").append(province);
        sb.append(", locality=").append(locality);
        sb.append(", organization=").append(organization);
        sb.append(", commonName=").append(commonName);
        sb.append(", emailAddress=").append(emailAddress);
        sb.append(", organizationUnitName=").append(organizationUnitName);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}