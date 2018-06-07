package com.shellshellfish.aaas.datamanager.service.impl;


import com.shellshellfish.aaas.common.enums.MonetaryFundEnum;
import com.shellshellfish.aaas.common.utils.InstantDateUtil;
import com.shellshellfish.aaas.datamanager.model.*;
import com.shellshellfish.aaas.datamanager.repositories.MongoGroupBaseRepository;
import com.shellshellfish.aaas.datamanager.repositories.mongo.*;
import com.shellshellfish.aaas.datamanager.service.DataService;
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

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

@Service
public class DataServiceImpl implements DataService {
    Logger logger = LoggerFactory.getLogger(getClass());

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
    MongoGroupBaseRepository mongoGroupBaseRepository;

    @Autowired
    MongoCoinFundYieldRateRepository coinFundYieldRateRepository;

    @Autowired
    private MongoTemplate mongoTemplate;


    public static final BigDecimal ONE_HUNDRED = BigDecimal.valueOf(100);

    public static final String PERCENT_SIGNS = "%";

    public List<FundCodes> getAllFundCodes() {
        //List<FundCodes> ftl=mongoFundCodesRepository.findByCodeAndDate("000001.OF","2017-11-12");
        return mongoFundCodesRepository.findAll();
    }


    public HashMap<String, Object> getFundManager(String name) {
        HashMap<String, Object> fmmap = null;
        List<FundManagers> lst = mongoFundManagersRepository.findByManagername(name);
        if (lst == null || lst.size() == 0) {
            logger.info("will return empty result for:{}", name);
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
            logger.info("will return empty result for:{}", code);
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
            logger.info("will return empty result for:{}", name);
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
        Long endTime = InstantDateUtil.getEpochSecondOfZero(endDate.plusDays(1)); //seconds
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
            amendResults(hnmap, "baselinehistoryprofitlist");
            amendResults(hnmap, "historyprofitlist");

        }
        hnmap.put("isMontaryFund", isMontaryFund);

        return hnmap;
    }

