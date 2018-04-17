package com.shellshellfish.aaas.assetallocation.mapper;

import com.shellshellfish.aaas.assetallocation.entity.FundGroupDetails;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *
 */
public interface FundGroupDetailsMapper {

 List<FundGroupDetails> getFundProportion(@Param("groupId") String groupId, @Param("subGroupId")
     String subGroupId, @Param("oemId") Integer oemId);


}
