package kkafara;

import kkafara.server.Server;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;

public class Main {
  public static void main(String[] args) {
    setupLogging();
    launchServer();
  }

  public static void setupLogging() {
    Configurator.setRootLevel(Level.TRACE);
  }

  public static void launchServer() {
    Logger logger = LogManager.getLogger(Main.class);

    logger.info("Starting server");
    Server applicationServer = new Server();
    applicationServer.run();
  }
}
