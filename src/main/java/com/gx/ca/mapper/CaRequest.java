package com.gx.ca.mapper;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 
 * @TableName ca_requests
 */
@Data
@TableName("ca_requests")
public class CaRequest implements Serializable {
    public final static int AUDITING = 0;
    public final static int AUDIT_SUCCESS = 1;
    public final static int AUDIT_FAILURE = 2;

    public final static int AUTO_GENERATE_KEYS = 1;
    public final static int NOT_AUTO_GENERATE_KEYS = 0;
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 字段创建时间
     */
    private Date createdAt;

    /**
     * 更新时间
     */
    private Date updatedAt;

    /**
     * 删除时间
     */
    private Date deletedAt;

    /**
     * 申请证书的用户ID
     */
    private String userAccount;

    /**
     * 证书状态（1：待审核， 2： 审核通过， 3：审核未通过）
     */
    private Integer state;

    /**
     * 公钥
     */
    private String publicKey;
    /**
     * 私钥
     */
    private String privateKey;

    /**
     * 国家
     */
    private String country;

    /**
     * 州市
     */
    private String province;

    /**
     * 地区
     */
    private String locality;

    /**
     * 组织
     */
    private String organization;

    /**
     * 部门
     */
    private String organizationUnitName;

    /**
     * 姓名
     */
    private String commonName;

    /**
     * 邮箱
     */
    private String emailAddress;

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
        CaRequest other = (CaRequest) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getCreatedAt() == null ? other.getCreatedAt() == null : this.getCreatedAt().equals(other.getCreatedAt()))
            && (this.getUpdatedAt() == null ? other.getUpdatedAt() == null : this.getUpdatedAt().equals(other.getUpdatedAt()))
            && (this.getDeletedAt() == null ? other.getDeletedAt() == null : this.getDeletedAt().equals(other.getDeletedAt()))
            && (this.getUserAccount() == null ? other.getUserAccount() == null : this.getUserAccount().equals(other.getUserAccount()))
            && (this.getState() == null ? other.getState() == null : this.getState().equals(other.getState()))
            && (this.getPublicKey() == null ? other.getPublicKey() == null : this.getPublicKey().equals(other.getPublicKey()))
            && (this.getCountry() == null ? other.getCountry() == null : this.getCountry().equals(other.getCountry()))
            && (this.getProvince() == null ? other.getProvince() == null : this.getProvince().equals(other.getProvince()))
            && (this.getLocality() == null ? other.getLocality() == null : this.getLocality().equals(other.getLocality()))
            && (this.getOrganization() == null ? other.getOrganization() == null : this.getOrganization().equals(other.getOrganization()))
            && (this.getOrganizationUnitName() == null ? other.getOrganizationUnitName() == null : this.getOrganizationUnitName().equals(other.getOrganizationUnitName()))
            && (this.getCommonName() == null ? other.getCommonName() == null : this.getCommonName().equals(other.getCommonName()))
            && (this.getEmailAddress() == null ? other.getEmailAddress() == null : this.getEmailAddress().equals(other.getEmailAddress()))
            && (this.getPrivateKey() == null ? other.getPrivateKey() == null : this.getPrivateKey().equals(other.getPrivateKey()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getCreatedAt() == null) ? 0 : getCreatedAt().hashCode());
        result = prime * result + ((getUpdatedAt() == null) ? 0 : getUpdatedAt().hashCode());
        result = prime * result + ((getDeletedAt() == null) ? 0 : getDeletedAt().hashCode());
        result = prime * result + ((getUserAccount() == null) ? 0 : getUserAccount().hashCode());
        result = prime * result + ((getState() == null) ? 0 : getState().hashCode());
        result = prime * result + ((getPublicKey() == null) ? 0 : getPublicKey().hashCode());
        result = prime * result + ((getCountry() == null) ? 0 : getCountry().hashCode());
        result = prime * result + ((getProvince() == null) ? 0 : getProvince().hashCode());
        result = prime * result + ((getLocality() == null) ? 0 : getLocality().hashCode());
        result = prime * result + ((getOrganization() == null) ? 0 : getOrganization().hashCode());
        result = prime * result + ((getOrganizationUnitName() == null) ? 0 : getOrganizationUnitName().hashCode());
        result = prime * result + ((getCommonName() == null) ? 0 : getCommonName().hashCode());
        result = prime * result + ((getEmailAddress() == null) ? 0 : getEmailAddress().hashCode());
        result = prime * result + ((getPrivateKey() == null) ? 0 : getPrivateKey().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", createdAt=").append(createdAt);
        sb.append(", updatedAt=").append(updatedAt);
        sb.append(", deletedAt=").append(deletedAt);
        sb.append(", userId=").append(userAccount);
        sb.append(", state=").append(state);
        sb.append(", publicKey=").append(publicKey);
        sb.append(", country=").append(country);
        sb.append(", province=").append(province);
        sb.append(", locality=").append(locality);
        sb.append(", organization=").append(organization);
        sb.append(", organizationUnitName=").append(organizationUnitName);
        sb.append(", commonName=").append(commonName);
        sb.append(", emailAddress=").append(emailAddress);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }

}