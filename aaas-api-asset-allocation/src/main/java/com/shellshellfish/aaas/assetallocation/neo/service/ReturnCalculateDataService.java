package com.shellshellfish.aaas.assetallocation.neo.service;

import com.shellshellfish.aaas.assetallocation.neo.entity.CovarianceModel;
import com.shellshellfish.aaas.assetallocation.neo.mapper.CovarianceMapper;
import com.shellshellfish.aaas.assetallocation.neo.mapper.FundCalculateDataMapper;
import com.shellshellfish.aaas.assetallocation.neo.mapper.FundNetValMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
    @Autowired
    private FundNetValMapper fundNetValMapper;
    @Autowired
    private FundCalculateService fundCalculateService;
    @Autowired
    private CovarianceCalculateService covarianceCalculateService;

    SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");

    /*
     * @Desc:返回MVO 方法所需参数
     * @param:
     *        selectDate:净值日期
     *        codeList:基金代码集合
     *        type:数据类型（日周月年）
     */
    public CovarianceModel getMVOParamData(String selectDate, List<String> codeList,String type){
        CovarianceModel covarianceModel=new CovarianceModel();
        //查询组合中基金最晚成立日 作为 该组合成立日
        Date minDate=fundNetValMapper.getMinNavDateByCodeList(codeList);
        if(minDate==null){
            covarianceModel.setStatus(FAILUED_STATUS);//失败，数据无效
            logger.debug("获取 组合中基金最晚成立日 失败 ！");
            return covarianceModel;
        }
        covarianceModel.setNavDate(minDate);    //作为组合成立日
        Date startDate=null;
        try{
            //查询参数（取值数量）
            Integer number=fundCalculateService.getNumberFromSysConfig(type);

            Calendar now = Calendar.getInstance();
            now.add(Calendar.WEEK_OF_YEAR, -number); //现在时间的number 周前
            Date date=now.getTime();
            startDate=minDate.compareTo(date)>0?minDate:date;
            System.out.print(date);
        }catch(Exception e){
            logger.error("取 组合数据 开始时间 失败");
            e.printStackTrace();
        }

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
            if(code!=null && !"".equals(code) && !"".equals(tableName2) && startDate!=null){
                //根据code & selectDate 查询收益率
//                Double yieldRatioVal=fundCalculateDataMapper.findYieldRatio(tableName2,code,selectDate);
                List<Double> yieldRatioValList=fundCalculateDataMapper.findYieldRatio(tableName2,code,sdf.format(startDate));
                //计算几何平均收益率 = (π（1+Ri）)^(1/N) - 1
                if(yieldRatioValList!=null && yieldRatioValList.size()>0){
                    Double yieldRatioVal=calculateGeometricMean(yieldRatioValList);
                    if(yieldRatioVal!=null){
                        yieldRatio[i]=yieldRatioVal;
                    }else{
                        covarianceModel.setStatus(NULL_STATUS);//无数据
                        logger.debug("yieldRatioVal 无数据");
                        break;
                    }
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
            if(codeA!=null && !"".equals(codeA) ){
                for(int j=0;j<codeList.size();j++){
                    codeB=codeList.get(j);
                    if(codeB!=null && !"".equals(codeB) && startDate!=null){
                        covarianceModel.setCodeA(codeA);
                        covarianceModel.setCodeB(codeB);
                        covarianceModel.setNavDate(startDate);
                        //根据code组合查找基金数据
                        List<CovarianceModel> tempCovarianceModelList=fundNetValMapper.getDataByCodeAndDate(covarianceModel);
                        //计算协方差 组成 矩阵
                        List<Double> yieldRatioArrA=new ArrayList<>();
                        List<Double> yieldRatioArrB=new ArrayList<>();
                        List<Double> listA=new ArrayList<>();
                        List<Double> listB=new ArrayList<>();
                        for(CovarianceModel tempCovarianceModel: tempCovarianceModelList){
                            if(tempCovarianceModel.getNavadjA()!=null && tempCovarianceModel.getNavadjB()!=null){
                                listA.add(tempCovarianceModel.getNavadjA().doubleValue());
                                listB.add(tempCovarianceModel.getNavadjB().doubleValue());

                                if(listA.size()>1){
                                    Double yieldRatioA =fundCalculateService.calculateYieldRatio(listA.get(listA.size()-2),listA.get(listA.size()-1));
                                    yieldRatioArrA.add(yieldRatioA);

                                    Double yieldRatioB =fundCalculateService.calculateYieldRatio(listB.get(listB.size()-2),listB.get(listB.size()-1));
                                    yieldRatioArrB.add(yieldRatioB);

                                }

                            }
                        }
                        Double cov=null;
                        //计算协方差
                        if(yieldRatioArrA.size()>1){
                            cov=covarianceCalculateService.getCovariance(yieldRatioArrA.toArray(new Double[0]),yieldRatioArrB.toArray(new Double[0]));
                        }
                        if(cov!=null){
                            tempList.add(cov);
                        }else{
                            covarianceModel.setStatus(NULL_STATUS);//无数据
                            logger.debug("covariance 无数据：");
                            break;
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
                logger.debug("计算协方差 获取协方差矩阵 失败");
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

    //计算几何平均收益率
    public Double calculateGeometricMean(List<Double> x){

        int m=x.size();
        double sum=1;
        for(int i=0;i<m;i++){//计算x值的累乘

            if(x.get(i)!=null){
                sum*=(x.get(i)+1);
            }

        }
        return Math.pow(sum,1.0/m) -1 ;//返回sum的m次方根

    }


}
