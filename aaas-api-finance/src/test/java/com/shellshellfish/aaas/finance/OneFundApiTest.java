package com.shellshellfish.aaas.finance;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shellshellfish.aaas.finance.trade.model.ApplyResult;
import com.shellshellfish.aaas.finance.trade.model.BuyFundResult;
import com.shellshellfish.aaas.finance.trade.model.OpenAccountResult;
import com.shellshellfish.aaas.finance.trade.model.SellFundResult;
import com.shellshellfish.aaas.finance.util.OneFundApi;
import org.junit.Ignore;
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
import java.util.UUID;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@Ignore
public class OneFundApiTest {
    private static final Logger logger = LoggerFactory.getLogger(OneFundApi.class);

    @Autowired
    private OneFundApi oneFundApi;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Test
    public void testOpenAccount() throws Exception {
        oneFundApi.openAccount("张飞", "13816629390", "612727198301116032", "4367421214584329558", "005");
    }

    @Test
    public void testGetOneFundInfo() throws Exception {
        String json = oneFundApi.getFundInfo("000072");
        logger.debug("json: {}", json);
    }

    @Test
    public void testGetAllFundsInfo() throws Exception {
        List<String> allFunds = oneFundApi.getAllFundsInfo();
        logger.debug("allFunds: {}", allFunds);
    }

    @Test
     public void testWriteFundToMongoDb() throws Exception {
        String json = oneFundApi.getFundInfo("000072");
        oneFundApi.writeFundToMongoDb(json);
    }

    @Test
     public void testWriteAllFundsToMongoDb() throws Exception {
        List<String> funds = oneFundApi.getAllFundsInfo();
        oneFundApi.writeAllFundsToMongoDb(funds);
    }

    @Test
    @Rollback(false)
    public void testWriteAllTradeRateToMongoDb() throws Exception {
        oneFundApi.writeAllTradeRateToMongoDb();
    }

    @Test
    @Rollback(false)
    public void testBuyFund() throws JsonProcessingException {
//        String result = oneFundApi.buyFund("33346", 108.8d, "201712-" + UUID.randomUUID(), "000590");

        // mongoTemplate.save(result, "buyfund");
    }

    @Test
    @Rollback(false)
    public void testGetExamContent() throws JsonProcessingException {
        String result = oneFundApi.getExamContent();
        mongoTemplate.save(result, "exam");
    }

    @Test
    @Rollback(false)
    public void testCommitFakeAnswer() throws JsonProcessingException {
        String result = oneFundApi.commitFakeAnswer();
        mongoTemplate.save(result, "fakeAnswerResult");
    }

    @Test
    public void testCommitRisk() throws JsonProcessingException {
        String result = oneFundApi.commitRisk();
        mongoTemplate.save(result, "riskResult");
    }

    @Test
    public void testGetUserRiskList() throws JsonProcessingException {
        oneFundApi.getUserRiskList();
    }

    @Test
    @Rollback(false)
    public void testGetApplyList() throws JsonProcessingException {
        String result = oneFundApi.getAllApplyList();
        mongoTemplate.save(result, "appplyResult");
    }

    @Test
    public void testGetApplyResultByApplySerial() throws Exception {
        ApplyResult applyResult = oneFundApi.getApplyResultByApplySerial("20171207000676");
        assertNotNull(applyResult);
        logger.info(new ObjectMapper().writeValueAsString(applyResult));
    }

    @Test
    public void testGetApplyResultByOutsideOrderNo() throws JsonProcessingException {
        ApplyResult applyResult = oneFundApi.getApplyResultByOutsideOrderNo("201712-17a7807d-5d40-4681-adf3-23f");
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
