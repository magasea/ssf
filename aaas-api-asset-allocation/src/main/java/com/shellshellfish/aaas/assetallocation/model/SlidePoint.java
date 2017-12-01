package com.shellshellfish.aaas.assetallocation.model;

public class SlidePoint {
	private Integer Id;
	private Double value;
	
	public SlidePoint() {
		
	}
	
	public SlidePoint(Integer id, Double value) {
		super();
		Id = id;
		this.value = value;
	}
	
	public Integer getId() {
		return Id;
	}
	public void setId(Integer id) {
		Id = id;
	}
	public Double getValue() {
		return value;
	}
	public void setValue(Double value) {
		this.value = value;
	}
	
	
}
