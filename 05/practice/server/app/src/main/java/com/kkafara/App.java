package com.kkafara;

import com.kkafara.server.ChatServer;
import com.kkafara.server.config.ChatServerConfig;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;

import java.io.IOException;

public class App {

  public static void main(String[] args) throws IOException, InterruptedException {
    Configurator.setRootLevel(Level.ALL);

    final Logger logger = LogManager.getLogger(App.class);

    logger.info("Creating server instance");

    ChatServer server = new ChatServer(new ChatServerConfig(
        8088,
        "127.0.0.1"
    ));

    server.start();
  }
}
