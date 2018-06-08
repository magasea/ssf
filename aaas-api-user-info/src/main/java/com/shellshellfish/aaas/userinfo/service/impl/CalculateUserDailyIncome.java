package com.shellshellfish.aaas.userinfo.service.impl;

import com.shellshellfish.aaas.common.utils.InstantDateUtil;
import com.shellshellfish.aaas.userinfo.model.PortfolioInfo;
import com.shellshellfish.aaas.userinfo.model.dao.MongoUserDailyIncome;
import com.shellshellfish.aaas.userinfo.model.dao.UiUser;
import com.shellshellfish.aaas.userinfo.model.dto.ProductsDTO;
import com.shellshellfish.aaas.userinfo.repositories.mysql.UiProductDetailRepo;
import com.shellshellfish.aaas.userinfo.repositories.mysql.UiProductRepo;
import com.shellshellfish.aaas.userinfo.repositories.mysql.UserInfoRepository;
import com.shellshellfish.aaas.userinfo.service.UserInfoService;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 * @Author pierre 18-3-12 每日计算用户累计收益
 */
@Service
public class CalculateUserDailyIncome {

    private static final Logger logger = LoggerFactory.getLogger(CalculateUserDailyIncome.class);
    @Autowired
    UserInfoRepository userInfoRepository;

    @Autowired
    UserInfoService userInfoService;

    @Autowired
    UiProductDetailRepo uiProductDetailRepo;

    @Autowired
    UiProductRepo uiProductRepo;

    @Autowired
    @Qualifier("zhongZhengMongoTemplate")
    MongoTemplate zhongZhengMongoTemplate;


    @Value("${daily-finance-calculate-thread}")

    private Integer threadNum;
    private ExecutorService threadPool = Executors.newCachedThreadPool();

    public void dailyCalculateIncome(LocalDate date) {
        List<UiUser> users = userInfoRepository.findAll();
        if (CollectionUtils.isEmpty(users)) {
            logger.info("user list is empty.");
            return;
        }
        int size = users.size();
        Long start = System.currentTimeMillis();
        logger.info("calculate daily income start  startTime:{}", start);
        if (size <= threadNum * 2) {
            calculateUserDailyIncome(users, date);
        } else {
            int averageSize = size / threadNum;
            CountDownLatch countDownLatch = new CountDownLatch(threadNum);
            for (int i = 0; i < threadNum; i++) {
                int fromIndex = i * averageSize;
                int toIndex;
                if (i == threadNum - 1) {
                    toIndex = size;
                } else {
                    toIndex = (i + 1) * averageSize;
                }
                threadPool.submit(() -> {
                            try {
                                calculateUserDailyIncome(users.subList(fromIndex, toIndex), date);
                            } catch (Exception e) {
                                logger.error("calculate daily income error  date :{}", date, e);
                            } finally {
                                countDownLatch.countDown();
                            }
                        },
                        countDownLatch);
            }
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                logger.error("calculate daily income error   date :{}", date, e);
            }
            Long end = System.currentTimeMillis();
            logger.info("calculate daily income finished   endTime:{}", end);
            logger
                    .info("calculate daily income cost  {} mills", end - start);
        }
    }

    public void calcualteUserDailyIncome(String userUuid, LocalDate date) {
        UiUser uiUser = userInfoRepository.findByUuid(userUuid);
        calculateUserDailyIncome(uiUser, date);
    }


    private void calculateUserDailyIncome(List<UiUser> uiUserList, LocalDate date) {
        logger.info("{} start", Thread.currentThread().getName());
        for (UiUser uiUser : uiUserList) {
            calculateUserDailyIncome(uiUser, date);
        }
        logger.info("{} end", Thread.currentThread().getName());
    }

    /**
     * 计算用户每日累计收益收益
     */
    private void calculateUserDailyIncome(UiUser uiUser, LocalDate date) {
        List<ProductsDTO> productsDTOList;
        try {
            productsDTOList = userInfoService.findProductInfos(uiUser.getUuid());
        } catch (Exception e) {
            logger.error("获取用户产品组合失败!", e);
            return;
        }
        for (ProductsDTO productsDTO : productsDTOList) {
            //创建时间在计算时间之前
            if (LocalDateTime
                    .ofInstant(Instant.ofEpochMilli(productsDTO.getCreateDate()), ZoneId.systemDefault())
                    .toLocalDate().isAfter(date)) {
                continue;
            }
            try {
                PortfolioInfo portfolioInfo = userInfoService
                        .getChicombinationAssets(uiUser.getUuid(), uiUser.getId(), productsDTO, date);
                MongoUserDailyIncome mongoUserDailyIncome = new MongoUserDailyIncome();
                mongoUserDailyIncome.setUserId(uiUser.getId());
                mongoUserDailyIncome.setUserProdId(productsDTO.getId());
                mongoUserDailyIncome.setDailyIncome(portfolioInfo.getDailyIncome());
                mongoUserDailyIncome.setAccumulativeIncome(portfolioInfo.getTotalIncome());
                mongoUserDailyIncome.setCreateDate(System.currentTimeMillis());
                mongoUserDailyIncome.setUpdateDate(System.currentTimeMillis());
                mongoUserDailyIncome.setCreateDateStr(InstantDateUtil.format(date));
                findAndModifyUserDailyIncome(mongoUserDailyIncome);
            } catch (Exception e) {
                logger
                        .error("calculate user daily income failed   userId:{}  userProdId:{}", uiUser.getId(),
                                productsDTO.getId(), e);
                return;
            }

        }
    }

    private void findAndModifyUserDailyIncome(MongoUserDailyIncome mongoUserDailyIncome) {
        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(mongoUserDailyIncome.getUserId()))
                .addCriteria(Criteria.where("userProdId").is(mongoUserDailyIncome.getUserProdId()))
                .addCriteria(Criteria.where("createDateStr").is(mongoUserDailyIncome.getCreateDateStr()));

        Update update = new Update();
        update.set("createDate", mongoUserDailyIncome.getCreateDate());
        update.set("updateDate", mongoUserDailyIncome.getUpdateDate());
        update.set("dailyIncome", Optional.ofNullable(mongoUserDailyIncome.getDailyIncome()).orElse(
                BigDecimal.ZERO));
        update.set("accumulativeIncome",
                Optional.ofNullable(mongoUserDailyIncome.getAccumulativeIncome()).orElse(
                        BigDecimal.ZERO));
        zhongZhengMongoTemplate
                .findAndModify(query, update, FindAndModifyOptions.options().upsert(true),
                        MongoUserDailyIncome.class);
    }
}
