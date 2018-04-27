package com.shellshellfish.aaas.assetallocation.mapper;

import com.shellshellfish.aaas.assetallocation.entity.FundGroupHistory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface FundGroupHistoryMapper {
    List<FundGroupHistory> findAllByDateBefore(@Param("date") LocalDate date, @Param("groupId") String groupId, @Param("subGroupId") String subGroupId);

    //获取特定日期的复权单位净值（没有就取后一天的数据 比如15号没有数据，那就取16号的数据）
    Double getLatestNavAdj(@Param("groupId") String groupId, @Param("subGroupId") String
        subGroupId, @Param("date") LocalDate date, @Param("oemId") Integer oemId);

    //更新最大回撤
    int updateMaxDrawDown(FundGroupHistory fundGroupHistory);

    //批量更新最大回撤
    int updateMaxDrawDownFromList(@Param("fundGroupHistoryList") List<FundGroupHistory> fundGroupHistoryList, @Param("groupId") String groupId, @Param("subGroupId") String subGroupId);

    //获取最大回撤值
    Double getMaxDrawDown(@Param("groupId") String groupId, @Param("subGroupId") String subGroupId);
}
