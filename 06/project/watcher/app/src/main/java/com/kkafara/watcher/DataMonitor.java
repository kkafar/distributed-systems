package com.kkafara.watcher;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.zookeeper.*;

import java.nio.file.Path;

public class DataMonitor implements Watcher {

  private final Logger logger = LogManager.getLogger(DataMonitor.class);
  private ZooKeeper zooKeeper;
  private String znode;
  private Watcher watcher;
  private DataMonitorListener listener;
  boolean dead;
  private ChildrenNumberHandler childrenNumberHandler;
  private ZNodeTreePrinter treePrinter;

  public DataMonitor(ZooKeeper zooKeeper, String znode, Watcher watcher, DataMonitorListener listener) throws InterruptedException, KeeperException {
    this.zooKeeper = zooKeeper;
    this.znode = znode;
    this.watcher = watcher;
    this.listener = listener;
    this.childrenNumberHandler = new ChildrenNumberHandler();
    this.treePrinter = new ZNodeTreePrinter(zooKeeper);


    // register persistent watch for events
    this.zooKeeper.addWatch(znode, this, AddWatchMode.PERSISTENT_RECURSIVE);
  }


  public void process(WatchedEvent event) {
    logger.info("DataMonitor notified of event: " + event.toString());

    Path path = event.getPath() != null ? Path.of(event.getPath()) : null;
    logger.info("Node path: " + (path != null ? path.toString() : " undefined"));

    if (path == null) {
      logger.warn("Null node path in received event; interrupting event processing");
      return;
    }

    logger.info("Event type: " + event.getType().toString());
    switch (event.getType()) {
      case NodeCreated -> {
        if (path.toString().equals(znode)) {
          logger.info("Reacting to creation of /z node");
          listener.onZNodeCreated(event);
        } else {
          logger.info("Node different to " + znode + " was created");
          listener.onChildAdded(event);
          logger.info("Delegating child number request to zookeeper with callback to childrenNumberHandler");
          zooKeeper.getAllChildrenNumber(znode, childrenNumberHandler, null);
          logger.info("Delegating child print request to TreePrinter via zookeeper getChildren request");
          zooKeeper.getChildren(znode, false, treePrinter, null);
        }
      }
      case NodeDeleted -> {
        if (path.toString().equals(znode)) {
          logger.info("Reacting to deletion of /z node");
          listener.onZNodeDeleted(event);
        } else {
          logger.info("Node different to " + znode + " was deleted");
        }
      }
      case NodeChildrenChanged -> {
        logger.info("Reacting to children change");
        listener.onChildrenChange(event);
      }
      default -> logger.info("Unhandled event type: " + event.getType().toString());
    }
  }

  public boolean isDead() {
    return dead;
  }
}
