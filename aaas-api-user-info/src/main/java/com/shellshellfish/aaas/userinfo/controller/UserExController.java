package com.shellshellfish.aaas.userinfo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/api")
public class UserExController {
    public static final Logger logger = LoggerFactory.getLogger(RestApiController.class);

    @RequestMapping(value = "/userInfo/{userId}/message", method = RequestMethod.GET)
    @ResponseBody
    public String getMessage(@PathVariable String userId) {
        return "{\n" +
                "\t\"name\":\"message\",\n" +
                "\t\"title\":\"我的消息\",\n" +
                "\t\"_links\": {\n" +
                "\t\t\"self\": \"/api/100/message\",\t\t\n" +
                "\t\t\"realted\":[\n" +
                "\t\t\t{\n" +
                "\t\t\t\t\"title\":\"智投推送\",\n" +
                "\t\t\t\t\"href\":\"/api/users/100/promotionMessages\",\n" +
                "\t\t\t\t\"describedBy\":\"/schema/promotionMessage.json\"\n" +
                "\t\t\t},\n" +
                "\t\t\t{\n" +
                "\t\t\t\t\"title\":\"系统消息\",\n" +
                "\t\t\t\t\"href\":\"/api/users/100/systemMessages\",\n" +
                "\t\t\t\t\"describedBy\":\"/schema/systemMessage.json\"\n" +
                "\t\t\t}\n" +
                "\t\t]\n" +
                "\t}\n" +
                "}";
    }

    @RequestMapping(value = "/userInfo/{userId}/promotionMessages", method = RequestMethod.GET)
    @ResponseBody
    public String getPromotionMessages(@PathVariable String userId){
        return "{\n" +
                "    \"title\":\"智能推送\",\t\n" +
                "\t\"_total\":3,\n" +
                "\t\"items\": [\n" +
                "\t\t{\n" +
                "\t\t\t\"title\": \"智能推送A\",\n" +
                "\t\t\t\"time\": \"2017-08-06 10:00\",\n" +
                "\t\t\t\"content\": \"智投推送消息内容\"\n" +
                "\t\t},\n" +
                "\t\t{\n" +
                "\t\t\t\"title\": \"智能推送A\",\n" +
                "\t\t\t\"time\": \"2017-08-06 10:00\",\n" +
                "\t\t\t\"content\": \"智投推送消息内容\"\n" +
                "\t\t},\n" +
                "\t\t{\n" +
                "\t\t\t\"title\": \"智能推送A\",\n" +
                "\t\t\t\"time\": \"2017-08-06 10:00\",\n" +
                "\t\t\t\"content\": \"智投推送消息内容\"\n" +
                "\t\t}\n" +
                "\t],\t\n" +
                "\t\"_links\": {\n" +
                "\t\t\"self\":\"/api/users/100/promotionMessages\",\n" +
                "\t\t\"describedBy\":\"/schema/promotionMessage/collection.json\"\n" +
                "\t}\n" +
                "}";
    }

    @RequestMapping(value = "/userInfo/{userId}/systemMessages", method = RequestMethod.GET)
    @ResponseBody
    public String getSystemMessages(@PathVariable String userId){
        return "{\n" +
                "    \"title\":\"智能推送\",\t\n" +
                "\t\"_total\":1,\n" +
                "\t\"items\":[\n" +
                "\t\t{\n" +
                "\t\t\t\"title\": \"系统通知\",\n" +
                "\t\t\t\"time\": \"2017-08-06 10:00\",\n" +
                "\t\t\t\"content\": \"系统将于07日凌晨进行维护, 给您带来的不变, 敬请谅解!\",\n" +
                "\t\t}\t\t\n" +
                "\t],\n" +
                "\t\"_links\": {\n" +
                "\t\t\"self\":\"/api/users/100/systemMessages\",\n" +
                "\t\t\"describedBy\":\"/schema/systemMessage/collection.json\"\n" +
                "\t}\t\n" +
                "\t\n" +
                "}";
    }


}
