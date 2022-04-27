package kkafara.client.chat;

import kkafara.data.model.Message;

public class ChatPrinter {
  public void printMessage(Message message) {
    System.out.println(message.getUser().getName() + " says:");
    System.out.println(message.getContent());
  }
}
