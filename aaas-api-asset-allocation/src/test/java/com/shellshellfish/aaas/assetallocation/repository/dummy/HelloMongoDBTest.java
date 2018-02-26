package com.shellshellfish.aaas.assetallocation.repository.dummy;

import com.alibaba.fastjson.JSON;
import com.mongodb.*;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.shellshellfish.aaas.assetallocation.neo.returnType.ReturnType;
import com.shellshellfish.aaas.assetallocation.neo.service.FundGroupService;
import com.shellshellfish.aaas.assetallocation.neo.util.ConstantUtil;
import org.bson.Document;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.shellshellfish.aaas.assetallocation.neo.util.ConstantUtil.*;
import static com.shellshellfish.aaas.assetallocation.neo.util.ConstantUtil.MONGO_DB_COLLECTION;

/**
 * Author: hongming
 * Date: 2018/2/23
 * Desc:
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ActiveProfiles("dev")
public class HelloMongoDBTest {

    @Autowired
    private FundGroupService fundGroupService;

    @Test
    public void fundGroupServiceTest() {

        try {
            // 连接到 mongodb 服务
            MongoClient mongoClient = new MongoClient(MONGO_DB_HOST, MONGO_DB_PORT);
            // 连接到数据库
            MongoDatabase mongoDatabase = mongoClient.getDatabase(MONGO_DB_DATABASE_NAME);
            MongoCollection<Document> collection = mongoDatabase.getCollection(MONGO_DB_COLLECTION);

            List<Document> documents = new ArrayList<>();

            //插入文档
            /**
             * 1. 创建文档 org.bson.Document 参数为key-value的格式
             * 2. 创建文档集合List<Document>
             * 3. 将文档集合插入数据库集合中 mongoCollection.insertMany(List<Document>) 插入单个文档可以用 mongoCollection.insertOne(Document)
             * */

            String returnType = "income";
            String subfix = SUB_GROUP_ID_SUBFIX;
            for (int index = 1; index <= ConstantUtil.FUND_GROUP_COUNT; index++) {
                String groupId = String.valueOf(index);
                String subGroupId = String.valueOf(index + subfix);
                String key = groupId + "_" + subGroupId;
                ReturnType rt = fundGroupService.getFundGroupIncomeAll(groupId, subGroupId, returnType);

                String _total = JSON.toJSONString(rt.get_total());
                String _items = JSON.toJSONString(rt.get_items());
                String name = JSON.toJSONString(rt.getName());
                String _links = JSON.toJSONString(rt.get_links());
                String maxMinMap = JSON.toJSONString(rt.getMaxMinMap());
                String maxMinBenchmarkMap = JSON.toJSONString(rt.getMaxMinBenchmarkMap());
                String expectedIncomeSizeMap = JSON.toJSONString(rt.getExpectedIncomeSizeMap());
                String highPercentMaxIncomeSizeMap = JSON.toJSONString(rt.getHighPercentMaxIncomeSizeMap());
                String highPercentMinIncomeSizeMap = JSON.toJSONString(rt.getHighPercentMinIncomeSizeMap());
                String lowPercentMaxIncomeSizeMap = JSON.toJSONString(rt.getLowPercentMaxIncomeSizeMap());
                String lowPercentMinIncomeSizeMap = JSON.toJSONString(rt.getLowPercentMinIncomeSizeMap());
                String _schemaVersion = JSON.toJSONString(rt.get_schemaVersion());
                String _serviceId = JSON.toJSONString(rt.get_serviceId());

                Document document = new Document("title", MONGO_DB_COLLECTION).
                        append("key", key).
                        append("_total", _total).
                        append("_items", _items).
                        append("name", name).
                        append("_links", _links).
                        append("maxMinMap", maxMinMap).
                        append("maxMinBenchmarkMap", maxMinBenchmarkMap).
                        append("expectedIncomeSizeMap", expectedIncomeSizeMap).
                        append("highPercentMaxIncomeSizeMap", highPercentMaxIncomeSizeMap).
                        append("highPercentMinIncomeSizeMap", highPercentMinIncomeSizeMap).
                        append("lowPercentMaxIncomeSizeMap", lowPercentMaxIncomeSizeMap).
                        append("lowPercentMinIncomeSizeMap", lowPercentMinIncomeSizeMap).
                        append("_schemaVersion", _schemaVersion).
                        append("_serviceId", _serviceId);

                documents.add(document);
            }
            //删除所有符合条件的文档
            collection.deleteMany (Filters.eq("title", MONGO_DB_COLLECTION));

            collection.insertMany(documents);

        } catch (Exception e) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }

        System.out.println("---- over ----");
    }

    @Test
    public void queryOneTest() {
        // 连接到 mongodb 服务
        MongoClient mongoClient = new MongoClient(MONGO_DB_HOST, MONGO_DB_PORT);
        // 连接到数据库
        MongoDatabase mongoDatabase = mongoClient.getDatabase(MONGO_DB_DATABASE_NAME);
        MongoCollection<Document> collection = mongoDatabase.getCollection(MONGO_DB_COLLECTION);

        String subfix = SUB_GROUP_ID_SUBFIX;
        for (int index = 1; index <= ConstantUtil.FUND_GROUP_COUNT; index++) {
            String groupId = String.valueOf(index);
            String subGroupId = String.valueOf(index + subfix);
            String key = groupId + "_" + subGroupId;

            FindIterable<Document> findIterable = collection.find(Filters.eq("key", key));
            MongoCursor<Document> mongoCursor = findIterable.limit(1).iterator();
            while (mongoCursor.hasNext()) {
                Document doc = mongoCursor.next();

                String _total = doc.getString("_total");
                String _items = doc.getString("_items");
                String name = doc.getString("name");
                String _links = doc.getString("_links");
                String maxMinMap = doc.getString("maxMinMap");
                String maxMinBenchmarkMap = doc.getString("maxMinBenchmarkMap");
                String expectedIncomeSizeMap = doc.getString("expectedIncomeSizeMap");
                String highPercentMaxIncomeSizeMap = doc.getString("highPercentMaxIncomeSizeMap");
                String highPercentMinIncomeSizeMap = doc.getString("highPercentMinIncomeSizeMap");
                String lowPercentMaxIncomeSizeMap = doc.getString("lowPercentMaxIncomeSizeMap");
                String lowPercentMinIncomeSizeMap = doc.getString("lowPercentMinIncomeSizeMap");
                String _schemaVersion = doc.getString("_schemaVersion");
                String _serviceId = doc.getString("_serviceId");

                ReturnType rt = new ReturnType();
                rt.set_total(Integer.valueOf(_total).intValue());
                rt.set_items(JSON.parseObject(_items, List.class));
                rt.setName(name);
                rt.set_links(JSON.parseObject(_links, Map.class));
                rt.setMaxMinMap(JSON.parseObject(maxMinMap, Map.class));
                rt.setMaxMinBenchmarkMap(JSON.parseObject(maxMinBenchmarkMap, Map.class));
                rt.setExpectedIncomeSizeMap(JSON.parseObject(expectedIncomeSizeMap, Map.class));
                rt.setHighPercentMaxIncomeSizeMap(JSON.parseObject(highPercentMaxIncomeSizeMap, Map.class));
                rt.setHighPercentMinIncomeSizeMap(JSON.parseObject(highPercentMinIncomeSizeMap, Map.class));
                rt.setLowPercentMaxIncomeSizeMap(JSON.parseObject(lowPercentMaxIncomeSizeMap, Map.class));
                rt.setLowPercentMinIncomeSizeMap(JSON.parseObject(lowPercentMinIncomeSizeMap, Map.class));
                rt.set_schemaVersion(_schemaVersion);
                rt.set_serviceId(_serviceId);

                System.out.println(doc);
            }
        }

    }

    @Test
    public void queryAllTest() {
        // 连接到 mongodb 服务
        MongoClient mongoClient = new MongoClient(MONGO_DB_HOST, MONGO_DB_PORT);
        // 连接到数据库
        MongoDatabase mongoDatabase = mongoClient.getDatabase(MONGO_DB_DATABASE_NAME);
        MongoCollection<Document> collection = mongoDatabase.getCollection(MONGO_DB_COLLECTION);

        // 检索查看结果
        showDocuments(collection);
    }

    private static void showDocuments(MongoCollection<Document> collection) {
        /**
         * 1. 获取迭代器FindIterable<Document> 2. 获取游标MongoCursor<Document> 3.
         * 通过游标遍历检索出的文档集合
         * */
        FindIterable<Document> findIterable = collection.find();
        MongoCursor<Document> mongoCursor = findIterable.iterator();
        while (mongoCursor.hasNext()) {
            System.out.println(mongoCursor.next());
        }
    }

}
