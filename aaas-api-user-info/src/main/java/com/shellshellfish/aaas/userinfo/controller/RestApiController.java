package com.shellshellfish.aaas.userinfo.controller;

import com.shellshellfish.aaas.userinfo.model.User;
import com.shellshellfish.aaas.userinfo.service.UserService;
import com.shellshellfish.aaas.userinfo.util.CustomErrorType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/api")
public class RestApiController {

	public static final Logger logger = LoggerFactory.getLogger(RestApiController.class);

	@Autowired
  UserService userService; //Service which will do all data retrieval/manipulation work

	// -------------------Retrieve All Users---------------------------------------------

	@RequestMapping(value = "/user/", method = RequestMethod.GET)
	public ResponseEntity<List<User>> listAllUsers() {
		List<User> users = userService.findAllUsers();
		if (users.isEmpty()) {
			return new ResponseEntity(HttpStatus.NO_CONTENT);
			// You many decide to return HttpStatus.NOT_FOUND
		}
		return new ResponseEntity<List<User>>(users, HttpStatus.OK);
	}

	// -------------------Retrieve Single User------------------------------------------

	@RequestMapping(value = "/user/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getUser(@PathVariable("id") long id) {
		logger.info("Fetching User with id {}", id);
		User user = userService.findById(id);
		if (user == null) {
			logger.error("User with id {} not found.", id);
			return new ResponseEntity(new CustomErrorType("User with id " + id
					+ " not found"), HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<User>(user, HttpStatus.OK);
	}

	// -------------------Create a User-------------------------------------------

	@RequestMapping(value = "/user/", method = RequestMethod.POST)
	public ResponseEntity<?> createUser(@RequestBody User user, UriComponentsBuilder ucBuilder) {
		logger.info("Creating User : {}", user);

		if (userService.isUserExist(user)) {
			logger.error("Unable to create. A User with name {} already exist", user.getName());
			return new ResponseEntity(new CustomErrorType("Unable to create. A User with name " + 
			user.getName() + " already exist."),HttpStatus.CONFLICT);
		}
		userService.saveUser(user);

		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(ucBuilder.path("/api/user/{id}").buildAndExpand(user.getId()).toUri());
		return new ResponseEntity<String>(headers, HttpStatus.CREATED);
	}

	// ------------------- Update a User ------------------------------------------------

	@RequestMapping(value = "/user/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateUser(@PathVariable("id") long id, @RequestBody User user) {
		logger.info("Updating User with id {}", id);

		User currentUser = userService.findById(id);

		if (currentUser == null) {
			logger.error("Unable to update. User with id {} not found.", id);
			return new ResponseEntity(new CustomErrorType("Unable to upate. User with id " + id + " not found."),
					HttpStatus.NOT_FOUND);
		}

		currentUser.setName(user.getName());
		currentUser.setAge(user.getAge());
		currentUser.setSalary(user.getSalary());

		userService.updateUser(currentUser);
		return new ResponseEntity<User>(currentUser, HttpStatus.OK);
	}

	// ------------------- Delete a User-----------------------------------------

	@RequestMapping(value = "/user/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteUser(@PathVariable("id") long id) {
		logger.info("Fetching & Deleting User with id {}", id);

		User user = userService.findById(id);
		if (user == null) {
			logger.error("Unable to delete. User with id {} not found.", id);
			return new ResponseEntity(new CustomErrorType("Unable to delete. User with id " + id + " not found."),
					HttpStatus.NOT_FOUND);
		}
		userService.deleteUserById(id);
		return new ResponseEntity<User>(HttpStatus.NO_CONTENT);
	}

	// ------------------- Delete All Users-----------------------------

	@RequestMapping(value = "/user/", method = RequestMethod.DELETE)
	public ResponseEntity<User> deleteAllUsers() {
		logger.info("Deleting All Users");

		userService.deleteAllUsers();
		return new ResponseEntity<User>(HttpStatus.NO_CONTENT);
	}


	@RequestMapping(value = "/userinfo/id/{id}", method = RequestMethod.GET)
	public ResponseEntity<Object> getUserInfoBase(@PathVariable("id") String id) {
		System.out.println("userId is " + id);
		if(StringUtils.isEmpty(id)){
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}else{
			Object result = makeFakeResponse();
			return new ResponseEntity<Object>(result , HttpStatus.OK);
		}

	}

	@RequestMapping(value = "/userInfo/getUserPersonalInfo/id/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getUserPersonalInfo(@PathVariable("id") String id){
		if(StringUtils.isEmpty(id)){
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}else{
			Object result =  makePersonInfoResponse();
			return new ResponseEntity<Object>(result , HttpStatus.OK);
		}
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
		links.put("describedBy","schema/userinfo/item.json");
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