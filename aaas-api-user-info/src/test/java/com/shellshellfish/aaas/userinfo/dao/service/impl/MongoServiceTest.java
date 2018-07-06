package com.shellshellfish.aaas.userinfo.dao.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.shellshellfish.aaas.common.utils.TradeUtil;
import com.shellshellfish.aaas.userinfo.UserInfoApp;
import com.shellshellfish.aaas.userinfo.model.dao.MongoCaculateBase;
import com.shellshellfish.aaas.userinfo.service.FundTradeApiService;
import java.math.BigDecimal;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(value = SpringRunner.class)
@SpringBootTest(classes = UserInfoApp.class, webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles(profiles = "pretest")
public class MongoServiceTest {

	@Autowired
	MongoTemplate mongoTemplate;

	@Test
	public void fundAndModify() throws Exception {

		Query querySub = new Query();
		querySub.addCriteria(Criteria.where("user_prod_id").is(279L).and("fund_code").is
				("004399.OF").and("outside_order_id").is("6222021560000015290356403092231"));
		List<MongoCaculateBase> mongoCaculateBaseList = mongoTemplate.find(querySub, MongoCaculateBase
				.class);
		mongoCaculateBaseList.get(0).setLastModifiedDate(TradeUtil.getUTCTime());

//		mongoTemplate.save(mongoCaculateBaseList.get(0));
		Update update = new Update();

		update.set("last_modified_date", TradeUtil.getUTCTime());
		System.out.println("set the modify time as:"+ update.getUpdateObject().toJson());
		mongoTemplate.findAndModify(querySub, update, MongoCaculateBase.class);
	}


}
