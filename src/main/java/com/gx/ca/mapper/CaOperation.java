package com.gx.ca.mapper;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * 
 * @TableName ca_operation
 */
@Data
public class CaOperation implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 0:通过审核
1:
2:
3:
4:
     */
    private Integer operation;

    /**
     * 
     */
    private Date time;

    /**
     * 
     */
    private String caCn;

    /**
     * 
     */
    private Integer caId;

    /**
     * 
     */
    private String content;

    /**
     * 
     */
    private String userAccount;

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
        CaOperation other = (CaOperation) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getOperation() == null ? other.getOperation() == null : this.getOperation().equals(other.getOperation()))
            && (this.getTime() == null ? other.getTime() == null : this.getTime().equals(other.getTime()))
            && (this.getCaCn() == null ? other.getCaCn() == null : this.getCaCn().equals(other.getCaCn()))
            && (this.getCaId() == null ? other.getCaId() == null : this.getCaId().equals(other.getCaId()))
            && (this.getContent() == null ? other.getContent() == null : this.getContent().equals(other.getContent()))
            && (this.getUserAccount() == null ? other.getUserAccount() == null : this.getUserAccount().equals(other.getUserAccount()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getOperation() == null) ? 0 : getOperation().hashCode());
        result = prime * result + ((getTime() == null) ? 0 : getTime().hashCode());
        result = prime * result + ((getCaCn() == null) ? 0 : getCaCn().hashCode());
        result = prime * result + ((getCaId() == null) ? 0 : getCaId().hashCode());
        result = prime * result + ((getContent() == null) ? 0 : getContent().hashCode());
        result = prime * result + ((getUserAccount() == null) ? 0 : getUserAccount().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", operation=").append(operation);
        sb.append(", time=").append(time);
        sb.append(", caCn=").append(caCn);
        sb.append(", caId=").append(caId);
        sb.append(", content=").append(content);
        sb.append(", userAccount=").append(userAccount);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}