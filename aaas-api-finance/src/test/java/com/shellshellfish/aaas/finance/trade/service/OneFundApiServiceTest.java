package com.shellshellfish.aaas.finance.trade.service;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shellshellfish.aaas.finance.FinanceApp;
import com.shellshellfish.aaas.finance.trade.model.FundIncome;
import com.shellshellfish.aaas.finance.trade.model.*;
import com.shellshellfish.aaas.finance.trade.service.impl.OneFundApiService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = FinanceApp.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("pretest")
//@Ignore
public class OneFundApiServiceTest {
    private static final Logger logger = LoggerFactory.getLogger(OneFundApiService.class);

    @Autowired
    private OneFundApiService oneFundApiService;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Test
    public void testOpenAccount() throws Exception {
        oneFundApiService.openAccount("shellshellfish", "张飞", "13816629390", "612727198301116032", "4367421214584329558", "005");
    }

    @Test
    public void testGetOneFundInfo() throws Exception {
        String json = oneFundApiService.getFundInfo("000072");
        logger.debug("json: {}", json);
    }

    @Test
    public void testGetAllFundsInfo() throws Exception {
        List<String> allFunds = oneFundApiService.getAllFundsInfo();
        logger.debug("allFunds: {}", allFunds);
    }

    @Test
    public void testWriteFundToMongoDb() throws Exception {
        String json = oneFundApiService.getFundInfo("000072");
        oneFundApiService.writeFundToMongoDb(json);
    }

    @Test
    public void testWriteAllFundsToMongoDb() throws Exception {
        List<String> funds = oneFundApiService.getAllFundsInfo();
        oneFundApiService.writeAllFundsToMongoDb(funds);
    }

    @Test
    public void testWriteAllTradeRateToMongoDb() throws Exception {
        oneFundApiService.writeAllTradeRateToMongoDb();
    }

    @Test
    public void testBuyFund() throws Exception {
        BuyFundResult result = oneFundApiService.buyFund("shellshellfish", "33346", BigDecimal.valueOf(1.09d), "201712-" + UUID.randomUUID(), "000614.OF");

        mongoTemplate.save(result, "buyfund");
    }

    @Test
    public void testSellFund() throws Exception {
        SellFundResult result = oneFundApiService.sellFund("shellshellfish", 16, "201712-" + UUID.randomUUID(), "33346", "000407");
        mongoTemplate.save(result, "sellfund");
    }

    @Test
    public void testGetExamContent() throws JsonProcessingException {
        String result = oneFundApiService.getExamContent();
        mongoTemplate.save(result, "exam");
    }

    @Test
    public void testCommitFakeAnswer() throws JsonProcessingException {
        String result = oneFundApiService.commitFakeAnswer("shellshellfish");
        mongoTemplate.save(result, "fakeAnswerResult");
    }

    @Test
    public void testCommitRisk() throws JsonProcessingException {
        String result = oneFundApiService.commitRisk("shellshellfish");
        mongoTemplate.save(result, "riskResult");
    }

    @Test
    public void testGetUserRiskList() throws JsonProcessingException {
        oneFundApiService.getUserRiskList("shellshellfish");
    }

    @Test
    public void testGetApplyList() throws JsonProcessingException {
        String result = oneFundApiService.getAllApplyList("shellshellfish");
        mongoTemplate.save(result, "appplyResult");
    }

    @Test
    public void testGetApplyResultByApplySerial() throws Exception {
        ApplyResult applyResult = oneFundApiService.getApplyResultByApplySerial("shellshellfish", "20171207000676");
        assertNotNull(applyResult);
        logger.info(new ObjectMapper().writeValueAsString(applyResult));
    }

    @Test
    public void testGetApplyResultByOutsideOrderNo() throws JsonProcessingException {
        ApplyResult applyResult = oneFundApiService.getApplyResultByOutsideOrderNo("shellshellfish", "201712-17a7807d-5d40-4681-adf3-23f");
        assertNotNull(applyResult);
        logger.info(new ObjectMapper().writeValueAsString(applyResult));
    }

    @Test
    public void testGetConfirmList() throws JsonProcessingException {
        String result = oneFundApiService.getAllConfirmList("shellshellfish");
        mongoTemplate.save(result, "confirmResult");
    }

    @Test
    public void testGetConfirmResultByApplySerial() throws JsonProcessingException {
        ConfirmResult confirmResult = oneFundApiService.getConfirmResultByApplySerial("shellshellfish", "20171212000399");
        assertNotNull(confirmResult);
        logger.info(new ObjectMapper().writeValueAsString(confirmResult));
    }

