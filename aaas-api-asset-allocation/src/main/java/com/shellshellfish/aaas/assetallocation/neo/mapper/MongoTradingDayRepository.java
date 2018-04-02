package com.shellshellfish.aaas.assetallocation.neo.mapper;

import com.shellshellfish.aaas.assetallocation.neo.entity.MongoTradingDay;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MongoTradingDayRepository extends MongoRepository<MongoTradingDay,Long> {

    List<MongoTradingDay>  findAllBy();
}
