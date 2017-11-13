package com.shellshellfish.aaas.userinfo.model.dao.userinfo;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "ui_sysmsg")
public class UiSysMsg {
  private static final long serialVersionUID = 1L;
  @Id
  private String id;

  private String source;

  private String content;


  private String createdBy;


  private Long createdDate;

  private String lastModifiedBy;

  private Long lastModifiedDate;

  private Long date;

  private String title;


  @Override
  public String toString() {
    return "UiSysMsg{" +
        ", content='" + content + '\'' +
        ", source='" + source + '\'' +
        ", createdBy='" + createdBy + '\'' +
        ", createdDate=" + createdDate +
        ", lastModifiedBy='" + lastModifiedBy + '\'' +
        ", lastModifiedDate=" + lastModifiedDate +
        ", date=" + date +
        ", title='" + title + '\'' +
        '}';
  }
}
