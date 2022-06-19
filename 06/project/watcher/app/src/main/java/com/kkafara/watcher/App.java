package com.kkafara.watcher;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;

import java.io.IOException;
import java.util.Optional;

public class App {
  private final static Logger logger = LogManager.getLogger(App.class);

  public static void main(String[] args) throws IOException {
    Configurator.setRootLevel(Level.INFO);

    if (args.length != 3) {
      logger.error("Invalid number of arguments");
      return;
    }

    String hostPort = args[0];
    String znode = args[1];
    String programToExecute = args[2];


    logger.info("Host port: " + hostPort);
    logger.info("ZNode: " + znode);
    logger.info("Executable: " + programToExecute);

    try {
      logger.info("Creating executor");
      new Executor(hostPort, znode, programToExecute).run();
    } catch (Exception ex) {
      Optional.ofNullable(ex.getMessage()).ifPresent(logger::error);
      ex.printStackTrace();
    }
  }
}
