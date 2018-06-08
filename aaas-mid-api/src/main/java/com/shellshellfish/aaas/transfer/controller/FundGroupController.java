package com.shellshellfish.aaas.transfer.controller;

import com.shellshellfish.aaas.common.utils.URLutils;
import com.shellshellfish.aaas.oeminfo.model.JsonResult;
import com.shellshellfish.aaas.transfer.aop.AopTimeResources;
import com.shellshellfish.aaas.transfer.service.FundGroupService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/phoneapi-ssf")
@Api("智投组合相关接口")
public class FundGroupController {

    Logger logger = LoggerFactory.getLogger(FundGroupController.class);

    @Autowired
    RestTemplate restTemplate;

    @Value("${shellshellfish.userinfo-url}")
    private String userinfoUrl;

    @Value("${shellshellfish.data-manager-url}")
    private String datamanagerUrl;

    @Value("${shellshellfish.trade-order-url}")
    private String tradeOrderUrl;
    
    @Value("${shellshellfish.asset-alloction-url}")
    private String assetAlloctionUrl;

    @Autowired
    FundGroupService fundGroupService;

    @ApiOperation("我的智投组合详情")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "uuid", dataType = "String", required = true, value = "用户ID"),
            @ApiImplicitParam(paramType = "query", name = "prodId", dataType = "String", required = true, value = "产品ID"),
            @ApiImplicitParam(paramType = "query", name = "groupId", dataType = "String", value = "groupId", defaultValue = "12"),
            @ApiImplicitParam(paramType = "query", name = "subGroupId", dataType = "String", value = "subGroupId", defaultValue = "120049"),
            @ApiImplicitParam(paramType = "query", name = "buyDate", dataType = "String", value = "买入时间"),
            @ApiImplicitParam(paramType = "query", name = "totals", dataType = "String", value = "组合资产"),
            @ApiImplicitParam(paramType = "query", name = "totalIncome", dataType = "String", value = "累计收益"),
            @ApiImplicitParam(paramType = "query", name = "totalIncomeRate", dataType = "String", value = "累计收益率"),
            @ApiImplicitParam(paramType = "query", name = "count", dataType = "String", value = "统计数量")})
    @RequestMapping(value = "/getMyProductDetail", method = RequestMethod.POST)
    @ResponseBody
    @AopTimeResources
    public JsonResult getProductDetail(@RequestParam String uuid,
                                       @RequestParam String prodId,
                                       @RequestParam String groupId,
                                       @RequestParam String subGroupId,
                                       @RequestParam(required = false) String buyDate,
                                       @RequestParam(required = false) String totals,
                                       @RequestParam(required = false) String totalIncome,
                                       @RequestParam(required = false) String totalIncomeRate,
                                       @RequestParam(required = false) String count) {

    	 //TODO 暂时未区分oemId，待区分时需要输入变量oemid
        String oemId = "1";
        String url = assetAlloctionUrl + "/api/asset-allocation/product-groups/" + groupId
                + "/sub-groups/" + subGroupId + "/" + oemId;
        String status = "";
        Map productMap = restTemplate.getForEntity(url, Map.class).getBody();
        if(productMap!=null&&productMap.containsKey("status")){
        	status = productMap.get("status") + "";
        }
        
        Map result = fundGroupService.getMyProductDetail(prodId, uuid, groupId, subGroupId);

        if (CollectionUtils.isEmpty(result)) {
            logger.error("getMyProductDetail　failed uuid:{}, userProdId:{}", uuid, prodId);
            return new JsonResult(JsonResult.Fail, "获取失败", JsonResult.EMPTYRESULT);
        }
        
        result.put("status", status);
        result.put("groupId", groupId);
        result.put("subGroupId", subGroupId);
        result.put("totals", totals == null ? "" : totals);
        result.put("totalIncome", totalIncome == null ? "" : totalIncome);
        result.put("totalIncomeRate", totalIncomeRate == null ? "" : totalIncomeRate);
        return new JsonResult(JsonResult.SUCCESS, "获取成功", result);

    }

    @ApiOperation("获取组合基准数据")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "groupId", dataType = "long", required = true, value = "组合ID", defaultValue = "1"),
            @ApiImplicitParam(name = "endDate", value = "截止日期", paramType = "query", dataType = "String", defaultValue = "yyyy-MM-dd"),
            @ApiImplicitParam(name = "period", value = "区间 1:3个月,2:6个月 3:一年 4:三年  5:组合成立以来", paramType = "query", dataType = "int", defaultValue = "1")
    })
    @GetMapping(value = "/getGroupBaseLine")
    public Map<String, Object> getGroupBaseLine(
            @NotNull(message = "组合ID不能为空") @RequestParam(value = "groupId") Long groupId,
            @RequestParam(value = "endDate", required = false) String endDate,
            @RequestParam(value = "period", required = false) Integer period) {
        String methodUrl = "/api/datamanager/getGroupBaseLine";
        Map params = new HashMap();
        params.put("groupId", String.valueOf(groupId));
        params.put("endDate", Optional.ofNullable(endDate).orElse(""));
        params.put("period", String.valueOf(Optional.ofNullable(period).orElse(-1)));
        return restTemplate
                .getForEntity(URLutils.prepareParameters(datamanagerUrl + methodUrl, params), Map.class)
                .getBody();
    }

}
