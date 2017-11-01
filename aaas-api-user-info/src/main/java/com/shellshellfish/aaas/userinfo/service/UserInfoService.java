package com.shellshellfish.aaas.userinfo.service;


import com.shellshellfish.aaas.userinfo.model.user.UserInfoAssectsBrief;
import com.shellshellfish.aaas.userinfo.model.user.UserInfoBankCards;
import com.shellshellfish.aaas.userinfo.model.user.UserInfoBase;
import com.shellshellfish.aaas.userinfo.model.user.UserPortfolio;

public interface UserInfoService {
    UserInfoBase getUserInfoBase(Long userId);

    UserInfoAssectsBrief getUserInfoAssectsBrief(Long userId);

    UserInfoBankCards getUserInfoBankCards(Long userId);

    UserPortfolio getUserPortfolio(Long userId);
}
