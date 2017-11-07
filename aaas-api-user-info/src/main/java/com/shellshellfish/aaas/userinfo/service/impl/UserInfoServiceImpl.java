package com.shellshellfish.aaas.userinfo.service.impl;

import com.shellshellfish.aaas.userinfo.model.dao.userinfo.User;
import com.shellshellfish.aaas.userinfo.model.dao.userinfo.UserPortfolioDao;
import com.shellshellfish.aaas.userinfo.model.dto.user.UserInfoAssectsBrief;
import com.shellshellfish.aaas.userinfo.model.dto.user.UserInfoBankCards;
import com.shellshellfish.aaas.userinfo.model.dto.user.UserBaseInfo;
import com.shellshellfish.aaas.userinfo.model.dto.user.UserPortfolio;
import com.shellshellfish.aaas.userinfo.dao.repositories.UserInfoRepository;
import com.shellshellfish.aaas.userinfo.dao.repositories.UserPortfolioRepository;
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
    UserInfoRepository userInfoRepository;

    @Autowired
    UserPortfolioRepository userPortfolioRepository;



    @Override
    public UserBaseInfo getUserInfoBase(Long userId) {
//        UserBaseInfo userInfoBase = new UserBaseInfo();
//        userInfoBase.setBirthDay(new Date());
//        userInfoBase.setOccupation("程序员");
//        userInfoBase.setPhoneNumber("123456789123456789");
//        userInfoBase.setUserId(userId);
//        return userInfoBase;
        User userInfoDao = userInfoRepository.findById(userId);
        UserBaseInfo userBaseInfo = new UserBaseInfo();
        BeanUtils.copyProperties(userInfoDao, userBaseInfo);

        return userBaseInfo;
    }

    @Override
    public UserInfoAssectsBrief getUserInfoAssectsBrief(Long userId) {
        UserInfoAssectsBrief userInfoAssectsBrief = new UserInfoAssectsBrief();
        userInfoAssectsBrief.setUserId(userId);
        userInfoAssectsBrief.setDailyProfit(Float.valueOf("0.1"));
        userInfoAssectsBrief.setTotalProfit(Float.valueOf("1.1"));
        userInfoAssectsBrief.setTotalAssects("100000");
        return userInfoAssectsBrief;
    }

    @Override
    public UserInfoBankCards getUserInfoBankCards(Long userId) {
        UserInfoBankCards userInfoBankCards = new UserInfoBankCards();
//        userInfoBankCards.setUserId(userId);
//        List<BankCard> bankCardList = new ArrayList<>();
//        List<BankCard> cards = IntStream.rangeClosed(1, 4)
//                .boxed()
//                .flatMap(value ->
//                        IntStream.rangeClosed(1, 13)
//                                .mapToObj(suit -> new BankCard(""+value,
//                                    "张三", new Date(), "银行"+suit ))
//                )
//                .collect(Collectors.toList());
//        userInfoBankCards.setBankCardList(cards);

        return userInfoBankCards;
    }

    @Override
    public List<UserPortfolio> getUserPortfolios(Long userId) {

        List<UserPortfolioDao> userPortfolioDaos =  userPortfolioRepository.findAllByUserId(userId);
        List<UserPortfolio> userPortfolios = new ArrayList<>();
        for(UserPortfolioDao userPortfolioDao: userPortfolioDaos){
            UserPortfolio userPortfolio = new UserPortfolio();
            BeanUtils.copyProperties(userPortfolioDao, userPortfolio);
            userPortfolios.add(userPortfolio);
        }
        return userPortfolios;
    }

}
