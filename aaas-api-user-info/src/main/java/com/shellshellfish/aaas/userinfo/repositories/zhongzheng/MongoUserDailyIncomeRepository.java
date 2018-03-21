package com.shellshellfish.aaas.userinfo.repositories.zhongzheng;

import com.shellshellfish.aaas.userinfo.model.dao.MongoUserDailyIncome;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @Author pierre 18-2-24
 */
public interface MongoUserDailyIncomeRepository extends
		MongoRepository<MongoUserDailyIncome, Long> {

	List<MongoUserDailyIncome> findByUserId(Long userProdId);

}
