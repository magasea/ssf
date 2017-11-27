package com.shellshellfish.aaas.userinfo.utils;

import java.math.BigDecimal;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.stream.Collectors;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
  //for lists
  public static <T, U> List<U> convertList(List<T> from, Function<T, U> func) {
    return from.stream().map(func).collect(Collectors.toList());
  }

  //for arrays
  public static <T, U> U[] convertArray(T[] from,
      Function<T, U> func,
      IntFunction<U[]> generator) {
    return Arrays.stream(from).map(func).toArray(generator);
  }

  public static <A, B> List<B> convertList(List<A> sourceList, Class<B> targetClass)
      throws IllegalAccessException, InstantiationException {
    List<B> targetList = new ArrayList<>();
    for(A item: sourceList){
      B targetItem = targetClass.newInstance();
      BeanUtils.copyProperties(item, targetItem);
      targetList.add(targetItem);
    }
    return targetList;
  }

  public static <T> Page<T> convertListToPage(List<T> inputList, PageRequest pageRequest, long
      total){
    final Page<T> page = new PageImpl<>(inputList, pageRequest, total);
    return page;
  }

  public static int getRandomNumberInRange(int min, int max) {

    if (min >= max) {
      throw new IllegalArgumentException("max must be greater than min");
    }

    Random r = new Random();
    return r.nextInt((max - min) + 1) + min;
  }

  public static BigDecimal getRandomDecimalInRange(int min, int max){
    return BigDecimal.valueOf(getRandomNumberInRange(min, max)).movePointLeft(2);
  }

  public static Long getCurrentUTCTime(){
    ZonedDateTime utc = ZonedDateTime.now(ZoneOffset.UTC);
    Date date = Date.from(utc.toInstant());
    return date.getTime();
  }
}
