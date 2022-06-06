package com.kkafara.server.smarthome.heating;

import Smarthome.DeviceMetadata;
import Smarthome.ExecutionException;
import Smarthome.Heating.IWaterHeater;
import Smarthome.Time;
import com.kkafara.server.smarthome.DeviceControllerImpl;
import com.zeroc.Ice.Current;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

public class WaterHeaterImpl extends DeviceControllerImpl implements IWaterHeater {
  private final Logger logger = LogManager.getLogger(WaterHeaterImpl.class);

  private float temperature;

  public WaterHeaterImpl(DeviceMetadata metadata, float temp) {
    super(metadata);
    temperature = temp;
  }

  @Override
  public void setWaterTempForTime(float temp, Optional<Time> time, Current current) throws ExecutionException {
    logger.info(getTag() + "Setting water temperature to: " + temp);
    if (temp > 90) {
      logger.error("Water temperature must be below 91 degrees");
      throw new ExecutionException("Water temperature must be below 91 degrees");
    }
    temperature = temp;
  }

  @Override
  public float getCurrentTemp(Current current) {
    return temperature;
  }
}
