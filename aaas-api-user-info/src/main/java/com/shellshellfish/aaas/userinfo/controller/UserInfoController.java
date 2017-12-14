package com.shellshellfish.aaas.userinfo.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shellshellfish.aaas.userinfo.aop.AopLinkResources;
import com.shellshellfish.aaas.userinfo.aop.AopPageResources;
import com.shellshellfish.aaas.userinfo.model.dto.AssetDailyReptDTO;
import com.shellshellfish.aaas.userinfo.model.dto.BankCardDTO;
import com.shellshellfish.aaas.userinfo.model.dto.BankcardDetailBodyDTO;
import com.shellshellfish.aaas.userinfo.model.dto.TradeLogDTO;
import com.shellshellfish.aaas.userinfo.model.dto.UserBaseInfoDTO;
import com.shellshellfish.aaas.userinfo.model.dto.UserInfoAssectsBriefDTO;
import com.shellshellfish.aaas.userinfo.model.dto.UserInfoCompanyInfoDTO;
import com.shellshellfish.aaas.userinfo.model.dto.UserInfoFriendRuleDTO;
import com.shellshellfish.aaas.userinfo.model.dto.UserPersonMsgDTO;
import com.shellshellfish.aaas.userinfo.model.dto.UserPersonalMsgBodyDTO;
import com.shellshellfish.aaas.userinfo.model.dto.UserPortfolioDTO;
import com.shellshellfish.aaas.userinfo.model.dto.UserSysMsgDTO;
import com.shellshellfish.aaas.userinfo.service.UserInfoService;
import com.shellshellfish.aaas.userinfo.utils.DateUtil;
import com.shellshellfish.aaas.userinfo.utils.PageWrapper;
import com.shellshellfish.aaas.userinfo.utils.UserInfoUtils;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/api/userinfo")
public class UserInfoController {

	public static final Logger logger = LoggerFactory.getLogger(UserInfoController.class);
	
	public static String URL_HEAD="/api/userinfo";
	
	@Autowired
	UserInfoService userInfoService;

	/**
	 * 我的 初始页面
	 * @param id
	 * @param userId
	 * @return
	 */
	@ApiOperation("我的 初始页面")
	@ApiResponses({
		@ApiResponse(code=200,message="OK"),
        @ApiResponse(code=400,message="请求参数没填好"),
        @ApiResponse(code=401,message="未授权用户"),        				
		@ApiResponse(code=403,message="服务器已经理解请求，但是拒绝执行它"),
		@ApiResponse(code=404,message="请求路径没有或页面跳转路径不对")   
    })
	@ApiImplicitParams({
		@ApiImplicitParam(paramType="path",name="userUuid",dataType="String",required=true,value="userUuid",defaultValue="")
	})
	@RequestMapping(value = "/users/{userUuid}/initpage", method = RequestMethod.GET)
	@AopLinkResources
	public ResponseEntity<Object> getUserBaseInfo(
			@Valid @NotNull(message="userUuid不能为空") @PathVariable("userUuid") String userUuid
			) throws Exception {
		System.out.println("userUuid is " + userUuid);
		Map<String, Object> result = new HashMap<>();
		Map<String, Object> links = new HashMap<>();
		Map<String, Object> selfmap = new HashMap<>();
		selfmap.put("href", URL_HEAD+"/users/"+userUuid+"/initpage");
		selfmap.put("describedBy","schema//"+URL_HEAD+"/users/"+userUuid+"/initpage.json");
		
		List<BankCardDTO> bankCards =  userInfoService.getUserInfoBankCards(userUuid);
		UserInfoAssectsBriefDTO userInfoAssectsBrief = userInfoService.getUserInfoAssectsBrief(userUuid);
		List<UserPortfolioDTO> userPortfolios = userInfoService.getUserPortfolios(userUuid);
		UserBaseInfoDTO userBaseInfo = userInfoService.getUserInfoBase(userUuid);

		result.put("userCellphone", "手机号码");
		Calendar cal = Calendar.getInstance();
		int year = 0;
		if (null != userBaseInfo.getBirthAge()) {
			cal.setTime(userBaseInfo.getBirthAge());
			year = (cal.get(Calendar.YEAR) % 10) * 10;
		}

		result.put("userBirthAge", "" + year);
		result.put("userCarrier", userBaseInfo.getOccupation());

		result.put("userAssets", userInfoAssectsBrief);
		result.put("userPortfolios", userPortfolios);
		result.put("userBankCards", bankCards);
		links.put("self", selfmap );
		result.put("_links", links);
		result.put("userUuid", userUuid);
		return new ResponseEntity<Object>(result , HttpStatus.OK);
	}

	/**
	 * 我的银行卡 查看页面
	 * @param id
	 * @param cardNumber
	 * @return
	 * @throws Exception
	 */
	@ApiOperation("银行卡信息查看页面")
	@ApiResponses({
		@ApiResponse(code=200,message="OK"),
        @ApiResponse(code=400,message="请求参数没填好"),
        @ApiResponse(code=401,message="未授权用户"),        				
		@ApiResponse(code=403,message="服务器已经理解请求，但是拒绝执行它"),
		@ApiResponse(code=404,message="请求路径没有或页面跳转路径不对")   
    })
	@ApiImplicitParams({
		//@ApiImplicitParam(paramType="query",name="userId",dataType="String",required=true,value="id",defaultValue=""),
		@ApiImplicitParam(paramType="query",name="cardNumber",dataType="String",required=true,value="银行卡号",defaultValue="")
	})
	@RequestMapping(value = "/bankcardinfopage", method = RequestMethod.GET)
	public ResponseEntity<?> getUserBankCards(
			//@Valid @NotNull(message = "id不能为空") @RequestParam("userId") String id,
			@Valid @NotNull(message = "银行卡号不能为空") @Size(max = 20, min = 15) @RequestParam("cardNumber") String cardNumber
			)throws Exception {
		Map<String, Object> result = new HashMap<>();
		Map<String, Object> links = new HashMap<>();
		List<Map> relateList = new ArrayList<Map>();
		if(StringUtils.isEmpty(cardNumber)){
			throw new ServletRequestBindingException("no cardNumber in params");
		}else{
			BankCardDTO bankCard =  userInfoService.getUserInfoBankCard(cardNumber);
			Map<String, Object> selfmap = new HashMap<>();
			selfmap.put("href", URL_HEAD+"/bankcardinfopage?cardNumber="+cardNumber);
			selfmap.put("describedBy","schema//"+URL_HEAD+"/bankcardinfopage.json");
			result.put("bankCard", bankCard);

			HashMap<String,Object> relateditemmap=new HashMap<>();
			relateditemmap = new HashMap<String,Object>();
			relateditemmap.put("href", URL_HEAD+"/unbindbanks");
			relateditemmap.put("name", "unbindbanks");
			relateList.add(relateditemmap);
			links.put("related", relateList);
			
			links.put("self", selfmap );
			result.put("_links", links);
			
			result.put("cardNumber", cardNumber);
			return new ResponseEntity<Object>(result , HttpStatus.OK);
		}
	}


