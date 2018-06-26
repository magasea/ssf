package com.shellshellfish.aaas.common.grpc.zzapi;

import com.google.gson.annotations.SerializedName;

/**
 * Created by chenwei on 2018- 五月 - 23
 */

public class ZZWltSellAndBuyResult {
  @SerializedName("result")
  ZZWltSellAndBuyRltContent result;

  public ZZWltSellAndBuyRltContent getResult() {
    return result;
  }

  public void setResult(ZZWltSellAndBuyRltContent result) {
    this.result = result;
  }
}
