package com.shellshellfish.aaas.finance.model.dto;

import java.util.List;

/**
 * @Author pierre
 * 18-1-2
 */
public class FundCompany {
	String scale;
	String companyName;
	Integer fundnum;
	List<FundDetail> fundDetails;

	public String getScale() {
		return scale;
	}

	public void setScale(String scale) {
		this.scale = scale;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}


	public Integer getFundnum() {
		return fundnum;
	}

	public void setFundnum(Integer fundnum) {
		this.fundnum = fundnum;
	}

	public List<FundDetail> getFundDetails() {
		return fundDetails;
	}

	public void setFundDetails(List<FundDetail> fundDetails) {
		this.fundDetails = fundDetails;
	}


	@Override
	public String toString() {
		return "FundCompany{" +
				"scale='" + scale + '\'' +
				", companyName='" + companyName + '\'' +
				", fundnum=" + fundnum +
				", fundDetails=" + fundDetails.toString() +
				'}';
	}


	public class FundDetail {
		String fundName;
		String fundType;
		String Markup;

		public String getFundName() {
			return fundName;
		}

		public void setFundName(String fundName) {
			this.fundName = fundName;
		}

		public String getFundType() {
			return fundType;
		}

		public void setFundType(String fundType) {
			this.fundType = fundType;
		}

		public String getMarkup() {
			return Markup;
		}

		public void setMarkup(String markup) {
			Markup = markup;
		}

		public FundDetail(String fundName, String fundType, String markup) {
			this.fundName = fundName;
			this.fundType = fundType;
			Markup = markup;
		}

		@Override
		public String toString() {
			return "{" +
					"fundName='" + fundName + '\'' +
					", fundType='" + fundType + '\'' +
					", Markup='" + Markup + '\'' +
					'}';
		}
	}
}
