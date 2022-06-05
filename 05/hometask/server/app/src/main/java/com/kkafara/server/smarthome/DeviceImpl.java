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
        metadata.name,
        metadata.status,
        metadata.id
    );
  }

  public String toString() {
    return metadata.name;
  }

  protected void setMetadata(DeviceMetadata metadata) {
    setName(metadata.name);
    setStatus(metadata.status);
    this.metadata.id = metadata.id;
  }

  protected void setName(String name) {
    this.metadata.name = name != null ? name
        : "Device (not specified)";
  }

  protected void setStatus(DeviceStatus status) {
    this.metadata.status = status != null ? status : DeviceStatus.Unknown;
  }

  protected String getTag() {
    return this.metadata.name + ": ";
  }
}
