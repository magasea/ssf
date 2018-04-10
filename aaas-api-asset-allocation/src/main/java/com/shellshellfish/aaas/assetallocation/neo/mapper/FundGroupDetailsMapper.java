package com.shellshellfish.aaas.assetallocation.neo.mapper;

import com.shellshellfish.aaas.assetallocation.neo.entity.FundGroupDetails;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *
 */
public interface FundGroupDetailsMapper {

 List<FundGroupDetails> getFundProportion(@Param("groupId") String groupId, @Param("subGroupId") String subGroupId);


}
