package com.shellshellfish.aaas.userinfo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

@Controller
@RequestMapping("/api")
public class UserExController {
    public static final Logger logger = LoggerFactory.getLogger(RestApiController.class);

    @RequestMapping(value = "/userInfo/{userId}/message", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Object> getMessage(@PathVariable String userId) {
        Map<String, Object> resource = new LinkedHashMap<>();
        resource.put("name", "message");
        resource.put("title", "我的消息");

        Map<String, Object> links = new LinkedHashMap<>();
        resource.put("_links", links);
        links.put("self", "/api/userInfo/100/message");
        links.put("describedBy", "/schema/userInfo/message.json");
        List<Object> related = new ArrayList<>();
        links.put("related", related);
        Map<String, Object> promotionMessage = new LinkedHashMap<>();
        related.add(promotionMessage);
        promotionMessage.put("title", "智投推送");
        promotionMessage.put("href", "/api/userInfo/100/promotionMessages");
        promotionMessage.put("describedBy", "/schema/userInfo/promotionMessage.json");
        Map<String, Object> systemMessage = new LinkedHashMap<>();
        related.add(systemMessage);
        systemMessage.put("title", "系统消息");
        systemMessage.put("href", "/api/userInfo/100/systemMessages");
        systemMessage.put("describedBy", "/schema/userInfo/systemMessage.json");

        return new ResponseEntity<>(resource, HttpStatus.OK);
//                "{\n" +
//                "\t\"name\":\"message\",\n" +
//                "\t\"title\":\"我的消息\",\n" +
//                "\t\"_links\": {\n" +
//                "\t\t\"self\": \"/api/100/message\",\t\t\n" +
//                "\t\t\"realted\":[\n" +
//                "\t\t\t{\n" +
//                "\t\t\t\t\"title\":\"智投推送\",\n" +
//                "\t\t\t\t\"href\":\"/api/users/100/promotionMessages\",\n" +
//                "\t\t\t\t\"describedBy\":\"/schema/promotionMessage.json\"\n" +
//                "\t\t\t},\n" +
//                "\t\t\t{\n" +
//                "\t\t\t\t\"title\":\"系统消息\",\n" +
//                "\t\t\t\t\"href\":\"/api/users/100/systemMessages\",\n" +
//                "\t\t\t\t\"describedBy\":\"/schema/systemMessage.json\"\n" +
//                "\t\t\t}\n" +
//                "\t\t]\n" +
//                "\t}\n" +
//                "}";
    }

    @RequestMapping(value = "/userInfo/{userId}/promotionMessages", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Object> getPromotionMessages(@PathVariable String userId){
        Map<String, Object> resource = new LinkedHashMap<>();
        resource.put("title", "智能推送");
        resource.put("_total", 3);
        List<Object> items = new ArrayList<>();
        resource.put("items", items);
        Map<String, Object> message1 = new LinkedHashMap<>();
        items.add(message1);
        message1.put("title", "智能推送A");
        message1.put("time", "2017-08-06 10:00");
        message1.put("content", "智投推送消息内容");

        Map<String, Object> message2 = new LinkedHashMap<>();
        items.add(message2);
        message1.put("title", "智能推送B");
        message1.put("time", "2017-08-06 10:00");
        message1.put("content", "智投推送消息内容");

        Map<String, Object> message3 = new LinkedHashMap<>();
        items.add(message1);
        message1.put("title", "智能推送C");
        message1.put("time", "2017-08-06 10:00");
        message1.put("content", "智投推送消息内容");

        Map<String, Object> links = new LinkedHashMap<>();
        resource.put("_links", links);
        links.put("self", "/api/userInfo/100/promotionMessages");
        links.put("describedBy", "/schema/userInfo/promotionMessage/collection.json");

        return new ResponseEntity<>(resource, HttpStatus.OK);
//        return "{\n" +
//                "    \"title\":\"智能推送\",\t\n" +
//                "\t\"_total\":3,\n" +
//                "\t\"items\": [\n" +
//                "\t\t{\n" +
//                "\t\t\t\"title\": \"智能推送A\",\n" +
//                "\t\t\t\"time\": \"2017-08-06 10:00\",\n" +
//                "\t\t\t\"content\": \"智投推送消息内容\"\n" +
//                "\t\t},\n" +
//                "\t\t{\n" +
//                "\t\t\t\"title\": \"智能推送A\",\n" +
//                "\t\t\t\"time\": \"2017-08-06 10:00\",\n" +
//                "\t\t\t\"content\": \"智投推送消息内容\"\n" +
//                "\t\t},\n" +
//                "\t\t{\n" +
//                "\t\t\t\"title\": \"智能推送A\",\n" +
//                "\t\t\t\"time\": \"2017-08-06 10:00\",\n" +
//                "\t\t\t\"content\": \"智投推送消息内容\"\n" +
//                "\t\t}\n" +
//                "\t],\t\n" +
//                "\t\"_links\": {\n" +
//                "\t\t\"self\":\"/api/users/100/promotionMessages\",\n" +
//                "\t\t\"describedBy\":\"/schema/promotionMessage/collection.json\"\n" +
//                "\t}\n" +
//                "}";
    }

    @RequestMapping(value = "/userInfo/{userId}/systemMessages", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Object> getSystemMessages(@PathVariable String userId){
        Map<String, Object> resource = new LinkedHashMap<>();
        resource.put("title", "智能推送");
        resource.put("_total", 1);
        List<Object> items = new ArrayList<>();
        resource.put("items", items);
        Map<String, Object> message1 = new LinkedHashMap<>();
        items.add(message1);
        message1.put("title", "智能推送A");
        message1.put("time", "2017-08-06 10:00");
        message1.put("content", "系统将于07日凌晨进行维护, 给您带来的不变, 敬请谅解!");

        Map<String, Object> links = new LinkedHashMap<>();
        resource.put("_links", links);
        links.put("self", "/api/userInfo/100/systemMessages");
        links.put("describedBy", "/schema/userInfo/systemMessage/collection.json");

        return new ResponseEntity<>(resource, HttpStatus.OK);

//        return "{\n" +
//                "    \"title\":\"智能推送\",\t\n" +
//                "\t\"_total\":1,\n" +
//                "\t\"items\":[\n" +
//                "\t\t{\n" +
//                "\t\t\t\"title\": \"系统通知\",\n" +
//                "\t\t\t\"time\": \"2017-08-06 10:00\",\n" +
//                "\t\t\t\"content\": \"系统将于07日凌晨进行维护, 给您带来的不变, 敬请谅解!\",\n" +
//                "\t\t}\t\t\n" +
//                "\t],\n" +
//                "\t\"_links\": {\n" +
//                "\t\t\"self\":\"/api/users/100/systemMessages\",\n" +
//                "\t\t\"describedBy\":\"/schema/systemMessage/collection.json\"\n" +
//                "\t}\t\n" +
//                "\t\n" +
//                "}";
    }


}
