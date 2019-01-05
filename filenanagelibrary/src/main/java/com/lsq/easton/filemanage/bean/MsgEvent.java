package com.lsq.easton.filemanage.bean;
public class MsgEvent {
  private String jsonArray;
  public MsgEvent(String jsonArray) {
    this.jsonArray = jsonArray;
  }

  public String getJsonArray() {
    return jsonArray;
  }

  public void setJsonArray(String jsonArray) {
    this.jsonArray = jsonArray;
  }
}