    /**
     * 所有数据以基金净值为基准对齐
     *
     * @param hnmap
     * @param key
     * @return
     */
    public boolean amendResults(Map hnmap, String key) {

        final String baseKey = "historynetlist";
        // Amend values
        Object target = hnmap.get(key);

        List<Map<String, Object>> targetList;
        if (target instanceof Map[]) {
            Map<String, Object>[] maps = (Map[]) target;
            targetList = new ArrayList<>(maps.length);
            for (Map map : maps) {
                targetList.add(map);
            }
        } else if (target instanceof List) {
            targetList = (List<Map<String, Object>>) target;
        } else {
            return false;
        }

        //以基金净值为基准进行数据对齐
        Map<String, Object>[] baselineList;
        Object base = hnmap.get(baseKey);
        if (base instanceof Map[]) {
            baselineList = (Map<String, Object>[]) base;
        } else if (base instanceof List) {
            List<Map<String, Object>> list = (List<Map<String, Object>>) base;
            baselineList = new HashMap[list.size()];
            list.toArray(baselineList);
        } else {
            logger.error("base is not Map or list:{}", base.getClass());
            return false;
        }


        HashSet<String> baseHashSet = new HashSet<>(baselineList.length);
        List<Map<String, Object>> targetBaseList = new ArrayList<>();
        for (Map<String, Object> map : baselineList) {
            String date = map.get("date").toString();
            if (!baseHashSet.contains(date)) {
                baseHashSet.add(date);
                targetBaseList.add(map);
            }
        }

        HashSet<String> targetHashSet = new HashSet<>(baseHashSet.size());
        for (Iterator iterator = targetList.iterator(); iterator.hasNext(); ) {
            Map<String, Object> map = (Map<String, Object>) iterator.next();
            String date = map.get("date").toString();
            if (!baseHashSet.contains(date) || targetHashSet.contains(date)) {
                iterator.remove();
            }
            targetHashSet.add(date);
        }
        hnmap.replace(baseKey, targetBaseList);
        hnmap.replace(key, targetList);
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


        LocalDate stdate = InstantDateUtil.format(date);


        BigDecimal net;
        if (MonetaryFundEnum.containsCode(code)) {
            net = BigDecimal.ONE;//货币基金单位净值为１
            CoinFundYieldRate coinFundYieldRate = coinFundYieldRateRepository
                    .findFirstByCodeAndQueryDateStrLessThanEqualOrderByQueryDateStrDesc(code, InstantDateUtil.format
                            (stdate, "yyyy/MM/dd").toString());
            hnmap.put("yieldOf7Days", coinFundYieldRate.getYieldOf7Days());
            hnmap.put("10kUnitYield", coinFundYieldRate.getTenKiloUnityYield());
        } else {
            long sttime = InstantDateUtil.getEpochSecondOfZero(stdate.plusDays(1)); //seconds
            Sort sort = new Sort(Sort.Direction.DESC, "querydate");
            FundYearIndicator fundYearIndicator = mongoFundYearIndicatorRepository
                    .getFirstByCodeAndQuerydateBefore(code, sttime, sort);
            net = fundYearIndicator.getNavunit();
        }
        hnmap.put("net", net);//当天单位净值
        String dayup = getUprate(code, stdate, 1).toString() + PERCENT_SIGNS; //a day ago
        String weekup = getUprate(code, stdate, 2).toString() + PERCENT_SIGNS; //a week ago
        String monthup = getUprate(code, stdate, 3).toString() + PERCENT_SIGNS; //a month ago
        String threemonthup = getUprate(code, stdate, 4).toString() + PERCENT_SIGNS; //3 month ago
        String sixmonthup = getUprate(code, stdate, 5).toString() + PERCENT_SIGNS; //6 month ago
        String thisyearup = getUprate(code, stdate, 8).toString() + PERCENT_SIGNS; //this year ago
        String oneyearup = getUprate(code, stdate, 6).toString() + PERCENT_SIGNS; //1 year ago
        String threeyearup = getUprate(code, stdate, 7).toString() + PERCENT_SIGNS; //3 year ago

        HashMap[] dmap = new HashMap[8];
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

    private BigDecimal getUprate(String code, LocalDate curdate, int type) {
        LocalDate befdate = null;
        Map map = getNavadjOfFund(code, curdate);
        curdate = (LocalDate) map.get("date");
        BigDecimal curval = (BigDecimal) map.get("navAdj");

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

        Map yesMap = getNavadjOfFund(code, befdate);
        BigDecimal yesval = (BigDecimal) yesMap.get("navAdj");
        if (yesval == null) {
            yesMap = getNavadjOfFundFirstDay(code);
            yesval = (BigDecimal) yesMap.get("navAdj");
        }
        BigDecimal up = BigDecimal.ZERO;
        if (yesval != null && BigDecimal.ZERO.compareTo(yesval) != 0) {
            up = ((curval.subtract(yesval)).divide(yesval, MathContext.DECIMAL32)).
                    multiply(BigDecimal.valueOf(100L));
        }
        return up.setScale(2, BigDecimal.ROUND_HALF_UP);

    }

    /**
     * 获取基金的基金复权单位净值
     *
     * @param code
     * @param date
     * @return
     */
    private Map<String, Object> getNavadjOfFund(String code, LocalDate date) {
        final String DATE_FORMAT = "yyyy/MM/dd";
        Map<String, Object> map = new HashMap<>(2);
        BigDecimal yesval = null;
        long endtime = InstantDateUtil.getEpochSecondOfZero(date.plusDays(1)); //seconds
        if (MonetaryFundEnum.containsCode(code)) {
            CoinFundYieldRate coinFundYieldRate = coinFundYieldRateRepository
                    .findFirstByCodeAndQueryDateStrLessThanEqualOrderByQueryDateStrDesc
                            (code, InstantDateUtil.format(date, DATE_FORMAT));
            if (coinFundYieldRate != null) {
                yesval = coinFundYieldRate.getNavAdj();
                String datestr = coinFundYieldRate.getQueryDateStr();
                if (datestr.contains("-"))
                    date = InstantDateUtil.format(datestr, "yyyy-MM-dd");
                else
                    date = InstantDateUtil.format(coinFundYieldRate.getQueryDateStr(), DATE_FORMAT);
            }
        } else {
            Sort sort = new Sort(Sort.Direction.DESC, "querydate");
            FundYearIndicator fundYearIndicator = mongoFundYearIndicatorRepository
                    .getFirstByCodeAndQuerydateBefore(code, endtime, sort);

            if (fundYearIndicator != null) {
                yesval = fundYearIndicator.getNavadj();
                date = InstantDateUtil.toLocalDate(fundYearIndicator.getQuerydate());
            }
        }
        map.put("date", date);
        map.put("navAdj", yesval);
        return map;
    }

    /**
     * 获取基金的基金复权单位净值
     *
     * @param code
     * @return
     */
    private Map<String, Object> getNavadjOfFundFirstDay(String code) {
        final String DATE_FORMAT = "yyyy/MM/dd";
        LocalDate date = InstantDateUtil.now();
        Map<String, Object> map = new HashMap<>(2);
        BigDecimal yesval = null;
        if (MonetaryFundEnum.containsCode(code)) {
            CoinFundYieldRate coinFundYieldRate = coinFundYieldRateRepository
                    .findFirstByCodeOrderByQueryDateStr(code);
            if (coinFundYieldRate != null) {
                yesval = coinFundYieldRate.getNavAdj();
                date = InstantDateUtil.format(coinFundYieldRate.getQueryDateStr(), DATE_FORMAT);
            }
        } else {
            FundYearIndicator fundYearIndicator = mongoFundYearIndicatorRepository.getFirstByCodeOrderByQuerydate(code);
            if (fundYearIndicator != null) {
                yesval = fundYearIndicator.getNavadj();
                date = InstantDateUtil.toLocalDate(fundYearIndicator.getQuerydate());
            }
        }
        map.put("date", date);
        map.put("navAdj", yesval);
        return map;
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
        } else {
            logger.error("no fundRate found for code:{}", code);
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
        } else {
            if (CollectionUtils.isEmpty(list)) {
                logger.error("FundResources is empty for code:{}", code);
            } else {
                logger.error("FundResources have multi records for code:{} and size:{}", code,
                        list.size());
            }
        }

        return "";

    }

