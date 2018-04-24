package com.shellshellfish.aaas.assetallocation.repository.dummy;

import com.shellshellfish.aaas.assetallocation.entity.FundNetVal;
import com.shellshellfish.aaas.assetallocation.entity.Interval;
import com.shellshellfish.aaas.assetallocation.mapper.FundGroupMapper;
import com.shellshellfish.aaas.assetallocation.mapper.FundNetValMapper;
import com.shellshellfish.aaas.assetallocation.service.impl.FundGroupService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.*;

/**
 * Author: hongming
 * Date: 2018/1/27
 * Desc:
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ActiveProfiles("dev")
public class FundGroupMapperTest {

    @Autowired
    private FundGroupMapper fundGroupMapper;
    @Autowired
    private FundGroupService fundGroupService;
    @Autowired
    private FundNetValMapper fundNetValMapper;

    @Test
    public void getFundGroupHistoryTimeTest() {
        Map<String, Object> query = new HashMap<>();
        query.put("fund_group_id", 5);
        query.put("subGroupId", 50059);
        String groupStartTime = fundGroupMapper.getFundGroupHistoryTime(query);
        System.out.println(groupStartTime);
    }

    @Test
    public void getFundGroupHistoryTimeByRiskLevelTest() {
        Map<String, Object> query = new HashMap<>();
        query.put("risk_level", "C1");
        String groupStartTime = fundGroupMapper.getFundGroupHistoryTimeByRiskLevel(query);
        System.out.println(groupStartTime);
    }

    @Test
    public void updateMaximumRetracementTest() {
        Map<String, Object> query = new HashMap<>();
        query.put("fund_group_id", "5");
        query.put("subGroupId", "50059");
        query.put("retracement", -0.014); //-0.014
        query.put("time", "2017-12-18");
        query.put("oemId", 1);
        Integer effectRow = fundGroupMapper.updateMaximumRetracement(query);
        System.out.println(effectRow);
    }

    @Test
    public void updateMaximumRetracementByRiskLevelTest() {
        Map<String, Object> query = new HashMap<>();
        query.put("retracement", -0.0397); //-0.0397
        query.put("risk_level", "C1");
        query.put("time", "2018-01-15");
        Integer effectRow = fundGroupMapper.updateMaximumRetracementByRiskLevel(query);
        System.out.println(effectRow);
    }

    @Test
    public void getProportionGroupByFundTypeTwoTest() {
        Map<String, Object> query = new HashMap<>();
        query.put("groupId", "2");
        query.put("subGroupId", "20059");
        List<Interval> intervals = fundGroupMapper.getProportionGroupByFundTypeTwo(query);
        System.out.println(intervals.size());
    }

    @Test
    public void getFundGroupNameByIdTest() {
        String fundGroupName = fundGroupMapper.getFundGroupNameById("2",1);
        System.out.println(fundGroupName);
    }

    @Test
    public void getNavadjTest() {
        Map<String, Object> query = new HashMap<>();
        query.put("fund_group_id", "2");
        query.put("subGroupId", "20048");
        query.put("oemId", 1);
        String groupStartTime = fundGroupMapper.getFundGroupHistoryTime(query);
        if (StringUtils.isEmpty(groupStartTime)) {
            groupStartTime = fundGroupMapper.getGroupStartTime(query);
        }
        query.put("startTime", groupStartTime);
        List<FundNetVal> list = fundGroupMapper.getNavadj(query);
        System.out.println("");
    }

    @Test
    public void getNavlatestdateCountTest() {
        String group_id = "9";
        String subGroupId = "90048";
        List<String> codeList = fundGroupService.getFundGroupCodes(group_id, subGroupId,1);
        int codeSize = codeList.size();
        Map query = new HashMap();
        query.put("list", codeList);
        //查询组合中基金最晚成立日 作为 该组合成立日
        Date minNavDate = fundNetValMapper.getMinNavDateByCodeList(codeList);
        query.put("minNavDate", minNavDate);
        List<Map> resultMap = fundGroupMapper.getNavlatestdateCount(query);

        List<Date> navDateList = new ArrayList<>();
        List<Date> otherNavDateList = new ArrayList<>();
        for (Map map : resultMap) {
            int count = ((Long)map.get("count")).intValue();
            Date navDate = (Date)map.get("navDate");
            if (count != codeSize) {
                otherNavDateList.add(navDate);
                continue;
            }

            navDateList.add(navDate);
        }

        if (!CollectionUtils.isEmpty(navDateList)) {
            System.out.println(navDateList.size());
        }

        if (!CollectionUtils.isEmpty(otherNavDateList)) {
            System.out.println(otherNavDateList.size());
        }

    }

    @Test
    public void getNavadjByNavDatesTest() {
        String groupId = "9";
        String subGroupId = "90048";

        List<LocalDate> navDateList = fundGroupService.getNavlatestdateCount(groupId, subGroupId,1);

        Map query = new HashMap();
        query.put("groupId", groupId);
        query.put("subGroupId", subGroupId);
        query.put("list", navDateList);
        List<FundNetVal> fundNetVals = fundGroupMapper.getNavadjByNavDates(query);

        Assert.assertNotNull("数据为空",fundNetVals);
    }

}
