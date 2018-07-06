package com.shellshellfish.aaas;

import com.shellshellfish.aaas.datacollection.server.DataServerServiceApplication;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.data.mongodb.core.query.Query;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = DataServerServiceApplication.class)
@WebAppConfiguration
public class MongoServiceApplicationTests {

    @Autowired
    MongoTemplate mongoTemplate;

    @Test
    public void findNullContentField() {
        Query query = new Query();
        query.addCriteria(Criteria.where("querydate").gte(1529424000L).orOperator(Criteria.where
            ("000906SH").is(null), Criteria.where("000906SH").is("")));
        List<Map> results =  mongoTemplate.find(query, Map.class, "fundbaseclose");
        for(Map item: results){
            item.forEach(
                (key, value) ->{
                    System.out.println(key+":"+ value);
                }
            );

        }

    }

}

