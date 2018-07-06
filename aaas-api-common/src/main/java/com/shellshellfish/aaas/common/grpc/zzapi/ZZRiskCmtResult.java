package com.shellshellfish.aaas.common.grpc.zzapi;

import com.google.gson.annotations.SerializedName;

/**
 * Created by chenwei on 2018- 五月 - 23
 */

public class ZZRiskCmtResult {
  @SerializedName("riskability")
  Integer riskAbility;
  @SerializedName("questionpoint")
  String questionPoint;
  @SerializedName("riskId")
  String riskId;
  @SerializedName("qnoandanswer")
  String qnoAndAnswer;

  public Integer getRiskAbility() {
    return riskAbility;
  }

  public void setRiskAbility(Integer riskAbility) {
    this.riskAbility = riskAbility;
  }

  public String getQuestionPoint() {
    return questionPoint;
  }

  public void setQuestionPoint(String questionPoint) {
    this.questionPoint = questionPoint;
  }

  public String getRiskId() {
    return riskId;
  }

  public void setRiskId(String riskId) {
    this.riskId = riskId;
  }

  public String getQnoAndAnswer() {
    return qnoAndAnswer;
  }

  public void setQnoAndAnswer(String qnoAndAnswer) {
    this.qnoAndAnswer = qnoAndAnswer;
  }
}
