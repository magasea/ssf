package com.shellshellfish.aaas.assetallocation.neo.service;


import com.shellshellfish.aaas.assetallocation.neo.entity.CovarianceModel;
import com.shellshellfish.aaas.assetallocation.neo.entity.FundCombination;
import com.shellshellfish.aaas.assetallocation.neo.mapper.FundGroupMapper;
import com.shellshellfish.aaas.assetallocation.neo.util.MVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.SimpleDateFormat;
import java.util.*;

import static com.shellshellfish.aaas.assetallocation.neo.util.ConstantUtil.*;


/**
 * Author: yongquan.xiong
 * Date: 2017/12/21
 * Desc:获取组合收益、风险、权重等数据并入库
 */
@Service
public class FundGroupDataService {

    @Autowired
    private FundGroupMapper fundGroupMapper;

    @Autowired
    private ReturnCalculateDataService returnCalculateDataService;

    private static final Logger logger = LoggerFactory.getLogger(FundGroupDataService.class);

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    public Boolean insertFundGroupData() {
        Boolean doSuccess = true;

        HashMap<Integer, List<String>> groupCodeMap = new HashMap<>();
        //查询 fund_group_basic （基金组合基本表）中有效组合的code
        List<FundCombination> groupIdList = fundGroupMapper.findAllGroupId();
        for (FundCombination fundCombination : groupIdList) {
            // 根据groupId 分组
            Integer groupId = fundCombination.getGroupId();
            if (null == groupId) {
                continue;
            }

            List<String> codes = groupCodeMap.get(groupId);
            if (codes == null) {
                List<String> tmpCodes = new ArrayList<>();
                tmpCodes.add(fundCombination.getCode());
                groupCodeMap.put(groupId, tmpCodes);
            } else {
                codes.add(fundCombination.getCode());
                groupCodeMap.put(groupId, codes);
            }
        }

        // 将 fund_group_details 中 数据 备份到 fund_group_details_history
        Integer transToDetailsEffectRows = fundGroupMapper.transIntoFundGroupDetailsHistory();
        // 将 fund_group_sub 中 数据 备份到 fund_group_sub_history
        Integer transToSubEffectRows = fundGroupMapper.transIntoFundGroupSubHistory();
        // 将 fund_group_details 中 数据 删除
        Integer deleteDetailsEffectRows = fundGroupMapper.deleteFundGroupDetails();
        // 将 fund_group_sub 中 数据 删除
        Integer deleteSubEffectRows = fundGroupMapper.deleteFundGroupSub();
        if (transToDetailsEffectRows >= 0 && transToSubEffectRows >= 0 && deleteDetailsEffectRows >= 0 && deleteSubEffectRows >= 0) {
            //查询时间
            String todayDate = sdf.format(new Date());
            //取出code
            if (!CollectionUtils.isEmpty(groupCodeMap)) {
                Iterator<Map.Entry<Integer, List<String>>> entries = groupCodeMap.entrySet().iterator();
                while (entries.hasNext()) {
                    Map.Entry<Integer, List<String>> entry = entries.next();
                    Integer groupId = entry.getKey(); // 组合id
                    List<String> codeList = entry.getValue(); // 组合中所含基金代码
                    //计算组合数据并入库
                    try {
                        doSuccess = this.insertFundGroupDatas(groupId, codeList, todayDate);
                        if (!doSuccess) {
                            return doSuccess;
                        }
                    } catch (Exception e) {
                        logger.error("计算组合数据失败:", e);
                    }
                }
            }
        }

        return doSuccess;
    }


