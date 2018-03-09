package com.shellshellfish.aaas.assetallocation.neo.service;

import com.alibaba.fastjson.JSON;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.shellshellfish.aaas.assetallocation.neo.enmu.SlidebarTypeEnmu;
import com.shellshellfish.aaas.assetallocation.neo.enmu.StandardTypeEnmu;
import com.shellshellfish.aaas.assetallocation.neo.entity.*;
import com.shellshellfish.aaas.assetallocation.neo.mapper.FundGroupMapper;
import com.shellshellfish.aaas.assetallocation.neo.mapper.FundNetValMapper;
import com.shellshellfish.aaas.assetallocation.neo.returnType.*;
import com.shellshellfish.aaas.assetallocation.neo.util.*;
import com.shellshellfish.aaas.common.utils.TradeUtil;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;

import static com.shellshellfish.aaas.assetallocation.neo.util.ConstantUtil.*;
//import static com.shellshellfish.aaas.assetallocation.neo.util.ConstantUtil.MONGO_DB_COLLECTION;

/**
 * Created by wangyinuo on 2017/11/27.
 */
@Service
public class FundGroupService {

    @Autowired
    private FundGroupMapper fundGroupMapper;
    @Autowired
    private FundNetValMapper fundNetValMapper;
    @Autowired
    private FundGroupService fundGroupService;

    @Autowired
    MongoDatabase mongoDatabase;

    @Value("${spring.data.mongodb.collection}")
    String collectionName;

    Logger logger = LoggerFactory.getLogger(FundGroupService.class);

