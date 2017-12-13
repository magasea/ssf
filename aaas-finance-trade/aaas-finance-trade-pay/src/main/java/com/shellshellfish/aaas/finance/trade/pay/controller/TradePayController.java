package com.shellshellfish.aaas.finance.trade.pay.controller;

import com.shellshellfish.aaas.finance.trade.pay.service.PayService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/trade")
public class TradePayController {
  public static final Logger logger = LoggerFactory.getLogger(TradePayController.class);

  public static String URL_HEAD="/api/userinfo";

  @Autowired
  PayService payService;

}
