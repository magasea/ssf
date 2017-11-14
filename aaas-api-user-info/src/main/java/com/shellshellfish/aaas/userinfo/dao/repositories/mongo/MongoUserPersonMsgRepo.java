package com.shellshellfish.aaas.userinfo.dao.repositories.mongo;

import com.shellshellfish.aaas.userinfo.model.dao.userinfo.UiPersonMsg;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface MongoUserPersonMsgRepo extends MongoRepository<UiPersonMsg, Long> {

  @Query(value = "{'$and':[{'userId':'?0'},{'readed':?1}]}")
  List<UiPersonMsg> getUiPersonMsgsByUserIdAndReaded(Long userId, Boolean readed);

  @Override
  <S extends UiPersonMsg> List<S> save(Iterable<S> entites);

}
