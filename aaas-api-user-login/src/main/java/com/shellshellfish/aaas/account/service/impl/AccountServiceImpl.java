package com.shellshellfish.aaas.account.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
//import org.springframework.test.context.ActiveProfiles;
import org.springframework.beans.factory.annotation.Autowired;
import com.shellshellfish.aaas.account.exception.UserException;
import com.shellshellfish.aaas.account.model.dao.BankCard;
import com.shellshellfish.aaas.account.model.dao.SmsVerification;
import com.shellshellfish.aaas.account.model.dao.User;
import com.shellshellfish.aaas.account.model.dto.LoginBodyDTO;
import com.shellshellfish.aaas.account.model.dto.PwdSettingBodyDTO;
import com.shellshellfish.aaas.account.model.dto.RegistrationBodyDTO;
import com.shellshellfish.aaas.account.model.dto.UpdateRegistrationBodyDTO;
import com.shellshellfish.aaas.account.model.dto.UserDTO;
import com.shellshellfish.aaas.account.model.dto.VerificationBodyDTO;
import com.shellshellfish.aaas.account.repositories.mysql.BankCardRepository;
import com.shellshellfish.aaas.account.repositories.mysql.SmsVerificationRepository;
import com.shellshellfish.aaas.account.repositories.mysql.SmsVerificationRepositoryCustom;
import com.shellshellfish.aaas.account.repositories.mysql.UserRepository;
import com.shellshellfish.aaas.account.service.AccountService;
import com.shellshellfish.aaas.account.service.RedisService;
import com.shellshellfish.aaas.account.utils.AliSms;
import com.shellshellfish.aaas.account.utils.MD5;
import com.shellshellfish.aaas.account.utils.MyBeanUtils;

//@ActiveProfiles(profiles = "prod")
public class AccountServiceImpl implements AccountService {

	private final static Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);

	@Autowired
    private UserRepository userRepository;

	@Autowired
	private SmsVerificationRepositoryCustom smsVerificationRepositoryCustom;
	
	@Autowired
	private BankCardRepository bankCardRepository;
	
	@Autowired
	private SmsVerificationRepository smsVerificationRepository;
	
