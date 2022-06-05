package com.kkafara.server.smarthome;

import Smarthome.IDevice;
import com.zeroc.Ice.ObjectAdapter;
import com.zeroc.Ice.Util;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;

public class DeviceRegistry {

  private final Logger logger = LogManager.getLogger(DeviceRegistry.class);

  private final LinkedHashSet<DeviceImpl> devices = new LinkedHashSet<>();

  public void acceptAdapter(ObjectAdapter adapter) {
    devices.forEach(device -> adapter.add(device, Util.stringToIdentity(device.toString())));
  }

  public void addDevice(DeviceImpl device) {
    logger.info("Adding device " + device + " to adapter");
    devices.add(device);
  }

  public void addDevices(DeviceImpl... devices) {
    Arrays.stream(devices).forEach(this::addDevice);
  }

  public void addDevices(Iterable<DeviceImpl> devices) {
    devices.forEach(this::addDevice);
  }

  public List<DeviceImpl> getDevices() {
    return Collections.unmodifiableList(devices.stream().toList());
  }
}
