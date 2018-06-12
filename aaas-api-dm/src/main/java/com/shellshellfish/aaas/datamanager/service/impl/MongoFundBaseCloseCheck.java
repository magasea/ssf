package com.shellshellfish.aaas.datamanager.service.impl;

import com.shellshellfish.aaas.datamanager.model.FundBaseClose;
import com.shellshellfish.aaas.datamanager.repositories.mongo.MongoFundBaseCloseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author pierre.chen
 * @Date 18-6-2
 */
@Service
public class MongoFundBaseCloseCheck {

    @Autowired
    MongoFundBaseCloseRepository mongoFundBaseCloseRepository;

    private static final Logger logger = LoggerFactory.getLogger(MongoFundBaseCloseCheck.class);

    private List<FundBaseClose> toUpdate = null;

    @Autowired
    MongoTemplate mongoTemplate;

    public void check() {
        getToUpdate();
        update();
    }


    /**
     * 获取所有H11025CSI为空的数据，并用最近的前一天补充完整
     *
     * @return
     */
    private List<FundBaseClose> getToUpdate() {
        List<FundBaseClose> fundBaseCloseList = mongoFundBaseCloseRepository.findByQueryDateBetween(0L, System
                .currentTimeMillis(), Sort.by(Sort.Direction.DESC, "querydatestr"));
        if (CollectionUtils.isEmpty(fundBaseCloseList)) {
            logger.error("fundbaseclose is empty ");
            return null;
        }

        toUpdate = new ArrayList<>(fundBaseCloseList.size() * (2 / 7));

        for (int i = 0; i < fundBaseCloseList.size(); i++) {
            FundBaseClose fundBaseClose = fundBaseCloseList.get(i);

            if (fundBaseClose.getH11025CSI() != null)
                continue;

            FundBaseClose newInstance = fundBaseClose;

            int index = i;
            while (fundBaseClose.getH11025CSI() == null && index >= 0) {
                fundBaseClose = fundBaseCloseList.get(index);
                index--;
            }

            if (fundBaseClose.getH11025CSI() == null)
                continue;

            newInstance.setH11025CSI(fundBaseClose.getH11025CSI());
            toUpdate.add(newInstance);
        }
        return toUpdate;
    }

    private void update() {
        mongoFundBaseCloseRepository.saveAll(toUpdate);
    }

    public static void main(String[] args) {
        List a = new ArrayList(5);
        System.out.println(CollectionUtils.isEmpty(a));
    }
}
