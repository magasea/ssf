package com.shellshellfish.aaas.assetallocation.neo.service;

import com.shellshellfish.aaas.assetallocation.neo.entity.*;
import com.shellshellfish.aaas.assetallocation.neo.mapper.FundGroupMapper;
import com.shellshellfish.aaas.assetallocation.neo.returnType.*;
import com.shellshellfish.aaas.assetallocation.neo.util.CalculateMaxdrawdowns;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.shellshellfish.aaas.assetallocation.neo.util.MVO;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by wangyinuo on 2017/11/27.
 */
@Service
public class FundGroupService {
    @Autowired
    private FundGroupMapper fundGroupMapper;

    /**
     * 查询所有基金组合
     *
     * @return
     */
    public FundAllReturn selectAllFundGroup() {
        List<Interval> fundGroupNum = fundGroupMapper.selectAllFundGroupNum();
        List<Map<String, Object>> list = new ArrayList<>();
        FundAllReturn far = new FundAllReturn();
        if (fundGroupNum.size() != 0) {
            for(Interval interval : fundGroupNum){
                Map<String, Object> _items = new HashMap<>();
                Map<String, String> query = new HashMap<>();
                query.put("fund_group_id", interval.getFund_group_id());
                List<RiskIncomeInterval> riskIncomeIntervalList = fundGroupMapper.getPerformanceVolatility(query);
                RiskIncomeInterval riskIncomeInterval = riskIncomeIntervalList.get(riskIncomeIntervalList.size()/2);
                query.put("id", riskIncomeInterval.getFund_group_id());
                query.put("subId", riskIncomeInterval.getId());
                List<Interval> intervals = fundGroupMapper.getProportion(query);
                //基金组合内的各基金权重
                for (Interval inter : intervals) {
                    Map<String, Object> assetsRatios = new HashMap<>();
                    assetsRatios.put("type",inter.getFund_type_two());
                    assetsRatios.put("value", inter.getProportion());
                    _items.put("assetsRatios", assetsRatios);//组合内各基金权重
                }
                _items.put("groupId", interval.getFund_group_id());
                _items.put("subGroupId", riskIncomeInterval.getId());
                _items.put("name", interval.getFund_group_name());
                list.add(_items);
            }
            far.setName("基金组合");
            far.set_total(fundGroupNum.size());
            far.set_items(list);
            far.set_schemaVersion("0.1.1");
            far.set_serviceId("资产配置");
        }
        return far;
    }

    /**
     * 产品类别比重
     * @param fund_group_id
     * @param fund_group_sub_id
     * @return
     */
    public ReturnType getProportionOne(String fund_group_id, String fund_group_sub_id){
        ReturnType fr = new ReturnType();
        List<Map<String, Object>> listMap = new ArrayList<>();
        Map<String, String> _links = new HashMap<>();
        Map<String, String> query = new HashMap<>();
        query.put("id", fund_group_id);
        query.put("subId", fund_group_sub_id);
        List<Interval> intervals = fundGroupMapper.getProportionOne(query);
        for(Interval interval :intervals){
            Map<String, Object> map = new HashMap<>();
            map.put("type",interval.getFund_type_one());
            map.put("value",interval.getProportion());
            listMap.add(map);
        }
        fr.set_total(listMap.size());
        fr.setName("产品类别比重");
        fr.set_items(listMap);
        fr.set_links(_links);
        fr.set_schemaVersion("0.1.1");
        fr.set_serviceId("资产配置");
        return fr;
    }

    public ReturnType getPerformanceVolatilityHomePage() {
        ReturnType fr = new ReturnType();
        List<Map<String, Object>> listMap = new ArrayList<>();
        Map<String, String> _links = new HashMap<>();
        Map<String, Object> map = new HashMap<>();
        for (int i = 1; i < 6; i++) {
            PerformanceVolatilityReturn pfvr = getPerformanceVolatility("C" + i, "2");
            map.put("C" + i, pfvr);
        }
        listMap.add(map);
        fr.set_items(listMap);
        fr.set_links(_links);
        fr.set_schemaVersion("0.1.1");
        fr.set_serviceId("资产配置");
        return fr;
    }

    /**
     * 按照ID查询基金组合明细
     *
     * @param id
     * @return
     */
    public FundReturn selectById(String id, String subGroupId) {
        FundReturn fr = null;
        Map<String, String> query = new HashMap<>();
        query.put("id", id);
        query.put("subGroupId", subGroupId);
        List<Interval> interval = fundGroupMapper.selectById(query);
        if (interval.size() != 0) {
            fr = getFundReturn(interval);
        }
        return fr;
    }

