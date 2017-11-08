package com.shellshellfish.aaas.userinfo.dao.repositories;

import com.shellshellfish.aaas.userinfo.model.dao.userinfo.UiBankcard;
import com.shellshellfish.aaas.userinfo.model.dao.userinfo.UserPortfolioDao;
import com.shellshellfish.aaas.userinfo.model.dto.user.UserInfoBankCards;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface UserInfoBankCardsRepository extends CrudRepository<UiBankcard, Long> {

  List<UiBankcard> findAllByUserId(Long userId);

}
