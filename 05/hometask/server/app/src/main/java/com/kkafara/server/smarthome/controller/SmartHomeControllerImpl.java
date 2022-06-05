package com.kkafara.server.smarthome.controller;

import Smarthome.Controller.ISmartHomeController;
import Smarthome.DeviceMetadata;
import com.kkafara.server.smarthome.DeviceImpl;
import com.kkafara.server.smarthome.DeviceRegistry;
import com.zeroc.Ice.Current;

import java.util.List;
import java.util.stream.Collectors;

public class SmartHomeControllerImpl implements ISmartHomeController {
  private final DeviceRegistry registry;

  public SmartHomeControllerImpl(DeviceRegistry registry) {
    this.registry = registry;
  }

  @Override
  public DeviceMetadata[] getDevices(Current current) {
    List<DeviceImpl> devices = registry.getDevices();
    DeviceMetadata[] data = new DeviceMetadata[devices.size()];
    List<DeviceMetadata> devicesMetadata = devices
        .stream()
        .map(device -> device.getMetadata(current)).toList();
    devicesMetadata.toArray(data);
    return data;
  }
}
