package com.shellshellfish.datamanager.repositories.mongo;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import com.shellshellfish.datamanager.model.MongoFinanceAll;

public interface MongoFinanceALLRepository extends MongoRepository<MongoFinanceAll, Long> {
	MongoFinanceAll findAllByDate(String date);
	void deleteAllByDate(String date);
	List<MongoFinanceAll> findBySerialIn(List<Integer> serial);
}
