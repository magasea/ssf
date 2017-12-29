package com.shellshellfish.aaas.assetallocation.neo.service;


import com.shellshellfish.aaas.assetallocation.neo.entity.Dailyfunds;
import com.shellshellfish.aaas.assetallocation.neo.mapper.FundGroupMapper;
import com.shellshellfish.aaas.assetallocation.neo.mapper.FundNetValMapper;
import com.shellshellfish.aaas.assetallocation.service.FundInfoService;
import com.shellshellfish.aaas.common.utils.SSFDateUtils;
import com.shellshellfish.aaas.datacollect.DailyFunds;
import com.shellshellfish.aaas.datacollect.DailyFundsQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Author: yongquan.xiong
 * Date: 2017/12/20
 * Desc:获取每日基金数据并入库
 */
@Service
public class DailyFundService {
    @Autowired
    private FundGroupMapper fundGroupMapper;
    @Autowired
    private FundNetValMapper fundNetValMapper;
    @Autowired
    FundInfoService fundInfoService;

    private static final Logger logger= LoggerFactory.getLogger(DailyFundService.class);

    SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");

    private List<String> benchmarkCode;

    /*
     * 获取基金每日数据并insert into：fund_net_value 以及 fund_basic
     *
     */
    public Boolean insertDailyFund(){
        Boolean flag=true;
        //查询 fund_group_basic ，获取需要调用每日接口抓取数据的 code
        List<String> codeList=fundGroupMapper.findAllGroupCode();

        //查询 fund_group_basic ，获取 需要收盘价的 code
        benchmarkCode=fundGroupMapper.findBenchmarkCode();

        for(int i=0;i<codeList.size();i++){
            String code=codeList.get(i);
            //根据 code 查询fund_net_val 中已有数据的最近净值日期
            Date maxDate=fundNetValMapper.getMaxNavDateByCode(code);
            String maxDateStr="";
            if(maxDate!=null){
                try {
                    maxDateStr=sdf.format(maxDate);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else {
                maxDateStr="2010-01-01";
            }

            String todayDate=sdf.format(new Date());

            //rpc 调用获取每日数据
             flag=insertDailyData(code,maxDateStr,todayDate);
             if(!flag){
                 break;
             }
        }

        return flag;
    }


    /*
     * 调用每日接口获取数据并入库
     */
    public Boolean insertDailyData(String code,String startDate,String endDate){
        Boolean flag=true; //判断方法是否执行成功(默认 否)
        List<DailyFunds> dailyFundsList=new ArrayList<>();
        try{
            DailyFundsQuery.Builder builder = DailyFundsQuery.newBuilder();
            builder.setNavLatestDateStart(startDate);
            builder.setNavLatestDateEnd(endDate);
            builder.addCodes(code);
            dailyFundsList = fundInfoService.getDailyFunds(builder.build());

        }catch(Exception e){
            logger.error("调用每日接口获取数据失败：code="+code+",startDate="+startDate+",todayDate="+endDate,e);
        }

        if(dailyFundsList!=null && dailyFundsList.size()>0){

            //部分数据插入 fund_basic
            try{
                //先判断是否已经有该 code 的基本数据
                String basicCode=fundNetValMapper.findBasicDataByCode(code);
                if(!code.equals(basicCode)){
                    Dailyfunds dailyfunds=new Dailyfunds();
                    dailyfunds.setCode(dailyFundsList.get(0).getCode());//基金代码
                    dailyfunds.setFname(dailyFundsList.get(0).getFname());//基金简称
                    dailyfunds.setFundTypeOne(dailyFundsList.get(dailyFundsList.size()-1).getFirstInvestType());//一级分类
                    dailyfunds.setFundTypeTwo(dailyFundsList.get(dailyFundsList.size()-1).getSecondInvestType());//二级分类
//                    dailyfunds.setFundScale(dailyFundsList.get(dailyFundsList.size()-1).getFundScale());//基金规模
//                    dailyfunds.setBmIndexChgPct(dailyFundsList.get(dailyFundsList.size()-1).getBmIndexChgPct());//标的指数涨跌幅

                    fundNetValMapper.insertBasicDataToFundBasic(dailyfunds);
                    logger.debug("Succeed: Insert into fund_basic by call getFundDataOfDay!");
                }

            }catch(Exception e){
                logger.error("Failed: Insert into fund_basic by call getFundDataOfDay!");
            }

            List<Dailyfunds> dailyFundsDetailList=new ArrayList<>();

            //判断 是否 取用 收盘价
            if(benchmarkCode!=null && benchmarkCode.contains(code)){

                for(DailyFunds dailyFunds: dailyFundsList){
                    Dailyfunds dailyfunds=new Dailyfunds();

                    //每日数据日期格式转换
                    try {
                        dailyfunds.setNavLatestDate(sdf.parse(SSFDateUtils.getDateStrFromLong(dailyFunds.getNavLatestDate())));
                    } catch (ParseException e) {
                        logger.error("日期数据转换失败");
                        e.printStackTrace();
                    }

                    dailyfunds.setCode(dailyFunds.getCode());
                    dailyfunds.setNavUnit(Double.parseDouble(dailyFunds.getClose()));//将 收盘价 存入 复权单位净值
                    dailyfunds.setNavAccum(dailyFunds.getNavaccum());
                    dailyfunds.setNavAdj(dailyFunds.getNavadj());
//                    dailyfunds.setYieldOf7Days(dailyFunds.getYieldOf7Days());
//                    dailyfunds.setMillionRevenue(dailyFunds.getMillionRevenue());

                    dailyFundsDetailList.add(dailyfunds);
                }

            }else{

                for(DailyFunds dailyFunds: dailyFundsList){
                    Dailyfunds dailyfunds=new Dailyfunds();

                    //每日数据日期格式转换
                    try {
                        dailyfunds.setNavLatestDate(sdf.parse(SSFDateUtils.getDateStrFromLong(dailyFunds.getNavLatestDate())));
                    } catch (ParseException e) {
                        logger.error("日期数据转换失败");
                        e.printStackTrace();
                    }

                    dailyfunds.setCode(dailyFunds.getCode());
                    dailyfunds.setNavUnit(dailyFunds.getNavunit());
                    dailyfunds.setNavAccum(dailyFunds.getNavaccum());
                    dailyfunds.setNavAdj(dailyFunds.getNavadj());
//                    dailyfunds.setYieldOf7Days(dailyFunds.getYieldOf7Days());
//                    dailyfunds.setMillionRevenue(dailyFunds.getMillionRevenue());

                    dailyFundsDetailList.add(dailyfunds);

                }
            }

            if(dailyFundsDetailList!=null && dailyFundsDetailList.size()>0){
                //数据插入 fund_net_val
                try{
                    Integer tag=fundNetValMapper.insertDailyDataToFundNetVal(dailyFundsDetailList);
                    if(tag==null){
                        flag=false;
                    }
                    logger.debug("Succeed: Insert into fund_net_val by call getFundDataOfDay!");
                }catch(Exception e){
                    logger.error("Failed: Insert into fund_net_val by call getFundDataOfDay!",e);
                }
            }

        }

        return flag;

    }


}
