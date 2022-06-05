package com.kkafara.server.smarthome.controller;

import Smarthome.Controller.ISmartHomeController;
import Smarthome.DeviceMetadata;
import com.zeroc.Ice.Current;

public class SmartHomeControllerImpl implements ISmartHomeController {
  @Override
  public DeviceMetadata[] getDevices(Current current) {
    return new DeviceMetadata[0];
  }
}
