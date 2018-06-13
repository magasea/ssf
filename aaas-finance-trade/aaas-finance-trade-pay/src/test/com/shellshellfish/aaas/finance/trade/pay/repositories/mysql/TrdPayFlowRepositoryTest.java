package com.shellshellfish.aaas.finance.trade.pay.repositories.mysql;

import com.shellshellfish.aaas.common.utils.MyBeanUtils;
import com.shellshellfish.aaas.finance.trade.pay.model.dao.mysql.TrdPayFlow;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by chenwei on 2018- 六月 - 13
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ActiveProfiles("local")
public class TrdPayFlowRepositoryTest {

  @Autowired
  TrdPayFlowRepository trdPayFlowRepository;


  @Test
  public void findSerialOrderinfo() {
    Pageable pageable = new PageRequest(0, 10);
    Iterable<Object[]> trdPayFlowPage =
    trdPayFlowRepository.findSerialOrderinfo(pageable);
    System.out.println(((Page<Object[]>) trdPayFlowPage).getTotalElements());;
    System.out.println(((Page<Object[]>) trdPayFlowPage).getTotalPages());;
    pageable = new PageRequest(0, 20);
    trdPayFlowPage =
        trdPayFlowRepository.findSerialOrderinfo(pageable);
    System.out.println(((Page<Object[]>) trdPayFlowPage).getTotalElements());;
    System.out.println(((Page<Object[]>) trdPayFlowPage).getTotalPages());;

    String[] properties = {""};
    List<TrdPayFlow> trdPayFlows = new ArrayList<>();

      TrdPayFlow trdPayFlow = new TrdPayFlow();
      for(Object[] subItem: trdPayFlowPage){
        trdPayFlow.setApplySerial((String)subItem[1]);
        trdPayFlow.setOutsideOrderno((String)subItem[2]);
        trdPayFlow.setOrderDetailId(Long.parseLong(""+subItem[3]));
        trdPayFlows.add(trdPayFlow);
      }

    for(TrdPayFlow item: trdPayFlows){
      System.out.println(item.getTrdStatus());
      System.out.println(item.getTrdType());
      System.out.println(item.getTrdbkerStatusCode());
      System.out.println(item.getApplydateUnitvalue());
      System.out.println(item.getBuyDiscount());
      System.out.println(item.getBuyFee());
      System.out.println(item.getCreateBy());
      System.out.println(item.getCreateDate());
      System.out.println(item.getId());
      System.out.println(item.getOrderDetailId());
      System.out.println(item.getTradeBrokeId());
      System.out.println(item.getTradeConfirmShare());
      System.out.println(item.getTradeConfirmSum());
      System.out.println(item.getTradeTargetShare());
      System.out.println(item.getTradeTargetSum());
      System.out.println(item.getTrdApplyShare());
      System.out.println(item.getTrdApplySum());
      System.out.println(item.getTrdConfirmDate());
      System.out.println(item.getUpdateBy());
      System.out.println(item.getUpdateDate());
      System.out.println(item.getUserId());
      System.out.println(item.getUserProdId());
      System.out.println(item.getApplySerial());
      System.out.println(item.getBankCardNum());
      System.out.println(item.getErrCode());
      System.out.println(item.getErrMsg());
      System.out.println(item.getFundCode());
      System.out.println(item.getOutsideOrderno());
      System.out.println(item.getTradeAcco());
      System.out.println(item.getTrdApplyDate());
      System.out.println(item.getTrdbkerStatusName());
    }
//    trdPayFlowPage.map( ())
//    for( (Object[]) item: trdPayFlowPage){
//
//      TrdPayFlow trdPayFlow = new TrdPayFlow();
//      trdPayFlow.setApplySerial(item[1]);
//      trdPayFlow.setOutsideOrderno(item[2]);
//      trdPayFlow.setOrderDetailId(item[3]);
//    }
  }
}
