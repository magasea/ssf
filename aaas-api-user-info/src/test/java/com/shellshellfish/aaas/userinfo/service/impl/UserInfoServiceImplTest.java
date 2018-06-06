package com.shellshellfish.aaas.userinfo.service.impl;

import com.shellshellfish.aaas.finance.trade.pay.PayRpcServiceGrpc.PayRpcServiceFutureStub;
import com.shellshellfish.aaas.userinfo.UserInfoApp;
import com.shellshellfish.aaas.userinfo.model.PortfolioInfo;
import com.shellshellfish.aaas.userinfo.model.dao.UiProducts;
import com.shellshellfish.aaas.userinfo.model.dto.ProductsDTO;
import com.shellshellfish.aaas.userinfo.repositories.mysql.UiProductRepo;
import com.shellshellfish.aaas.userinfo.service.UserInfoService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = UserInfoApp.class)
@ActiveProfiles(profiles = "test")

public class UserInfoServiceImplTest {
    PayRpcServiceFutureStub payRpcServiceFutureStub;

    @Autowired
    UserInfoService userInfoService;

    @Autowired
    UiProductRepo uiProductRepo;


    @Test
    public void queryTrdResultByOrderDetailId() throws Exception {
        Long userId = 5605L;
        Long orderDetailId = 971L;
        userInfoService.queryTrdResultByOrderDetailId(userId, orderDetailId);
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void getUserInfoBase() throws Exception {
    }

    @Test
    public void getUserInfoAssectsBrief() throws Exception {
    }

    @Test
    public void getUserInfoBankCards() throws Exception {
    }

    @Test
    public void getUserPortfolios() throws Exception {
    }

    @Test
    public void getUserInfoBankCard() throws Exception {
    }

    @Test
    public void createBankcard() throws Exception {
    }

    @Test
    public void getAssetDailyRept() throws Exception {
    }

    @Test
    public void addAssetDailyRept() throws Exception {
    }

    @Test
    public void getUserSysMsg() throws Exception {
    }

    @Test
    public void getUserPersonMsg() throws Exception {
    }

    @Test
    public void updateUserPersonMsg() throws Exception {
    }

    @Test
    public void getUserTradeLogs() throws Exception {
    }

    @Test
    public void getUserInfoFriendRules() throws Exception {
    }

    @Test
    public void getCompanyInfo() throws Exception {
    }

    @Test
    public void TestGetChicombinationAssets() throws Exception {
        String uuid = "3a4401ae-d6f9-49ee-97e2-ce7ebf122822";
        Long userId = 5628L;
        Long prodId = 310L;
        Optional<UiProducts> products = uiProductRepo.findById(prodId);

        ProductsDTO productsDTO = new ProductsDTO();
        BeanUtils.copyProperties(products.get(), productsDTO);
        LocalDate date = LocalDate.of(2018, 06, 04);

        PortfolioInfo portfolioInfo = userInfoService.getChicombinationAssets(uuid, userId, productsDTO, date);
        System.out.println(portfolioInfo);
    }

}