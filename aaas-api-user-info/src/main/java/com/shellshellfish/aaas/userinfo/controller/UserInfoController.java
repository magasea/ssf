package com.shellshellfish.aaas.userinfo.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shellshellfish.aaas.userinfo.aop.AopLinkResources;
import com.shellshellfish.aaas.userinfo.model.dto.bankcard.BankCard;
import com.shellshellfish.aaas.userinfo.model.dto.invest.AssetDailyRept;
import com.shellshellfish.aaas.userinfo.model.dto.user.UserBaseInfo;
import com.shellshellfish.aaas.userinfo.model.dto.user.UserInfoAssectsBrief;
import com.shellshellfish.aaas.userinfo.model.dto.user.UserPersonMsg;
import com.shellshellfish.aaas.userinfo.model.dto.user.UserPortfolio;
import com.shellshellfish.aaas.userinfo.model.dto.user.UserSysMsg;
import com.shellshellfish.aaas.userinfo.model.vo.UserPersonalMsgVo;
import com.shellshellfish.aaas.userinfo.model.vo.BankcardDetailVo;
import com.shellshellfish.aaas.userinfo.service.UserInfoService;
import com.shellshellfish.aaas.userinfo.util.DateUtil;
import com.shellshellfish.aaas.userinfo.util.UserInfoUtils;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

	@Autowired
  UserInfoService userInfoService;

	@RequestMapping(value = "/userinfo/id/{id}", method = RequestMethod.GET)
	@AopLinkResources
	public ResponseEntity<Object> getUserBaseInfo(@PathVariable("id") String id) {
		System.out.println("userId is " + id);
		Long userId = Long.parseLong(id);
		List<BankCard> bankCards =  userInfoService.getUserInfoBankCards(userId);
		UserInfoAssectsBrief userInfoAssectsBrief = userInfoService.getUserInfoAssectsBrief(userId);
		List<UserPortfolio> userPortfolios = userInfoService.getUserPortfolios(userId);
		UserBaseInfo userBaseInfo = userInfoService.getUserInfoBase(userId);

		Map<String, Object> result = new HashMap<>();
		result.put("title","个人信息");
		Map<String, String> userCellphone = new HashMap<>();
		userCellphone.put("number",userBaseInfo.getCellPhone());
		userCellphone.put("title","手机号");
		result.put("userCellphone",userCellphone);
		Map<String, String> userBirthAge = new HashMap<>();
		Calendar cal = Calendar.getInstance();
		int year = 0;
		if(null !=  userBaseInfo.getBirthAge()){
			cal.setTime(userBaseInfo.getBirthAge());
			year = (cal.get(Calendar.YEAR)%10)* 10;
		}

		userBirthAge.put("birthAge",""+year);
		userBirthAge.put("title","出生年代");
		result.put("userBirthAge",userBirthAge);
		result.put("userCarrier",userBaseInfo.getOccupation());

		result.put("userAssets", userInfoAssectsBrief);
		result.put("userPortfolios", userPortfolios);
		result.put("userBankCards", bankCards);

		if(StringUtils.isEmpty(id)){
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}else{
			return new ResponseEntity<Object>(result , HttpStatus.OK);
		}

	}

	@RequestMapping(value = "/userinfo/userbankcards/id/{cardNumber}", method = RequestMethod.GET)
	public ResponseEntity<?> getUserBankCards(@PathVariable("cardNumber") String cardNumber)
			throws Exception {
		if(StringUtils.isEmpty(cardNumber)){
			throw new ServletRequestBindingException("no cardNumber in params");
		}else{
			BankCard bankCard =  userInfoService.getUserInfoBankCard(cardNumber);
			return new ResponseEntity<Object>(bankCard , HttpStatus.OK);
		}
	}

	@RequestMapping(value = "/userInfo/getUserPersonalInfo/id/{id}", method = RequestMethod.GET)
	@AopLinkResources
	public ResponseEntity<?> getUserPersonalInfo(@PathVariable("id") String id){
		if(StringUtils.isEmpty(id)){
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}else{
			Object result =  makePersonInfoResponse();
			return new ResponseEntity<Object>(result , HttpStatus.OK);
		}
	}

	@RequestMapping(value = "/userinfo/bankcards/add/cardnumber/{cardNumber}", method = RequestMethod
			.POST)
	@AopLinkResources
	public ResponseEntity<?> addBankCardWithCardNumber(@PathVariable("cardNumber") String cardNumber){
		if(StringUtils.isEmpty(cardNumber) ){
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}else{
			if(!UserInfoUtils.matchLuhn(cardNumber)){
				return new ResponseEntity<Object>("银行卡号是否准确？" , HttpStatus.OK);
			}
			return new ResponseEntity<Object>("银行卡号准确" , HttpStatus.OK);
		}
	}

	@RequestMapping(value = "/userinfo/bankcards/add/", method = RequestMethod.POST)
	public ResponseEntity<?> addBankCardWithDetailInfo(@RequestBody BankcardDetailVo bankcardDetailVo)
			throws Exception {
		ObjectMapper mapper = new ObjectMapper();

		// Convert POJO to Map
		Map<String, Object> params =
				mapper.convertValue(bankcardDetailVo, new TypeReference<Map<String, Object>>() {});

		if(CollectionUtils.isEmpty(params)){
			throw new ServletRequestBindingException("no cardNumber in params");
		}
		params.forEach((k, v)-> { if( null == v || StringUtils.isEmpty(v.toString())){
			throw new IllegalArgumentException("no "+k.toString()+"'s value in params");
		}});
		return new ResponseEntity<Object>(userInfoService.createBankcard(params) , HttpStatus.OK);
	}

	@RequestMapping(value = "/userinfo/userassets/overview/data", method = RequestMethod.GET)
	public ResponseEntity<?> getUserAssetsOverview(@RequestParam Map<String, String> params)
			throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-DD");
		sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
		Date beginDate = null;
		Date endDate = null;
		Long beginTimeLong;
		Long endTimeLong;
		try {
			beginDate = sdf.parse(params.get("beginDate"));

		}catch (ParseException ex) {
			ex.printStackTrace();
		}
		if (beginDate == null && !StringUtils.isEmpty(params.get("beginDate") )) {
			// Invalid date format
			//maybe frontend send long time value to backend
			beginTimeLong = Long.getLong(params.get("b" +
					"eginDate"));
			endTimeLong = Long.getLong(params.get("endDate"));
		} else {
			// Valid date format
			beginTimeLong = DateUtil.getDateOneDayBefore(beginDate);
			endDate = sdf.parse(params.get("endDate"));
			endTimeLong = endDate.getTime();
		}

		List<AssetDailyRept> assetDailyRepts =
		userInfoService.getAssetDailyRept(Long.parseLong(params.get("userId")), beginTimeLong, endTimeLong);
		return new ResponseEntity<Object>(assetDailyRepts , HttpStatus.OK);
	}

	@RequestMapping(value = "/userinfo/userassets/overview/data", method = RequestMethod.POST)
	public ResponseEntity<?> addUserAssetsOverview(@RequestBody AssetDailyRept assetDailyRept)
			throws Exception {

		AssetDailyRept assetDailyReptRlt =
				 userInfoService.addAssetDailyRept(assetDailyRept);
		return new ResponseEntity<Object>(assetDailyReptRlt , HttpStatus.OK);
	}

	@RequestMapping(value = "/userinfo/message/personal/{userUuid}", method = RequestMethod.GET)
	public ResponseEntity<?> getPersonalMsg(@PathVariable String userUuid)
			throws Exception {

		List<UserPersonMsg> userPersonMsgs =  userInfoService.getUserPersonMsg(userUuid);
		Map<String, Object> result = new HashMap<>();
		result.put("userPersonMsg", userPersonMsgs);
		return new ResponseEntity<Object>(result , HttpStatus.OK);
	}

	@RequestMapping(value = "/userinfo/message/system/{userUuid}", method = RequestMethod.GET)
	public ResponseEntity<?> getSystemMsg(@PathVariable String userUuid)
			throws Exception {
		List<UserSysMsg> userSysMsgs = userInfoService.getUserSysMsg(userUuid);
		Map<String, Object> result = new HashMap<>();
		result.put("userSysMsgs", userSysMsgs);
		return new ResponseEntity<Object>(result , HttpStatus.OK);
	}

	@RequestMapping(value = "/userinfo/message/personal/{userUuid}", method = RequestMethod.POST)
	public ResponseEntity<?> updatePersonalMsg(@RequestBody UserPersonalMsgVo userPersonalMsgVo)
			throws Exception {

		List<UserPersonMsg> userPersonMsgs =  userInfoService.updateUserPersonMsg(userPersonalMsgVo
				.getMessagesToUpdate(), userPersonalMsgVo.getUuid(), userPersonalMsgVo.getReadedStatus());
		Map<String, Object> result = new HashMap<>();
		result.put("userPersonMsg", userPersonMsgs);
		return new ResponseEntity<Object>(result , HttpStatus.OK);
	}

	private Object makePersonInfoResponse() {
		Map<String, Object> result = new HashMap<>();
		result.put("title","个人信息");
		Map<String, String> userCellphone = new HashMap<>();
		userCellphone.put("number","189****8782");
		userCellphone.put("title","手机号");
		result.put("userCellphone",userCellphone);
		Map<String, String> userBirthAge = new HashMap<>();
		userBirthAge.put("birthAge","90后");
		userBirthAge.put("title","出生年代");
		result.put("userBirthAge",userBirthAge);
		Map<String, String> userCarrier = new HashMap<>();
		userCarrier.put("birthAge","90后");
		userCarrier.put("title","出生年代");
		result.put("userCarrier",userCarrier);

		Map<String, Object> links = new HashMap<>();
		links.put("self", "/api/user/getUserPersonalInfo/id" );
		links.put("describedBy","schema//user/getUserPersonalInfo/item.json");
		List<Map> related = new ArrayList<>();
		Map<String, Object> userBirthAgeLink = new HashMap<>();
		userBirthAgeLink.put("name", "userBirthAge");
		userBirthAgeLink.put("href", "/api/userInfo/getBirthAges/");
		related.add( userBirthAgeLink);

		Map<String, Object> userCarrierLink = new HashMap<>();
		userCarrierLink.put("name", "userCarrier");
		userCarrierLink.put("href", "/api/userInfo/getCarriers/");
		related.add( userCarrierLink);

		links.put("related", related );
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
		links.put("self", "/api/user/baseinfo/id" );
		links.put("describedBy","schema/common/item.json");
		List<Map> related = new ArrayList<>();
		Map<String, Object> itemsCellphone = new HashMap<>();
		itemsCellphone.put("name", "cellphone");
		itemsCellphone.put("href", "/api/userInfo/getUserPersonalInfo/id/");
		itemsCellphone.put("describedBy", "schema/userInfo/userBase/item.json");
		itemsCellphone.put("title", "189****8899");
		Map<String, Object> iconObj = new HashMap<>();
		Map<String, String> iconRef = new HashMap<>();
		iconRef.put("href", "cdn/userInfo/phoneicon");
		iconObj.put("icon", iconRef);

		related.add(itemsCellphone);
		Map<String, String> userAssetsBrief = new HashMap<>();
		userAssetsBrief.put("name", "userAssetsBrief");
		userAssetsBrief.put("href", "/api/userInfo/userAssetsBrief/id");
		related.add(userAssetsBrief);
		Map<String, String> userPortfolio = new HashMap<>();
		userPortfolio.put("name", "userPortfolio");
		userPortfolio.put("href", "/api/userInfo/userPortfolio/id");
		related.add(userPortfolio);
		Map<String, String> userBankCards = new HashMap<>();
		userBankCards.put("name", "userBankCards");
		userBankCards.put("href", "/api/userInfo/userBankCards/id");
		related.add(userBankCards);

		Map<String, String> userInviteFriends = new HashMap<>();
		userInviteFriends.put("name", "userInviteFriends");
		userInviteFriends.put("href", "/api/userInfo/userInviteFriends/id");
		related.add(userInviteFriends);


		Map<String, String> userMessage = new HashMap<>();
		userMessage.put("name", "userMessage");
		userMessage.put("href", "/api/userInfo/userMessage/id");
		related.add(userMessage);

		Map<String, String> aboutShellShellFish = new HashMap<>();
		aboutShellShellFish.put("name", "aboutShellShellFish");
		aboutShellShellFish.put("href", "/api/userInfo/aboutShellShellFish/id");
		related.add(aboutShellShellFish);


		Map<String, String> logout = new HashMap<>();
		logout.put("name", "logout");
		logout.put("href", "/api/userInfo/logout/id");
		related.add(logout);

		Map<String, String> homePage = new HashMap<>();
		homePage.put("name", "homePage");
		homePage.put("href", "/api/userInfo/homePage/id");
		related.add(homePage);

		Map<String, String> finance = new HashMap<>();
		finance.put("name", "finance");
		finance.put("href", "/api/userInfo/finance/id");
		related.add(finance);

		Map<String, String> userInfo = new HashMap<>();
		userInfo.put("name", "userInfo");
		userInfo.put("href", "/api/userInfo/userInfo/id");
		related.add(userInfo);

		links.put("related", related );
		result.put("_links", links);
		return result;
	}
}