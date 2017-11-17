package com.shellshellfish.aaas.userinfo.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shellshellfish.aaas.userinfo.aop.AopLinkResources;
import com.shellshellfish.aaas.userinfo.model.dto.bankcard.BankCard;
import com.shellshellfish.aaas.userinfo.model.dto.invest.AssetDailyRept;
import com.shellshellfish.aaas.userinfo.model.dto.invest.TradeLog;
import com.shellshellfish.aaas.userinfo.model.dto.user.UserBaseInfo;
import com.shellshellfish.aaas.userinfo.model.dto.user.UserInfoAssectsBrief;
import com.shellshellfish.aaas.userinfo.model.dto.user.UserInfoCompanyInfo;
import com.shellshellfish.aaas.userinfo.model.dto.user.UserInfoFriendRule;
import com.shellshellfish.aaas.userinfo.model.dto.user.UserPersonMsg;
import com.shellshellfish.aaas.userinfo.model.dto.user.UserPortfolio;
import com.shellshellfish.aaas.userinfo.model.dto.user.UserSysMsg;
import com.shellshellfish.aaas.userinfo.model.vo.BankcardDetailVo;
import com.shellshellfish.aaas.userinfo.model.vo.UserPersonalMsgVo;
import com.shellshellfish.aaas.userinfo.service.UserInfoService;
import com.shellshellfish.aaas.userinfo.util.Constants;
import com.shellshellfish.aaas.userinfo.util.DateUtil;
import com.shellshellfish.aaas.userinfo.util.UserInfoUtils;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
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

