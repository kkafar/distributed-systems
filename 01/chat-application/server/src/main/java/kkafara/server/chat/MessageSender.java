package kkafara.server.chat;

import kkafara.server.data.model.Message;

public interface MessageSender {
  boolean send(Message message);
}
