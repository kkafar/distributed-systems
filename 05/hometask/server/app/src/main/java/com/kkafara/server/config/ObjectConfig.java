package com.kkafara.server.config;

import java.util.Set;

public class ObjectConfig {
  private String name;

  private ObjectType type;

  private Float temperature;

  private Set<Integer> zones;

  public ObjectType getType() {
    return type;
  }

  public String getName() {
    return name;
  }

  public Float getTemperature() {
    return temperature;
  }

  public Set<Integer> getZones() {
    return zones;
  }
}
