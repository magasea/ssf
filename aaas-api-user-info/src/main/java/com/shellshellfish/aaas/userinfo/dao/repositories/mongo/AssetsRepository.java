package com.shellshellfish.aaas.userinfo.dao.repositories.mongo;

import com.shellshellfish.aaas.userinfo.model.dao.userinfo.UiAssetDailyRept;
import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AssetsRepository extends MongoRepository<UiAssetDailyRept, Long> {

    @Query(value = "{ 'date' : {'$gte' : ?1, '$lte': ?2 }}")
    List<UiAssetDailyRept> findByDateBetweenAndUserId( Long beginDate, Long endDate, Long userId);


    List<UiAssetDailyRept> findByUserId(Long userId);
}
