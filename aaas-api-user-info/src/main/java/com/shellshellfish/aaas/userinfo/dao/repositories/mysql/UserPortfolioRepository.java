package com.shellshellfish.aaas.userinfo.dao.repositories.mysql;

import java.math.BigInteger;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.shellshellfish.aaas.userinfo.model.dao.UiPortfolio;

public interface UserPortfolioRepository extends PagingAndSortingRepository<UiPortfolio, Long> {

  List<UiPortfolio> findAllByUserId( Long userId);

}
