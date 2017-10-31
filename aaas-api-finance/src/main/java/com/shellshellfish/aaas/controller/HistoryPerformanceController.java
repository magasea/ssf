package com.shellshellfish.aaas.controller;

import com.shellshellfish.aaas.model.ChartResource;
import com.shellshellfish.aaas.service.HistoryPerformanceService;
import com.shellshellfish.aaas.util.FishChart;
import com.shellshellfish.aaas.util.FishLinks;
import com.shellshellfish.aaas.util.PerformanceResource;
import com.shellshellfish.aaas.util.TableEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api")
public class HistoryPerformanceController {

    private final Logger log = LoggerFactory.getLogger(HistoryPerformanceController.class);

    @Autowired
    private HistoryPerformanceService historyPerformanceService;

    @RequestMapping(value = "/history-performance/{product}", method = RequestMethod.GET)
    public ResponseEntity<PerformanceResource> getHistoryPerformance(@PathVariable String product) {
        log.debug("REST request to get history performance.");

        // HistoryPerformance historyPerformance = historyPerformanceService.getHistoryPerformance();

        PerformanceResource performanceResource = new PerformanceResource();
        TableEntity tableEntity = new TableEntity();
        tableEntity.setHeader(Arrays.asList("", "等级2", "比较基准"));
        List<List<Object>> valueList = new ArrayList<>();
        valueList.add(Arrays.asList("累计收益", "22.65%", "16.81%"));
        valueList.add(Arrays.asList("年化收益", "7.05%", "5.32%"));
        valueList.add(Arrays.asList("最大回撤", "-1.65%", "-4.05%"));
        valueList.add(Arrays.asList("年化收益/最大回撤", 4.27, 1.31));
        valueList.add(Arrays.asList("夏普比率", 2.05, 0.55));
        tableEntity.setValues(valueList);
        tableEntity.setCaption("比较基准=89%中证全债+11%上证指数");
        performanceResource.setPerformance(tableEntity);

        performanceResource.setName("历史收益");
        FishLinks links = new FishLinks();
        links.setSelf("/api/history-performance/abcdef-higjk");
        links.setDescribedBy("/schema/history-performance/item.json");

        List<FishChart> charts = new ArrayList<>();
        charts.add(new FishChart("收益率走势图", "/api/history-performance/abcdef-higjk/charts/1", "/schema/history-performance/yield-chart/item.json"));
        charts.add(new FishChart("最大回撤走势图", "/api/history-performance/abcdef-higjk/charts/2", "/schema/history-performance/max-drawdown-chart/item.json"));
        links.setCharts(charts);
        performanceResource.setLinks(links);

        return new ResponseEntity<>(performanceResource, HttpStatus.OK);
    }

    @RequestMapping(value = "/history-performance/{product}/charts/1", method = RequestMethod.GET)
    public ResponseEntity<ChartResource> getYieldChart(@PathVariable String product) {
        ChartResource chartResource = new ChartResource();
        chartResource.setHorizentalValues(Arrays.asList("2014-10-13", "2017-10-11"));
        chartResource.setVerticalValues(Arrays.asList(-0.1, 0, 0.1, 0.2, 0.3));
        chartResource.setLineValues(Arrays.asList(
                Arrays.asList(0.01, 0.1, 0.12, 0.13, 0.18, 0.20, 0.30),
                Arrays.asList(0.01, 0.12, 0.11, 0.13, 0.17, 0.21, 0.20)
        ));

        FishLinks links = new FishLinks();
        links.setSelf("/api/history-performance/abcdef-higjk/charts/1");
        links.setDescribedBy("/schema/history-performance/yield-chart/item.json");
        chartResource.setLinks(links);

        return new ResponseEntity<>(chartResource, HttpStatus.OK);
    }

    @RequestMapping(value = "/history-performance/{product}/charts/2", method = RequestMethod.GET)
    public ResponseEntity<ChartResource> getMaxDrawdownChart(@PathVariable String product) {
        ChartResource chartResource = new ChartResource();
        chartResource.setHorizentalValues(Arrays.asList("2014-10-13", "2017-10-11"));
        chartResource.setVerticalValues(Arrays.asList(-0.1, 0, 0.1, 0.2, 0.3));
        chartResource.setLineValues(Arrays.asList(
                Arrays.asList(0.01, 0.1, 0.12, 0.13, 0.18, 0.20, 0.30),
                Arrays.asList(0.01, 0.12, 0.11, 0.13, 0.17, 0.21, 0.20)
        ));

        FishLinks links = new FishLinks();
        links.setSelf("/api/history-performance/abcdef-higjk/charts/2");
        links.setDescribedBy("/schema/history-performance/max-drawdown-chart/item.json");
        chartResource.setLinks(links);

        return new ResponseEntity<>(chartResource, HttpStatus.OK);
    }
}