    /**
     * 预期收益率调整 风险率调整 最优组合(有效前沿线)
     *
     * @param id
     * @param riskValue
     * @param returnValue
     * @return
     */
    public FundReturn getinterval(String id, String riskValue, String returnValue) {
        FundReturn fr = null;
        Map<String, Object> map = new HashMap<>();
        map.put("riskValue", riskValue);
        map.put("returnValue", returnValue);
        map.put("id", id);
        List<Interval> interval = fundGroupMapper.getinterval(map);
        if (interval.size() != 0) {
            fr = getFundReturn(interval);
        }
        return fr;
    }

    /**
     * 预期年化收益(action=calcExpectedAnnualizedReturn), 预期最大回撤(action=calcExpectedMaxPullback)
     *
     * @param id
     * @param returntype
     * @param subGroupId
     * @return
     */
    public Map<String, Object> selectReturnAndPullback(String id, String returntype, String subGroupId) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("subGroupId", subGroupId);
        Interval interval = fundGroupMapper.selectReturnAndPullback(map);
        map.clear();
        if (interval != null) {
            if (returntype.equalsIgnoreCase("1")) {
                map.put("name", "预期年化收益");
                map.put("value", interval.getExpected_annualized_return());
            } else {
                map.put("name", "预期最大回撤");
                map.put("value", interval.getExpected_max_retracement());
            }
        }
        return map;
    }

    /**
     * 配置收益贡献
     *
     * @return
     */
    public ReturnType getRevenueContribution(String id, String subGroupId) {
        ReturnType rcb = new ReturnType();
        Map<String, String> _links = new HashMap<>();
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        map.put("subGroupId", subGroupId);
        map.put("id", id);
        List<Interval> itr = fundGroupMapper.getRevenueContribution(map);
        if (itr.size() != 0) {
            for (int i = 0; i < itr.size(); i++) {
                Map<String, Object> _items = new HashMap<>();
                _items.put("id", i + 1);
                _items.put("name", itr.get(i).getFund_type_two());
                _items.put("value", itr.get(i).getRevenue_contribution());
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
                String startTime = sdf.format(itr.get(i).getDetails_last_mod_time());
                Calendar ca = Calendar.getInstance();
                ca.add(Calendar.DATE,-1);
                String endTime = sdf.format(ca.getTime());
                _items.put("time",startTime+"~"+endTime);
                list.add(_items);
            }
            rcb.setName("配置收益贡献");
            rcb.set_total(itr.size());
            rcb.set_items(list);
            rcb.set_links(_links);
            rcb.set_schemaVersion("0.1.1");
            rcb.set_serviceId("资产配置");
        }
        return rcb;
    }

    /**
     * 有效前沿线
     *
     * @return
     */
    public ReturnType efficientFrontier(String id) {
        ReturnType aReturn = new ReturnType();
        Map<String, String> _links = new HashMap<>();
        List<Map<String, Object>> list = new ArrayList<>();
        List<RiskIncomeInterval> riskIncomeIntervalList = fundGroupMapper.getScaleMark(id,"risk_num");
        if (riskIncomeIntervalList.size()!=0) {
            for (int i = 1; i < 11;i++){
                Map<String, Object> _items = new HashMap<>();
                _items.put("id", i);
                //_items.put("x", riskIncomeIntervalList.get(10*i-1).getRisk_num());
                //_items.put("y", riskIncomeIntervalList.get(10*i-1).getIncome_num());
                _items.put("x", riskIncomeIntervalList.get(i-1).getRisk_num());
                _items.put("y", riskIncomeIntervalList.get(i-1).getIncome_num());
                list.add(_items);
            }
            aReturn.setName("有效前沿线数据");
            aReturn.set_items(list);
            aReturn.set_total(10);
            aReturn.set_links(_links);
            aReturn.set_schemaVersion("0.1.1");
            aReturn.set_serviceId("资产配置");
        }
        return aReturn;
    }

    /**
     * 风险控制
     *
     * @param id
     * @return
     */
    public ReturnType getRiskController(String id, String subGroupId) {
        Map<String, String> query = new HashMap<>();
        /*query.put("id", id);
        query.put("subGroupId", subGroupId);*/
        query.put("id", "1");
        query.put("subGroupId", "2");
        List<RiskController> riskControllers = fundGroupMapper.getRiskController(query);
        ReturnType rct = new ReturnType();
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, String> _links = new HashMap<>();
        rct.set_total(riskControllers.size());
        if (riskControllers.size() > 0) {
            for (RiskController riskController : riskControllers) {
                Map<String, Object> _items = new HashMap<>();
                _items.put("id", riskController.getId());
                _items.put("name", riskController.getName());
                _items.put("level2RiskControl", riskController.getRisk_controller());
                _items.put("time", riskController.getStart_time()+"~"+riskController.getEnd_time());
                _items.put("benchmark", riskController.getBenchmark());
                list.add(_items);
            }
            rct.setName("风险控制");
            rct.set_items(list);
            rct.set_links(_links);
            rct.set_schemaVersion("0.1.1");
            rct.set_serviceId("资产配置");
        }
        return rct;
    }

    /**
     * 风险控制手段与通知
     *
     * @return
     */
    public ReturnType getmeansAndNoticesRetrun() {
        ReturnType man = new ReturnType();
        Map<String, String> _links = new HashMap<>();
        List<Map<String, Object>> list = new ArrayList<>();
        man.set_total(4);
        man.setName("风险控制通知");
        for (int i = 0; i < 4; i++) {
            Map<String, Object> _items = new HashMap<>();
            if (i == 0) {
                _items.put("id", 1);
                _items.put("name", "全市场的系统风险");
                _items.put("content", null);
            } else if (i == 1) {
                _items.put("id", 2);
                _items.put("name", "各类资产的市场风险");
                _items.put("content", null);
            } else if (i == 2) {
                _items.put("id", 3);
                _items.put("name", "风险控制是第一要位!作任何的投资，防范风险是关键的");
                _items.put("content", null);
            } else if (i == 3) {
                _items.put("id", 4);
                _items.put("name", "具备相关钩子的专业知识");
                _items.put("content", null);
            }
            list.add(_items);
        }
        man.set_items(list);
        man.set_links(_links);
        man.set_schemaVersion("0.1.1");
        man.set_serviceId("资产配置");
        return man;
    }

    /**
     * 模拟历史年化业绩与模拟历史年化波动率
     *
     * @param cust_risk
     * @param investment_horizon
     * @return
     */
    public PerformanceVolatilityReturn getPerformanceVolatility(String cust_risk, String investment_horizon) {
        Map<String, Object> map = new HashMap<>();
        Map<String, String> _links = new HashMap<>();
        List<Map<String, Object>> list = new ArrayList<>();
        PerformanceVolatilityReturn aReturn = new PerformanceVolatilityReturn();
        if (cust_risk == null || cust_risk.equalsIgnoreCase("")) {
            map.put("cust_risk", "C3");
        } else {
            map.put("cust_risk", cust_risk);
        }
        if (investment_horizon == null || investment_horizon.equalsIgnoreCase("")) {
            map.put("investment_horizon", "2");
        } else {
            map.put("investment_horizon", investment_horizon);
        }
        List<RiskIncomeInterval> riskIncomeIntervals = fundGroupMapper.getPerformanceVolatility(map);
        if (riskIncomeIntervals.size() > 0) {
            RiskIncomeInterval riskIncomeInterval = riskIncomeIntervals.get(riskIncomeIntervals.size() / 2-1);
            aReturn.setName("模拟数据");
            aReturn.setProductGroupId(riskIncomeInterval.getFund_group_id());
            aReturn.setProductSubGroupId(riskIncomeInterval.getId());
            for (int i = 0; i < 4; i++) {
                Map<String, Object> maps = new HashMap<>();
                if (i == 0) {
                    maps.put("id", 1);
                    maps.put("name", "模拟历史年化业绩");
                    maps.put("value", riskIncomeInterval.getSimulate_historical_year_performance());
                } else if (i == 1) {
                    maps.put("id", 2);
                    maps.put("name", "模拟历史年化波动率");
                    maps.put("value", riskIncomeInterval.getSimulate_historical_volatility());
                } else if (i == 2) {
                    maps.put("id", 3);
                    maps.put("name", "置信区间");
                    maps.put("value", riskIncomeInterval.getConfidence_interval());
                } else if (i == 3) {
                    maps.put("id", 4);
                    maps.put("name", "最大亏损额");
                    maps.put("value", riskIncomeInterval.getMaximum_losses());
                }
                list.add(maps);
            }
            aReturn.set_items(list);
            aReturn.set_links(_links);
            aReturn.set_schemaVersion("0.1.1");
            aReturn.set_serviceId("资产配置");
        }
        return aReturn;
    }

    /**
     * 返回历史业绩
     * @param fund_group_id
     * @param subGroupId
     * @return
     */
    public PerformanceVolatilityReturn getHistoricalPerformance(String fund_group_id,String subGroupId){
        Map<String, String> _links = new HashMap<>();
        List<Map<String, Object>> list = new ArrayList<>();
        PerformanceVolatilityReturn aReturn = new PerformanceVolatilityReturn();

        Map<String, Object> query = new HashMap<>();
        query.put("fund_group_id", fund_group_id);
        query.put("subGroupId", subGroupId);
        List<FundNetVal> navadjStart = fundGroupMapper.getNavadjStartTime(query);
        query.put("num",navadjStart.size());
        List<FundNetVal> navadjEnd = fundGroupMapper.getNavadjEndTime(query);
        double accumulatedIncome = 0;
        for(FundNetVal fundNetVal : navadjStart){
            for (FundNetVal fundNetVal1 : navadjEnd){
                if (fundNetVal.getCode().equalsIgnoreCase(fundNetVal1.getCode())){
                    accumulatedIncome+=(fundNetVal1.getNavadj()-fundNetVal.getNavadj())/fundNetVal.getNavadj();
                }
            }
        }
        List<RiskIncomeInterval> riskIncomeIntervals = fundGroupMapper.getPerformanceVolatility(query);
        if (riskIncomeIntervals.size() > 0) {
            RiskIncomeInterval riskIncomeInterval = riskIncomeIntervals.get(riskIncomeIntervals.size() / 2);
            aReturn.setName("模拟数据");
            aReturn.setProductGroupId(riskIncomeInterval.getFund_group_id());
            aReturn.setProductSubGroupId(riskIncomeInterval.getId());
            for (int i = 0; i < 5; i++) {
                Map<String, Object> maps = new HashMap<>();
                if (i == 0) {
                    maps.put("id", 1);
                    maps.put("name", "累计收益");
                    maps.put("value", accumulatedIncome);
                } else if (i == 1) {
                    maps.put("id", 2);
                    maps.put("name", "年化收益");
                    maps.put("value", riskIncomeInterval.getIncome_num());
                } else if (i == 2) {
                    maps.put("id", 3);
                    maps.put("name", "最大回撤");
                    maps.put("value", riskIncomeInterval.getRisk_num());
                } else if (i == 3) {
                    maps.put("id", 4);
                    maps.put("name", "年化收益/最大回撤");
                    maps.put("value", Math.abs(riskIncomeInterval.getIncome_num()/riskIncomeInterval.getRisk_num()));
                } else if (i == 4) {
                    maps.put("id", 4);
                    maps.put("name", "夏普比率");
                    maps.put("value", riskIncomeInterval.getSharpe_ratio());
                }
                list.add(maps);
            }
            aReturn.set_items(list);
            aReturn.set_links(_links);
            aReturn.set_schemaVersion("0.1.1");
            aReturn.set_serviceId("资产配置");
        }
        return aReturn;
    }

    /**
     * 滑动条分段数据
     *
     * @param id
     * @param slidebarType (risk_num    风险率,income_num  收益率)
     * @return
     */
    public ReturnType getScaleMark(String id, String slidebarType) {
        ReturnType smk = new ReturnType();
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, String> _links = new HashMap<>();
        List<RiskIncomeInterval> riskIncomeIntervalList = fundGroupMapper.getScaleMark(id,slidebarType);
        if (riskIncomeIntervalList.size() != 0) {
            if (slidebarType.equalsIgnoreCase("risk_num")) {
                smk.setName("风险率");
                for (int i = 0; i < 10; i++) {
                    Map<String, Object> maps = new HashMap<>();
                    maps.put("id", i + 1);
                    //maps.put("value", riskIncomeIntervalList.get(10*i+9).getRisk_num());
                    maps.put("value", riskIncomeIntervalList.get(i).getRisk_num());
                    list.add(maps);
                }
            } else if (slidebarType.equalsIgnoreCase("income_num")){
                smk.setName("收益率");
                for (int i = 0; i < 10; i++) {
                    Map<String, Object> maps = new HashMap<>();
                    maps.put("id", i + 1);
                    //maps.put("value", riskIncomeIntervalList.get(10*i+9).getIncome_num());
                    maps.put("value", riskIncomeIntervalList.get(i).getIncome_num());
                    list.add(maps);
                }
            }
            smk.set_items(list);
            smk.set_total(riskIncomeIntervalList.size());
            smk.set_links(_links);
            smk.set_schemaVersion("0.1.1");
            smk.set_serviceId("资产配置");
        }
        return smk;
    }

    /**
     * 组合收益率(最大回撤)走势图
     *
     * @param id
     * @param subGroupId
     * @param mouth      几个月以来每天
     * @return
     * @throws ParseException
     */
    public ReturnType getFundGroupIncome(String id, String subGroupId, int mouth, String returnType) throws ParseException {
        ReturnType fgi = new ReturnType();
        List<Map<String, Object>> list = new ArrayList<>();
        Calendar ca = Calendar.getInstance();
        Map<String, String> _links = new HashMap<>();
        Date enddate = new Date();
        ca.setTime(enddate);
        ca.add(Calendar.MONTH, mouth);
        String starttime = new SimpleDateFormat("yyyy-MM-dd").format(ca.getTime());
        ca.setTime(enddate);
        ca.add(Calendar.DATE, -1);
        String endtime = new SimpleDateFormat("yyyy-MM-dd").format(ca.getTime());
        Map<String, String> mapStr = new HashMap<>();
        mapStr.put("fund_group_id", id);
        mapStr.put("fund_group_sub_id", subGroupId);
        mapStr.put("starttime", starttime);
        mapStr.put("endtime", endtime);
        List<FundGroupHistory> fundGroupHistoryList = fundGroupMapper.getHistory(mapStr);
        if (fundGroupHistoryList.size() != 0) {
            if (returnType.equalsIgnoreCase("income")) {
                for (FundGroupHistory fundGroupHistory : fundGroupHistoryList) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("time",new SimpleDateFormat("yyyy-MM-dd").format(fundGroupHistory.getTime()));
                    map.put("value", fundGroupHistory.getIncome_num());
                    list.add(map);
                }
                fgi.setName("组合收益率走势图");
            } else {
                for (FundGroupHistory fundGroupHistory : fundGroupHistoryList) {
                    Map<String, Object> map = new HashMap<>();
                    map.put(new SimpleDateFormat("yyyy-MM-dd").format(fundGroupHistory.getTime()), fundGroupHistory.getMaximum_retracement());
                    list.add(map);
                }
                fgi.setName("组合最大回撤走势图");
            }
            fgi.set_total(list.size());
            fgi.set_items(list);
            fgi.set_links(_links);
            fgi.set_schemaVersion("0.1.1");
            fgi.set_serviceId("资产配置");
        }
        return fgi;
    }

    /**
     * 组合收益率(最大回撤)走势图     一周以来以来每天
     *
     * @param id
     * @param subGroupId
     * @return
     * @throws ParseException
     */
    public ReturnType getFundGroupIncomeWeek(String id, String subGroupId, String returnType) throws ParseException {
        ReturnType fgi = new ReturnType();
        List<Map<String, Object>> list = new ArrayList<>();
        Calendar ca = Calendar.getInstance();
        Map<String, String> _links = new HashMap<>();
        Map<String, Object> allMap = new HashMap<>();
        Map<String, Object> mapBasic = new HashMap<>();
        Map<String, Object> mapBenchmark = new HashMap<>();
        //Date endDate = new Date();
        Date endDate = new SimpleDateFormat("yyyy-MM-dd").parse("2017-11-25");
        ca.setTime(endDate);
        ca.add(Calendar.DATE, -7);
        String startTime = new SimpleDateFormat("yyyy-MM-dd").format(ca.getTime());
        ca.setTime(endDate);
        ca.add(Calendar.DATE, -1);
        String endTime = new SimpleDateFormat("yyyy-MM-dd").format(ca.getTime());
        Map<String, String> mapStr = new HashMap<>();
        mapStr.put("fund_group_id", id);
        mapStr.put("fund_group_sub_id", subGroupId);
        mapStr.put("starttime", startTime);
        mapStr.put("endtime", endTime);
        List<FundGroupHistory> fundGroupHistoryList = fundGroupMapper.getHistory(mapStr);
        if (fundGroupHistoryList.size() != 0) {
            if (returnType.equalsIgnoreCase("income")) {
                List<Map<String, Object>> listFund = new ArrayList<>();
                for (FundGroupHistory fundGroupHistory : fundGroupHistoryList) {
                    mapBasic.put("time", new SimpleDateFormat("yyyy-MM-dd").format(fundGroupHistory.getTime()));
                    mapBasic.put("value", fundGroupHistory.getIncome_num());
                    listFund.add(mapBasic);
                }
                allMap.put("income",listFund);
                String riskNum = fundGroupMapper.getRiskNum(fundGroupHistoryList.get(0).getFund_group_id());
                mapStr.put("fund_group_id",riskNum);
                mapStr.remove("fund_group_sub_id");
                fundGroupHistoryList = fundGroupMapper.getHistory(mapStr);
                for (FundGroupHistory fundGroupHistory : fundGroupHistoryList) {
                    mapBenchmark.put(new SimpleDateFormat("yyyy-MM-dd").format(fundGroupHistory.getTime()), fundGroupHistory.getIncome_num());
                }
                allMap.put("incomeBenchmark",mapBenchmark);
                list.add(allMap);

                fgi.setName("组合收益率走势图");
            } else {
                for (FundGroupHistory fundGroupHistory : fundGroupHistoryList) {
                    mapBasic.put(new SimpleDateFormat("yyyy-MM-dd").format(fundGroupHistory.getTime()), fundGroupHistory.getMaximum_retracement());
                }
                allMap.put("retracement",mapBasic);
                String riskNum = fundGroupMapper.getRiskNum(fundGroupHistoryList.get(0).getFund_group_id());
                mapStr.put("fund_group_id",riskNum);
                mapStr.remove("fund_group_sub_id");
                fundGroupHistoryList = fundGroupMapper.getHistory(mapStr);
                for (FundGroupHistory fundGroupHistory : fundGroupHistoryList) {
                    mapBenchmark.put(new SimpleDateFormat("yyyy-MM-dd").format(fundGroupHistory.getTime()), fundGroupHistory.getMaximum_retracement());
                }
                allMap.put("retracementBenchmark",mapBenchmark);
                list.add(allMap);
                fgi.setName("组合最大回撤走势图");
            }
            fgi.set_total(list.size());
            fgi.set_items(list);
            fgi.set_links(_links);
            fgi.set_schemaVersion("0.1.1");
            fgi.set_serviceId("资产配置");
        }
        return fgi;
    }

    /**
     * 净值增长率(净值增长)走势图     一周以来以来每天
     *
     * @param id
     * @param subGroupId
     * @return
     * @throws ParseException
     */
    public ReturnType getFundNetValue(String id, String subGroupId, String returnType) throws ParseException {
        ReturnType fgi = new ReturnType();
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, String> _links = new HashMap<>();
        Calendar ca = Calendar.getInstance();
        //Date endDate = new Date();
        Date endDate = new SimpleDateFormat("yyyy-MM-dd").parse("2017-11-25");
        ca.setTime(endDate);
        ca.add(Calendar.DATE, -8);
        String startTime = new SimpleDateFormat("yyyy-MM-dd").format(ca.getTime());
        ca.setTime(endDate);
        ca.add(Calendar.DATE, -1);
        String endTime = new SimpleDateFormat("yyyy-MM-dd").format(ca.getTime());
        Map<String, String> query = new HashMap<>();
        query.put("id", id);
        query.put("subId", subGroupId);
        List<Interval> interval = fundGroupMapper.getProportion(query);
        List<Interval> intervalCode = fundGroupMapper.getFundCode(query);
        if (intervalCode.size() != 0) {
            for (Interval interval1 : intervalCode) {
                Map<String, Object> map = new HashMap<>();
                Map<String, String> query1 = new HashMap<>();
                query1.put("fund_code", interval1.getFund_id());
                query1.put("startTime", startTime);
                query1.put("endtTime", endTime);
                List<FundNetVal> fundNetValues = fundGroupMapper.getFundNetValue(query1);
                List<Map<String, Object>> listFund = new ArrayList<>();
                Map<String, Object> fundMap = new HashMap<>();
                for (int i = 1; i < fundNetValues.size(); i++) {
                    if (returnType.equalsIgnoreCase("1")) {
                        fgi.setName("净值增长");
                        fundMap.put("time", new SimpleDateFormat("yyyy-MM-dd").format(fundNetValues.get(i).getNavLatestDate()));
                        fundMap.put("value", fundNetValues.get(i).getNavadj());
                    } else {
                        fgi.setName("净值增长率");
                        double navadjReturn = (fundNetValues.get(i).getNavadj() - fundNetValues.get(i - 1).getNavadj()) / fundNetValues.get(i - 1).getNavadj();
                        fundMap.put("time", new SimpleDateFormat("yyyy-MM-dd").format(fundNetValues.get(i).getNavLatestDate()));
                        fundMap.put("value", navadjReturn);
                    }
                    listFund.add(fundMap);
                }
                map.put("navadj", listFund);
                map.put("基金类型", interval1.getFund_type_two());
                for(Interval interval2:interval){
                    if(interval1.getFund_type_two().equalsIgnoreCase(interval2.getFund_type_two())){
                        map.put(interval1.getFund_type_two(),interval2.getProportion());
                        break;
                    }
                }
                map.put("fund_code",interval1.getFund_id());
                map.put("name",interval1.getFname());
                list.add(map);
            }
            fgi.set_total(list.size());
            fgi.set_items(list);
            fgi.set_links(_links);
            fgi.set_schemaVersion("0.1.1");
            fgi.set_serviceId("资产配置");
        }
        return fgi;
    }

    /**
     * 未来收益走势图
     *
     * @param id
     * @param subGroupId
     * @return
     */
    public ReturnType getExpectedIncome(String id, String subGroupId) {
        ReturnType rt = new ReturnType();
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, String> _links = new HashMap<>();
        Map<String, String> query = new HashMap<>();
        /*query.put("fund_group_id", id);
        query.put("subGroupId", subGroupId);*/
        query.put("fund_group_id", "1");
        query.put("subGroupId", "1");
        List<FundGroupExpectedIncome> fgeiList = fundGroupMapper.getExpectedIncome(query);
        if (fgeiList.size() != 0) {
            for (FundGroupExpectedIncome fgei : fgeiList) {
                Map<String, Object> map = new HashMap<>();
                Map<String, Object> map1 = new HashMap<>();
                map1.put("预期收益", fgei.getExpected_income());
                map1.put("高概率最高收益", fgei.getHigh_percent_max_income());
                map1.put("高概率最低收益", fgei.getHigh_percent_min_income());
                map1.put("低概率最高收益", fgei.getLow_percent_max_income());
                map1.put("低概率最低收益", fgei.getLow_percent_min_income());
                map.put("income_mounth_time", fgei.getIncome_mounth_time());
                map.put("_item", map1);
                list.add(map);
            }
            rt.set_total(list.size());
            rt.set_items(list);
            rt.set_links(_links);
            rt.set_schemaVersion("0.1.1");
            rt.set_serviceId("资产配置");
        }
        return rt;
    }


    /**
     * 计算组合单位收益净值和最大回撤
     *
     * @param group_id
     * @param subGroupId
     */
    public void getNavadj(String group_id, String subGroupId) {
        Map<String, Object> query = new HashMap<>();
        query.put("fund_group_id", group_id);
        query.put("subGroupId", subGroupId);
        List<FundNetVal> list = fundGroupMapper.getNavadj(query);
        for (FundNetVal fundNetVal : list){
            query.put("num",fundNetVal.getNavadj());
            query.put("time",fundNetVal.getNavLatestDate());
            //fundGroupMapper.insertGroupNavadj(query);
        }
        String groupStartTime = fundGroupMapper.getGroupStartTime(query);
        Calendar ca = Calendar.getInstance();
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse("2016-11-17");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //Date date = new Date();
        try {
            for (; date.getTime() > new SimpleDateFormat("yyyy-MM-dd").parse(groupStartTime).getTime(); ) {
                query.put("endTime", new SimpleDateFormat("yyyy-MM-dd").format(date));
                ca.setTime(date);
                ca.add(Calendar.MONTH, -3);
                query.put("startTime", new SimpleDateFormat("yyyy-MM-dd").format(ca.getTime()));
                list = fundGroupMapper.getNavadj(query);
                double[] temp = new double[list.size()];
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getNavadj() == null){
                        for (int k = 0;;k++){
                            if (list.get(k).getNavadj() != null){
                                temp[i] = list.get(k).getNavadj();
                                break;
                            }
                        }
                    }else {
                        temp[i] = list.get(i).getNavadj();
                    }
                }
                double maximum_retracement = 0;
                if (temp.length>1) {
                    CalculateMaxdrawdowns cm = new CalculateMaxdrawdowns();
                    maximum_retracement = cm.calculateMaxdrawdown(temp);
                }
                query.put("retracement", maximum_retracement);
                query.put("time", new SimpleDateFormat("yyyy-MM-dd").format(date));
                fundGroupMapper.updateMaximumRetracement(query);
                ca.setTime(date);
                ca.add(Calendar.DATE,-1);
                date = ca.getTime();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * 计算组合基准单位收益净值和最大回撤
     *
     * @param risk_level
     */
    public void getNavadjBenchmark(String risk_level) {
        Map<String, Object> query = new HashMap<>();
        query.put("risk_level", risk_level);
        List<FundNetVal> list = fundGroupMapper.getNavadjBenchmark(query);
        int a = 0;
        for (FundNetVal fundNetVal : list){a++;
            query.put("num",fundNetVal.getNavadj());
            query.put("time",fundNetVal.getNavLatestDate());
            fundGroupMapper.insertGroupNavadjBenchmark(query);
        }
        Calendar ca = Calendar.getInstance();
        /*Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse("2017-05-19");
        } catch (ParseException e) {
            e.printStackTrace();
        }*/
        Date date = new Date();
        ca.setTime(date);
        ca.add(Calendar.YEAR, -3);
        String groupStartTime = new SimpleDateFormat("yyyy-MM-dd").format(ca.getTime());
        try {
            for (; date.getTime() > new SimpleDateFormat("yyyy-MM-dd").parse(groupStartTime).getTime(); ) {
                query.put("endTime", new SimpleDateFormat("yyyy-MM-dd").format(date));
                ca.setTime(date);
                ca.add(Calendar.MONTH, -3);
                query.put("startTime", new SimpleDateFormat("yyyy-MM-dd").format(ca.getTime()));
                list = fundGroupMapper.getNavadjBenchmark(query);
                double[] temp = new double[list.size()];
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getNavadj() == null){
                        for (int k = 0;;k++){
                            if (list.get(k).getNavadj() != null){
                                temp[i] = list.get(k).getNavadj();
                                break;
                            }
                        }
                    }else {
                        temp[i] = list.get(i).getNavadj();
                    }
                }
                double maximum_retracement = 0;
                if (temp.length>1) {
                    CalculateMaxdrawdowns cm = new CalculateMaxdrawdowns();
                    maximum_retracement = cm.calculateMaxdrawdown(temp);
                }
                query.put("retracement", maximum_retracement);
                query.put("time", new SimpleDateFormat("yyyy-MM-dd").format(date));
                ca.setTime(date);
                ca.add(Calendar.DATE,-1);
                date = ca.getTime();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * 计算夏普比率
     *
     * @param group_id
     * @param subGroupId
     * @return
     */
    public int sharpeRatio(String group_id, String subGroupId) {
        //测试数据
        //Double[] asset ={1.2000,   1.3000  ,  0.9000 ,   1.5000};
        int flag = -1;
        Double cash = 0.0135;
        Map<String, String> query = new HashMap<>();
        query.put("fund_group_id", group_id);
        query.put("subGroupId", subGroupId);
        List<FundNetVal> fundNetValList = fundGroupMapper.getSharpeRatio(query);
        if (fundNetValList.size() != 0) {
            Double[] asset = new Double[fundNetValList.size()];
            for (int i = 0; i < fundNetValList.size(); i++) {
                asset[i] = fundNetValList.get(i).getNavadj();
            }
            double sharpeRatio = Double.parseDouble(MVO.sharpeRatio(asset, cash).toString());
            Map<String, Object> update = new HashMap<>();
            update.put("id", subGroupId);
            update.put("sharpeRatio", sharpeRatio);
            flag = fundGroupMapper.updateSharpeRatio(update);
        }
        return flag;
    }

    /**
     * 把传出数据转为json格式
     *
     * @param interval
     * @return
     */
    public FundReturn getFundReturn(List<Interval> interval) {
        FundReturn fr = new FundReturn();
        if (interval.size() != 0) {
            Map<String, Object> assetsRatios = new HashMap<>();
            Map<String, String> _links = new HashMap<>();
            List<Map<String, Object>> list = new ArrayList<>();
            Map<String, String> query = new HashMap<>();
            query.put("id", interval.get(0).getFund_group_id());
            query.put("subId", interval.get(0).getId());
            List<Interval> intervals = fundGroupMapper.getProportion(query);
            //基金组合内的各基金权重
            for (Interval inter : intervals) {
                assetsRatios.put("type",inter.getFund_type_two());
                assetsRatios.put("value", inter.getProportion());
            }
            list.add(assetsRatios);
            fr.setGroupId(interval.get(0).getFund_group_id());
            fr.setSubGroupId(interval.get(0).getId());
            fr.setName(interval.get(0).getFund_group_name());
            /*fr.setMinAnnualizedReturn(interval.get(0).getIncome_min_num());
            fr.setMaxAnnualizedReturn(interval.get(0).getIncome_max_num());
            fr.setMinRiskLevel(interval.get(0).getRisk_min_num());
            fr.setMaxRiskLevel(interval.get(0).getRisk_max_num());*/
            fr.set_links(_links);
            //fr.setCreationTime(interval.get(0).getDetails_last_mod_time().getTime());
            fr.set_schemaVersion("0.1.1");
            fr.set_serviceId("资产配置");
            fr.setAssetsRatios(list);
        }
        return fr;
    }

    public void getAllIdAndSubId(){
        List<Interval> aa = fundGroupMapper.getAllIdAndSubId();
        for (Interval a : aa){
            getNavadj(a.getFund_group_id(),a.getId());
            sharpeRatio(a.getFund_group_id(),a.getId());
        }
    }

    public int deleteData(String tableName){
        return fundGroupMapper.deleteData(tableName);
    }

    /**
     * 计算所有组合中基金收益贡献比
     */
    public void contribution(){
        List<Interval> aa = fundGroupMapper.getAllIdAndSubId();
        for (Interval a : aa){
            Map<String, Object> query = new HashMap<>();
            query.put("fund_group_id", a.getFund_group_id());
            query.put("subGroupId", a.getId());
            List<FundNetVal> navadjStart = fundGroupMapper.getNavadjStartTime(query);
            query.put("num",navadjStart.size());
            List<FundNetVal> navadjEnd = fundGroupMapper.getNavadjEndTime(query);
            double accumulatedIncome = 0;
            for(FundNetVal fundNetVal : navadjStart){
                for (FundNetVal fundNetVal1 : navadjEnd){
                    if (fundNetVal.getCode().equalsIgnoreCase(fundNetVal1.getCode())){
                        accumulatedIncome+=(fundNetVal1.getNavadj()-fundNetVal.getNavadj())/fundNetVal.getNavadj();
                    }
                }
            }
            for(FundNetVal fundNetVal : navadjStart){
                for (FundNetVal fundNetVal1 : navadjEnd){
                    if (fundNetVal.getCode().equalsIgnoreCase(fundNetVal1.getCode())){
                        double contribution =(fundNetVal1.getNavadj()-fundNetVal.getNavadj())/fundNetVal.getNavadj()/accumulatedIncome;
                        query.put("code",fundNetVal.getCode());
                        query.put("contribution",contribution);
                        fundGroupMapper.updateContribution(query);
                    }
                }
            }
        }
    }
}