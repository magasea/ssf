package com.shellshellfish.aaas.userinfo.repositories.mysql;

import java.math.BigInteger;
import java.util.List;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.shellshellfish.aaas.userinfo.model.dao.UiFriendRule;

public interface UserInfoFriendRuleRepository extends PagingAndSortingRepository<UiFriendRule,
    Long> {
  List<UiFriendRule> findAllByBankId( Long bankId);
  List<UiFriendRule> findAll();
}
