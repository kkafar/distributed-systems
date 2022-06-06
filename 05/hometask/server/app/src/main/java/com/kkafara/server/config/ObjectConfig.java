package com.kkafara.server.config;

import Smarthome.DeviceStatus;
import com.google.gson.annotations.SerializedName;

import java.util.Set;

public class ObjectConfig {
  private String name;

  private ObjectType type;

  private Float temperature;

  private Float percent;

  private Set<Integer> zones;

  private boolean drawn;

  private DeviceStatus status;

  @SerializedName("energy-save-mode")
  private boolean energySaveModeOn;

  public ObjectType getType() {
    return type;
  }

  public String getName() {
    return name;
  }

  public Float getTemperature() {
    return temperature;
  }

  public Float getPercent() {
    return percent;
  }

  public Set<Integer> getZones() {
    return zones;
  }

  public boolean getDrawn() {
    return drawn;
  }

  public boolean getEnergySaveModeOn() {
    return energySaveModeOn;
  }

  public DeviceStatus getStatus() {
    return status;
  }
}
