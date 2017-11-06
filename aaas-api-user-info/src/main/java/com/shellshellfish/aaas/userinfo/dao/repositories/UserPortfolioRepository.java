package com.shellshellfish.aaas.userinfo.dao.repositories;

import com.shellshellfish.aaas.userinfo.model.dao.userinfo.UserPortfolioDao;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface UserPortfolioRepository extends CrudRepository<UserPortfolioDao, Long> {

  List<UserPortfolioDao> findAllByUserId( Long userId);

}
