package com.shellshellfish.aaas.userinfo.service.impl;

import com.shellshellfish.aaas.userinfo.dao.service.UserInfoRepoService;
import com.shellshellfish.aaas.userinfo.model.dao.userinfo.BankCard;
import com.shellshellfish.aaas.userinfo.model.dao.userinfo.UiAsset;
import com.shellshellfish.aaas.userinfo.model.dao.userinfo.UiBankcard;
import com.shellshellfish.aaas.userinfo.model.dao.userinfo.UiPortfolio;
import com.shellshellfish.aaas.userinfo.model.dao.userinfo.UiUser;
import com.shellshellfish.aaas.userinfo.model.dto.user.UserBaseInfo;
import com.shellshellfish.aaas.userinfo.model.dto.user.UserInfoAssectsBrief;
import com.shellshellfish.aaas.userinfo.model.dto.user.UserPortfolio;
import com.shellshellfish.aaas.userinfo.service.UserInfoService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("userInfoService")
@Transactional
public class UserInfoServiceImpl implements UserInfoService {

    @Autowired
    UserInfoRepoService userInfoRepoService;

    @Override
    public UserBaseInfo getUserInfoBase(Long userId) {
        UiUser userInfoDao = userInfoRepoService.getUserInfoBase(userId);
        UserBaseInfo userBaseInfo = new UserBaseInfo();
        if( null != userInfoDao) {
            BeanUtils.copyProperties(userInfoDao, userBaseInfo);
        }
        return userBaseInfo;
    }

    @Override
    public UserInfoAssectsBrief getUserInfoAssectsBrief(Long userId) {
        UserInfoAssectsBrief userInfoAssectsBrief = new UserInfoAssectsBrief();
        UiAsset userInfoAssect = userInfoRepoService.getUserInfoAssectsBrief(userId);
        if(null != userInfoAssect){
            BeanUtils.copyProperties(userInfoAssect, userInfoAssectsBrief);
        }
        return userInfoAssectsBrief;
    }

    @Override
    public List<BankCard> getUserInfoBankCards(Long userId) {

        List<UiBankcard> uiBankcards =  userInfoRepoService.getUserInfoBankCards(userId);
        List<BankCard> bankCardsDto = new ArrayList<>();
        for(UiBankcard uiBankcard: uiBankcards ){
            BankCard bankCard = new BankCard();
            BeanUtils.copyProperties(uiBankcard, bankCard);
            bankCardsDto.add(bankCard);
        }
        return bankCardsDto;
    }

    @Override
    public List<UserPortfolio> getUserPortfolios(Long userId) {
        List<UiPortfolio> userPortfolioDaos =  userInfoRepoService.getUserPortfolios(userId);
        List<UserPortfolio> userPortfolios = new ArrayList<>();
        for(UiPortfolio userPortfolioDao: userPortfolioDaos){
            UserPortfolio userPortfolio = new UserPortfolio();
            BeanUtils.copyProperties(userPortfolioDao, userPortfolio);
            userPortfolios.add(userPortfolio);
        }
        return userPortfolios;
    }

}
