package com.kkafara.server.smarthome.airconditioning;

import Smarthome.AirConditioning.ITimedAirConditioner;
import Smarthome.DeviceMetadata;
import Smarthome.ExecutionException;
import Smarthome.Time;
import com.zeroc.Ice.Current;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TimedAirConditionerImpl extends AirConditionerImpl implements ITimedAirConditioner {
  private final Logger logger = LogManager.getLogger(TimedAirConditionerImpl.class);

  public TimedAirConditionerImpl(DeviceMetadata metadata) {
    super(metadata, DEFAULT_TEMP);
  }

  @Override
  public void setTargetTempForTime(float temp, Time time, Current current) throws ExecutionException {
    logger.info(getTag() + "Setting temperature for time...");
    if (time.hour < 0 || time.hour > 23 || time.minutes < 0 || time.minutes > 59) {
      logger.info(getTag() + "Invalid time stamp provided");
      throw new ExecutionException("Invalid time stamp provided");
    }
    super.setTargetTemp(temp, current);
  }
}
