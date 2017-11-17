package com.shellshellfish.aaas.userinfo.dao.repositories.mysql;

import com.shellshellfish.aaas.userinfo.model.dao.userinfo.UiFriendRule;
import java.math.BigInteger;
import java.util.List;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface UserInfoFriendRuleRepository extends PagingAndSortingRepository<UiFriendRule,
    Long> {
  List<UiFriendRule> findAllByBankId( Long bankId);
  List<UiFriendRule> findAll();
}
