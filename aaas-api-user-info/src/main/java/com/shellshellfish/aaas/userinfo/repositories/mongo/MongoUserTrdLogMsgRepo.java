package com.shellshellfish.aaas.userinfo.repositories.mongo;

import com.shellshellfish.aaas.userinfo.model.dao.MongoUiTrdLog;
import com.shellshellfish.aaas.userinfo.model.dao.UiSysMsg;
import com.shellshellfish.aaas.userinfo.model.dao.UiTrdLog;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MongoUserTrdLogMsgRepo extends MongoRepository<MongoUiTrdLog, Long> {

  @Override
  MongoUiTrdLog save(MongoUiTrdLog uiTrdLog);

  List<MongoUiTrdLog> findAllByUserIdAndUserProdId(Long userId, Long userProdId);
  
  List<MongoUiTrdLog> findAllByUserIdAndUserProdIdAndOperationsAndTradeStatus(Long userId, Long userProdId,int operations,int tradeStatus);

  List<MongoUiTrdLog> findAllByUserId(Long userId);
}
