package com.kkafara.watcher;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

public class DataMonitor {

  private ZooKeeper zooKeeper;
  private String znode;
  private Watcher watcher;
  private DataMonitorListener listener;

  public DataMonitor(ZooKeeper zooKeeper, String znode, Watcher watcher, DataMonitorListener listener) {
    this.zooKeeper = zooKeeper;
    this.znode = znode;
    this.watcher = watcher;
    this.listener = listener;
  }

  public void process(WatchedEvent event) {
  }

  public boolean isDead() {
    // TODO
    return false;
  }
}
