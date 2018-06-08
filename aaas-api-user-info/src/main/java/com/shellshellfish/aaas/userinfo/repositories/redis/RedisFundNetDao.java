package com.shellshellfish.aaas.userinfo.repositories.redis;

import com.shellshellfish.aaas.common.constants.RedisConstants;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.TimeUnit;

@Repository
public class RedisFundNetDao {

    private static final String KEY = RedisConstants.USER_INFO_KEY + RedisConstants.SEPARATOR + "fundNet";

    private static final int TIMEOUT = 1; //过期时间
    private static final TimeUnit TIME_UNIT = TimeUnit.HOURS; //过期时间单位


    private HashOperations<String, String, String> hashOps;

    @Resource
    RedisTemplate redisTemplate;

    @PostConstruct
    void init() {
        redisTemplate.setHashValueSerializer(new StringRedisSerializer());
        hashOps = redisTemplate.opsForHash();

    }

    public BigDecimal get(String key, String hashKey) {
        String value = hashOps.get(KEY + RedisConstants.SEPARATOR + key, hashKey);
        if (value == null)
            return null;
        else
            return new BigDecimal(value).setScale(6, BigDecimal.ROUND_HALF_UP);
    }

    @Transactional
    public void set(String key, String hashKey, BigDecimal value) {
        hashOps.putIfAbsent(KEY + RedisConstants.SEPARATOR + key, hashKey, value.setScale(6, RoundingMode.HALF_UP)
                .toString());
        redisTemplate.expire(KEY + RedisConstants.SEPARATOR + key, TIMEOUT, TIME_UNIT);
    }
}