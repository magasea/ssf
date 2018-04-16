package com.shellshellfish.aaas.userinfo.repositories.mongo;

import com.shellshellfish.aaas.userinfo.model.dao.MongoUiTrdLog;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MongoUserTrdLogMsgRepo extends MongoRepository<MongoUiTrdLog, Long> {

  @Override
  MongoUiTrdLog save(MongoUiTrdLog uiTrdLog);

  List<MongoUiTrdLog> findAllByUserIdAndUserProdId(Long userId, Long userProdId);
  
  List<MongoUiTrdLog> findAllByUserIdAndUserProdIdAndOperationsAndTradeStatus(Long userId, Long userProdId,int operations,int tradeStatus);

  List<MongoUiTrdLog> findAllByUserProdIdAndOperations(Long userProdId, Integer operations);
}
