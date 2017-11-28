package com.shellshellfish.aaas.service;

import com.shellshellfish.aaas.MongoServiceApplication;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.IntStream;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = MongoServiceApplication.class)
@WebAppConfiguration
public class DataCollectionServiceImplTest {

  @Autowired
  DataCollectionService dataCollectionService;

  @Test
  public void collectItems() throws Exception {
    List<String> inputFunds = IntStream.rangeClosed(1,1000)
        .mapToObj(value -> "test" + value).collect(Collector.toList());
    dataCollectionService.CollectItems(inputFunds);
  }

}