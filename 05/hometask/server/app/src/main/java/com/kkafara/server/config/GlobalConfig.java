package com.kkafara.server.config;

import com.google.gson.annotations.SerializedName;

import java.util.Collections;
import java.util.List;

public class GlobalConfig {
  @SerializedName("servers")
  private  List<ServerConfig> serverList;

  public List<ServerConfig> getServerList() {
    return Collections.unmodifiableList(serverList);
  }
}
