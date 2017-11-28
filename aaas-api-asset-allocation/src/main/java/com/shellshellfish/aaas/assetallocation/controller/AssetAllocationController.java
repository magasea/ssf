package com.shellshellfish.aaas.assetallocation.controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Pageable;
import org.springframework.data.geo.Point;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shellshellfish.aaas.assetallocation.model.Adjustment;
import com.shellshellfish.aaas.assetallocation.model.Estimation;
import com.shellshellfish.aaas.assetallocation.model.ProductInfo;
import com.shellshellfish.aaas.assetallocation.model.RiskControl;
import com.shellshellfish.aaas.assetallocation.util.CollectionResourceWrapper;
import com.shellshellfish.aaas.assetallocation.util.NameValuePair;
import com.shellshellfish.aaas.assetallocation.util.ResourceWrapper;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/api/asset-allocation")
public class AssetAllocationController {

	@ApiOperation("预期年化收益(action=calcExpectedAnnualizedReturn), 预期最大回撤(action=calcExpectedMaxPullback)")
	@RequestMapping(value = "/products/{uuid}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<NameValuePair<String, Double>> getExpectedAnnualizedReturn(@PathVariable String uuid, @RequestParam(required = true, defaultValue = "calcExpectedAnnualizedReturn") String action) {
		
//		if () {
//			
//		} else {
//			
//		}
		
		return new ResponseEntity<>(new NameValuePair<>("预期年化收益", 0.105d), HttpStatus.OK);
	}
	
	@ApiOperation("返回单个理财产品信息")
	@RequestMapping(value = "/products/{uuid}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResourceWrapper<ProductInfo>> getProduct(@PathVariable String uuid,
																	HttpServletRequest request) {
		
		ProductInfo productInfo = new ProductInfo(uuid, "贝贝鱼理财产品A", new Date());
		productInfo.setInvestmentPeriod(new NameValuePair<>("中期", "1-3年"));
		productInfo.setRiskToleranceLevel(new NameValuePair<>("平衡型", "C3"));
		productInfo.setMinAnnualizedReturn(0.035);
		productInfo.setMaxAnnualizedReturn(0.325);
		productInfo.setMinRiskLevel(0.035);
		productInfo.setMaxRiskLevel(0.325);
		
		Map<String, Double> assetsRatios = new LinkedHashMap<>();
		assetsRatios.put("利率债", 0.09d);
		assetsRatios.put("信用债", 0.08d);
		assetsRatios.put("大盘股票", 0.3d);
		assetsRatios.put("香港股票", 0.18d);
		assetsRatios.put("小盘股票", 0.15);
		assetsRatios.put("其他1", 0.15d);
		assetsRatios.put("其他2", 0.05d);
		
		productInfo.setAssetsRatios(assetsRatios);
		
		ResourceWrapper<ProductInfo> resource = new ResourceWrapper<>(productInfo);
		
		return new ResponseEntity<>(resource, HttpStatus.OK);
	}
	
