package com.shellshellfish.aaas.datacollection.server.repositories;


import com.shellshellfish.aaas.datacollection.server.model.DailyFunds;
import com.shellshellfish.aaas.datacollection.server.model.FundCodes;
import com.shellshellfish.aaas.datacollection.server.model.FundResources;
import java.util.List;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface FundResourcesRepository extends PagingAndSortingRepository<FundResources, String> {

  List<FundResources> findByCodeIn(List<String> code);

}
