package com.shellshellfish.aaas.userinfo.service.impl;

import com.shellshellfish.aaas.common.message.order.MongoUiTrdZZInfo;
import com.shellshellfish.aaas.common.utils.InstantDateUtil;
import com.shellshellfish.aaas.userinfo.model.dao.UiProductDetail;
import com.shellshellfish.aaas.userinfo.repositories.mysql.UiProductDetailRepo;
import com.shellshellfish.aaas.userinfo.repositories.mysql.UiProductRepo;
import com.shellshellfish.aaas.userinfo.repositories.mysql.UserInfoRepository;
import com.shellshellfish.aaas.userinfo.service.UserFinanceProdCalcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

/**
 * @Author pierre 18-3-1
 */
@Service
public class CalculateConfirmedAsset {

    Logger logger = LoggerFactory.getLogger(CalculateConfirmedAsset.class);

    @Autowired
    UserFinanceProdCalcService userFinanceProdCalcService;

    @Autowired
    UiProductDetailRepo uiProductDetailRepo;

    @Autowired
    UiProductRepo uiProductRepo;

    @Autowired
    @Qualifier("zhongZhengMongoTemplate")
    MongoTemplate zhongZhengMongoTemplate;

    @Autowired
    UserInfoRepository userInfoRepository;

    /**
     * 当有申购或者赎回确认的消息时 ,重新计算资产
     */
    public void calculateConfirmedAsset(MongoUiTrdZZInfo mongoUiTrdZZInfo) {

        try {
            logger.info("calculate conformed Asset :{}", mongoUiTrdZZInfo);
            final String pattern = InstantDateUtil.yyyyMMdd;
            List<UiProductDetail> uiProductDetailList = uiProductDetailRepo
                    .findAllByUserProdId(mongoUiTrdZZInfo.getUserProdId());
            String date = mongoUiTrdZZInfo.getConfirmDate();

            //从确认日期开始到当前时间的数据都要修正（此处默认此次确认到当前时间没有其他操作）
            LocalDate now = LocalDate.now(ZoneId.systemDefault()).plusDays(1);
            LocalDate confirmDate = InstantDateUtil.format(date, pattern);
            for (LocalDate startDate = confirmDate; startDate.isBefore(now); startDate = startDate.plusDays(1)) {
                for (UiProductDetail uiProductDetail : uiProductDetailList) {
                    try {
                        logger.info("start to calculate asset startDate:{},userProdDetail:{} ", startDate, uiProductDetail);
                        userFinanceProdCalcService
                                .calculateFromZzInfo(uiProductDetail.getUserProdId(), uiProductDetail.getFundCode(),
                                        InstantDateUtil.format(startDate, pattern));
                    } catch (Exception e) {
                        logger.error("calculate dailyAmount failed:{}", uiProductDetail, e);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("计算用户资产失败：mongoUiTrdZZInfo：{}，{}", mongoUiTrdZZInfo, e);
        }
    }

}
