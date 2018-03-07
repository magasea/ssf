package com.shellshellfish.aaas.userinfo.service.impl;

import com.shellshellfish.aaas.common.enums.TrdOrderOpTypeEnum;
import com.shellshellfish.aaas.common.message.order.MongoUiTrdZZInfo;
import com.shellshellfish.aaas.common.utils.InstantDateUtil;
import com.shellshellfish.aaas.common.utils.TradeUtil;
import com.shellshellfish.aaas.userinfo.model.DailyAmount;
import com.shellshellfish.aaas.userinfo.model.dao.UiProductDetail;
import com.shellshellfish.aaas.userinfo.model.dao.UiProducts;
import com.shellshellfish.aaas.userinfo.repositories.mysql.UiProductDetailRepo;
import com.shellshellfish.aaas.userinfo.repositories.mysql.UiProductRepo;
import com.shellshellfish.aaas.userinfo.repositories.mysql.UserInfoRepository;
import com.shellshellfish.aaas.userinfo.service.UserFinanceProdCalcService;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

/**
 * @Author pierre 18-3-1
 */
@Service
public class CalculateConfirmedAsset {

	Logger logger = LoggerFactory.getLogger(CalculateConfirmedAsset.class);

	@Autowired
	UserFinanceProdCalcService userFinanceProdCalcService;

	@Autowired
	UiProductDetailRepo uiProductDetailRepo;

	@Autowired
	UiProductRepo uiProductRepo;

	@Autowired
	@Qualifier("zhongZhengMongoTemplate")
	MongoTemplate zhongZhengMongoTemplate;

	@Autowired
	UserInfoRepository userInfoRepository;

	/**
	 * 当有申购或者赎回确认的消息时 ,重新计算资产
	 */
	public void calculateConfirmedAsset(MongoUiTrdZZInfo mongoUiTrdZZInfo) {
		UiProducts uiProducts = uiProductRepo.findById(mongoUiTrdZZInfo.getUserProdId());
		List<UiProductDetail> uiProductDetailList = uiProductDetailRepo
				.findAllByUserProdId(mongoUiTrdZZInfo.getUserProdId());
		String date = InstantDateUtil.format(LocalDate.now(), "yyyyMMdd");
		String uuid = Optional.ofNullable(userInfoRepository.findById(mongoUiTrdZZInfo.getUserId()))
				.map(m -> m.getUuid()).orElse("-1");
		for (UiProductDetail uiProductDetail : uiProductDetailList) {
			try {
				userFinanceProdCalcService
						.calculateFromZzInfo(uiProductDetail, uuid, uiProducts.getProdId(), date);
			} catch (Exception e) {
				logger.error("calculate dailyAmount failed:{}", uiProductDetail);
			}
		}
		updateDailyAmountFromZzInfo(uuid, uiProducts.getProdId(), uiProducts.getId(),
				mongoUiTrdZZInfo.getFundCode(), date,
				TradeUtil.getBigDecimalNumWithDiv100(mongoUiTrdZZInfo.getTradeConfirmSum()),
				mongoUiTrdZZInfo.getTradeType());
	}

	private void updateDailyAmountFromZzInfo(String userUuid, Long prodId, Long userProdId,
			String fundCode, String startDate, BigDecimal amount, int type) {

		Query query = new Query();
		query.addCriteria(Criteria.where("userUuid").is(userUuid))
				.addCriteria(Criteria.where("date").is(startDate))
				.addCriteria(Criteria.where("fundCode").is(fundCode))
				.addCriteria(Criteria.where("prodId").is(prodId))
				.addCriteria(Criteria.where("userProdId").is(userProdId));

		Update update = new Update();

		if (TrdOrderOpTypeEnum.BUY.getOperation() == type) {
			update.set("buyAmount", amount);
		}
		if (TrdOrderOpTypeEnum.REDEEM.getOperation() == type) {
			update.set("sellAmount", amount);
		}

		DailyAmount dailyAmount = zhongZhengMongoTemplate
				.findAndModify(query, update, new FindAndModifyOptions().returnNew(true).upsert(true),
						DailyAmount.class);
		logger.info(
				"set buyAmount and sell Amount ==> dailyAmount:{}", dailyAmount);

	}
}
