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
  public final static String ROUTING_KEY_UI_PENDRECORDS = "routing.ui.pending.records";
  public final static String ROUTING_KEY_SELL = "routing.sell";
  public final static String ROUTING_KEY_SELLPERCENT = "routing.sellpercent";
  public final static String ROUTING_KEY_ORDER = "routing.order";
  public final static String ROUTING_KEY_PREORDER = "routing.preorder";
  public final static String ROUTING_KEY_USERINFO = "routing.userinfo";
  public final static String ROUTING_KEY_USERINFO_TRDLOG = "routing.userinfo.trdlog";
  public final static String ROUTING_KEY_USERINFO_REDEEM = "routing.userinfo.redeem";
  public final static String ROUTING_KEY_USERINFO_ORDSTATCHG = "routing.userinfo.ordstatchg";
  public final static String ROUTING_KEY_USERINFO_CFMLOG = "routing.userinfo.confirmlog";
  public final static String ROUTING_KEY_USERINFO_UPDATEPROD = "routing.userinfo.updateprod";
  public final static String OPERATION_TYPE_BUY_PROD = "buy_prod";
  public final static String OPERATION_TYPE_BUY_PREORDER_PROD = "buy_preorder_prod";
  public final static String OPERATION_TYPE_SEL_PROD = "sell_prod";
  public final static String OPERATION_TYPE_SELPERCENT_PROD = "sellpercent_prod";
  public final static String OPERATION_TYPE_UPDATE_UIPROD = "update_uiprod";
  public final static String OPERATION_TYPE_UPDATE_UITRDLOG = "update_uitrdlog";
  public final static String OPERATION_TYPE_UPDATE_UITRDCONFIRMINFO = "update_uitrdconfirminfo";
  public final static String OPERATION_TYPE_UPDATE_BUY_PENDINGRECORDS = "update_buy_pendingrecords";
  public final static String OPERATION_TYPE_UPDATE_SELL_PENDINGRECORDS = "update_sell_pendingrecords";
  public final static String OPERATION_TYPE_FAILED_PENDINGRECORDS = "update_failed_pendingrecords";
  public final static String OPERATION_TYPE_UPDATE_PRECONFIRM_PENDINGRECORDS = "update_preconfirm_pendingrecords";
  public final static String OPERATION_TYPE_FAILED_SELL_PENDINGRECORDS = "update_sellfailed_pendingrecords";
  public final static String OPERATION_TYPE_CONFIRM_PENDINGRECORDS = "update_confirm_pendingrecords";
  public final static String OPERATION_TYPE_FAILED_TRADE = "order_failed_trdflow";
  public final static String OPERATION_TYPE_CACULATE_UIACCECTS = "caculate_userassect";
  public final static String OPERATION_TYPE_UPDATE_UIPRODQUANTITY = "update_uiprodquantity";
  public final static String OPERATION_TYPE_CHECKSELL_ROLLBACK = "update_checksell_rollback";
  public final static String OPERATION_TYPE_UPDATE_ORDER = "update_order";
  public final static String OPERATION_TYPE_HANDLE_PREORDER = "handle_preorder";
  public final static String EXCHANGE_NAME = "aaas_ex";



}
