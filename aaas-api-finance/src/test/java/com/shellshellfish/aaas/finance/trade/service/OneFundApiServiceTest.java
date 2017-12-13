package com.shellshellfish.aaas.finance.trade.service;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shellshellfish.aaas.finance.trade.model.*;
import com.shellshellfish.aaas.finance.trade.service.impl.OneFundApiService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
//@Ignore
public class OneFundApiServiceTest {
    private static final Logger logger = LoggerFactory.getLogger(OneFundApiService.class);

    @Autowired
    private OneFundApiService oneFundApiService;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Test
    public void testOpenAccount() throws Exception {
        oneFundApiService.openAccount("张飞", "13816629390", "612727198301116032", "4367421214584329558", "005");
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
    @Rollback(false)
    public void testWriteAllTradeRateToMongoDb() throws Exception {
        oneFundApiService.writeAllTradeRateToMongoDb();
    }

    @Test
    @Rollback(false)
    public void testBuyFund() throws JsonProcessingException {
//        String result = oneFundApiService.buyFund("33346", 108.8d, "201712-" + UUID.randomUUID(), "000590");

        // mongoTemplate.save(result, "buyfund");
    }

    @Test
    @Rollback(false)
    public void testGetExamContent() throws JsonProcessingException {
        String result = oneFundApiService.getExamContent();
        mongoTemplate.save(result, "exam");
    }

    @Test
    @Rollback(false)
    public void testCommitFakeAnswer() throws JsonProcessingException {
        String result = oneFundApiService.commitFakeAnswer();
        mongoTemplate.save(result, "fakeAnswerResult");
    }

    @Test
    public void testCommitRisk() throws JsonProcessingException {
        String result = oneFundApiService.commitRisk();
        mongoTemplate.save(result, "riskResult");
    }

    @Test
    public void testGetUserRiskList() throws JsonProcessingException {
        oneFundApiService.getUserRiskList();
    }

    @Test
    @Rollback(false)
    public void testGetApplyList() throws JsonProcessingException {
        String result = oneFundApiService.getAllApplyList();
        mongoTemplate.save(result, "appplyResult");
    }

    @Test
    public void testGetApplyResultByApplySerial() throws Exception {
        ApplyResult applyResult = oneFundApiService.getApplyResultByApplySerial("20171207000676");
        assertNotNull(applyResult);
        logger.info(new ObjectMapper().writeValueAsString(applyResult));
    }

    @Test
    public void testGetApplyResultByOutsideOrderNo() throws JsonProcessingException {
        ApplyResult applyResult = oneFundApiService.getApplyResultByOutsideOrderNo("201712-17a7807d-5d40-4681-adf3-23f");
        assertNotNull(applyResult);
        logger.info(new ObjectMapper().writeValueAsString(applyResult));
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
//        String url = "https://onetest.51fa.la/v2/internet/fundapi/open_account";
//        RestTemplate restTemplate = new RestTemplate();
//        String json = restTemplate.postForObject(url, info, String.class);
//        System.out.println(json);

    }




}