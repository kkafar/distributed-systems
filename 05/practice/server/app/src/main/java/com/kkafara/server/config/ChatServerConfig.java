package com.kkafara.server.config;

public class ChatServerConfig {

  public final int port;

  public final String address;

  public ChatServerConfig(int port, final String address) {
    this.port = port;
    this.address = address;
  }
}
