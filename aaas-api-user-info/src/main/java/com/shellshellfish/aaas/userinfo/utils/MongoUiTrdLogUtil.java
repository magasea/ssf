package com.shellshellfish.aaas.userinfo.utils;

import com.shellshellfish.aaas.userinfo.model.dao.MongoUiTrdLog;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.apache.el.stream.Optional;

/**
 * Created by chenwei on 2018- 二月 - 22
 */

public class MongoUiTrdLogUtil {

  public static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor)
  {
    Map<Object, Boolean> map = new ConcurrentHashMap<>();
    return t -> map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
  }

  public static List<MongoUiTrdLog> getDistinct(List<MongoUiTrdLog> originList){
    // Get distinct only
    List<MongoUiTrdLog> distinctElements = originList.stream().filter(distinctByKey(p -> p
        .getUserProdId()+ p.getFundCode() +p.getOperations()+ p.getTradeStatus() + (p
        .getApplySerial() == null? p.getTradeDate():p.getApplySerial())))
        .collect(Collectors.toList());
    return distinctElements;
  }

}
