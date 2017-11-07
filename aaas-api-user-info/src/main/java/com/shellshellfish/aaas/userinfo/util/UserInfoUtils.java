package com.shellshellfish.aaas.userinfo.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.util.CollectionUtils;

public class UserInfoUtils {

  public static Map<String, Map<String, Object>> mergeMapByKeyForLinks(Map map1, Map map2, String
      key){
    Map<String, Map<String, Object>> map = new HashMap<>();
    if(CollectionUtils.isEmpty(map1)){
      return map.put(key, map2);
    }
    if(CollectionUtils.isEmpty(map2)){
      return map.put(key, map1);
    }
    map.merge(key, map1, (m1, m2) -> {m1.putAll(m2);return m1;});
    map.merge(key, map2, (m1, m2) -> {m1.putAll(m2);return m1;});
    return map;
  }



}
