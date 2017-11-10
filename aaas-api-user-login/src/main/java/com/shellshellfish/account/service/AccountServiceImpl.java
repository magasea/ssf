package com.shellshellfish.account.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.validation.ConstraintViolationException;

import java.util.Date;
import java.sql.Timestamp;

import org.springframework.test.context.ActiveProfiles;

import org.springframework.beans.factory.annotation.Autowired;

import com.shellshellfish.account.commons.MD5;
import com.shellshellfish.account.exception.UserException;
import com.shellshellfish.account.model.Account;
import com.shellshellfish.account.model.BankCard;
import com.shellshellfish.account.model.User;
import com.shellshellfish.account.repositories.BankCardRepository;
import com.shellshellfish.account.repositories.SmsVerificationRepositoryCustom;
import com.shellshellfish.account.repositories.UserRepository;

@ActiveProfiles(profiles = "prod")
public class AccountServiceImpl implements AccountService {

	@Autowired
    private UserRepository userRepository;

	@Autowired
	private SmsVerificationRepositoryCustom smsVerificationRepositoryCustom;
	
	@Autowired
	private BankCardRepository bankCardRepository;
	
		
	@Override
	public boolean isRegisteredUser(String cellphone, String passwordhash) {
		if (userRepository.findByCellPhoneAndPasswordHash(cellphone, passwordhash)!=null)
			return true;
		return false;
	}
	
	@Override
	public boolean isSettingPWD(String telnum, String pwdsetting, String pwdconfirm) {
		List<User> userList = userRepository.findByCellPhone(telnum);
		if (userList == null || userList.size() != 1) {
			return false;
		}
		if (pwdsetting.equals(pwdconfirm)) {
			Pattern pwdPattern = Pattern.compile(
					"^(?![A-Za-z]+$)(?![A-Z\\d]+$)(?![A-Z\\W]+$)(?![a-z\\d]+$)(?![a-z\\W]+$)(?![\\d\\W]+$)\\S{8,20}$");
			Matcher pwdMatcher = pwdPattern.matcher(pwdconfirm);
			if (pwdMatcher.find()) {
				String telRegExp = "^((13[0-9])|(15[^4])|(18[0,2,3,5-9])|(17[0-8])|(147))\\d{8}$";
				Pattern telPattern = Pattern.compile(telRegExp);
				Matcher telMatcher = telPattern.matcher(telnum);
				if (!telMatcher.find()) {
					throw new UserException("101", "手机号格式不对");
				}
				User user = userList.get(0);
				String currentPWD = user.getPasswordHash();
				String pwd = MD5.getMD5(pwdconfirm);
				if (currentPWD == null || currentPWD.equals(pwd)) {
					throw new UserException("101", "不可与原密码一致");
				}
				user.setPasswordHash(MD5.getMD5(pwdconfirm));
				Date date = new Date();
				Timestamp nowdate = new Timestamp(date.getTime());
				user.setLastModifiedDate(nowdate);
				userRepository.save(user);
				return true;
			} else {
				throw new UserException("101", "密码长度至少8位,至多16位，必须是字母 大写、字母小写、数字、特殊字符中任意三种组合");
			}
		} else {
			throw new UserException("101", "两次密码需要一致");
		}
	}

	@Override
	public boolean addBankCard(String arg[]) {
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
	public boolean isRegisterredTel(String cellphone) {
		List<User> reslst=userRepository.findByCellPhone(cellphone);
		if (reslst.size()>0)
			return true;
		
		return false;		
	}
	
	@Override
	public boolean isSmsVerified(String cellphone,String verfiedcode) {
		List<Object[]> reslst=smsVerificationRepositoryCustom.getSmsVerification(cellphone, verfiedcode);
		if (reslst.size()>0)
			return true;
		
		return false;
		
	}
}
