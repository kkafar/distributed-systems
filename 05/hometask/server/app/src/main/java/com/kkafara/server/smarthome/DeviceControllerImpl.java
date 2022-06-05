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

  public DeviceControllerImpl(DeviceMetadata metadata) {
    super(metadata);
  }

  @Override
  public boolean turnOn(Current current) {
    if (metadata.status == DeviceStatus.Off) {
      logger.info(getTag() + "Turning on the device");
      metadata.status = DeviceStatus.On;
      return true;
    }
    logger.warn(getTag() + "Attempt to turn on working device");
    return false;
  }

  @Override
  public boolean turnOff(Current current) {
    if (metadata.status == DeviceStatus.On) {
      logger.info(getTag() + "Turning off the device");
      metadata.status = DeviceStatus.Off;
      return true;
    }
    logger.warn(getTag() + "Attempt to turn off not working device");
    return false;
  }

  @Override
  public DeviceStatus getStatus(Current current) {
    logger.info(getTag() + "Returning status");
    return metadata.status;
  }
}
