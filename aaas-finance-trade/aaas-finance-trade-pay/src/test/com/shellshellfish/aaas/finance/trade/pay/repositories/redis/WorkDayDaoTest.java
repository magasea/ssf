package com.shellshellfish.aaas.finance.trade.pay.repositories.redis;

import static org.junit.Assert.*;

import com.shellshellfish.aaas.common.constants.RedisConstants;
import com.shellshellfish.aaas.common.utils.TradeUtil;
import com.shellshellfish.aaas.finance.trade.pay.model.WorkDayRedis;
import java.util.concurrent.TimeUnit;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by chenwei on 2018- 二月 - 02
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ActiveProfiles("local")
public class WorkDayDaoTest {

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

  @Test
  public void addWorkDay() throws Exception {
    WorkDayRedis workDayRedis = new WorkDayRedis();
    workDayRedis.setWorkDay("1987");
    redisTemplate.opsForHash().put("test","test", workDayRedis);
    workDayRedis.setWorkDay("2007");
    redisTemplate.opsForHash().put("test","test1", workDayRedis);
//    redisTemplate.expireAt("test", TradeUtil.getDateOfSpecificTimeToday(18, 20));
    redisTemplate.expire("test", 30, TimeUnit.SECONDS);
    while (true){
      Thread.sleep(1000);
      WorkDayRedis value1 = (WorkDayRedis) redisTemplate.opsForHash().get("test", "test");
      WorkDayRedis value2 = (WorkDayRedis) redisTemplate.opsForHash().get("test", "test1");
      if(null != value1 && value1.getWorkDay().equals("1987")){
        System.out.println("object1 still exists");
//        redisTemplate.expire("test", 1, TimeUnit.SECONDS);
      }else
        if(null != value2 && value2.getWorkDay().equals("2007")){
        System.out.println("object2 still exists");
        redisTemplate.expireAt("test", TradeUtil.getDateOfSpecificTimeToday(20, 58));
      }else{
        System.out.println("objects is expired");
        break;
      }
    }
  }

  @Test
  public void updateWorkDay() throws Exception {
  }

}