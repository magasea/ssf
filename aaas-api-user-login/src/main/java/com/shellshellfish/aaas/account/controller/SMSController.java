package com.shellshellfish.aaas.account.controller;

import com.shellshellfish.aaas.account.repositories.mysql.SmsVerificationRepositoryCustom;
import com.shellshellfish.aaas.account.service.AccountService;
import io.swagger.annotations.*;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @Author pierre
 * 17-12-22
 */
@Api(
		"短信发送和校验"
)
@RestController
@RequestMapping("/api/sms")
public class SMSController {


	@Autowired
	AccountService accountService;


	@Autowired
	SmsVerificationRepositoryCustom smsVerificationRepositoryCustom;

	@ApiOperation("验证码发送接口，成功返回验证码，失败返回-1")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "path", name = "telnum", dataType = "String", required = true, value = "电话号码", defaultValue = "15205132051")
	})
	@RequestMapping(value = "/sendVerificationCode/{telnum}",method = RequestMethod.GET)
	public String sendVerificationCode(@PathVariable @NotNull @Length(min = 8, max = 11) String telnum) {
		String result = accountService.sendSmsMessage(telnum);

		if (result == null | result.isEmpty()) {
			return "-1";
		}

		return result;
	}

	@ApiOperation("短信校验接口 成功返回1,失败返回 -1")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", name = "telnum", dataType = "String", required = true, value = "电话号码", defaultValue = "15205132051"),
			@ApiImplicitParam(paramType = "query", name = "verificationCode", dataType = "String", required = true, value = "电话号码", defaultValue = "123456")
	})

	@RequestMapping(value = "/checkSmsCode",method = RequestMethod.GET)
	public int checkSmsCode(@RequestParam @NotNull @Length(min = 8, max = 11) String telnum, @RequestParam @NotNull @Length(min = 6, max = 6) String verificationCode) {
		List result = smsVerificationRepositoryCustom.getSmsVerification(telnum, verificationCode);

		if (result == null | result.isEmpty()) {
			return 0;
		}

		return -1;
	}
}
