package com.shellshellfish.aaas.userinfo.repositories.mongo;


import com.shellshellfish.aaas.userinfo.model.dao.MongoUiTrdZZInfo;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MongoUiTrdZZInfoRepo extends MongoRepository<MongoUiTrdZZInfo, Long> {

  @Override
  MongoUiTrdZZInfo save(MongoUiTrdZZInfo mongoUiTrdZZInfo);

  List<MongoUiTrdZZInfo> findAllByUserIdAndUserProdId(Long userId, Long userProdId);

  List<MongoUiTrdZZInfo> findAllByUserIdAndUserProdIdAndOperationsAndTradeStatus(Long userId,
      Long userProdId, int operations, int tradeStatus);

  List<MongoUiTrdZZInfo> findAllByUserId(Long userId);

  MongoUiTrdZZInfo findByUserProdIdAndUserIdAndOutSideOrderNo(Long userProdId, Long userId,
      String outSideOrderNo);
}
