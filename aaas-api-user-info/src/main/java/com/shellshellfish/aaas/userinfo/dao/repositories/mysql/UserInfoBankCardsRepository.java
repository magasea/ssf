package com.shellshellfish.aaas.userinfo.dao.repositories.mysql;

import com.shellshellfish.aaas.userinfo.model.dao.userinfo.UiBankcard;
import java.math.BigInteger;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface UserInfoBankCardsRepository extends
    PagingAndSortingRepository<UiBankcard, BigInteger> {

  List<UiBankcard> findAllByUserId(BigInteger userId);

  @Override
  UiBankcard findOne(BigInteger bigInteger);

//  @Query("select u from ui_bankcard u where u.bank_name = ?1")
  List<UiBankcard> findByBankName(String bankName);

  UiBankcard findUiBankcardByCardNumberIs(String cardNumber);
}
