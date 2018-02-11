package com.shellshellfish.aaas.userinfo.model.dao;

import java.math.BigDecimal;
import java.util.Objects;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * @Author pierre 18-2-1
 */
public class DailyAmountAggregation {


	@Field("_id")
	private Long id;


	private BigDecimal asset;

	private BigDecimal bonus;

	private BigDecimal sellAmount;

	private BigDecimal buyAmount;

	public BigDecimal getAsset() {
		return asset;
	}

	public void setAsset(BigDecimal asset) {
		this.asset = asset;
	}

	public BigDecimal getBonus() {
		return bonus;
	}

	public void setBonus(BigDecimal bonus) {
		this.bonus = bonus;
	}

	public BigDecimal getSellAmount() {
		return sellAmount;
	}

	public void setSellAmount(BigDecimal sellAmount) {
		this.sellAmount = sellAmount;
	}

	public BigDecimal getBuyAmount() {
		return buyAmount;
	}

	public void setBuyAmount(BigDecimal buyAmount) {
		this.buyAmount = buyAmount;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "DailyAmountAggregation{" +
				"id=" + id +
				", asset=" + asset +
				", bonus=" + bonus +
				", sellAmount=" + sellAmount +
				", buyAmount=" + buyAmount +
				'}';
	}

	@Override
	public boolean equals(Object o) {

		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		DailyAmountAggregation that = (DailyAmountAggregation) o;
		return Objects.equals(id, that.id) &&
				Objects.equals(asset, that.asset) &&
				Objects.equals(bonus, that.bonus) &&
				Objects.equals(sellAmount, that.sellAmount) &&
				Objects.equals(buyAmount, that.buyAmount);
	}

	@Override
	public int hashCode() {

		return Objects.hash(id, asset, bonus, sellAmount, buyAmount);
	}

	public static DailyAmountAggregation getEmptyInstance() {
		final DailyAmountAggregation instance = new DailyAmountAggregation();
		instance.setSellAmount(BigDecimal.ZERO);
		instance.setBuyAmount(BigDecimal.ZERO);
		instance.setBonus(BigDecimal.ZERO);
		instance.setAsset(BigDecimal.ZERO);
		return instance;
	}
}
