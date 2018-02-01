package com.shellshellfish.aaas.assetallocation.neo.service;

import com.shellshellfish.aaas.assetallocation.neo.entity.CovarianceModel;
import com.shellshellfish.aaas.assetallocation.neo.mapper.CovarianceMapper;
import com.shellshellfish.aaas.assetallocation.neo.mapper.FundCalculateDataMapper;
import com.shellshellfish.aaas.assetallocation.neo.mapper.FundNetValMapper;
import com.shellshellfish.aaas.assetallocation.neo.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

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

    private static final Logger logger = LoggerFactory.getLogger(ReturnCalculateDataService.class);

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

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    /*
     * @Desc:返回MVO 方法所需参数
     * @param:
     *        selectDate:净值日期
     *        codeList:基金代码集合
     *        type:数据类型（日周月年）
     */
    public CovarianceModel getMVOParamData(String selectDate, List<String> codeList, String type) {
        CovarianceModel covarianceModel = new CovarianceModel();
        //查询组合中基金最晚成立日 作为 该组合成立日
        Date minDate = fundNetValMapper.getMinNavDateByCodeList(codeList);
        if (minDate == null) {
            covarianceModel.setStatus(FAILUED_STATUS); // 失败，数据无效
            logger.debug("获取 组合中基金最晚成立日 失败 ！");
            return covarianceModel;
        }
        covarianceModel.setNavDate(minDate); //作为组合成立日

        //查询参数（取值数量）
        Integer number = fundCalculateService.getNumberFromSysConfig(type);
        Calendar now = Calendar.getInstance();
        now.add(Calendar.WEEK_OF_YEAR, -number); //现在时间的number 周前
        Date preDate = now.getTime();
        Date tmpStartDate = minDate.compareTo(preDate) > 0 ? minDate : preDate;
        Date startDate = DateUtil.getDateFromFormatStr(DateUtil.formatDate(tmpStartDate));

        String calculateTableName = ""; //方差/风险率（risk_ratio）记录表
        switch (type) {
            case TYPE_OF_DAY:
                calculateTableName = "fund_calculate_data_day";
                break;
            case TYPE_OF_WEEK:
                calculateTableName = "fund_calculate_data_week";
                break;
            case TYPE_OF_MONTH:
                calculateTableName = "fund_calculate_data_month";
                break;
            case TYPE_OF_YEAR:
                calculateTableName = "fund_calculate_data_year";
                break;
            default:
                calculateTableName = "";
                break;
        }

        //取出收益率组成 1 X n 矩阵
        int index = 0;
        Double[] yieldRatio = new Double[codeList.size()];
        List<List<Double>> yieldRatiosList = new ArrayList<>();
        for (String code : codeList) {
            if (StringUtils.isEmpty(code) || StringUtils.isEmpty(calculateTableName) || null == startDate) {
                covarianceModel.setStatus(FAILUED_STATUS); //失败，数据无效
                logger.debug("code or tableName 无效, code: " + code + " ,tableName: " + calculateTableName);
                break;
            }

            //根据code & selectDate 查询收益率
            List<Double> yieldRatioValList = fundCalculateDataMapper.findYieldRatio(calculateTableName, code, sdf.format(startDate));
            if (CollectionUtils.isEmpty(yieldRatioValList)) {
                covarianceModel.setStatus(NULL_STATUS); //无数据
                logger.debug("yieldRatioVal 无数据");
                break;
            }

            //计算几何平均收益率 = (π（1+Ri）)^(1/N) - 1
            Double geoMeanYieldRatio = calculateGeometricMean(yieldRatioValList);
            if (null == geoMeanYieldRatio) {
                covarianceModel.setStatus(NULL_STATUS); //无数据
                logger.debug("yieldRatioVal 无数据");
                break;
            }
            yieldRatio[index++] = geoMeanYieldRatio;
            yieldRatiosList.add(yieldRatioValList);
        }

        if (covarianceModel.getStatus() != null
                && !SUCCEED_STATUS.equalsIgnoreCase(covarianceModel.getStatus())) {
            return covarianceModel; //失败情况下就直接返回，不再继续后续步骤
        }
        covarianceModel.setStatus(SUCCEED_STATUS); //成功，数据有效
        covarianceModel.setYieldRatioArr(yieldRatio);

        if (CollectionUtils.isEmpty(yieldRatiosList)) {
            covarianceModel.setStatus(FAILUED_STATUS); //失败，数据无效
            logger.debug("计算协方差 获取协方差矩阵 失败");
            return covarianceModel; //失败情况下就直接返回，不再继续后续步骤
        }

        List<List<Double>> covsList = new ArrayList<>();
        for (List<Double> yieldRatioListA : yieldRatiosList) {
            if (CollectionUtils.isEmpty(yieldRatioListA) || yieldRatioListA.size() <= 1) {
                continue;
            }

            List<Double> tempCovs = new ArrayList();
            for (List<Double> yieldRatioListB : yieldRatiosList) {
                if (CollectionUtils.isEmpty(yieldRatioListB) || yieldRatioListB.size() <= 1) {
                    continue;
                }

                Double cov = covarianceCalculateService.getCovariance(yieldRatioListA.toArray(new Double[0]), yieldRatioListB.toArray(new Double[0]));
                if (cov != null) {
                    tempCovs.add(cov);
                } else {
                    covarianceModel.setStatus(NULL_STATUS); //无数据
                    logger.debug("covariance 无数据");
                    break;
                }
            }
            covsList.add(tempCovs);
        }

        if (!SUCCEED_STATUS.equals(covarianceModel.getStatus())) {
            return covarianceModel; //失败情况下就直接返回，不再继续后续步骤
        }

        //取出对应协方差组成 n X n 矩阵
        Double[][] covarianceDoubleArr = getDoubleArray(covsList); //转为数组矩阵
        covarianceModel.setCovarianceArr(covarianceDoubleArr);

        return covarianceModel;
    }


    //list转为二维数组
    public static Double[][] getDoubleArray(List<List<Double>> list) {
        Double[][] ps = new Double[list.size()][];
        for (int i = 0; i < list.size(); i++) {
            ps[i] = list.get(i).toArray(new Double[list.get(i).size()]);
        }
        return ps;
    }

    //计算几何平均收益率
    public Double calculateGeometricMean(List<Double> x) {
        int m = x.size();
        double sum = 1;
        for (int i = 0; i < m; i++) { //计算x值的累乘
            if (x.get(i) != null) {
                sum *= (x.get(i) + 1);
            }
        }
        return Math.pow(sum, 1.0/m) - 1; //返回sum的m次方根
    }

}
