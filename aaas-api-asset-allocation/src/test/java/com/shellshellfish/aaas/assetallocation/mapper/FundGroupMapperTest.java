package com.shellshellfish.aaas.assetallocation.mapper;

import com.shellshellfish.aaas.assetallocation.AssetAllocationApp;
import com.shellshellfish.aaas.assetallocation.service.FundGroupIndexService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by pierre
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AssetAllocationApp.class)
@ActiveProfiles("dev")
public class FundGroupMapperTest {
    @Autowired
    FundGroupMapper fundGroupMapper;

    @Test
    public void updateAllSharpeRatio() {
        int oemId = 1;
        List<Map> sharpeRatios = new ArrayList<>(3);

        for (int i = 0; i < 3; i++) {
            Map map = new HashMap(2);
            map.put("id", 1000 + i);
            map.put("sharpeRatio", 1);
            sharpeRatios.add(map);
        }
        int result = fundGroupMapper.updateAllSharpeRatio(sharpeRatios, oemId);
        Assert.assertEquals(sharpeRatios.size(), result);
    }

}