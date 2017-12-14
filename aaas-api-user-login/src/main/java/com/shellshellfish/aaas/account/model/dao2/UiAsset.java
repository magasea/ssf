package com.shellshellfish.aaas.account.model.dao2;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import javax.persistence.*;

@Entity
@Table(name="ui_asset")
@NamedQuery(name="UiAsset.findAll", query="SELECT u FROM UiAsset u")
public class UiAsset implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "UI_ASSET_ID_GENERATOR")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "UI_ASSET_ID_GENERATOR")
	private String id;

	@Column(name = "created_by")
	private String createdBy;

	@Column(name = "created_date")
	private BigInteger createdDate;

	@Column(name = "daily_profit")
	private BigDecimal dailyProfit;

	@Column(name = "last_modified_by")
	private String lastModifiedBy;

	@Column(name = "last_modified_date")
	private BigInteger lastModifiedDate;

	@Column(name = "total_assets")
	private BigDecimal totalAssets;

	@Column(name = "total_profit")
	private BigDecimal totalProfit;

	@Column(name = "user_id")
	private Long userId;

	public UiAsset() {
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public BigInteger getCreatedDate() {
		return this.createdDate;
	}

	public void setCreatedDate(BigInteger createdDate) {
		this.createdDate = createdDate;
	}

	public BigDecimal getDailyProfit() {
		return this.dailyProfit;
	}

	public void setDailyProfit(BigDecimal dailyProfit) {
		this.dailyProfit = dailyProfit;
	}

	public String getLastModifiedBy() {
		return this.lastModifiedBy;
	}

	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	public BigInteger getLastModifiedDate() {
		return this.lastModifiedDate;
	}

	public void setLastModifiedDate(BigInteger lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public BigDecimal getTotalAssets() {
		return this.totalAssets;
	}

	public void setTotalAssets(BigDecimal totalAssets) {
		this.totalAssets = totalAssets;
	}

	public BigDecimal getTotalProfit() {
		return this.totalProfit;
	}

	public void setTotalProfit(BigDecimal totalProfit) {
		this.totalProfit = totalProfit;
	}

	public Long getUserId() {
		return this.userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}
}
