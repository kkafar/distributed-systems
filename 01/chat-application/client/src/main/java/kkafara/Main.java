package kkafara;

import kkafara.client.Client;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
  public static void main(String[] args) throws IOException {
    setupLogging();
    launchClient(args);
  }

  public static void setupLogging() {
    Configurator.setRootLevel(Level.ERROR);
  }

  public static void launchClient(String[] args) throws IOException {
    Logger logger = LogManager.getLogger(Main.class);

    logger.info("Launching client");
    Client client = new Client(getClientName());
    client.run("127.0.0.1", 8080);
  }

  public static String getClientName() throws IOException {
    System.out.println("Please provide username");
    System.out.print("> ");
    return new BufferedReader(new InputStreamReader(System.in)).readLine();
  }
}
