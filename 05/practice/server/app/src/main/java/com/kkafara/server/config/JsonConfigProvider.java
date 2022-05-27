package com.kkafara.server.config;

import com.google.gson.Gson;

public class JsonConfigProvider implements ConfigProvider {
  private final Gson gson = new Gson();
  @Override
  public ChatServerConfig get() {

  }
}
