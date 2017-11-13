package com.shellshellfish.aaas.userinfo.dao.repositories.mysql;

import com.shellshellfish.aaas.userinfo.model.dao.userinfo.UiPortfolio;
import java.math.BigInteger;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface UserPortfolioRepository extends CrudRepository<UiPortfolio, Long> {

  List<UiPortfolio> findAllByUserId( BigInteger userId);

}
