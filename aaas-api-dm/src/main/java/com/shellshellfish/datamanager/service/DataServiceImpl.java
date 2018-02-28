package com.shellshellfish.datamanager.service;


import com.shellshellfish.aaas.common.enums.FundRiskLevelEnum;
import com.shellshellfish.aaas.common.enums.MonetaryFundEnum;
import com.shellshellfish.aaas.common.utils.InstantDateUtil;
import com.shellshellfish.datamanager.commons.DateUtil;
import com.shellshellfish.datamanager.model.CoinFundYieldRate;
import com.shellshellfish.datamanager.model.FundBaseClose;
import com.shellshellfish.datamanager.model.FundBaseList;
import com.shellshellfish.datamanager.model.FundCodes;
import com.shellshellfish.datamanager.model.FundCompanys;
import com.shellshellfish.datamanager.model.FundManagers;
import com.shellshellfish.datamanager.model.FundRate;
import com.shellshellfish.datamanager.model.FundResources;
import com.shellshellfish.datamanager.model.FundYearIndicator;
import com.shellshellfish.datamanager.repositories.MongoFundBaseCloseRepository;
import com.shellshellfish.datamanager.repositories.MongoFundBaseListRepository;
import com.shellshellfish.datamanager.repositories.MongoFundCodesRepository;
import com.shellshellfish.datamanager.repositories.MongoFundCompanysRepository;
import com.shellshellfish.datamanager.repositories.MongoFundManagersRepository;
import com.shellshellfish.datamanager.repositories.MongoFundYearIndicatorRepository;
import com.shellshellfish.datamanager.repositories.MongoListedFundCodesRepository;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.LinkedList;
import java.util.Map;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

@Service
public class DataServiceImpl implements DataService {

	@Autowired
	MongoFundCodesRepository mongoFundCodesRepository;

	@Autowired
	MongoListedFundCodesRepository mongoListedFundCodesRepository;


	@Autowired
	MongoFundManagersRepository mongoFundManagersRepository;

	@Autowired
	MongoFundCompanysRepository mongoFundCompanysRepository;

	@Autowired
	MongoFundYearIndicatorRepository mongoFundYearIndicatorRepository;

	@Autowired
	MongoFundBaseCloseRepository mongoFundBaseCloseRepository;

	@Autowired
	MongoFundBaseListRepository mongoFundBaseListRepository;

	@Autowired
	private MongoTemplate mongoTemplate;

	private Logger logger = LoggerFactory.getLogger(DataServiceImpl.class);


	public static final BigDecimal ONE_HUNDRED = BigDecimal.valueOf(100);


	public List<FundCodes> getAllFundCodes() {
		//List<FundCodes> ftl=mongoFundCodesRepository.findByCodeAndDate("000001.OF","2017-11-12");
		return mongoFundCodesRepository.findAll();
	}


	public HashMap<String, Object> getFundManager(String name) {
		HashMap<String, Object> fmmap = null;
		List<FundManagers> lst = mongoFundManagersRepository.findByManagername(name);
		if (lst == null || lst.size() == 0) {
			return new HashMap<String, Object>();
		} else {
			fmmap = new HashMap<String, Object>();
			fmmap.put("manager", lst.get(0).getMnager());
			if ("——".equals(lst.get(0).getAvgearningrate())) {
				fmmap.put("avgearningrate", "0");
			} else {
				fmmap.put("avgearningrate", lst.get(0).getAvgearningrate());
			}
			fmmap.put("workingdays", lst.get(0).getWorkingdays());
			fmmap.put("fundnum", lst.size());
			HashMap[] dmap = new HashMap[lst.size()];
			for (int i = 0; i < lst.size(); i++) {
				dmap[i] = new HashMap<String, String>();

				dmap[i].put("fundname", lst.get(i).getFundname());
				dmap[i].put("startdate", lst.get(i).getStartdate());
				dmap[i].put("earingrate", lst.get(i).getEarningrate());
			}
			fmmap.put("joblist", dmap);

		}

		return fmmap;

	}

