package com.shellshellfish.aaas.finance.trade.order.repositories.redis;

import com.shellshellfish.aaas.common.constants.RedisConstants;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository

public class UserPidDAO {
    private static final String BASE_KEY = RedisConstants.TRADE_ORDER_KEY + RedisConstants.SEPARATOR;

    private static final long DEFAULT_TTL = 1800; //in second

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Transactional
    public void addUserPid(String trdAcco, int brokerId, Long userId, String userPid) {
        stringRedisTemplate.opsForValue().setIfAbsent(BASE_KEY + trdAcco + brokerId + userId, userPid);
        stringRedisTemplate.expire(BASE_KEY + trdAcco + brokerId + userId, DEFAULT_TTL, TimeUnit.SECONDS);
    }

    @Transactional
    public void updateUserPid(String trdAcco, int brokerId, Long userId, String userPid) {
        stringRedisTemplate.opsForValue().set(BASE_KEY + trdAcco + brokerId + userId, userPid);
        stringRedisTemplate.expire(BASE_KEY + trdAcco + brokerId + userId, DEFAULT_TTL, TimeUnit.SECONDS);
    }

    public String getUserPid(String trdAcco, int brokerId, Long userId) {
        return stringRedisTemplate.opsForValue().get(BASE_KEY + trdAcco + brokerId + userId);
    }

    @Transactional
    public void deleteUserPid(String trdAcco, int brokerId, Long userId) {
        stringRedisTemplate.delete(BASE_KEY + trdAcco + brokerId + userId);
    }
}
