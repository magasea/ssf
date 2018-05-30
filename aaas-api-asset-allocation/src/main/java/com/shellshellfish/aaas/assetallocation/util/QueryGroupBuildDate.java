package com.shellshellfish.aaas.assetallocation.util;

import com.shellshellfish.aaas.assetallocation.mapper.FundNetValMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

/**
 * 获取组合的成立日
 * （ （获取组合中每个基金的最小净值日期） 中最大的日期作为组合的成立日 ）
 * Author: Derek
 * Date: 2018/5/22
 * Desc:
 */
@Component
public class QueryGroupBuildDate {

    @Autowired
    public FundNetValMapper fundNetValMapper;


    //史上最安全的单例
    private static class SingletonHolder{
        private static QueryGroupBuildDate instance = new QueryGroupBuildDate();
    }

    public static QueryGroupBuildDate getInstance(){
        return SingletonHolder.instance;
    }

    public LocalDate getGroupBuildDate(String groupId, Integer oemId) {
        Date date = fundNetValMapper.getMinNavlatestDateByFundGroupId(groupId, oemId);
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }
}
