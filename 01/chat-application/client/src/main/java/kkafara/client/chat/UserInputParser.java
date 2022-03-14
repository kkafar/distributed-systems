package kkafara.client.chat;

import kkafara.data.model.MessageConfigurations;

public class UserInputParser {
  public UserInputParser() {

  }

  public static MessageConfigurations resolveMessageConfiguration(String input) {
    if (input.charAt(0) == 'U') {
      return MessageConfigurations.UDP;
    } else if (input.charAt(0) == 'M') {
      return MessageConfigurations.MULTICAST;
    } else {
      return MessageConfigurations.TCP;
    }
  }
}
