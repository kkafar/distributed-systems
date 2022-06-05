package com.kkafara.server.smarthome;

import Smarthome.IDevice;
import com.zeroc.Ice.ObjectAdapter;
import com.zeroc.Ice.Util;

import java.util.Arrays;
import java.util.LinkedHashSet;

public class DeviceRegistry {
  private final LinkedHashSet<DeviceImpl> devices = new LinkedHashSet<>();

  public void acceptAdapter(ObjectAdapter adapter) {
    devices.forEach(device -> adapter.add(device, Util.stringToIdentity(device.toString())));
  }

  public void addDevice(DeviceImpl device) {
    devices.add(device);
  }

  public void addDevices(DeviceImpl... devices) {
    Arrays.stream(devices).forEach(this::addDevice);
  }
}
