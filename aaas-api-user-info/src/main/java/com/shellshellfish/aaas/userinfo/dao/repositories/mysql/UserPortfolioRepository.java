package com.shellshellfish.aaas.userinfo.dao.repositories.mysql;

import com.shellshellfish.aaas.userinfo.model.dao.userinfo.UiPortfolio;
import java.math.BigInteger;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface UserPortfolioRepository extends PagingAndSortingRepository<UiPortfolio, Long> {

  List<UiPortfolio> findAllByUserId( Long userId);

}
