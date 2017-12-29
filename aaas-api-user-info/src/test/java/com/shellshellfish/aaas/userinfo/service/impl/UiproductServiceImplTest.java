package com.shellshellfish.aaas.userinfo.service.impl;

import com.shellshellfish.aaas.userinfo.UserInfoApp;
import com.shellshellfish.aaas.userinfo.model.dto.UiProductDetailDTO;
import com.shellshellfish.aaas.userinfo.service.UiProductService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = UserInfoApp.class)
@ActiveProfiles(profiles = "dev")
public class UiproductServiceImplTest {


	@Autowired
	UiProductService uiProductService;

	@Test
	public void getProductDetailsByProdIdTest() throws Exception {
		Long productId = 41L;
		List<UiProductDetailDTO> result =uiProductService.getProductDetailsByProdId(productId);
		for (int i=0;i<result.size();i++){
			System.out.println(result.get(i));
		}
		Assert.notEmpty(result,"不为空啊");
	}

}