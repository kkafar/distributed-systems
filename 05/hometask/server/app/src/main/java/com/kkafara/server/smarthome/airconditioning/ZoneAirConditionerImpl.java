package com.kkafara.server.smarthome.airconditioning;

import Smarthome.AirConditioning.IZoneAirConditioner;
import Smarthome.ExecutionException;
import com.zeroc.Ice.Current;

public class ZoneAirConditionerImpl extends AirConditionerImpl implements IZoneAirConditioner {
  @Override
  public void setTargetTempForZone(float temp, int areaId, Current current) throws ExecutionException {

  }
}
