package com.shellshellfish.aaas.userinfo.model.dto;

/**
 * Created by chenwei on 2017- 十二月 - 25
 */

public class UiProductDetailDTO {

	private long id;
	private long userProdId;
	private String fundCode;
	private String fundName;
	private int fundShare;
	private int fundQuantity;
	private long updateBy;
	private long updateDate;
	private long createBy;
	private long createDate;
	private Integer status;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getUserProdId() {
		return userProdId;
	}

	public void setUserProdId(long userProdId) {
		this.userProdId = userProdId;
	}

	public String getFundCode() {
		return fundCode;
	}

	public void setFundCode(String fundCode) {
		this.fundCode = fundCode;
	}

	public String getFundName() {
		return fundName;
	}

	public void setFundName(String fundName) {
		this.fundName = fundName;
	}

	public int getFundShare() {
		return fundShare;
	}

	public void setFundShare(int fundShare) {
		this.fundShare = fundShare;
	}

	public int getFundQuantity() {
		return fundQuantity;
	}

	public void setFundQuantity(int fundQuantity) {
		this.fundQuantity = fundQuantity;
	}

	public long getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(long updateBy) {
		this.updateBy = updateBy;
	}

	public long getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(long updateDate) {
		this.updateDate = updateDate;
	}

	public long getCreateBy() {
		return createBy;
	}

	public void setCreateBy(long createBy) {
		this.createBy = createBy;
	}

	public long getCreateDate() {
		return createDate;
	}

	public void setCreateDate(long createDate) {
		this.createDate = createDate;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (createBy ^ (createBy >>> 32));
		result = prime * result + (int) (createDate ^ (createDate >>> 32));
		result = prime * result + ((fundCode == null) ? 0 : fundCode.hashCode());
		result = prime * result + ((fundName == null) ? 0 : fundName.hashCode());
		result = prime * result + fundQuantity;
		result = prime * result + fundShare;
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + status;
		result = prime * result + (int) (updateBy ^ (updateBy >>> 32));
		result = prime * result + (int) (updateDate ^ (updateDate >>> 32));
		result = prime * result + (int) (userProdId ^ (userProdId >>> 32));
		return result;
	}

	@Override
	public String toString() {
		return "UiProductDetailDTO [id=" + id + ", userProdId=" + userProdId + ", fundCode=" + fundCode + ", fundName="
				+ fundName + ", fundShare=" + fundShare + ", fundQuantity=" + fundQuantity + ", updateBy=" + updateBy
				+ ", updateDate=" + updateDate + ", createBy=" + createBy + ", createDate=" + createDate + ", status="
				+ status + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UiProductDetailDTO other = (UiProductDetailDTO) obj;
		if (createBy != other.createBy)
			return false;
		if (createDate != other.createDate)
			return false;
		if (fundCode == null) {
			if (other.fundCode != null)
				return false;
		} else if (!fundCode.equals(other.fundCode))
			return false;
		if (fundName == null) {
			if (other.fundName != null)
				return false;
		} else if (!fundName.equals(other.fundName))
			return false;
		if (fundQuantity != other.fundQuantity)
			return false;
		if (fundShare != other.fundShare)
			return false;
		if (id != other.id)
			return false;
		if (status != other.status)
			return false;
		if (updateBy != other.updateBy)
			return false;
		if (updateDate != other.updateDate)
			return false;
		if (userProdId != other.userProdId)
			return false;
		return true;
	}
	
}
