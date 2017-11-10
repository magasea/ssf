package com.shellshellfish.aaas.userinfo.util;

import java.util.HashMap;
import java.util.Map;
import org.springframework.util.CollectionUtils;

public class UserInfoUtils {

  /**
   * 将2个hushMap 进行合并
   * @param map1
   * @param map2
   * @param key
   * @return
   */
  public static Map<String, Map<String, Object>> mergeMapByKeyForLinks(Map map1, Map map2, String
      key){
    Map<String, Map<String, Object>> map = new HashMap<>();
    if(CollectionUtils.isEmpty(map1)){
      map.put(key, map2);
      return map;
    }
    if(CollectionUtils.isEmpty(map2)){
      map.put(key, map1);
      return map;
    }
    map.merge(key, map1, (m1, m2) -> {m1.putAll(m2);return m1;});
    map.merge(key, map2, (m1, m2) -> {m1.putAll(m2);return m1;});
    return map;
  }

  /**
   * 匹配Luhn算法：可用于检测银行卡卡号
   * @param cardNo
   * @return
   */
  public static boolean matchLuhn(String cardNo) {
    int[] cardNoArr = new int[cardNo.length()];
    for (int i=0; i<cardNo.length(); i++) {
      cardNoArr[i] = Integer.valueOf(String.valueOf(cardNo.charAt(i)));
    }
    for(int i=cardNoArr.length-2;i>=0;i-=2) {
      cardNoArr[i] <<= 1;
      cardNoArr[i] = cardNoArr[i]/10 + cardNoArr[i]%10;
    }
    int sum = 0;
    for(int i=0;i<cardNoArr.length;i++) {
      sum += cardNoArr[i];
    }
    return sum % 10 == 0;
  }


}
