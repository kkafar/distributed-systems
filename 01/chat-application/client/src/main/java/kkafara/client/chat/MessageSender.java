package kkafara.client.chat;

import kkafara.data.model.Message;

public interface MessageSender {
  boolean send(Message message);
}
