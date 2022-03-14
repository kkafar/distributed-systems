package kkafara.server.chat;

import kkafara.server.data.model.Message;

public class ChatPrinter {
  public void print(Message message) {
    System.out.println(message.getUser().getName() + " says:");
    System.out.println(message.getContent());
  }
}
