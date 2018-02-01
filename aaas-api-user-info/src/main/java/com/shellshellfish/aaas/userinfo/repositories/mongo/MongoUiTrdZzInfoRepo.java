package com.shellshellfish.aaas.userinfo.repositories.mongo;

import com.shellshellfish.aaas.userinfo.model.dao.MongoUiTrdZZinfo;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MongoUiTrdZzInfoRepo extends MongoRepository<MongoUiTrdZZinfo, Long> {

	List<MongoUiTrdZZinfo> findAllByUserProdIdAndTradeTypeAndTradeStatus(Long userPordId,
			Integer operations, Integer tradeStatus);
}
