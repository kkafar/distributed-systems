package com.kkafara.server.util;

import Smarthome.DeviceMetadata;
import Smarthome.DeviceStatus;
import com.kkafara.server.config.ObjectConfig;
import com.kkafara.server.smarthome.DeviceImpl;
import com.kkafara.server.smarthome.airconditioning.AirConditionerImpl;
import com.kkafara.server.smarthome.airconditioning.TimedAirConditionerImpl;
import com.kkafara.server.smarthome.airconditioning.ZoneAirConditionerImpl;

public class ObjectInflater {
  private static long currentId = 0;

  public static DeviceImpl inflate(ObjectConfig objectConfig) {
    switch (objectConfig.getType()) {
      case AirConditioner -> {
        return new AirConditionerImpl(
            new DeviceMetadata(objectConfig.getName(), DeviceStatus.Off, currentId++),
            objectConfig.getTemperature() != null ? objectConfig.getTemperature() : AirConditionerImpl.DEFAULT_TEMP
        );
      }
      case TimedAirConditioner -> {
        return new TimedAirConditionerImpl(
            new DeviceMetadata(objectConfig.getName(), DeviceStatus.Off, currentId++)
        );
      }
      case ZoneAirConditioner -> {
        return new ZoneAirConditionerImpl(
            new DeviceMetadata(objectConfig.getName(), DeviceStatus.Off, currentId++),
            objectConfig.getTemperature() != null ? objectConfig.getTemperature() : AirConditionerImpl.DEFAULT_TEMP,
            objectConfig.getZones()
        );
      }
      default -> throw new IllegalStateException("Unimplemented object type: " + objectConfig.getType());
    }
  }
}
