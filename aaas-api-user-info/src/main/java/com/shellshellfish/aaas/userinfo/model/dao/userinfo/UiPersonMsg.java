package com.shellshellfish.aaas.userinfo.model.dao.userinfo;

import java.io.Serializable;
import java.math.BigInteger;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Getter
@Setter
@Document(collection = "ui_usermessage")
public class UiPersonMsg implements Serializable {
  private static final long serialVersionUID = 1L;
  @Id
  String id;

  private String content;

  String msgSource;

  String msgTitle;


  private String createdBy;


  private Long createdDate;


  private String lastModifiedBy;


  private Long lastModifiedDate;


  private Long time;

  private String title;

  private BigInteger userId;

  private Boolean readed;

  @Override
  public String toString() {
    return "UiPersonMsg{" +
        "content='" + content + '\'' +
        ", msgSource='" + msgSource + '\'' +
        ", msgTitle='" + msgTitle + '\'' +
        ", createdBy='" + createdBy + '\'' +
        ", createdDate=" + createdDate +
        ", lastModifiedBy='" + lastModifiedBy + '\'' +
        ", lastModifiedDate=" + lastModifiedDate +
        ", time=" + time +
        ", title='" + title + '\'' +
        ", userId=" + userId +
        ", readed=" + readed +
        '}';
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public String getMsgSource() {
    return msgSource;
  }

  public void setMsgSource(String msgSource) {
    this.msgSource = msgSource;
  }

  public String getMsgTitle() {
    return msgTitle;
  }

  public void setMsgTitle(String msgTitle) {
    this.msgTitle = msgTitle;
  }

  public String getCreatedBy() {
    return createdBy;
  }

  public void setCreatedBy(String createdBy) {
    this.createdBy = createdBy;
  }

  public Long getCreatedDate() {
    return createdDate;
  }

  public void setCreatedDate(Long createdDate) {
    this.createdDate = createdDate;
  }

  public String getLastModifiedBy() {
    return lastModifiedBy;
  }

  public void setLastModifiedBy(String lastModifiedBy) {
    this.lastModifiedBy = lastModifiedBy;
  }

  public Long getLastModifiedDate() {
    return lastModifiedDate;
  }

  public void setLastModifiedDate(Long lastModifiedDate) {
    this.lastModifiedDate = lastModifiedDate;
  }

  public Long getTime() {
    return time;
  }

  public void setTime(Long time) {
    this.time = time;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public BigInteger getUserId() {
    return userId;
  }

  public void setUserId(BigInteger userId) {
    this.userId = userId;
  }

  public Boolean getReaded() {
    return readed;
  }

  public void setReaded(Boolean readed) {
    this.readed = readed;
  }
}


