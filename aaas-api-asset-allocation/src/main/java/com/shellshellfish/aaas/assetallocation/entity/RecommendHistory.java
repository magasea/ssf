package com.shellshellfish.aaas.assetallocation.entity;

import java.util.Date;

/**
 * Created by wangyinuo on 2017/11/16.
 */
public class RecommendHistory {
    private String id;
    private String cust_id;//客户ID
    private String fund_group_id;//基金组合ID
    private Date recommend_add_time;//推荐时间
    private Date recommend_last_mod_time;//最后一次修改时间

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCust_id() {
        return cust_id;
    }

    public void setCust_id(String cust_id) {
        this.cust_id = cust_id;
    }

    public String getFund_group_id() {
        return fund_group_id;
    }

    public void setFund_group_id(String fund_group_id) {
        this.fund_group_id = fund_group_id;
    }

    public Date getRecommend_add_time() {
        return recommend_add_time;
    }

    public void setRecommend_add_time(Date recommend_add_time) {
        this.recommend_add_time = recommend_add_time;
    }

    public Date getRecommend_last_mod_time() {
        return recommend_last_mod_time;
    }

    public void setRecommend_last_mod_time(Date recommend_last_mod_time) {
        this.recommend_last_mod_time = recommend_last_mod_time;
    }
}
