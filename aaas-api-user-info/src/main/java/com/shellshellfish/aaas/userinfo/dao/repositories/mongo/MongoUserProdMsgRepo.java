package com.shellshellfish.aaas.userinfo.dao.repositories.mongo;

import com.shellshellfish.aaas.userinfo.model.dao.userinfo.UiProdMsg;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MongoUserProdMsgRepo extends MongoRepository<UiProdMsg, Long> {
    List<UiProdMsg> findAllByProdIdOrderByDateDesc(Long prodId);
}
