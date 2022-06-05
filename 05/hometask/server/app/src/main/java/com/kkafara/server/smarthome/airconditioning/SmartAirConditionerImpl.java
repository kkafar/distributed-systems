package com.kkafara.server.smarthome.airconditioning;

import Smarthome.AirConditioning.ISmartAirConditioner;
import Smarthome.DeviceMetadata;
import Smarthome.ExecutionException;
import Smarthome.Time;
import com.zeroc.Ice.Current;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Set;

public class SmartAirConditionerImpl extends AirConditionerImpl implements ISmartAirConditioner {
  private final Logger logger = LogManager.getLogger(SmartAirConditionerImpl.class);

  private boolean energySavingModeOn;

  private final Set<Integer> zones;

  public SmartAirConditionerImpl(DeviceMetadata metadata, float temp, Set<Integer> zones, boolean energySavingModeOn) {
    super(metadata, temp);
    this.energySavingModeOn = energySavingModeOn;
    this.zones = zones;
  }

  @Override
  public void turnOnEnergySavingMode(Current current) {
    logger.info(getTag() + "Turning energy-saving mode on");
    energySavingModeOn = true;
  }

  @Override
  public void turnOffEnergySavingMode(Current current) {
    logger.info(getTag() + "Turning energy-saving mode off");
    energySavingModeOn = false;
  }

  @Override
  public float getCurrentTemp(Current current) {
    return temperature;
  }

  @Override
  public void setTargetTempForTime(float temp, Time time, Current current) throws ExecutionException {
    logger.info(getTag() + "Setting temperature for time");
    if (time.hour < 0 || time.hour > 23 || time.minutes < 0 || time.minutes > 59) {
      logger.error(getTag() + "Invalid time argument");
      throw new ExecutionException("Invalid time argument");
    }
  }

  @Override
  public void setTargetTempForZone(float temp, int areaId, Current current) throws ExecutionException {
    logger.info(getTag() + "Setting temperature for zone: " + areaId);
    if (!zones.contains(areaId)) {
      logger.error(getTag() + "Invalid zone id provided");
      throw new ExecutionException("Invalid zone id provided");
    }
  }
}
