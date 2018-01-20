package com.shellshellfish.aaas.assetallocation.neo.util;

/**
 * Author: yongquan.xiong
 * Date: 2017/11/18
 * Desc:定义常量
 */
public interface ConstantUtil {
    String TYPE_OF_DAY = "Day";
    String TYPE_OF_WEEK = "Week";
    String TYPE_OF_MONTH = "Month";
    String TYPE_OF_YEAR = "Year";

    String WEEKDAY_OF_FRI = "Fri";

    String START_QUERY_DATE = "1980-01-01";
    String CALCULATE_DATA_OF_DAY = "calculateDataOfDay";
    String CALCULATE_DATA_OF_WEEK = "calculateDataOfWeek";
    String CALCULATE_DATA_OF_MONTH = "calculateDataOfMonth";
    String CALCULATE_DATA_OF_YEAR = "calculateDataOfYear";

    String FUND_CALCULATE_JOB = "fundCalculateJob";

    String CALCULATE_COVARIANCE_OF_DAY = "calculateCovarianceOfDay";
    String CALCULATE_COVARIANCE_OF_WEEK = "calculateCovarianceOfWeek";
    String CALCULATE_COVARIANCE_OF_MONTH = "calculateCovarianceOfMonth";
    String CALCULATE_COVARIANCE_OF_YEAR = "calculateCovarianceOfYear";

    String SUCCEED_STATUS = "succeed";
    String FAILUED_STATUS = "failued";
    String NULL_STATUS = "nullVal";

    String ADJUSTED_FACTOR_TRIGGER = "adjustedFactorTrigger";

    Integer SUB_GROUP_COUNT = 100; // 调用 MVO 输出点的个数
    Double LOW_BOUND = 0.05; // 调用 MVO 权重 下限
    Double UP_BOUND = 0.95; // 调用 MVO 权重 上限
    String JOB_SCHEDULE_NAME = "timedTask";
    String INSERT_DAILYFUND_JOBSCHEDULE = "insertDailyFundJobSchedule";
    String CALCULATE_YIELDANDRISKOFWEEK_JOBSCHEDULE = "calculateYieldAndRiskOfWeekJobSchedule";
    String CALCULATE_COVARIANCEOFWEEK_JOBSCHEDULE = "calculateCovarianceOfWeekJobSchedule";

    String INSERT_FUNDGROUPDATA_JOBSCHEDULE = "insertFundGroupDataJobSchedule";
    String GET_ALLIDANDSUBID_JOBSCHEDULE = "getAllIdAndSubIdJobSchedule";
    String UPDATE_ALLMAXIMUMLOSSES_JOBSCHEDULE = "updateAllMaximumLossesJobSchedule";

    Integer SUCCESSFUL_STATUS = 1;
    Integer FAILURED_STATUS = -1;

    String LATEST_START_DATE_STR_FOR_DAILY_DATA = "2010-01-01"; // 最近净值日期
    int DEFAULT_TYPE_DAY_NUMBER = 20; // 默认周期类型时, 计算风险率时取值数量
    int FUND_GROUP_COUNT = 15; // 基金组合数目
    int RISK_LEVEL_COUNT = 5; // 风险偏好等级数目

}
