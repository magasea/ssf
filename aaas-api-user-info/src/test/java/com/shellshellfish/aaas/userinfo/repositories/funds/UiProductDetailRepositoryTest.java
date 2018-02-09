package com.shellshellfish.aaas.userinfo.repositories.funds;

import com.shellshellfish.aaas.common.enums.TrdOrderStatusEnum;
import com.shellshellfish.aaas.common.utils.InstantDateUtil;
import com.shellshellfish.aaas.userinfo.UserInfoApp;
import com.shellshellfish.aaas.userinfo.model.dao.FundYieldRate;
import com.shellshellfish.aaas.userinfo.model.dao.UiProductDetail;
import com.shellshellfish.aaas.userinfo.repositories.mysql.UiProductDetailRepo;
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
import org.springframework.data.domain.Sort.Order;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Author pierre 18-1-12
 */
@RunWith(SpringRunner.class)
@ActiveProfiles("dev")
@SpringBootTest(classes = UserInfoApp.class, webEnvironment = WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration
public class UiProductDetailRepositoryTest {

	@Autowired
	UiProductDetailRepo uiProductDetailRepo;


	@Test
	public void findAllByUserProdIdAndStatusContainsTest() {

		Long userProdId = 113L;
		Integer[] a = new Integer[2];

		a[0] = 0;
		a[1] = 2;
		List<UiProductDetail> list = uiProductDetailRepo

				.findAllByUserProdIdAndStatusIn(userProdId, 0, 1);

		Assert.assertNotNull(list);
		Assert.assertEquals(2, list.size());

		for (UiProductDetail uiProductDetail : list) {
			Assert.assertNotNull(uiProductDetail);
			System.out.println(uiProductDetail.getStatus());
		}


	}
}


