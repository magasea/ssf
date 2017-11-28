package com.shellshellfish.aaas.userinfo.converter;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.shellshellfish.aaas.userinfo.model.dao.UiAssetDailyRept;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class UiAssetDailyReptWriterConverter implements Converter<UiAssetDailyRept, DBObject> {

    @Override
    public DBObject convert(final UiAssetDailyRept uiAssetDailyRept) {
        final DBObject dbObject = new BasicDBObject();
        dbObject.put("UserId", uiAssetDailyRept.getUserId());
        dbObject.put("Date", uiAssetDailyRept.getDate());
        dbObject.put("DailyProfit", uiAssetDailyRept.getDailyProfit());
        dbObject.put("AccumulateProfitRate", uiAssetDailyRept.getAccumulateProfitRate());
        dbObject.put("AccumulateProfit", uiAssetDailyRept.getAccumulateProfit());
        dbObject.put("CurrentProducts", uiAssetDailyRept.getCurrentProducts());
        dbObject.removeField("_class");
        return dbObject;
    }

}
