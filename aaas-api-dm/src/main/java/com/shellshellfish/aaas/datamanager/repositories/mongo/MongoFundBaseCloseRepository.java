package com.shellshellfish.aaas.datamanager.repositories.mongo;


import com.shellshellfish.aaas.datamanager.model.FundBaseClose;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MongoFundBaseCloseRepository extends MongoRepository<FundBaseClose, Long> {

    List<FundBaseClose> findByQueryDateBetween(Long startDate, Long endDate, Sort sort);
}
