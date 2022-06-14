package com.kkafara.watcher;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;

public class Executor implements Watcher, Runnable, DataMonitorListener {

  private final Logger logger = LogManager.getLogger(Executor.class);

  private String hostPort;
  private String znode;
  private String executable;

  private ZooKeeper zookeeper;
  private DataMonitor dataMonitor;

  Process guiProcess;

  public Executor(String hostPort, String znode, String executable) throws KeeperException, IOException {
    this.hostPort = hostPort;
    this.znode = znode;
    this.executable = executable;
    this.zookeeper = new ZooKeeper(hostPort, 3000, this);
    this.dataMonitor = new DataMonitor(this.zookeeper, znode, null, this);
  }

  @Override
  public void process(WatchedEvent event) {
    dataMonitor.process(event);
  }

  public void run() {
    try {
      synchronized (this) {
        while (!dataMonitor.isDead()) {
          wait();
        }
      }
    } catch (InterruptedException ex) {
      logger.warn("Interrupted exception in run");
      ex.printStackTrace();
    }
  }

  @Override
  public void exists(byte[] data) {

  }
}
