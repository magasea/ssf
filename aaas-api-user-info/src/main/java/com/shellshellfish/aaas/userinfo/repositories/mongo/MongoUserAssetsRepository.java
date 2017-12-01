package com.shellshellfish.aaas.userinfo.repositories.mongo;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.shellshellfish.aaas.userinfo.model.dao.UiAssetDailyRept;

public interface MongoUserAssetsRepository extends MongoRepository<UiAssetDailyRept, Long> {

//    @Query(value = "{$and: [{'userId':'?2'}]}")
    List<UiAssetDailyRept> findByUserIdAndDateIsBetween(BigInteger   userId, Long beginDate, Long endDate );

    @Query("{'userId':?0}")
    List<UiAssetDailyRept> findByUserId(Long userId);
}
