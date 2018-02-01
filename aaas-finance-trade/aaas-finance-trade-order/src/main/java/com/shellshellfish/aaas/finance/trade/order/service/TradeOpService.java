package com.shellshellfish.aaas.finance.trade.order.service;


import com.shellshellfish.aaas.common.grpc.finance.product.ProductMakeUpInfo;
import com.shellshellfish.aaas.common.message.order.TrdPayFlow;
import com.shellshellfish.aaas.finance.trade.order.model.dao.TrdBrokerUser;
import com.shellshellfish.aaas.finance.trade.order.model.vo.FinanceProdBuyInfo;
import com.shellshellfish.aaas.finance.trade.order.model.dao.TrdOrder;
import com.shellshellfish.aaas.userinfo.grpc.UserInfo;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import org.springframework.data.repository.query.Param;

public interface TradeOpService {

  TrdOrder buyFinanceProduct(FinanceProdBuyInfo financeProdInfo)
      throws Exception;

  Long getUserId(String uuid) throws ExecutionException, InterruptedException;

  UserInfo getUserInfoByUserUUID(String uuid) throws ExecutionException, InterruptedException;

  public void updateByParam(String tradeApplySerial,Long fundSum, Long fundSumConfirmed, Long
      fundNum, Long fundNumConfirmed, Long updateDate, Long updateBy, Long id, int orderDetailStatus)
      throws Exception;

  public  void updateByParamWithSerial(String tradeApplySerial,int orderDetailStatus,  Long
      updateDate, Long updateBy,   Long id);

  String getUserUUIDByUserId(Long userId);

  TrdBrokerUser getBrokerUserByUserIdAndBandCard(Long userId, String bankCardNum);

  /**
   * 第一步先购买货币基金，用同步方式调用
   * 在这时候把parent order 生成 对应交易流水生成
   * 返回给用户购买成功与否的结果
   * 第二步
   * 在定时任务里面发现份额确认后，触发正常购买http 但是调用的是中证赎回申购接口
   * @param financeProdInfo
   * @return
   * @throws Exception
   */
  TrdOrder buyFinanceProductWithPreOrder(FinanceProdBuyInfo financeProdInfo)
      throws Exception;

  /**
   * 如果货币基金份额确认， 那么触发正常的购买流程
   *  根据购买的货币基金份额，按照产品中各个基金的比例，换算成各个基金的转换份额
   */
   TrdOrder buyPreOrderProduct(TrdPayFlow trdPayFlow) throws Exception;


   TrdOrder genOrderFromBuyInfoAndProdMakeUpInfo(FinanceProdBuyInfo financeProdBuyInfo,
      List<ProductMakeUpInfo> productMakeUpInfos) throws Exception;

   Map<String, Object> sellorbuyDeatils(String orderId) throws Exception;

}
