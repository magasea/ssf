package com.shellshellfish.aaas.common.grpc.zzapi;

import com.google.gson.annotations.SerializedName;

/**
 * Created by chenwei on 2018- 五月 - 23
 */

/**
 * {
 * "results": {
 * "status": 1,
 * "code": "0000",
 * "message": "成功",
 * "content": {
 * "callingcode": "024",
 * "fixflag": "43",
 * "sell": {
 * "fundcode": "001987",
 * "applyserial": "20171018000037",
 * "outsideorderno": "test_selltobuy_000008",
 * "applydate": "20171018",
 * "confirmdate": "2017-10-19",
 * "applyshare": "1",
 * "platform_code": "tg"
 * },
 * "buy": {
 * "fundcode": "040003",
 * "applyserial": "20171018000040",
 * "outsideorderno": "test_selltobuy_000008",
 * "applydate": "20171018",
 * "confirmdate": "2017-10-19",
 * "applysum": "1.00",
 * "platform_code": "tg"
 * }
 * }
 * }
 * }
 */

public class ZZWltSellAndBuyResult {
  @SerializedName("status")
  String status;
  @SerializedName("errno")
  String errno;
  @SerializedName("msg")
  String msg;
  @SerializedName("data")
//  @JsonAdapter(value = ZZWltSellAndBuyRltContentDeserializer.class)
  Content content;

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getErrno() {
    return errno;
  }

  public void setErrno(String errno) {
    this.errno = errno;
  }

  public String getMsg() {
    return msg;
  }

  public void setMsg(String msg) {
    this.msg = msg;
  }

  public Content getContent() {
    return content;
  }

  public void setContent(Content content) {
    this.content = content;
  }

  public class Content{
    @SerializedName("callingcode")
    String callingCode;
    @SerializedName("fixflag")
    String fixFlag;
    @SerializedName("sell")
    Sell sell;
    @SerializedName("buy")
    Buy buy;

    public String getCallingCode() {
      return callingCode;
    }

    public void setCallingCode(String callingCode) {
      this.callingCode = callingCode;
    }

    public String getFixFlag() {
      return fixFlag;
    }

    public void setFixFlag(String fixFlag) {
      this.fixFlag = fixFlag;
    }

    public Sell getSell() {
      return sell;
    }

    public void setSell(Sell sell) {
      this.sell = sell;
    }

    public Buy getBuy() {
      return buy;
    }

    public void setBuy(Buy buy) {
      this.buy = buy;
    }







  }



  public class Sell{
    @SerializedName("fundcode")
    String fundCode;
    @SerializedName("applyserial")
    String applySerial;
    @SerializedName("outsideorderno")
    String outsideOrderNo;
    @SerializedName("applydate")
    String applyDate;
    @SerializedName("confirmdate")
    String confirmDate;
    @SerializedName("applyshare")
    String applyShare;
    @SerializedName("platform_code")
    String platformCode;

    public String getFundCode() {
      return fundCode;
    }

    public void setFundCode(String fundCode) {
      this.fundCode = fundCode;
    }

    public String getApplySerial() {
      return applySerial;
    }

    public void setApplySerial(String applySerial) {
      this.applySerial = applySerial;
    }

    public String getOutsideOrderNo() {
      return outsideOrderNo;
    }

    public void setOutsideOrderNo(String outsideOrderNo) {
      this.outsideOrderNo = outsideOrderNo;
    }

    public String getApplyDate() {
      return applyDate;
    }

    public void setApplyDate(String applyDate) {
      this.applyDate = applyDate;
    }

    public String getConfirmDate() {
      return confirmDate;
    }

    public void setConfirmDate(String confirmDate) {
      this.confirmDate = confirmDate;
    }

    public String getApplyShare() {
      return applyShare;
    }

    public void setApplyShare(String applyShare) {
      this.applyShare = applyShare;
    }

    public String getPlatformCode() {
      return platformCode;
    }

    public void setPlatformCode(String platformCode) {
      this.platformCode = platformCode;
    }
  }

  public class Buy{
    @SerializedName("fundcode")
    String fundCode;
    @SerializedName("applyserial")
    String applySerial;
    @SerializedName("outsideorderno")
    String outsideOrderNo;
    @SerializedName("applydate")
    String applyDate;
    @SerializedName("confirmdate")
    String confirmDate;
    @SerializedName("applysum")
    String applySum;
    @SerializedName("platform_code")
    String platformCode;

    public String getFundCode() {
      return fundCode;
    }

    public void setFundCode(String fundCode) {
      this.fundCode = fundCode;
    }

    public String getApplySerial() {
      return applySerial;
    }

    public void setApplySerial(String applySerial) {
      this.applySerial = applySerial;
    }

    public String getOutsideOrderNo() {
      return outsideOrderNo;
    }

    public void setOutsideOrderNo(String outsideOrderNo) {
      this.outsideOrderNo = outsideOrderNo;
    }

    public String getApplyDate() {
      return applyDate;
    }

    public void setApplyDate(String applyDate) {
      this.applyDate = applyDate;
    }

    public String getConfirmDate() {
      return confirmDate;
    }

    public void setConfirmDate(String confirmDate) {
      this.confirmDate = confirmDate;
    }

    public String getApplySum() {
      return applySum;
    }

    public void setApplySum(String applySum) {
      this.applySum = applySum;
    }

    public String getPlatformCode() {
      return platformCode;
    }

    public void setPlatformCode(String platformCode) {
      this.platformCode = platformCode;
    }
  }
}