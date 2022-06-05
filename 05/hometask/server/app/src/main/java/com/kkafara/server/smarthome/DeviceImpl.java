package com.kkafara.server.smarthome;

import Smarthome.DeviceMetadata;
import Smarthome.DeviceStatus;
import Smarthome.IDevice;
import com.zeroc.Ice.Current;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class DeviceImpl implements IDevice {
  private final Logger logger = LogManager.getLogger(DeviceImpl.class);

  protected final DeviceMetadata metadata;

  public DeviceImpl(DeviceMetadata metadata) {
    this.metadata = new DeviceMetadata();
    setMetadata(metadata);
  }

  @Override
  public DeviceMetadata getMetadata(Current current) {
    logger.info(getTag() + "Returning metadata");
    return new DeviceMetadata(
        metadata.description,
        metadata.status,
        metadata.id
    );
  }

  public String toString() {
    return metadata.description;
  }

  protected void setMetadata(DeviceMetadata metadata) {
    setDescription(metadata.description);
    setStatus(metadata.status);
    this.metadata.id = metadata.id;
  }

  protected void setDescription(String description) {
    this.metadata.description = description != null ? description
        : "Device (not specified)";
  }

  protected void setStatus(DeviceStatus status) {
    this.metadata.status = status != null ? status : DeviceStatus.Unknown;
  }

  protected String getTag() {
    return this.metadata.description + ": ";
  }
}
