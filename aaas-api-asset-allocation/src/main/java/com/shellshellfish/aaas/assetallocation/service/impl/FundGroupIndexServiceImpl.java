package com.shellshellfish.aaas.assetallocation.service.impl;

import com.shellshellfish.aaas.assetallocation.entity.FundGroupHistory;
import com.shellshellfish.aaas.assetallocation.entity.FundGroupIndex;
import com.shellshellfish.aaas.assetallocation.entity.Interval;
import com.shellshellfish.aaas.assetallocation.mapper.FundGroupHistoryMapper;
import com.shellshellfish.aaas.assetallocation.mapper.FundGroupIndexMapper;
import com.shellshellfish.aaas.assetallocation.mapper.FundGroupMapper;
import com.shellshellfish.aaas.assetallocation.mapper.FundNetValMapper;
import com.shellshellfish.aaas.assetallocation.service.FundGroupIndexService;
import org.apache.commons.math3.stat.StatUtils;
import org.apache.commons.math3.util.FastMath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Service
public class FundGroupIndexServiceImpl implements FundGroupIndexService {

    private static final Logger logger = LoggerFactory.getLogger(FundGroupHistory.class);

    @Autowired
    FundGroupHistoryMapper fundGroupHistoryMapper;

    @Autowired
    FundGroupIndexMapper fundGroupIndexMapper;

    @Autowired
    FundGroupMapper fundGroupMapper;

    @Autowired
    private FundNetValMapper fundNetValMapper;


    /**
     * 计算组合历史年华收益率和历史年华波动率
     * <p>
     * <p>
     * 1:计算月度收益率 并年化（乘以12）
     * 2：各月月度收益率年华之后算术平均数为年华收益率
     * 3：各月月度年华收益率标准差即为历史波动率
     * <／p>
     */
    @Override
    public void calculateAnnualVolatilityAndAnnualYield(String groupId, String subGroupId, LocalDate startDate, int oemId) {
        logger.info("start to calculate historical annual yield and Historical annual volatility   groupId:{} ," +
                "subGroupId:{}   startDate: {}", groupId, subGroupId, startDate);
//        if (startDate == null) {
//            startDate = FundGroupService.GROUP_START_DATE;
//        }
//        startDate = startDate.isBefore(FundGroupService.GROUP_START_DATE) ? FundGroupService.GROUP_START_DATE : startDate;
        List<Double> values = new LinkedList<>();
//        LocalDate date = LocalDate.of(startDate.getYear(), startDate.getMonth(), 15);
        do {
            Double value = fundGroupHistoryMapper.getLatestNavAdj(groupId, subGroupId, startDate, oemId);
            if (value != null)
                values.add(value);
            startDate = startDate.plusMonths(1);
        } while (startDate.isBefore(LocalDate.now(ZoneId.systemDefault()).plusDays(1)));
//        do {
//            Double value = fundGroupHistoryMapper.getLatestNavAdj(groupId, subGroupId, date, oemId);
//            if (value != null)
//                values.add(value);
//            date = date.plusMonths(1);
//        } while (date.isBefore(LocalDate.now(ZoneId.systemDefault()).plusDays(1)));


        if (CollectionUtils.isEmpty(values))
            return;

        double[] annualYieldArray = new double[values.size() - 1];

        for (int i = 0; i < values.size() - 1; i++) {
            Double pre = values.get(i);
            Double next = values.get(i + 1);
            //月度收益年化
            Double annualYield = ((next - pre) * 12) / pre;
            annualYieldArray[i] = annualYield;
        }

        double historicalAnnualYield = StatUtils.mean(annualYieldArray);
        double historicalAnnualVolatility = FastMath.sqrt(StatUtils.variance(annualYieldArray));
        FundGroupIndex fundGroupIndex = new FundGroupIndex(groupId, subGroupId, historicalAnnualYield, historicalAnnualVolatility);
        fundGroupIndex.setOemId(oemId);
        fundGroupIndexMapper.saveOrUpdate(fundGroupIndex);
        logger.info(" calculate historical annual yield and Historical annual volatility   groupId:{} ,subGroupId:{}", groupId, subGroupId);
    }

    @Override
    public void calculateAnnualVolatilityAndAnnualYield(int oemId) {
        logger.info("start to  calculate historical annual yield and Historical annual volatility   startDate:{}");
        long startTime = System.currentTimeMillis();
//        if (startDate == null) {
//            startDate = FundGroupService.GROUP_START_DATE;
//        }

        //查询组合成立日
//            LocalDate groupStartDate = QueryGroupBuildDate.getInstance().getGroupBuildDate(fundGroupId);
//        Date date = fundNetValMapper.getMinNavlatestDateByFundGroupId(fundGroupId);
//        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
//        System.out.println("local date : "+ localDate);

        List<Interval> list = fundGroupMapper.getAllIdAndSubId(oemId);
        for (Interval interval : list) {
            if (Integer.parseInt(interval.getFund_group_id()) <= 15){
                if (!interval.getId().endsWith("48")){
                    continue;
                }
            }

            //查询组合成立日
//            LocalDate groupStartDate = QueryGroupBuildDate.getInstance().getGroupBuildDate(fundGroupId);
            Date date = fundNetValMapper.getMinNavlatestDateByFundGroupId(interval.getFund_group_id());
            LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            calculateAnnualVolatilityAndAnnualYield(interval.getFund_group_id(), interval.getId()
                ,  localDate, oemId);
        }
        long endTime = System.currentTimeMillis();
        logger.info("finish to calculate historical annual yield and Historical annual volatility   startDate:{}," +
                "costTime:{}ms", endTime - startTime);
    }

}
