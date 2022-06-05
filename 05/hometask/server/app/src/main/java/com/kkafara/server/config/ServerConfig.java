package com.kkafara.server.config;

import com.google.gson.annotations.SerializedName;

import java.util.Collections;
import java.util.List;

public class ServerConfig {
  @SerializedName("name")
  private String name;

  @SerializedName("objects")
  private List<ObjectConfig> objectList;

  public List<ObjectConfig> getObjectList() {
    return Collections.unmodifiableList(objectList);
  }

  public String getName() {
    return name;
  }
}
