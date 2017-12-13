package com.shellshellfish.aaas.finance.trade.order.controller;

import com.shellshellfish.aaas.finance.trade.order.service.TradeOpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/trade")
public class TradeController {
  public static final Logger logger = LoggerFactory.getLogger(TradeController.class);

  public static String URL_HEAD="/api/userinfo";

  @Autowired
  TradeOpService tradeOpService;

}