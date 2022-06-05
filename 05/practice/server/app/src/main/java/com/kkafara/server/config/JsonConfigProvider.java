package com.kkafara.server.config;

import com.google.gson.Gson;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class JsonConfigProvider implements ConfigProvider {
  private final Gson gson = new Gson();

  private Path path;

  public JsonConfigProvider(Path configPath) {
    this.path = configPath;
  }

  @Override
  public ChatServerConfig get() {
    ChatServerConfig config = gson.fromJson(Files.newBufferedReader(path, StandardCharsets.UTF_8), )
  }
}
