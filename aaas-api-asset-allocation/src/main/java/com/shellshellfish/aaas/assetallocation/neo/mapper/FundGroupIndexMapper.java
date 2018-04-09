package com.shellshellfish.aaas.assetallocation.neo.mapper;

import com.shellshellfish.aaas.assetallocation.neo.entity.FundGroupIndex;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface FundGroupIndexMapper {

    int saveOrUpdate(FundGroupIndex fundGroupIndex);

    FundGroupIndex findByGroupIdAndSubGroupId(@Param("groupId") String groupId, @Param("subGroupId") String subGroupId);
}
