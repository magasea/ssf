package com.shellshellfish.aaas;

import com.shellshellfish.aaas.datacollection.server.DataServerServiceApplication;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;



@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = DataServerServiceApplication.class)
@WebAppConfiguration
public class MongoServiceApplicationTests {

    @Test
    public void contextLoads() {
    }

}

