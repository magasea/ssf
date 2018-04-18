package com.shellshellfish.aaas.userinfo.repositories.mongo;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.shellshellfish.aaas.userinfo.model.dao.UiPersonMsg;

public interface MongoUserPersonMsgRepo extends MongoRepository<UiPersonMsg, Long> {

  @Query(value = "{'$and':[{'userId':'?0'},{'readed':?1}]}")
  List<UiPersonMsg> getUiPersonMsgsByUserIdAndReaded(Long userId, Boolean readed);

  List<UiPersonMsg> findByUserIdAndReadedOrderByCreatedDateDesc(String userId, Boolean readed);

  <S extends UiPersonMsg> List<S> save(Iterable<S> entites);

}
