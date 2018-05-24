package com.shellshellfish.aaas.assetallocation.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * Author: Derek
 * Date: 2018/5/21
 * Desc:
 */
@Getter
@Setter
@ToString
public class FundGroupSub {

    private Integer id;//分组Id
    private String fund_group_id;//基金组合ID
    private Double risk_num;//风险率
    private Double income_num;//收益率
    private Float expected_annualized_return;//预期年化收益
    private Float expected_max_retracement;//预期最大回撤
    private Float simulate_historical_volatility;//模拟历史年化波动率
    private Float simulate_historical_year_performance;//模拟历史年化业绩
    private Float confidence_interval;//置信区间
    private Float maximum_losses;//最大亏损额
    private Float sharpe_ratio;//夏普比率
    private Date create_time;//创建时间
    private Date interval_last_mod_time;//最后一次修改时间

}
