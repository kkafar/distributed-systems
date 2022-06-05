package com.kkafara.server.smarthome.airconditioning;

import Smarthome.AirConditioning.IAirConditioner;
import Smarthome.DeviceStatus;
import Smarthome.ExecutionException;
import com.kkafara.server.smarthome.DeviceControllerImpl;
import com.zeroc.Ice.Current;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AirConditionerImpl extends DeviceControllerImpl implements IAirConditioner {
  private final Logger logger = LogManager.getLogger(AirConditionerImpl.class);

  private static final float DEFAULT_TEMP = 21;
  private static final long DEFAULT_ID = -1;

  protected float temperature;

  public AirConditionerImpl() {
    this("Air conditioner", DeviceStatus.Off, DEFAULT_ID, DEFAULT_TEMP);
  }

  public AirConditionerImpl(String description, DeviceStatus status, long id, float temperature) {
    super(description, status, id);
    this.temperature = temperature;
    logger.info("Created new AirConditionerImpl (servant)");
  }

  @Override
  public void setTargetTemp(float temp, Current current) throws ExecutionException {
    logger.info("Setting target temperature to: " + temp + " Celsius degree");
    if (temp < -20 || temp > 40) {
      throw new ExecutionException("Invalid temperature. Must be between -20..40 degrees");
    }
    temperature = temp;
  }
}
