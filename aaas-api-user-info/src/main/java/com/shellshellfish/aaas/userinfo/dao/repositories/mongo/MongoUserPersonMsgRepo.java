package com.shellshellfish.aaas.userinfo.dao.repositories.mongo;

import com.shellshellfish.aaas.userinfo.model.dao.userinfo.UiPersonMsg;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MongoUserPersonMsgRepo extends MongoRepository<UiPersonMsg, Long> {
  List<UiPersonMsg> getUiUserPersonMsgByUserIdAndReaded(Long userId, Boolean readed);

  List<UiPersonMsg> save(List<UiPersonMsg> inputList);

}
