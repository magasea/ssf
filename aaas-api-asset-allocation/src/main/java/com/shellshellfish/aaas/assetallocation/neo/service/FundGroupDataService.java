package com.shellshellfish.aaas.assetallocation.neo.service;


import com.shellshellfish.aaas.assetallocation.neo.entity.CovarianceModel;
import com.shellshellfish.aaas.assetallocation.neo.entity.FundCombination;
import com.shellshellfish.aaas.assetallocation.neo.mapper.FundGroupMapper;
import com.shellshellfish.aaas.assetallocation.neo.util.MVO;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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

    private static final Logger logger= LoggerFactory.getLogger(FundGroupDataService.class);

    private SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");

    public void insertFundData(){

       //查询 fund_group_basic （基金组合基本表）中有效组合的code
        List<FundCombination> groupIdList=fundGroupMapper.findAllGroupId();

        HashMap<Integer,List<String>> groupCodeMap=new HashMap<>();

        for(int i=0;i<groupIdList.size();i++){
            //根据groupId 分组
            Integer groupId=groupIdList.get(i).getGroupId();
            if(groupId!=null){
                List<String> list=groupCodeMap.get(groupId);
                if(list==null){
                    List<String> tempList=new ArrayList<>();
                    tempList.add(groupIdList.get(i).getCode());
                    groupCodeMap.put(groupId,tempList);
                }else{
                    list.add(groupIdList.get(i).getCode());
                    groupCodeMap.put(groupId,list);
                }
            }
        }


        //将 fund_group_details 中 数据 备份到 fund_group_details_history
        Integer tag1=fundGroupMapper.transIntoFundGroupDetailsHistory();

        //将 fund_group_sub 中 数据 备份到 fund_group_sub_history
        Integer tag2=fundGroupMapper.transIntoFundGroupSubHistory();

        //将 fund_group_details 中 数据 删除
        Integer tag3=fundGroupMapper.deleteFundGroupDetails();

        //将 fund_group_sub 中 数据 删除
        Integer tag4=fundGroupMapper.deleteFundGroupSub();

        if(tag1>=0 && tag2>=0 && tag3>=0 && tag4>=0){
            //查询时间
            String todayDate=sdf.format(new Date());
            //取出code
            if(groupCodeMap!=null && groupCodeMap.size()>0) {

                Iterator<Map.Entry<Integer, List<String>>> entries = groupCodeMap.entrySet().iterator();

                while (entries.hasNext()) {

                    Map.Entry<Integer, List<String>> entry = entries.next();
                    Integer groupId = entry.getKey();//组合id
                    List<String> codeList = entry.getValue();//组合中所含基金代码
                    //计算组合数据并入库
                    insertFundGroupData(groupId,codeList,todayDate);


                }

            }
        }

    }


    //调用 MVO 得出组合数据并入库
    public void insertFundGroupData( Integer groupId,List<String> codeList,String todayDate ){

        Double [] ExpReturn=null;
        Double[][] ExpCovariance=null;

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        CovarianceModel covarianceModel=returnCalculateDataService.getMVOParamData(todayDate,codeList,TYPE_OF_WEEK);

        while(!SUCCEED_STATUS.equals(covarianceModel.getStatus())){
            calendar.add(Calendar.DATE,-1);//若无合理数据 则 往前递推
            todayDate=sdf.format(calendar.getTime());
            covarianceModel=returnCalculateDataService.getMVOParamData(todayDate,codeList,TYPE_OF_WEEK);
            //若无查询基本数据则跳出循环
            if(NULL_STATUS.equals(covarianceModel.getStatus())){
                break;
            }
        }

        if(SUCCEED_STATUS.equals(covarianceModel.getStatus())){//成功，数据有效)
            ExpReturn=covarianceModel.getYieldRatioArr();
            ExpCovariance=covarianceModel.getCovarianceArr();
            logger.debug("MVO方法所需参数矩阵查询成功！");
        }else{
            logger.error("MVO方法所需参数矩阵查询失败！");
            return;
        }

        //调用 MVO 获取 组合收益、风险、权重
        List<float [][]> result= MVO.efficientFrontier(ExpReturn,ExpCovariance,SUB_GROUP_COUNT);

        if(result!=null && result.size()==3){
            float[][] riskArr=result.get(0);//子组合风险数组
            float[][] yieldArr=result.get(1);//子组合收益数组
            float[][] weightArr=result.get(2);//子组合权重数组
            int codeNum=weightArr.length;//基金个数

            List<FundCombination> fundCombinationList=new ArrayList<>();

                // 取出子组合
                for(int i=0;i<result.get(0).length;i++){

                    //risk
                    float riskVal=riskArr[i][0];

                    //yield
                    float yieldVal=yieldArr[i][0];

                    //取对应基金的权重
                    List<FundCombination> subGroupDetails=new ArrayList<>();

                    for(int j=0;j<codeNum;j++){
                        FundCombination fundGroupDetails=new FundCombination();
                        fundGroupDetails.setGroupId(groupId);//组合Id
                        fundGroupDetails.setSubGroupId(((Integer)(groupId*100)).toString()+((Integer)i).toString());//子组合Id
                        fundGroupDetails.setCode(codeList.get(j));//基金代码
                        fundGroupDetails.setProportion(weightArr[j][i]);//权重
                        subGroupDetails.add(fundGroupDetails);//
                    }
                    FundCombination fundCombination=new FundCombination();
                    fundCombination.setGroupId(groupId);//组合Id
                    fundCombination.setSubGroupId(((Integer)(groupId*100)).toString()+((Integer)i).toString());//子组合Id
                    fundCombination.setSubGroupRisk(riskVal*52);//子组合风险

                    fundCombination.setExpectedMaxRetracement(riskVal*(-52));//预期最大回撤
                    fundCombination.setSimulateHistoricalVolatility(riskVal*52);//模拟历史年化波动率

                    fundCombination.setSubGroupYield(yieldVal*52);//子组合收益

                    fundCombination.setExpectedAnnualizedReturn(yieldVal*52);//预期年化收益
                    fundCombination.setSimulateHistoricalYearPerformance(yieldVal*52);//模拟历史年化业绩


                    fundCombination.setSubGroupDetails(subGroupDetails);//子组合详情

                    fundCombinationList.add(fundCombination);
                    //将组合数据插入fund_group_details
                    fundGroupMapper.insertIntoFundGroupDetails(fundCombination.getSubGroupDetails());

                }

                //将组合数据插入fund_group_sub
                fundGroupMapper.insertIntoFundGroupSub(fundCombinationList);
            }


        }






}