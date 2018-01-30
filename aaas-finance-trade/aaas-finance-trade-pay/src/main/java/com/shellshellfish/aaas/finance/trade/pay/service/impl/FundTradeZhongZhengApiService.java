package com.shellshellfish.aaas.finance.trade.pay.service.impl;

import static java.awt.SystemColor.info;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.shellshellfish.aaas.common.grpc.trade.pay.ApplyResult;
import com.shellshellfish.aaas.finance.trade.pay.model.*;
import com.shellshellfish.aaas.finance.trade.pay.service.FundTradeApiService;
import org.apache.commons.codec.digest.UnixCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

@Service
public class FundTradeZhongZhengApiService implements FundTradeApiService {

    private static final Logger logger = LoggerFactory.getLogger(FundTradeZhongZhengApiService.class);
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final RestTemplate restTemplate = new RestTemplate();

    private final Gson gson = new Gson();

    public FundTradeZhongZhengApiService() {
        restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));
    }

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public OpenAccountResult openAccount(String userUuid, String name, String phone, String identityNo, String bankNo, String bankId) throws Exception {
        Map<String, Object> info = init(userUuid);
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

    private String trimSuffix(String fundCode) {
        if (fundCode != null && fundCode.contains(".")) {
            fundCode = StringUtils.split(fundCode, ".")[0];
        }
        return fundCode;
    }

    @Override
    public BuyFundResult buyFund(String userUuid, String tradeAcco, BigDecimal applySum, String outsideOrderNo, String fundCode) throws Exception {
        fundCode = trimSuffix(fundCode);

        Map<String, Object> info = init(userUuid);

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

    @Override
    public SellFundResult sellFund(String userUuid, BigDecimal sellNum, String outsideOrderNo, String
        tradeAcco, String fundCode) throws Exception {
        fundCode = trimSuffix(fundCode);

        Map<String, Object> info = init(userUuid);

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

    @Override
    public FundConvertResult fundConvert(String userUuid, BigDecimal applyShare, String
        outsideOrderNo,  String tradeAcco, String fundCode, String targetFundCode) throws Exception {

        fundCode = trimSuffix(fundCode);

        Map<String, Object> info = init(userUuid);

        info.put("tradeacco", tradeAcco);
        info.put("applyshare", applyShare);
        info.put("outsideorderno", outsideOrderNo);
        info.put("fundcode", fundCode);
        info.put("targetfundcode",targetFundCode);
        //info.put("platform_openid", "88048");

        postInit(info);
        String url = "https://onetest.51fa.la/v2/internet/fundapi/fund_convert";

        String json = restTemplate.postForObject(url, info, String.class);
        logger.info("{}", json);

        FundConvertResult fundConvertResult = null;
        JSONObject jsonObject = JSONObject.parseObject(json);
        Integer status = jsonObject.getInteger("status");
        if (status.equals(1)) {
            fundConvertResult = jsonObject.getObject("data", FundConvertResult.class);
        } else {
            String errno = jsonObject.getString("errno");
            String msg = jsonObject.getString("msg");
            throw new Exception(errno + ":" + msg);
        }

        return null;
    }

    @Override
    public CancelTradeResult cancelTrade(String userUuid, String applySerial) throws Exception {
        Map<String, Object> info = init(userUuid);

        info.put("applyserial", applySerial);

        postInit(info);
        String url = "https://onetest.51fa.la/v2/internet/fundapi/cancel_trade";

        String json = restTemplate.postForObject(url, info, String.class);
        logger.info("{}", json);

        CancelTradeResult cancelTradeResult = null;
        JSONObject jsonObject = JSONObject.parseObject(json);
        Integer status = jsonObject.getInteger("status");
        if (status.equals(1)) {
            cancelTradeResult = jsonObject.getObject("data", CancelTradeResult.class);
        } else {
            String errno = jsonObject.getString("errno");
            String msg = jsonObject.getString("msg");
            throw new Exception(errno + ":" + msg);
        }

        return cancelTradeResult;
    }

    @Override
    public ApplyResult getApplyResultByApplySerial(String userUuid, String applySerial) throws JsonProcessingException {
        Map<String, Object> info = init(userUuid);
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

    @Override
    public ApplyResult getApplyResultByOutsideOrderNo(String userUuid, String outsideOrderNo) throws JsonProcessingException {

        Map<String, Object> info = init(userUuid);
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

    private void init(String openId, String outsideOrderNo) throws JsonProcessingException {
        Map<String, Object> info = init(openId);
        info.put("outsideorderno", outsideOrderNo);
        postInit(info);
    }

    @Override
    public List<ConfirmResult> getConfirmResults(String openId, String outSideOrderNo)
        throws Exception {
        Map<String, Object> info = init(openId);
        info.put("outsideorderno", outSideOrderNo);
        postInit(info);
        String url = "https://onetest.51fa.la/v2/internet/fundapi/get_confirm_list";

        String json = restTemplate.postForObject(url, info, String.class);
        logger.info("{}", json);

        ConfirmCompondResult confirmCompondResult = gson.fromJson(json, ConfirmCompondResult.class);
        if(!confirmCompondResult.getStatus().equals("1")){
            throw new Exception(confirmCompondResult.getMsg());
        }
        return confirmCompondResult.getData();

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

    @Override
    public String getAllApplyList(String userUuid) throws JsonProcessingException {
        Map<String, Object> info = init(userUuid);

        postInit(info);
        String url = "https://onetest.51fa.la/v2/internet/fundapi/get_apply_list";

        String json = restTemplate.postForObject(url, info, String.class);
        logger.info("{}", json);

        return json;
    }

    @Override
    public String getExamContent() throws JsonProcessingException {
        Map<String, Object> info = init();

        postInit(info);
        String url = "https://onetest.51fa.la/v2/internet/fundapi/get_exam_content";

        String json = restTemplate.postForObject(url, info, String.class);
        logger.info("{}", json);

        return json;
    }

    @Override
    public String commitRisk(String userUuid) throws JsonProcessingException {
        Map<String, Object> info = init(userUuid);
        info.put("risk_ability", 3);
        postInit(info);
        String url = "https://onetest.51fa.la/v2/internet/fundapi/commit_risk";

        String json = restTemplate.postForObject(url, info, String.class);
        logger.info("{}", json);

        return json;
    }


    @Override
    public String commitRisk(String userUuid, int riskLevel) throws JsonProcessingException {
        Map<String, Object> info = init(userUuid);
        info.put("risk_ability", riskLevel);
        postInit(info);
        String url = "https://onetest.51fa.la/v2/internet/fundapi/commit_risk";

        String json = restTemplate.postForObject(url, info, String.class);
        logger.info("{}", json);

        return json;
    }

    @Override
    public String commitFakeAnswer(String userUuid) throws JsonProcessingException {
        Map<String, Object> info = init(userUuid);

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

    @Override
    public String getUserRiskList(String userUuid) throws JsonProcessingException {
        Map<String, Object> info = init(userUuid);

        postInit(info);
        String url = "https://onetest.51fa.la/v2/internet/fundapi/get_user_risk_list";

        String json = restTemplate.postForObject(url, info, String.class);
        logger.info("{}", json);

        return json;
    }

    @Override
    public String getFundInfo(String fundCode) throws Exception {
        fundCode = trimSuffix(fundCode);

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

    @Override
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

    @Override
    public String getTradeRate(String fundCode, String businFlag) throws JsonProcessingException {
        fundCode = trimSuffix(fundCode);

        Map<String, Object> info = init();
        info.put("fundcode", fundCode);
        info.put("buinflag", businFlag);
        postInit(info);

        String url = "https://onetest.51fa.la/v2/internet/fundapi/get_rate";

        String json = restTemplate.postForObject(url, info, String.class);
        logger.info("{}", json);

        return json;
    }

    @Override
    public List<TradeRateResult> getTradeRateAsList(String fundCode, String businFlag) throws JsonProcessingException {
        fundCode = trimSuffix(fundCode);

        String json = getTradeRate(fundCode, businFlag);

        return fillTradeRateResults(json);
    }

    public List<TradeRateResult> fillTradeRateResults(String json) {
        List<TradeRateResult> tradeRateResults = null;
        JSONObject jsonObject = JSONObject.parseObject(json);
        Integer status = jsonObject.getInteger("status");
        if (status.equals(1)) {
            tradeRateResults = new ArrayList<>();
            JSONArray jsonArray = jsonObject.getJSONArray("data");
            for(int i = 0; i < jsonArray.size(); i++) {
                TradeRateResult tradeRateResult = jsonArray.getObject(i, TradeRateResult.class);
                tradeRateResults.add(tradeRateResult);
            }
        }

        return tradeRateResults;
    }

    @Override
    public List<TradeLimitResult> getTradeLimits(String fundCode, String businFlag) throws Exception {
        fundCode = trimSuffix(fundCode);

        Map<String, Object> info = init();
        info.put("fundcode", fundCode);
        info.put("buinflag", businFlag);
        postInit(info);

        String url = "https://onetest.51fa.la/v2/internet/fundapi/get_trade_limit";

        String json = restTemplate.postForObject(url, info, String.class);
        logger.info("{}", json);

        return fillTradeLimitResults(json);
    }

    public List<TradeLimitResult> fillTradeLimitResults(String json) {
        List<TradeLimitResult> tradeLimitResults = null;
        JSONObject jsonObject = JSONObject.parseObject(json);
        Integer status = jsonObject.getInteger("status");
        if (status.equals(1)) {
            tradeLimitResults = new ArrayList<>();
            JSONArray jsonArray = jsonObject.getJSONArray("data");
            for(int i = 0; i < jsonArray.size(); i++) {
                TradeLimitResult tradeLimitResult = jsonArray.getObject(i, TradeLimitResult.class);
                tradeLimitResults.add(tradeLimitResult);
            }
        }

        return tradeLimitResults;
    }

    @Override
    public BigDecimal getDiscount(String fundCode, String businFlag) throws Exception {
        fundCode = trimSuffix(fundCode);

        Map<String, Object> info = init();
        info.put("fundcode", fundCode);
        info.put("businflag", businFlag);
        postInit(info);

        String url = "https://onetest.51fa.la/v2/internet/fundapi/get_trade_discount";

        String json = restTemplate.postForObject(url, info, String.class);
        logger.info("{}", json);

        JSONObject jsonObject = (JSONObject) JSONObject.parse(json);
        Integer status = jsonObject.getInteger("status");
        if (!status.equals(1)){
            throw new Exception(jsonObject.getString("msg"));
        }

        JSONArray jsonArray = jsonObject.getJSONArray("data");
        BigDecimal discount = jsonArray.getJSONObject(0).getBigDecimal("discount");
        return discount;
    }

    @Override
    public BigDecimal getRate(String fundCode, String businFlag) throws Exception {
        fundCode = trimSuffix(fundCode);

        List<TradeRateResult> tradeRateResults = getTradeRateAsList(fundCode, businFlag);
        for(TradeRateResult rateResult: tradeRateResults) {
            if (rateResult.getChngMinTermMark().equals("日常申购费") && rateResult.getChagRateUnitMark().equals("%")) {
                Double rate = Double.parseDouble(rateResult.getChagRateUpLim())/100d;
                return BigDecimal.valueOf(rate);
            }
        }
        throw new Exception("no rate found");
    }

    @Override
    public BigDecimal calcPoundageByTotalAmount(BigDecimal totalAmount, BigDecimal rate, BigDecimal discount) {
        // return totalAmount * rate * discount / (1 + rate * discount);
        BigDecimal temp = rate.multiply(discount);
        return totalAmount.multiply(temp).divide(temp.add(BigDecimal.ONE));
    }

    @Override
    public BigDecimal calcPoundage(BigDecimal amount, BigDecimal rate, BigDecimal discount){
        return amount.multiply(rate).multiply(discount);
    }

    @Override
    public BigDecimal calcDiscountPoundage(BigDecimal amount, BigDecimal rate, BigDecimal discount) {
        return amount.multiply(rate).multiply(BigDecimal.ONE.subtract(discount));
    }

    @Override
    public List<UserBank> getUserBank(String fundCode) throws Exception {
        fundCode = trimSuffix(fundCode);

        Map<String, Object> info = init();
        info.put("fundcode", fundCode);
        postInit(info);

        String url = "https://onetest.51fa.la/v2/internet/fundapi/get_user_bank_list";

        String json = restTemplate.postForObject(url, info, String.class);
        logger.info("{}", json);

        JSONObject jsonObject = (JSONObject) JSONObject.parse(json);
        Integer status = jsonObject.getInteger("status");
        if (!status.equals(1)){
            throw new Exception(jsonObject.getString("msg"));
        }

        List<UserBank> userBanks = new ArrayList<>();
        JSONArray jsonArray = jsonObject.getJSONArray("data");
        for(int i = 0; i < jsonArray.size(); i++) {
            userBanks.add(jsonArray.getObject(i, UserBank.class));
        }
        return userBanks;
    }

    @Override
    public List<UserBank> getUserBank(String userId, String fundCode) throws Exception {
        fundCode = trimSuffix(fundCode);

        Map<String, Object> info = init(userId);
        info.put("fundcode", fundCode);
        postInit(info);

        String url = "https://onetest.51fa.la/v2/internet/fundapi/get_user_bank_list";

        String json = restTemplate.postForObject(url, info, String.class);
        logger.info("{}", json);

        JSONObject jsonObject = (JSONObject) JSONObject.parse(json);
        Integer status = jsonObject.getInteger("status");
        if (!status.equals(1)){
            throw new Exception(jsonObject.getString("msg"));
        }

        List<UserBank> userBanks = new ArrayList<>();
        JSONArray jsonArray = jsonObject.getJSONArray("data");
        for(int i = 0; i < jsonArray.size(); i++) {
            userBanks.add(jsonArray.getObject(i, UserBank.class));
        }
        return userBanks;
    }

    @Override
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

    @Override
    public void writeFundToMongoDb(String json) {
        mongoTemplate.save(json, "fundInfo");
    }

    @Override
    public void writeAllFundsToMongoDb(List<String> funds) {
        for (String fund: funds) {
            mongoTemplate.save(fund, "fundInfo");
        }
    }



    private void postInit(Map<String, Object> info) {
        String sign = makeMsg(info);
        logger.info("sign: {}", sign);
        info.put("sign", sign);
    }

    private Map<String, Object> init() throws JsonProcessingException {
        return init("3d8f5e7713d365a122f2107c41385635145daffc5f14297c03997180c5cd9ee5");
    }

    private Map<String, Object> init(Long userId) throws JsonProcessingException {
        return init("" + userId);
    }

    private Map<String, Object> init(String userUuid) throws JsonProcessingException {
        Map<String, Object> info = new HashMap<>();

        String publicKey = "enVoZWNlc2hpMQ==";
        String platformCode = "zuheceshi1";
        String platformOpenId = userUuid;//"shellshellfish";//"noUserToOneFund";
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

    private String makeMsg(Map<String, Object> param) {
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

    private static String md5(String text) {
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

    private static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }
}
