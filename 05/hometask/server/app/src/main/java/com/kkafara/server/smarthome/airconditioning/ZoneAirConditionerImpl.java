package com.kkafara.server.smarthome.airconditioning;

import Smarthome.AirConditioning.IZoneAirConditioner;
import Smarthome.DeviceMetadata;
import Smarthome.DeviceStatus;
import Smarthome.ExecutionException;
import com.zeroc.Ice.Current;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Set;

public class ZoneAirConditionerImpl extends AirConditionerImpl implements IZoneAirConditioner {
  private final Logger logger = LogManager.getLogger(ZoneAirConditionerImpl.class);

  protected final Set<Integer> zones;

  public ZoneAirConditionerImpl(
      DeviceMetadata metadata,
      float temp,
      Set<Integer> zones
  ) {
    super(metadata, 20);
    this.zones = zones;
  }

  @Override
  public void setTargetTempForZone(float temp, int areaId, Current current) throws ExecutionException {
    logger.info(getTag() + "Setting temperature for zone: " + areaId);
    if (!zones.contains(areaId)) {
      logger.error(getTag() + "Invalid zone id");
      throw new ExecutionException("Invalid zone id");
    }
    super.setTargetTemp(temp, current);
  }
}
