package com.shellshellfish.aaas.oeminfo.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.shellshellfish.aaas.dto.FinanceProdBuyInfo;
import com.shellshellfish.aaas.dto.FinanceProdSellInfo;

/**
 * 作一些数据处理
 *
 * @author chenwei
 */
@Service
public interface MidApiService {


	/**
	 * 验证码验证接口
	 *
	 * @param msgCode
	 * @return
	 * @throws Exception
	 */
	String verifyMSGCode(String telNum, String msgCode) throws Exception;

	/**
	 * 获取产品的code，名字，净值增长值
	 *
	 * @param groupId
	 * @param subGroupId
	 * @return
	 */
	Map<String, Object> getPrdNPVList(String groupId, String subGroupId, Integer oemid);

	/**
	 * 获取产品的年收益率和最大回撤率
	 *
	 * @param groupId
	 * @param subGroupId
	 * @return
	 */
	Map<String, Object> getExpAnnualAndMaxReturn(String groupId, String subGroupId, Integer oemid);

	/**
	 * 获取优化方案后的结果
	 *
	 * @param riskLevel
	 * @param invstTerm
	 * @return
	 */
	Map<String, Object> getOptAdjustment(String riskLevel, String invstTerm, Integer oemid) throws Exception;

	/**
	 * 购买基金
	 *
	 * @param prdInfo
	 * @return
	 * @throws Exception
	 */
	Map buyProduct(FinanceProdBuyInfo prdInfo) throws Exception;

	/**
	 * 赎回单只基金接口(这个作废不要用)
	 *
	 * @param uuid     客户的uuid
	 * @param sellNum  份额
	 * @param tradeAcc 交易的中正给的账户号
	 * @param fundCode 基金代码
	 * @return
	 * @throws Exception
	 */
	Map sellFund(String uuid, String sellNum, String tradeAcc, String fundCode) throws Exception;

	/**
	 * 赎回基金页面计算每只基金的赎回金额
	 *
	 * @param groupId
	 * @param subGroupId
	 * @param totalAmount
	 * @return
	 * @throws Exception
	 */
	Map sellFundPage(String groupId, String subGroupId, String totalAmount, Integer oemid,BigDecimal present) throws Exception;

	/**
	 * 赎回基金确认
	 *
	 * @return
	 * @throws Exception
	 */
	Map sellFund(String userProdId, String prodId, String groupId, String userId, List<FinanceProdSellInfo> infoList) throws Exception;

	/**
	 * 按百分比赎回
	 * 
	 * @param userProdId
	 * @param prodId
	 * @param groupId
	 * @param userUuid
	 * @return
	 */
	Map sellFundPersent(String userProdId, String prodId, String groupId, String userUuid, String userBankNum, BigDecimal sellTargetPercent) throws Exception ;

}
