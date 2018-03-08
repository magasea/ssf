package com.shellshellfish.fundcheck.model.redis;

import java.io.Serializable;

/**
 * Created by chenwei on 2018- 二月 - 02
 */

public class UpdateFundsJobBaseInfoRedis implements Serializable {

  private Long fileUpdateTime;

  public Long getFileUpdateTime() {
    return fileUpdateTime;
  }

  public void setFileUpdateTime(long fileUpdateTime) {
    this.fileUpdateTime = fileUpdateTime;
  }
}

