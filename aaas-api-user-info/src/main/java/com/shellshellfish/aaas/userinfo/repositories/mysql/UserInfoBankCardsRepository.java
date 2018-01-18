package com.shellshellfish.aaas.userinfo.repositories.mysql;

import com.shellshellfish.aaas.common.enums.BankCardStatusEnum;
import com.shellshellfish.aaas.userinfo.model.dao.UiBankcard;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface UserInfoBankCardsRepository extends
		PagingAndSortingRepository<UiBankcard, Long> {

	List<UiBankcard> findAllByUserId(Long userId);

	List<UiBankcard> findAllByUserIdAndStatusIs(Long userId, int status);


	List<UiBankcard> findAllByUserIdAndCardNumber(Long userId, String cardNumber);

	@Override
	UiBankcard findOne(Long userId);

	//  @Query("select u from ui_bankcard u where u.bank_name = ?1")
	List<UiBankcard> findByBankName(String bankName);

	List<UiBankcard> findUiBankcardByCardNumberIs(String cardNumber);

	Integer deleteByCardNumber(String cardNumber);

	@Modifying(clearAutomatically = true)
	@Query("UPDATE UiBankcard SET status = "
			+ "-1 WHERE userId = :userId and cardNumber = :cardNumber")
	void setBankCardInvalid(String cardNumber, Long userId);
}
