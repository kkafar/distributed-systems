package com.kkafara.server.util;

import Smarthome.DeviceMetadata;
import Smarthome.DeviceStatus;
import com.kkafara.server.config.ObjectConfig;
import com.kkafara.server.smarthome.DeviceImpl;
import com.kkafara.server.smarthome.airconditioning.AirConditionerImpl;
import com.kkafara.server.smarthome.airconditioning.SmartAirConditionerImpl;
import com.kkafara.server.smarthome.airconditioning.TimedAirConditionerImpl;
import com.kkafara.server.smarthome.airconditioning.ZoneAirConditionerImpl;
import com.kkafara.server.smarthome.heating.WaterHeaterImpl;
import com.kkafara.server.smarthome.kitchen.OvenImpl;
import com.kkafara.server.smarthome.lighting.BlindsManager;
import com.kkafara.server.smarthome.lighting.PartialDrawBlindsManager;

public class ObjectInflater {
  private static long currentId = 0;

  public static DeviceImpl inflate(ObjectConfig objectConfig) {
    DeviceStatus status = objectConfig.getStatus() != null ? objectConfig.getStatus() : DeviceStatus.Off;
    switch (objectConfig.getType()) {
      case AirConditioner -> {
        return new AirConditionerImpl(
            new DeviceMetadata(objectConfig.getName(), status, currentId++),
            objectConfig.getTemperature() != null ? objectConfig.getTemperature() : AirConditionerImpl.DEFAULT_TEMP
        );
      }
      case TimedAirConditioner -> {
        return new TimedAirConditionerImpl(
            new DeviceMetadata(objectConfig.getName(), status, currentId++)
        );
      }
      case ZoneAirConditioner -> {
        return new ZoneAirConditionerImpl(
            new DeviceMetadata(objectConfig.getName(), status, currentId++),
            objectConfig.getTemperature() != null ? objectConfig.getTemperature() : AirConditionerImpl.DEFAULT_TEMP,
            objectConfig.getZones()
        );
      }
      case SmartAirConditioner -> {
        return new SmartAirConditionerImpl(
            new DeviceMetadata(objectConfig.getName(), status, currentId),
            objectConfig.getTemperature() != null ? objectConfig.getTemperature() : AirConditionerImpl.DEFAULT_TEMP,
            objectConfig.getZones(),
            objectConfig.getEnergySaveModeOn()
        );
      }
      case Oven -> {
        return new OvenImpl(
            new DeviceMetadata(objectConfig.getName(), status, currentId++),
            objectConfig.getTemperature() != null ? objectConfig.getTemperature() : AirConditionerImpl.DEFAULT_TEMP
        );
      }
      case WaterHeater -> {
        return new WaterHeaterImpl(
            new DeviceMetadata(objectConfig.getName(), status, currentId++),
            objectConfig.getTemperature() != null ? objectConfig.getTemperature() : AirConditionerImpl.DEFAULT_TEMP
        );
      }
      case BlindsManager -> {
        return new BlindsManager(
            new DeviceMetadata(objectConfig.getName(), status, currentId++),
            objectConfig.getDrawn()
        );
      }
      case PartialDrawBlindsManager -> {
        return new PartialDrawBlindsManager(
            new DeviceMetadata(objectConfig.getName(), status, currentId++),
            objectConfig.getPercent() == null ? 0.0f : objectConfig.getPercent()
        );
      }
      default -> throw new IllegalStateException("Unimplemented object type: " + objectConfig.getType());
    }
  }
}