	//基金概况信息
	public HashMap<String, Object> getFundInfoBycode(String code) {

		if (!code.contains("OF") && !code.contains("SH") && !code.contains("SZ")) {
			code = code + ".OF";
		}

		List<FundCompanys> lst = mongoFundCompanysRepository.findByCode(code);
		HashMap<String, Object> dmap = new HashMap<String, Object>();

		if (lst == null || lst.size() == 0) {
			return new HashMap<String, Object>();
		} else {

			dmap.put("fundcompany", lst.get(0).getCompanyname());//公司名称

			dmap.put("fundname", lst.get(0).getFundname());//基金名称
			dmap.put("fundtype", lst.get(0).getFundtype());//基金类型
			dmap.put("code", lst.get(0).getCode());
			dmap.put("manager", lst.get(0).getManager());//基金经理

			dmap.put("scale", lst.get(0).getScale());//最新资产净值(亿元)
			dmap.put("fundscale", lst.get(0).getFundScale());//最新基金份额(亿份)
			dmap.put("fundnum", lst.size());//基金总数
			dmap.put("createdate", lst.get(0).getCreatedate());//成立日期

			Criteria criteria = Criteria.where("code").is(code);
			Query query = new Query(criteria);
			query.with(new Sort(Sort.DEFAULT_DIRECTION.DESC, "queryenddate"));
			List<FundResources> reslist = mongoTemplate.find(query, FundResources.class);

			dmap.put("custobank", "");

			if (reslist != null && reslist.size() == 1) {
				dmap.put("custobank", reslist.get(0).getCustodianbank());
			}


		}

		return dmap;

	}

	//基金公司信息
	public HashMap<String, Object> getFundCompanyDetailInfo(String name) {
		HashMap<String, Object> fmmap = null;
		List<FundCompanys> lst = mongoFundCompanysRepository.findByCompanyname(name);
		if (lst == null || lst.size() == 0) {
			return new HashMap<String, Object>();
		} else {
			fmmap = new HashMap<String, Object>();
			fmmap.put("fundcompany", lst.get(0).getCompanyname());
			fmmap.put("scale", lst.get(0).getScale());//最新资产净值(亿元)
			fmmap.put("fundscale", lst.get(0).getFundScale());//最新基金份额(亿份)
			fmmap.put("fundnum", lst.size());
			fmmap.put("createdate", lst.get(0).getCreatedate());
			HashMap[] dmap = new HashMap[lst.size()];
			String[] codes = new String[lst.size()];

			for (int i = 0; i < lst.size(); i++) {
				dmap[i] = new HashMap<String, String>();
				dmap[i].put("fundname", lst.get(i).getFundname());
				dmap[i].put("fundtype",
						lst.get(i).getFundtype());//+"|||"+getYearscale(lst.get(i).getCode()); //还需要一个年化收益率

				dmap[i].put("code", lst.get(i).getCode());
				codes[i] = lst.get(i).getCode();
			}

			double[] netvallst = getYearscale(codes);
			if (netvallst != null) {
				for (int i = 0; i < dmap.length; i++) {
					String code = (String) dmap[i].get("code");

					String val;
					try {
						val = String.format("%.2f", netvallst[i] * 100); //区间累计单位净值增长率)
						dmap[i].put("accumnet", val + "%");
					} catch (Exception e) {
						dmap[i].put("accumnet", "0.0%");
					}
				}
			}
			fmmap.put("fundlist", dmap);

		}

		return fmmap;

	}

	//不分场内和场外基金,使用同一的计算方式：

	//unsued:场内基金(SH,SZ):区间涨跌幅
	//unused:场外基金(OF):区间复权单位净值增长率

	public double[] getYearscale(String[] codes) {

		Date d = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String sdate = sdf.format(d);
		String yearstr = sdate.substring(0, 4);
		String stdate = yearstr + "-01-01";

		String enddate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());//today
		long sttime = 0;
		long endtime = 0;
		try {

			Date stdated = sdf.parse(stdate);

			Calendar cal = Calendar.getInstance();
			cal.setTime(stdated);//date 换成已经已知的Date对象
			//cal.add(Calendar.HOUR_OF_DAY, -8);// before 8 hour (GMT 8)
			Date e = cal.getTime();
			sttime = e.getTime() / 1000;

			Date enddated = sdf.parse(enddate);

			cal.setTime(enddated);
			//cal.add(Calendar.HOUR_OF_DAY, -8);// before 8 hour (GMT 8)
			e = cal.getTime();
			endtime = e.getTime() / 1000;

			//sttime=sttime-18000; //diff in python and java
			//endtime=endtime-18000;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return null;
		}

