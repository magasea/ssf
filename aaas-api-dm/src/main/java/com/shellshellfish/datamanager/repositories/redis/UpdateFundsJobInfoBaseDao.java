package com.shellshellfish.datamanager.repositories.redis;

import com.shellshellfish.aaas.common.constants.RedisConstants;
import com.shellshellfish.aaas.userinfo.model.redis.UserBaseInfoRedis;
import com.shellshellfish.datamanager.model.redis.UpdateFundsJobBaseInfoRedis;
import java.util.concurrent.TimeUnit;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class UpdateFundsJobInfoBaseDao {

	private static final String KEY = RedisConstants.DATAMANAGE_KEY + "UpdateFundsJob";

	private static final int timeout = 7200; //记得用timeunit 为second

	private HashOperations<String, String, UpdateFundsJobBaseInfoRedis> hashOps;

	@Resource
	RedisTemplate redisTemplate;

	@PostConstruct
	void init() {
		hashOps = redisTemplate.opsForHash();
	}

	@Transactional
	public void addUserBaseInfo(UpdateFundsJobBaseInfoRedis updateFundsJobBaseInfoRedis) {
		hashOps.putIfAbsent(KEY , updateFundsJobBaseInfoRedis.getFileUpdateTime().toString(),
				updateFundsJobBaseInfoRedis);
		redisTemplate.expire(KEY + userBaseInfoRedis.getUuid(), timeout, TimeUnit.SECONDS);
	}

	@Transactional
	public void updateUserBaseInfo(UpdateFundsJobBaseInfoRedis updateFundsJobBaseInfoRedis) {
		hashOps.put(KEY + userBaseInfoRedis.getUuid(), userBaseInfoRedis.getUuid(), userBaseInfoRedis);
		redisTemplate.expire(KEY + userBaseInfoRedis.getUuid(), timeout, TimeUnit.SECONDS);

	}

	public UserBaseInfoRedis get(String userUUID) {
		return hashOps.get(KEY + userUUID, userUUID);
	}

}