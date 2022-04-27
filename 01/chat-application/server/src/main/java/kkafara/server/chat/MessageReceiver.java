package kkafara.server.chat;

import kkafara.server.data.model.Message;

import java.nio.CharBuffer;

public interface MessageReceiver {
  Message receive();
}