    // 调用 MVO 得出组合数据并入库
    public Boolean insertFundGroupDatas(Integer groupId, List<String> codeList, String todayDate) {
        Boolean doSuccess = true;
        Double [] expReturn = null;
        Double[][] expCovariance = null;
        Date combinationDate = null;

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        CovarianceModel covarianceModel = returnCalculateDataService.getMVOParamData(todayDate, codeList, TYPE_OF_WEEK);
        while (!SUCCEED_STATUS.equals(covarianceModel.getStatus())) {
            calendar.add(Calendar.DATE, -1); // 若无合理数据 则 往前递推
            todayDate = sdf.format(calendar.getTime());
            covarianceModel = returnCalculateDataService.getMVOParamData(todayDate, codeList, TYPE_OF_WEEK);
            // 若无查询基本数据则跳出循环
            if (NULL_STATUS.equals(covarianceModel.getStatus())) {
                break;
            }
        }

        if (SUCCEED_STATUS.equals(covarianceModel.getStatus())) { // 成功，数据有效
            expReturn = covarianceModel.getYieldRatioArr();
            expCovariance = covarianceModel.getCovarianceArr();
            combinationDate = covarianceModel.getNavDate();
            logger.debug("MVO方法所需参数矩阵查询成功！");
        } else {
            logger.error("MVO方法所需参数矩阵查询失败！");
            return false;
        }

        //调用 MVO 获取 组合收益、风险、权重
        List<float [][]> result = MVO.efficientFrontier(expReturn, expCovariance, SUB_GROUP_COUNT, LOW_BOUND, UP_BOUND);
        if (result != null && result.size() == 3) {
            float[][] riskArr = result.get(0); //子组合风险数组
            float[][] yieldArr = result.get(1); //子组合收益数组
            float[][] weightArr = result.get(2); //子组合权重数组
            int codeNum = weightArr.length; //基金个数

            List<FundCombination> fundCombinationList = new ArrayList<>();
            // 取出子组合
            for (int i = 0; i < result.get(0).length; i++) {
                //risk
                float riskVal = riskArr[i][0];
                //yield
                float yieldVal = yieldArr[i][0];

                //取对应基金的权重
                List<FundCombination> subGroupDetails = new ArrayList<>();
                for (int j = 0; j < codeNum; j++) {
                    FundCombination fundGroupDetails = new FundCombination();
                    fundGroupDetails.setGroupId(groupId); //组合Id
                    fundGroupDetails.setSubGroupId(((Integer)(groupId * 100)).toString() + ((Integer)i).toString()); //子组合Id
                    fundGroupDetails.setCode(codeList.get(j)); //基金代码
                    fundGroupDetails.setProportion(weightArr[j][i]); //权重
                    fundGroupDetails.setCreateDate(new Date()); //数据产生时间
                    subGroupDetails.add(fundGroupDetails); //
                }
                FundCombination fundCombination = new FundCombination();
                fundCombination.setGroupId(groupId); //组合Id
                fundCombination.setSubGroupId(((Integer)(groupId * 100)).toString() + ((Integer)i).toString()); //子组合Id
                fundCombination.setSubGroupRisk((float)(riskVal * Math.sqrt(52))); //子组合风险
                fundCombination.setSimulateHistoricalVolatility((float)(riskVal * Math.sqrt(52))); //模拟历史年化波动率
                fundCombination.setSubGroupYield(yieldVal * 52); //子组合收益
                fundCombination.setExpectedAnnualizedReturn(yieldVal * 52); //预期年化收益
                fundCombination.setSimulateHistoricalYearPerformance(yieldVal * 52); //模拟历史年化业绩
                fundCombination.setCreateDate(new Date()); //数据产生时间
                fundCombination.setCombinationDate(combinationDate); //组合成立日
                fundCombination.setSubGroupDetails(subGroupDetails); //子组合详情
                fundCombinationList.add(fundCombination);
                //将组合数据插入fund_group_details
                Integer effectRows = fundGroupMapper.insertIntoFundGroupDetails(fundCombination.getSubGroupDetails());
                if (effectRows == null) {
                    doSuccess = false;
                    return doSuccess;
                }
            }

            // 将组合数据插入fund_group_sub
            Integer effectRows = fundGroupMapper.insertIntoFundGroupSub(fundCombinationList);
            if (effectRows == null) {
                doSuccess = false;
            }
        }

        return doSuccess;
    }

}
