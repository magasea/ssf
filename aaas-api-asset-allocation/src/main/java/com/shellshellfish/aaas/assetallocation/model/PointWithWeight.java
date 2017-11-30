package com.shellshellfish.aaas.assetallocation.model;

public class PointWithWeight {
	private Integer id;
	private Double x;
	private Double y;
	private Double w;
	
		
	public PointWithWeight(Integer id, Double x, Double y, Double w) {
		super();
		this.id = id;
		this.x = x;
		this.y = y;
		this.w = w;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Double getX() {
		return x;
	}
	public void setX(Double x) {
		this.x = x;
	}
	public Double getY() {
		return y;
	}
	public void setY(Double y) {
		this.y = y;
	}
	public Double getW() {
		return w;
	}
	public void setW(Double w) {
		this.w = w;
	}
	
}
