package com.shellshellfish.aaas.finance.trade.order.service;


import com.shellshellfish.aaas.common.message.order.TrdPayFlow;
import com.shellshellfish.aaas.finance.trade.order.model.dao.TrdBrokerUser;
import com.shellshellfish.aaas.finance.trade.order.model.vo.FinanceProdBuyInfo;
import com.shellshellfish.aaas.finance.trade.order.model.dao.TrdOrder;
import java.util.concurrent.ExecutionException;

public interface TradeOpService {

  TrdOrder buyFinanceProduct(FinanceProdBuyInfo financeProdInfo)
      throws Exception;

  Long getUserId(String uuid) throws ExecutionException, InterruptedException;

  void updateByParam(String tradeApplySerial, Long updateDate, Long updateBy, Long id,
      int orderDetailStatus);

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



}
