package kkafara.server.chat;

import kkafara.server.data.model.Message;
import kkafara.server.data.model.User;

public interface UserActivityListener {
  void notifyOnMessageAsync(Message message);

  boolean notifyOnNewUserMessage(User user, MessageSender tcpMessageSender, MessageSender udpMessageSender);

  void notifyOnUserDisconnectedMessageAsync(User user);
}
