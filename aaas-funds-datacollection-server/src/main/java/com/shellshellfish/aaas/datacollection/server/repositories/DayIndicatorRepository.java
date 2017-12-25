package com.shellshellfish.aaas.datacollection.server.repositories;


import com.shellshellfish.aaas.datacollection.server.model.DayIndicator;
import com.shellshellfish.aaas.datacollection.server.model.FundResources;
import java.util.List;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface DayIndicatorRepository extends PagingAndSortingRepository<DayIndicator, String> {

  List<DayIndicator> findByCodeIn(List<String> code);

}
