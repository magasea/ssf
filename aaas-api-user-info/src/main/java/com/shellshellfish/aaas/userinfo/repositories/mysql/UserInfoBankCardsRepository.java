package com.shellshellfish.aaas.userinfo.repositories.mysql;

import com.shellshellfish.aaas.userinfo.model.dao.UiBankcard;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface UserInfoBankCardsRepository extends
		PagingAndSortingRepository<UiBankcard, Long> {

	List<UiBankcard> findAllByUserId(Long userId);

	List<UiBankcard> findAllByUserIdAndCardNumber(Long userId, String bankName);

	@Override
	UiBankcard findOne(Long userId);

	//  @Query("select u from ui_bankcard u where u.bank_name = ?1")
	List<UiBankcard> findByBankName(String bankName);

	UiBankcard findUiBankcardByCardNumberIs(String cardNumber);

	Integer deleteByCardNumber(String cardNumber);
}
