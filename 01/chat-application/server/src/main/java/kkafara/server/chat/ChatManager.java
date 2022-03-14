package kkafara.server.chat;

import kkafara.server.data.model.Message;
import kkafara.server.data.model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

public class ChatManager implements UserActivityListener {
  private final Logger mLogger;

  private final ReentrantLock mLock;

  private final Set<User> mUserSet;

  private final Map<User, MessageSender> mUserToTcpSenderMap;

  private final Map<User, MessageSender> mUserToUdpSenderMap;

  private final User mRootUser;

  private final ExecutorService mExecutor;

  private final ChatPrinter mChatPrinter;

  private final LinkedList<Message> mChatHistory;

  public ChatManager() {
    mLogger = LogManager.getLogger(ChatManager.class);
    mLock = new ReentrantLock(true);
    mUserSet = new LinkedHashSet<>();
    mUserToTcpSenderMap = new LinkedHashMap<>();
    mUserToUdpSenderMap = new LinkedHashMap<>();
    mRootUser = new User(0, "SERVER");
    mExecutor = Executors.newSingleThreadExecutor();
    mChatPrinter = new ChatPrinter();
    mChatHistory = new LinkedList<>();
  }

  public User getRootUser() {
    return mRootUser;
  }

  @Override
  public void notifyOnMessageAsync(@NotNull Message message) {
    mExecutor.submit(() -> {
      mLock.lock();
      try {
        mLogger.info("notifyOnMessage");

        if (mChatHistory.size() > 10) {
          mChatHistory.removeFirst();
        }
        mChatHistory.addLast(message);

        printChat();

        User user = message.getUser();
        Map<User, MessageSender> map = null;
        switch (message.getType()) {
          case UDP -> { map = mUserToUdpSenderMap; }
          case TCP -> { map = mUserToTcpSenderMap; }
          default -> {
            mLogger.warn("MESSAGE W/O SPECIFIED TYPE");}
        }
        if (map != null) {
          map.forEach((user_, sender_) -> {
            if (!user.getName().equals(user_.getName())) {
              if (message.getType() == Message.Type.UDP) {
                ((UDPMessageSender) sender_).setPort(user_.getPort());
              }
              mLogger.info("Sending message to: " + user_.getName() + " port: " + user_.getPort());
              sender_.send(message);
            }
          });
        }
      } finally {
        mLock.unlock();
      }
    });
  }


  private void printChat() {
    for (Message message : mChatHistory) {
      mChatPrinter.print(message);
    }
  }

  @Override
  public boolean notifyOnNewUserMessage(User user, MessageSender tcpMessageSender, MessageSender udpMessageSender) {
    mLock.lock();
    try {
      mLogger.info("Registering user with nickname: " + user.getName());
      if (mUserSet.contains(user)) {
        mLogger.info("User with nickname: " + user.getName() + " already exists!");
        return false;
      } else {
        mUserSet.add(user);
        mUserToTcpSenderMap.put(user, tcpMessageSender);
        mUserToUdpSenderMap.put(user, udpMessageSender);
        return true;
      }
    } finally {
      mLock.unlock();
    }
  }

  @Override
  public void notifyOnUserDisconnectedMessageAsync(User user) {
    mExecutor.submit(() -> {
      mLock.lock();
      try {
        mUserSet.remove(user);
        mUserToTcpSenderMap.remove(user);
        mUserToUdpSenderMap.remove(user);
      } finally {
        mLock.unlock();
      }
    });
  }
}
