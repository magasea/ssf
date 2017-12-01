package com.shellshellfish.aaas.assetallocation.model;

import java.util.List;

import com.shellshellfish.aaas.assetallocation.util.NameValuePair;

public class SimulationData {
	private Integer productGroupId;
	private Integer productSubGroupId;
	private List<NameValuePair<String, Double>> values;
	
	public SimulationData() {
		
	}
	
	public Integer getProductGroupId() {
		return productGroupId;
	}
	public void setProductGroupId(Integer productGroupId) {
		this.productGroupId = productGroupId;
	}
	public Integer getProductSubGroupId() {
		return productSubGroupId;
	}
	public void setProductSubGroupId(Integer productSubGroupId) {
		this.productSubGroupId = productSubGroupId;
	}
	public SimulationData(Integer productGroupId, Integer productSubGroupId) {
		super();
		this.productGroupId = productGroupId;
		this.productSubGroupId = productSubGroupId;
	}
	public List<NameValuePair<String, Double>> getValues() {
		return values;
	}
	public void setValues(List<NameValuePair<String, Double>> values) {
		this.values = values;
	}
	
}
