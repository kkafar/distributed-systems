package com.kkafara.watcher;

import org.apache.commons.exec.*;
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

  private org.apache.commons.exec.Executor processExecutor = new DefaultExecutor();
  private ExecuteWatchdog processWatchdog = null;

  public Executor(String hostPort, String znode, String executable) throws KeeperException, IOException, InterruptedException {
    this.hostPort = hostPort;
    this.znode = znode;
    this.executable = executable;
    this.zookeeper = new ZooKeeper(hostPort, 3000, this);
    this.dataMonitor = new DataMonitor(this.zookeeper, znode, null, this);
  }

  @Override
  public void process(WatchedEvent event) {
    logger.info("Notified of event " + event.toString());
    dataMonitor.process(event);
  }

  public void run() {
    try {
      synchronized (this) {
        while (!dataMonitor.isDead()) {
          logger.info("Waiting...");
          wait();
          logger.info("Woke up!");
        }
      }
    } catch (InterruptedException ex) {
      logger.warn("Interrupted exception in run");
      ex.printStackTrace();
    }
  }

  public void closing() {
    synchronized (this) {
      notifyAll();
    }
  }

  @Override
  public void onZNodeCreated(WatchedEvent event) {
    logger.info("Notified of node created event");
    try {
      logger.info("Starting drawing program");
      CommandLine cmdLine = CommandLine.parse(executable);
      DefaultExecuteResultHandler resultHandler = new DefaultExecuteResultHandler();
      processWatchdog = new ExecuteWatchdog(60 * 60 * 1000);
      processExecutor.setWatchdog(processWatchdog);
      processExecutor.execute(cmdLine, resultHandler);
    } catch (IOException e) {
      logger.warn("Failed while running the graphical application process");
      e.printStackTrace();
    }
  }

  @Override
  public void onZNodeDeleted(WatchedEvent event) {
    logger.info("Notified of node deleted event.");
    if (processWatchdog != null) {
      logger.info("Shutting down drawing program");
      processWatchdog.destroyProcess();
      processWatchdog = null;
    } else {
      logger.info("No qui process active");
    }
  }

  @Override
  public void onChildrenChange(WatchedEvent event) {
    logger.info("Notified of children change");
  }

  @Override
  public void onChildAdded(WatchedEvent event) {
    logger.info("Notified of child added event");
  }
}

