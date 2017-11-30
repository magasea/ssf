package com.shellshellfish.aaas.datacollection.server.repositories;


import com.shellshellfish.aaas.datacollection.server.model.DailyFunds;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface DailyFundsRepository extends PagingAndSortingRepository<DailyFunds, String> {

  List<DailyFunds> findByNavLatestDateAndCodeIsIn(String navLastestDate, List<String> code);

  List<DailyFunds> findByNavLatestDateBetweenAndCodeIsIn(Long navLastestDateLow, Long
      navLastestDateHigh,  List<String> code);
}
