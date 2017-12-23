package com.shellshellfish.aaas.userinfo.repositories.mongo;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.shellshellfish.aaas.userinfo.model.dao.UiSysMsg;

public interface MongoUserSysMsgRepo extends MongoRepository<UiSysMsg, Long> {
  List<UiSysMsg> findAllByOrderByDateDesc();
  UiSysMsg save(UiSysMsg uiSysMsg);
}
