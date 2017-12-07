package com.shellshellfish.aaas.finance;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.shellshellfish.aaas.finance.util.OneFundApi;
import org.junit.Test;

public class OneFundApiTest {

    //private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testOpenAccount() throws JsonProcessingException {
        OneFundApi.openAccount();
    }

    @Test
    public void testGetFundInfo() throws JsonProcessingException {
        String json = OneFundApi.getFundInfo("");
        JsonParser jsonParser = new JsonParser();

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
