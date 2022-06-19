package com.kkafara.watcher;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.zookeeper.AsyncCallback;

public class ChildrenNumberHandler implements AsyncCallback.AllChildrenNumberCallback {
  private final Logger logger = LogManager.getLogger(ChildrenNumberHandler.class);

  @Override
  public void processResult(int rc, String path, Object ctx, int number) {
    logger.info("Processing child number request");
    System.out.println("Number of children: " + number);
  }
}
