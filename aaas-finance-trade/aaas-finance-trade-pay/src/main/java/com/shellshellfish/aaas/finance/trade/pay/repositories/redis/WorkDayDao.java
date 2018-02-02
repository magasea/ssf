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
	  private static final String KEY = RedisConstants.TRADE_PAY_KEY+"WorkDay";
	  
		private static final int TIMEOUT = 1800;//in seconds

		private static final int EXPIRE_HOUR = 5;

		private static final int EXPIRE_MINUTE = 30;

	  private HashOperations<String, String, WorkDayRedis> hashOps;



		@Resource
		RedisTemplate redisTemplate;

		@PostConstruct
		void init(){
			hashOps = redisTemplate.opsForHash();
		}
	  public void addWorkDay(WorkDayRedis workDayRedis) {
			String currentDay = TradeUtil.getReadableDateTime(workDayRedis.getCreate_date()).split("T")
					[0];
		  hashOps.putIfAbsent(KEY, currentDay+workDayRedis.getFundCode(), workDayRedis);
		  redisTemplate.expireAt(KEY, TradeUtil.getDateOfSpecificTimeToday(EXPIRE_HOUR, EXPIRE_MINUTE));
	  }
	  public void updateWorkDay(WorkDayRedis workDayRedis) {
			String currentDay = TradeUtil.getReadableDateTime(workDayRedis.getCreate_date()).split("T")
					[0];
		  hashOps.put(KEY, currentDay+workDayRedis.getFundCode(), workDayRedis);
			redisTemplate.expire(KEY, TIMEOUT, TimeUnit.SECONDS);
	  }	  
	  public WorkDayRedis get(String currentDay) {
		  return hashOps.get(KEY, currentDay);
	  }
	  public long getNumberOfWorkDays() {
		  return hashOps.size(KEY);
	  }
	  public Map<String, WorkDayRedis> getAllEmployees() {
		  return hashOps.entries(KEY);
	  }
	  public long deleteWorkDays(String... ids) {
		  return hashOps.delete(KEY, (Object)ids);
	  }	  		  
}