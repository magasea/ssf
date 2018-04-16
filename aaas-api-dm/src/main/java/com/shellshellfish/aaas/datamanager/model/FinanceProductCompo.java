package com.shellshellfish.aaas.datamanager.model;
/**
 * 理财产品组合实体类
 * @author developer4
 *
 */

import java.util.List;
import java.util.Map;


public class FinanceProductCompo {
	

	private String groupId;
	private String subGroupId;
	private String prdName;
	/*预期年化收益*/
	private String expAnnReturn; 
	/*预期最大回撤*/
	private String expMaxDrawDown="";
	/*产品组合饼图数据assetRatio*/
	private List productCompo;
	/*产品历史收益率*/
	private Map<String,Object> histYieldRate;
	private Map<String,Object> baseLine;
	public FinanceProductCompo(){}

	
	public FinanceProductCompo(String groupId, String subGroupId, String prdName, String expAnnReturn,
			List productCompo, Map<String, Object> histYieldRate,Map baseLine) {
		super();
		this.groupId = groupId;
		this.subGroupId = subGroupId;
		this.prdName = prdName;
		this.expAnnReturn = expAnnReturn;
		this.productCompo = productCompo;
		this.histYieldRate = histYieldRate;
		this.baseLine= baseLine;
	}
	public String getExpAnnReturn() {
		return expAnnReturn;
	}
	public void setExpAnnReturn(String expAnnReturn) {
		this.expAnnReturn = expAnnReturn;
	}
	public List getProductCompo() {
		return productCompo;
	}
	public void setProductCompo(List productCompo) {
		this.productCompo = productCompo;
	}
	public Map<String, Object> getHistYieldRate() {
		return histYieldRate;
	}
	public void setHistYieldRate(Map<String, Object> histYieldRate) {
		this.histYieldRate = histYieldRate;
	}

	public String getExpMaxDrawDown() {
		return expMaxDrawDown;
	}

	public void setExpMaxDrawDown(String expMaxDrawDown) {
		this.expMaxDrawDown = expMaxDrawDown;
	}
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public String getSubGroupId() {
		return subGroupId;
	}
	public void setSubGroupId(String subGroupId) {
		this.subGroupId = subGroupId;
	}
	public String getPrdName() {
		return prdName;
	}
	public void setPrdName(String prdName) {
		this.prdName = prdName;
	}

	public Map<String, Object> getBaseLine() {
		return baseLine;
	}

	public void setBaseLine(Map<String, Object> baseLine) {
		this.baseLine = baseLine;
	}


	@Override
	public String toString() {
		return "FinanceProductCompo{" +
				"groupId='" + groupId + '\'' +
				", subGroupId='" + subGroupId + '\'' +
				", prdName='" + prdName + '\'' +
				", expAnnReturn='" + expAnnReturn + '\'' +
				", expMaxDrawDown='" + expMaxDrawDown + '\'' +
				", productCompo=" + productCompo +
				", histYieldRate=" + histYieldRate +
				", baseLine=" + baseLine +
				'}';
	}
}
