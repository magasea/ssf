package com.shellshellfish.aaas.assetallocation.model;

public class RiskControl {
	
	public RiskControl(Integer id, String name, Double level2RiskControl, Double benchmark) {
		super();
		this.setId(id);
		this.setName(name);
		this.setLevel2RiskControl(level2RiskControl);
		this.setBenchmark(benchmark);
	}
	
	private Integer id;
	
	private String name;
	private Double level2RiskControl;
	private Double benchmark;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public Double getLevel2RiskControl() {
		return level2RiskControl;
	}

	public void setLevel2RiskControl(Double level2RiskControl) {
		this.level2RiskControl = level2RiskControl;
	}

	public Double getBenchmark() {
		return benchmark;
	}

	public void setBenchmark(Double benchmark) {
		this.benchmark = benchmark;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

}
