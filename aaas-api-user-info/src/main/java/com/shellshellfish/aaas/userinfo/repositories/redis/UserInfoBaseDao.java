package com.shellshellfish.aaas.userinfo.repositories.redis;
import com.shellshellfish.aaas.common.constants.RedisConstants;
import com.shellshellfish.aaas.userinfo.model.redis.UserBaseInfoRedis;
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
public class UserInfoBaseDao {
	  private static final String KEY = RedisConstants.TRADE_PAY_KEY+"UserInfo";
	  
		private static final int timeout = 7200; //记得用timeunit 为second

	  private HashOperations<String, String, UserBaseInfoRedis> hashOps;

	  @Resource
		RedisTemplate redisTemplate;

	  @PostConstruct
		void init(){
	  	hashOps = redisTemplate.opsForHash();
		}

	  public void addUserBaseInfo(UserBaseInfoRedis userBaseInfoRedis) {
		  hashOps.putIfAbsent(KEY, userBaseInfoRedis.getUuid(), userBaseInfoRedis);
			redisTemplate.expire(KEY, timeout, TimeUnit.SECONDS);
	  }
	  public void updateUserBaseInfo(UserBaseInfoRedis userBaseInfoRedis) {
		  hashOps.put(KEY, userBaseInfoRedis.getUuid(), userBaseInfoRedis);
		  redisTemplate.expire(KEY, timeout, TimeUnit.SECONDS);

	  }	  
	  public UserBaseInfoRedis get(String userUUID) {
		  return hashOps.get(KEY, userUUID);
	  }
	  public long getNumberOfUserBaseInfoInRedis() {
		  return hashOps.size(KEY);
	  }
	  public Map<String, UserBaseInfoRedis> getAllUserBaseInfoes() {
		  return hashOps.entries(KEY);
	  }
	  public long deleteUserBaseInfos(String... userUUIDs) {
		  return hashOps.delete(KEY, (Object)userUUIDs);
	  }	  		  
}