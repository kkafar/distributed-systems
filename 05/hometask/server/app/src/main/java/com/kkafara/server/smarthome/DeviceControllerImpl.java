package com.kkafara.server.smarthome;

import Smarthome.DeviceMetadata;
import Smarthome.DeviceStatus;
import Smarthome.IDevice;
import Smarthome.IDeviceStatusController;
import com.zeroc.Ice.Current;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class DeviceControllerImpl extends DeviceImpl implements IDeviceStatusController {
  private final Logger logger = LogManager.getLogger(DeviceControllerImpl.class);

  public DeviceControllerImpl(String description, DeviceStatus status, long id) {
    super(description, status, id);
  }

  @Override
  public boolean turnOn(Current current) {
    if (status == DeviceStatus.Off) {
      logger.info("Turning on the device with id: " + id);
      status = DeviceStatus.On;
      return true;
    }
    logger.warn("Attempt to turn on working device with id: " + id);
    return false;
  }

  @Override
  public boolean turnOff(Current current) {
    if (status == DeviceStatus.On) {
      logger.info("Turning off the device with id: " + id);
      status = DeviceStatus.Off;
      return true;
    }
    logger.warn("Attempt to turn off not working device with id: " + id);
    return false;
  }

  @Override
  public DeviceStatus getStatus(Current current) {
    logger.info("Returning status for device with id: " + id);
    return status;
  }
}