		double[] vallst = new double[codes.length];
		for (int i = 0; i < codes.length; i++) {
			//区间内查询
			Criteria criteria = Criteria.where("code").is(codes[i]).and("querydate").gte(sttime)
					.lte(endtime);
			Query query = new Query(criteria);
			query.with(new Sort(Sort.DEFAULT_DIRECTION.ASC, "querydate"));
			List<FundYearIndicator> list = mongoTemplate.find(query, FundYearIndicator.class);

			if (list != null && list.size() >= 2) {
				BigDecimal x1 = list.get(list.size() - 1).getNavadj().subtract(list.get(0).getNavadj());
				BigDecimal x2 = list.get(0).getNavadj();
				double accunet = x1.divide(x2, MathContext.DECIMAL128).doubleValue();

				vallst[i] = accunet;
			}
		}

		return vallst;
	}


	//历史净值,收益走势
	public HashMap<String, Object> getHistoryNetvalue(String code, String type, String settingdate) {

		logger.debug("=================================================================");
		logger.debug("code:{},type:{},date:{}", code, type, settingdate);

		if (!code.contains("OF") && !code.contains("SH") && !code.contains("SZ")) {
			code = code + ".OF";
		}
		HashMap<String, Object> hnmap = new HashMap<String, Object>();
		hnmap.put("code", code);
		hnmap.put("period", type);
		//基金名称
		String name = GetSimpleName(code);
		hnmap.put("fundname", name);
		//基准名称
		String basename = GetBaseName(code);
		hnmap.put("basename", basename);

		LocalDate endDate = InstantDateUtil.format(settingdate);
		Long endTime = InstantDateUtil.getEpochSecondOfZero(endDate); //seconds
		LocalDate startDate = endDate.plusDays(-1);
		switch (type) {
			case "1":
				startDate = startDate.plusMonths(-3);//3 month
				break;
			case "2":
				startDate = startDate.plusMonths(-6);// 6 month
				break;
			case "3":
				startDate = startDate.plusYears(-1);//1  year
				break;
			case "4":
				startDate = startDate.plusYears(-3); // 3 years
				break;
			default:
				startDate = endDate;
		}
		Long startTime = InstantDateUtil.getEpochSecondOfZero(startDate); //seconds

		//获取基准走势
		getBaseLine(hnmap, code, startTime, endTime);

		Integer isMontaryFund = 0;//是否式货币基金
		if (MonetaryFundEnum.containsCode(code)) {
			getYieldOf7DaysAndTenKiloUnitYield(hnmap, code, startTime, endTime);
			isMontaryFund = 1;
		} else {
			//获取净值收益走势
			getHistoryNetValue(hnmap, code, startTime, endTime);
			//货币基金没有这两条数据
			amendResults(hnmap, "historynetlist");
			amendResults(hnmap, "historyprofitlist");

		}
		hnmap.put("isMontaryFund", isMontaryFund);

		return hnmap;
	}

	public boolean amendResults(Map hnmap, String key) {

		// Amend values
		Map<String, Object>[] historyNetValueList = (Map<String, Object>[]) hnmap.get(key);
		List<Map<String, Object>> baselineHistoryProfitList = (List<Map<String, Object>>) hnmap
				.get("baselinehistoryprofitlist");

		try {
			List<Date> datesToAmend = new ArrayList<Date>();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

			for (Map<String, Object> baseline : baselineHistoryProfitList) {
				String baselineDateStr = baseline.get("date").toString();
				Date baselineDate = sdf.parse(baselineDateStr);

				int i = 0;
				for (i = 0; i < historyNetValueList.length; i++) {
					String valueDateStr = historyNetValueList[i].get("date").toString();
					Date valueDate = sdf.parse(valueDateStr);
					if (valueDate.equals(baselineDate)) {
						break;
					}
				}

				if (i == historyNetValueList.length) {
					// not found, amend
					datesToAmend.add(baselineDate);
				}

			}

			List<Map<String, Object>> listCopy = new LinkedList<Map<String, Object>>();
			for (int i = 0; i < historyNetValueList.length; i++) {
				listCopy.add(historyNetValueList[i]);
			}

			int amendCount = 0;
			int tmp = 0;
			for (Date date : datesToAmend) {
				for (int i = tmp; i < historyNetValueList.length; i++) {
					Date valueDate = sdf.parse(historyNetValueList[i].get("date").toString());
					// TODO: historyNetValueList must be ascend order
					if (valueDate.after(date)) {
						Map<String, Object> dateCopy;
						if (i > 0) {
							dateCopy = new HashMap<String, Object>(
									listCopy.get(i + amendCount - 1));
						} else {
							dateCopy = new HashMap<String, Object>(
									listCopy.get(i + amendCount));
						}
						dateCopy.replace("date", sdf.format(date));
						listCopy.add(i + amendCount, dateCopy);
						amendCount++;
						tmp = i;
						break;
					}
				}
			}

			Map<String, Object>[] arrayCopy = (Map<String, Object>[]) listCopy
					.toArray(new Map[listCopy.size()]);
			hnmap.replace(key, arrayCopy);

		} catch (ParseException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	//日涨幅,近一年涨幅,净值,分级类型,评级

	public HashMap<String, Object> getFundValueInfo(String code, String date) {

		if (!code.contains("OF") && !code.contains("SH") && !code.contains("SZ")) {
			code = code + ".OF";
		}

		HashMap<String, Object> hnmap = new HashMap<>();
		hnmap.put("code", code);
		HashMap<String, String> typemap = getClassType(code);
		hnmap.put("classtype", typemap.get("classtype"));//分级类型
		hnmap.put("investtype", typemap.get("investtype"));//投资类型
		hnmap.put("rate", getRate(code));//评级
		hnmap.put("net", 0);//当天净值
		hnmap.put("navreturnrankingp", 0);//基金排名

		HashMap[] dmap = new HashMap[8];

		LocalDate stdate = InstantDateUtil.format(date);
		long sttime = InstantDateUtil.getEpochSecondOfZero(stdate.plusDays(1)); //seconds

		Sort sort = new Sort(Sort.Direction.DESC, "querydate");
		FundYearIndicator fundYearIndicator = mongoFundYearIndicatorRepository
				.getFirstByCodeAndQuerydateBefore(code, sttime, sort);
		BigDecimal curdayval;

		String dayup = "0.0%";
		String weekup = "0.0%";
		String monthup = "0.0%";
		String threemonthup = "0.0%";
		String sixmonthup = "0.0%";
		String oneyearup = "0.0%";
		String threeyearup = "0.0%";
		String thisyearup = "0.0%";
		if (fundYearIndicator != null) {
			curdayval = fundYearIndicator.getNavadj();
			stdate = InstantDateUtil.toLocalDate(fundYearIndicator.getQuerydate());
			hnmap.put("net", fundYearIndicator.getNavunit().doubleValue());//当天单位净值
			dayup = getUprate(code, curdayval, stdate, 1).toString() + "%"; //a day ago
			weekup = getUprate(code, curdayval, stdate, 2).toString() + "%"; //a week ago
			monthup = getUprate(code, curdayval, stdate, 3).toString() + "%"; //a month ago
			threemonthup = getUprate(code, curdayval, stdate, 4).toString() + "%"; //3 month ago
			sixmonthup = getUprate(code, curdayval, stdate, 5).toString() + "%"; //6 month ago
			thisyearup = getUprate(code, curdayval, stdate, 8).toString() + "%"; //this year ago
			oneyearup = getUprate(code, curdayval, stdate, 6).toString() + "%"; //1 year ago
			threeyearup = getUprate(code, curdayval, stdate, 7).toString() + "%"; //3 year ago

		}

		dmap[0] = new HashMap<String, String>();
		dmap[0].put("time", "日涨幅");
		dmap[0].put("val", dayup);
		dmap[0].put("type", "dayIncrease");

		dmap[1] = new HashMap<String, String>();
		dmap[1].put("time", "近一周");
		dmap[1].put("val", weekup);
		dmap[1].put("type", "weekIncrease");

		dmap[2] = new HashMap<String, String>();
		dmap[2].put("time", "近一月");
		dmap[2].put("val", monthup);
		dmap[2].put("type", "month1Increase");

		dmap[3] = new HashMap<String, String>();
		dmap[3].put("time", "近三月");
		dmap[3].put("val", threemonthup);
		dmap[3].put("type", "month3Increase");

		dmap[4] = new HashMap<String, String>();
		dmap[4].put("time", "近六月");
		dmap[4].put("val", sixmonthup);
		dmap[4].put("type", "month6Increase");

		dmap[5] = new HashMap<String, String>();
		dmap[5].put("time", "近一年");
		dmap[5].put("val", oneyearup);
		dmap[5].put("type", "year1Increase");

		dmap[6] = new HashMap<String, String>();
		dmap[6].put("time", "近三年");
		dmap[6].put("val", threeyearup);
		dmap[6].put("type", "year3Increase");

		dmap[7] = new HashMap<String, String>();
		dmap[7].put("time", "今年来");
		dmap[7].put("val", thisyearup);
		dmap[7].put("type", "yearIncrease");

		hnmap.put("uplist", dmap);

		hnmap.put("dayIncrease", dayup);
		hnmap.put("yearIncrease", oneyearup);

		return hnmap;
	}


	public BigDecimal getUprate(String code, BigDecimal curval, LocalDate curdate, int type) {
		LocalDate befdate = null;
		switch (type) {
			case 1:
				befdate = curdate.plusDays(-1);//昨天日期
				break;
			case 2:
				befdate = curdate.plusWeeks(-1);//前一周
				break;
			case 3:
				befdate = curdate.plusMonths(-1);//前一月
				break;
			case 4:
				befdate = curdate.plusMonths(-3);//前三月
				break;
			case 5:
				befdate = curdate.plusMonths(-6);//前六月
				break;
			case 6:
				befdate = curdate.plusYears(-1);//前一年
				break;
			case 7:
				befdate = curdate.plusYears(-3);//前三年
				break;
			case 8:
				befdate = LocalDate.of(curdate.getYear(), 1, 1);//本年年初
				break;
			default:
				befdate = LocalDate.now();
		}

		long endtime = InstantDateUtil.getEpochSecondOfZero(befdate.plusDays(1)); //seconds

		Sort sort = new Sort(Sort.Direction.DESC, "querydate");

		FundYearIndicator fundYearIndicator = mongoFundYearIndicatorRepository
				.getFirstByCodeAndQuerydateBefore(code, endtime, sort);

		BigDecimal yesval = Optional.ofNullable(fundYearIndicator).map(m -> m.getNavadj())
				.orElse(BigDecimal.ZERO);
		BigDecimal up = BigDecimal.ZERO;
		if (BigDecimal.ZERO.compareTo(yesval) != 0) {
			up = ((curval.subtract(yesval)).divide(yesval, MathContext.DECIMAL128)).
					multiply(BigDecimal.valueOf(100L));
		}
		return up.setScale(2, BigDecimal.ROUND_HALF_UP);

	}

	public HashMap<String, String> getClassType(String code) {

		Criteria criteria = Criteria.where("code").is(code);
		Query query = new Query(criteria);
		query.with(new Sort(Sort.DEFAULT_DIRECTION.DESC, "queryenddate"));
		List<FundResources> list = mongoTemplate.find(query, FundResources.class);

		String ret = "";
		String ret2 = "";
		if (list != null && list.size() == 1) {
			ret = list.get(0).getRisklevel();
			ret2 = list.get(0).getFirstinvesttype();
		}

		HashMap<String, String> map = new HashMap<>();

		map.put("classtype", Optional.ofNullable(ret).orElse("-"));
		map.put("investtype", ret2);
		return map;
	}

	public String getRate(String code) {
		Criteria criteria = Criteria.where("code").is(code);
		Query query = new Query(criteria);
		query.with(new Sort(Sort.DEFAULT_DIRECTION.DESC, "querydate"));
		List<FundRate> list = mongoTemplate.find(query, FundRate.class);
		String rate = "暂无评级";
		if (!CollectionUtils.isEmpty(list) && list.get(0) != null && !list.get(0)
				.getShstockstar3ycomrat().isEmpty()) {
			rate = list.get(0).getShstockstar3ycomrat();
		}

		return rate;
	}

	public String GetSimpleName(String code) {
		Criteria criteria = Criteria.where("code").is(code);
		Query query = new Query(criteria);
		query.with(new Sort(Sort.DEFAULT_DIRECTION.DESC, "queryenddate"));
		List<FundResources> list = mongoTemplate.find(query, FundResources.class);
		if (list != null && list.size() == 1) {
			return list.get(0).getName();
		}

		return "";

	}

	public String GetBaseName(String code) {
		Criteria criteria = Criteria.where("code").is(code);
		Query query = new Query(criteria);
		List<FundBaseList> list = mongoTemplate.find(query, FundBaseList.class);
		if (list != null && list.size() == 1) {
			return list.get(0).getBaseName();
		}

		return "";

	}


	/**
	 * 非货币基金里斯净值以及历史净值增长率
	 */
	private void getHistoryNetValue(Map result, String code, Long startTime, Long endTime) {
		//区间内查询
		Criteria criteria = Criteria.where("code").is(code).and("querydate").gte(startTime)
				.lte(endTime);
		Query query = new Query(criteria);
		query.with(new Sort(Sort.DEFAULT_DIRECTION.ASC, "querydate"));
		List<FundYearIndicator> list = mongoTemplate.find(query, FundYearIndicator.class);

		if (list == null || list.size() == 0) {
			return;
		}

		HashMap<String, Object>[] profitmap = new HashMap[list.size()];//收益走势
		HashMap<String, Object>[] dmap = new HashMap[list.size()];//历史净值

		for (int i = 0; i < list.size(); i++) {

			dmap[i] = new HashMap<>();
			profitmap[i] = new HashMap<>();

			String qd = InstantDateUtil.format(InstantDateUtil.toLocalDate(list.get(i).getQuerydate()));
			BigDecimal dayup = BigDecimal.ZERO;
			BigDecimal profit;

			BigDecimal navUnit = Optional.ofNullable(list.get(i)).map(m -> m.getNavunit())
					.orElse(BigDecimal.ZERO);
			BigDecimal navAccum = Optional.ofNullable(list.get(i)).map(m -> m.getNavaccum())
					.orElse(BigDecimal.ZERO);

			BigDecimal navAdj = Optional.ofNullable(list.get(i)).map(m -> m.getNavadj())
					.orElse(BigDecimal.ZERO);

			if (i != 0) {
				BigDecimal d2 = Optional.ofNullable(list.get(i - 1)).map(m -> m.getNavunit())
						.orElse(BigDecimal.ONE);
				dayup = (navUnit.subtract(d2)).divide(d2, MathContext.DECIMAL128)
						.multiply(ONE_HUNDRED);//日涨幅
			}

			BigDecimal p2 = Optional.ofNullable(list.get(0)).map(m -> m.getNavadj())
					.orElse(BigDecimal.ONE); //起始日累计净值
			profit = (navAdj.subtract(p2)).divide(p2, MathContext.DECIMAL128);//日涨幅

			dmap[i].put("navunit", navUnit.setScale(4, BigDecimal.ROUND_HALF_UP)); //单位净值
			dmap[i].put("navaccum", navAccum.setScale(4, RoundingMode.HALF_UP));//累计净值
			dmap[i].put("navAdj", navAdj.setScale(4, RoundingMode.HALF_UP));//累计净值

			dmap[i].put("date", qd);
			dmap[i].put("dayup", dayup.setScale(2, BigDecimal.ROUND_HALF_UP).toString() + "%");
			profitmap[i]
					.put("profit", profit.multiply(ONE_HUNDRED).setScale(2, BigDecimal.ROUND_HALF_UP));
			profitmap[i].put("date", qd);
		}

		result.put("historynetlist", dmap); //净值列表
		//历史收益走势
		result.put("historyprofitlist", profitmap); //历史收益走势


	}

	/**
	 * 获取基准数据
	 */
	private void getBaseLine(Map result, String fundCode, Long startTime, Long endTime) {
		//银行一年定存固定为1.5
		final String oneYearRateOfBank = "1.5";
		BigDecimal oneDayRate = BigDecimal.valueOf(1.5)
				.divide(BigDecimal.valueOf(365), MathContext.DECIMAL128);
		List baseLineList = new ArrayList();//基准历史收益走势
		FundBaseList fundBase = mongoFundBaseListRepository.findFirstByCode(fundCode);
		if (fundBase == null) {
			return;
		}
		if (oneYearRateOfBank.equals(fundBase.getBaseLine())) {
			LocalDate startDate = InstantDateUtil.toLocalDate(startTime);
			LocalDate endDate = InstantDateUtil.toLocalDate(endTime);

			// 过滤工作日
			BigDecimal dayNum = BigDecimal.ZERO;
			while (startDate.isBefore(endDate.plusDays(1))) {
				Map map = new HashMap(2);
				map.put("date", InstantDateUtil.format(startDate));
				map.put("dayup", oneDayRate.multiply(dayNum).setScale(2, BigDecimal.ROUND_HALF_UP));
				dayNum = dayNum.add(BigDecimal.ONE);
				startDate = startDate.plusDays(1);
				baseLineList.add(map);
			}
		} else {
			List<FundBaseClose> fundBaseCloseList = mongoFundBaseCloseRepository
					.findByQueryDateBetween(startTime, endTime, new Sort(Direction.ASC, "querydate"));

			FundBaseClose startBaseClose = fundBaseCloseList.get(0);

			for (int i = 0; i < fundBaseCloseList.size(); i++) {
				FundBaseClose fundBaseClose = fundBaseCloseList.get(i);

				Map map = new HashMap(2);
				map.put("date", fundBaseClose.getQueryDateStr());
				BigDecimal dayUp = BigDecimal.ZERO;
				String baseName = fundBase.getBaseLine();

				switch (baseName) {
					case "GDAXIGI":
						dayUp = fundBaseClose.getGDAXIGI()
								.subtract(startBaseClose.getGDAXIGI())
								.divide(startBaseClose.getGDAXIGI(), MathContext.DECIMAL128);
						break;
					case "000300SH":
						dayUp = fundBaseClose.getSH300()
								.subtract(startBaseClose.getSH300())
								.divide(startBaseClose.getSH300(), MathContext.DECIMAL128);
						break;
					case "300SH_6_CSI_4":
						dayUp = fundBaseClose.getSH300_6_CSI_4()
								.subtract(startBaseClose.getSH300_6_CSI_4())
								.divide(startBaseClose.getSH300_6_CSI_4(), MathContext.DECIMAL128);
						break;
					case "300SH_4_CSI_6":
						dayUp = fundBaseClose.getSH300_4_CSI_6()
								.subtract(startBaseClose.getSH300_4_CSI_6())
								.divide(startBaseClose.getSH300_4_CSI_6(), MathContext.DECIMAL128);
						break;
					case "300SH_5_CSI_5":
						dayUp = fundBaseClose.getSH300_5_CSI_5()
								.subtract(startBaseClose.getSH300_5_CSI_5())
								.divide(startBaseClose.getSH300_5_CSI_5(), MathContext.DECIMAL128);
						break;
					case "H11001CSI":
						dayUp = fundBaseClose.getH11001CSI()
								.subtract(startBaseClose.getH11001CSI())
								.divide(startBaseClose.getH11001CSI(), MathContext.DECIMAL128);
						break;
					case "000905SH":
						dayUp = fundBaseClose.getSH905()
								.subtract(startBaseClose.getSH905())
								.divide(startBaseClose.getSH905(), MathContext.DECIMAL128);
						break;
					default:
				}
				map.put("dayup",
						dayUp.multiply(ONE_HUNDRED).setScale(2, BigDecimal.ROUND_HALF_UP));
				baseLineList.add(map);
			}
		}

		result.put("baselinehistoryprofitlist", baseLineList);//基准历史收益走势
	}

	/**
	 * 货币基金7日年华以及附权单位净值
	 */
	private void getYieldOf7DaysAndTenKiloUnitYield(Map hnMap, String code, Long startTime,
			Long endTime) {
		Criteria criteria = Criteria.where("code").is(code)
				.and("querydate").gte(startTime).lte(endTime);
		Query query = new Query(criteria);
		query.with(new Sort(Sort.DEFAULT_DIRECTION.ASC, "querydate"));
		List<CoinFundYieldRate> coinFundYieldRateList = mongoTemplate
				.find(query, CoinFundYieldRate.class);

		Map<String, Object>[] result = new HashMap[coinFundYieldRateList.size() + 1];

		List<BigDecimal> yieldOf7DaysList = new ArrayList();
		List<BigDecimal> yieldOfTenKiloUnitYieldList = new ArrayList();
		for (int i = 0; i < coinFundYieldRateList.size(); i++) {

			Map map = new HashMap<String, Object>(3);
			CoinFundYieldRate coinFundYieldRate = coinFundYieldRateList.get(i);
			map.put("date",
					InstantDateUtil
							.format(InstantDateUtil.toLocalDate(coinFundYieldRate.getQuerydate()), "yyyy.MM.dd"));
			map.put("yieldOf7Days", coinFundYieldRate.getYieldOf7Days());
			map.put("tenKiloUnitYield", coinFundYieldRate.getTenKiloUnityYield());

			BigDecimal todayNavAdj = coinFundYieldRate.getNavAdj();
			map.put("navAdj", todayNavAdj);

			BigDecimal dayUp;
			BigDecimal dayUpRate = BigDecimal.ZERO;
			if (i != 0) {
				BigDecimal yesterdayNavAdj = Optional.ofNullable(coinFundYieldRateList.get(i - 1))
						.map(m -> m.getNavAdj()).orElse(BigDecimal.ZERO);
				dayUp = todayNavAdj.subtract(yesterdayNavAdj);

				if (BigDecimal.ZERO.compareTo(yesterdayNavAdj) != 0) {
					dayUpRate = dayUp.divide(yesterdayNavAdj, MathContext.DECIMAL128);
				}
			}

			map.put("dayup",
					dayUpRate.multiply(ONE_HUNDRED).setScale(2, BigDecimal.ROUND_HALF_UP).toString() + "%");

			BigDecimal p2 = Optional.ofNullable(coinFundYieldRateList.get(0)).map(m -> m.getNavAdj())
					.orElse(BigDecimal.ONE); //起始日复权净值
			BigDecimal profit = (todayNavAdj.subtract(p2)).divide(p2, MathContext.DECIMAL128);//收益走势
			map.put("profit", profit.setScale(2, RoundingMode.HALF_UP));

			result[i] = map;
			yieldOf7DaysList.add(coinFundYieldRate.getYieldOf7Days());
			yieldOfTenKiloUnitYieldList.add(coinFundYieldRate.getTenKiloUnityYield());
		}

		Map<String, Object> maxAndMinMap = new HashMap<>();
		maxAndMinMap.put("yieldOf7DaysMax", Collections.max(yieldOf7DaysList));
		maxAndMinMap.put("yieldOf7DaysMin", Collections.min(yieldOf7DaysList));
		maxAndMinMap.put("yieldOfTenKiloUnitYieldMax", Collections.max(yieldOfTenKiloUnitYieldList));
		maxAndMinMap.put("yieldOfTenKiloUnitYieldMin", Collections.min(yieldOfTenKiloUnitYieldList));
		result[result.length - 1] = maxAndMinMap;

		hnMap.put("yieldOf7DaysAndTenKiloUnitYield", result);
	}
}

