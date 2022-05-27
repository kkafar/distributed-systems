package com.kkafara.server;

public class ChatServerConfig {

  public final int port;

  public final String address;

  private ChatServerConfig(int port, final String address) {
    this.port = port;
    this.address = address;
  }
}
