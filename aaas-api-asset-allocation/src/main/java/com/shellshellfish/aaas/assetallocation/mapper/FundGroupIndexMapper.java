package com.shellshellfish.aaas.assetallocation.mapper;

import com.shellshellfish.aaas.assetallocation.entity.FundGroupIndex;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FundGroupIndexMapper {

    int saveOrUpdate(FundGroupIndex fundGroupIndex);

    List<FundGroupIndex> findAll(@Param("oemId")Integer oemId);

    FundGroupIndex findByGroupIdAndSubGroupId(@Param("groupId") String groupId, @Param("subGroupId") String
            subGroupId, @Param("oemId") int oemId);
}
