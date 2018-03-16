package com.shellshellfish.aaas.tools.fundcheck.utils;

import com.shellshellfish.aaas.tools.fundcheck.model.CSVBaseInfo;
import com.shellshellfish.aaas.tools.fundcheck.model.CSVFundInfo;
import java.io.UnsupportedEncodingException;
import org.springframework.util.StringUtils;

/**
 * Created by chenwei on 2018- 三月 - 09
 */

public class CSVParser {
  public static CSVFundInfo getCSVFundInfo(String line){
    System.out.println(line);
    try {
      String decodedString=new String(line.getBytes(),"UTF-8");
      System.out.println(decodedString);
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
    if(line.contains("DATE") || line.contains("CODE") || line.startsWith("\n") || line.startsWith
        (",")){
      return null;
    }else{
      String[] items = line.split(",");
      if(items.length < 3){
        System.out.println(line);
        return null;
      }
      if(StringUtils.isEmpty(items[2])||StringUtils.isEmpty(items[3])||StringUtils.isEmpty
          (items[4])){
        System.out.println(line);
        return null;
      }
      return new CSVFundInfo(items[0], items[1], items[2], items[3], items[4]);

    }
  }

  public static CSVBaseInfo getCSVBaseInfo(String line){

    if(line.contains("DATE") || line.contains("CODE") || line.startsWith("\n") || line.startsWith
        (",")){
      return null;
    }else{
      String[] items = line.split(",");
      if(items.length < 3){
        System.out.println(line);
        return null;
      }
      if(StringUtils.isEmpty(items[2])){
        System.out.println(line);
        return null;
      }
      return new CSVBaseInfo(items[0], items[1], items[2]);

    }
  }

}
