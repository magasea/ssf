package com.shellshellfish.aaas.datamanager.model;

public class MonetaryFund {
	private Long queryDate;
	private String code;
	private Double tenKiloUnitYield;
	private Long update;
	private String queryDateStr;
	private Double yieldof7days;


	public Long getQueryDate() {
		return queryDate;
	}

	public void setQueryDate(Long queryDate) {
		this.queryDate = queryDate;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Double getTenKiloUnitYield() {
		return tenKiloUnitYield;
	}

	public void setTenKiloUnitYield(Double tenKiloUnitYield) {
		this.tenKiloUnitYield = tenKiloUnitYield;
	}

	public Long getUpdate() {
		return update;
	}

	public void setUpdate(Long update) {
		this.update = update;
	}

	public String getQueryDateStr() {
		return queryDateStr;
	}

	public void setQueryDateStr(String queryDateStr) {
		this.queryDateStr = queryDateStr;
	}

	public Double getYieldof7days() {
		return yieldof7days;
	}

	public void setYieldof7days(Double yieldof7days) {
		this.yieldof7days = yieldof7days;
	}
}
