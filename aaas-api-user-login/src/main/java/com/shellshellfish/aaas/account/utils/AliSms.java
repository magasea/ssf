package com.shellshellfish.aaas.account.utils;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.QuerySendDetailsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.QuerySendDetailsResponse;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;

import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.shellshellfish.aaas.account.exception.UserException;
import com.shellshellfish.aaas.account.model.dto.VerificationBodyDTO;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Created on 17/6/7.
 * 短信API产品的DEMO程序,工程中包含了一个SmsDemo类，直接通过
 * 执行main函数即可体验短信产品API功能(只需要将AK替换成开通了云通信-短信产品功能的AK即可)
 * 工程依赖了2个jar包(存放在工程的libs目录下)
 * 1:aliyun-java-sdk-core.jar
 * 2:aliyun-java-sdk-dysmsapi.jar
 *
 * 备注:Demo工程编码采用UTF-8
 * 国际短信发送请勿参照此DEMO
 */
@Configuration
public class AliSms {
	public static final Logger logger = LoggerFactory.getLogger(AliSms.class);
	
    //产品名称:云通信短信API产品,开发者无需替换
    static final String product = "Dysmsapi";
    //产品域名,开发者无需替换
    static final String domain = "dysmsapi.aliyuncs.com";

    // TODO 此处需要替换成开发者自己的AK(在阿里云访问控制台寻找)
    @Value("${ali.accessKeyId}")
    String accessKeyId ;//= "LTAIpZL9r6q0kkPV"; 
  
    @Value("${ali.accessKeySecret}")    
    String accessKeySecret ;//= "dBsex06RIIB7LfBF2Pdx4TeRzpklwr";
    
    @Value("${app.debug}")
    boolean debugflag;
    
    @Value("${sms.signname}")
    String signname;
    
    @Value("${sms.signcode}")
    String signcode;
    
    public  SendSmsResponse sendSms(String phonenum) throws ClientException {

        //可自助调整超时时间
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");

        //初始化acsClient,暂不支持region化
        
        
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
        DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
        IAcsClient acsClient = new DefaultAcsClient(profile);

        //组装请求对象-具体描述见控制台-文档部分内容
        SendSmsRequest request = new SendSmsRequest();
        //必填:待发送手机号
        request.setPhoneNumbers(phonenum);
        //必填:短信签名-可在短信控制台中找到
        request.setSignName(signname);
        //必填:短信模板-可在短信控制台中找到
        request.setTemplateCode(signcode);
        //可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
        String vcode=makeverfication();//vcode:min=000001,max=999999
        request.setTemplateParam("{\"code\":\""+vcode+"\"}");

        //选填-上行短信扩展码(无特殊需求用户请忽略此字段)
        //request.setSmsUpExtendCode("90997");

        //可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
        request.setOutId("yourOutId");

        //hint 此处可能会抛出异常，注意catch
        
        SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);
        
