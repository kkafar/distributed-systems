package com.kkafara.server.config;

import com.google.gson.annotations.SerializedName;

public enum ObjectType {

  @SerializedName("air-conditioner")
  AirConditioner,

  @SerializedName("zone-air-conditioner")
  ZoneAirConditioner,

  @SerializedName("timed-air-conditioner")
  TimedAirConditioner,

  @SerializedName("smart-air-conditioner")
  SmartAirConditioner,

  @SerializedName("water-heater")
  WaterHeater,

  @SerializedName("oven")
  Oven,

  @SerializedName("blinds-manager")
  BlindsManager,

  @SerializedName("partial-draw-blinds-manager")
  PartialDrawBlindsManager
}