    @Test
    public void testGetConfirmResultByOutsideOrderNo() throws JsonProcessingException {
        ConfirmResult confirmResult = oneFundApiService.getConfirmResultByOutsideOrderNo("shellshellfish", "201712-adedd068-ced2-46e5-a00b-f5f");
        assertNotNull(confirmResult);
        logger.info(new ObjectMapper().writeValueAsString(confirmResult));
    }

    @Test
    public void testGetUserBankList() throws Exception {
        List<FundInfo> fundInfos = mongoTemplate.findAll(FundInfo.class, "fundInfo");
        List<String> lines = new ArrayList<>();
        for (FundInfo info : fundInfos) {
            List<UserBank> userBanks = oneFundApiService.getUserBank(info.getFundcode());
            if (!userBanks.get(0).getBankName().equals("建设银行")) {
                logger.info(new ObjectMapper().writeValueAsString(userBanks));
                break;
            }
        }
    }

    @Test
    public void testWriteFundCodeAndFundNameToCsvFile() throws IOException {
        List<FundInfo> fundInfos = mongoTemplate.findAll(FundInfo.class, "fundInfo");
        List<String> lines = new ArrayList<>();
        for (FundInfo info : fundInfos) {
            lines.add(info.getFundcode() + ", " + info.getFundname());
        }

        Path file = Paths.get("d:\\zhongzheng99.csv");
        Files.write(file, lines, Charset.forName("UTF-8"));
    }

    @Test
    public void testOpenAccountResult() throws JsonProcessingException {
        String json = "{\"status\":1,\"errno\":\"0000\",\"msg\":\"\\u6210\\u529f\",\"data\":{\"custno\":\"88048\",\"fundacco\":\"*21000033346\",\"tradeacco\":\"33346\",\"applyserial\":\"20171206000967\"}}";
        JSONObject jsonObject = JSONObject.parseObject(json);
        OpenAccountResult openAccountResult = jsonObject.getObject("data", OpenAccountResult.class);
        assertNotNull(openAccountResult);
        logger.info("openAccountResult: {}", (new ObjectMapper()).writeValueAsString(openAccountResult));
    }

    @Test
    public void testBuyFundResult() throws JsonProcessingException {
        String json = "{ \"status\": 1, \"errno\":'0000', 'msg':'成功', \"data\": {\n" +
                "\"applyserial\": \"2017031700200221\", \"capitalmode\": \"r\", \"requestdate\": 20170317 , \"outsideorderno\": \"test000001\", \"kkstat\": \"2\" } }";
        JSONObject jsonObject = JSONObject.parseObject(json);
        BuyFundResult buyFundResult = jsonObject.getObject("data", BuyFundResult.class);
        assertNotNull(buyFundResult);
        logger.info("buyFundResult: {}", (new ObjectMapper()).writeValueAsString(buyFundResult));
    }

    @Test
    public void testSellFundResult() throws JsonProcessingException {
        String json = "{ \"status\": 1, \"errno\":'0000', \"msg\":\"成功\", \"data\":{ \"applyserial\":\"20170317000057\", \"acceptdate\":\"20170317\", \"requestdate\":\"20170320\", \"resubdate\":\"\", \"outsideorderno\":\"test884961162\" }}";
        JSONObject jsonObject = JSONObject.parseObject(json);
        SellFundResult sellFundResult = jsonObject.getObject("data", SellFundResult.class);
        assertNotNull(sellFundResult);
        logger.info("sellFundResult: {}", (new ObjectMapper()).writeValueAsString(sellFundResult));
    }

    @Test
    public void testGetTradeLimits() throws Exception {
        List<TradeLimitResult> tradeLimits = oneFundApiService.getTradeLimits("000072", "022");
        assertNotNull(tradeLimits);
    }

    @Test
    public void testGetDiscount() throws Exception {
        BigDecimal discount = oneFundApiService.getDiscount("000590", "022");
        logger.info("{}", discount);
    }

    @Test
    public void testGetRate() throws Exception {
        BigDecimal rate = oneFundApiService.getRate("000590", "022");
        logger.info("{}", rate);
    }

    @Test
    public void testCalcPoundage() throws Exception {
        BigDecimal totalAmount = BigDecimal.valueOf(108.8d);
        BigDecimal rate = oneFundApiService.getRate("000590", "022");
        BigDecimal discount = oneFundApiService.getDiscount("000590", "022");
        BigDecimal poundage = oneFundApiService.calcPoundage(totalAmount, rate, discount);

        logger.info("{}", poundage);
    }

