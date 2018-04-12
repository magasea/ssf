package com.shellshellfish.datamanager.repositories;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import com.shellshellfish.datamanager.model.MongoFinanceDetail;

public interface MongoFinanceDetailRepository extends MongoRepository<MongoFinanceDetail, Long> {
	List<MongoFinanceDetail> findAllByDate(String date);
	void deleteAllByDate(String date);
	MongoFinanceDetail findAllByDateAndGroupIdAndSubGroupId(String date, String groupId, String subGroupId);
    MongoFinanceDetail findAllByGroupIdAndSubGroupId(String groupId, String subGroupId);
}
