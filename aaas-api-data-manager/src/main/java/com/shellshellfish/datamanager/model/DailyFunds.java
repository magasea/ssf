package com.shellshellfish.datamanager.model;

import javax.persistence.Id;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Document(collection = "dailyfunds")
public class DailyFunds {
	
	@Id
	private String id;
	@Field("code")
	private String code;    //基金代码
	@Field("update")
	private String update;    //更新日期
	@Field("querydate")
	private String querydate;    //查询日期
	
	@Field("navadj")
	private String navadj;  //复权单位净值
	@Field("navlatestdate")
	private String navlatestdate; //最新净值日期
	@Field("fundscale")
	private String fundscale; //基金规模
	@Field("navunit")
	private String navunit; //单位净值
	@Field("navaccum")
	private String navaccum; //累计单位净值
	@Field("yieldof7days")
	private String yieldof7days; //7日年化收益率
	@Field("10kunityield")
	private String tenkunityield;//万份基金单位收益
	@Field("bmindexchgpct")
	private String bmindexchgpct;//标的指数涨跌幅
	
	public String getCode() {
	    return code;
	}

	public void setCode(String code) {
	    this.code = code;
	}

	public String getUpdate() {
	    return update;
	}

	public void setUpdate(String date) {
	    this.update = date;
	}
	
	public String getQuerydate() {
	    return querydate;
	}

	public void setQuerydate(String date) {
	    this.querydate = date;
	}

	public String getNavadj() {
	    return navadj;
	}

	public void setNavadj(String navadj) {
	    this.navadj = navadj;
	}
	

	public String getNavlatestdate() {
	    return navlatestdate;
	}

	public void setNavlatestdate(String navlatestdate) {
	    this.navlatestdate = navlatestdate;
	}
	
	
	public String getFundscale() {
	    return fundscale;
	}

	public void setFundscale(String fundscale) {
	    this.fundscale = fundscale;
	}
	
	public String getNavunit() {
	    return navunit;
	}

	public void setNavunit(String navunit) {
	    this.navunit = navunit;
	}

	public String getNavaccum() {
	    return navaccum;
	}

	public void setNavaccum(String navaccum) {
	    this.navaccum = navaccum;
	}


	public String getYieldof7days() {
	    return yieldof7days;
	}

	public void setYieldof7days(String yieldof7days) {
	    this.yieldof7days = yieldof7days;
	}
	
	public String getTenkunityield() {
	    return tenkunityield;
	}

	public void setTenkunityield(String tenkunityield) {
	    this.tenkunityield = tenkunityield;
	}
	

	public String getBmindexchgpct() {
	    return bmindexchgpct;
	}

	public void setBmindexchgpct(String bmindexchgpct) {
	    this.bmindexchgpct = bmindexchgpct;
	}

	
	
}