	/**
	 * 个人信息 页面
	 *
	 * @param id
	 * @return
	 */
	@ApiOperation("个人信息 页面")
	@ApiResponses({
		@ApiResponse(code=200,message="OK"),
        @ApiResponse(code=400,message="请求参数没填好"),
        @ApiResponse(code=401,message="未授权用户"),
		@ApiResponse(code=403,message="服务器已经理解请求，但是拒绝执行它"),
		@ApiResponse(code=404,message="请求路径没有或页面跳转路径不对")
    })
	@ApiImplicitParam(paramType="path",name="Uuid",dataType="String",required=true,value="用户Uuid",defaultValue="")
	@RequestMapping(value = "/users/{Uuid}", method = RequestMethod.GET)
	@AopLinkResources
	public ResponseEntity<?> getUserPersonalInfo(
			@Valid @NotNull(message = "用户Uuid不能为空") @PathVariable("Uuid") String userUuid)throws Exception {
		if(StringUtils.isEmpty(userUuid)){
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}else{
			UserBaseInfoDTO userBaseInfo =  userInfoService.getUserInfoBase(userUuid);
			
			Map<String, Object> result = new HashMap<>();
			Map<String, Object> links = new HashMap<>();
			Map<String, Object> selfmap = new HashMap<>();
			selfmap.put("href", URL_HEAD+"/users/"+userUuid);
			selfmap.put("describedBy","schema//"+URL_HEAD+"/users/"+userUuid+".json");
			links.put("self", selfmap);
			result.put("_links", links);
			result.put("userBaseInfo", userBaseInfo);
			result.put("uuid", userUuid);
			return new ResponseEntity<Object>(result , HttpStatus.OK);
		}
	}

	/**
	 * 银行卡 添加银行卡 下一步 action
	 * @param cardNumber
	 * @return
	 */
	@ApiOperation("银行卡 添加银行卡 下一步")
	@ApiResponses({
		@ApiResponse(code=200,message="OK"),
        @ApiResponse(code=400,message="请求参数没填好"),
        @ApiResponse(code=401,message="未授权用户"),        				
		@ApiResponse(code=403,message="服务器已经理解请求，但是拒绝执行它"),
		@ApiResponse(code=404,message="请求路径没有或页面跳转路径不对")   
    })
	@ApiImplicitParam(paramType="query",name="cardNumber",dataType="String",required=true,value="银行卡号",defaultValue="")
	@RequestMapping(value = "/users/{userUuid}/bankcardinfopage", method = RequestMethod.GET)
	public ResponseEntity<?> preCheckBankCardWithCardNumber(
			@Valid @NotNull(message = "userUuid不能为空") @PathVariable("userUuid") String userUuid,
			@Valid @NotNull(message = "银行卡号不能为空") @Size(max = 20, min = 15) @RequestParam("cardNumber") String cardNumber
			){
		if(StringUtils.isEmpty(cardNumber) ){
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}else{
			if(!UserInfoUtils.matchLuhn(cardNumber)){
				return new ResponseEntity<Object>("银行卡号不正确." , HttpStatus.NOT_ACCEPTABLE);
			}
			
			return new ResponseEntity<Object>("/users/"+userUuid+"/bankcards?cardNumber="+cardNumber,HttpStatus.OK);
		}
	}
	
	/**
	 * 银行卡 添加银行卡  下一步 初始页面
	 * @param cardNumber
	 * @return
	 */
	@ApiOperation("银行卡 添加银行卡 下一步 初始页面")
	@ApiResponses({ 
		@ApiResponse(code = 200, message = "OK"), 
		@ApiResponse(code = 400, message = "请求参数没填好"),
		@ApiResponse(code = 401, message = "未授权用户"), 
		@ApiResponse(code = 403, message = "服务器已经理解请求，但是拒绝执行它"),
		@ApiResponse(code = 404, message = "请求路径没有或页面跳转路径不对") })
	@ApiImplicitParam(paramType = "path", name = "userUuid", dataType = "String", required = true, value = "id", defaultValue = "")
	@RequestMapping(value = "/users/{userUuid}/bankcardpage", method = RequestMethod.GET)
	public ResponseEntity<?> addPreCheckBankCardWithCardNumber(
			@Valid @NotNull(message = "id不能为空") @PathVariable("userUuid") String userUuid) {

		Map<String, Object> result = new HashMap<>();
		Map<String, Object> links = new HashMap<>();
		Map<String, Object> relateditemmap = new HashMap<>();
		Map<String, Object> selfmap = new HashMap<>();
		List<Map> relateList = new ArrayList<Map>();
		
		result.put("bankcardnum", "");

		selfmap.put("href", URL_HEAD + "/users/" + userUuid + "/bankcardpage");
		selfmap.put("describedBy", "schema//"+URL_HEAD + "/users/" + userUuid + "/bankcardpage.json");

		relateditemmap = new HashMap<String, Object>();
		relateditemmap.put("href", URL_HEAD + "/users/" + userUuid + "/bankcards");
		relateditemmap.put("name", "bankcards");
		relateList.add(relateditemmap);
		Map<String, Object> selectBankLink = new HashMap<>();
		selectBankLink.put("name", "supportbankcards");
		selectBankLink.put("href", URL_HEAD+"/supportbankcards");
		relateList.add(selectBankLink);
		
		HashMap<String,Object> executemap=new HashMap<>();
		executemap.put("href", URL_HEAD+"/users/"+userUuid+"/bankcardsubmitpage?cardNumber=\"\""); 
		executemap.put("describedBy", URL_HEAD+"/users/"+userUuid+"/bankcardsubmitpage.json");
		executemap.put("method", "POST");
		executemap.put("name", "bankcardinfopage");
		
		links.put("execute", executemap);
		links.put("related", relateList);
		links.put("self", selfmap);
		result.put("_links", links);
		
		return new ResponseEntity<Object>(result, HttpStatus.OK);
	}
	
