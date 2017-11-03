package com.shellshellfish.aaas.finance.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/schema")
public class StaticResourceController {
    @RequestMapping(value = "/history-performance/item.json", method = RequestMethod.GET)
    @ResponseBody
    public String getHistoryPerformanceSchema(){
        return "{\n" +
                "\t\"title\": \"历史业绩\",\n" +
                "\t\"description\": \"历史业绩\",\n" +
                "\t\"properties\":{\n" +
                "\t\t\"name\": {\n" +
                "\t\t\t\"title\": \"\",\n" +
                "\t\t\t\"description\": \"资源描述\",\n" +
                "\t\t\t\"type\": \"string\",\n" +
                "\t\t\t\"required\": true,\n" +
                "\t\t\t\"maxLength\": 128,\n" +
                "\t\t\t\"minLength\": 1,\n" +
                "\t\t\t\"ordinal\": 1,\n" +
                "\t\t\t\"pattern\": \"to be defined\"\n" +
                "\t\t},\n" +
                "\t\t\"performance\": {\n" +
                "\t\t\t\"type\":\"table\",\n" +
                "\t\t\t\"description\":\"资料表描述\",\n" +
                "\t\t\t\"properties\": {\n" +
                "\t\t\t\t\"header\": {\n" +
                "\t\t\t\t\t\"type\":\"array\",\n" +
                "\t\t\t\t\t\"items\": {\n" +
                "\t\t\t\t\t\t\"type\": [\"string\", \"string\", \"string\"]\n" +
                "\t\t\t\t\t}\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"values\":{\n" +
                "\t\t\t\t\t\"type\": \"array\",\n" +
                "\t\t\t\t\t\"items\": {\n" +
                "\t\t\t\t\t\t\"type\": [\"string\", \"percentage\", \"percentage\"]\n" +
                "\t\t\t\t\t}\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"caption\": {\n" +
                "\t\t\t\t\t\"type\": \"string\"\n" +
                "\t\t\t\t}\n" +
                "\t\t\t}\n" +
                "\t\t},\n" +
                "\t\t\"_links\": {\n" +
                "\t\t\t\"self\": {\n" +
                "\t\t\t\t\"default\": {\n" +
                "\t\t\t\t\t\"title\": \"历史业绩\",\n" +
                "\t\t\t\t\t\"icon\": \"/images/shared/history-performance.png\"\t\t  \n" +
                "\t\t\t\t}\n" +
                "\t\t\t}\n" +
                "\t\t}\n" +
                "\t}\n" +
                "}";
    }

    @RequestMapping(value = "/history-performance/yield-chart/item.json", method = RequestMethod.GET)
    @ResponseBody
    public String getYieldChartSchema() {
        return "{\n" +
                "\t\"$schema\": \"http://localhost:8080/api/schema#\",\n" +
                "\t\"title\": \"YieldChart\",\n" +
                "\t\"description\": \"收益率走势图\",\n" +
                "\t\"properties\":{\n" +
                "\t\t\"name\": {\n" +
                "\t\t\t\"title\": \"\",\n" +
                "\t\t\t\"description\": \"收益率走势图\",\n" +
                "\t\t\t\"type\": \"string\",\n" +
                "\t\t\t\"required\": true,\n" +
                "\t\t\t\"maxLength\": 128,\n" +
                "\t\t\t\"minLength\": 1,\n" +
                "\t\t\t\"ordinal\": 1,\n" +
                "\t\t\t\"pattern\": \"\"\n" +
                "\t\t},\n" +
                "\t\t\"lineValues\": {\n" +
                "\t\t\t\"title\": \"\",\n" +
                "\t\t\t\"description\": \"画线数据\",\n" +
                "\t\t\t\"type\": \"array\",\n" +
                "\t\t\t\"elementType\": \"array\",\n" +
                "\t\t\t\"properties\": \n" +
                "\t\t\t[\n" +
                "\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\"title\": \"line1\",\n" +
                "\t\t\t\t\t\t\"description\": \"first line\",\n" +
                "\t\t\t\t\t\t\"type\": \"double\"\t\t\t\t\t\t\n" +
                "\t\t\t\t\t},\n" +
                "\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\"title\": \"line2\",\n" +
                "\t\t\t\t\t\t\"description\": \"second line\",\n" +
                "\t\t\t\t\t\t\"type\": \"double\"\t\t\t\t\t\t\n" +
                "\t\t\t\t\t}\n" +
                "\t\t\t]\n" +
                "\t\t\t\t\t\t\n" +
                "\t\t},\n" +
                "\t\t\"legends\": {\n" +
                "\t\t\t\"title\": \"\",\n" +
                "\t\t\t\"description\": \"画线数据\",\n" +
                "\t\t\t\"type\": \"array\",\n" +
                "\t\t\t\"elementType\": \"string\"\n" +
                "\t\t}\n" +
                "\t}\n" +
                "}";
    }

    @RequestMapping(value = "/history-performance/max-drawdown-chart/item.json", method = RequestMethod.GET)
    @ResponseBody
    public String getMaxDrawdownChartSchema() {
        return "{\n" +
                "\t\"$schema\": \"http://localhost:8080/api/schema#\",\n" +
                "\t\"title\": \"YieldChart\",\n" +
                "\t\"description\": \"最大回撤走势图\",\n" +
                "\t\"properties\":{\n" +
                "\t\t\"name\": {\n" +
                "\t\t\t\"title\": \"\",\n" +
                "\t\t\t\"description\": \"最大回撤走势图\",\n" +
                "\t\t\t\"type\": \"string\",\n" +
                "\t\t\t\"required\": true,\n" +
                "\t\t\t\"maxLength\": 128,\n" +
                "\t\t\t\"minLength\": 1,\n" +
                "\t\t\t\"ordinal\": 1,\n" +
                "\t\t\t\"pattern\": \"\"\n" +
                "\t\t},\n" +
                "\t\t\"lineValues\": {\n" +
                "\t\t\t\"title\": \"\",\n" +
                "\t\t\t\"description\": \"画线数据\",\n" +
                "\t\t\t\"type\": \"array\",\n" +
                "\t\t\t\"elementType\": \"array\",\n" +
                "\t\t\t\"properties\": \n" +
                "\t\t\t[\n" +
                "\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\"title\": \"line1\",\n" +
                "\t\t\t\t\t\t\"description\": \"first line\",\n" +
                "\t\t\t\t\t\t\"type\": \"double\"\t\t\t\t\t\t\n" +
                "\t\t\t\t\t},\n" +
                "\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\"title\": \"line2\",\n" +
                "\t\t\t\t\t\t\"description\": \"second line\",\n" +
                "\t\t\t\t\t\t\"type\": \"double\"\t\t\t\t\t\t\n" +
                "\t\t\t\t\t}\n" +
                "\t\t\t]\n" +
                "\t\t\t\t\t\t\n" +
                "\t\t},\n" +
                "\t\t\"legends\": {\n" +
                "\t\t\t\"title\": \"\",\n" +
                "\t\t\t\"description\": \"画线数据\",\n" +
                "\t\t\t\"type\": \"array\",\n" +
                "\t\t\t\"elementType\": \"string\"\n" +
                "\t\t}\n" +
                "\t}\n" +
                "}";
    }

}