    @Test
    public void testCalcDiscountPoundage() throws Exception {
        BigDecimal totalAmount = BigDecimal.valueOf(100d);
        BigDecimal rate = BigDecimal.valueOf(0.04);
        BigDecimal discount = BigDecimal.valueOf(0.5);
        BigDecimal poundage = oneFundApiService.calcDiscountPoundage(totalAmount, rate, discount);
        poundage = poundage.setScale(1);
        assertEquals(poundage, BigDecimal.valueOf(2.0));
        logger.info("{}", poundage);
    }

    @Test
    public void testGetBankCardLimitation() throws JsonProcessingException {
        BankCardLimitation bankCardLimitation = oneFundApiService.getBankCardLimitation("中国银行");
        assertEquals(BigDecimal.valueOf(50000), bankCardLimitation.getPerTrans());
        assertEquals(BigDecimal.valueOf(50000), bankCardLimitation.getPerDay());
        logger.info("{}", new ObjectMapper().writeValueAsString(bankCardLimitation));
    }

    @Test
    public void testGetFundNotice() throws Exception {
        List<FundNotice> fundNotices = oneFundApiService.getFundNotices("000614");
        logger.info("{}", new ObjectMapper().writeValueAsString(fundNotices));
    }

    @Test
    public void testGetBonusList() throws Exception {
        List<BonusInfo> bonusInfoList = oneFundApiService.getBonusList("shellshellfish", "002163", "20170101");
        logger.info(new ObjectMapper().writeValueAsString(bonusInfoList));
    }

    @Test
    public void testGetFundShare() throws Exception {
        FundShare fundShare = oneFundApiService.getFundShare("shellshellfish", "002163");
        logger.info(new ObjectMapper().writeValueAsString(fundShare));
    }

    @Test
    public void testGetFundIncome() throws Exception {
        long start = System.currentTimeMillis();
        FundIncome fundIncome = oneFundApiService.getFundIncome("shellshellfish", "002163");
        long end = System.currentTimeMillis();
        logger.info("{}", end - start);
        logger.info(new ObjectMapper().writeValueAsString(fundIncome));
    }

    @Test
    public void testWriteAllTradeDiscountToMongodDb() throws Exception {
        oneFundApiService.writeAllTradeDiscountToMongodDb();
    }

    @Test
    public void testWriteAllTradeLimitToMongoDb() throws Exception {
        oneFundApiService.writeAllTradeLimitToMongoDb();
    }

