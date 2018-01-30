package com.shellshellfish.aaas.userinfo.model.dto;

/**
 * Created by chenwei on 2017- 十二月 - 25
 */

public class UiProductDetailDTO {

	private long id;
	private Long createBy;
	private Long createDate;
	private String fundCode;
	private String fundName;
	private Integer fundQuantity;
	private Integer fundQuantityTrade;
	private Integer fundShare;
	private Integer status;
	private Long updateBy;
	private Long updateDate;
	private Long userProdId;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Long getCreateBy() {
		return createBy;
	}

	public void setCreateBy(Long createBy) {
		this.createBy = createBy;
	}

	public Long getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Long createDate) {
		this.createDate = createDate;
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

	public Integer getFundQuantity() {
		return fundQuantity;
	}

	public void setFundQuantity(Integer fundQuantity) {
		this.fundQuantity = fundQuantity;
	}

	public Integer getFundQuantityTrade() {
		return fundQuantityTrade;
	}

	public void setFundQuantityTrade(Integer fundQuantityTrade) {
		this.fundQuantityTrade = fundQuantityTrade;
	}

	public Integer getFundShare() {
		return fundShare;
	}

	public void setFundShare(Integer fundShare) {
		this.fundShare = fundShare;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Long getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(Long updateBy) {
		this.updateBy = updateBy;
	}

	public Long getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Long updateDate) {
		this.updateDate = updateDate;
	}

	public Long getUserProdId() {
		return userProdId;
	}

	public void setUserProdId(Long userProdId) {
		this.userProdId = userProdId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((createBy == null) ? 0 : createBy.hashCode());
		result = prime * result + ((createDate == null) ? 0 : createDate.hashCode());
		result = prime * result + ((fundCode == null) ? 0 : fundCode.hashCode());
		result = prime * result + ((fundName == null) ? 0 : fundName.hashCode());
		result = prime * result + ((fundQuantity == null) ? 0 : fundQuantity.hashCode());
		result = prime * result + ((fundQuantityTrade == null) ? 0 : fundQuantityTrade.hashCode());
		result = prime * result + ((fundShare == null) ? 0 : fundShare.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result + ((updateBy == null) ? 0 : updateBy.hashCode());
		result = prime * result + ((updateDate == null) ? 0 : updateDate.hashCode());
		result = prime * result + ((userProdId == null) ? 0 : userProdId.hashCode());
		return result;
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
		if (createBy == null) {
			if (other.createBy != null)
				return false;
		} else if (!createBy.equals(other.createBy))
			return false;
		if (createDate == null) {
			if (other.createDate != null)
				return false;
		} else if (!createDate.equals(other.createDate))
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
		if (fundQuantity == null) {
			if (other.fundQuantity != null)
				return false;
		} else if (!fundQuantity.equals(other.fundQuantity))
			return false;
		if (fundQuantityTrade == null) {
			if (other.fundQuantityTrade != null)
				return false;
		} else if (!fundQuantityTrade.equals(other.fundQuantityTrade))
			return false;
		if (fundShare == null) {
			if (other.fundShare != null)
				return false;
		} else if (!fundShare.equals(other.fundShare))
			return false;
		if (id != other.id)
			return false;
		if (status == null) {
			if (other.status != null)
				return false;
		} else if (!status.equals(other.status))
			return false;
		if (updateBy == null) {
			if (other.updateBy != null)
				return false;
		} else if (!updateBy.equals(other.updateBy))
			return false;
		if (updateDate == null) {
			if (other.updateDate != null)
				return false;
		} else if (!updateDate.equals(other.updateDate))
			return false;
		if (userProdId == null) {
			if (other.userProdId != null)
				return false;
		} else if (!userProdId.equals(other.userProdId))
			return false;
		return true;
	}

}