@RestController
@RequestMapping("/api")
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
	@RequestMapping(value = "/userinfo/users/{userUuid}/initpage/", method = RequestMethod.GET)
	@AopLinkResources
	public ResponseEntity<Object> getUserBaseInfo(
			@Valid @NotNull(message="userUuid不能为空") @PathVariable("userUuid") String userUuid
			) throws Exception {
		System.out.println("userUuid is " + userUuid);
		Map<String, Object> result = new HashMap<>();
		Map<String, Object> links = new HashMap<>();
		Map<String, Object> selfmap = new HashMap<>();
		selfmap.put("href", URL_HEAD+"/initpage/"+userUuid);
		selfmap.put("describedBy",URL_HEAD+"/initpage.json");
		
		List<BankCard> bankCards =  userInfoService.getUserInfoBankCards(userUuid);
		UserInfoAssectsBrief userInfoAssectsBrief = userInfoService.getUserInfoAssectsBrief(userUuid);
		List<UserPortfolio> userPortfolios = userInfoService.getUserPortfolios(userUuid);
		UserBaseInfo userBaseInfo = userInfoService.getUserInfoBase(userUuid);

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
		@ApiImplicitParam(paramType="path",name="id",dataType="String",required=true,value="id",defaultValue=""),
		@ApiImplicitParam(paramType="query",name="cardNumber",dataType="String",required=true,value="银行卡号",defaultValue="")
	})
	@RequestMapping(value = "/userinfo/bankcardinfopage/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getUserBankCards(
			@Valid @NotNull(message = "id不能为空") @PathVariable("id") String id,
			@Valid @NotNull(message = "银行卡号不能为空") @Size(max = 20, min = 15) @RequestParam("cardNumber") String cardNumber
			)throws Exception {
		Map<String, Object> result = new HashMap<>();
		Map<String, Object> links = new HashMap<>();
		List<Map> relateList = new ArrayList<Map>();
		if(StringUtils.isEmpty(cardNumber)){
			throw new ServletRequestBindingException("no cardNumber in params");
		}else{
			BankCard bankCard =  userInfoService.getUserInfoBankCard(cardNumber);
			Map<String, Object> selfmap = new HashMap<>();
			selfmap.put("href", URL_HEAD+"/bankcardinfopage/"+id+ "?cardNumber="+cardNumber);
			selfmap.put("describedBy",URL_HEAD+"/bankcardinfopage.json");
			result.put("bankCard", bankCard);

			HashMap<String,Object> relateditemmap=new HashMap<>();
			relateditemmap = new HashMap<String,Object>();
			relateditemmap.put("href", URL_HEAD+"/unbindbanks");
			relateditemmap.put("name", "unbindbanks");
			relateList.add(relateditemmap);
			links.put("related", relateList);
			
			links.put("self", selfmap );
			result.put("_links", links);
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

	@ApiImplicitParam(paramType="path",name="userUuid",dataType="String",required=true,value="userUuid",defaultValue="")
	@RequestMapping(value = "/userInfo/user/{userUuid}/userpersonalpage", method = RequestMethod.GET)
	@AopLinkResources
	public ResponseEntity<?> getUserPersonalInfo(
			@Valid @NotNull(message = "userUuid不能为空") @PathVariable("userUuid") String userUuid)throws Exception {
		if(StringUtils.isEmpty(userUuid)){
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}else{
			UserBaseInfo userBaseInfo =  userInfoService.getUserInfoBase(userUuid);
			
			Map<String, Object> result = new HashMap<>();
			Map<String, Object> links = new HashMap<>();
			Map<String, Object> selfmap = new HashMap<>();
			selfmap.put("href", URL_HEAD+"/"+userUuid+"/userpersonalpage");
			selfmap.put("describedBy",URL_HEAD+"/"+userUuid+"/userpersonalpage.json");
			links.put("self", selfmap);
			result.put("_links", links);
			result.put("userBaseInfo", userBaseInfo);
			return new ResponseEntity<Object>(result , HttpStatus.OK);
		}
	}


	/**
	 * 银行卡 添加银行卡 下一步
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
	@RequestMapping(value = "/userinfo/user/{userUuid}/bankcardnum", method = RequestMethod.GET)
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
			Map<String, Object> result = new HashMap<>();
			Map<String, Object> links = new HashMap<>();
			Map<String, Object> selfmap = new HashMap<>();
			selfmap.put("href", URL_HEAD+"/user/"+userUuid+"/bankcardnum");
			selfmap.put("describedBy",URL_HEAD+"/user/"+userUuid+"/bankcardnum.json");
			links.put("self", selfmap);
			result.put("_links", links);
			return new ResponseEntity<Object>(result , HttpStatus.OK);
		}
	}
	
	/**
	 *银行卡 添加银行卡 初始页面
	 * @param cardNumber
	 * @return
	 */
	@ApiOperation("银行卡 添加银行卡 初始页面")
	@ApiResponses({ 
		@ApiResponse(code = 200, message = "OK"), 
		@ApiResponse(code = 400, message = "请求参数没填好"),
		@ApiResponse(code = 401, message = "未授权用户"), 
		@ApiResponse(code = 403, message = "服务器已经理解请求，但是拒绝执行它"),
		@ApiResponse(code = 404, message = "请求路径没有或页面跳转路径不对") })
	@ApiImplicitParam(paramType = "path", name = "userUuid", dataType = "String", required = true, value = "id", defaultValue = "")
	@RequestMapping(value = "/userinfo/user/{userUuid}/bankcardpage", method = RequestMethod.GET)
	public ResponseEntity<?> addPreCheckBankCardWithCardNumber(
			@Valid @NotNull(message = "id不能为空") @PathVariable("userUuid") String userUuid) {

		Map<String, Object> result = new HashMap<>();
		Map<String, Object> links = new HashMap<>();
		Map<String, Object> relateditemmap = new HashMap<>();
		Map<String, Object> selfmap = new HashMap<>();
		List<Map> relateList = new ArrayList<Map>();
		
		result.put("bankcardnum", "");

		selfmap.put("href", URL_HEAD + "/user/" + userUuid + "/bankcardpage");
		selfmap.put("describedBy", URL_HEAD + "/user/" + userUuid + "/bankcardpage.json");

		relateditemmap = new HashMap<String, Object>();
		relateditemmap.put("href", URL_HEAD + "/user/" + userUuid + "/bankcards");
		relateditemmap.put("name", "bankcards");
		relateList.add(relateditemmap);
		
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
	@RequestMapping(value = "/userinfo/user/{userUuid}/bankcards", method = RequestMethod.POST)
		public ResponseEntity<?> addBankCardWithDetailInfo(@Valid @NotNull(message="不能为空")
	@PathVariable("userUuid") String userUuid,
			@RequestBody BankcardDetailVo bankcardDetailVo) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		// Convert POJO to Map
		Map<String, Object> params =
				mapper.convertValue(bankcardDetailVo, new TypeReference<Map<String, Object>>() {});
		params.put("userUuid", userUuid);
		if(CollectionUtils.isEmpty(params)){
			throw new ServletRequestBindingException("no cardNumber in params");
		}
		params.forEach((k, v)-> { if( null == v || StringUtils.isEmpty(v.toString())){
			throw new IllegalArgumentException("no "+k.toString()+"'s value in params");
		}});
		BankCard bankCard = userInfoService.createBankcard(params);
		if(bankCard==null){
			return new ResponseEntity<Object>(URL_HEAD+"/userpersonalpage" , HttpStatus.OK);
		}else{
			return new ResponseEntity<Object>(URL_HEAD+"/userpersonalpage?action='userpersonalpage'" , HttpStatus.OK);
		}
	}

	/**
	 * 列出银行卡	action
	 * @param id
	 * @param bankcardDetailVo
	 * @return
	 * @throws Exception
	 */
	@ApiOperation("用户银行卡集合")
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
	@RequestMapping(value = "/userinfo/user/{userUuid}/bankcards", method = RequestMethod.GET)
	public ResponseEntity<?> addBankCardWithDetailInfo(@Valid @NotNull(message="不能为空")
	@PathVariable("userUuid") String userUuid) throws Exception {

		List<BankCard> bankCards = userInfoService.getUserInfoBankCards(userUuid);
		
		Map<String, Object> result = new HashMap<>();
		Map<String, Object> links = new HashMap<>();
		Map<String, Object> selfmap = new HashMap<>();
		List<Map> relateList = new ArrayList<Map>();
		
		selfmap.put("href", URL_HEAD+"/"+userUuid+"/bankcards" );
		selfmap.put("describedBy",URL_HEAD+"/"+userUuid+"/bankcards.json");
		result.put("bankCards", bankCards);
		
		HashMap<String,Object> relateditemmap=new HashMap<>();
		relateditemmap = new HashMap<String,Object>();
		relateditemmap.put("href", URL_HEAD+"/user/"+userUuid+"/bankcardnum");
		relateditemmap.put("name", "bankcardnum");
		relateList.add(relateditemmap);
		links.put("related", relateList);
		
		links.put("self", selfmap );
		result.put("_links", links);
		return new ResponseEntity<Object>(result , HttpStatus.OK);

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

	@RequestMapping(value = "/userinfo/user/{userUuid}/assetoverview", method = RequestMethod
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

		List<AssetDailyRept> assetDailyRepts =

		userInfoService.getAssetDailyRept(userUuid, beginTimeLong, endTimeLong);
		
		result.put("assetDailyRepts", assetDailyRepts);
		
		Map<String, Object> selfmap = new HashMap<>();
		selfmap.put("href", URL_HEAD+"/"+userUuid+"/userassets/overview?beginDate="+bgDate+"&endDate="+edDate);
		selfmap.put("describedBy",URL_HEAD+"/"+userUuid+"/userassets/overview.json");
		links.put("self", selfmap );
		
		HashMap<String,Object> relateditemmap=new HashMap<>();
		relateditemmap = new HashMap<String,Object>();
		relateditemmap.put("href", URL_HEAD+"/incometrendchart");
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
	@RequestMapping(value = "/userinfo/user/{userUuid}/investmentmessages", method = RequestMethod.GET)
	public ResponseEntity<?> getPersonalInvstMsg(
			@Valid @NotNull(message = "userUuid不可为空") @PathVariable(name = "userUuid") String userUuid
			)throws Exception {
		List<UserPersonMsg> userPersonMsgs =  userInfoService.getUserPersonMsg(userUuid);
		Map<String, Object> result = new HashMap<>();
		Map<String, Object> links = new HashMap<>();
		HashMap<String,Object> relateditemmap=new HashMap<>();
		List<Map> relateList = new ArrayList<Map>();
		result.put("userPersonMsg", userPersonMsgs);
		
		Map<String, Object> selfmap = new HashMap<>();
		selfmap.put("href", URL_HEAD+"/"+ userUuid+"/message/investmentmessages");
		selfmap.put("describedBy",URL_HEAD+"/"+ userUuid+"/message/investmentmessages.json");
		
		relateditemmap = new HashMap<String,Object>();
		relateditemmap.put("href", URL_HEAD+"/user/"+userUuid+"/investmentmessages");
		relateditemmap.put("name", "investmentmessages");
		relateList.add(relateditemmap);
		links.put("self", selfmap );
		links.put("related", relateList);
		result.put("_links", links);
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
	@RequestMapping(value = "/userinfo/user/{userUuid}/systemmessages", method = RequestMethod
			.GET)
	public ResponseEntity<?> getSystemMsg(@Valid @NotNull(message = "userUuid不可为空")@PathVariable String userUuid)
			throws Exception {
		List<UserSysMsg> userSysMsgs = userInfoService.getUserSysMsg(userUuid);
		Map<String, Object> result = new HashMap<>();
		Map<String, Object> links = new HashMap<>();
		result.put("userSysMsgs", userSysMsgs);
		
		Map<String, Object> selfmap = new HashMap<>();
		selfmap.put("href", URL_HEAD+"/"+userUuid+"/message/systemmessages" );
		selfmap.put("describedBy",URL_HEAD+"/"+userUuid+"/message/systemmessages.json");
		
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
	@ApiImplicitParam(name="userPersonalMsgVo", value ="推送内容",required=true,paramType="body",dataType="UserPersonalMsgVo")
	@RequestMapping(value = "/userinfo/user/{userUuid}/investmentmessages", method =
			RequestMethod.PATCH)
	public ResponseEntity<?> updatePersonalMsg(@RequestBody UserPersonalMsgVo userPersonalMsgVo)
			throws Exception {

		Boolean result =  userInfoService.updateUserPersonMsg(userPersonalMsgVo
				.getMessagesToUpdate(), userPersonalMsgVo.getUuid(), userPersonalMsgVo.getReadedStatus());
		if(!result){
			return new ResponseEntity<Object>("更新内容失败",HttpStatus.UNAUTHORIZED);
		} else {
			return new ResponseEntity<Object>(URL_HEAD+"/message/updateinvestmentmessages/investmentmessages?userUuid="+userPersonalMsgVo.getUuid() , HttpStatus.OK);
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
		@ApiImplicitParam(paramType="query",name="pageNum",dataType="String",required=true,value="页面Num",defaultValue=""),
		@ApiImplicitParam(paramType="query",name="pageSize",dataType="String",required=true,value="页面大小",defaultValue=""),
		@ApiImplicitParam(paramType="query",name="sortField",dataType="String",required=false,
				value="排序",defaultValue="")
	})
	@RequestMapping(value = "/userinfo/user/{userUuid}/tradelog", method = RequestMethod.GET)
	public ResponseEntity<?> getTradLogsOfUser(@PathVariable String userUuid, @RequestParam( required = false) String
			pageNum, @RequestParam( required = false) String pageSize, @RequestParam( required = false) String sortField )
			throws Exception {
		PageRequest pageRequest = null;
		if(!StringUtils.isEmpty(pageNum)){
			if(!StringUtils.isEmpty(pageSize)){
				pageRequest = new PageRequest(Integer.parseInt(pageNum) -1, Integer.parseInt
						(pageSize), Direction.DESC, StringUtils.isEmpty(sortField)? "createdDate": sortField);
			}else{
				pageRequest = new PageRequest(Integer.parseInt(pageNum) -1, Constants.PAGE_SIZE, Direction.DESC,
						sortField);
			}
		}else{
			pageRequest = new PageRequest(0, Constants.PAGE_SIZE, Direction.DESC, "createdDate");
		}

		Page<TradeLog> tradeLogs =  userInfoService.getUserTradeLogs(userUuid, pageRequest);

		Map<String, Object> result = new HashMap<>();
		Map<String, Object> links = new HashMap<>();
		result.put("_items", tradeLogs);
		result.put("_page","");
		
		Map<String, Object> selfmap = new HashMap<>();
		selfmap.put("href", URL_HEAD+"/user"+userUuid+"/trade/log?pageNum="+pageNum+"&pageSize"
				+ "="+pageSize );
		selfmap.put("describedBy",URL_HEAD+"/"+userUuid+"/trade/log.json");
		
		links.put("self", selfmap );
		result.put("_links", links);
		return new ResponseEntity<Object>(result , HttpStatus.OK);
	}

//	@ApiOperation("邀请规则")
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
			@ApiImplicitParam(paramType="path",name="userUuid",dataType="String",required=true,value="userUuid",defaultValue="")
	})
	@ApiResponses({
			@ApiResponse(code=200,message="OK"),
			@ApiResponse(code=400,message="请求参数没填好"),
			@ApiResponse(code=401,message="未授权用户"),
			@ApiResponse(code=403,message="服务器已经理解请求，但是拒绝执行它"),
			@ApiResponse(code=404,message="请求路径没有或页面跳转路径不对")
	})
	@RequestMapping(value = "/userinfo/friendrules/{userUuid}", method = RequestMethod.GET)
	public ResponseEntity<?> getTradLogsOfUser(
			@Valid @NotNull(message = "userUuid不可为空")@PathVariable String userUuid,
			@RequestParam( required = false) Long bankId)
			throws Exception {

		List<UserInfoFriendRule> userInfoFriendRules = userInfoService.getUserInfoFriendRules(bankId);
		Map<String, Object> result = new HashMap<>();
		Map<String, Object> links = new HashMap<>();
		result.put("_items", userInfoFriendRules);
		result.put("_page","");
		
		Map<String, Object> selfmap = new HashMap<>();
		selfmap.put("href", URL_HEAD+"/friendrules/"+userUuid+"?bankId="+bankId );
		selfmap.put("describedBy",URL_HEAD+"/friendrules.json");
		
		links.put("self", selfmap );
		result.put("_links", links);
		return new ResponseEntity<Object>(result , HttpStatus.OK);
	}

	@ApiOperation("好友邀请")
	@ApiImplicitParams({
		@ApiImplicitParam(paramType="query",name="userUuid",dataType="String",required=true,value="用户Uuid",defaultValue=""),
		@ApiImplicitParam(paramType="query",name="bankId",dataType="Long",required=true,value="银行ID",defaultValue="")
	})
	@ApiResponses({
		@ApiResponse(code=200,message="OK"),
		@ApiResponse(code=400,message="请求参数没填好"),
		@ApiResponse(code=401,message="未授权用户"),
		@ApiResponse(code=403,message="服务器已经理解请求，但是拒绝执行它"),
		@ApiResponse(code=404,message="请求路径没有或页面跳转路径不对")
	})
	@RequestMapping(value = "/userinfo/user/{userUuid}/frndinvation", method = RequestMethod.GET)
	public ResponseEntity<?> getFriendsInvationLinks(
			@Valid @NotNull(message="用户Uuid") @PathVariable("userUuid") String userUuid,
			@Valid @NotNull(message="银行ID") @RequestParam( required = false) Long bankId)
			throws Exception {
		//:TODO 这段分享朋友邀请好友的做法是要改的
		Map<String, Object> friendIvtLinks = new HashMap<>();
		friendIvtLinks.put("wechat","http://wx.qq.com");
		friendIvtLinks.put("weibo","http://weibo.com");
		friendIvtLinks.put("qq","http://qq.com");
		Map<String, Object> result = new HashMap<>();
		Map<String, Object> selfmap = new HashMap<>();
		Map<String, Object> links = new HashMap<>();

		result.put("_items", friendIvtLinks );
		result.put("_page","");

		selfmap.put("href", "/api/userinfo/user/"+userUuid+"/frndinvation?bankId="+ bankId);
		selfmap.put("describedBy","schema//api/userinfo/user/friendInvation.json");
		links.put("self", selfmap );
		result.put("_links", links);
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
	@RequestMapping(value = "/userinfo/companyinfos", method = RequestMethod.GET)
	public ResponseEntity<?> getCompanyInfo(
			 @RequestParam String userUuid, 
			 @RequestParam( required = false) Long bankId)
			throws Exception {
		UserInfoCompanyInfo userInfoCompanyInfo = userInfoService.getCompanyInfo(userUuid, bankId);
		Map<String, Object> result = new HashMap<>();
		Map<String, Object> selfmap = new HashMap<>();
		Map<String, Object> links = new HashMap<>();
		
		result.put("_items", userInfoCompanyInfo);
		result.put("_page","");
		
		selfmap.put("href", URL_HEAD+"/companyinfos?userUuid="+userUuid+"&bankId="+bankId);
		selfmap.put("describedBy",URL_HEAD+"/companyinfos.json");
		links.put("self", selfmap );
		result.put("_links", links);
		return new ResponseEntity<Object>(result , HttpStatus.OK);
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
		userInfo.put("href", URL_HEAD+"/userInfo/id");
		related.add(userInfo);

		links.put("related", related );
		result.put("_links", links);
		return result;
	}
}