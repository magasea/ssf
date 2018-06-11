package com.shellshellfish.aaas.finance.trade.order.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.shellshellfish.aaas.finance.trade.order.model.DiscountInfo;
import com.shellshellfish.aaas.finance.trade.order.model.FundInfo;
import com.shellshellfish.aaas.finance.trade.order.model.LimitInfo;
import com.shellshellfish.aaas.finance.trade.order.model.RateInfo;
import com.shellshellfish.aaas.finance.trade.order.model.TradeLimitResult;
import com.shellshellfish.aaas.finance.trade.order.model.TradeRateResult;
import com.shellshellfish.aaas.finance.trade.order.model.UserBank;
import com.shellshellfish.aaas.finance.trade.order.service.FundInfoApiService;
import org.apache.commons.codec.digest.UnixCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
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
public class FundInfoZhongZhengApiService implements FundInfoApiService {

    private static final Logger logger = LoggerFactory.getLogger(FundInfoZhongZhengApiService.class);
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final RestTemplate restTemplate = new RestTemplate();

    public FundInfoZhongZhengApiService() {
        restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));
    }

    @Autowired
    private MongoTemplate mongoTemplate;

    private String trimSuffix(String fundCode) {
        if (fundCode != null && fundCode.contains(".")) {
            fundCode = StringUtils.split(fundCode, ".")[0];
        }
        return fundCode;
    }

    @Override
    public String getExamContent() throws JsonProcessingException {
        Map<String, Object> info = init();

        postInit(info);
        String url = "https://onetest.zhongzhengfund.com/v2/internet/fundapi/get_exam_content";

        String json = restTemplate.postForObject(url, info, String.class);
        logger.info("{}", json);

        return json;
    }

    @Override
    public String commitRisk(String userUuid) throws JsonProcessingException {
        Map<String, Object> info = init(userUuid);
        info.put("risk_ability", 3);
        postInit(info);
        String url = "https://onetest.zhongzhengfund.com/v2/internet/fundapi/commit_risk";

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
        String url = "https://onetest.zhongzhengfund.com/v2/internet/fundapi/commit_fake_answer";

        String json = restTemplate.postForObject(url, info, String.class);
        logger.info("{}", json);

        return json;
    }

    @Override
    public String getUserRiskList(String userUuid) throws JsonProcessingException {
        Map<String, Object> info = init(userUuid);

        postInit(info);
        String url = "https://onetest.zhongzhengfund.com/v2/internet/fundapi/get_user_risk_list";

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

        String url = "https://onetest.zhongzhengfund.com/v2/internet/fundapi/get_fund_info";
        String json = restTemplate.postForObject(url, info, String.class);
        logger.info(json);
        JSONObject jsonObject = JSONObject.parseObject(json);
        if (!jsonObject.getInteger("status").equals(1)) {
            logger.error(jsonObject.getString("msg"));
            throw new Exception(jsonObject.getString("msg"));
        }

        if (!StringUtils.isEmpty(fundCode)) {
            JSONArray jsonArray = jsonObject.getJSONArray("data");
            return jsonArray.getString(0);
        }

        return jsonObject.getJSONArray("data").toJSONString();
    }

    @Override
    public String getDiscountRawString(String fundCode, String businFlag) {
        String json="";
        try {
            fundCode = trimSuffix(fundCode);

            Map<String, Object> info = init();
            info.put("fundcode", fundCode);
            info.put("businflag", businFlag);
            postInit(info);

            String url = "https://onetest.zhongzhengfund.com/v2/internet/fundapi/get_trade_discount";

           json = restTemplate.postForObject(url, info, String.class);
            logger.info("{}", json);
            return json;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }

    @Override
    public List<String> getAllFundsInfo() throws Exception {
        String json = getFundInfo(null);
        JSONArray jsonArray = JSONObject.parseArray(json);
        List<String> funds = new ArrayList<>();
        if(jsonArray.size()>0){
            for(int i=0;i<jsonArray.size();i++){
                JSONObject fund = jsonArray.getJSONObject(i);
                funds.add(fund.toJSONString());
                logger.info("fund:{}", fund.toJSONString());
            }
        }
        return funds;
    }

    @Override
    public void writeAllFundsToMongoDb(List<String> funds) {
        //清理老数据
        Query query=new Query();
        mongoTemplate.findAllAndRemove(query,FundInfo.class);
        for (String fund: funds) {
            mongoTemplate.save(fund, "fundInfo");
        }
    }

    @Override
    public void writeAllFundsTradeRateToMongoDb(List<String> funds) {
        //清理老数据
        Query query=new Query();
        mongoTemplate.findAllAndRemove(query,RateInfo.class);
        List<String> tradeRateInfoList=new ArrayList<>();
        try {
            for (String fund : funds) {
                JSONObject jsonObject = JSONObject.parseObject(fund);
                String fundCode = jsonObject.getString("fundcode");
                logger.info("fundCode:{}", fundCode);
                String tradeRateInfo = getTradeRate(fundCode, "022");
                tradeRateInfoList.add(tradeRateInfo);
            }
            mongoTemplate.insert(tradeRateInfoList, "rateInfo");
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    @Override
    public void writeAllFundsDiscountToMongoDb(List<String> funds) {
        //清理老数据
        Query query=new Query();
        mongoTemplate.findAllAndRemove(query,DiscountInfo.class);
        List<String> tradeDiscountInfoList=new ArrayList<>();
        try {
            for (String fund : funds) {
                JSONObject jsonObject = JSONObject.parseObject(fund);
                String fundCode = jsonObject.getString("fundcode");
                logger.info("fundCode:{}", fundCode);
                String tradeRateInfo = getDiscountRawString(fundCode, "022");
                mongoTemplate.save(tradeRateInfo, "discountInfo");
                tradeRateInfo = getDiscountRawString(fundCode, "024");
                mongoTemplate.save(tradeRateInfo, "discountInfo");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void writeAllTradeLimitToMongoDb(List<String> funds) {
        //清理老数据
        Query query=new Query();
        mongoTemplate.findAllAndRemove(query,LimitInfo.class);
        try {
            for(String fund:funds) {
                JSONObject jsonObject = JSONObject.parseObject(fund);
                String fundCode = jsonObject.getString("fundcode");
                logger.info("fundCode:{}", fundCode);
                String tradeLimitInfo = getTradeLimitAsRawString(fundCode, "022");
                System.out.println("tradelimit:"+tradeLimitInfo);
                mongoTemplate.save(tradeLimitInfo, "limitInfo");
                tradeLimitInfo = getTradeLimitAsRawString(fundCode, "024");
                mongoTemplate.save(tradeLimitInfo, "limitInfo");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getTradeLimitAsRawString(String fundCode, String businFlag)  {
        String json="{}";
        try {
            fundCode = trimSuffix(fundCode);
            Map<String, Object> info = init();
            info.put("fundcode", fundCode);
            info.put("buinflag", businFlag);
            postInit(info);
            String url = "https://onetest.zhongzhengfund.com/v2/internet/fundapi/get_trade_limit";
            json = restTemplate.postForObject(url, info, String.class);
            logger.info("{}", json);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }


    @Override
    public String getTradeRate(String fundCode, String businFlag) throws JsonProcessingException {
        fundCode = trimSuffix(fundCode);

        Query query = new Query();
        query.addCriteria(Criteria.where("data.FUND_CODE").is(fundCode));
        List<String> rateInfoDocs = mongoTemplate.find(query, String.class, "rateInfo");

        if (rateInfoDocs.size() > 0) {
            logger.info(rateInfoDocs.get(0));
            return rateInfoDocs.get(0);
        }

        Map<String, Object> info = init();
        info.put("fundcode", fundCode);
        info.put("businflag", businFlag);
        postInit(info);

        String url = "https://onetest.zhongzhengfund.com/v2/internet/fundapi/get_rate";

        String json = restTemplate.postForObject(url, info, String.class);
        mongoTemplate.save(json,"rateInfo");
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

        Query query = new Query();
        query.addCriteria(Criteria.where("data.fundCode").is(fundCode));
        List<String> rateInfoDocs = mongoTemplate.find(query, String.class, "limitInfo");

        String json;
        if (rateInfoDocs.size() > 0) {
            json = rateInfoDocs.get(0);

        } else {
            Map<String, Object> info = init();
            info.put("fundcode", fundCode);
            info.put("businflag", businFlag);
            postInit(info);

            String url = "https://onetest.zhongzhengfund.com/v2/internet/fundapi/get_trade_limit";

            json = restTemplate.postForObject(url, info, String.class);
            mongoTemplate.save(json,"limitInfo");
        }

//        logger.info("{}", json);

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

        Query query = new Query();
        query.addCriteria(Criteria.where("data.fundcode").is(fundCode));
        List<String> discountInfoDocs = mongoTemplate.find(query, String.class, "discountInfo");

        String json;
        if (discountInfoDocs.size() > 0) {
            logger.info(discountInfoDocs.get(0));
            json = discountInfoDocs.get(0);
        } else {
            Map<String, Object> info = init();
            info.put("fundcode", fundCode);
            info.put("businflag", businFlag);
            postInit(info);
            String url = "https://onetest.zhongzhengfund.com/v2/internet/fundapi/get_trade_discount";
            json = restTemplate.postForObject(url, info, String.class);
            mongoTemplate.save(json,"discountInfo");
        }

        logger.info("{}", json);

        JSONObject jsonObject = (JSONObject) JSONObject.parse(json);
        Integer status = jsonObject.getInteger("status");
        if (!status.equals(1)){
        	logger.error(jsonObject.getString("msg"));
            throw new Exception(jsonObject.getString("msg"));
        }

        JSONArray jsonArray = jsonObject.getJSONArray("data");
        BigDecimal discount = BigDecimal.ONE;
        if (jsonArray.size() > 0) {
            discount = jsonArray.getJSONObject(0).getBigDecimal("discount");
        }
        return discount;
    }

    @Override
    public BigDecimal getRateOfBuyFund(BigDecimal amount, String fundCode, String businFlag) throws Exception {
        // TODO:
        fundCode = trimSuffix(fundCode);

        List<TradeRateResult> tradeRateResults = getTradeRateAsList(fundCode, businFlag);
        for(TradeRateResult rateResult: tradeRateResults) {
            if (rateResult.getChngMinTermMark().equals("日常申购费")) {
                double lowLim = Double.parseDouble(rateResult.getPertValLowLim())*10000;
                double UpLim = Double.parseDouble(rateResult.getPertValUpLim())*10000;

                if(BigDecimal.valueOf(lowLim).compareTo(amount)==-1&&(BigDecimal.valueOf(UpLim).compareTo(amount)==1||UpLim==0.00)){
                    Double rate = Double.parseDouble(rateResult.getChagRateUpLim())/100d;
                    return BigDecimal.valueOf(rate);
                }
            }
        }
        logger.error("no rate found");
        throw new Exception("no rate found");
    }

    @Override
    public BigDecimal getRateOfSellFund(String fundCode, String businFlag) throws Exception {
        // TODO:
        fundCode = trimSuffix(fundCode);

        List<TradeRateResult> tradeRateResults = getTradeRateAsList(fundCode, businFlag);
        for(TradeRateResult rateResult: tradeRateResults) {
            if (rateResult.getChngMinTermMark().equals("日常赎回费") && rateResult.getChagRateUnitMark().equals("%")) {
                Double rate = Double.parseDouble(rateResult.getChagRateUpLim())/100d;
                return BigDecimal.valueOf(rate);
            }
        }
        logger.error("no rate found");
        throw new Exception("no rate found");
    }

    @Override
    public BigDecimal calcPoundageByGrossAmount(BigDecimal totalAmount, BigDecimal rate, BigDecimal discount) {
        // return totalAmount * rate * discount / (1 + rate * discount);
        BigDecimal temp = rate.multiply(discount);
        return totalAmount.multiply(temp).divide(temp.add(BigDecimal.ONE),6);
    }

    @Override
    public BigDecimal calcPoundageWithDiscount(BigDecimal amount, BigDecimal rate, BigDecimal discount){
        return amount.multiply(rate).multiply(discount);
    }

    @Override
    public BigDecimal calcDiscountSaving(BigDecimal amount, BigDecimal rate, BigDecimal discount) {
        return amount.multiply(rate).multiply(BigDecimal.ONE.subtract(discount));
    }

    @Override
    public List<UserBank> getUserBank(String fundCode) throws Exception {
        fundCode = trimSuffix(fundCode);

        Map<String, Object> info = init();
        info.put("fundcode", fundCode);
        postInit(info);

        String url = "https://onetest.zhongzhengfund.com/v2/internet/fundapi/get_user_bank_list";

        String json = restTemplate.postForObject(url, info, String.class);
        logger.info("{}", json);

        JSONObject jsonObject = (JSONObject) JSONObject.parse(json);
        Integer status = jsonObject.getInteger("status");
		if (!status.equals(1)) {
			logger.error(jsonObject.getString("msg"));
			throw new Exception(jsonObject.getString("msg"));
		}

        List<UserBank> userBanks = new ArrayList<>();
        JSONArray jsonArray = jsonObject.getJSONArray("data");
        for(int i = 0; i < jsonArray.size(); i++) {
            userBanks.add(jsonArray.getObject(i, UserBank.class));
        }
        return userBanks;
    }

    private void postInit(Map<String, Object> info) {
        String sign = makeMsg(info);
        logger.info("sign: {}", sign);
        info.put("sign", sign);
    }

    private Map<String, Object> init() throws JsonProcessingException {
        return init("shellshellfish");
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
            logger.error("exception:",e);
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
