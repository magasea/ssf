package com.shellshellfish.aaas.finance.trade.order.repositories.mysql;

import com.shellshellfish.aaas.finance.trade.order.model.dao.TrdTradeBankDic;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by chenwei on 2017- 十二月 - 23
 */
public interface TrdTradeBankDicRepository extends PagingAndSortingRepository<TrdTradeBankDic, Long> {


  TrdTradeBankDic findByBankNameAndTraderBrokerId(String bankName, Long traderBrokerId);



  TrdTradeBankDic findByBankCode(String bankCode);

	TrdTradeBankDic findByBankShortName(String shortName);

}
