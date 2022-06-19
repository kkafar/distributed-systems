package com.kkafara.watcher;

import org.apache.zookeeper.WatchedEvent;

public interface DataMonitorListener {
  void onZNodeCreated(WatchedEvent event);

  void onZNodeDeleted(WatchedEvent event);

  void onChildrenChange(WatchedEvent event);

  void onChildAdded(WatchedEvent event);
}
