/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package com.kkafara.server;

import Smarthome.DeviceStatus;
import com.kkafara.server.smarthome.airconditioning.AirConditionerImpl;
import com.zeroc.Ice.Communicator;
import com.zeroc.Ice.ObjectAdapter;
import com.zeroc.Ice.Util;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;

public class App {
  private static final Logger logger = LogManager.getLogger(App.class);

  public static void main(String[] args) {
    Configurator.setRootLevel(Level.DEBUG);

    logger.info("Starting application");

    try (Communicator communicator = Util.initialize(args)) {
      // Create adapter
      ObjectAdapter adapter = communicator.createObjectAdapterWithEndpoints("SmartHomeAdapter", "default -p 10000");

      // Create servants
      com.zeroc.Ice.Object object = new AirConditionerImpl("AirConditioner", DeviceStatus.Off, 0, 20);



      // Add servants to adapter
      adapter.add(object, com.zeroc.Ice.Util.stringToIdentity("SmartHome"));

      // Run the adapter
      adapter.activate();

      communicator.waitForShutdown();
    }
  }
}
