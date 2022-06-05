package com.kkafara.server.config;

import com.google.gson.Gson;
import com.kkafara.rt.Result;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class ConfigLoader {
  public static Result<ServerConfig, String> load(Path configPath, String serverName) {
    Logger logger = LogManager.getLogger(ConfigLoader.class);
    Gson gson = new Gson();

    assert serverName != null : "Server name must not be null";

    if (!Files.isReadable(configPath)) {
      return Result.err(("Provided path does not point to readable file"));
    }

    GlobalConfig globalConfig;

    try {
      globalConfig = gson.fromJson(
          Files.newBufferedReader(configPath, StandardCharsets.UTF_8), GlobalConfig.class);
    } catch (IOException ex) {
      return Result.err(("Failed to read config file"));
    }

    assert globalConfig != null;

    for (ServerConfig serverConfig : globalConfig.getServerList()) {
      if (serverName.equals(serverConfig.getName())) {
        for (ObjectConfig objectConfig : serverConfig.getObjectList()) {

        }
        return Result.ok(serverConfig);
      }
    }

    return Result.err("No config specified for server with name: " + serverName);
  }
}