        sendSmsResponse.setBizId(String.valueOf(vcode)); //set vericode
        return sendSmsResponse;
    }


    public  QuerySendDetailsResponse querySendDetails(String bizId) throws ClientException {

        //可自助调整超时时间
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");

        //初始化acsClient,暂不支持region化
        
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
        DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
        IAcsClient acsClient = new DefaultAcsClient(profile);

        //组装请求对象
        QuerySendDetailsRequest request = new QuerySendDetailsRequest();
        //必填-号码
        request.setPhoneNumber("XXXXXX");
        //可选-流水号
        request.setBizId(bizId);
        //必填-发送日期 支持30天内记录查询，格式yyyyMMdd
        SimpleDateFormat ft = new SimpleDateFormat("yyyyMMdd");
        request.setSendDate(ft.format(new Date()));
        //必填-页大小
        request.setPageSize(10L);
        //必填-当前页码从1开始计数
        request.setCurrentPage(1L);

        //hint 此处可能会抛出异常，注意catch
        QuerySendDetailsResponse querySendDetailsResponse;
        try {
             querySendDetailsResponse = acsClient.getAcsResponse(request);
        }catch (ClientException e) {
        	logger.debug(e.getErrMsg());
        	return null;
        	
        }
        return querySendDetailsResponse;
    }

    public  VerificationBodyDTO sendVerificationSms(String phonenum) {
    	SendSmsResponse response;
    	try {
    	     response = sendSms(phonenum);
    	     if(response.getCode() == null || !response.getCode().equals("OK")) {
    	    	 throw new UserException("400", response.getMessage());
    	     }
    	}catch (ClientException e) {
    	
        	logger.debug(e.getErrMsg());
        	throw new UserException(e.getErrCode(), e.getErrMsg());
        	//return null;
        	
        } 
    	
    	if (debugflag) //if debug,print debug info
    		outputDebugInfo(response);
    		//if (response.getCode().equals("OK")) {
    		
    	VerificationBodyDTO vericodebody= new VerificationBodyDTO();
        vericodebody.setTelnum(phonenum);
        vericodebody.setIdentifyingcode(response.getBizId());//get vericode        	
    	return vericodebody;
    	//}
    	
    	//return null; //send sms error
    }
    
    
    public void outputDebugInfo(SendSmsResponse response) {
    	 System.out.println("短信接口返回的数据----------------");
         System.out.println("Code=" + response.getCode());
         System.out.println("Message=" + response.getMessage());
         System.out.println("RequestId=" + response.getRequestId());
         System.out.println("BizId=" + response.getBizId());

    }
    
   /*
    public static void main(String[] args) throws ClientException, InterruptedException {

        //发短信
        //SendSmsResponse response = sendSms("15026646271");
        System.out.println("短信接口返回的数据----------------");
        System.out.println("Code=" + response.getCode());
        System.out.println("Message=" + response.getMessage());
        System.out.println("RequestId=" + response.getRequestId());
        System.out.println("BizId=" + response.getBizId());

        Thread.sleep(3000L);

        //查明细
        if(response.getCode() != null && response.getCode().equals("OK")) {
            QuerySendDetailsResponse querySendDetailsResponse = querySendDetails(response.getBizId());
            System.out.println("短信明细查询接口返回数据----------------");
            System.out.println("Code=" + querySendDetailsResponse.getCode());
            System.out.println("Message=" + querySendDetailsResponse.getMessage());
            int i = 0;
            for(QuerySendDetailsResponse.SmsSendDetailDTO smsSendDetailDTO : querySendDetailsResponse.getSmsSendDetailDTOs())
            {
                System.out.println("SmsSendDetailDTO["+i+"]:");
                System.out.println("Content=" + smsSendDetailDTO.getContent());
                System.out.println("ErrCode=" + smsSendDetailDTO.getErrCode());
                System.out.println("OutId=" + smsSendDetailDTO.getOutId());
                System.out.println("PhoneNum=" + smsSendDetailDTO.getPhoneNum());
                System.out.println("ReceiveDate=" + smsSendDetailDTO.getReceiveDate());
                System.out.println("SendDate=" + smsSendDetailDTO.getSendDate());
                System.out.println("SendStatus=" + smsSendDetailDTO.getSendStatus());
                System.out.println("Template=" + smsSendDetailDTO.getTemplateCode());
            }
            System.out.println("TotalCount=" + querySendDetailsResponse.getTotalCount());
            System.out.println("RequestId=" + querySendDetailsResponse.getRequestId());
        }

    }
    */
    public static String makeverfication(){
    	int size=6;
    	String code="";
    	for (int i=0;i<size;i++) {
    		code=code+makeonenum();
    	}
    	        //System.out.println(vcode); 
        return code;
    }
    
    public static int makeonenum() {
    	
    	Random random = new Random();

        int vcode = random.nextInt(9)%10 ;
        return vcode;

    }
}