    @Test
    public void testFillTradeRateResult() throws JsonProcessingException {
        String json = "{\n" +
                "    \"status\": 1,\n" +
                "    \"errno\": \"0000\",\n" +
                "    \"msg\": \"\\u6210\\u529f\",\n" +
                "    \"data\": [{\n" +
                "        \"FUND_CODE\": \"000001\",\n" +
                "        \"ISS_STARTDATE\": \"2001-11-28 00:00:00\",\n" +
                "        \"ISS_ENDDATE\": \"2001-12-12 00:00:00\",\n" +
                "        \"CHNG_MIN_TERM_MARK\": \"\\u524d\\u7aef\\u8ba4\\u8d2d\\u8d39\",\n" +
                "        \"PERT_VAL_LOW_LIM\": 0,\n" +
                "        \"PERT_VAL_UP_LIM\": 0,\n" +
                "        \"PERT_VAL_UNIT_MARK\": \"\\u4e07\\u5143\",\n" +
                "        \"CHAG_RATE_UP_LIM\": 1,\n" +
                "        \"CHAG_RATE_UNIT_MARK\": \"%\",\n" +
                "        \"HLD_TERM_LOW_LIM\": 0,\n" +
                "        \"HLD_TERM_UP_LIM\": 0,\n" +
                "        \"HLD_TERM_UNIT_MARK\": \"\"\n" +
                "    }, {\n" +
                "        \"FUND_CODE\": \"000001\",\n" +
                "        \"ISS_STARTDATE\": \"2001-11-28 00:00:00\",\n" +
                "        \"ISS_ENDDATE\": \"2001-12-12 00:00:00\",\n" +
                "        \"CHNG_MIN_TERM_MARK\": \"\\u57fa\\u91d1\\u6258\\u7ba1\\u8d39\",\n" +
                "        \"PERT_VAL_LOW_LIM\": 0,\n" +
                "        \"PERT_VAL_UP_LIM\": 0,\n" +
                "        \"PERT_VAL_UNIT_MARK\": \"\",\n" +
                "        \"CHAG_RATE_UP_LIM\": 0.25,\n" +
                "        \"CHAG_RATE_UNIT_MARK\": \"%\",\n" +
                "        \"HLD_TERM_LOW_LIM\": 0,\n" +
                "        \"HLD_TERM_UP_LIM\": 0,\n" +
                "        \"HLD_TERM_UNIT_MARK\": \"\"\n" +
                "    }, {\n" +
                "        \"FUND_CODE\": \"000001\",\n" +
                "        \"ISS_STARTDATE\": \"2001-11-28 00:00:00\",\n" +
                "        \"ISS_ENDDATE\": \"2001-12-12 00:00:00\",\n" +
                "        \"CHNG_MIN_TERM_MARK\": \"\\u57fa\\u91d1\\u7ba1\\u7406\\u8d39\",\n" +
                "        \"PERT_VAL_LOW_LIM\": 0,\n" +
                "        \"PERT_VAL_UP_LIM\": 0,\n" +
                "        \"PERT_VAL_UNIT_MARK\": \"\",\n" +
                "        \"CHAG_RATE_UP_LIM\": 1.5,\n" +
                "        \"CHAG_RATE_UNIT_MARK\": \"%\",\n" +
                "        \"HLD_TERM_LOW_LIM\": 0,\n" +
                "        \"HLD_TERM_UP_LIM\": 0,\n" +
                "        \"HLD_TERM_UNIT_MARK\": \"\"\n" +
                "    }, {\n" +
                "        \"FUND_CODE\": \"000001\",\n" +
                "        \"ISS_STARTDATE\": \"2001-11-28 00:00:00\",\n" +
                "        \"ISS_ENDDATE\": \"2001-12-12 00:00:00\",\n" +
                "        \"CHNG_MIN_TERM_MARK\": \"\\u65e5\\u5e38\\u7533\\u8d2d\\u8d39\",\n" +
                "        \"PERT_VAL_LOW_LIM\": 0,\n" +
                "        \"PERT_VAL_UP_LIM\": 100,\n" +
                "        \"PERT_VAL_UNIT_MARK\": \"\\u4e07\\u5143\",\n" +
                "        \"CHAG_RATE_UP_LIM\": 1.5,\n" +
                "        \"CHAG_RATE_UNIT_MARK\": \"%\",\n" +
                "        \"HLD_TERM_LOW_LIM\": 0,\n" +
                "        \"HLD_TERM_UP_LIM\": 0,\n" +
                "        \"HLD_TERM_UNIT_MARK\": \"\"\n" +
                "    }, {\n" +
                "        \"FUND_CODE\": \"000001\",\n" +
                "        \"ISS_STARTDATE\": \"2001-11-28 00:00:00\",\n" +
                "        \"ISS_ENDDATE\": \"2001-12-12 00:00:00\",\n" +
                "        \"CHNG_MIN_TERM_MARK\": \"\\u65e5\\u5e38\\u8d4e\\u56de\\u8d39\",\n" +
                "        \"PERT_VAL_LOW_LIM\": 0,\n" +
                "        \"PERT_VAL_UP_LIM\": 0,\n" +
                "        \"PERT_VAL_UNIT_MARK\": \"\",\n" +
                "        \"CHAG_RATE_UP_LIM\": 0.5,\n" +
                "        \"CHAG_RATE_UNIT_MARK\": \"%\",\n" +
                "        \"HLD_TERM_LOW_LIM\": 0,\n" +
                "        \"HLD_TERM_UP_LIM\": 0,\n" +
                "        \"HLD_TERM_UNIT_MARK\": \"\\u5e74\"\n" +
                "    }, {\n" +
                "        \"FUND_CODE\": \"000001\",\n" +
                "        \"ISS_STARTDATE\": \"2001-11-28 00:00:00\",\n" +
                "        \"ISS_ENDDATE\": \"2001-12-12 00:00:00\",\n" +
                "        \"CHNG_MIN_TERM_MARK\": \"\\u65e5\\u5e38\\u7533\\u8d2d\\u8d39\",\n" +
                "        \"PERT_VAL_LOW_LIM\": 100,\n" +
                "        \"PERT_VAL_UP_LIM\": 500,\n" +
                "        \"PERT_VAL_UNIT_MARK\": \"\\u4e07\\u5143\",\n" +
                "        \"CHAG_RATE_UP_LIM\": 1.2,\n" +
                "        \"CHAG_RATE_UNIT_MARK\": \"%\",\n" +
                "        \"HLD_TERM_LOW_LIM\": 0,\n" +
                "        \"HLD_TERM_UP_LIM\": 0,\n" +
                "        \"HLD_TERM_UNIT_MARK\": \"\"\n" +
                "    }, {\n" +
                "        \"FUND_CODE\": \"000001\",\n" +
                "        \"ISS_STARTDATE\": \"2001-11-28 00:00:00\",\n" +
                "        \"ISS_ENDDATE\": \"2001-12-12 00:00:00\",\n" +
                "        \"CHNG_MIN_TERM_MARK\": \"\\u65e5\\u5e38\\u7533\\u8d2d\\u8d39\",\n" +
                "        \"PERT_VAL_LOW_LIM\": 500,\n" +
                "        \"PERT_VAL_UP_LIM\": 1000,\n" +
                "        \"PERT_VAL_UNIT_MARK\": \"\\u4e07\\u5143\",\n" +
                "        \"CHAG_RATE_UP_LIM\": 0.8,\n" +
                "        \"CHAG_RATE_UNIT_MARK\": \"%\",\n" +
                "        \"HLD_TERM_LOW_LIM\": 0,\n" +
                "        \"HLD_TERM_UP_LIM\": 0,\n" +
                "        \"HLD_TERM_UNIT_MARK\": \"\"\n" +
                "    }, {\n" +
                "        \"FUND_CODE\": \"000001\",\n" +
                "        \"ISS_STARTDATE\": \"2001-11-28 00:00:00\",\n" +
                "        \"ISS_ENDDATE\": \"2001-12-12 00:00:00\",\n" +
                "        \"CHNG_MIN_TERM_MARK\": \"\\u65e5\\u5e38\\u7533\\u8d2d\\u8d39\",\n" +
                "        \"PERT_VAL_LOW_LIM\": 1000,\n" +
                "        \"PERT_VAL_UP_LIM\": 0,\n" +
                "        \"PERT_VAL_UNIT_MARK\": \"\\u4e07\\u5143\",\n" +
                "        \"CHAG_RATE_UP_LIM\": 1000,\n" +
                "        \"CHAG_RATE_UNIT_MARK\": \"\\u5143\\/\\u7b14\",\n" +
                "        \"HLD_TERM_LOW_LIM\": 0,\n" +
                "        \"HLD_TERM_UP_LIM\": 0,\n" +
                "        \"HLD_TERM_UNIT_MARK\": \"\"\n" +
                "    }]\n" +
                "}";

        List<TradeRateResult> tradeRateResults = oneFundApiService.fillTradeRateResults(json);
        assertNotNull(tradeRateResults);
        assertEquals(tradeRateResults.size(), 8);

        logger.info("tradeRateResults: {}", new ObjectMapper().writeValueAsString(tradeRateResults));
    }