	/**
	 *银行卡 添加银行卡 提交 初始页面
	 * @param cardNumber
	 * @return
	 */
	@ApiOperation("银行卡 添加银行卡 提交 初始页面")
	@ApiResponses({
		@ApiResponse(code = 200, message = "OK"), 
		@ApiResponse(code = 400, message = "请求参数没填好"),
		@ApiResponse(code = 401, message = "未授权用户"), 
		@ApiResponse(code = 403, message = "服务器已经理解请求，但是拒绝执行它"),
		@ApiResponse(code = 404, message = "请求路径没有或页面跳转路径不对") })
	@ApiImplicitParams({
		@ApiImplicitParam(paramType = "path", name = "userUuid", dataType = "String", required = true, value = "id", defaultValue = ""),
		@ApiImplicitParam(paramType="query",name="cardNumber",dataType="String",required=true,value="银行卡号",defaultValue="")
	})
	@RequestMapping(value = "/users/{userUuid}/bankcardsubmitpage", method = RequestMethod.GET)
	public ResponseEntity<?> bankCardSubmitInit(
			@Valid @NotNull(message = "id不能为空") @PathVariable("userUuid") String userUuid,
			@Valid @NotNull(message = "银行卡NUM不能为空") @RequestParam("cardNumber") String cardNumber) {

		Map<String, Object> result = new HashMap<>();
		Map<String, Object> links = new HashMap<>();
		Map<String, Object> relateditemmap = new HashMap<>();
		Map<String, Object> selfmap = new HashMap<>();
		List<Map> relateList = new ArrayList<Map>();
		
		result.put("userUuid", userUuid);
		result.put("cardName", "");
		result.put("cardNumber", cardNumber);
		result.put("identityCard", "");
		result.put("cardCellphone", "");
		result.put("verificationCode", "");

		selfmap.put("href", URL_HEAD + "/users/" + userUuid + "/bankcardnumpage?cardNumber="+cardNumber);
		selfmap.put("describedBy", "schema//"+URL_HEAD + "/users/" + userUuid + "/bankcardnumpage.json");

		relateditemmap = new HashMap<String, Object>();
		relateditemmap.put("href", URL_HEAD + "/telnums");
		relateditemmap.put("name", "telnums");
		relateList.add(relateditemmap);
		
		HashMap<String,Object> executemap=new HashMap<>();
		executemap.put("href", URL_HEAD+"/users/"+userUuid+"/bankcards"); 
		executemap.put("describedBy", URL_HEAD+"/users/"+userUuid+"/bankcardsjson");
		executemap.put("method", "POST");
		executemap.put("name", "bankcards");
		
		links.put("execute", executemap);
		links.put("related", relateList);
		links.put("self", selfmap);
		result.put("_links", links);
		return new ResponseEntity<Object>(result, HttpStatus.OK);
	}
 

