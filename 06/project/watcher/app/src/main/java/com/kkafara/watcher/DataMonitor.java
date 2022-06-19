package com.kkafara.watcher;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.nio.file.Path;
import java.util.Optional;

public class DataMonitor implements Watcher, AsyncCallback.StatCallback {

  private final Logger logger = LogManager.getLogger(DataMonitor.class);
  private ZooKeeper zooKeeper;
  private String znode;
  private Watcher watcher;
  private DataMonitorListener listener;
  boolean dead;

  public DataMonitor(ZooKeeper zooKeeper, String znode, Watcher watcher, DataMonitorListener listener) {
    this.zooKeeper = zooKeeper;
    this.znode = znode;
    this.watcher = watcher;
    this.listener = listener;
    this.zooKeeper.exists(znode, true, this, null);
  }

  public void process(WatchedEvent event) {
    Path path = event.getPath() != null ? Path.of(event.getPath()) : null;

    if (event.getType() == Watcher.Event.EventType.None) {
      logger.info("Notified of None event. Doing nothing");
    } else {
      if (path != null && path.toString().equals(znode)) {
        zooKeeper.exists(znode, true, this, null);
      }
    }
    if (watcher != null) {
      watcher.process(event);
    }
  }

  public boolean isDead() {
    return dead;
  }

  @Override
  public void processResult(int rc, String path, Object ctx, Stat stat) {
    logger.info("Processing result");
    boolean exists;
    switch (KeeperException.Code.get(rc)) {
      case OK: {
        logger.info("OK");
        exists = true;
//        zooKeeper.exists(znode, true, this, null);
        break;
      }
      case NONODE: {
        logger.info("NONODE");
        exists = false;
        break;
      }
      case NOAUTH:
      case SESSIONEXPIRED: {
        logger.info("NOAUTH || SESSIONEXPIRED");
       dead = true;
//       listener
        return;
      }
      default: {
        logger.info("DEFAULT");
        zooKeeper.exists(znode, true, this, null);
        return;
      }
    }

    byte[] b = null;
    if (exists) {
      try {
        b = zooKeeper.getData(znode, false, null);
      } catch (KeeperException ex) {
        Optional.ofNullable(ex.getMessage()).ifPresent(logger::error);
        ex.printStackTrace();
      } catch (InterruptedException ex) {
        Optional.ofNullable(ex.getMessage()).ifPresent(logger::error);
        ex.printStackTrace();
        return;
      }
      listener.exists(b);
    }
  }
}