    @Test
    public void test() throws JsonProcessingException {
//        String publicKey = "enVoZWNlc2hpMQ==";
//        String platformCode = "zuheceshi1";
//        String platformOpenId = "noUserToOneFund";
//        Long time = new Date().toInstant().getEpochSecond();
//        String data = objectMapper.writeValueAsString(Arrays.asList(platformOpenId));
//
//        String privateKey = "6t9L76KqurWlX9Zn";
//        String key = UnixCrypt.crypt(privateKey, "en");
//        System.out.println("key: " + key);
//
//        Map<String, Object> info = new HashMap<>();
//        info.put("public_key", publicKey);
//        info.put("platform_code", platformCode);
//        info.put("platform_openid", platformOpenId);
//        info.put("time", time);
//        info.put("data", data);
//        info.put("name", "刘晓伟");
//        info.put("phone", "13611683352");
//        info.put("identityno", "310110197812263619");
//        info.put("bankno", "6214832152964875");
//        info.put("bank_id", "004");
//
//        info.put("key", key);
//        String sign = makeMsg(info);
//        System.out.println("sign: " + sign);
//        info.put("sign", sign);
//
//        HttpEntity<Map<String, Object>> request = new HttpEntity<>(info);
//        String url = "https://onetest.zhongzhengfund.com/v2/internet/fundapi/open_account";
//        RestTemplate restTemplate = new RestTemplate();
//        String json = restTemplate.postForObject(url, info, String.class);
//        System.out.println(json);

    }
}
