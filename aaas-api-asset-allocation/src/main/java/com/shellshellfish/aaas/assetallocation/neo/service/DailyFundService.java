package com.shellshellfish.aaas.assetallocation.neo.service;


import com.shellshellfish.aaas.assetallocation.neo.entity.Dailyfunds;
import com.shellshellfish.aaas.assetallocation.neo.mapper.FundGroupMapper;
import com.shellshellfish.aaas.assetallocation.neo.mapper.FundNetValMapper;
import org.junit.Test;
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

    private static final Logger logger= LoggerFactory.getLogger(DailyFundService.class);

    SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");

    /*
     * 获取基金每日数据并insert into：fund_net_value 以及 fund_basic
     *
     */
    @Test
    public void  insertDailyFund(){


        //查询 fund_group_basic ，获取需要调用每日接口抓取数据的 code
        List<String> codeList=fundGroupMapper.findAllGroupCode();

        //查询 fund_group_basic ，获取 需要收盘价的 code
        List<String> benchmarkCode=fundGroupMapper.findBenchmarkCode();

        for(int i=0;i<codeList.size();i++){
            String code=codeList.get(i);
            //根据 code 查询fund_net_val 中已有数据的最近净值日期

            Date maxDate=fundNetValMapper.getMaxNavDateByCode(code);
            if(maxDate==null){
                try {
                    maxDate=sdf.parse("1970-01-01");
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            Date todayDate=new Date();


            //rpc 调用获取每日数据
            List<Dailyfunds> dailyfundsList=new ArrayList<>();
            try{
//                List<Dailyfunds> dailyfundsList=getFundDataOfDay(code,maxDate,todayDate);
            }catch(Exception e){
                logger.error("调用每日接口获取数据失败：code="+code+",startDate="+maxDate+",todayDate="+todayDate);
            }


            if(dailyfundsList!=null && dailyfundsList.size()>0){

                //部分数据插入 fund_basic
                try{
                    //先判断是否已经有该 code 的基本数据
                    String basicCode=fundNetValMapper.findBasicDataByCode(code);
                    if(!code.equals(basicCode)){
                        Dailyfunds dailyfunds=new Dailyfunds();
                        dailyfunds.setCode(dailyfundsList.get(0).getCode());//基金代码
                        dailyfunds.setFname(dailyfundsList.get(0).getFname());//基金简称
                        dailyfunds.setFundTypeOne(dailyfundsList.get(dailyfundsList.size()-1).getFundTypeOne());//一级分类
                        dailyfunds.setFundTypeTwo(dailyfundsList.get(dailyfundsList.size()-1).getFundTypeTwo());//二级分类
                        dailyfunds.setFundScale(dailyfundsList.get(dailyfundsList.size()-1).getFundScale());//基金规模
                        dailyfunds.setBmIndexChgPct(dailyfundsList.get(dailyfundsList.size()-1).getBmIndexChgPct());//标的指数涨跌幅

                        fundNetValMapper.insertBasicDataToFundBasic(dailyfunds);
                        logger.debug("Succeed: Insert into fund_basic by call getFundDataOfDay!");
                    }

                }catch(Exception e){
                    logger.error("Failed: Insert into fund_basic by call getFundDataOfDay!");
                }


                //判断 是否 取用 收盘价
                if(benchmarkCode.contains(code)){

                    //数据插入 fund_net_val()
                    try{
                        fundNetValMapper.insertBenchmarkDailyDataToFundNetVal(dailyfundsList);
                        logger.debug("Succeed: Insert into fund_net_val by call getFundDataOfDay!");
                    }catch(Exception e){
                        logger.error("Failed: Insert into fund_net_val by call getFundDataOfDay!");
                    }
                }else{

                    //数据插入 fund_net_val
                    try{
                        fundNetValMapper.insertDailyDataToFundNetVal(dailyfundsList);
                        logger.debug("Succeed: Insert into fund_net_val by call getFundDataOfDay!");
                    }catch(Exception e){
                        logger.error("Failed: Insert into fund_net_val by call getFundDataOfDay!");
                    }

                }


            }

        }


    }
}
