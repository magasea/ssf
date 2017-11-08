package com.shellshellfish.aaas.userinfo.dao.repositories;

import com.shellshellfish.aaas.userinfo.model.dao.userinfo.UiBankcard;
import java.math.BigInteger;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface UserInfoBankCardsRepository extends CrudRepository<UiBankcard, BigInteger> {

  List<UiBankcard> findAllByUserId(BigInteger userId);

  @Override
  UiBankcard findOne(BigInteger bigInteger);

//  @Query("select u from ui_bankcard u where u.bank_name = ?1")
  List<UiBankcard> findByBankName(String bankName);
}
