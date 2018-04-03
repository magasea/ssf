package com.shellshellfish.aaas.assetallocation.neo.mapper;

import com.shellshellfish.aaas.assetallocation.neo.entity.FundGroupHistory;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

public interface FundGroupHistoryMapper {
    List<FundGroupHistory> findAllByDateBefore(@Param("date") LocalDate date, @Param("groupId") String groupId, @Param("subGroupId") String subGroupId);

    int updateMaxDrawDown(FundGroupHistory fundGroupHistory);

    int updateMaxDrawDownFromList(@Param("fundGroupHistoryList") List<FundGroupHistory> fundGroupHistoryList, @Param("groupId") String groupId, @Param("subGroupId") String subGroupId);

}
