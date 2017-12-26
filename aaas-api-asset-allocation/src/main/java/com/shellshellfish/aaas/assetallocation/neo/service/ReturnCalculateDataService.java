package com.shellshellfish.aaas.assetallocation.neo.service;

import com.shellshellfish.aaas.assetallocation.neo.entity.CovarianceModel;
import com.shellshellfish.aaas.assetallocation.neo.mapper.CovarianceMapper;
import com.shellshellfish.aaas.assetallocation.neo.mapper.FundCalculateDataMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.shellshellfish.aaas.assetallocation.neo.util.ConstantUtil.*;


/**
 * Author: yongquan.xiong
 * Date: 2017/11/23
 * Desc:返回基金数据
 */
@Service
public class ReturnCalculateDataService {

    private static final Logger logger= LoggerFactory.getLogger(ReturnCalculateDataService.class);

    @Autowired
    private FundCalculateDataMapper fundCalculateDataMapper;
    @Autowired
    private CovarianceMapper covarianceMapper;


    /*
     * @Desc:返回MVO 方法所需参数
     * @param:
     *        selectDate:净值日期
     *        codeList:基金代码集合
     *        type:数据类型（日周月年）
     */
    public CovarianceModel getMVOParamData(String selectDate, List<String> codeList,String type){
        CovarianceModel covarianceModel=new CovarianceModel();
        Double[] yieldRatio=new Double[codeList.size()];
        String code="";
        String tableName1="";//协方差记录表
        String tableName2="";//方差/风险率（risk_ratio）记录表
        switch (type){
            case TYPE_OF_DAY:
                tableName1="fund_covariance_day";
                tableName2="fund_calculate_data_day";
                break;
            case TYPE_OF_WEEK:
                tableName1="fund_covariance_week";
                tableName2="fund_calculate_data_week";
                break;
            case TYPE_OF_MONTH:
                tableName1="fund_covariance_month";
                tableName2="fund_calculate_data_month";
                break;
            case TYPE_OF_YEAR:
                tableName1="fund_covariance_year";
                tableName2="fund_calculate_data_year";
                break;
            default:
                tableName1="";
                tableName2="";
                break;
        }

        //取出收益率组成 1 X n 矩阵
        for(int i=0;i<codeList.size();i++){
            code=codeList.get(i);
            if(code!=null && !"".equals(code) && !"".equals(tableName2)){
                //根据code & selectDate 查询收益率
                Double yieldRatioVal=fundCalculateDataMapper.findYieldRatio(tableName2,code,selectDate);
                if(yieldRatioVal!=null){
                    yieldRatio[i]=yieldRatioVal;
                }else{
                    covarianceModel.setStatus(NULL_STATUS);//无数据
                    logger.debug("yieldRatioVal 无数据");
                    break;
                }
            }else{
                covarianceModel.setStatus(FAILUED_STATUS);//失败，数据无效
                logger.debug("code or tableName 无效：code："+code +",tableName:"+tableName2);
                break;
            }

        }

        if(covarianceModel.getStatus()==null){
            covarianceModel.setStatus(SUCCEED_STATUS);//成功，数据有效
        }else{
            return covarianceModel; //失败情况下就直接返回，不再继续后续步骤
        }

        covarianceModel.setYieldRatioArr(yieldRatio);

        //取出对应协方差组成 n X n 矩阵
        Double[][] covarianceDoubleArr=new Double[codeList.size()][codeList.size()];
        String codeA="";
        String codeB="";
        List<List<Double>> list=new ArrayList<>();
        //遍历查询数据
        for(int i=0;i<codeList.size();i++){
            List<Double> tempList=new ArrayList();
            codeA=codeList.get(i);
            if(codeA!=null && !"".equals(codeA) && !"".equals(tableName1) && !"".equals(tableName2)){
                for(int j=0;j<codeList.size();j++){
                    codeB=codeList.get(j);
                    if(codeB!=null && !"".equals(codeB)){
                        if(i==j){  //此时 codeA=codeB, 协方差=方差,查询 tableName2
                            Double riskRatio=fundCalculateDataMapper.findRiskRatio(tableName2,codeA,selectDate);
                            if(riskRatio!=null){
                                tempList.add(riskRatio*riskRatio);
                            }else{
                                covarianceModel.setStatus(NULL_STATUS);//无数据
                                logger.debug("riskRatio 无数据：");
                                break;
                            }
                        }else{   //此时 codeA ！=codeB, 协方差 查询 tableName1
                            Double covariance=covarianceMapper.findCovariance(tableName1,codeA,codeB,selectDate);
                            if(covariance!=null){
                                tempList.add(covariance);
                            }else{
                                covarianceModel.setStatus(NULL_STATUS);//无数据
                                logger.debug("covariance 无数据：");
                                break;
                            }
                        }

                    }else{
                        covarianceModel.setStatus(FAILUED_STATUS);//失败，数据无效
                        logger.debug("code 无效：codeB："+codeB);
                        break;
                    }

                }
                list.add(tempList);

            }else{
                covarianceModel.setStatus(FAILUED_STATUS);//失败，数据无效
                logger.debug("code or tableName 无效：codeA："+codeA +",tableName1:"+tableName1 +",tableName2:"+tableName2);
                break;
            }

        }

        if(!SUCCEED_STATUS.equals(covarianceModel.getStatus())){
            return covarianceModel; //失败情况下就直接返回，不再继续后续步骤
        }

        //转为数组矩阵
        covarianceDoubleArr=getDoubleArray(list);

        covarianceModel.setCovarianceArr(covarianceDoubleArr);

        return covarianceModel;
    }


    //list转为二维数组
    public static Double[][] getDoubleArray(List<List<Double>> list){
        Double[][] ps = new Double[list.size()][];
        for (int i = 0; i < list.size(); i++) {
            ps[i] = list.get(i).toArray(new Double[list.get(i).size()]);
        }
        return ps;
    }


}
