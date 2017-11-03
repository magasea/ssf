package com.shellshellfish.aaas.userinfo.service;


import com.shellshellfish.aaas.userinfo.model.dto.user.UserInfoAssectsBrief;
import com.shellshellfish.aaas.userinfo.model.dto.user.UserInfoBankCards;
import com.shellshellfish.aaas.userinfo.model.dto.user.UserBaseInfo;
import com.shellshellfish.aaas.userinfo.model.dto.user.UserPortfolio;

public interface UserInfoService {
    UserBaseInfo getUserInfoBase(Long userId);

    UserInfoAssectsBrief getUserInfoAssectsBrief(Long userId);

    UserInfoBankCards getUserInfoBankCards(Long userId);

    UserPortfolio getUserPortfolio(Long userId);
}
