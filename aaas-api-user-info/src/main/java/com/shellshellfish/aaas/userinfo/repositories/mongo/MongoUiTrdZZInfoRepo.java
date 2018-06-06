package com.shellshellfish.aaas.userinfo.repositories.mongo;


import com.shellshellfish.aaas.userinfo.model.dao.MongoUiTrdZZInfo;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface MongoUiTrdZZInfoRepo extends MongoRepository<MongoUiTrdZZInfo, Long> {

    @Override
    MongoUiTrdZZInfo save(MongoUiTrdZZInfo mongoUiTrdZZInfo);


    List<MongoUiTrdZZInfo> findAllByUserIdAndUserProdIdAndTradeTypeAndTradeStatusAndConfirmDateLessThanEqual(
            Long userId, Long userProdId, int tradeType, int tradeStatus, String date);

    MongoUiTrdZZInfo findByUserProdIdAndUserIdAndApplySerial(Long userProdId, Long userId,
                                                             String applySerial);

    List<MongoUiTrdZZInfo> findByUserProdIdAndFundCodeAndTradeTypeAndTradeStatusAndConfirmDate(
            Long userProdId, String fundCode, int tradeType, int tradeStatus, String confirmDate);

    List<MongoUiTrdZZInfo> findByUserProdIdAndFundCodeAndTradeTypeAndTradeStatusAndConfirmDateLessThanEqual(
            long userProdId, String fundCode, int tradeType, int tradeStatus, String confirmDate);

    List<MongoUiTrdZZInfo> findByTradeTypeAndTradeStatusAndConfirmDateGreaterThanEqual(
            int trdType, int tradeStatus, String confirmDate);

    List<MongoUiTrdZZInfo> findAllByUserProdId(Long userProdId);

    List<MongoUiTrdZZInfo> findAllByUserProdIdAndConfirmDateLessThanEqual(Long userProdId, String date);

    List<MongoUiTrdZZInfo> findAllByUserProdIdAndFundCodeAndConfirmDate(Long userProdId, String fundCode,
                                                                        String confirmDate);

    List<MongoUiTrdZZInfo> findAllByConfirmDate(String confirmDate);
}
