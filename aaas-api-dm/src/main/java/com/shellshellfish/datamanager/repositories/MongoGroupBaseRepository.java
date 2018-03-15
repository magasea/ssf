package com.shellshellfish.datamanager.repositories;


import com.shellshellfish.datamanager.model.GroupBase;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MongoGroupBaseRepository extends MongoRepository<GroupBase, Long> {

	GroupBase findFirstByGroupId(Long groupId);

}
