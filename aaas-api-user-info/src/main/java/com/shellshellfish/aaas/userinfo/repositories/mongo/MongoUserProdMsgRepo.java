package com.shellshellfish.aaas.userinfo.repositories.mongo;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.shellshellfish.aaas.userinfo.model.dao.UiProdMsg;

public interface MongoUserProdMsgRepo extends MongoRepository<UiProdMsg, Long> {
    List<UiProdMsg> findAllByProdIdOrderByDateDesc(Long prodId);
}
