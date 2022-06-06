package com.kkafara.server.smarthome.kitchen;

import Smarthome.DeviceMetadata;
import Smarthome.ExecutionException;
import Smarthome.Kitchen.IOven;
import com.kkafara.server.smarthome.DeviceControllerImpl;
import com.zeroc.Ice.Current;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class OvenImpl extends DeviceControllerImpl implements IOven {

  private final Logger logger = LogManager.getLogger(OvenImpl.class);
  private float temperature;

  public OvenImpl(DeviceMetadata metadata, float temp) {
    super(metadata);
    temperature = temp;
  }


  @Override
  public void preheat(float temp, Current current) throws ExecutionException {
    logger.info(getTag() + "Preheating oven to " + temp + " degrees");
    if (temp < 30 || temp > 350) {
      logger.error(getTag() + "Invalid temperature provided. Must be >= 30 && <= 350");
      throw new ExecutionException("Invalid temperature provided. Must be >= 30 && <= 350");
    }
    temperature = temp;
  }

  @Override
  public float getCurrentTemp(Current current) {
    return temperature;
  }
}
