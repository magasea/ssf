package com.shellshellfish.aaas.userinfo.repositories.redis;

import com.shellshellfish.aaas.common.constants.RedisConstants;
import com.shellshellfish.aaas.userinfo.model.redis.UserBaseInfoRedis;

import java.security.Key;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class UserInfoBaseDao {


    private static final String KEY = RedisConstants.USER_INFO_KEY + RedisConstants.SEPARATOR + "UserInfo" + RedisConstants.SEPARATOR;;
    private static final String ITEM_CACULATE_STATUS = "ITEM_CACULATE_STATUS";

    private static final int timeout = 7200; //记得用timeunit 为second

    private static final int CALCULATE_TIMEOUT = 90;
    private HashOperations<String, String, UserBaseInfoRedis> hashOps;

    private HashOperations<String, String, Boolean> hashOpsCaculate;

    @Resource
    RedisTemplate redisTemplate;

    @PostConstruct
    void init() {
        redisTemplate.setHashValueSerializer(new JdkSerializationRedisSerializer());
        hashOps = redisTemplate.opsForHash();
        hashOpsCaculate = redisTemplate.opsForHash();
    }

    @Transactional
    public void addUserBaseInfo(UserBaseInfoRedis userBaseInfoRedis) {
        hashOps.putIfAbsent(KEY + userBaseInfoRedis.getUuid(), userBaseInfoRedis.getUuid(),
                userBaseInfoRedis);
        redisTemplate.expire(KEY + userBaseInfoRedis.getUuid(), timeout, TimeUnit.SECONDS);
    }

    @Transactional
    public void updateUserBaseInfo(UserBaseInfoRedis userBaseInfoRedis) {
        hashOps.put(KEY + userBaseInfoRedis.getUuid(), userBaseInfoRedis.getUuid(), userBaseInfoRedis);
        redisTemplate.expire(KEY + userBaseInfoRedis.getUuid(), timeout, TimeUnit.SECONDS);

    }

    @Transactional
    public void setCaculateStatus(Long userProdId, String fundCode){
        Boolean status = Boolean.TRUE;
        hashOpsCaculate.put(KEY +userProdId+fundCode, ITEM_CACULATE_STATUS,status);
        redisTemplate.expire(KEY +userProdId+fundCode, CALCULATE_TIMEOUT, TimeUnit.SECONDS);
    }

    @Transactional
    public Boolean getCaculateStatus(Long userProdId, String fundCode){
        Boolean result =
        hashOpsCaculate.get(KEY +userProdId+fundCode, ITEM_CACULATE_STATUS);
        return result;
    }

    public UserBaseInfoRedis get(String userUUID) {
        return hashOps.get(KEY + userUUID, userUUID);
    }

}