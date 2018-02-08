package com.shellshellfish.aaas.dto;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.shellshellfish.aaas.model.MonetaryFund;
import com.shellshellfish.aaas.transfer.utils.EasyKit;

/**
 * 记录查询详情的所有信息
 *
 * @author developer4
 */
public class FundNAVInfo {
	/*平均增长率*/
	private String avgIncreRate;
	/*基金名*/
	private String name;
	/*基金代码*/
	private String fundCode;
	/*净值增长*/
	private List<Map> NPVIncrement;
	/*净值增长率*/
	private List<Map> NPVIncreRate;
	/*基金类型*/
	private String fundType;
	/*净值增长最小最大值*/
	private Map incrementMinMaxValueMap;

	/*净值增长率最小最大值*/
	private Map incrementRateMinMaxValueMap;

	/* 货币基金七日年化收益走势 */
	private List<Map> yieldof7days;
	/* 货币基金的万份收益走势*/
	private List<Map> tenKiloUnitYield;

	/* 是否时货币基金*/
	private Integer isMonetaryFund;
	
	private Map yieldof7daysMap;

	private Map tenKiloUnitYieldMap;
	
	public Map getIncrementMinMaxValueMap() {
		return incrementMinMaxValueMap;
	}

	public void setIncrementMinMaxValueMap(Object NPVIncrement) {
		if (NPVIncrement instanceof List) {
			List NPVIncrementList = (List) NPVIncrement;
			this.incrementMinMaxValueMap = EasyKit.getMaxMinValue(NPVIncrementList);
		} else if (NPVIncrement instanceof Map) {
			this.incrementMinMaxValueMap = (Map) NPVIncrement;
		}
	}


	public Map getIncrementRateMinMaxValueMap() {
		return incrementRateMinMaxValueMap;
	}


	public void setIncrementRateMinMaxValueMap(Object NPVIncreRate) {

		if (NPVIncreRate instanceof List) {
			List NPVIncreRateList = (List) NPVIncreRate;
			this.incrementRateMinMaxValueMap = EasyKit.getMaxMinValue(NPVIncreRateList);
		} else if (NPVIncreRate instanceof Map) {
			this.incrementRateMinMaxValueMap = (Map) NPVIncreRate;
		}
	}


	public String getAvgIncreRate() {
		return avgIncreRate;
	}


