package com.shellshellfish.aaas.userinfo.repositories.mongo;

import com.shellshellfish.aaas.userinfo.model.dao.UiSysMsg;
import com.shellshellfish.aaas.userinfo.model.dao.UiTrdLog;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MongoUserTrdLogMsgRepo extends MongoRepository<UiTrdLog, Long> {

  @Override
  UiTrdLog save(UiTrdLog uiTrdLog);
}