	/**
	 * 添加银行卡	提交action
	 * @param id
	 * @param bankcardDetailVo
	 * @return
	 * @throws Exception
	 */
	@ApiOperation("银行卡 添加银行卡 提交")
	@ApiResponses({
		@ApiResponse(code=200,message="OK"),
        @ApiResponse(code=400,message="请求参数没填好"),
        @ApiResponse(code=401,message="未授权用户"),        				
		@ApiResponse(code=403,message="服务器已经理解请求，但是拒绝执行它"),
		@ApiResponse(code=404,message="请求路径没有或页面跳转路径不对")   
    })
	@ApiImplicitParams({
		@ApiImplicitParam(paramType="path",name="userUuid",dataType="String",required=true,value="id",defaultValue=""),
		@ApiImplicitParam(name="bankcardDetailVo", value ="银行卡信息",required=true,paramType="body",dataType="BankcardDetailVo")
	})
	@RequestMapping(value = "/users/{userUuid}/bankcards", method = RequestMethod.POST)
	public ResponseEntity<?> addBankCardWithDetailInfo(
			@Valid @NotNull(message = "不能为空") @PathVariable("userUuid") String userUuid,
			@RequestBody BankcardDetailBodyDTO bankcardDetailVo) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		// Convert POJO to Map
		Map<String, Object> params = mapper.convertValue(bankcardDetailVo, new TypeReference<Map<String, Object>>() {
		});
		params.put("userUuid", userUuid);
		if (CollectionUtils.isEmpty(params)) {
			throw new ServletRequestBindingException("no cardNumber in params");
		}
		params.forEach((k, v) -> {
			if (null == v || StringUtils.isEmpty(v.toString())) {
				throw new IllegalArgumentException("no " + k.toString() + "'s value in params");
			}
		});
		BankCardDTO bankCard = userInfoService.createBankcard(params);
		if (bankCard == null) {
			return new ResponseEntity<Object>(
					URL_HEAD + "/users/" + userUuid + "/bankcardpage?cardNumber=" + bankcardDetailVo.getCardNumber(),
					HttpStatus.NO_CONTENT);
		} else {
			return new ResponseEntity<Object>(URL_HEAD + "/initpage", HttpStatus.OK);
		}
	}

	/**
	 * 列出银行卡	action
	 * @param id
	 * @param bankcardDetailVo
	 * @return
	 * @throws Exception
	 */
	@ApiOperation("用户的银行卡集合")
	@ApiResponses({
			@ApiResponse(code=200,message="OK"),
			@ApiResponse(code=400,message="请求参数没填好"),
			@ApiResponse(code=401,message="未授权用户"),
			@ApiResponse(code=403,message="服务器已经理解请求，但是拒绝执行它"),
			@ApiResponse(code=404,message="请求路径没有或页面跳转路径不对")
	})
	@ApiImplicitParams({
			@ApiImplicitParam(paramType="path",name="userUuid",dataType="String",required=true,defaultValue=""),
	})
	@RequestMapping(value = "/users/{userUuid}/bankcards", method = RequestMethod.GET)
	//@RequestMapping(value = "/userinfo/users/supportbankcards", method = RequestMethod.GET)
	public ResponseEntity<?> bankcardsInfo(@Valid @NotNull(message="不能为空")
	@PathVariable("userUuid") String userUuid) throws Exception {

		List<BankCardDTO> bankCards = userInfoService.getUserInfoBankCards(userUuid);
		
		Map<String, Object> result = new HashMap<>();
		Map<String, Object> links = new HashMap<>();
		Map<String, Object> selfmap = new HashMap<>();
		List<Map> relateList = new ArrayList<Map>();
		
		selfmap.put("href", URL_HEAD+"/"+userUuid+"/bankcards" );
		selfmap.put("describedBy","schema//"+URL_HEAD+"/"+userUuid+"/bankcards.json");
		
		result.put("_items", bankCards);
		result.put("_total", 0);
		if(bankCards!=null){
			result.put("_total", bankCards.size());
		}
		
		HashMap<String,Object> relateditemmap=new HashMap<>();
		relateditemmap = new HashMap<String,Object>();
		relateditemmap.put("href", URL_HEAD+"/users/"+userUuid+"/bankcardnum");
		relateditemmap.put("name", "bankcardnum");
		relateList.add(relateditemmap);
		links.put("related", relateList);
		
		links.put("self", selfmap );
		result.put("_links", links);
		return new ResponseEntity<Object>(result , HttpStatus.OK);

	}
	
	/**
	 * 支持的银行卡
	 * @param id
	 * @param bankcardDetailVo
	 * @return
	 * @throws Exception
	 */
	@ApiOperation("支持的银行卡查看")
	@ApiResponses({
			@ApiResponse(code=200,message="OK"),
			@ApiResponse(code=400,message="请求参数没填好"),
			@ApiResponse(code=401,message="未授权用户"),
			@ApiResponse(code=403,message="服务器已经理解请求，但是拒绝执行它"),
			@ApiResponse(code=404,message="请求路径没有或页面跳转路径不对")
	})
	@RequestMapping(value = "/users/supportbankcards", method = RequestMethod.GET)
	public ResponseEntity<?> supportBankCards() throws Exception {
		HashMap<String, Object> rsmap = new HashMap<String, Object>();
		Map<String, Object> links = new HashMap<>();
		List<Map> rsList = new ArrayList<Map>();
		HashMap<String, Object> arrayMap = new HashMap<>();
		arrayMap = new HashMap<String, Object>();
		arrayMap.put("bank", "工商银行");
		rsList.add(arrayMap);
		
		arrayMap = new HashMap<String, Object>();
		arrayMap.put("bank", "广发银行");
		rsList.add(arrayMap);
		rsmap.put("_items", rsList);
		rsmap.put("_total", rsList.size());
		//self
		Map<String, Object> selfmap = new HashMap<>();
		selfmap.put("href", URL_HEAD+"/users/supportbankcards");
		selfmap.put("describedBy","schema//"+URL_HEAD+"/users/supportbankcards.json");
		links.put("self", selfmap );
		
		rsmap.put("_links", links);
		return new ResponseEntity<Object>(rsmap , HttpStatus.OK);
	}
	
	/**
	 * 我的消息
	 * @param id
	 * @param bankcardDetailVo
	 * @return
	 * @throws Exception
	 */
	@ApiOperation("我的消息")
	@ApiResponses({
			@ApiResponse(code=200,message="OK"),
			@ApiResponse(code=400,message="请求参数没填好"),
			@ApiResponse(code=401,message="未授权用户"),
			@ApiResponse(code=403,message="服务器已经理解请求，但是拒绝执行它"),
			@ApiResponse(code=404,message="请求路径没有或页面跳转路径不对")
	})
	@ApiImplicitParams({
		@ApiImplicitParam(paramType="path",name="userUuid",dataType="String",required=true,defaultValue=""),
	})
	@RequestMapping(value = "/users/{userUuid}/messages", method = RequestMethod.GET)
	public ResponseEntity<?> messages(
			@Valid @NotNull(message = "userUuid不能为空") @PathVariable("userUuid") String userUuid
			) throws Exception {
		HashMap<String, Object> rsmap = new HashMap<String, Object>();
		Map<String, Object> links = new HashMap<>();
		List<Map> rsList = new ArrayList<Map>();
		Map<String, Object> relatedMap = new HashMap<>();
		// 智投消息
		relatedMap.put("href", URL_HEAD + "/users/" + userUuid + "/investmentmessages/{messageid}");
		relatedMap.put("name", "investmentmessages");
		rsList.add(relatedMap);
		// 系统消息
		relatedMap = new HashMap<>();
		relatedMap.put("href", URL_HEAD + "/users/" + userUuid + "/systemmessages");
		relatedMap.put("name", "systemmessages");
		rsList.add(relatedMap);

		// self
		Map<String, Object> selfmap = new HashMap<>();
		selfmap.put("href", URL_HEAD + "/users/" + userUuid + "/messages");
		selfmap.put("describedBy", "schema//" + URL_HEAD + "/users/" + userUuid + "/messages.json");
		links.put("self", selfmap);

		links.put("related", rsList);
		rsmap.put("_links", links);
		return new ResponseEntity<Object>(rsmap, HttpStatus.OK);
	}

	/**
	 * 个人资产总览 首页
	 * @param id
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@ApiOperation("个人资产总览")
	@ApiResponses({
		@ApiResponse(code=200,message="OK"),
        @ApiResponse(code=400,message="请求参数没填好"),
        @ApiResponse(code=401,message="未授权用户"),        				
		@ApiResponse(code=403,message="服务器已经理解请求，但是拒绝执行它"),
		@ApiResponse(code=404,message="请求路径没有或页面跳转路径不对")   
    })

	@RequestMapping(value = "/users/{userUuid}/assetoverviewpage", method = RequestMethod
			.GET)
	public ResponseEntity<?> getUserAssetsOverview(@Valid @NotNull(message="不能为空") @PathVariable
			("userUuid") String userUuid, @RequestParam("beginDate") String bgDate, @RequestParam
			("endDate") String edDate)

			throws Exception {
		Map<String, Object> result = new HashMap<>();
		Map<String, Object> links = new HashMap<>();
		List<Map> relateList = new ArrayList<Map>();
		SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-DD");
		sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
		Date beginDate = null;
		Date endDate = null;
		Long beginTimeLong;
		Long endTimeLong;
		try {
			beginDate = sdf.parse(bgDate);

		}catch (ParseException ex) {
			ex.printStackTrace();
		}
		if (beginDate == null && !StringUtils.isEmpty(bgDate )) {
			// Invalid date format
			//maybe frontend send long time value to backend
			beginTimeLong = Long.getLong(bgDate);
			endTimeLong = Long.getLong(edDate);
		} else {
			// Valid date format
			beginTimeLong = DateUtil.getDateOneDayBefore(beginDate);
			endDate = sdf.parse(edDate);
			endTimeLong = endDate.getTime();
		}

		List<AssetDailyReptDTO> assetDailyRepts = userInfoService.getAssetDailyRept(userUuid, beginTimeLong, endTimeLong);
		
		result.put("_items", assetDailyRepts);
		if(assetDailyRepts!=null){
			result.put("_total", assetDailyRepts.size());
		}
		Map<String, Object> selfmap = new HashMap<>();
		selfmap.put("href", URL_HEAD+"/users/"+userUuid+"/assetoverviewpage?beginDate="+bgDate+"&endDate="+edDate);
		selfmap.put("describedBy","schema//"+URL_HEAD+"/users/"+userUuid+"/assetoverviewpage.json");
		links.put("self", selfmap );
		
		HashMap<String,Object> relateditemmap=new HashMap<>();
		relateditemmap = new HashMap<String,Object>();
		relateditemmap.put("href", URL_HEAD+"/users/"+userUuid+"/incometrendchart");
		relateditemmap.put("name", "incometrendchart");
		relateList.add(relateditemmap);
		
		links.put("related", relateList);
		result.put("_links", links);

		return new ResponseEntity<Object>(result , HttpStatus.OK);
	}

//	//TODO
//	//@RequestMapping(value = "/userinfo/userassets/overview/data", method = RequestMethod.POST)
//	public ResponseEntity<?> addUserAssetsOverview(@RequestBody AssetDailyRept assetDailyRept)
//			throws Exception {
//
//		AssetDailyRept assetDailyReptRlt =
//				 userInfoService.addAssetDailyRept(assetDailyRept);
//		return new ResponseEntity<Object>(assetDailyReptRlt , HttpStatus.OK);
//	}

	/**
	 * 我的消息 智投推送 首页
	 * @param userUuid
	 * @return
	 * @throws Exception
	 */
	@ApiOperation("我的消息 智投推送 首页")
	@ApiResponses({
		@ApiResponse(code=200,message="OK"),
        @ApiResponse(code=400,message="请求参数没填好"),
        @ApiResponse(code=401,message="未授权用户"),        				
		@ApiResponse(code=403,message="服务器已经理解请求，但是拒绝执行它"),
		@ApiResponse(code=404,message="请求路径没有或页面跳转路径不对")   
    })
	@ApiImplicitParams({
		@ApiImplicitParam(paramType="path",name="userUuid",dataType="String",required=true,value="用户uuid",defaultValue="")
	})
	@RequestMapping(value = "/users/{userUuid}/investmentmessages", method = RequestMethod.GET)
	public ResponseEntity<?> getPersonalInvstMsg(
			@Valid @NotNull(message = "userUuid不可为空") @PathVariable(name = "userUuid") String userUuid
			)throws Exception {
		List<UserPersonMsgDTO> userPersonMsgs =  userInfoService.getUserPersonMsg(userUuid);
		Map<String, Object> result = new HashMap<>();
		Map<String, Object> links = new HashMap<>();
		HashMap<String,Object> relateditemmap=new HashMap<>();
		List<Map> relateList = new ArrayList<Map>();
		result.put("_items", userPersonMsgs);
		result.put("_total", 0);
		if(userPersonMsgs!=null){
			result.put("_total", userPersonMsgs.size());
		}
		
		Map<String, Object> selfmap = new HashMap<>();
		selfmap.put("href", URL_HEAD+"/"+ userUuid+"/message/investmentmessages");
		selfmap.put("describedBy","schema//"+URL_HEAD+"/"+ userUuid+"/message/investmentmessages.json");
		
		relateditemmap = new HashMap<String,Object>();
		relateditemmap.put("href", URL_HEAD+"/users/"+userUuid+"/investmentmessages");
		relateditemmap.put("name", "investmentmessages");
		relateList.add(relateditemmap);
		links.put("self", selfmap );
		links.put("related", relateList);
		result.put("_links", links);
		result.put("userUuid", userUuid);
		return new ResponseEntity<Object>(result , HttpStatus.OK);
	}

	/**
	 * 我的消息 系统消息
	 * @param userUuid
	 * @return
	 * @throws Exception
	 */
	@ApiOperation("我的消息 系统消息")
	@ApiResponses({
		@ApiResponse(code=200,message="OK"),
        @ApiResponse(code=400,message="请求参数没填好"),
        @ApiResponse(code=401,message="未授权用户"),        				
		@ApiResponse(code=403,message="服务器已经理解请求，但是拒绝执行它"),
		@ApiResponse(code=404,message="请求路径没有或页面跳转路径不对")   
    })
	@ApiImplicitParam(paramType="path",name="userUuid",dataType="String",required=true,value="用户uuid",defaultValue="")
	@RequestMapping(value = "/users/{userUuid}/systemmessages", method = RequestMethod
			.GET)
	public ResponseEntity<?> getSystemMsg(@Valid @NotNull(message = "userUuid不可为空")@PathVariable String userUuid)
			throws Exception {
		List<UserSysMsgDTO> userSysMsgs = userInfoService.getUserSysMsg(userUuid);
		Map<String, Object> result = new HashMap<>();
		Map<String, Object> links = new HashMap<>();
		result.put("_items", userSysMsgs);
		result.put("_total", 0);
		if(userSysMsgs!=null){
			result.put("_total", userSysMsgs.size());
		}
		
		Map<String, Object> selfmap = new HashMap<>();
		selfmap.put("href", URL_HEAD+"/users/"+userUuid+"/systemmessages" );
		selfmap.put("describedBy","schema//"+URL_HEAD+"/users/"+userUuid+"/systemmessages.json");
		
		links.put("self", selfmap );
		result.put("_links", links);
		return new ResponseEntity<Object>(result , HttpStatus.OK);
	}

	/**
	 * 我的消息 智投推送 action
	 * @param userPersonalMsgVo
	 * @return
	 * @throws Exception
	 */
	@ApiOperation("我的消息 智投推送 link")
	@ApiResponses({
		@ApiResponse(code=200,message="OK"),
        @ApiResponse(code=400,message="请求参数没填好"),
        @ApiResponse(code=401,message="未授权用户"),        				
		@ApiResponse(code=403,message="服务器已经理解请求，但是拒绝执行它"),
		@ApiResponse(code=404,message="请求路径没有或页面跳转路径不对")   
    })
	@ApiImplicitParams({
		@ApiImplicitParam(paramType="path",name="userUuid",dataType="String",required=true,value="用户uuid",defaultValue=""),
		@ApiImplicitParam(paramType="path",name="id",dataType="String",required=true,value="message ID",defaultValue=""),
		@ApiImplicitParam(name="userPersonalMsgVo", value ="推送内容",required=true,paramType="body",dataType="UserPersonalMsgVo")
	})
	@RequestMapping(value = "/users/{userUuid}/investmentmessages/{id}", method =
			RequestMethod.PATCH)
	public ResponseEntity<?> updatePersonalMsg(
			@Valid @NotNull(message = "userUuid不可为空")@PathVariable String userUuid,
			@Valid @NotNull(message = "id")@PathVariable String id,
			@RequestBody UserPersonalMsgBodyDTO userPersonalMsgVo)
			throws Exception {	
		//id message ID
		Boolean result =  userInfoService.updateUserPersonMsg(id, userUuid, userPersonalMsgVo.getReadedStatus());
		if(!result){
			return new ResponseEntity<Object>("更新内容失败",HttpStatus.UNAUTHORIZED);
		} else {
			return new ResponseEntity<Object>(URL_HEAD+"/message/updateinvestmentmessages/investmentmessages?userUuid="+userUuid , HttpStatus.OK);
		}
	}

	/**
	 * 交易记录
	 * @param userUuid
	 * @param pageNum
	 * @param pageSize
	 * @param sortField
	 * @return
	 * @throws Exception
	 */
	@ApiOperation("交易记录")
	@ApiResponses({
		@ApiResponse(code=200,message="OK"),
        @ApiResponse(code=400,message="请求参数没填好"),
        @ApiResponse(code=401,message="未授权用户"),        				
		@ApiResponse(code=403,message="服务器已经理解请求，但是拒绝执行它"),
		@ApiResponse(code=404,message="请求路径没有或页面跳转路径不对")   
    })
	@ApiImplicitParams({
		@ApiImplicitParam(paramType="path",name="userUuid",dataType="String",required=true,value="id",defaultValue=""),
		@ApiImplicitParam(paramType="query",name="size",dataType="Long",value="每页显示记录数",defaultValue="25"),
		@ApiImplicitParam(paramType="query",name="page",dataType="Long",value="显示页数（默认第0页开始）",defaultValue="0"),
		@ApiImplicitParam(paramType="query",name="sort",dataType="String",value="排序条件",defaultValue="id")
	})
	@RequestMapping(value = "/users/{userUuid}/traderecords", method = RequestMethod.GET)
	@AopPageResources
	public ResponseEntity<PageWrapper<TradeLogDTO>> getTradLogsOfUser(
			@PathVariable String userUuid, Pageable pageable,
			@RequestParam(value = "size") Long size, 
			@RequestParam(value = "page", defaultValue = "0") Long page,
			@RequestParam(value = "sort") String sort) throws Exception {
		Page<TradeLogDTO> pages = userInfoService.findByUserId(userUuid, pageable);
		Map<String, Object> selfMap = new HashMap<String, Object>();
		Map<String, Object> self = new HashMap<String, Object>();
		selfMap.put("name", "test");
		selfMap.put("href", URL_HEAD + "/users/"+userUuid+"/traderecords");
		selfMap.put("describedBy", URL_HEAD + "/users/"+userUuid+"/traderecords.json");
		self.put("self", selfMap);
		if (pages == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		PageWrapper<TradeLogDTO> pageWrapper = new PageWrapper<>(pages);
		pageWrapper.set_links(self);
		return new ResponseEntity<>(pageWrapper,HttpStatus.OK);
	}

//	@ApiImplicitParams({
//		@ApiImplicitParam(paramType="path",name="userUuid",dataType="String",required=true,value="userUuid",defaultValue=""),
//		@ApiImplicitParam(paramType="query",name="bankId",dataType="Long",required=true,value="银行ID",defaultValue="")
//    })
//	@ApiResponses({
//		@ApiResponse(code=200,message="OK"),
//        @ApiResponse(code=400,message="请求参数没填好"),
//        @ApiResponse(code=401,message="未授权用户"),
//		@ApiResponse(code=403,message="服务器已经理解请求，但是拒绝执行它"),
//		@ApiResponse(code=404,message="请求路径没有或页面跳转路径不对")
//    })
//	@RequestMapping(value = "/userinfo/companyinfo/{userUuid}", method = RequestMethod.GET)
//	public ResponseEntity<?> getCompanyInfo(@PathVariable String userUuid, @RequestParam( required = false) Long bankId)
//			throws Exception {
//
//		UserInfoCompanyInfo userInfoCompanyInfo = userInfoService.getCompanyInfo(userUuid, bankId);
//		Map<String, Object> result = new HashMap<>();
//		Map<String, Object> selfmap = new HashMap<>();
//		Map<String, Object> links = new HashMap<>();
//		result.put("_items", userInfoCompanyInfo);
//		result.put("_page","");
//
//		selfmap.put("href", "/api/userinfo/companyinfo/{userUuid}" );
//		selfmap.put("describedBy","schema//api/userinfo/companyinfo.json/{userUuid}");
//		links.put("self", selfmap );
//		result.put("_links", links);
//		return new ResponseEntity<Object>(result , HttpStatus.OK);
//	}
	
	/**
	 * 邀请规则
	 * @param userUuid
	 * @param bankId
	 * @return
	 * @throws Exception
	 */
	@ApiOperation("邀请规则")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType="path",name="bankid",dataType="Long",required=false,value="银行ID",defaultValue="")
	})
	@ApiResponses({
			@ApiResponse(code=200,message="OK"),
			@ApiResponse(code=400,message="请求参数没填好"),
			@ApiResponse(code=401,message="未授权用户"),
			@ApiResponse(code=403,message="服务器已经理解请求，但是拒绝执行它"),
			@ApiResponse(code=404,message="请求路径没有或页面跳转路径不对")
	})
	@RequestMapping(value = "/friendrules/{bankid}", method = RequestMethod.GET)
	public ResponseEntity<?> getFriendRules(
			@Valid @NotNull(message = "userUuid不可为空")@PathVariable Long bankid)
			throws Exception {

		List<UserInfoFriendRuleDTO> userInfoFriendRules = userInfoService.getUserInfoFriendRules(bankid);
		Map<String, Object> result = new HashMap<>();
		Map<String, Object> links = new HashMap<>();
		result.put("_items", userInfoFriendRules);
		result.put("_total", 0);
		if(userInfoFriendRules!=null){
			result.put("_total", userInfoFriendRules.size());
		}
		//result.put("_page","");
		
		Map<String, Object> selfmap = new HashMap<>();
		selfmap.put("href", URL_HEAD+"/friendrules/"+bankid);
		selfmap.put("describedBy","schema//"+URL_HEAD+"/friendrules.json");
		
		links.put("self", selfmap );
		result.put("_links", links);
		return new ResponseEntity<Object>(result , HttpStatus.OK);
	}

	/**
	 * 好友邀请
	 * @param userUuid
	 * @param bankId
	 * @return
	 * @throws Exception
	 */
	@ApiOperation("好友邀请")
	@ApiImplicitParams({
		@ApiImplicitParam(paramType="path",name="userUuid",dataType="String",required=true,value="用户Uuid",defaultValue=""),
		@ApiImplicitParam(paramType="query",name="bankId",dataType="Long",required=true,value="银行ID",defaultValue="")
	})
	@ApiResponses({
		@ApiResponse(code=200,message="OK"),
		@ApiResponse(code=400,message="请求参数没填好"),
		@ApiResponse(code=401,message="未授权用户"),
		@ApiResponse(code=403,message="服务器已经理解请求，但是拒绝执行它"),
		@ApiResponse(code=404,message="请求路径没有或页面跳转路径不对")
	})
	@RequestMapping(value = "/users/{userUuid}/friendinvitationpage", method = RequestMethod.GET)
	public ResponseEntity<?> getFriendsInvationLinks(
			@Valid @NotNull(message="用户Uuid") @PathVariable("userUuid") String userUuid,
			@Valid @NotNull(message="银行ID") @RequestParam( required = false) Long bankId)
			throws Exception {
		//:TODO 这段分享朋友邀请好友的做法是要改的
		List<Map> friendIvtList = new ArrayList<>();
		Map<String, Object> friendIvtLinks = new HashMap<>();
		friendIvtLinks.put("href", "http://wx.qq.com");
		friendIvtLinks.put("name", "wechat");
		friendIvtList.add(friendIvtLinks);
		friendIvtLinks.put("href", "http://weibo.com");
		friendIvtLinks.put("name", "weibo");
		friendIvtList.add(friendIvtLinks);
		friendIvtLinks.put("href", "http://qq.com");
		friendIvtLinks.put("name", "qq");
		friendIvtList.add(friendIvtLinks);
		Map<String, Object> result = new HashMap<>();
		Map<String, Object> selfmap = new HashMap<>();
		Map<String, Object> links = new HashMap<>();
		result.put("_items", friendIvtList );
		result.put("_total",0);
		if(friendIvtList!=null){
			result.put("_total",friendIvtList.size());
		}

		selfmap.put("href", URL_HEAD+"/users/"+userUuid+"/friendinvitationpage?bankId="+ bankId);
		selfmap.put("describedBy","schema//"+URL_HEAD+"/users/friendInvation.json");
		links.put("self", selfmap );
		result.put("_links", links);
		result.put("userUuid", userUuid);
		result.put("bankId", bankId);
		return new ResponseEntity<Object>(result , HttpStatus.OK);
	}


	/**
	 * 关于我们
	 * @param userUuid
	 * @param bankId
	 * @return
	 * @throws Exception
	 */

	@ApiOperation("关于我们")
	@ApiImplicitParams({
		//@ApiImplicitParam(paramType="path",name="id",dataType="String",required=true,value="id",defaultValue=""),
		@ApiImplicitParam(paramType="query",name="userUuid",dataType="String",required=false,value="用户Uuid",defaultValue=""),
		@ApiImplicitParam(paramType="query",name="bankId",dataType="Long",required=false,value="银行ID",defaultValue="")
    })
	@ApiResponses({
		@ApiResponse(code=200,message="OK"),
        @ApiResponse(code=400,message="请求参数没填好"),
        @ApiResponse(code=401,message="未授权用户"),        				
		@ApiResponse(code=403,message="服务器已经理解请求，但是拒绝执行它"),
		@ApiResponse(code=404,message="请求路径没有或页面跳转路径不对")         				
    })
	@RequestMapping(value = "/companyinfos", method = RequestMethod.GET)
	public ResponseEntity<?> getCompanyInfo(@RequestParam String userUuid, @RequestParam(required = false) Long bankId)
			throws Exception {
		UserInfoCompanyInfoDTO userInfoCompanyInfo = userInfoService.getCompanyInfo(userUuid, bankId);
		Map<String, Object> result = new HashMap<>();
		Map<String, Object> selfmap = new HashMap<>();
		Map<String, Object> links = new HashMap<>();

		result.put("companyinfo", userInfoCompanyInfo);
		// result.put("_page","");
		StringBuffer sbf = new StringBuffer();
		sbf.append(URL_HEAD);
		sbf.append("/companyinfos");
		if (userUuid != null || bankId != null) {
			sbf.append("?");
			if (userUuid != null && bankId != null) {
				sbf.append("userUuid=" + userUuid);
				sbf.append("&bankId=" + bankId);
			} else if (userUuid != null && bankId == null) {
				sbf.append("userUuid=" + userUuid);
			} else if (userUuid == null && bankId != null) {
				sbf.append("bankId=" + bankId);
			}
		}
		selfmap.put("href", sbf.toString());
		selfmap.put("describedBy", "schema//" + URL_HEAD + "/companyinfos.json");
		links.put("self", selfmap);
		if (userUuid != null) {
			result.put("userUuid", userUuid);
		} else {
			result.put("userUuid", "");
		}
		if (bankId != null) {
			result.put("bankId", bankId);
		} else {
			result.put("bankId", "");
		}
		result.put("_links", links);
		return new ResponseEntity<Object>(result, HttpStatus.OK);
	}

	private Object makePersonInfoResponse() {
		Map<String, Object> result = new HashMap<>();
		result.put("userCellphone","189****8782");
		result.put("userBirthAge","90后");
		result.put("userCarrier","非金融业");
		
		Map<String, Object> selfmap = new HashMap<>();
		selfmap.put("href", URL_HEAD+"/userpersonalpage/{id}" );
		selfmap.put("describedBy",URL_HEAD+"/userpersonalpage.json");
		
		Map<String, Object> links = new HashMap<>();
		List<Map> relateList = new ArrayList<>();
		Map<String, Object> userBirthAgeLink = new HashMap<>();
		userBirthAgeLink.put("name", "userBirthAge");
		userBirthAgeLink.put("href", URL_HEAD+"/getBirthAges");
		relateList.add(userBirthAgeLink);

		Map<String, Object> userCarrierLink = new HashMap<>();
		userCarrierLink.put("name", "userCarrier");
		userCarrierLink.put("href", URL_HEAD+"/getCarriers");
		relateList.add(userCarrierLink);
		
		Map<String, Object> pwdupdateLink = new HashMap<>();
		pwdupdateLink.put("name", "updatepassword");
		pwdupdateLink.put("href", URL_HEAD+"/updatepassword");
		relateList.add(pwdupdateLink);

		links.put("self", selfmap );
		links.put("related", relateList );
		result.put("_links", links);
		return result;

	}

	private Object makeFakeResponse() {
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, String> userAssets = new HashMap<>();
		userAssets.put("totalAssetsValue", "2478.90");
		userAssets.put("dailyProfit", "0.90");
		userAssets.put("accumulatedProfit", "78.90");
		result.put("userAssets",userAssets );
		result.put("userPortfolio", 1);
		result.put("userBankCards", 1);
		Map<String, Object> links = new HashMap<>();
		links.put("self", URL_HEAD+"/baseinfo/id" );
		links.put("describedBy",URL_HEAD+"/baseinfo/id.json");
		List<Map> related = new ArrayList<>();
		Map<String, Object> itemsCellphone = new HashMap<>();
		itemsCellphone.put("name", "cellphone");
		itemsCellphone.put("href", URL_HEAD+"/getUserPersonalInfo/id/");
		itemsCellphone.put("describedBy", URL_HEAD+"/userBase/item.json");
		itemsCellphone.put("title", "189****8899");
		Map<String, Object> iconObj = new HashMap<>();
		Map<String, String> iconRef = new HashMap<>();
		iconRef.put("href", "cdn/userInfo/phoneicon");
		iconObj.put("icon", iconRef);

		related.add(itemsCellphone);
		Map<String, String> userAssetsBrief = new HashMap<>();
		userAssetsBrief.put("name", "userAssetsBrief");
		userAssetsBrief.put("href", URL_HEAD+"/userAssetsBrief/id");
		related.add(userAssetsBrief);
		Map<String, String> userPortfolio = new HashMap<>();
		userPortfolio.put("name", "userPortfolio");
		userPortfolio.put("href", URL_HEAD+"/userPortfolio/id");
		related.add(userPortfolio);
		Map<String, String> userBankCards = new HashMap<>();
		userBankCards.put("name", "userBankCards");
		userBankCards.put("href", URL_HEAD+"/userBankCards/id");
		related.add(userBankCards);

		Map<String, String> userInviteFriends = new HashMap<>();
		userInviteFriends.put("name", "userInviteFriends");
		userInviteFriends.put("href", URL_HEAD+"/userInviteFriends/id");
		related.add(userInviteFriends);


		Map<String, String> userMessage = new HashMap<>();
		userMessage.put("name", "userMessage");
		userMessage.put("href", URL_HEAD+"/userMessage/id");
		related.add(userMessage);

		Map<String, String> aboutShellShellFish = new HashMap<>();
		aboutShellShellFish.put("name", "aboutShellShellFish");
		aboutShellShellFish.put("href", URL_HEAD+"/aboutShellShellFish/id");
		related.add(aboutShellShellFish);


		Map<String, String> logout = new HashMap<>();
		logout.put("name", "logout");
		logout.put("href", URL_HEAD+"/logout/id");
		related.add(logout);

		Map<String, String> homePage = new HashMap<>();
		homePage.put("name", "homePage");
		homePage.put("href", URL_HEAD+"/homePage/id");
		related.add(homePage);

		Map<String, String> finance = new HashMap<>();
		finance.put("name", "finance");
		finance.put("href", URL_HEAD+"/finance/id");
		related.add(finance);

		Map<String, String> userInfo = new HashMap<>();
		userInfo.put("name", "userInfo");
		userInfo.put("href", URL_HEAD+"/id");
		related.add(userInfo);

		links.put("related", related );
		result.put("_links", links);
		return result;
	}
	
	/**
	 * 解绑银行卡
	 * @return
	 * @throws Exception
	 */
	@ApiOperation("解绑银行卡")
	@ApiResponses({
		@ApiResponse(code=200,message="OK"),
        @ApiResponse(code=400,message="请求参数没填好"),
        @ApiResponse(code=401,message="未授权用户"),        				
		@ApiResponse(code=403,message="服务器已经理解请求，但是拒绝执行它"),
		@ApiResponse(code=404,message="请求路径没有或页面跳转路径不对")   
    })
	@ApiImplicitParams({
		@ApiImplicitParam(paramType="path",name="userUuid",dataType="String",required=true,value="用户uuid",defaultValue=""),
		@ApiImplicitParam(paramType="path",name="bankcardId",dataType="String",required=true,value="银行卡ID",defaultValue=""),
	})
	@RequestMapping(value = "/users/{userUuid}/unbundlingBankCards/{bankcardId}", method = RequestMethod.DELETE)
	public ResponseEntity<Map> unbundlingBank(
			@Valid @NotNull(message = "userUuid不可为空")@PathVariable String userUuid,
			@Valid @NotNull(message = "id")@PathVariable String bankcardId)
			throws Exception {	
		Map<String, Object> resultMap = new HashMap<String, Object>();
		//id message ID
		Boolean result =  userInfoService.deleteBankCard(userUuid, bankcardId);
		if(!result){
			resultMap.put("status", "NG");
			return new ResponseEntity<Map>(resultMap,HttpStatus.UNAUTHORIZED);
		} else {
			//return new ResponseEntity<Object>(URL_HEAD+"/message/updateinvestmentmessages/investmentmessages?userUuid="+userUuid , HttpStatus.OK);
			resultMap.put("status", "OK");
			resultMap.put("msg", "解绑成功");
			return new ResponseEntity<Map>(resultMap, HttpStatus.OK);
		}
	}
	
}