	public void setAvgIncreRate(String avgIncreRate) {
		this.avgIncreRate = avgIncreRate;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getFundCode() {
		return fundCode;
	}


	public void setFundCode(String fundCode) {
		this.fundCode = fundCode;
	}


	public List<Map> getNPVIncrement() {
		return NPVIncrement;
	}


	public void setNPVIncrement(List<Map> nPVIncrement) {
		NPVIncrement = nPVIncrement;
	}


	public List<Map> getNPVIncreRate() {
		return NPVIncreRate;
	}


	public void setNPVIncreRate(List<Map> nPVIncreRate) {
		NPVIncreRate = nPVIncreRate;
	}


	public String getFundType() {
		return fundType;
	}


	public void setFundType(String fundType) {
		this.fundType = fundType;
	}


	public List<Map> getYieldof7days() {
		return yieldof7days;
	}

	public void setYieldof7days(List<MonetaryFund> monetaryFunds) {
		if (monetaryFunds == null || monetaryFunds.isEmpty())
			yieldof7days = null;

		this.yieldof7days = new ArrayList<>(monetaryFunds.size());

		for (MonetaryFund fund : monetaryFunds) {
			Map map = new HashMap<>(2);
			map.put("time", dateFormat(fund.getQueryDateStr()));
			map.put("value", fund.getYieldof7days());
			this.yieldof7days.add(map);
		}
	}

	public List<Map> getTenKiloUnitYield() {
		return tenKiloUnitYield;
	}


	public void setTenKiloUnitYield(List<MonetaryFund> monetaryFunds) {
		if (monetaryFunds == null || monetaryFunds.isEmpty())
			tenKiloUnitYield = null;

		this.tenKiloUnitYield = new ArrayList<>(monetaryFunds.size());

		for (MonetaryFund fund : monetaryFunds) {
			Map map = new HashMap<>(2);
			map.put("time", dateFormat(fund.getQueryDateStr()));
			map.put("value", fund.getTenKiloUnitYield());
			this.tenKiloUnitYield.add(map);
		}
	}

	public Integer getIsMonetaryFund() {
		return isMonetaryFund;
	}

	public void setIsMonetaryFund(Integer isMonetaryFund) {
		this.isMonetaryFund = isMonetaryFund;
	}
	
	public Map getYieldof7daysMap() {
		return yieldof7daysMap;
	}

	public void setYieldof7daysMap(Map yieldof7daysMap) {
		this.yieldof7daysMap = yieldof7daysMap;
	}

	public Map getTenKiloUnitYieldMap() {
		return tenKiloUnitYieldMap;
	}

	public void setTenKiloUnitYieldMap(Map tenKiloUnitYieldMap) {
		this.tenKiloUnitYieldMap = tenKiloUnitYieldMap;
	}

	public void setIncrementMinMaxValueMap(Map incrementMinMaxValueMap) {
		this.incrementMinMaxValueMap = incrementMinMaxValueMap;
	}

	public void setIncrementRateMinMaxValueMap(Map incrementRateMinMaxValueMap) {
		this.incrementRateMinMaxValueMap = incrementRateMinMaxValueMap;
	}

	@Override
	public String toString() {
		return "FundNAVInfo{" +
				"avgIncreRate='" + avgIncreRate + '\'' +
				", name='" + name + '\'' +
				", fundCode='" + fundCode + '\'' +
				", NPVIncrement=" + NPVIncrement +
				", NPVIncreRate=" + NPVIncreRate +
				", fundType='" + fundType + '\'' +
				", incrementMinMaxValueMap=" + incrementMinMaxValueMap +
				", incrementRateMinMaxValueMap=" + incrementRateMinMaxValueMap +
				", yieldof7days=" + yieldof7days +
				", tenKiloUnitYield=" + tenKiloUnitYield +
				", isMonetaryFund=" + isMonetaryFund +
				'}';
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((fundCode == null) ? 0 : fundCode.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	/**
	 * 数据进行交换
	 *
	 * @param infoA
	 * @param infoB
	 * @return
	 */
	public static FundNAVInfo mergeIntoOne(FundNAVInfo infoA, FundNAVInfo infoB) {
		if (infoA.equals(infoB)) {
			Field[] fieldA = infoA.getClass().getDeclaredFields();
			Field[] fieldB = infoB.getClass().getDeclaredFields();
			for (int i = 0; i < fieldA.length; i++) {
				for (int j = 0; j < fieldB.length; j++) {
					if (fieldA[i].getName().equals(fieldB[j].getName())) {
						String fieldName = fieldA[i].getName();
						try {
							Method getMethod = FundNAVInfo.class.getDeclaredMethod("get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1));//获取get方法
							String strSetMethod = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
							Method setMethod = FundNAVInfo.class.getDeclaredMethod(strSetMethod, getMethodParamClass(strSetMethod));//获取set方法

							Object infoAValue = getMethod.invoke(infoA); //获取infoA的值
							Object infoBValue = getMethod.invoke(infoB);//获取infoB的属性值

							if (infoAValue == null && infoBValue != null) { //如果infoA的值为空，将infoB的值给infoA
								setMethod.invoke(infoA, infoBValue);
							}
						} catch (NoSuchMethodException e) {
							e.printStackTrace();
						} catch (SecurityException e) {
							e.printStackTrace();
						} catch (IllegalAccessException e) {
							e.printStackTrace();
						} catch (IllegalArgumentException e) {
							e.printStackTrace();
						} catch (InvocationTargetException e) {
							e.printStackTrace();
						}
					}
				}
			}
			return infoA;
		}
		return null;
	}

	/**
	 * 获取方法对应的Class
	 *
	 * @param methodName
	 * @return
	 */
	private static Class<?>[] getMethodParamClass(String methodName) {
		Method[] methods = FundNAVInfo.class.getDeclaredMethods();
		for (int i = 0; i < methods.length; i++) {
			if (methodName.equals(methods[i].getName())) {
				Class<?>[] paraTypeClass = methods[i].getParameterTypes();
				return paraTypeClass;
			}
		}
		return null;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FundNAVInfo other = (FundNAVInfo) obj;
		if (fundCode == null) {
			if (other.fundCode != null)
				return false;
		} else if (!fundCode.equals(other.fundCode))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}


	public static String dateFormat(String date) {
		if (date == null || date.length() != 10)
			return null;

		if (date.contains("/"))
			return date.replace('/', '-');

		if (date.contains("\\"))
			return date.replace('\\', '-');
		return date;
	}
}
