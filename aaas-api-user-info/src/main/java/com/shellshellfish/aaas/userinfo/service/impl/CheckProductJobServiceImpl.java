package com.shellshellfish.aaas.userinfo.service.impl;

import com.shellshellfish.aaas.common.enums.SystemUserEnum;
import com.shellshellfish.aaas.common.enums.TrdOrderStatusEnum;
import com.shellshellfish.aaas.common.utils.TradeUtil;
import com.shellshellfish.aaas.userinfo.model.dao.UiProductDetail;
import com.shellshellfish.aaas.userinfo.model.dao.UiProducts;
import com.shellshellfish.aaas.userinfo.repositories.mysql.UiProductDetailRepo;
import com.shellshellfish.aaas.userinfo.repositories.mysql.UiProductRepo;
import com.shellshellfish.aaas.userinfo.service.CheckProductJobService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by chenwei on 2018- 一月 - 26
 */
@Service
public class CheckProductJobServiceImpl implements CheckProductJobService {

  @Autowired
  UiProductRepo uiProductRepo;

  @Autowired
  UiProductDetailRepo uiProductDetailRepo;

  @Override
  public void checkProductsAndSetStatus() {
    List<UiProducts> uiProductsList =  uiProductRepo.findAllByStatusIs(TrdOrderStatusEnum.WAITPAY.getStatus());
    Long yesterday = TradeUtil.getUTCTime1DayBefore();
    for(UiProducts uiProducts: uiProductsList){
      if(uiProducts.getCreateDate() > yesterday){
        continue;
      }

      List<UiProductDetail> uiProductDetails =  uiProductDetailRepo.findAllByUserProdIdIs
          (uiProducts.getId());

      boolean canSetFail = true;
      for(UiProductDetail uiProductDetail: uiProductDetails){
        if(uiProductDetail.getStatus() != TrdOrderStatusEnum.WAITPAY.getStatus()){
          canSetFail = false;
        }
      }
      if(canSetFail){
        uiProducts.setUpdateBy(SystemUserEnum.SYSTEM_USER_ENUM.getUserId());
        uiProducts.setUpdateDate(TradeUtil.getUTCTime());
        uiProducts.setStatus(TrdOrderStatusEnum.FAILED.getStatus());
        uiProductRepo.save(uiProducts);
      }
    }
  }
}