    /**
     * 查询所有基金组合
     *
     * @return
     */
    public FundAllReturn selectAllFundGroup() {
        FundAllReturn far = new FundAllReturn();
        List<Map<String, Object>> list = new ArrayList<>();
        List<Interval> fundGroupNum = fundGroupMapper.selectAllFundGroupNum();
        if (CollectionUtils.isEmpty(fundGroupNum)) {
            Map<String, Object> _items = new HashMap<>();
            List<Map<String, Object>> listMap = new ArrayList<>();
            _items.put("assetsRatios", listMap); //组合内各基金权重
            _items.put("groupId", "");
            _items.put("subGroupId", "");
            _items.put("name", "");
            list.add(_items);
            far.setName("基金组合");
            far.set_total(0);
            far.set_items(list);
            far.set_schemaVersion("0.1.1");
            far.set_serviceId("资产配置");
            return far;
        }

        for (Interval interval : fundGroupNum) {
            Map<String, Object> _items = new HashMap<>();
            Map<String, String> query = new HashMap<>();
            query.put("fund_group_id", interval.getFund_group_id());
            List<RiskIncomeInterval> riskIncomeIntervalList = fundGroupMapper.getPerformanceVolatility(query);
            if (CollectionUtils.isEmpty(riskIncomeIntervalList)) {
                continue;
            }
            query.remove("fund_group_id");

            int index = (riskIncomeIntervalList.size() - 1) / 2;
            index = index > 0 ? index - 1 : 0;
            RiskIncomeInterval riskIncomeInterval = riskIncomeIntervalList.get(index);
            query.put("groupId", riskIncomeInterval.getFund_group_id());
            query.put("subGroupId", riskIncomeInterval.getId());
            //基金组合内的各基金权重
            List<Interval> intervals = fundGroupMapper.getProportionGroupByFundTypeTwo(query);
            List<Map<String, Object>> listMap = this.intervalListToListMap(intervals);

            _items.put("assetsRatios", listMap); //组合内各基金权重
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

        return far;
    }

    private List<Map<String, Object>> intervalListToListMap(List<Interval> intervals) {
        List<Map<String, Object>> listMap = new ArrayList<>();
        if (CollectionUtils.isEmpty(intervals)) {
            return listMap;
        }

        //基金组合内的各基金权重
        for (Interval interval : intervals) {
            if (interval.getProportion() != 0d) {
                Map<String, Object> assetsRatio = new HashMap<>();
                assetsRatio.put("type", interval.getFund_type_two());
                assetsRatio.put("value", interval.getProportion());
                listMap.add(assetsRatio);
            }
        }
        return listMap;
    }

    /**
     * 产品类别比重
     * @param fund_group_id
     * @param fund_group_sub_id
     * @return
     */
    public ReturnType getProportionOne(String fund_group_id, String fund_group_sub_id) {
        ReturnType fr = new ReturnType();
        List<Map<String, Object>> listMap = new ArrayList<>();
        Map<String, String> _links = new HashMap<>();
        Map<String, String> query = new HashMap<>();
        query.put("id", fund_group_id);
        query.put("subId", fund_group_sub_id);
        List<Interval> intervals = fundGroupMapper.getProportionOne(query);
        long accum = 0L;
        long value = 0L;
        for (Interval interval : intervals) {
            if (interval.getProportion() != 0) {
                value = Math.round(interval.getProportion() * 100);
                Map<String, Object> map = new HashMap<>();
                map.put("type", interval.getFund_type_one());
                map.put("value", value);
                listMap.add(map);
                accum += value;
            }
        }
        int size = CollectionUtils.isEmpty(listMap) ? 0 : listMap.size();
        if (accum < 100L && accum != 0L && size > 0) {
            int newValue = Integer.parseInt(listMap.get(size - 1).get("value").toString()) + 1;
            listMap.get(size - 1).put("value", newValue);
        }
        fr.set_total(listMap.size());
        fr.setName("产品类别比重");
        fr.set_items(listMap);
        fr.set_links(_links);
        fr.set_schemaVersion("0.1.1");
        fr.set_serviceId("资产配置");
        return fr;
    }

    /**
     * @param groupId
     * @param subGroupId
     * @return
     */
    public ReturnType getFnameAndProportion(String groupId, String subGroupId) {
        Map<String, String> query = new HashMap<>();
        query.put("id", groupId);
        query.put("subId", subGroupId);
        List<Interval> intervals = fundGroupMapper.getFnameAndProportion(query);

        ReturnType fr = new ReturnType();
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, String> _links = new HashMap<>();
        if (CollectionUtils.isEmpty(intervals)) {
            fr.set_total(0);
            fr.setName("组合内基金名称及其百分比");
            fr.set_items(list);
            fr.set_links(_links);
            fr.set_schemaVersion("0.1.1");
            fr.set_serviceId("资产配置");
            return fr;
        }

        List<Interval> intervalProportions = new ArrayList<>();
        for (Interval tmpInterval : intervals) {
            if (tmpInterval.getProportion() > 0d) {
                intervalProportions.add(tmpInterval);
            }
        }

        for(Interval interval : intervalProportions){
            if (interval.getProportion() != 0d) {
                Map<String, Object> map = new HashMap<>();
                map.put("fund_type_one", interval.getFund_type_one());
                map.put("fund_type_two", interval.getFund_type_two());
                map.put("fund_code", interval.getFund_id());
                map.put("name", interval.getFname());
                map.put("value", interval.getProportion());
                list.add(map);
            }
        }
        fr.set_total(list.size());
        fr.setName("组合内基金名称及其百分比");
        fr.set_items(list);
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
        for (int i = 1; i <= RISK_LEVEL_COUNT; i++) {
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
     * 预期收益率调整 风险率调整 最优组合(有效前沿线)
     *
     * @param groupId
     * @param riskValue
     * @param returnValue
     * @return
     */
    public FundReturn getInterval(String groupId, String riskValue, String returnValue) {
        FundReturn fr = null;

        Map<String, Object> map = new HashMap<>();
        map.put("riskValue", riskValue);
        map.put("returnValue", returnValue);
        map.put("groupId", groupId);
        List<Interval> intervals = fundGroupMapper.getInterval(map);
        if (CollectionUtils.isEmpty(intervals)) {
            return fr;
        }

        String subGroupId = intervals.get(0).getFund_group_sub_id();
        fr = getProportionGroupByFundTypeTwo(groupId, subGroupId);
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
    public ReturnType getRevenueContribution(String groupId, String subGroupId) {
        ReturnType rcb = new ReturnType();
        Map<String, String> _links = new HashMap<>();
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        map.put("subGroupId", subGroupId);
        map.put("id", groupId);
        List<Interval> intervals = fundGroupMapper.getRevenueContribution(map);
        if (CollectionUtils.isEmpty(intervals)) {
            rcb.setName("配置收益贡献");
            rcb.set_total(0);
            rcb.set_items(list);
            rcb.set_links(_links);
            rcb.set_schemaVersion("0.1.1");
            rcb.set_serviceId("资产配置");
            return rcb;
        }

        for (int i = 0; i < intervals.size(); i++) {
            if(intervals.get(i).getRevenue_contribution() != 0){
                Map<String, Object> _items = new HashMap<>();
                _items.put("id", i + 1);
                _items.put("name", intervals.get(i).getFund_type_two());
                _items.put("value", intervals.get(i).getRevenue_contribution());
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
                String startTime = sdf.format(intervals.get(i).getDetails_last_mod_time());
                Calendar ca = Calendar.getInstance();
                ca.add(Calendar.DATE, -1);
                String endTime = sdf.format(ca.getTime());
                _items.put("time", startTime + "~" + endTime);
                list.add(_items);
            }
        }
        rcb.setName("配置收益贡献");
        rcb.set_total(intervals.size());
        rcb.set_items(list);
        rcb.set_links(_links);
        rcb.set_schemaVersion("0.1.1");
        rcb.set_serviceId("资产配置");

        return rcb;
    }

    /**
     * 有效前沿线
     *
     * @return
     */
    public ReturnType efficientFrontier(String fundGroupId) {
        ReturnType aReturn = new ReturnType();
        Map<String, String> _links = new HashMap<>();
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        map.put("slidebarType", "risk_num");
        map.put("fundGroupId", fundGroupId);
        List<RiskIncomeInterval> riskIncomeIntervalList = fundGroupMapper.getScaleMark(map);
        if (CollectionUtils.isEmpty(riskIncomeIntervalList)) {
            aReturn.setName("有效前沿线数据");
            aReturn.set_items(list);
            aReturn.set_total(100);
            aReturn.set_links(_links);
            aReturn.set_schemaVersion("0.1.1");
            aReturn.set_serviceId("资产配置");
            return aReturn;
        }

        for (int i = 0; i < 100;i++){
            Map<String, Object> _items = new HashMap<>();
            _items.put("id", i+1);
            _items.put("x", riskIncomeIntervalList.get(i).getRisk_num());
            _items.put("y", riskIncomeIntervalList.get(i).getIncome_num());
            list.add(_items);
        }
        aReturn.setName("有效前沿线数据");
        aReturn.set_items(list);
        aReturn.set_total(100);
        aReturn.set_links(_links);
        aReturn.set_schemaVersion("0.1.1");
        aReturn.set_serviceId("资产配置");

        return aReturn;
    }

    /**
     * 风险控制
     *
     * @param groupId
     * @return
     */
    public ReturnType getRiskController(String groupId, String subGroupId) {
        Map<String, String> query = new HashMap<>();
        query.put("id", groupId);
        query.put("subGroupId", subGroupId);
        List<RiskController> riskControllers = fundGroupMapper.getRiskController(query);

        ReturnType rct = new ReturnType();
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, String> _links = new HashMap<>();
        if (CollectionUtils.isEmpty(riskControllers)) {
            rct.set_total(0);
            rct.setName("风险控制");
            rct.set_items(list);
            rct.set_links(_links);
            rct.set_schemaVersion("0.1.1");
            rct.set_serviceId("资产配置");
            return rct;
        }

        rct.set_total(riskControllers.size());
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

        return rct;
    }

    /**
     * 根据 组合ID 获取 风险等级
     * @param groupId
     * @return
     */
    public Return getCustRiskByGroupId(String groupId) {
        String riskLevel = fundGroupMapper.getRiskNum(groupId);

        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, String> _links = new HashMap<>();
        Map<String, Object> _items = new HashMap<>();
        _items.put("riskLevel", riskLevel);
        list.add(_items);

        Return rct = new Return();
        rct.set_total(1);
        rct.setProductGroupId(groupId);
        rct.setName("组合风险等级");
        rct.set_items(list);
        rct.set_links(_links);
        rct.set_schemaVersion("0.1.1");
        rct.set_serviceId("资产配置");

        return rct;
    }

    /**
     * 风险控制手段与通知
     *
     * @return
     */
    public ReturnType getMeansAndNoticesReturn() {
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
                _items.put("name", "具备相关投资的专业知识");
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
        if (StringUtils.isEmpty(cust_risk)) {
            map.put("cust_risk", "C3");
        } else {
            map.put("cust_risk", cust_risk);
        }
        if (StringUtils.isEmpty(investment_horizon)) {
            map.put("investment_horizon", "2");
        } else {
            map.put("investment_horizon", investment_horizon);
        }
        List<RiskIncomeInterval> riskIncomeIntervals = fundGroupMapper.getPerformanceVolatility(map);
        if (CollectionUtils.isEmpty(riskIncomeIntervals)) {
            aReturn.setName("模拟数据");
            aReturn.setProductGroupId("");
            aReturn.setProductSubGroupId("");
            aReturn.set_items(list);
            aReturn.set_links(_links);
            aReturn.set_schemaVersion("0.1.1");
            aReturn.set_serviceId("资产配置");
            return aReturn;
        }

        int index = (riskIncomeIntervals.size() - 1) / 2;
        index = index > 0 ? index - 1 : 0;
        RiskIncomeInterval riskIncomeInterval = riskIncomeIntervals.get(index);
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

        return aReturn;
    }

    /**
     * 返回历史业绩
     * @param groupId
     * @param subGroupId
     * @return
     */
    public PerformanceVolatilityReturn getHistoricalPerformance(String groupId, String subGroupId) {
        Map<String, String> _links = new HashMap<>();
        List<Map<String, Object>> list = new ArrayList<>();
        PerformanceVolatilityReturn aReturn = new PerformanceVolatilityReturn();

        Map<String, Object> query = new HashMap<>();
        query.put("fund_group_id", groupId);
        query.put("subGroupId", subGroupId);

        List<String> codeList = getFundGroupCodes(groupId, subGroupId);
        //查询组合中基金最晚成立日 作为 该组合成立日
        Date minNavDate = fundNetValMapper.getMinNavDateByCodeList(codeList);
        query.put("minNavDate", minNavDate);
        List<FundNetVal> navadjStartList = fundGroupMapper.getNavadjFromStartDate(query);

        // 根据 codeList 查询基金最近的净值更新日期
        Date maxNavDate = fundNetValMapper.getMaxNavDateByCodeList(codeList);
        query.put("navDate", maxNavDate);
        List<FundNetVal> navadjEndList = fundGroupMapper.getNavadjByNavDate(query);

        if (CollectionUtils.isEmpty(navadjStartList) || CollectionUtils.isEmpty(navadjEndList)) {
            aReturn.setName("历史业绩");
            aReturn.setProductGroupId("");
            aReturn.setProductSubGroupId("");
            aReturn.set_items(list);
            aReturn.set_links(_links);
            aReturn.set_schemaVersion("0.1.1");
            aReturn.set_serviceId("资产配置");
            return aReturn;
        }

        double accumulatedIncome = 0;
        for (FundNetVal navadjStart : navadjStartList) {
            for (FundNetVal navadjEnd : navadjEndList) {
                if (navadjStart.getCode().equalsIgnoreCase(navadjEnd.getCode()) && navadjStart.getNavadj() != 0) {
                    accumulatedIncome += (navadjEnd.getNavadj() - navadjStart.getNavadj()) / navadjStart.getNavadj();
                } else {
                    accumulatedIncome += navadjEnd.getNavadj();
                }
            }
        }

        List<RiskIncomeInterval> riskIncomeIntervals = fundGroupMapper.getPerformanceVolatility(query);
        if (CollectionUtils.isEmpty(riskIncomeIntervals)) {
            aReturn.setName("历史业绩");
            aReturn.setProductGroupId("");
            aReturn.setProductSubGroupId("");
            aReturn.set_items(list);
            aReturn.set_links(_links);
            aReturn.set_schemaVersion("0.1.1");
            aReturn.set_serviceId("资产配置");
            return aReturn;
        }

        int index = (riskIncomeIntervals.size() - 1) / 2;
        index = index > 0 ? index - 1 : 0;
        RiskIncomeInterval riskIncomeInterval = riskIncomeIntervals.get(index);
        aReturn.setName("历史业绩");
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
                maps.put("value", Math.abs(riskIncomeInterval.getIncome_num() / riskIncomeInterval.getRisk_num()));
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

        return aReturn;
    }

    public List<Date> getNavlatestdateCount(String groupId, String subGroupId) {
        List<String> codeList = fundGroupService.getFundGroupCodes(groupId, subGroupId);
        int codeSize = codeList.size();
        Map query = new HashMap();
        query.put("list", codeList);
        //查询组合中基金最晚成立日 作为 该组合成立日
        Date minNavDate = fundNetValMapper.getMinNavDateByCodeList(codeList);
        query.put("minNavDate", minNavDate);
        List<Map> resultMap = fundGroupMapper.getNavlatestdateCount(query);

        List<Date> navDateList = new ArrayList<>();
        for (Map map : resultMap) {
            int count = ((Long)map.get("count")).intValue();
            Date navDate = (Date)map.get("navDate");
            if (count != codeSize) {
                continue;
            }

            navDateList.add(navDate);
        }

        return navDateList;
    }

    public List<String> getFundGroupCodes(String groupId, String subGroupId) {
        Map<String, Object> queryCodes = new HashMap<>();
        queryCodes.put("fundGroupId", groupId);
        queryCodes.put("subGroupId", subGroupId);
        List<String> codeList = fundGroupMapper.getFundGroupCodeList(queryCodes);
        return codeList;
    }

    /**
     * 滑动条分段数据
     *
     * @param fundGroupId
     * @param slidebarType (risk_num    风险率,income_num  收益率)
     * @return
     */
    public ReturnType getScaleMark(String fundGroupId, String slidebarType) {
        ReturnType smk = new ReturnType();
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, String> _links = new HashMap<>();

        Map<String, Object> map = new HashMap<>();
        map.put("slidebarType", slidebarType);
        map.put("fundGroupId", fundGroupId);
        List<RiskIncomeInterval> riskIncomeIntervalList = fundGroupMapper.getScaleMark(map);
        if (CollectionUtils.isEmpty(riskIncomeIntervalList)
                || StringUtils.isEmpty(slidebarType)) {
            smk.setName(SlidebarTypeEnmu.getNameByType(slidebarType));
            smk.set_items(list);
            smk.set_total(0);
            smk.set_links(_links);
            smk.set_schemaVersion("0.1.1");
            smk.set_serviceId("资产配置");
            return smk;
        }

        smk.setName(SlidebarTypeEnmu.getNameByType(slidebarType));
        if (slidebarType.equalsIgnoreCase(SlidebarTypeEnmu.RISK_NUM.getName())) {
            for (int i = 0; i < 10; i++) {
                Map<String, Object> maps = new HashMap<>();
                maps.put("id", i + 1);
                maps.put("value", riskIncomeIntervalList.get(10 * i + 9).getRisk_num());
                list.add(maps);
            }
        } else if (slidebarType.equalsIgnoreCase(SlidebarTypeEnmu.INCOME_NUM.getName())) {
            for (int i = 0; i < 10; i++) {
                Map<String, Object> maps = new HashMap<>();
                maps.put("id", i + 1);
                maps.put("value", riskIncomeIntervalList.get(10 * i + 9).getIncome_num());
                list.add(maps);
            }
        }
        smk.set_items(list);
        smk.set_total(list.size());
        smk.set_links(_links);
        smk.set_schemaVersion("0.1.1");
        smk.set_serviceId("资产配置");

        return smk;
    }

    /**
     * 滑动条分段数据  从fund_group_sub_choose 表中获取数据
     *
     * @param groupId
     * @param slidebarType (risk_num    风险率, income_num  收益率)
     * @return
     */
    public ReturnType getScaleMarkFromChoose(String groupId, String slidebarType) {
        ReturnType smk = new ReturnType();
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, String> _links = new HashMap<>();

        smk.setName(SlidebarTypeEnmu.getNameByType(slidebarType));
        smk.set_items(list);
        smk.set_total(0);
        smk.set_links(_links);
        smk.set_schemaVersion("0.1.1");
        smk.set_serviceId("资产配置");

        if (StringUtils.isEmpty(slidebarType)) {
            return smk;
        }

        String standtardType = StandardTypeEnmu.getStandardTypeBySlidebarType(slidebarType);
        List<RiskIncomeInterval> riskIncomeIntervalList =
                fundGroupMapper.getScaleMarkFromChoose(groupId, slidebarType, standtardType);
        if (CollectionUtils.isEmpty(riskIncomeIntervalList)) {
            return smk;
        }

        smk.setName(SlidebarTypeEnmu.getNameByType(slidebarType));
        if (slidebarType.equalsIgnoreCase(SlidebarTypeEnmu.INCOME_NUM.getName())) {
            int i = 0;
            for (RiskIncomeInterval rii : riskIncomeIntervalList) {
                Map<String, Object> map = new HashMap<>();
                map.put("id", ++i);
                map.put("value", rii.getIncome_num());
                list.add(map);
            }
        } else if (slidebarType.equalsIgnoreCase(SlidebarTypeEnmu.RISK_NUM.getName())) {
            int i = 0;
            for (RiskIncomeInterval rii : riskIncomeIntervalList) {
                Map<String, Object> map = new HashMap<>();
                map.put("id", ++i);
                map.put("value", rii.getRisk_num());
                list.add(map);
            }
        }
        smk.set_items(list);
        smk.set_total(list.size());
        smk.set_links(_links);
        smk.set_schemaVersion("0.1.1");
        smk.set_serviceId("资产配置");

        return smk;
    }

    /**
     * 组合收益率(最大回撤)走势图   自组合基金成立以来的每天
     *
     * @param groupId
     * @param subGroupId
     * @return
     * @throws ParseException
     */
    public ReturnType getFundGroupIncomeAll(String groupId, String subGroupId, String returnType) {
        ReturnType fgi = new ReturnType();
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, String> _links = new HashMap<>();
        Map maxMinValueMap = new HashMap();
        Map maxMinBenchmarkMap = new HashMap();
        Map<String, Object> allMap = new HashMap<>();
        Map<String, String> mapStr = new HashMap<>();
        mapStr.put("fund_group_id", groupId);
        mapStr.put("fund_group_sub_id", subGroupId);
        List<FundGroupHistory> fundGroupHistoryList = fundGroupMapper.getHistoryAll(mapStr);
        if (CollectionUtils.isEmpty(fundGroupHistoryList)) {
            if (returnType.equalsIgnoreCase("income")) {
                allMap.put("income", new ArrayList<>());
                allMap.put("incomeBenchmark", new ArrayList<>());
                list.add(allMap);
                fgi.setName("组合收益率走势图");
            } else {
                allMap.put("retracement", new ArrayList<>());
                allMap.put("incomeBenchmark", new ArrayList<>());
                list.add(allMap);
                fgi.setName("组合最大回撤走势图");
            }
            fgi.set_total(0);
            fgi.set_items(list);
            fgi.set_links(_links);
            fgi.set_schemaVersion("0.1.1");
            fgi.set_serviceId("资产配置");
            fgi.setMaxMinMap(maxMinValueMap);
            fgi.setMaxMinBenchmarkMap(maxMinBenchmarkMap);
            return fgi;
        }

        List maxMinValueList = new ArrayList();
        List maxMinBenchmarkList = new ArrayList();
        if (returnType.equalsIgnoreCase("income")) {
            List<FundNetVal> fundNetVals = this.getNavadjNew(groupId, subGroupId);
            if (CollectionUtils.isEmpty(fundNetVals)) {
                return fgi;
            }

            List<Map<String, Object>> listFund = new ArrayList<>();
            for (int i = 1; i < fundNetVals.size(); i++) {
                Map<String, Object> mapBasic = new HashMap<>();
                mapBasic.put("time", DateUtil.formatDate(fundNetVals.get(i).getNavLatestDate()));
                mapBasic.put("value", (fundNetVals.get(i).getNavadj() - fundNetVals.get(0).getNavadj()) / fundNetVals.get(0).getNavadj());
                listFund.add(mapBasic);
                maxMinValueList.add((fundNetVals.get(i).getNavadj() - fundNetVals.get(0).getNavadj()) / fundNetVals.get(0).getNavadj());
            }
            maxMinValueMap = TransformUtil.getMaxMinValue(maxMinValueList);
            allMap.put("income", listFund);

            //组合基准数据
            String riskNum = fundGroupMapper.getRiskNum(fundGroupHistoryList.get(0).getFund_group_id());
            mapStr.put("fund_group_id", riskNum);
            mapStr.remove("fund_group_sub_id");
            mapStr.put("time", DateUtil.formatDate(fundGroupHistoryList.get(0).getTime()));
            fundGroupHistoryList = fundGroupMapper.getHistory(mapStr);
            List<Map<String, Object>> listBenchmark = new ArrayList<>();
            for (int i = 1; i< fundGroupHistoryList.size(); i++) {
                Map<String, Object> mapBenchmark = new HashMap<>();
                mapBenchmark.put("time", DateUtil.formatDate(fundGroupHistoryList.get(i).getTime()));
                mapBenchmark.put("value", (fundGroupHistoryList.get(i).getIncome_num() - fundGroupHistoryList.get(0).getIncome_num()) / fundGroupHistoryList.get(0).getIncome_num());
                listBenchmark.add(mapBenchmark);
                maxMinBenchmarkList.add((fundGroupHistoryList.get(i).getIncome_num() - fundGroupHistoryList.get(0).getIncome_num()) / fundGroupHistoryList.get(0).getIncome_num());
            }
            maxMinBenchmarkMap = TransformUtil.getMaxMinValue(maxMinBenchmarkList);
            allMap.put("incomeBenchmark", listBenchmark);
            list.add(allMap);
            fgi.setName("组合收益率走势图");
        } else {
            List<Map<String, Object>> listFund = new ArrayList<>();
            for (FundGroupHistory fundGroupHistory : fundGroupHistoryList) {
                Map<String, Object> mapBasic = new HashMap<>();
                mapBasic.put("time", DateUtil.formatDate(fundGroupHistory.getTime()));
                mapBasic.put("value", fundGroupHistory.getMaximum_retracement());
                listFund.add(mapBasic);
                maxMinValueList.add(fundGroupHistory.getMaximum_retracement());
            }
            maxMinValueMap = TransformUtil.getMaxMinValue(maxMinValueList);
            allMap.put("retracement", listFund);

            //组合基准数据
            String riskNum = fundGroupMapper.getRiskNum(fundGroupHistoryList.get(0).getFund_group_id());
            mapStr.put("fund_group_id",riskNum);
            mapStr.remove("fund_group_sub_id");
            mapStr.put("time", DateUtil.formatDate(fundGroupHistoryList.get(0).getTime()));
            fundGroupHistoryList = fundGroupMapper.getHistory(mapStr);
            List<Map<String, Object>> listBenchmark = new ArrayList<>();
            for (FundGroupHistory fundGroupHistory : fundGroupHistoryList) {
                Map<String, Object> mapBenchmark = new HashMap<>();
                mapBenchmark.put("time", DateUtil.formatDate(fundGroupHistory.getTime()));
                mapBenchmark.put("value", fundGroupHistory.getMaximum_retracement());
                listBenchmark.add(mapBenchmark);
                maxMinBenchmarkList.add(fundGroupHistory.getMaximum_retracement());
            }
            maxMinBenchmarkMap = TransformUtil.getMaxMinValue(maxMinBenchmarkList);
            allMap.put("incomeBenchmark", listBenchmark);
            list.add(allMap);
            fgi.setName("组合最大回撤走势图");
        }
        fgi.set_total(list.size());
        fgi.set_items(list);
        fgi.set_links(_links);
        fgi.set_schemaVersion("0.1.1");
        fgi.set_serviceId("资产配置");
        fgi.setMaxMinMap(maxMinValueMap);
        fgi.setMaxMinBenchmarkMap(maxMinBenchmarkMap);
        return fgi;
    }

    public ReturnType getFundGroupIncomeAllFromMongo(String groupId, String subGroupId, String returnType) {
        ReturnType rt = null;
        try {
            MongoCollection<Document> collection = mongoDatabase.getCollection(collectionName);
            logger.info(collectionName + "集合选择成功");

            String key = groupId + "_" + subGroupId;
            FindIterable<Document> findIterable = collection.find(Filters.eq("key", key));
            MongoCursor<Document> mongoCursor = findIterable.limit(1).iterator();
            while (mongoCursor.hasNext()) {
                Document doc = mongoCursor.next();
                rt = documentToReturnType(doc);
                break;
            }
        } catch (Exception e) {
            logger.error(e.getClass().getName() + ":" + e.getMessage());
        }

        return rt;
    }

    private ReturnType documentToReturnType(Document doc) {
        String _total = doc.getString("_total");
        String _items = doc.getString("_items");
        String name = doc.getString("name");
        String _links = doc.getString("_links");
        String maxMinMap = doc.getString("maxMinMap");
        String maxMinBenchmarkMap = doc.getString("maxMinBenchmarkMap");
        String expectedIncomeSizeMap = doc.getString("expectedIncomeSizeMap");
        String highPercentMaxIncomeSizeMap = doc.getString("highPercentMaxIncomeSizeMap");
        String highPercentMinIncomeSizeMap = doc.getString("highPercentMinIncomeSizeMap");
        String lowPercentMaxIncomeSizeMap = doc.getString("lowPercentMaxIncomeSizeMap");
        String lowPercentMinIncomeSizeMap = doc.getString("lowPercentMinIncomeSizeMap");
        String _schemaVersion = doc.getString("_schemaVersion");
        String _serviceId = doc.getString("_serviceId");

        ReturnType rt = new ReturnType();
        rt.set_total(Integer.valueOf(_total).intValue());
        rt.set_items(JSON.parseObject(_items, List.class));
        rt.setName(name);
        rt.set_links(JSON.parseObject(_links, Map.class));
        rt.setMaxMinMap(JSON.parseObject(maxMinMap, Map.class));
        rt.setMaxMinBenchmarkMap(JSON.parseObject(maxMinBenchmarkMap, Map.class));
        rt.setExpectedIncomeSizeMap(JSON.parseObject(expectedIncomeSizeMap, Map.class));
        rt.setHighPercentMaxIncomeSizeMap(JSON.parseObject(highPercentMaxIncomeSizeMap, Map.class));
        rt.setHighPercentMinIncomeSizeMap(JSON.parseObject(highPercentMinIncomeSizeMap, Map.class));
        rt.setLowPercentMaxIncomeSizeMap(JSON.parseObject(lowPercentMaxIncomeSizeMap, Map.class));
        rt.setLowPercentMinIncomeSizeMap(JSON.parseObject(lowPercentMinIncomeSizeMap, Map.class));
        rt.set_schemaVersion(_schemaVersion);
        rt.set_serviceId(_serviceId);

        return rt;
    }

    /**
     * 组合收益率(最大回撤)走势图
     *
     * @param groupId
     * @param subGroupId
     * @param mouth      几个月以来每天
     * @return
     * @throws ParseException
     */
    public ReturnType getFundGroupIncome(String groupId, String subGroupId, int mouth, String returnType) {
        Calendar ca = Calendar.getInstance();
        Date enddate = new Date();
        ca.setTime(enddate);
        ca.add(Calendar.MONTH, mouth);
        String starttime = DateUtil.formatDate(ca.getTime());
        ca.setTime(enddate);
        ca.add(Calendar.DATE, -1);
        String endtime = DateUtil.formatDate(ca.getTime());
        Map<String, String> mapStr = new HashMap<>();
        mapStr.put("fund_group_id", groupId);
        mapStr.put("fund_group_sub_id", subGroupId);
        mapStr.put("starttime", starttime);
        mapStr.put("endtime", endtime);
//        List<FundGroupHistory> fundGroupHistoryList = fundGroupMapper.getHistoryOne(mapStr);
//
//        ReturnType fgi = this.getFundGroupIncomeFromListAndType(fundGroupHistoryList, returnType);
        ReturnType fgi = this.getFundGroupIncomeAllFromMongo(groupId, subGroupId, returnType);
		if (fgi != null) {
			List<Map<String, Object>> _items = fgi.get_items();
			if (_items != null && !_items.isEmpty()) {
				Map<String, Object> itemsMap = _items.get(0);
				if (itemsMap != null && itemsMap.get("income") != null) {
					List<Map<String, Object>> incomeList = (List<Map<String, Object>>) itemsMap.get("income");
					List<Map<String, Object>> resultList = new ArrayList<>();
					if (incomeList != null && incomeList.size() > 0) {
						for (Map<String, Object> incomeMap : incomeList) {
							String time = incomeMap.get("time") + "";
							starttime = starttime.replaceAll("-", "");
							time = time.replaceAll("-", "");
							if (TradeUtil.getLongNumWithMul100(time) - TradeUtil.getLongNumWithMul100(starttime) >= 0 || resultList.size() > 0) {
								resultList.add(incomeMap);
							}
						}
//						itemsMap.put("income", resultList);
						fgi.set_items(resultList);
						fgi.set_total(resultList.size());
					}
				}
			}
		}
        return fgi;
    }

    private ReturnType getFundGroupIncomeFromListAndType(List<FundGroupHistory> fundGroupHistoryList, String returnType) {
        ReturnType fgi = new ReturnType();
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, String> _links = new HashMap<>();
        Map maxMinValueMap = new HashMap();
        if (CollectionUtils.isEmpty(fundGroupHistoryList)) {
            if (returnType.equalsIgnoreCase("income")) {
                fgi.setName("组合收益率走势图");
            } else {
                fgi.setName("组合最大回撤走势图");
            }
            fgi.set_total(0);
            fgi.set_items(list);
            fgi.set_links(_links);
            fgi.set_schemaVersion("0.1.1");
            fgi.set_serviceId("资产配置");
            fgi.setMaxMinMap(maxMinValueMap);
            return fgi;
        }

        List<Double> maxMinValueList = new ArrayList<Double>();
        if (returnType.equalsIgnoreCase("income")) {
            for (int i = 0; i < fundGroupHistoryList.size(); i++) {
                Map<String, Object> map = new HashMap<>();
                map.put("time", DateUtil.formatDate(fundGroupHistoryList.get(i).getTime()));
                double earningRate = (fundGroupHistoryList.get(i).getIncome_num() - fundGroupHistoryList.get(0).getIncome_num()) / fundGroupHistoryList.get(0).getIncome_num();
                map.put("value", earningRate);
                list.add(map);
                maxMinValueList.add(earningRate);
            }
            maxMinValueMap = TransformUtil.getMaxMinValue(maxMinValueList);
            fgi.setName("组合收益率走势图");
        } else {
            for (FundGroupHistory fundGroupHistory : fundGroupHistoryList) {
                Map<String, Object> map = new HashMap<>();
                map.put("time", DateUtil.formatDate(fundGroupHistory.getTime()));
                map.put("value", fundGroupHistory.getMaximum_retracement());
                list.add(map);
                maxMinValueList.add(fundGroupHistory.getMaximum_retracement());
            }
            maxMinValueMap = TransformUtil.getMaxMinValue(maxMinValueList);
            fgi.setName("组合最大回撤走势图");
        }
        fgi.set_total(list.size());
        fgi.set_items(list);
        fgi.set_links(_links);
        fgi.set_schemaVersion("0.1.1");
        fgi.set_serviceId("资产配置");
        fgi.setMaxMinMap(maxMinValueMap);

        return fgi;
    }

    /**
     * 组合收益率(最大回撤)走势图     一周以来以来每天
     *
     * @param id
     * @param subGroupId
     * @return
     */
    public ReturnType getFundGroupIncomeWeek(String id, String subGroupId, String returnType) {
        ReturnType fgi = new ReturnType();
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, String> _links = new HashMap<>();
        Map<String, Object> allMap = new HashMap<>();
        Map maxMinValueMap = new HashMap();
        Map maxMinBenchmarkMap = new HashMap();

        Map<String, Object> queryTime = new HashMap<>();
        queryTime.put("fund_group_id", id);
        queryTime.put("subGroupId", subGroupId);
        String endTime = fundGroupMapper.getFundGroupHistoryTime(queryTime);
        if (StringUtils.isEmpty(endTime)) {
            if (returnType.equalsIgnoreCase("income")) {
                allMap.put("income", new ArrayList<>());
                allMap.put("incomeBenchmark", new ArrayList<>());
                list.add(allMap);
                fgi.setName("组合收益率走势图");
            } else {
                allMap.put("retracement", new ArrayList<>());
                allMap.put("incomeBenchmark", new ArrayList<>());
                list.add(allMap);
                fgi.setName("组合最大回撤走势图");
            }
            fgi.set_total(0);
            fgi.set_items(list);
            fgi.set_links(_links);
            fgi.set_schemaVersion("0.1.1");
            fgi.set_serviceId("资产配置");
            fgi.setMaxMinMap(maxMinValueMap);
            fgi.setMaxMinBenchmarkMap(maxMinBenchmarkMap);
            return fgi;
        }

        Calendar ca = Calendar.getInstance();
        Date endDate = DateUtil.getDateFromFormatStr(endTime);
        ca.setTime(endDate);
        ca.add(Calendar.DATE, -7);
        String startTime = DateUtil.formatDate(ca.getTime());
        Map<String, String> mapStr = new HashMap<>();
        mapStr.put("fund_group_id", id);
        mapStr.put("fund_group_sub_id", subGroupId);
        mapStr.put("starttime", startTime);
        mapStr.put("endtime", endTime);
        List<FundGroupHistory> fundGroupHistoryList = fundGroupMapper.getHistoryOne(mapStr);
        if (CollectionUtils.isEmpty(fundGroupHistoryList)) {
            if (returnType.equalsIgnoreCase("income")) {
                allMap.put("income", new ArrayList<>());
                allMap.put("incomeBenchmark", new ArrayList<>());
                list.add(allMap);
                fgi.setName("组合收益率走势图");
            } else {
                allMap.put("retracement", new ArrayList<>());
                allMap.put("incomeBenchmark", new ArrayList<>());
                list.add(allMap);
                fgi.setName("组合最大回撤走势图");
            }
            fgi.set_total(0);
            fgi.set_items(list);
            fgi.set_links(_links);
            fgi.set_schemaVersion("0.1.1");
            fgi.set_serviceId("资产配置");
            fgi.setMaxMinMap(maxMinValueMap);
            fgi.setMaxMinBenchmarkMap(maxMinBenchmarkMap);
            return fgi;
        }

        List maxMinValueList = new ArrayList();
        List maxMinBenchmarkList = new ArrayList();
        if (returnType.equalsIgnoreCase("income")) {
            List<Map<String, Object>> listFund = new ArrayList<>();
            for (int i = 1; i< fundGroupHistoryList.size(); i++) {
                Map<String, Object> mapBasic = new HashMap<>();
                mapBasic.put("time", DateUtil.formatDate(fundGroupHistoryList.get(i).getTime()));
                mapBasic.put("value", (fundGroupHistoryList.get(i).getIncome_num() - fundGroupHistoryList.get(i - 1).getIncome_num()) / fundGroupHistoryList.get(i-1).getIncome_num());
                listFund.add(mapBasic);
                maxMinValueList.add((fundGroupHistoryList.get(i).getIncome_num() - fundGroupHistoryList.get(i - 1).getIncome_num()) / fundGroupHistoryList.get(i-1).getIncome_num());
            }
            maxMinValueMap = TransformUtil.getMaxMinValue(maxMinValueList);
            allMap.put("income", listFund);

            String riskNum = fundGroupMapper.getRiskNum(fundGroupHistoryList.get(0).getFund_group_id());
            mapStr.put("fund_group_id", riskNum);
            mapStr.remove("fund_group_sub_id");
            fundGroupHistoryList = fundGroupMapper.getHistoryOne(mapStr);
            List<Map<String, Object>> listBenchmark = new ArrayList<>();
            for (int i = 1; i < fundGroupHistoryList.size(); i++) {
                Map<String, Object> mapBenchmark = new HashMap<>();
                mapBenchmark.put("time", DateUtil.formatDate(fundGroupHistoryList.get(i).getTime()));
                mapBenchmark.put("value", (fundGroupHistoryList.get(i).getIncome_num() - fundGroupHistoryList.get(i - 1).getIncome_num()) / fundGroupHistoryList.get(i-1).getIncome_num());
                listBenchmark.add(mapBenchmark);
                maxMinBenchmarkList.add((fundGroupHistoryList.get(i).getIncome_num() - fundGroupHistoryList.get(i - 1).getIncome_num()) / fundGroupHistoryList.get(i-1).getIncome_num());
            }
            maxMinBenchmarkMap = TransformUtil.getMaxMinValue(maxMinBenchmarkList);
            allMap.put("incomeBenchmark", listBenchmark);
            list.add(allMap);
            fgi.setName("组合收益率走势图");
        } else {
            List<Map<String, Object>> listFund = new ArrayList<>();
            for (FundGroupHistory fundGroupHistory : fundGroupHistoryList) {
                Map<String, Object> mapBasic = new HashMap<>();
                mapBasic.put("time", DateUtil.formatDate(fundGroupHistory.getTime()));
                mapBasic.put("value", fundGroupHistory.getMaximum_retracement());
                listFund.add(mapBasic);
                maxMinValueList.add(fundGroupHistory.getMaximum_retracement());
            }
            maxMinValueMap = TransformUtil.getMaxMinValue(maxMinValueList);
            allMap.put("retracement", listFund);

            String riskNum = fundGroupMapper.getRiskNum(fundGroupHistoryList.get(0).getFund_group_id());
            mapStr.put("fund_group_id", riskNum);
            mapStr.remove("fund_group_sub_id");
            fundGroupHistoryList = fundGroupMapper.getHistoryOne(mapStr);
            List<Map<String, Object>> listBenchmark = new ArrayList<>();
            for (FundGroupHistory fundGroupHistory : fundGroupHistoryList) {
                Map<String, Object> mapBenchmark = new HashMap<>();
                mapBenchmark.put("time", DateUtil.formatDate(fundGroupHistory.getTime()));
                mapBenchmark.put("value", fundGroupHistory.getMaximum_retracement());
                listBenchmark.add(mapBenchmark);
                maxMinBenchmarkList.add(fundGroupHistory.getMaximum_retracement());
            }
            maxMinBenchmarkMap = TransformUtil.getMaxMinValue(maxMinBenchmarkList);
            allMap.put("incomeBenchmark", listBenchmark);
            list.add(allMap);
            fgi.setName("组合最大回撤走势图");
        }
        fgi.set_total(list.size());
        fgi.set_items(list);
        fgi.set_links(_links);
        fgi.set_schemaVersion("0.1.1");
        fgi.set_serviceId("资产配置");
        fgi.setMaxMinMap(maxMinValueMap);
        fgi.setMaxMinBenchmarkMap(maxMinBenchmarkMap);

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
    public ReturnType getFundNetValue(String id, String subGroupId, String returnType) {
        ReturnType fgi = new ReturnType();
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, String> _links = new HashMap<>();

        if (returnType.equalsIgnoreCase("1")) {
            fgi.setName("净值增长");
        } else {
            fgi.setName("净值增长率");
        }
        fgi.set_total(0);
        fgi.set_items(list);
        fgi.set_links(_links);
        fgi.set_schemaVersion("0.1.1");
        fgi.set_serviceId("资产配置");

        Map<String, String> query = new HashMap<>();
        query.put("id", id);
        query.put("subId", subGroupId);
        List<Interval> intervalProportionList = fundGroupMapper.getProportion(query);
        if (CollectionUtils.isEmpty(intervalProportionList)) {
            return fgi;
        }

        List<Interval> intervalProportions = new ArrayList<>();
        for (Interval tmpInterval : intervalProportionList) {
            if (tmpInterval.getProportion() > 0d) {
                intervalProportions.add(tmpInterval);
            }
        }

        List<Interval> intervalCodes = fundGroupMapper.getFundCode(query);
        if (CollectionUtils.isEmpty(intervalCodes)) {
            return fgi;
        }

        for (Interval intervalCode : intervalCodes) {
            Map<String, Object> map = new HashMap<>();
            for(Interval intervalProportion : intervalProportions){
                if(intervalCode.getFund_type_two().equalsIgnoreCase(intervalProportion.getFund_type_two())
                        && intervalCode.getFund_id().equals(intervalProportion.getFund_id())){
                    map.put("type_value", intervalProportion.getProportion());
                    break;
                }
            }

            if (StringUtils.isEmpty(map.get("type_value"))
                    || Double.parseDouble(map.get("type_value").toString()) == 0d) {
                continue;
            }

            Map<String, String> query1 = new HashMap<>();
            query1.put("fund_code", intervalCode.getFund_id());
            List<FundNetVal> fundNetValues = fundGroupMapper.getFundNetValue(query1);

            List<Map<String, Object>> listFund = new ArrayList<>();
            if (!CollectionUtils.isEmpty(fundNetValues)) {
                for (int i = 1; i < fundNetValues.size(); i++) {
                    Map<String, Object> fundMap = new HashMap<>();
                    if (returnType.equalsIgnoreCase("1")) {
                        fgi.setName("净值增长");
                        fundMap.put("time", DateUtil.formatDate(fundNetValues.get(i).getNavLatestDate()));
                        fundMap.put("value", fundNetValues.get(i).getNavadj());
                    } else {
                        fgi.setName("净值增长率");
                        double navadjReturn = (fundNetValues.get(i).getNavadj() - fundNetValues.get(0).getNavadj()) / fundNetValues.get(0).getNavadj();
                        fundMap.put("time", DateUtil.formatDate(fundNetValues.get(i).getNavLatestDate()));
                        fundMap.put("value", navadjReturn);
                    }
                    listFund.add(fundMap);
                }
            }
            map.put("navadj", listFund);
            map.put("fund_type_two", intervalCode.getFund_type_two());
            map.put("fund_code", intervalCode.getFund_id());
            map.put("name", intervalCode.getFname());
            list.add(map);
        }
        fgi.set_total(list.size());
        fgi.set_items(list);
        fgi.set_links(_links);
        fgi.set_schemaVersion("0.1.1");
        fgi.set_serviceId("资产配置");

        return fgi;
    }

    /**
     * 未来收益走势图
     *
     * @param groupId
     * @param subGroupId
     * @return
     */
    public ReturnType getExpectedIncome(String groupId, String subGroupId) {
        ReturnType rt = new ReturnType();
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, String> _links = new HashMap<>();
        Map<String, String> query = new HashMap<>();
        query.put("fund_group_id", "1");
        query.put("subGroupId", "1");
        List<FundGroupExpectedIncome> fgeiList = fundGroupMapper.getExpectedIncome(query);
        if (CollectionUtils.isEmpty(fgeiList)) {
            return rt;
        }

        Map expectedIncomeSizeMap  = new HashMap();
        Map highPercentMaxIncomeSizeMap  = new HashMap();
        Map highPercentMinIncomeSizeMap  = new HashMap();
        Map lowPercentMaxIncomeSizeMap  = new HashMap();
        Map lowPercentMinIncomeSizeMap  = new HashMap();
        List expectedIncomeSizeList = new ArrayList();
        List highPercentMaxIncomeSizeList = new ArrayList();
        List highPercentMinIncomeSizeList = new ArrayList();
        List lowPercentMaxIncomeSizeList = new ArrayList();
        List lowPercentMinIncomeSizeList = new ArrayList();
        for (FundGroupExpectedIncome fgei : fgeiList) {
            Map<String, Object> map = new HashMap<>();
            Map<String, Object> map1 = new HashMap<>();
            map1.put("expected_income", fgei.getExpected_income());
            //高概率最高收益
            map1.put("high_percent_max_income", fgei.getHigh_percent_max_income());
            //高概率最低收益
            map1.put("high_percent_min_income", fgei.getHigh_percent_min_income());
            //低概率最高收益
            map1.put("low_percent_max_income", fgei.getLow_percent_max_income());
            //低概率最低收益
            map1.put("low_percent_min_income", fgei.getLow_percent_min_income());
            map.put("income_mounth_time", fgei.getIncome_mounth_time());
            map.put("_item", map1);
            list.add(map);
            expectedIncomeSizeList.add(fgei.getExpected_income());
            highPercentMaxIncomeSizeList.add(fgei.getHigh_percent_max_income());
            highPercentMinIncomeSizeList.add(fgei.getHigh_percent_min_income());
            lowPercentMaxIncomeSizeList.add(fgei.getLow_percent_max_income());
            lowPercentMinIncomeSizeList.add(fgei.getLow_percent_min_income());
        }
        expectedIncomeSizeMap = TransformUtil.getMaxMinValue(expectedIncomeSizeList);
        highPercentMaxIncomeSizeMap = TransformUtil.getMaxMinValue(highPercentMaxIncomeSizeList);
        highPercentMinIncomeSizeMap = TransformUtil.getMaxMinValue(highPercentMinIncomeSizeList);
        lowPercentMaxIncomeSizeMap = TransformUtil.getMaxMinValue(lowPercentMaxIncomeSizeList);
        lowPercentMinIncomeSizeMap = TransformUtil.getMaxMinValue(lowPercentMinIncomeSizeList);
        rt.set_total(list.size());
        rt.set_items(list);
        rt.set_links(_links);
        rt.set_schemaVersion("0.1.1");
        rt.set_serviceId("资产配置");
        rt.setExpectedIncomeSizeMap(expectedIncomeSizeMap);
        rt.setHighPercentMaxIncomeSizeMap(highPercentMaxIncomeSizeMap);
        rt.setHighPercentMinIncomeSizeMap(highPercentMinIncomeSizeMap);
        rt.setLowPercentMaxIncomeSizeMap(lowPercentMaxIncomeSizeMap);
        rt.setLowPercentMinIncomeSizeMap(lowPercentMinIncomeSizeMap);

        return rt;
    }

    public List<String> findAllGroupCode(){
        return fundGroupMapper.findAllGroupCode();
    }

    public List<FundNetVal> getNavadjNew(String groupId, String subGroupId) {
        List<Date> navDateList = fundGroupService.getNavlatestdateCount(groupId, subGroupId);

        Map query = new HashMap();
        query.put("groupId", groupId);
        query.put("subGroupId", subGroupId);
        query.put("list", navDateList);
        List<FundNetVal> fundNetVals = fundGroupMapper.getNavadjByNavDates(query);

        return fundNetVals;
    }

    /**
     * 计算组合单位收益净值和最大回撤
     *
     * @param group_id
     * @param subGroupId
     */
    public void getNavadj(String group_id, String subGroupId) {
        logger.info("getNavadj begin");

        Map<String, Object> query = new HashMap<>();
        query.put("fund_group_id", group_id);
        query.put("subGroupId", subGroupId);

        List<String> codeList = getFundGroupCodes(group_id, subGroupId);
        if (CollectionUtils.isEmpty(codeList)) {
            return ;
        }

        //查询组合中基金最晚成立日 作为 该组合成立日
        Date minNavDate = fundNetValMapper.getMinNavDateByCodeList(codeList);
        String startTime = DateUtil.formatDate(minNavDate);
        query.put("startTime", startTime);

        List<FundNetVal> list = this.getNavadjNew(group_id, subGroupId);
        if (CollectionUtils.isEmpty(list)) {
            return ;
        }

        this.insertToFundGroupHistory(list, group_id, subGroupId, startTime);

        List<Map> updateMapList = new ArrayList<>();
        Calendar ca = Calendar.getInstance();
        Date date = new Date();
        Date groupStartDate = DateUtil.getDateFromFormatStr(startTime);

        List<FundNetVal> fundNetValList = null;
        if (date.getTime() > groupStartDate.getTime()) {
            query.put("endTime", DateUtil.formatDate(ca.getTime()));
            fundNetValList = fundGroupMapper.getNavadj(query);
        }

        long startMaxRetracement = System.currentTimeMillis();
        for ( ; !CollectionUtils.isEmpty(fundNetValList) && date.getTime() > groupStartDate.getTime(); ) {
            long beginGetNewMaxDrawDown = System.currentTimeMillis();
            Double maximumRetracement = getMaxdrawdownFromNetVals(fundNetValList);
            long endGetNewMaxDrawDown = System.currentTimeMillis();
            logger.info("calculate MaxDrawDown elapse : {}", endGetNewMaxDrawDown - beginGetNewMaxDrawDown);
            logger.info("MaxDrawDown: {}", maximumRetracement);

            Map<String, Object> updateParam = new HashMap<>();
            updateParam.put("fund_group_id", group_id);
            updateParam.put("subGroupId", subGroupId);
            updateParam.put("retracement", maximumRetracement);
            updateParam.put("time", DateUtil.formatDate(date));
            updateMapList.add(updateParam);

            fundNetValList.remove(fundNetValList.size() - 1);

            ca.setTime(date);
            ca.add(Calendar.DATE,-1);
            date = ca.getTime();
        }
        long endMaxRetracement = System.currentTimeMillis();
        logger.info("maxRetracement elapse : {} ", endMaxRetracement - startMaxRetracement);

        if (!CollectionUtils.isEmpty(updateMapList)) {
            logger.info("batchUpdateMaximumRetracement begin, updateMapList.size() : {}", updateMapList.size());
            long beginBatchUpdate = System.currentTimeMillis();
            batchUpdateMaximumRetracement(updateMapList);
            long endBatchUpdate = System.currentTimeMillis();
            logger.info("batch update elapse : {}", endBatchUpdate - beginBatchUpdate);
        }

        logger.info("getNavadj end");
    }

    private void insertToFundGroupHistory(List<FundNetVal> fundNetValList, String fundGroupId, String subGroupId, String startTime) {
        List<Map> fundGroupHistoryMapList = new ArrayList<>();
        for (int i = 0; i < fundNetValList.size(); i++) {
            Map<String, Object> historyMap = new HashMap<>();
            historyMap.put("fundGroupId", fundGroupId);
            historyMap.put("subGroupId", subGroupId);
            historyMap.put("incomeNum", fundNetValList.get(i).getNavadj());
            historyMap.put("time", fundNetValList.get(i).getNavLatestDate());
            if (StringUtils.isEmpty(startTime)) {
                if (i != 0) {
                    fundGroupHistoryMapList.add(historyMap);
                }
            } else {
                fundGroupHistoryMapList.add(historyMap);
            }
        }

        if (!CollectionUtils.isEmpty(fundGroupHistoryMapList)) {
            logger.info("batchInsertFundGroupHistory begin, fundGroupHistoryMapList.size() : {}", fundGroupHistoryMapList.size());
            long beginBatchInsert = System.currentTimeMillis();
            batchInsertFundGroupHistory(fundGroupHistoryMapList);
            long endBatchInsert = System.currentTimeMillis();
            logger.info("batch insert elapse : {} ", endBatchInsert - beginBatchInsert);
            logger.info("batchInsertFundGroupHistory end");
        }
    }

    private void batchInsertFundGroupHistory(List<Map> dataMapList) {
        if (CollectionUtils.isEmpty(dataMapList)) {
            return;
        }
        List<Map> mapList = new ArrayList<>();
        for (Map map : dataMapList) {
            mapList.add(map);
            if (mapList.size() == BATCH_SIZE_NUM) {
                fundGroupMapper.batchInsertFundGroupHistory(mapList);
                mapList.clear();
            }
        }
        if (!CollectionUtils.isEmpty(mapList)) {
            fundGroupMapper.batchInsertFundGroupHistory(mapList);
        }
        return;
    }

    private void batchUpdateMaximumRetracement(List<Map> dataMapList) {
        if (CollectionUtils.isEmpty(dataMapList)) {
            return;
        }
        List<Map> mapList = new ArrayList<>();
        for (Map map : dataMapList) {
            mapList.add(map);
            if (mapList.size() == BATCH_SIZE_NUM) {
                fundGroupMapper.batchUpdateMaximumRetracement(mapList);
                mapList.clear();
            }
        }
        if (!CollectionUtils.isEmpty(mapList)) {
            fundGroupMapper.batchUpdateMaximumRetracement(mapList);
        }
        return;
    }

    /**
     * 计算组合基准单位收益净值和最大回撤
     *
     * @param risk_level
     */
    public void  getNavadjBenchmark(String risk_level) {
        logger.info("getNavadjBenchmark begin, risk_level : {}", risk_level);

        Map<String, Object> query = new HashMap<>();
        query.put("risk_level", risk_level);
        String groupStartTime = fundGroupMapper.getFundGroupHistoryTimeByRiskLevel(query);

        Calendar ca = Calendar.getInstance();
        Date date = new Date();
        String startTime = null;
        if (StringUtils.isEmpty(groupStartTime)) {
            ca.setTime(date);
            ca.add(Calendar.YEAR, -3);
            groupStartTime = DateUtil.formatDate(ca.getTime());
            startTime = groupStartTime;
        }
        query.put("startTime", groupStartTime);
        List<FundNetVal> list = fundGroupMapper.getNavadjBenchmark(query);
        this.insertFundGroupHistoryBenchmark(list, risk_level, startTime);

        if (StringUtils.isEmpty(startTime)) {
            ca.setTime(date);
            ca.add(Calendar.YEAR, -3);
            startTime = DateUtil.formatDate(ca.getTime());
        }
        query.put("startTime", startTime);

        List<Map> updateMapList = new ArrayList<>();
        Date groupStartDate = DateUtil.getDateFromFormatStr(groupStartTime);

        List<FundNetVal> fundNetValList = null;
        if (date.getTime() > groupStartDate.getTime()) {
            query.put("endTime", DateUtil.formatDate(date));
            fundNetValList = fundGroupMapper.getNavadj(query);
        }
        long startMaxRetracement = System.currentTimeMillis();
        for ( ; !CollectionUtils.isEmpty(fundNetValList) && date.getTime() > groupStartDate.getTime(); ) {
            long beginGetNewMaxDrawDown = System.currentTimeMillis();
            Double maximumRetracement = getMaxdrawdownFromNetVals(fundNetValList);
            long endGetNewMaxDrawDown = System.currentTimeMillis();
            logger.info("calculate MaxDrawDown elapse : {}", endGetNewMaxDrawDown - beginGetNewMaxDrawDown);
            logger.info("MaxDrawDown: {}", maximumRetracement);

            Map<String, Object> updateParam = new HashMap<>();
            updateParam.put("risk_level", risk_level);
            updateParam.put("retracement", maximumRetracement);
            updateParam.put("time", DateUtil.formatDate(date));
            updateMapList.add(updateParam);

            fundNetValList.remove(fundNetValList.size() - 1);

            ca.setTime(date);
            ca.add(Calendar.DATE,-1);
            date = ca.getTime();
        }
        long endMaxRetracement = System.currentTimeMillis();
        logger.info("benchmark maxRetracement elapse : {} ", endMaxRetracement - startMaxRetracement);

        if (!CollectionUtils.isEmpty(updateMapList)) {
            logger.info("batchUpdateMaximumRetracementByRiskLevel begin, updateMapList.size() : {}", updateMapList.size());
            long beginBatchUpdate = System.currentTimeMillis();
            batchUpdateMaximumRetracementByRiskLevel(updateMapList);
            long endBatchUpdate = System.currentTimeMillis();
            logger.info("batch update elapse : {}", endBatchUpdate - beginBatchUpdate);
        }

        logger.info("getNavadjBenchmark end");
    }

    private void insertFundGroupHistoryBenchmark(List<FundNetVal> fundNetValList, String risk_level, String startTime) {
        List<Map> benchmarkMapList = new ArrayList<>();
        for (int i = 0; i < fundNetValList.size(); i++) {
            Map<String, Object> benchmarkMap = new HashMap<>();
            benchmarkMap.put("risk_level", risk_level);
            benchmarkMap.put("incomeNum", fundNetValList.get(i).getNavadj());
            benchmarkMap.put("time", fundNetValList.get(i).getNavLatestDate());
            if (StringUtils.isEmpty(startTime)) {
                if (i != 0) {
                    benchmarkMapList.add(benchmarkMap);
                }
            } else {
                benchmarkMapList.add(benchmarkMap);
            }
        }

        if (!CollectionUtils.isEmpty(benchmarkMapList)) {
            logger.info("batchInsertFundGroupHistoryBenchmark begin, benchmarkMapList.size() : {}", benchmarkMapList.size());
            long beginBatchInsert = System.currentTimeMillis();
            batchInsertFundGroupHistoryBenchmark(benchmarkMapList);
            long endBatchInsert = System.currentTimeMillis();
            logger.info("batch insert benchmark elapse : {} ", endBatchInsert - beginBatchInsert);
            logger.info("batchInsertFundGroupHistoryBenchmark end");
        }
    }

    private void batchInsertFundGroupHistoryBenchmark(List<Map> dataMapList) {
        if (CollectionUtils.isEmpty(dataMapList)) {
            return;
        }
        List<Map> mapList = new ArrayList<>();
        for (Map map : dataMapList) {
            mapList.add(map);
            if (mapList.size() == BATCH_SIZE_NUM) {
                fundGroupMapper.batchInsertFundGroupHistoryBenchmark(mapList);
                mapList.clear();
            }
        }
        if (!CollectionUtils.isEmpty(mapList)) {
            fundGroupMapper.batchInsertFundGroupHistoryBenchmark(mapList);
        }
        return;
    }

    private void batchUpdateMaximumRetracementByRiskLevel(List<Map> dataMapList) {
        if (CollectionUtils.isEmpty(dataMapList)) {
            return;
        }
        List<Map> mapList = new ArrayList<>();
        for (Map map : dataMapList) {
            mapList.add(map);
            if (mapList.size() == BATCH_SIZE_NUM) {
                fundGroupMapper.batchUpdateMaximumRetracementByRiskLevel(mapList);
                mapList.clear();
            }
        }
        if (!CollectionUtils.isEmpty(mapList)) {
            fundGroupMapper.batchUpdateMaximumRetracementByRiskLevel(mapList);
        }
        return;
    }

    /**
     * 通过组合收益净值序列得到基金组合最大回撤值
     * @param list
     * @return
     */
    public double getMaxdrawdowns(List<FundNetVal> list) {
        double[] temp = new double[list.size()];
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getNavadj() == null  || list.get(i).getNavadj() == 0) {
                for (int k = 0; ; k++) {
                    if (list.get(k).getNavadj() != null  && list.get(k).getNavadj() != 0) {
                        temp[i] = list.get(k).getNavadj();
                        break;
                    }
                }
            } else {
                temp[i] = list.get(i).getNavadj();
            }
        }
        double maximum_retracement = 0;
        if (temp.length > 1) {
            maximum_retracement = CalculateMaxdrawdowns.calculateMaxdrawdown(temp) * (-1);
        }
        return maximum_retracement;
    }

    /**
     * 通过组合收益净值序列得到基金组合最大回撤值
     * @param fundNetValList
     * @return
     */
    public Double getMaxdrawdownFromNetVals(List<FundNetVal> fundNetValList) {
        List<Double> data = new ArrayList<>();
        for (int i = 0; i < fundNetValList.size(); i++) {
            if (fundNetValList.get(i).getNavadj() == null  || fundNetValList.get(i).getNavadj() == 0) {
                for (int k = 0; ; k++) {
                    if (fundNetValList.get(k).getNavadj() != null  && fundNetValList.get(k).getNavadj() != 0) {
                        data.add(fundNetValList.get(k).getNavadj());
                        break;
                    }
                }
            } else {
                data.add(fundNetValList.get(i).getNavadj());
            }
        }
        Double maximumRetracement = 0d;
        int length = data.size();
        if (length > 1) {
            maximumRetracement = CalculateMaxdrawdowns.calculateMaxdrawdown(data);
        }
        return maximumRetracement;
    }

    /**
     * 计算夏普比率
     *
     * @param group_id
     * @param subGroupId
     * @return
     */
    public int sharpeRatio(String group_id, String subGroupId) {
        logger.info("sharpeRatio begin");

        int effectRow = -1;
        Double cash = 0.0135;
        Map<String, String> query = new HashMap<>();
        query.put("fund_group_id", group_id);
        query.put("subGroupId", subGroupId);
        List<FundNetVal> fundNetValList = fundGroupMapper.getSharpeRatio(query);
        if (CollectionUtils.isEmpty(fundNetValList)) {
            return effectRow;
        }

        List<FundNetVal> filteredFundNetVals = new ArrayList<>();
        for (FundNetVal fundNetVal : fundNetValList) {
            if (null == fundNetVal) {
                continue;
            }
            filteredFundNetVals.add(fundNetVal);
        }
        if (CollectionUtils.isEmpty(filteredFundNetVals)) {
            return effectRow;
        }

        Double[] asset = new Double[filteredFundNetVals.size()];
        int i = 0;
        for (FundNetVal fundNetVal : filteredFundNetVals) {
            asset[i++] = fundNetVal.getNavadj();
        }

        double sharpeRatio = Double.parseDouble(MVO.sharpeRatio(asset, cash).toString());
        Map<String, Object> update = new HashMap<>();
        update.put("id", subGroupId);
        update.put("sharpeRatio", sharpeRatio);
        effectRow = fundGroupMapper.updateSharpeRatio(update);

        logger.info("sharpeRatio end");

        return effectRow;
    }

    /**
     * 更新 基金组合 的最大亏损额
     * @param fundGroupId
     * @param subGroupId
     */
    public void maximumLosses(String fundGroupId, String subGroupId) {
        Map<String, Object> query = new HashMap<>();
        query.put("slidebarType", "risk_num");
        query.put("subGroupId", subGroupId);
        query.put("fundGroupId", fundGroupId);
        List<RiskIncomeInterval> riskIncomeIntervals = fundGroupMapper.getScaleMark(query);
        if (CollectionUtils.isEmpty(riskIncomeIntervals)) {
            return;
        }

        Double maximumLosses = CalculatePortvrisks.calculatePortvrisk(riskIncomeIntervals.get(0).getIncome_num(), riskIncomeIntervals.get(0).getRisk_num(), 0.975, 10000);
        query.put("maximum_losses", maximumLosses);
        fundGroupMapper.updateMaximumLosses(query);
    }

    /**
     * 更新 所有基金组合 的最大亏损额
     */
    public void updateAllMaximumLosses() {
        try {
            final CountDownLatch countDownLatch = new CountDownLatch(ConstantUtil.FUND_GROUP_COUNT);
            ExecutorService pool = ThreadPoolUtil.getThreadPool();
            for (int index = 1; index <= ConstantUtil.FUND_GROUP_COUNT; index++) {
                int fundGroupId = index;
                pool.execute(() -> {
                    updateMaximumLossesTask(fundGroupId);
                    countDownLatch.countDown();
                });
            }
            this.sleep(1000);
            countDownLatch.await();
        } catch (InterruptedException e) {
            logger.error("exception:",e);
        }
    }

    private void updateMaximumLossesTask(int fundGroupId) {
        Map<String, Object> map = new HashMap<>();
        map.put("slidebarType", "risk_num");
        map.put("fundGroupId", fundGroupId);
        List<RiskIncomeInterval> riskIncomeIntervals = fundGroupMapper.getScaleMark(map);
        if (CollectionUtils.isEmpty(riskIncomeIntervals)) {
            return;
        }

        for (RiskIncomeInterval riskIncomeInterval : riskIncomeIntervals) {
            this.maximumLosses(fundGroupId + "", riskIncomeInterval.getId());
        }
    }

    /**
     * 把传出数据转为json格式
     *
     * @param groupId
     * @param subGroupId
     * @return
     */
    public FundReturn getProportionGroupByFundTypeTwo(String groupId, String subGroupId) {
        FundReturn fr = new FundReturn();
        Map<String, String> _links = new HashMap<>();

        Map<String, String> query = new HashMap<>();
        query.put("groupId", groupId);
        query.put("subGroupId", subGroupId);
        //基金组合内的各基金权重
        List<Interval> intervals = fundGroupMapper.getProportionGroupByFundTypeTwo(query);
        if (CollectionUtils.isEmpty(intervals)) {
            return fr;
        }
        List<Map<String, Object>> listMap = this.intervalListToListMap(intervals);

        String fundGroupName = fundGroupMapper.getFundGroupNameById(groupId);
        fr.setName(fundGroupName);
        fr.setGroupId(groupId);
        fr.setSubGroupId(subGroupId);
        fr.set_links(_links);
        fr.set_schemaVersion("0.1.1");
        fr.set_serviceId("资产配置");
        fr.setAssetsRatios(listMap);

        return fr;
    }

    public void getAllIdAndSubId(){
        logger.info("getAllIdAndSubId begin");
        long start = System.currentTimeMillis();

        this.fundGroupIdTasks();
        this.contribution();
        this.navadjBenchmark();

        long end = System.currentTimeMillis();
        logger.info("getAllIdAndSubId elapse : {}", end - start);
        logger.info("getAllIdAndSubId end");
    }

    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (Exception e) {
            logger.error("exception:",e);
        }
    }

    private void navadjBenchmark() {
        logger.info("navadjBenchmark begin");
        long start = System.currentTimeMillis();

        try {
            final CountDownLatch countDownLatch = new CountDownLatch(RISK_LEVEL_COUNT);
            ExecutorService pool = ThreadPoolUtil.getThreadPool();
            for (int index = 1; index <= RISK_LEVEL_COUNT; index++) {
                String riskLevel = "C" + index;
                pool.execute(() -> {
                    getNavadjBenchmark(riskLevel);
                    countDownLatch.countDown();
                });
            }
            sleep(1000);
            countDownLatch.await();
        } catch (InterruptedException e) {
            logger.error("exception:",e);
        }

        long end = System.currentTimeMillis();
        logger.info("navadjBenchmark elapse : {}", end - start);
        logger.info("navadjBenchmark end");
    }

    private void fundGroupIdTasks() {
        try {
            final CountDownLatch countDownLatch = new CountDownLatch(ConstantUtil.FUND_GROUP_COUNT);
            ExecutorService pool = ThreadPoolUtil.getThreadPool();
            for (int index = 1; index <= ConstantUtil.FUND_GROUP_COUNT; index++) {
                int fundGroupId = index;
                pool.execute(() -> {
                    fundGroupIdTask(fundGroupId);
                    countDownLatch.countDown();
                });
            }
            this.sleep(1000);
            countDownLatch.await();
        } catch (InterruptedException e) {
            logger.error("exception:",e);
        }
    }

    private void fundGroupIdTask(int fundGroupId) {
        Map<String, Object> map = new HashMap<>();
        map.put("slidebarType", SlidebarTypeEnmu.RISK_NUM.getName());
        map.put("fundGroupId", fundGroupId);
        List<RiskIncomeInterval> riskIncomeIntervals = fundGroupMapper.getScaleMark(map);
        for (RiskIncomeInterval riskIncomeInterval : riskIncomeIntervals) {
            long startTime = System.currentTimeMillis();

            fundGroupIdAndSubIdTask(fundGroupId + "", riskIncomeInterval.getId());

            long endTime = System.currentTimeMillis();
            logger.info("fundGroupId : {} , subGroupId : {}", fundGroupId, riskIncomeInterval.getId());
            logger.info("one loop elapse : {}", endTime - startTime);
        }
    }

    public void fundGroupIdAndSubIdTask(String fundGroupId, String subGroupId) {
        getNavadj(fundGroupId, subGroupId);
        updateExpectedMaxRetracement(fundGroupId, subGroupId);
        sharpeRatio(fundGroupId, subGroupId);
    }

    /**
     * 计算预期最大回撤值/模拟波动率
     */
    public void updateExpectedMaxRetracement(String fundGroupId, String subGroupId) {
        logger.info("updateExpectedMaxRetracement begin");
        Map<String, Object> map = new HashMap<>();
        map.put("id", fundGroupId);
        map.put("subId", subGroupId);
        List<FundGroupHistory> fundGroupHistories = fundGroupMapper.selectMaximumRetracement(map);
        double retracement = 0d;
        for (FundGroupHistory fundGroupHistory : fundGroupHistories) {
            if (null != fundGroupHistory && retracement > fundGroupHistory.getMaximum_retracement()) {
                retracement = fundGroupHistory.getMaximum_retracement();
            }
        }
        map.put("expected_max_retracement", retracement);
        fundGroupMapper.updateExpectedMaximumRetracement(map);
        logger.info("updateExpectedMaxRetracement end");
    }

    public int deleteData(String tableName){
        return fundGroupMapper.deleteData(tableName);
    }

    /**
     * 计算所有组合中基金收益贡献比
     */
    public void contribution() {
        logger.info("contribution begin");
        long start = System.currentTimeMillis();

        Map<String, List<Interval>> groupedMap = this.getGroupedMapIntervals();
        if (CollectionUtils.isEmpty(groupedMap)) {
            return;
        }

        try {
            final CountDownLatch countDownLatch = new CountDownLatch(groupedMap.size());
            ExecutorService pool = ThreadPoolUtil.getThreadPool();
            for (List<Interval> groupedIntervals : groupedMap.values()) {
                List<Interval> intervals = groupedIntervals;
                pool.execute(() -> {
                    contributionTask(intervals);
                    countDownLatch.countDown();
                });
            }
            sleep(1000);
            countDownLatch.await();
        } catch (InterruptedException e) {
            logger.error("exception:",e);
        }

        long end = System.currentTimeMillis();
        logger.info("contribution elapse : {}", end - start);
        logger.info("contribution end");
    }

    private void contributionTask(List<Interval> intervals) {
        logger.info("contributionTask begin");

        for (Interval interval : intervals) {
            Map<String, Object> query = new HashMap<>();
            query.put("fund_group_id", interval.getFund_group_id());
            query.put("subGroupId", interval.getId());
            List<FundNetVal> navadjStart = fundGroupMapper.getNavadjStartTime(query);
            query.put("num", navadjStart.size());
            List<FundNetVal> navadjEnd = fundGroupMapper.getNavadjEndTime(query);
            double accumulatedIncome = 0;
            for (FundNetVal fundNetVal : navadjStart) {
                for (FundNetVal fundNetVal1 : navadjEnd) {
                    if (fundNetVal.getCode().equalsIgnoreCase(fundNetVal1.getCode()) && fundNetVal.getNavadj() != 0) {
                        accumulatedIncome += (fundNetVal1.getNavadj() - fundNetVal.getNavadj()) / fundNetVal.getNavadj();
                    }
                }
            }

            List<Map> updateMapList = new ArrayList<>();
            for (FundNetVal fundNetVal : navadjStart) {
                for (FundNetVal fundNetVal1 : navadjEnd) {
                    if (fundNetVal.getCode().equalsIgnoreCase(fundNetVal1.getCode())) {
                        Map<String, Object> updateParam = new HashMap<>();
                        if (fundNetVal.getNavadj() != 0) {
                            double contribution = (fundNetVal1.getNavadj() - fundNetVal.getNavadj()) / fundNetVal.getNavadj() / accumulatedIncome;
                            updateParam.put("code", fundNetVal.getCode());
                            updateParam.put("contribution", contribution);
                            updateParam.put("subGroupId", query.get("subGroupId"));
                            updateMapList.add(updateParam);
                            break;
                        } else {
                            updateParam.put("code", fundNetVal.getCode());
                            updateParam.put("contribution", 0);
                            updateParam.put("subGroupId", query.get("subGroupId"));
                            updateMapList.add(updateParam);
                            break;
                        }
                    }
                }
            }
            if (!CollectionUtils.isEmpty(updateMapList)) {
                this.batchUpdateContribution(updateMapList);
            }
        }

        logger.info("contributionTask end");
    }

    private Map<String, List<Interval>> getGroupedMapIntervals() {
        List<Interval> intervals = fundGroupMapper.getAllIdAndSubId();
        if (CollectionUtils.isEmpty(intervals)) {
            return null;
        }

        Map<String, List<Interval>> groupedMap = new HashMap<>();
        for (Interval interval : intervals) {
            List<Interval> groupedIntervals = groupedMap.get(interval.getFund_group_id());
            if (!CollectionUtils.isEmpty(groupedIntervals)) {
                groupedIntervals.add(interval);
            } else {
                List<Interval> tmpIntervals = new ArrayList<>();
                tmpIntervals.add(interval);
                groupedMap.put(interval.getFund_group_id(), tmpIntervals);
            }
        }
        return groupedMap;
    }

    private void batchUpdateContribution(List<Map> dataMapList) {
        logger.info("batchUpdateContribution begin");

        if (CollectionUtils.isEmpty(dataMapList)) {
            return;
        }
        logger.info("batchUpdateContribution size : {}", dataMapList.size());
        List<Map> mapList = new ArrayList<>();
        for (Map map : dataMapList) {
            mapList.add(map);
            if (mapList.size() == BATCH_SIZE_NUM) {
                fundGroupMapper.batchUpdateContribution(mapList);
                mapList.clear();
            }
        }
        if (!CollectionUtils.isEmpty(mapList)) {
            fundGroupMapper.batchUpdateContribution(mapList);
        }

        logger.info("batchUpdateContribution end");
        return;
    }
}
