package com.kkafara.server.smarthome.lighting;

import Smarthome.DeviceMetadata;
import Smarthome.Lighting.IBlindsManager;
import com.kkafara.server.smarthome.DeviceControllerImpl;
import com.zeroc.Ice.Current;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BlindsManager extends DeviceControllerImpl implements IBlindsManager {

  private final Logger logger = LogManager.getLogger(BlindsManager.class);
  private boolean drawn;

  public BlindsManager(DeviceMetadata metadata, boolean drawn) {
    super(metadata);
    this.drawn = drawn;
  }

  @Override
  public void draw(Current current) {
    logger.info(getTag() + "Drawing the blinds");
    drawn = true;
  }

  @Override
  public void undraw(Current current) {
    // there is no such word as "undraw", ye I know
    logger.info(getTag() + "Undrawing the blinds");
    drawn = false;
  }

  @Override
  public boolean isDrawn(Current current) {
    return drawn;
  }


}
