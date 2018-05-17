package com.shellshellfish.aaas.userinfo.model.dao;

import com.shellshellfish.aaas.finance.trade.order.OrderDetail;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.Id;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * The persistent class for the user_fund_quantity_log database table.
 */
@Document(collection = "user_fund_quantity_log")
public class MongoUserFundQuantityLog implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Indexed(direction = IndexDirection.DESCENDING, unique = true)
    @Field(value = "user_prod_id")
    private Long userProdId;

    @Field(value = "create_Time")
    private Long createTime;

    @Indexed(direction = IndexDirection.DESCENDING)
    @Field(value = "update_time")
    private Long updateTime;


    @Field(value = "order_details")
    private List<OrderDetail> orderDetails;

    @Field(value = "product_status")
    private Map productStatusMap;

    @Field(value = "fund_quantity")
    private Map fundQuantityMap;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getUserProdId() {
        return userProdId;
    }

    public void setUserProdId(Long userProdId) {
        this.userProdId = userProdId;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    public List<OrderDetail> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<OrderDetail> orderDetails) {
        this.orderDetails = orderDetails;
    }

    public Map getProductStatusMap() {
        return productStatusMap;
    }

    public void setProductStatusMap(Map productStatusMap) {
        this.productStatusMap = productStatusMap;
    }

    public Map getFundQuantityMap() {
        return fundQuantityMap;
    }

    public void setFundQuantityMap(Map fundQuantityMap) {
        this.fundQuantityMap = fundQuantityMap;
    }
}