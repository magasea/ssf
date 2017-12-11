package com.shellshellfish.aaas.finance.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shellshellfish.aaas.finance.trade.model.ApplyResult;
import com.shellshellfish.aaas.finance.trade.model.BuyFundResult;
import com.shellshellfish.aaas.finance.trade.model.OpenAccountResult;
import com.shellshellfish.aaas.finance.trade.model.SellFundResult;
import org.apache.commons.codec.digest.UnixCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

@Component
public class OneFundApi {

    private static final Logger logger = LoggerFactory.getLogger(OneFundApi.class);
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final RestTemplate restTemplate = new RestTemplate();

    public OneFundApi() {
        restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));
    }

    @Autowired
    private MongoTemplate mongoTemplate;

    public OpenAccountResult openAccount(String name, String phone, String identityNo, String bankNo, String bankId) throws Exception {
        Map<String, Object> info = init();
//        info.put("name", "张飞");
//        info.put("phone", "13816629390");
//        info.put("identityno", "612727198301116032");
//        info.put("bankno", "4367421214584329558");
//        info.put("bank_id", "005");

        info.put("name", name);
        info.put("phone", phone);
        info.put("identityno", identityNo);
        info.put("bankno", bankNo);
        info.put("bank_id", bankId);

        postInit(info);

        String url = "https://onetest.51fa.la/v2/internet/fundapi/open_account";
        String json = restTemplate.postForObject(url, info, String.class);
        logger.info("{}", json);

        OpenAccountResult openAccountResult = null;
        JSONObject jsonObject = JSONObject.parseObject(json);
        Integer status = jsonObject.getInteger("status");
        if (status.equals(1)) {
            openAccountResult = jsonObject.getObject("data", OpenAccountResult.class);
        } else {
            String errno = jsonObject.getString("errno");
            String msg = jsonObject.getString("msg");
            throw new Exception(errno + ":" + msg);
        }
        return openAccountResult;
    }

    public BuyFundResult buyFund(String tradeAcco, Double applySum, String outsideOrderNo, String fundCode) throws Exception {
        Map<String, Object> info = init();

        info.put("tradeacco", tradeAcco);
        info.put("applysum", applySum);
        info.put("outsideorderno", outsideOrderNo);
        info.put("fundcode", fundCode);
        //info.put("platform_openid", "88048");

        postInit(info);
        String url = "https://onetest.51fa.la/v2/internet/fundapi/buy_fund";

        String json = restTemplate.postForObject(url, info, String.class);
        logger.info("{}", json);

        BuyFundResult buyFundResult = null;
        JSONObject jsonObject = JSONObject.parseObject(json);
        Integer status = jsonObject.getInteger("status");
        if (status.equals(1)) {
            buyFundResult = jsonObject.getObject("data", BuyFundResult.class);
        } else {
            String errno = jsonObject.getString("errno");
            String msg = jsonObject.getString("msg");
            throw new Exception(errno + ":" + msg);
        }

        return buyFundResult;
    }

    public SellFundResult sellFund(Integer sellNum, String outsideOrderNo, String tradeAcco, String fundCode) throws Exception {
        Map<String, Object> info = init();

        info.put("sell_num", sellNum);
        info.put("outsideorderno", outsideOrderNo);
        info.put("tradeacco", tradeAcco);
        info.put("fundcode", fundCode);
        info.put("sell_type", 0);

        postInit(info);
        String url = "https://onetest.51fa.la/v2/internet/fundapi/sell_fund";

        String json = restTemplate.postForObject(url, info, String.class);
        logger.info("{}", json);

        SellFundResult sellFundResult = null;
        JSONObject jsonObject = JSONObject.parseObject(json);
        Integer status = jsonObject.getInteger("status");
        if (status.equals(1)) {
            sellFundResult = jsonObject.getObject("data", SellFundResult.class);
        } else {
            String errno = jsonObject.getString("errno");
            String msg = jsonObject.getString("msg");
            throw new Exception(errno + ":" + msg);
        }

        return sellFundResult;
    }

    public ApplyResult getApplyResultByApplySerial(String applySerial) throws JsonProcessingException {
        Map<String, Object> info = init();
        info.put("applyserial", applySerial);
        postInit(info);
        String url = "https://onetest.51fa.la/v2/internet/fundapi/get_apply_list";

        String json = restTemplate.postForObject(url, info, String.class);
        logger.info("{}", json);

        ApplyResult applyResult = null;
        JSONObject jsonObject = JSONObject.parseObject(json);
        Integer status = jsonObject.getInteger("status");
        applyResult = getApplyResult(applyResult, jsonObject, status);
        return applyResult;
    }

    public ApplyResult getApplyResultByOutsideOrderNo(String outsideOrderNo) throws JsonProcessingException {
        Map<String, Object> info = init();
        info.put("outsideorderno", outsideOrderNo);
        postInit(info);
        String url = "https://onetest.51fa.la/v2/internet/fundapi/get_apply_list";

        String json = restTemplate.postForObject(url, info, String.class);
        logger.info("{}", json);

        ApplyResult applyResult = null;
        JSONObject jsonObject = JSONObject.parseObject(json);
        Integer status = jsonObject.getInteger("status");
        applyResult = getApplyResult(applyResult, jsonObject, status);
        return applyResult;
    }

    private ApplyResult getApplyResult(ApplyResult applyResult, JSONObject jsonObject, Integer status) {
        if (status.equals(1)) {
            JSONArray jsonArray = jsonObject.getJSONArray("data");
            applyResult = jsonArray.getObject(0, ApplyResult.class);
        } else {
            String errno = jsonObject.getString("errno");
            String msg = jsonObject.getString("msg");
            // throw new Exception(errno + ":" + msg);
        }
        return applyResult;
    }

    public String getAllApplyList() throws JsonProcessingException {
        Map<String, Object> info = init();

        postInit(info);
        String url = "https://onetest.51fa.la/v2/internet/fundapi/get_apply_list";

        String json = restTemplate.postForObject(url, info, String.class);
        logger.info("{}", json);

        return json;
    }

    public String getExamContent() throws JsonProcessingException {
        Map<String, Object> info = init();

        postInit(info);
        String url = "https://onetest.51fa.la/v2/internet/fundapi/get_exam_content";

        String json = restTemplate.postForObject(url, info, String.class);
        logger.info("{}", json);

        return json;
    }

    public String commitRisk() throws JsonProcessingException {
        Map<String, Object> info = init();
        info.put("risk_ability", 3);
        postInit(info);
        String url = "https://onetest.51fa.la/v2/internet/fundapi/commit_risk";

        String json = restTemplate.postForObject(url, info, String.class);
        logger.info("{}", json);

        return json;
    }

    public String commitFakeAnswer() throws JsonProcessingException {
        Map<String, Object> info = init();

        Map<String, String> fakeAnswer = new LinkedHashMap<>();
        fakeAnswer.put("00066", "001");
        fakeAnswer.put("00067", "002");
        fakeAnswer.put("00068", "003");
        fakeAnswer.put("00069", "002");
        fakeAnswer.put("00070", "001");
        fakeAnswer.put("00071", "002");
        fakeAnswer.put("00072", "002");
        fakeAnswer.put("00073", "003");
        fakeAnswer.put("00074", "003");
        fakeAnswer.put("00075", "003");
        fakeAnswer.put("00076", "003");
        fakeAnswer.put("00077", "003");
        fakeAnswer.put("00078", "004");
//        fakeAnswer.put("00029", "005");
//        fakeAnswer.put("00030", "001");
//        fakeAnswer.put("00031", "003");
//        fakeAnswer.put("00032", "004");

        info.put("fake_answer", objectMapper.writeValueAsString(fakeAnswer));

        postInit(info);
        String url = "https://onetest.51fa.la/v2/internet/fundapi/commit_fake_answer";

        String json = restTemplate.postForObject(url, info, String.class);
        logger.info("{}", json);

        return json;
    }

    public String getUserRiskList() throws JsonProcessingException {
        Map<String, Object> info = init();

        postInit(info);
        String url = "https://onetest.51fa.la/v2/internet/fundapi/get_user_risk_list";

        String json = restTemplate.postForObject(url, info, String.class);
        logger.info("{}", json);

        return json;
    }

    public String getFundInfo(String fundCode) throws Exception {
        Map<String, Object> info = init();
        if (!StringUtils.isEmpty(fundCode)) {
            info.put("fundcode", fundCode);
        }

        postInit(info);

        String url = "https://onetest.51fa.la/v2/internet/fundapi/get_fund_info";
        String json = restTemplate.postForObject(url, info, String.class);
        logger.info(json);
        JSONObject jsonObject = JSONObject.parseObject(json);
        if (!jsonObject.getInteger("status").equals(1)) {
            throw new Exception(jsonObject.getString("msg"));
        }

        if (!StringUtils.isEmpty(fundCode)) {
            JSONArray jsonArray = jsonObject.getJSONArray("data");
            return jsonArray.getString(0);
        }

        return jsonObject.getJSONObject("data").toJSONString();
    }

    public List<String> getAllFundsInfo() throws Exception {
        String json = getFundInfo(null);

        JSONObject jsonObject = JSONObject.parseObject(json);
        List<String> keys = new ArrayList<>(jsonObject.keySet());
        Collections.sort(keys);
        logger.info("size of keys: {}, keys: {}", keys.size(), keys);

        List<String> funds = new ArrayList<>();
        for(String key: keys) {
            JSONObject fund = jsonObject.getJSONObject(key);
            funds.add(fund.toJSONString());
            logger.info("fund:{}", fund.toJSONString());
        }

        return funds;
    }

    public String getTradeRate(String fundCode, String buinflag) throws JsonProcessingException {
        Map<String, Object> info = init();
        info.put("fundcode", fundCode);
//        info.put("buinflag", buinflag);
        postInit(info);

        String url = "https://onetest.51fa.la/v2/internet/fundapi/get_rate";

        String json = restTemplate.postForObject(url, info, String.class);
        logger.info("{}", json);

        return json;
    }

    public void writeAllTradeRateToMongoDb() throws Exception {
        List<String> funds = getAllFundsInfo();
        for(String fund:funds) {
            JSONObject jsonObject = JSONObject.parseObject(fund);
            String fundCode = jsonObject.getString("fundcode");
            logger.info("fundCode:{}", fundCode);
            String tradeRateInfo = getTradeRate(fundCode, "022");
            mongoTemplate.save(tradeRateInfo, "rateInfo");
        }
    }

    public void writeFundToMongoDb(String json) {
        mongoTemplate.save(json, "fundInfo");
    }

    public void writeAllFundsToMongoDb(List<String> funds) {
        for (String fund: funds) {
            mongoTemplate.save(fund, "fundInfo");
        }
    }

    public void postInit(Map<String, Object> info) {
        String sign = makeMsg(info);
        logger.info("sign: {}", sign);
        info.put("sign", sign);
    }

    public Map<String, Object> init() throws JsonProcessingException {
        Map<String, Object> info = new HashMap<>();

        String publicKey = "enVoZWNlc2hpMQ==";
        String platformCode = "zuheceshi1";
        String platformOpenId = "shellshellfish";//"noUserToOneFund";
        Long time = new Date().toInstant().getEpochSecond();
        String data = objectMapper.writeValueAsString(Arrays.asList(platformOpenId));

        String privateKey = "6t9L76KqurWlX9Zn";
        String key = UnixCrypt.crypt(privateKey, "en");
        logger.info("key: {}", key);
        info.put("key", key);

        info.put("public_key", publicKey);
        info.put("platform_code", platformCode);
        info.put("platform_openid", platformOpenId);
        info.put("time", time);
        info.put("data", data);

        return info;
    }

    public String makeMsg(Map<String, Object> param) {
        List<String> keys  = new ArrayList<>(param.keySet());
        Collections.sort(keys);
        String str = "";
        for(String key:keys) {
            str += key;
            str += param.get(key);
        }
        logger.info("str :{}", str);

        String sign = md5(str);
        return sign;
    }

    public static String md5(String text) {
        try {
            MessageDigest digester = MessageDigest.getInstance("MD5");
            digester.update(text.getBytes());
            byte[] md5Bytes = digester.digest();
            String md5Text = null;

            md5Text = bytesToHex(md5Bytes);

            return md5Text;

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    final protected static char[] hexArray = "0123456789abcdef".toCharArray();

    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }
}
