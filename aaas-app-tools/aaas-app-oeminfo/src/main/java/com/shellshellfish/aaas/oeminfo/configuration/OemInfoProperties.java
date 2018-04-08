package com.shellshellfish.aaas.oeminfo.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Map;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

/**
 * Created by chenwei on 2018- 四月 - 04
 */
@Component
@ConfigurationProperties(prefix="oeminfo")
public class OemInfoProperties {
  List<Map<String, String>> oemInfo;

  public List<Map<String, String>> getOemInfo() {
    return oemInfo;
  }

  public void setOemInfo(List<Map<String, String>> oemInfo) {
    this.oemInfo = oemInfo;
  }


}
