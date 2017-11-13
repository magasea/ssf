package com.shellshellfish.aaas.userinfo.dao.repositories.mongo;

import com.shellshellfish.aaas.userinfo.model.dao.userinfo.UiAssetDailyRept;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface MongoUserAssetsRepository extends MongoRepository<UiAssetDailyRept, Long> {

//    @Query(value = "{$and: [{'userId':'?2'}]}")
    List<UiAssetDailyRept> findByUserIdAndDateIsBetween(BigInteger   userId, Long beginDate, Long endDate );

    @Query("{'userId':?0}")
    List<UiAssetDailyRept> findByUserId(Long userId);
}
