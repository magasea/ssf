package com.shellshellfish.aaas.userinfo.service;


import com.shellshellfish.aaas.userinfo.model.user.UserInfoAssectsBrief;
import com.shellshellfish.aaas.userinfo.model.user.UserInfoBankCards;
import com.shellshellfish.aaas.userinfo.model.user.UserBaseInfo;
import com.shellshellfish.aaas.userinfo.model.user.UserPortfolio;

public interface UserInfoService {
    UserBaseInfo getUserInfoBase(Long userId);

    UserInfoAssectsBrief getUserInfoAssectsBrief(Long userId);

    UserInfoBankCards getUserInfoBankCards(Long userId);

    UserPortfolio getUserPortfolio(Long userId);
}
