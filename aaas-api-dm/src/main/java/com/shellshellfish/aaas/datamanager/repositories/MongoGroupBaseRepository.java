package com.shellshellfish.aaas.datamanager.repositories;


import com.shellshellfish.aaas.datamanager.model.GroupBase;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MongoGroupBaseRepository extends MongoRepository<GroupBase, Long> {

	GroupBase findFirstByGroupId(Long groupId);

}
