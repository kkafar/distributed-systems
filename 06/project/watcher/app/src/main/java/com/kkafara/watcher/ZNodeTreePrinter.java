package com.kkafara.watcher;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Objects;

public class ZNodeTreePrinter implements AsyncCallback.ChildrenCallback {
  private final Logger logger = LogManager.getLogger(ZNodeTreePrinter.class);

  private WeakReference<ZooKeeper> zooKeeperWeakRef;

  public ZNodeTreePrinter(ZooKeeper zooKeeper) {
    zooKeeperWeakRef = new WeakReference<>(zooKeeper);
  }

  private void printSubtree(String root) {
    // print node path
    System.out.println(root);

    // get zk instance; for now lets assume that it is not null
    var zk = zooKeeperWeakRef.get();
    assert zk != null : "ZooKeeper instance is null in ZNodeTreePrinter";

    // get node children
    try {
      List<String> currChildren = zk.getChildren(root, false);
      currChildren.forEach(childPath -> printSubtree(root + '/' + childPath));
    } catch (KeeperException | InterruptedException e) {
      logger.error("Failed to get children for " + root);
      e.printStackTrace();
    }
  }

  @Override
  public void processResult(int rc, String path, Object ctx, List<String> children) {
    logger.info("Processing print children request");
    logger.info("Children list below:");
    printSubtree(path);
  }
}
