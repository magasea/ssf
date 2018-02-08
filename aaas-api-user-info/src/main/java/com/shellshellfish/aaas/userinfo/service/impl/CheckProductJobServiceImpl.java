package com.shellshellfish.aaas.userinfo.service.impl;

import com.shellshellfish.aaas.common.enums.SystemUserEnum;
import com.shellshellfish.aaas.common.enums.TrdOrderOpTypeEnum;
import com.shellshellfish.aaas.common.enums.TrdOrderStatusEnum;
import com.shellshellfish.aaas.common.utils.TradeUtil;
import com.shellshellfish.aaas.userinfo.model.dao.MongoUiTrdZZInfo;
import com.shellshellfish.aaas.userinfo.model.dao.UiProductDetail;
import com.shellshellfish.aaas.userinfo.model.dao.UiProducts;
import com.shellshellfish.aaas.userinfo.repositories.mongo.MongoUiTrdZZInfoRepo;
import com.shellshellfish.aaas.userinfo.repositories.mysql.UiProductDetailRepo;
import com.shellshellfish.aaas.userinfo.repositories.mysql.UiProductRepo;
import com.shellshellfish.aaas.userinfo.service.CheckProductJobService;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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

  @Autowired
  MongoUiTrdZZInfoRepo mongoUiTrdZZInfoRepo;


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
    //check all confirmed information and caculate the remaining fund shares there
    String confirmDateCheck = TradeUtil.getReadableDateTime(TradeUtil.getUTCTime()).split("T")[0];
    List<MongoUiTrdZZInfo> mongoUiTrdZZInfos = mongoUiTrdZZInfoRepo
    .findByTradeTypeAndTradeStatusAndConfirmDateGreaterThanEqual(
        TrdOrderOpTypeEnum.REDEEM.getOperation(), TrdOrderStatusEnum.CONFIRMED.getStatus(),
        confirmDateCheck);
    Set<Long> userProductIdsToCheck = new HashSet<>();
    for(MongoUiTrdZZInfo mongoUiTrdZZInfo: mongoUiTrdZZInfos){
      userProductIdsToCheck.add(mongoUiTrdZZInfo.getUserProdId());
    }
    //现在筛选出了需要进行技术的user_product_id 列
    for(Long userProdId: userProductIdsToCheck){
      boolean fundBuyCfm = false;
      List<MongoUiTrdZZInfo> mongoUiTrdZZInfoList = mongoUiTrdZZInfoRepo.findAllByUserProdId
          (userProdId);
      Long quantityStart = 0L;
      //1. get the first confirm of buy as base

      //2. caculate all sell command and number to deduct the current fund share

    }
  }

}
