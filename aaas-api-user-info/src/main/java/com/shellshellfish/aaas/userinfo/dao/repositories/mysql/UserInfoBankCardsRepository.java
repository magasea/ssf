package com.shellshellfish.aaas.userinfo.dao.repositories.mysql;

import java.math.BigInteger;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.shellshellfish.aaas.userinfo.model.dao.UiBankcard;

public interface UserInfoBankCardsRepository extends
    PagingAndSortingRepository<UiBankcard, Long> {

  List<UiBankcard> findAllByUserId(Long userId);

  @Override
  UiBankcard findOne(Long userId);

//  @Query("select u from ui_bankcard u where u.bank_name = ?1")
  List<UiBankcard> findByBankName(String bankName);

  UiBankcard findUiBankcardByCardNumberIs(String cardNumber);
}
