package com.shellshellfish.aaas.assetallocation.model;

public class RiskControl {
	
	public RiskControl(String event, Double level2RiskControl, Double benchmark) {
		super();
		this.setEvent(event);
		this.setLevel2RiskControl(level2RiskControl);
		this.setBenchmark(benchmark);
	}
	
	private String event;
	private Double level2RiskControl;
	private Double benchmark;
	
	public String getEvent() {
		return event;
	}
	public void setEvent(String event) {
		this.event = event;
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

}
