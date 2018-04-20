package com.shellshellfish.aaas.datamanager.repositories;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import com.shellshellfish.aaas.datamanager.model.MongoFinanceDetail;

public interface MongoFinanceDetailRepository extends MongoRepository<MongoFinanceDetail, Long> {
	List<MongoFinanceDetail> findAllByDate(String date);
	void deleteAllByDate(String date);
	MongoFinanceDetail findAllByDateAndGroupIdAndSubGroupId(String date, String groupId, String subGroupId);
    List<MongoFinanceDetail> findAllByGroupIdAndSubGroupId(String groupId, String subGroupId);
	List<MongoFinanceDetail> findAllByGroupIdAndSubGroupIdAndOemid(String groupId, String subGroupId, Integer oemid);
}
