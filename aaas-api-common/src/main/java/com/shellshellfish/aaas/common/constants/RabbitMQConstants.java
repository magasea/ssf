package com.shellshellfish.aaas.common.constants;

/**
 * Created by chenwei on 2017- 十二月 - 28
 */

public class RabbitMQConstants {

  /**
   * 目前的设计思想是： 每个消息消费模块有个基本的queue name
   * 根据这个模块消费的消息的操作类型来做queue name的第二级细分
   * 比如 QUEUE_PAY_BASE+‘-’+ OPERATION_TYPE_BUY_PROD
   * 可以确定需要传递的消息类型
   * 用消息类型决定一个queue
   * routing key
   */
  public final static String QUEUE_PAY_BASE = "QUEUE_PAY";
  public final static String QUEUE_ORDER_BASE = "QUEUE_ORDER";
  public final static String QUEUE_USERINFO_BASE = "QUEUE_USERINFO";
  public final static String ROUTING_KEY_PAY = "routing.pay";
  public final static String ROUTING_KEY_SELL = "routing.sell";
  public final static String ROUTING_KEY_ORDER = "routing.order";
  public final static String ROUTING_KEY_USERINFO = "routing.userinfo";
  public final static String OPERATION_TYPE_BUY_PROD = "buy_prod";
  public final static String OPERATION_TYPE_SEL_PROD = "sell_prod";
  public final static String OPERATION_TYPE_UPDATE_UIPROD = "update_uiprod";
  public final static String OPERATION_TYPE_UPDATE_UITRDLOG = "update_uitrdlog";
  public final static String OPERATION_TYPE_UPDATE_ORDER = "update_order";
  public final static String EXCHANGE_NAME = "aaas_ex";



}
