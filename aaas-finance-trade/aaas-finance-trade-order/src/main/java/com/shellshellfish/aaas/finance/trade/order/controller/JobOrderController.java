package com.shellshellfish.aaas.finance.trade.order.controller;

import com.shellshellfish.aaas.finance.trade.order.service.JobOrderService;
import io.swagger.annotations.ApiOperation;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by developer4 on 2018- 六月 - 14
 */
@RestController
@RequestMapping("/api/trade/job")
public class JobOrderController {

  @Autowired
  private JobOrderService jobOrderService;


  @ApiOperation("检查orderDetail表中和trdpayFlow表不一致的数据做patch ")
  @RequestMapping(value = "/patchOrderWithPay", method = RequestMethod.GET)
  @ResponseBody
  public ResponseEntity<Map> patchOrderWithPay() throws Exception {
    jobOrderService.patchOrderWithPay();
    return new ResponseEntity<Map>( HttpStatus.OK);
  }
}
