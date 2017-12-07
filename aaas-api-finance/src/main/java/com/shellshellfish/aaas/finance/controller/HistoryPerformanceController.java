package com.shellshellfish.aaas.finance.controller;

import com.shellshellfish.aaas.finance.service.HistoryPerformanceService;
import com.shellshellfish.aaas.finance.model.ChartResource;
import com.shellshellfish.aaas.finance.util.FishChart;
import com.shellshellfish.aaas.finance.util.FishLinks;
import com.shellshellfish.aaas.finance.util.PerformanceResource;
import com.shellshellfish.aaas.finance.util.TableEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
        links.setSelf("/api/history-performance/1");
        links.setDescribedBy("/schema/history-performance/item.json");

        List<FishChart> charts = new ArrayList<>();
        charts.add(new FishChart("收益率走势图", "/api/history-performance/1/charts/1", "/schema/history-performance/yield-chart/item.json"));
        charts.add(new FishChart("最大回撤走势图", "/api/history-performance/1/charts/2", "/schema/history-performance/max-drawdown-chart/item.json"));
        links.setCharts(charts);
        performanceResource.setLinks(links);
        performanceResource.setType("历史业绩");

        return new ResponseEntity<>(performanceResource, HttpStatus.OK);
    }

    @RequestMapping(value = "/history-performance/{product}/charts/1", method = RequestMethod.GET)
    public ResponseEntity<ChartResource> getYieldChart(@PathVariable String product) {
        ChartResource chartResource = new ChartResource();
        chartResource.setName("收益率走势图");
        chartResource.setType("收益率走势图");
//        chartResource.setHorizentalValues(Arrays.asList("2014-10-13", "2017-10-11"));
//        chartResource.setVerticalValues(Arrays.asList(-0.1, 0, 0.1, 0.2, 0.3));
        chartResource.setLineValues(Arrays.asList(
                // line 1
                Arrays.asList(Arrays.asList("2014-10-13", 0.1),
                        Arrays.asList("2015-10-13", 0.3),
                        Arrays.asList("2016-02-13", 0.2)),
                // line 2
                Arrays.asList(Arrays.asList("2016-10-11", 0.2),
                        Arrays.asList("2017-05-11", 0.1),
                        Arrays.asList("2017-10-11", 0.3))

        ));

        chartResource.setLegends(Arrays.asList("等级2", "比较基准=89%中证全债+11%上证指数"));

        FishLinks links = new FishLinks();
        links.setSelf("/api/history-performance/1/charts/1");
        links.setDescribedBy("/schema/history-performance/yield-chart/item.json");
        chartResource.setLinks(links);

        return new ResponseEntity<>(chartResource, HttpStatus.OK);
    }

    @RequestMapping(value = "/history-performance/{product}/charts/2", method = RequestMethod.GET)
    public ResponseEntity<ChartResource> getMaxDrawdownChart(@PathVariable String product) {
        ChartResource chartResource = new ChartResource();
        chartResource.setName("最大回撤走势图");
        chartResource.setType("最大回撤走势图");
//        chartResource.setHorizentalValues(Arrays.asList("2014-10-13", "2017-10-11"));
//        chartResource.setVerticalValues(Arrays.asList(-0.1, 0, 0.1, 0.2, 0.3));
        chartResource.setLineValues(Arrays.asList(
                // line 1
                Arrays.asList(Arrays.asList("2014-10-13", 0.1),
                        Arrays.asList("2015-10-13", 0.3),
                        Arrays.asList("2016-02-13", 0.2)),
                // line 2
                Arrays.asList(Arrays.asList("2016-10-11", 0.2),
                        Arrays.asList("2017-05-11", 0.1),
                        Arrays.asList("2017-10-11", 0.3))
        ));

        FishLinks links = new FishLinks();
        links.setSelf("/api/history-performance/1/charts/2");
        links.setDescribedBy("/schema/history-performance/max-drawdown-chart/item.json");
        chartResource.setLinks(links);

        return new ResponseEntity<>(chartResource, HttpStatus.OK);
    }
}
