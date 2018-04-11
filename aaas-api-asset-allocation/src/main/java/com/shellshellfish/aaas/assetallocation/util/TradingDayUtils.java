package com.shellshellfish.aaas.assetallocation.util;

import com.shellshellfish.aaas.assetallocation.entity.MongoTradingDay;
import com.shellshellfish.aaas.assetallocation.mapper.MongoTradingDayRepository;
import com.shellshellfish.aaas.common.utils.InstantDateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.*;

/**
 * 所有交易日的数据
 */
@Component
public class TradingDayUtils implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(TradingDayUtils.class);

    private static HashSet<String> dateSet;

    @Autowired
    MongoTradingDayRepository mongoTradingDayRepository;

    @Override
    public void run(String... strings) {
        logger.debug("start init {}", TradingDayUtils.class);
        List<MongoTradingDay> mongoTradingDays = mongoTradingDayRepository.findAllBy();
        dateSet = new HashSet<>(mongoTradingDays.size());
        for (MongoTradingDay mongoTradingDay : mongoTradingDays) {
            dateSet.add(mongoTradingDay.getDate());
        }
        logger.debug("init {} , size of dateSet :{}", TradingDayUtils.class, dateSet.size());
    }

    public static boolean isTradingDay(String date) {
        return dateSet.contains(date);
    }

    public static boolean isTradingDay(String date, String pattern) {
        return isTradingDay(InstantDateUtil.format(date, pattern));
    }

    public static boolean isTradingDay(LocalDate localDate) {
        return isTradingDay(InstantDateUtil.format(localDate));
    }
}