//	@Autowired
//	private AssetRepository assetRepository;
	
	@Autowired
    private RedisService redisService;
	
	@Autowired
    private AliSms alisms;

	@Override
	public List<User> isRegisteredUser(LoginBodyDTO loginBodyDTO) throws RuntimeException{
//		String password = redisService.doGetPwd(cellphone, passwordhash);
//		if(password!=null){
//			return true;
//		}
		String cellphone = loginBodyDTO.getTelnum();
		String passwordhash = loginBodyDTO.getPassword();
		List<User> userListPhone = userRepository.findByCellPhone(cellphone);
		if(userListPhone==null||userListPhone.size()==0){
			logger.error("登录用户账号不正确");
			throw new UserException("101", "登录用户账号不正确");
		}
		List<User> userList = userRepository.findByCellPhoneAndPasswordHash(cellphone, passwordhash);
		//List<UiAsset> userAssetList = assetRepository.findAll();
		if (userList != null && userList.size() > 0) {
//			redisService.doPwdSave(cellphone,passwordhash);
			return userList;
		} else {
			//return new ArrayList<User>();
			logger.error("密码不正确");
			throw new UserException("101", "密码不正确");
		}
	}
	
	@Override
	public String isSettingPWD(PwdSettingBodyDTO pwdSettingBody) throws RuntimeException{
		String telnum = pwdSettingBody.getTelnum();
		String pwdsetting = pwdSettingBody.getPassword();
		String pwdconfirm = pwdSettingBody.getPwdconfirm();
		List<User> userList = userRepository.findByCellPhone(telnum);
		if (userList == null || userList.size() != 1) {//有可能多于1条的冗余数据
			return "";
		}
		if (pwdsetting.equals(pwdconfirm)) {
			Pattern pwdPattern = Pattern.compile(
					"^(?![A-Za-z]+$)(?![A-Z\\d]+$)(?![A-Z\\W]+$)(?![a-z\\d]+$)(?![a-z\\W]+$)(?![\\d\\W]+$)\\S{8,20}$");
			Matcher pwdMatcher = pwdPattern.matcher(pwdconfirm);
			if (pwdMatcher.find()) {
				String telRegExp = "^((1[3,5,8][0-9])|(14[5,7])|(17[0,6,7,8])|(19[7]))\\d{8}$";
				Pattern telPattern = Pattern.compile(telRegExp);
				Matcher telMatcher = telPattern.matcher(telnum);
				if (!telMatcher.find()) {
					//throw new UserException("101", "手机号格式不对");
				}
				User user = userList.get(0);
				String currentPWD = user.getPasswordHash();
				String pwd = MD5.getMD5(pwdconfirm);
				if (currentPWD == null || currentPWD.equals(pwd)) {
					logger.error("不可与原密码一致");
					throw new UserException("101", "不可与原密码一致");
				}
				user.setPasswordHash(MD5.getMD5(pwdconfirm));
				Date date = new Date();
				Timestamp nowdate = new Timestamp(date.getTime());
				user.setLastModifiedDate(nowdate);
				userRepository.save(user);
				String userId = user.getId()+"";
				return userId;
			} else {
				logger.error("密码长度至少8位,至多16位，必须是字母 大写、字母小写、数字、特殊字符中任意三种组合");
				throw new UserException("102", "密码长度至少8位,至多16位，必须是字母 大写、字母小写、数字、特殊字符中任意三种组合");
			}
		} else {
			logger.error("两次密码需要一致");
			throw new UserException("103", "两次密码需要一致");
		}
	}

	@Override
	public boolean addBankCard(String arg[]) throws RuntimeException{
		// 手机号telnum, bkcardnum, bkname, name
		String telnum = arg[0];
		// 用户名
		String name = arg[3];
		// 银行卡号
		String bkcard = arg[1];
		// 银行名称
		String bkname = arg[2];
		// 验证银行卡号的正则表达式
		List<User> userList = userRepository.findByCellPhone(telnum);
		if (userList == null || userList.size() != 1) {
			return false;
		}
		User user = userList.get(0);
		BankCard bankCard = new BankCard();
		bankCard.setBankName(bkname);
		bankCard.setCardNumber(bkcard);
		bankCard.setCardUserName(bkname);
		bankCard.setCreatedBy(user.getCellPhone());
		Date date = new Date();
		Timestamp nowdate = new Timestamp(date.getTime());
		bankCard.setCreatedDate(nowdate);
		bankCard.setUserId(user.getId());
		bankCardRepository.save(bankCard);

		return true;
	}
	
	@Override
	public List<UserDTO> isRegisterredTel(RegistrationBodyDTO registrationBody) throws RuntimeException {
		String cellphone = registrationBody.getTelnum();
		String telRegExp = "^((1[3,5,8][0-9])|(14[5,7])|(17[0,6,7,8])|(19[7]))\\d{8}$";
		Pattern telPattern = Pattern.compile(telRegExp);
		Matcher telMatcher = telPattern.matcher(cellphone);
		if (!telMatcher.find()) {
			//throw new UserException("101", "手机号格式不对");
		}
		List<User> result = userRepository.findByCellPhone(cellphone);
		List<UserDTO> userDtoList = null;
		if(result!=null&&result.size()>0){
			userDtoList = new ArrayList<UserDTO>();
			try {
				userDtoList = MyBeanUtils.convertList(result, UserDTO.class);
			} catch (IllegalAccessException | InstantiationException e) {
				logger.error("exception:",e);
			}
		}
		
		return userDtoList;		
	}
	
	@Override
	public UserDTO isSmsVerified(UpdateRegistrationBodyDTO registrationBodyDTO) throws RuntimeException {
		String cellphone = registrationBodyDTO.getTelnum();
		String verfiedcode = registrationBodyDTO.getIdentifyingcode();
		String password = registrationBodyDTO.getPassword();
		if (password.length() < 6 || password.length() > 20) {
			logger.error("密码长度至少为6~20位，请重新输入.");
			throw new UserException("101", "密码长度至少为6~20位，请重新输入.");
		}
		List<Object[]> reslst=smsVerificationRepositoryCustom.getSmsVerification(cellphone, verfiedcode);
		// List<SmsVerification> reslst=smsVerificationRepository.findByCellPhoneAndSmsCode(cellphone, verfiedcode);
		if (reslst==null||reslst.size()==0){
			// TODO 临时注释2018-01-22 
			/**********************start****************************/
			if(!"123456".equals(verfiedcode)){
				logger.error("验证码不正确，请重新输入");
				throw new UserException("101", "验证码不正确，请重新输入");
			}
			/**********************end******************************/
			//throw new UserException("101", "验证码不正确，请重新输入");
		}
		User user = new User();
		if(!registrationBodyDTO.getPassword().equals(registrationBodyDTO.getPwdconfirm())){
			logger.error("两次密码不一致");
			throw new UserException("101", "两次密码不一致");
		}
		List<User> userList = userRepository.findByCellPhone(cellphone);
		if(userList!=null&&userList.size()>0){
			user = userList.get(0);
		} else {
			user.setCellPhone(cellphone);
			user.setUuid(UUID.randomUUID().toString());
		}
		user.setPasswordHash(MD5.getMD5(registrationBodyDTO.getPassword()));
		Date date = new Date();
		Timestamp nowdate = new Timestamp(date.getTime());
		user.setLastModifiedDate(nowdate);
		User user1 = userRepository.save(user);
//		List<User> userList = userRepository.findByCellPhoneAndPasswordHash(user.getCellPhone(),user.getPasswordHash());
//		User userResult = userList.get(0);
		UserDTO userDto = new UserDTO();
		BeanUtils.copyProperties(user1, userDto);
		return userDto;
	}
	
	@Override
	public String sendSmsMessage(String telnum) throws RuntimeException {
		
		VerificationBodyDTO vcodebody=alisms.sendVerificationSms(telnum);
		if (vcodebody==null)
			return "";
		
		//save to redis server
		//Boolean result = redisService.saveVeribody(vcodebody);
		SmsVerification sms = smsVerificationRepository.findByCellPhone(telnum);
		SmsVerification smsVerification = new SmsVerification();
		if(sms!=null){
			smsVerification.setId(sms.getId());
		}
		smsVerification.setCellPhone(telnum);
		Date date = new Date();
//		smsVerification.setCreatedDate(nowdate);
//		smsVerification.setExpireTime(nowdate);
		Timestamp nowdate = new Timestamp(date.getTime()); 
		Timestamp expdate = new Timestamp(date.getTime()+ 600000); //1 minute
		
		smsVerification.setCreatedDate(nowdate);
		smsVerification.setExpireTime(expdate);
		smsVerification.setSmsCode(vcodebody.getIdentifyingcode());
		smsVerificationRepository.save(smsVerification);
		return vcodebody.getIdentifyingcode();
	}
	
	@Override
	public boolean doSmsVerification(VerificationBodyDTO vbody) throws RuntimeException {
	  	return redisService.doSmsVerification(vbody);
	}

