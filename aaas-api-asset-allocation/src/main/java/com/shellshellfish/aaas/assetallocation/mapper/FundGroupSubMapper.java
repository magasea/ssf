package com.shellshellfish.aaas.assetallocation.mapper;

import com.shellshellfish.aaas.assetallocation.entity.FundGroupSub;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FundGroupSubMapper {

    List<FundGroupSub> findByGroupId(@Param("fundGroupId")String fundGroupId,@Param("oemId")Integer oemId);
}
