package com.kkafara.server.smarthome;

import Smarthome.DeviceMetadata;
import Smarthome.DeviceStatus;
import Smarthome.IDevice;
import com.zeroc.Ice.Current;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class DeviceImpl implements IDevice {
  private final Logger logger = LogManager.getLogger(DeviceImpl.class);

  protected DeviceStatus status;

  protected String description;

  protected long id;

  public DeviceImpl(String description, DeviceStatus status, long id) {
    this.description = description != null ? description
        : "Auto-generated description. This method needs to be overloaded in subclasses.";
    this.status = status != null ? status : DeviceStatus.Unknown;
    this.id = id;
  }

  @Override
  public DeviceMetadata getMetadata(Current current) {
    logger.info("Returning metadata for device with id: " + id);
    return new DeviceMetadata(
        description,
        status,
        id
    );
  }
}
