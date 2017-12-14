package com.shellshellfish.aaas.userinfo.message;


import com.shellshellfish.aaas.common.message.order.TrdLog;
import com.shellshellfish.aaas.userinfo.model.dao.UiTrdLog;
import com.shellshellfish.aaas.userinfo.repositories.mongo.MongoUserTrdLogMsgRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BroadcastMessageConsumers {
    private static final Logger logger = LoggerFactory.getLogger(BroadcastMessageConsumers.class);



    @Autowired
    MongoUserTrdLogMsgRepo mongoUserTrdLogMsgRepo;

    public void receiveMessage(TrdLog trdLog) throws Exception {
        logger.info("Received fanout 1 message: " + trdLog);
        UiTrdLog uiTrdLog = new UiTrdLog();
        BeanUtils.copyProperties(trdLog, uiTrdLog);
        mongoUserTrdLogMsgRepo.save(uiTrdLog);
    }

}
