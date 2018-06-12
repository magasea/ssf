package com.shellshellfish.aaas.datamanager.repositories.mongo;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import com.shellshellfish.aaas.datamanager.model.MongoFinanceAll;

public interface MongoFinanceALLRepository extends MongoRepository<MongoFinanceAll, Long> {
	List<MongoFinanceAll> findAllByDate(String date);
	void deleteAllByDate(String date);
	List<MongoFinanceAll> findBySerialIn(List<Integer> serial);
	void deleteAllByOemid(Integer oemid);
	List<MongoFinanceAll> findBySerialInAndOemid(List<Integer> serialList, Integer oemid);
}
