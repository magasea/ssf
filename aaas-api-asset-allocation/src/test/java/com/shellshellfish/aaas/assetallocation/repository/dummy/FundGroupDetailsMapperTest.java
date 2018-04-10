package com.shellshellfish.aaas.assetallocation.repository.dummy;

import com.shellshellfish.aaas.assetallocation.neo.entity.FundGroupDetails;
import com.shellshellfish.aaas.assetallocation.neo.mapper.FundGroupDetailsMapper;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * Author: hongming
 * Date: 2018/1/27
 * Desc:
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ActiveProfiles("dev")
public class FundGroupDetailsMapperTest {

    @Autowired
    private FundGroupDetailsMapper fundGroupDetailsMapper;


    @Test
    public void getFundGroupHistoryTimeTest() {
        String groupId = "5";
        String subGroupId = "50059";
        List<FundGroupDetails> result = fundGroupDetailsMapper.getFundProportion(groupId,subGroupId);
        Assert.assertNotNull("结果为空",result);
        Assert.assertTrue("结果集大小为零",result.size()>0);
    }


}
