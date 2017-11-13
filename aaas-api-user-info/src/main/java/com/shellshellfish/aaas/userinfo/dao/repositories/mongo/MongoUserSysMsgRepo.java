package com.shellshellfish.aaas.userinfo.dao.repositories.mongo;

import com.shellshellfish.aaas.userinfo.model.dao.userinfo.UiSysMsg;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MongoUserSysMsgRepo extends MongoRepository<UiSysMsg, Long> {
  List<UiSysMsg> findAllByOrderByDateDesc();
}
