package com.shellshellfish.datamanager.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.Id;

/**
 * 日行情
 */
@Getter
@Setter
@Document(collection = "dayindicator")
public class DayIndicatorDTO {

	@Id
	private String id;
	@Field("avgprice")
	private String avgprice;
	@Field("discountrate")
	private String discountrate;
	@Field("low")
	private String low;
	@Field("differrange")
	private String differrange;//涨跌幅
	@Field("open")
	private String open;
	@Field("update")
	private String update;
	@Field("high")
	private String high;
	@Field("amplitude")
	private String amplitude;//振幅
	@Field("turn")
	private String turn;
	@Field("close")
	private String close;
	@Field("amount")
	private String amount;
	@Field("differ")
	private String differ;
	@Field("code")
	private String code;
	@Field("querydate")
	private String querydate;
	@Field("volume")
	private String volume;
	@Field("discount")
	private String discount;//贴水
	@Field("preclose")
	private String preclose;


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAvgprice() {
		return avgprice;
	}

	public void setAvgprice(String avgprice) {
		this.avgprice = avgprice;
	}

	public String getDiscountrate() {
		return discountrate;
	}

	public void setDiscountrate(String discountrate) {
		this.discountrate = discountrate;
	}

	public String getLow() {
		return low;
	}

	public void setLow(String low) {
		this.low = low;
	}

	public String getDifferrange() {
		return differrange;
	}

	public void setDifferrange(String differrange) {
		this.differrange = differrange;
	}

	public String getOpen() {
		return open;
	}

	public void setOpen(String open) {
		this.open = open;
	}

	public String getUpdate() {
		return update;
	}

	public void setUpdate(String update) {
		this.update = update;
	}

	public String getHigh() {
		return high;
	}

	public void setHigh(String high) {
		this.high = high;
	}

	public String getAmplitude() {
		return amplitude;
	}

	public void setAmplitude(String amplitude) {
		this.amplitude = amplitude;
	}

	public String getTurn() {
		return turn;
	}

	public void setTurn(String turn) {
		this.turn = turn;
	}

	public String getClose() {
		return close;
	}

	public void setClose(String close) {
		this.close = close;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getDiffer() {
		return differ;
	}

	public void setDiffer(String differ) {
		this.differ = differ;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getQuerydate() {
		return querydate;
	}

	public void setQuerydate(String querydate) {
		this.querydate = querydate;
	}

	public String getVolume() {
		return volume;
	}

	public void setVolume(String volume) {
		this.volume = volume;
	}

	public String getDiscount() {
		return discount;
	}

	public void setDiscount(String discount) {
		this.discount = discount;
	}

	public String getPreclose() {
		return preclose;
	}

	public void setPreclose(String preclose) {
		this.preclose = preclose;
	}
}
