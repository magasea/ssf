package com.shellshellfish.aaas.userinfo.repositories.zhongzheng;

import com.shellshellfish.aaas.userinfo.UserInfoApp;
import com.shellshellfish.aaas.userinfo.model.DailyAmount;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Author pierre 18-1-12
 */
@RunWith(SpringRunner.class)
@ActiveProfiles("dev")
@SpringBootTest(classes = UserInfoApp.class, webEnvironment = WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration
public class MongDailyAmountRepositoryTest {

	@Autowired
	MongoDailyAmountRepository mongoDailyAmountRepository;


	@Test
	public void test() {
		Long userProdId = 27L;
		String fundCode = "000149.OF";
		List<DailyAmount> dailyAmountList = mongoDailyAmountRepository
				.findAllByUserProdIdAndFundCode(userProdId, fundCode, new Sort(
						Direction.DESC, "date"));
		Assert.assertNotNull("result is null", dailyAmountList);
		Assert.assertNotNull("index 0 is null", dailyAmountList.get(0));
		System.out.println(dailyAmountList.get(0));
	}

}


