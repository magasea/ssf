package com.shellshellfish.aaas.assetallocation.mapper;

import com.shellshellfish.aaas.assetallocation.entity.MongoTradingDay;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MongoTradingDayRepository extends MongoRepository<MongoTradingDay,Long> {

    List<MongoTradingDay>  findAllBy();
}
