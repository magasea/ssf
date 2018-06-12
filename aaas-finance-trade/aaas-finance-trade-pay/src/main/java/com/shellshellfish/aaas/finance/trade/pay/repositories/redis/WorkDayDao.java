package com.shellshellfish.aaas.finance.trade.pay.repositories.redis;


import com.shellshellfish.aaas.common.constants.RedisConstants;
import com.shellshellfish.aaas.common.utils.TradeUtil;
import com.shellshellfish.aaas.finance.trade.pay.model.WorkDayRedis;

import java.util.Map;
import java.util.concurrent.TimeUnit;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class WorkDayDao {
    private static final String KEY = RedisConstants.TRADE_PAY_KEY + RedisConstants.SEPARATOR +
            "WorkDay" + RedisConstants.SEPARATOR;

    private static final int TIMEOUT = 1800;//in seconds

    private static final int EXPIRE_HOUR = 4;

    private static final int EXPIRE_MINUTE = 30;

    private HashOperations<String, String, WorkDayRedis> hashOps;


    @Resource
    RedisTemplate redisTemplate;

    @PostConstruct
    void init() {
        hashOps = redisTemplate.opsForHash();
    }

    public void addWorkDay(WorkDayRedis workDayRedis) {
        String currentDay = workDayRedis.getQueryDay();

        hashOps.putIfAbsent(KEY + currentDay, currentDay + workDayRedis.getFundCode()
                , workDayRedis);
        redisTemplate.expireAt(KEY + currentDay, TradeUtil.getDateOfSpecificTimeToday(EXPIRE_HOUR, EXPIRE_MINUTE));
    }

    public void updateWorkDay(WorkDayRedis workDayRedis) {

        hashOps.put(KEY + workDayRedis.getQueryDay(), workDayRedis.getQueryDay() + workDayRedis.getFundCode(),
                workDayRedis);
//			redisTemplate.expireAt(KEY+currentDay, TradeUtil.getDateOfSpecificTimeToday(EXPIRE_HOUR,
//					EXPIRE_MINUTE));
    }

    public WorkDayRedis get(String Code) {
        String currentDay = TradeUtil.getReadableDateTime(TradeUtil.getUTCTime()).split("T")[0];
        return hashOps.get(KEY + currentDay, currentDay + Code);
    }

    public long getNumberOfWorkDays() {
        return hashOps.size(KEY);
    }

    public Map<String, WorkDayRedis> getAllEmployees() {
        return hashOps.entries(KEY);
    }

    public long deleteWorkDays(String... ids) {
        return hashOps.delete(KEY, (Object) ids);
    }
}