    public String GetBaseName(String code) {
        Criteria criteria = Criteria.where("code").is(code);
        Query query = new Query(criteria);
        List<FundBaseList> list = mongoTemplate.find(query, FundBaseList.class);
        if (list != null && list.size() == 1) {
            return list.get(0).getBaseName();
        } else {
            if (CollectionUtils.isEmpty(list)) {
                logger.error("FundBaseList is empty for code:{}", code);
            } else {
                logger.error("FundBaseList have multi records for code:{} and size:{}", code,
                        list.size());
            }
        }

        return "";

    }


    /**
     * 非货币基金里斯净值以及历史净值增长率
     */
    private void getHistoryNetValue(Map result, String code, Long startTime, Long endTime) {
        //区间内查询
        Criteria criteria = Criteria.where("code").is(code).and("querydate").gt(startTime)
                .lt(endTime);
        Query query = new Query(criteria);
        query.with(new Sort(Sort.DEFAULT_DIRECTION.ASC, "querydate"));
        List<FundYearIndicator> list = mongoTemplate.find(query, FundYearIndicator.class);

        if (CollectionUtils.isEmpty(list)) {
            logger.error("empty list for FundYearIndicator with code:{} and time between:{} and :{}",
                    code, startTime, endTime);
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
            dmap[i].put("navAdj", navAdj.setScale(4, RoundingMode.HALF_UP));//复权单位净值

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
     * 获取单个基准数据
     */
    private void getBaseLine(Map result, String fundCode, Long startTime, Long endTime) {
        //银行一年定存固定为1.5
        final String oneYearRateOfBank = "1.5";
        BigDecimal oneDayRate = BigDecimal.valueOf(1.5)
                .divide(BigDecimal.valueOf(365), MathContext.DECIMAL128);
        List baseLineList = new ArrayList();//基准历史收益走势
        FundBaseList fundBase = mongoFundBaseListRepository.findFirstByCode(fundCode);
        if (fundBase == null) {
            logger.error("empty FundBaseList for fundCode:{}", fundCode);
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
            logger.info("fundBaseCloseList.size():{}", fundBaseCloseList.size());
            for (int i = 0; i < fundBaseCloseList.size(); i++) {
                FundBaseClose fundBaseClose = fundBaseCloseList.get(i);

                Map map = new HashMap(2);
                map.put("date", fundBaseClose.getQueryDateStr());
                BigDecimal dayUp = BigDecimal.ZERO;
                String baseName = fundBase.getBaseLine();

                switch (baseName) {
                    case "GDAXIGI":
                        BigDecimal gdaxiGi = fundBaseClose.getGDAXIGI();
                        if (gdaxiGi == null)
                            continue;
                        dayUp = fundBaseClose.getGDAXIGI()
                                .subtract(startBaseClose.getGDAXIGI())
                                .divide(startBaseClose.getGDAXIGI(), MathContext.DECIMAL128);
                        break;
                    case "000300SH":
                        BigDecimal sh300 = fundBaseClose.getSH300();
                        if (sh300 == null)
                            continue;
                        dayUp = fundBaseClose.getSH300()
                                .subtract(startBaseClose.getSH300())
                                .divide(startBaseClose.getSH300(), MathContext.DECIMAL128);
                        break;
                    case "300SH_6_CSI_4":
                        BigDecimal sh300_6_csi_4 = fundBaseClose.getSH300_6_CSI_4();
                        if (sh300_6_csi_4 == null)
                            continue;
                        dayUp = fundBaseClose.getSH300_6_CSI_4()
                                .subtract(startBaseClose.getSH300_6_CSI_4())
                                .divide(startBaseClose.getSH300_6_CSI_4(), MathContext.DECIMAL128);
                        break;
                    case "300SH_4_CSI_6":
                        BigDecimal sh300_4_csi_6 = fundBaseClose.getSH300_4_CSI_6();
                        if (sh300_4_csi_6 == null)
                            continue;
                        dayUp = fundBaseClose.getSH300_4_CSI_6()
                                .subtract(startBaseClose.getSH300_4_CSI_6())
                                .divide(startBaseClose.getSH300_4_CSI_6(), MathContext.DECIMAL128);
                        break;
                    case "300SH_5_CSI_5":
                        BigDecimal sh300_5_csi_5 = fundBaseClose.getSH300_5_CSI_5();
                        if (sh300_5_csi_5 == null)
                            continue;
                        dayUp = fundBaseClose.getSH300_5_CSI_5()
                                .subtract(startBaseClose.getSH300_5_CSI_5())
                                .divide(startBaseClose.getSH300_5_CSI_5(), MathContext.DECIMAL128);
                        break;
                    case "H11001CSI":
                        BigDecimal h11001csi = fundBaseClose.getH11001CSI();
                        if (h11001csi == null)
                            continue;
                        dayUp = fundBaseClose.getH11001CSI()
                                .subtract(startBaseClose.getH11001CSI())
                                .divide(startBaseClose.getH11001CSI(), MathContext.DECIMAL128);
                        break;
                    case "000905SH":
                        BigDecimal sh905 = fundBaseClose.getSH905();
                        if (sh905 == null)
                            continue;
                        dayUp = fundBaseClose.getSH905()
                                .subtract(startBaseClose.getSH905())
                                .divide(startBaseClose.getSH905(), MathContext.DECIMAL128);
                        break;
                    case "H11025CSI":
                        BigDecimal h11025csi = fundBaseClose.getH11025CSI();
                        if (h11025csi == null)
                            continue;
                        dayUp = fundBaseClose.getH11025CSI()
                                .subtract(startBaseClose.getH11025CSI())
                                .divide(startBaseClose.getH11025CSI(), MathContext.DECIMAL128);
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
     * 获取组合基准
     */
    @Override
    public Map<String, Object> getGroupBaseLine(Long groupId, Long startTime, Long endTime) {
        //银行一年定存固定为1.5
        Map<String, Object> result = new HashMap();
        final String oneYearRateOfBank = "1.5";
        BigDecimal oneDayRate = BigDecimal.valueOf(1.5)
                .divide(BigDecimal.valueOf(365), MathContext.DECIMAL128);
        List baseLineList = new ArrayList();//基准历史收益走势
        GroupBase groupBase = mongoGroupBaseRepository.findFirstByGroupId(groupId);
        if (groupBase == null) {
            return null;
        }
        result.put("baseName", groupBase.getBaseName());
        result.put("baseCode", groupBase.getBaseLine());
        if (oneYearRateOfBank.equals(groupBase.getBaseLine())) {
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

            if (CollectionUtils.isEmpty(fundBaseCloseList))
                logger.error("fundBaseCloseList is null groupId:{},startTime:{},endTime:{}", groupId, startTime, endTime);

            FundBaseClose startBaseClose = fundBaseCloseList.get(0);

            for (int i = 0; i < fundBaseCloseList.size(); i++) {
                FundBaseClose fundBaseClose = fundBaseCloseList.get(i);

                Map map = new HashMap(2);
                map.put("date", fundBaseClose.getQueryDateStr());
                BigDecimal dayUp = BigDecimal.ZERO;
                String baseName = groupBase.getBaseLine();

                switch (baseName) {
                    case "GDAXIGI":
                        BigDecimal gdaxiGi = fundBaseClose.getGDAXIGI();
                        if (gdaxiGi == null)
                            continue;
                        dayUp = gdaxiGi
                                .subtract(startBaseClose.getGDAXIGI())
                                .divide(startBaseClose.getGDAXIGI(), MathContext.DECIMAL128);
                        break;
                    case "000300SH":
                        BigDecimal sh300 = fundBaseClose.getSH300();
                        if (sh300 == null)
                            continue;
                        dayUp = fundBaseClose.getSH300()
                                .subtract(startBaseClose.getSH300())
                                .divide(startBaseClose.getSH300(), MathContext.DECIMAL128);
                        break;
                    case "300SH_6_CSI_4":
                        BigDecimal sh300_6_csi_4 = fundBaseClose.getSH300_6_CSI_4();
                        if (sh300_6_csi_4 == null)
                            continue;
                        dayUp = fundBaseClose.getSH300_6_CSI_4()
                                .subtract(startBaseClose.getSH300_6_CSI_4())
                                .divide(startBaseClose.getSH300_6_CSI_4(), MathContext.DECIMAL128);
                        break;
                    case "300SH_4_CSI_6":
                        BigDecimal sh300_4_csi_6 = fundBaseClose.getSH300_4_CSI_6();
                        if (sh300_4_csi_6 == null)
                            continue;
                        dayUp = fundBaseClose.getSH300_4_CSI_6()
                                .subtract(startBaseClose.getSH300_4_CSI_6())
                                .divide(startBaseClose.getSH300_4_CSI_6(), MathContext.DECIMAL128);
                        break;
                    case "300SH_5_CSI_5":
                        BigDecimal sh300_5_csi_5 = fundBaseClose.getSH300_5_CSI_5();
                        if (sh300_5_csi_5 == null)
                            continue;
                        dayUp = fundBaseClose.getSH300_5_CSI_5()
                                .subtract(startBaseClose.getSH300_5_CSI_5())
                                .divide(startBaseClose.getSH300_5_CSI_5(), MathContext.DECIMAL128);
                        break;
                    case "H11001CSI":
                        BigDecimal h11001csi = fundBaseClose.getH11001CSI();
                        if (h11001csi == null)
                            continue;
                        dayUp = fundBaseClose.getH11001CSI()
                                .subtract(startBaseClose.getH11001CSI())
                                .divide(startBaseClose.getH11001CSI(), MathContext.DECIMAL128);
                        break;
                    case "000905SH":
                        BigDecimal sh905 = fundBaseClose.getSH905();
                        if (sh905 == null)
                            continue;
                        dayUp = fundBaseClose.getSH905()
                                .subtract(startBaseClose.getSH905())
                                .divide(startBaseClose.getSH905(), MathContext.DECIMAL128);
                        break;
                    case "H11025CSI":
                        BigDecimal h11025csi = fundBaseClose.getH11025CSI();
                        if (h11025csi == null)
                            continue;
                        dayUp = fundBaseClose.getH11025CSI()
                                .subtract(startBaseClose.getH11025CSI())
                                .divide(startBaseClose.getH11025CSI(), MathContext.DECIMAL128);
                        break;
                    default:
                }
                map.put("dayup",
                        dayUp.multiply(ONE_HUNDRED).setScale(2, BigDecimal.ROUND_HALF_UP));
                baseLineList.add(map);
            }
        }
        result.put("value", baseLineList);
        return result;
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
        if (CollectionUtils.isEmpty(coinFundYieldRateList)) {
            logger.error("coinFundYieldRateList is empty for code:{} startTime:{}, endTime:{}",
                    code, startTime, endTime);
        }
        logger.info("coinFundYieldRateList size:{}", coinFundYieldRateList.size());
        for (int i = 0; i < coinFundYieldRateList.size(); i++) {

            CoinFundYieldRate coinFundYieldRate = coinFundYieldRateList.get(i);

            //七日年化可能为空
            BigDecimal yieldOf7Days = coinFundYieldRate.getYieldOf7Days();

            Map map = new HashMap<String, Object>(3);
            map.put("date",
                    InstantDateUtil
                            .format(InstantDateUtil.toLocalDate(coinFundYieldRate.getQuerydate()), "yyyy-MM-dd"));
            map.put("yieldOf7Days", coinFundYieldRate.getYieldOf7Days());
            map.put("tenKiloUnitYield", coinFundYieldRate.getTenKiloUnityYield());

            BigDecimal todayNavAdj = getNavAdjOfCoinFundYield(coinFundYieldRateList, i);


            map.put("navAdj", todayNavAdj);

            BigDecimal dayUp;
            BigDecimal dayUpRate = BigDecimal.ZERO;
            if (i != 0) {
                BigDecimal yesterdayNavAdj = getNavAdjOfCoinFundYield(coinFundYieldRateList, i - 1);
                dayUp = todayNavAdj.subtract(yesterdayNavAdj);

                if (BigDecimal.ZERO.compareTo(yesterdayNavAdj) != 0) {
                    dayUpRate = dayUp.divide(yesterdayNavAdj, MathContext.DECIMAL128);
                }
            }

            map.put("dayup",
                    dayUpRate.multiply(ONE_HUNDRED).setScale(6, BigDecimal.ROUND_HALF_UP).toString() + "%");

            BigDecimal p2 = getNavAdjOfCoinFundYield(coinFundYieldRateList, 0); //起始日复权净值
            BigDecimal profit = (todayNavAdj.subtract(p2)).divide(p2, MathContext.DECIMAL128);//收益走势
            map.put("profit", profit.multiply(ONE_HUNDRED).setScale(6, RoundingMode.HALF_UP));

            result[i] = map;
            if (yieldOf7Days != null)
                yieldOf7DaysList.add(coinFundYieldRate.getYieldOf7Days());

            if (coinFundYieldRate.getTenKiloUnityYield() != null)
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

    private BigDecimal getNavAdjOfCoinFundYield(List<CoinFundYieldRate> coinFundYieldRateList, int index) {

        BigDecimal todayNavAdj = null;
        int i = index;
        //数据有错误，使用前一天数据补充, 监控系统由监控人员调查之后补充数据
        while (todayNavAdj == null && i >= 0) {
            logger.error("货币基金复权单位净值数据错误：{}", coinFundYieldRateList.get(i));
            todayNavAdj = coinFundYieldRateList.get(i).getNavAdj();
            i--;
        }

        if (todayNavAdj == null)
            todayNavAdj = BigDecimal.ONE;

        return todayNavAdj;
    }
}

