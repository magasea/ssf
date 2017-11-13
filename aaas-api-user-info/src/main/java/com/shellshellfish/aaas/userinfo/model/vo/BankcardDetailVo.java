package com.shellshellfish.aaas.userinfo.model.vo;

import java.math.BigInteger;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BankcardDetailVo {

  private String cardNumber;
  private String cardUserName;
  private String cardCellphone;
  private String cardUserPid;
  private BigInteger cardUserId;
  private String bankName;
}
