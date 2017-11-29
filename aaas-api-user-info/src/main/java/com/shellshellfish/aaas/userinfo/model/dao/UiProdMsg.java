package com.shellshellfish.aaas.userinfo.model.dao;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "ui_prodmsg")
public class UiProdMsg {
  @Id
  String id;
  private String content;
  String msgSource;
  String prodId;
  String msgTitle;
  private String createdBy;
  private Long createdDate;
  private String lastModifiedBy;
  private Long lastModifiedDate;
  private Long date;
  private String title;

  @Override
  public String toString() {
    return "UiProdMsg{" +
        "id='" + id + '\'' +
        ", content='" + content + '\'' +
        ", msgSource='" + msgSource + '\'' +
        ", prodId='" + prodId + '\'' +
        ", msgTitle='" + msgTitle + '\'' +
        ", createdBy='" + createdBy + '\'' +
        ", createdDate=" + createdDate +
        ", lastModifiedBy='" + lastModifiedBy + '\'' +
        ", lastModifiedDate=" + lastModifiedDate +
        ", date=" + date +
        ", title='" + title + '\'' +
        '}';
  }
}