	@ApiOperation("返回所有理财产品信息")
	@ApiImplicitParams({
        @ApiImplicitParam(name = "page", dataType = "string", paramType = "query", defaultValue="0",
                value = "页数(0..N)"),
        @ApiImplicitParam(name = "size", dataType = "string", paramType = "query", defaultValue="20",
                value = "每页条数."),
//        @ApiImplicitParam(name = "sort")
	})
	@RequestMapping(value = "/products", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CollectionResourceWrapper<List<ProductInfo>>> getAllProducts(Pageable pageable, HttpServletRequest request) {
		
		ProductInfo productInfo1 = new ProductInfo(UUID.randomUUID().toString(), "贝贝鱼理财产品A", new Date());
		productInfo1.setInvestmentPeriod(new NameValuePair<>("中期", "1-3年"));
		productInfo1.setRiskToleranceLevel(new NameValuePair<>("平衡型", "C3"));
		productInfo1.setMinAnnualizedReturn(0.035);
		productInfo1.setMaxAnnualizedReturn(0.325);
		productInfo1.setMinRiskLevel(0.035);
		productInfo1.setMaxRiskLevel(0.035);
		Map<String, Double> assetsRatios1 = new LinkedHashMap<>();
		assetsRatios1.put("利率债", 0.09d);
		assetsRatios1.put("信用债", 0.08d);
		assetsRatios1.put("大盘股票", 0.3d);
		assetsRatios1.put("香港股票", 0.18d);
		assetsRatios1.put("小盘股票", 0.15);
		assetsRatios1.put("其他1", 0.15d);
		assetsRatios1.put("其他2", 0.05d);		
		productInfo1.setAssetsRatios(assetsRatios1);
		
		ProductInfo productInfo2 = new ProductInfo(UUID.randomUUID().toString(), "贝贝鱼理财产品B", new Date());
		productInfo2.setInvestmentPeriod(new NameValuePair<>("中期", "1-3年"));
		productInfo2.setRiskToleranceLevel(new NameValuePair<>("平衡型", "C3"));
		productInfo2.setMinAnnualizedReturn(0.035);
		productInfo2.setMaxAnnualizedReturn(0.325);
		productInfo2.setMinRiskLevel(0.035);
		productInfo2.setMaxRiskLevel(0.035);
		Map<String, Double> assetsRatios2 = new LinkedHashMap<>();
		assetsRatios2.put("利率债", 0.09d);
		assetsRatios2.put("信用债", 0.08d);
		assetsRatios2.put("大盘股票", 0.3d);
		assetsRatios2.put("香港股票", 0.18d);
		assetsRatios2.put("小盘股票", 0.15);
		assetsRatios2.put("其他1", 0.15d);
		assetsRatios2.put("其他2", 0.05d);
		
		productInfo2.setAssetsRatios(assetsRatios2);		
		CollectionResourceWrapper<List<ProductInfo>> resource = new CollectionResourceWrapper<>(Arrays.asList(productInfo1, productInfo2));				
		
		return new ResponseEntity<>(resource, HttpStatus.OK);
	}
	
	@ApiOperation("未来预期")
	@ApiImplicitParams({
        @ApiImplicitParam(name = "page", dataType = "string", paramType = "query", defaultValue="0",
                value = "页数(0..N)"),
        @ApiImplicitParam(name = "size", dataType = "string", paramType = "query", defaultValue="20",
                value = "每页条数."),
//        @ApiImplicitParam(name = "sort")
	})
	@RequestMapping(value = "/products/{uuid}/estimations", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResourceWrapper<Map<String, List<Estimation>>>> getFutureExpectations(Pageable pageable, @PathVariable String uuid,
													   @RequestParam(defaultValue="2017-01-01") @DateTimeFormat(pattern="yyyy-MM-dd")Date startDate, @RequestParam(defaultValue="2019-12-12") @DateTimeFormat(pattern="yyyy-MM-dd") Date endDate, 
													   @RequestParam(defaultValue="30") Integer interval) throws ParseException {
		
		Map<String, List<Estimation>> estimations = new HashMap<>();
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		
		estimations.put("利率债", Arrays.asList(new Estimation(dateFormat.parse("2017-01-01"), 0.059), new Estimation(dateFormat.parse("2018-01-01"), 0.069), new Estimation(dateFormat.parse("2019-01-01"), 0.079)));
		estimations.put("信用债", Arrays.asList(new Estimation(dateFormat.parse("2017-01-01"), 0.059), new Estimation(dateFormat.parse("2018-01-01"), 0.069), new Estimation(dateFormat.parse("2019-01-01"), 0.079)));
		estimations.put("大盘股票", Arrays.asList(new Estimation(dateFormat.parse("2017-01-01"), 0.059), new Estimation(dateFormat.parse("2018-01-01"), 0.069), new Estimation(dateFormat.parse("2019-01-01"), 0.079)));
		estimations.put("香港股票", Arrays.asList(new Estimation(dateFormat.parse("2017-01-01"), 0.059), new Estimation(dateFormat.parse("2018-01-01"), 0.069), new Estimation(dateFormat.parse("2019-01-01"), 0.079)));
		return new ResponseEntity<>(new ResourceWrapper<>(estimations), HttpStatus.OK);
	}
	
	@ApiOperation("风险控制")
	@RequestMapping(value = "/products/{uuid}/risk-controls", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CollectionResourceWrapper<List<RiskControl>>> getRiskControls(@PathVariable String uuid) {
		
		CollectionResourceWrapper<List<RiskControl>> resource = new CollectionResourceWrapper<>();
		resource.setItems(Arrays.asList(new RiskControl("历史最大回撤",  -0.0189d, -0.0455d), 
										new RiskControl("股灾1",  -0.0189d, -0.0455d), 
										new RiskControl("股灾2",  -0.0189d, -0.0455d), 
										new RiskControl("熊市1",  -0.0189d, -0.0455d)));
		
		return new ResponseEntity<>(resource, HttpStatus.OK);
	}
	
	@ApiOperation("风险控制手段与通知")
	@RequestMapping(value = "/products/{uuid}/risk-notifications", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CollectionResourceWrapper<List<String>>> getRiskNotifications(@PathVariable String uuid) {
		CollectionResourceWrapper<List<String>> resource = new CollectionResourceWrapper<>();
		resource.setItems(Arrays.asList("全市场的系统性风险通知1", 
										"全市场的系统性风险通知2", 
										"各类资产的市场风险通知XXX", 
										"通知XXX"));
		
		return new ResponseEntity<>(resource, HttpStatus.OK);
	}
	
	@ApiOperation("调仓记录")
	@RequestMapping(value = "/products/{uuid}/adjustments", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CollectionResourceWrapper<List<Adjustment>>> getAdjustments(Pageable pageable, @PathVariable String uuid) {
		CollectionResourceWrapper<List<Adjustment>> resource = new CollectionResourceWrapper<>();
		
		Adjustment adjustment1 = new Adjustment("调整美股港股债券配置比例", new Date());
		Map<String, Double> assetsRatios1 = new LinkedHashMap<>();
		assetsRatios1.put("利率债", 0.09d);
		assetsRatios1.put("信用债", 0.08d);
		assetsRatios1.put("大盘股票", 0.3d);
		assetsRatios1.put("香港股票", 0.18d);
		assetsRatios1.put("小盘股票", 0.15);
		assetsRatios1.put("其他1", 0.15d);
		assetsRatios1.put("其他2", 0.05d);	
		adjustment1.setAssetsRatios(assetsRatios1);
		
		Adjustment adjustment2 = new Adjustment("调整权益类资产配置比例", new Date());
		Map<String, Double> assetsRatios2 = new LinkedHashMap<>();
		assetsRatios2.put("利率债", 0.09d);
		assetsRatios2.put("信用债", 0.08d);
		assetsRatios2.put("大盘股票", 0.3d);
		assetsRatios2.put("香港股票", 0.18d);
		assetsRatios2.put("小盘股票", 0.15);
		assetsRatios2.put("其他1", 0.15d);
		assetsRatios2.put("其他2", 0.05d);	
		adjustment2.setAssetsRatios(assetsRatios2);
		
		resource.setItems(Arrays.asList(adjustment1, adjustment2));
		
		return new ResponseEntity<>(resource, HttpStatus.OK);
	}
	
	@ApiOperation("模拟历史年化业绩与模拟历史年化波动率")
	@RequestMapping(value = "/products/{uuid}/simulated-data", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CollectionResourceWrapper<List<NameValuePair<String, Double>>>> getSimulatedData(@PathVariable String uuid, 
																										   @RequestParam(defaultValue="中期") String investmentPeriod, 
																										   @RequestParam(defaultValue="C3") String riskLevel) {
		CollectionResourceWrapper<List<NameValuePair<String, Double>>> resource = new CollectionResourceWrapper<>();
		resource.setItems(Arrays.asList(new NameValuePair<String, Double>("模拟历史年化业绩", 0.073), 
										new NameValuePair<String, Double>("模拟历史年化波动率", 0.0527)));
		
		return new ResponseEntity<>(resource, HttpStatus.OK);
	}
	
	@ApiOperation("配置收益贡献")
	@RequestMapping(value = "/products/{uuid}/contributions", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CollectionResourceWrapper<List<NameValuePair<String, Double>>>> getContributions(@PathVariable String uuid) {
		CollectionResourceWrapper<List<NameValuePair<String, Double>>> resource = new CollectionResourceWrapper<>();
		resource.setItems(Arrays.asList(new NameValuePair<String, Double>("信用债", 0.0906), 
										new NameValuePair<String, Double>("利率债", 0.0490),
										new NameValuePair<String, Double>("货币", 0.0349),
										new NameValuePair<String, Double>("小盘股票", 0.0269),
										new NameValuePair<String, Double>("香港股票", 0.0073),
										new NameValuePair<String, Double>("美国股票", 0.0073),
										new NameValuePair<String, Double>("黄金股票", 0.0073)
										));
		
		return new ResponseEntity<>(resource, HttpStatus.OK);
	}
	
	@ApiOperation("预期收益率调整 风险率调整  最优组合(有效前沿线)")
	@RequestMapping(value = "/products/{uuid}/optimizations", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResourceWrapper<ProductInfo>> optimize(@PathVariable String uuid, @RequestBody NameValuePair<String, Object> parameter) {
		ProductInfo productInfo = new ProductInfo(uuid, "贝贝鱼理财产品A", new Date());
		productInfo.setInvestmentPeriod(new NameValuePair<>("中期", "1-3年"));
		productInfo.setRiskToleranceLevel(new NameValuePair<>("平衡型", "C3"));
		productInfo.setMinAnnualizedReturn(0.035);
		productInfo.setMaxAnnualizedReturn(0.325);
		productInfo.setMinRiskLevel(0.035);
		productInfo.setMaxRiskLevel(0.325);
		
		Map<String, Double> assetsRatios = new LinkedHashMap<>();
		assetsRatios.put("利率债", 0.091d);
		assetsRatios.put("信用债", 0.081d);
		assetsRatios.put("大盘股票", 0.31d);
		assetsRatios.put("香港股票", 0.18d);
		assetsRatios.put("小盘股票", 0.15);
		assetsRatios.put("其他1", 0.15d);
		assetsRatios.put("其他2", 0.05d);
		
		productInfo.setAssetsRatios(assetsRatios);
		
		ResourceWrapper<ProductInfo> resource = new ResourceWrapper<>(productInfo);
		
		return new ResponseEntity<>(resource, HttpStatus.OK);
	}
	
	@ApiOperation("有效前沿线")
	@RequestMapping(value = "/products/{uuid}/effective-frontier-points", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CollectionResourceWrapper<List<Point>>> getEffectiveFrontier(@PathVariable String uuid) {
		CollectionResourceWrapper<List<Point>> resource = new CollectionResourceWrapper<>();
		resource.setItems(Arrays.asList(new Point(1, -0.5),
										new Point(1.5, -0.4),
										new Point(2.0, -0.3),
										new Point(3.0, -0.2),
										new Point(4.1, -0.1)
										));
		
		return new ResponseEntity<>(resource, HttpStatus.OK);
	}
	
}
