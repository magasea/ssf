package com.shellshellfish.aaas.finance.trade.repositories;


import com.shellshellfish.aaas.finance.trade.model.DailyFunds;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface DailyFundsRepository extends PagingAndSortingRepository<DailyFunds, Long> {

  @Query(value = "{ 'navlatestdate' : '$0' , 'code' : { '$in' : [ '$1']}}")
  List<DailyFunds> findByNavLatestDateAndCodeIsIn(Long navLastestDate, List<String> code);


  List<DailyFunds> findByNavLatestDateBetweenAndCodeIsIn(Long navLastestDateLow, Long
      navLastestDateHigh,  List<String> code);
}
