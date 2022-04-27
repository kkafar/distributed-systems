package kkafara.client.chat;

import kkafara.data.model.Message;

public interface MessageReceiver {
  Message receive();
}
