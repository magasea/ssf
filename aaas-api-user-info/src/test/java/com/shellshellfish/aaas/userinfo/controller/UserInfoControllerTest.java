package com.shellshellfish.aaas.userinfo.controller;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles(profiles = "prod")
public class UserInfoControllerTest {
	// @Autowired
	private MockMvc mvc;

	@Autowired
	protected WebApplicationContext wac;

	private static String URL_HEAD = "/api/userinfo";

	@Before
	public void setUp() throws Exception {
		mvc = MockMvcBuilders.webAppContextSetup(wac).build(); // 初始化MockMvc对象
	}

	@After
	public void tearDown() throws Exception {
	}

	/**
	 * 我的 初始页面
	 */
	@Test
	public void getUserBaseInfo() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get(URL_HEAD+"/users/1/initpage")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	/**
	 * 我的银行卡 查看页面
	 */
	@Test
	public void getUserBankCards() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get(URL_HEAD+"/bankcardinfopage?cardNumber=1234567890123456")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	/**
	 * 个人信息 页面
	 */
	@Test
	public void getUserPersonalInfo() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get(URL_HEAD+"/users/1")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	/**
	 * 银行卡 添加银行卡 下一步 action
	 */
	@Test
	public void getPreCheckBankCardWithCardNumber() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get(URL_HEAD+"/users/1/bankcardinfopage?cardNumber=62284804025648900")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	/**
	 * 银行卡 添加银行卡 下一步 初始页面
	 */
	@Test
	public void addPreCheckBankCardWithCardNumber() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get(URL_HEAD+"/users/1/bankcardpage")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	/**
	 * 银行卡 添加银行卡 提交 初始页面
	 */
	@Test
	public void getBankCardSubmitInit() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get(URL_HEAD+"/users/1/bankcardsubmitpage?cardNumber=1234567890123456")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}
	
	/**
	 * 银行卡 添加银行卡 提交
	 */
	@Test
	public void addBankCardWithDetailInfo() throws Exception {
		String jsonstr="{\"bankName\":\"中国建设银行\","
				+ "\"cardCellphone\": \"13512345678\","
				+ "\"cardNumber\": \"6212151111156744\","
				+ "\"cardUserId\": 2,"
				+ "\"cardUserName\": \"Test01\","
				+ "\"cardUserPid\": \"1231235134134\"}";
		mvc.perform(MockMvcRequestBuilders.get(URL_HEAD+"/users/1/bankcards")
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonstr))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}
	
	/**
	 * 用户的银行卡集合
	 */
	@Test
	public void bankcardsInfo() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get(URL_HEAD+"/users/1/bankcards")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}
	
	/**
	 * 支持的银行卡查看
	 */
	@Test
	public void supportBankCards() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get(URL_HEAD+"/users/supportbankcards")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}
	
	/**
	 * 我的消息
	 */
	@Test
	public void messages() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get(URL_HEAD+"/users/1/messages")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}
	
	/**
	 * 个人资产总览
	 */
	@Test
	public void getUserPersonalAssetsOverview() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get(URL_HEAD+"/users/1/assetoverviewpage?beginDate=1990-01-01&endDate=2099-01-01")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}
	
	/**
	 * 我的消息 智投推送 首页
	 */
	@Test
	public void getPersonalInvstMsg() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get(URL_HEAD+"/users/1/investmentmessages")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}
	
	/**
	 * 我的消息 系统消息
	 */
	@Test
	public void getSystemMsg() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get(URL_HEAD+"/users/1/systemmessages")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}
	
	/**
	 * 我的消息 智投推送 link
	 */
	@Test
	public void updatePersonalMsg() throws Exception {
		String jsonstr="{\"readedStatus\": true}";
		mvc.perform(MockMvcRequestBuilders.patch(URL_HEAD+"/users/1/investmentmessages/5a098b8a3f6b9e23048bd336")
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonstr))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}
	
	/**
	 * 我的消息 智投推送 link
	 */
	@Test
	public void updatePersonalMsgError() throws Exception {
		String jsonstr="{\"readedStatus\":true}";
		mvc.perform(MockMvcRequestBuilders.get(URL_HEAD+"/users/1/investmentmessages/5a098b8a3f6b9e23048bd330")
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonstr))
				.andExpect(MockMvcResultMatchers.status().is(500));
	}
	
	/**
	 * 交易记录
	 */
	@Test
	public void getTradLogsOfUser() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get(URL_HEAD+"/users/1/traderecords?pageNum=10&pageSize=2")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}
	
	/**
	 * 邀请规则
	 */
	@Test
	public void getFriendRules() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get(URL_HEAD+"/friendrules/1")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}
	
	/**
	 * 好友邀请
	 */
	@Test
	public void getFriendsInvationLinks() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get(URL_HEAD+"/users/1/friendinvitationpage?bankId=1")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}
	
	/**
	 * 关于我们
	 */
	@Test
	public void getCompanyInfo() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get(URL_HEAD+"/companyinfos?userUuid=1&bankId=1")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}
}
