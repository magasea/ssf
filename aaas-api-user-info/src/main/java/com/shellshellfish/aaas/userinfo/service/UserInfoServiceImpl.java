package com.shellshellfish.aaas.userinfo.service;

import com.shellshellfish.aaas.userinfo.model.bankcard.BankCard;
import com.shellshellfish.aaas.userinfo.model.invest.InvestProduct;
import com.shellshellfish.aaas.userinfo.model.user.UserInfoAssectsBrief;
import com.shellshellfish.aaas.userinfo.model.user.UserInfoBankCards;
import com.shellshellfish.aaas.userinfo.model.user.UserInfoBase;
import com.shellshellfish.aaas.userinfo.model.user.UserPortfolio;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("userInfoService")
@Transactional
public class UserInfoServiceImpl implements UserInfoService {


    @Override
    public UserInfoBase getUserInfoBase(Long userId) {
        UserInfoBase userInfoBase = new UserInfoBase();
        userInfoBase.setBirthDay(new Date());
        userInfoBase.setCarrier("程序员");
        userInfoBase.setPhoneNumber("123456789123456789");
        userInfoBase.setUserId(userId);
        return userInfoBase;
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
        userInfoBankCards.setUserId(userId);
        List<BankCard> bankCardList = new ArrayList<>();
        List<BankCard> cards = IntStream.rangeClosed(1, 4)
                .boxed()
                .flatMap(value ->
                        IntStream.rangeClosed(1, 13)
                                .mapToObj(suit -> new BankCard(""+value,
                                    "张三", new Date(), "银行"+suit ))
                )
                .collect(Collectors.toList());
        userInfoBankCards.setBankCardList(cards);

        return userInfoBankCards;
    }

    @Override
    public UserPortfolio getUserPortfolio(Long userId) {
        UserPortfolio userPortfolio = new UserPortfolio();
        userPortfolio.setUserId(userId);
        List<InvestProduct> investProducts = IntStream.rangeClosed(1, 4)
            .boxed()
            .flatMap(value ->
                IntStream.rangeClosed(1, 13)
                    .mapToObj(suit -> new InvestProduct(Long.valueOf(""+value),
                        "张三", Float.valueOf("0.1"), Long.valueOf(""+suit) ))
            )
            .collect(Collectors.toList());
        userPortfolio.setInvests(investProducts);
        return userPortfolio;
    }
}