//	@Override
//	public UserDTO doLogout(LoginBodyDTO loginBody) {
//		String cellphone = loginBody.getTelnum();
//		String password = loginBody.getPassword();
//		redisService.doLogout(cellphone, password);
//		return null;
//	}
	
	@Override
	public boolean doLogout(String uuid, String token) {
		User user = userRepository.findByUuid(uuid);
		if(user!=null){
			return true;
		}
		return false;
	}
	
	@Override
	public UserDTO getUserInfo(String uuid) throws IllegalAccessException, InstantiationException {
		User user = userRepository.findByUuid(uuid);
		UserDTO userDTO = new UserDTO();
		if(user!=null){
			BeanUtils.copyProperties(user, userDTO);
		}
		return userDTO;
	}

	@Override
	public String getSmsMessage(String cellphone) {
		SmsVerification sms = smsVerificationRepository.findByCellPhone(cellphone);
		String smsCode = "";
		if(sms!=null){
			smsCode = sms.getSmsCode();
		}
		return smsCode;
	}

	@Override
	public void updateUser(String uuid, String password, String newPassword) throws IllegalAccessException, InstantiationException {
		UserDTO userDto = this.getUserInfo(uuid);
		User user = new User();
		if(userDto == null){
			logger.error("用户不存在");
			throw new UserException("404", "用户不存在");
		} else {
			if(!password.equals(userDto.getPasswordHash())){
				logger.error("用户与原密码不一致，请重新输入");
				throw new UserException("404", "用户与原密码不一致，请重新输入");
			}
			userDto.setPasswordHash(newPassword);
			BeanUtils.copyProperties(userDto, user);
			userRepository.save(user);
		}
	}
}