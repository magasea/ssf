package com.shellshellfish.aaas.userinfo.repositories.redis;

import com.shellshellfish.aaas.common.constants.RedisConstants;
import com.shellshellfish.aaas.userinfo.model.redis.UserBaseInfoRedis;

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

    private static final String KEY = RedisConstants.USER_INFO_KEY + "UserInfo";

    private static final int timeout = 7200; //记得用timeunit 为second

    private HashOperations<String, String, UserBaseInfoRedis> hashOps;

    @Resource
    RedisTemplate redisTemplate;

    @PostConstruct
    void init() {
        redisTemplate.setHashValueSerializer(new JdkSerializationRedisSerializer());
        hashOps = redisTemplate.opsForHash();
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

    public UserBaseInfoRedis get(String userUUID) {
        return hashOps.get(KEY + userUUID, userUUID);
    }

}