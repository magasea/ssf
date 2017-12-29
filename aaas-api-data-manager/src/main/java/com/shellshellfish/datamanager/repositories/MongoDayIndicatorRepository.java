package com.shellshellfish.datamanager.repositories;


import com.shellshellfish.datamanager.model.DayIndicatorDTO;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MongoDayIndicatorRepository extends MongoRepository<DayIndicatorDTO, Long> {
	List<DayIndicatorDTO> findByCode(String code);

}
