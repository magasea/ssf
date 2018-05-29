package com.shellshellfish.aaas.userinfo.repositories.redis;

import com.shellshellfish.aaas.common.constants.RedisConstants;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.TimeUnit;

@Repository
public class RedisSellRateDao {

    private static final String KEY = RedisConstants.USER_INFO_KEY + RedisConstants.SEPARATOR + "sellRate" + RedisConstants.SEPARATOR;

    private static final int TIMEOUT = 30; //过期时间
    private static final TimeUnit TIME_UNIT = TimeUnit.DAYS; //过期时间单位


    private ValueOperations<String, String> valueOps;

    @Resource
    RedisTemplate redisTemplate;

    @PostConstruct
    void init() {
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        valueOps = redisTemplate.opsForValue();
    }

    public BigDecimal get(String key) {
        String value = valueOps.get(KEY + key);
        if (value == null)
            return null;
        else
            return new BigDecimal(value).setScale(4, BigDecimal.ROUND_HALF_UP);
    }

    @Transactional
    public void set(String key, BigDecimal value) {
        valueOps.setIfAbsent(KEY + key, value.setScale(4, RoundingMode.HALF_UP).toString());
        redisTemplate.expire(KEY + key, TIMEOUT, TIME_UNIT);
    }

}