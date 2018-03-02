package com.shellshellfish.aaas.userinfo.service.impl;

import com.shellshellfish.aaas.common.utils.InstantDateUtil;
import com.shellshellfish.aaas.userinfo.model.dao.UiProductDetail;
import com.shellshellfish.aaas.userinfo.model.dao.UiProducts;
import com.shellshellfish.aaas.userinfo.repositories.mysql.UiProductDetailRepo;
import com.shellshellfish.aaas.userinfo.repositories.mysql.UiProductRepo;
import com.shellshellfish.aaas.userinfo.repositories.mysql.UserInfoRepository;
import com.shellshellfish.aaas.userinfo.service.UserFinanceProdCalcService;
import java.time.LocalDate;
import java.util.Optional;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author pierre 18-3-1
 */
@Service
public class CalculateConfirmedAsset {

	@Autowired
	UserFinanceProdCalcService userFinanceProdCalcService;

	@Autowired
	UiProductDetailRepo uiProductDetailRepo;

	@Autowired
	UiProductRepo uiProductRepo;

	@Autowired
	UserInfoRepository userInfoRepository;

	/**
	 * 当有申购或者赎回确认的消息时 ,重新计算资产
	 */
	public void calculateConfirmedAsset(Long userProdId, Long userId, String fundCode) {
		UiProductDetail uiProductDetail = uiProductDetailRepo
				.findByUserProdIdAndFundCode(userProdId, fundCode);
		UiProducts uiProducts = uiProductRepo.findByProdId(uiProductDetail.getUserProdId());

		String uuid = Optional.ofNullable(userInfoRepository.findById(userId)).map(m -> m.getUuid())
				.orElse("-1");
		String date = InstantDateUtil.format(LocalDate.now(), "yyyyMMdd");
		userFinanceProdCalcService
				.calculateProductAsset(uiProductDetail, uuid, uiProducts.getProdId(), date);


	}
}
