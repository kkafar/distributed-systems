package com.kkafara.server.smarthome.lighting;

import Smarthome.DeviceMetadata;
import Smarthome.ExecutionException;
import Smarthome.Lighting.IPartialDrawBlindsManager;
import com.kkafara.server.smarthome.DeviceControllerImpl;
import com.zeroc.Ice.Current;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PartialDrawBlindsManager extends DeviceControllerImpl implements IPartialDrawBlindsManager {

  private final Logger logger = LogManager.getLogger(PartialDrawBlindsManager.class);
  private float drawLevel;

  private final String ERR_INVALID_VALUE = "Draw level must be between 0 and 1";

  public PartialDrawBlindsManager(DeviceMetadata metadata, float drawLevel) {
    super(metadata);
    assert drawLevel >= 0 && drawLevel <= 1 : ERR_INVALID_VALUE;
    this.drawLevel = drawLevel;
  }

  @Override
  public void drawTo(float percent, Current current) throws ExecutionException {
    if (percent < 0 || percent > 1) {
      logger.error(getTag() + ERR_INVALID_VALUE);
      throw new ExecutionException(ERR_INVALID_VALUE);
    }
    drawLevel = percent;
  }

  @Override
  public float getDrawLevel(Current current) {
    return drawLevel;
  }
}
