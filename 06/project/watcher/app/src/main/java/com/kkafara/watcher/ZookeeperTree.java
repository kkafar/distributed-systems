package com.kkafara.watcher;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;

import java.util.Iterator;
import java.util.List;

public class ZookeeperTree {
  private static final String isLastChild = "└─";
  private static final String isChild = "├─";
  private static final String spacer = "  ";
  private static final String isMore = "│ ";

  ZooKeeper zk;
  String znode;

  public ZookeeperTree(ZooKeeper zk, String znode) {
    this.zk = zk;
    this.znode = znode;
  }

  private boolean hasChildren(List<String> list) {
    return list.size() > 0;
  }

  private String buildPathSlashName(String path, String name) {
    return path + "/" + name;
  }

  void print() throws KeeperException, InterruptedException {
    String path = znode;

    print(path, znode, "");
  }

  private void print(String path, String parent, String tab) throws KeeperException, InterruptedException {
    List<String> list = zk.getChildren(path, true);
    Iterator<String> iterator = list.iterator();

    System.out.println(parent);
    if (hasChildren(list)) {
      while (true) {
        String name = iterator.next();
        if (iterator.hasNext()) {
          String tmp = returnWithChild(tab);
          System.out.print(tmp);
          print(buildPathSlashName(path, name), name, returnWithIsMore(tab));
          continue;
        } else {
          String tmp = returnWithLastChild(tab);
          System.out.print(tmp);
          print(buildPathSlashName(path, name), name, returnWithSpacer(tab));
          break;
        }

      }
    }

  }

  private String returnWithIsMore(String tab) {
    return returnWithSpacer(tab, isMore);
  }

  private String returnWithLastChild(String tab) {
    return returnWithSpacer(tab, isLastChild);
  }

  private String returnWithChild(String tab) {
    return returnWithSpacer(tab, isChild);
  }

  private String returnWithSpacer(String tab, String spacer) {
    return tab + spacer;
  }

  private String returnWithSpacer(String tab) {
    return returnWithSpacer(tab, spacer);
  }
}
