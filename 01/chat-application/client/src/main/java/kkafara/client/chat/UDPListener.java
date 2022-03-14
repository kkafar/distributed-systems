package kkafara.client.chat;

import kkafara.client.Client;
import kkafara.data.model.Message;

public class UDPListener implements Runnable {
  private final MessageReceiver mMessageReceiver;
  private final Client mClient;
  private boolean mIsRunning;

  public UDPListener(MessageReceiver udpMessageReceiver, Client client) {
    mMessageReceiver = udpMessageReceiver;
    mClient = client;
    mIsRunning = true;
  }

  @Override
  public void run() {
    Message message;
    while (isRunning()) {
      message = mMessageReceiver.receive();
      if (message != null) {
        System.out.println("Received non null UDP message");
        mClient.notifyOnMessageReceivedAsync(message);
      } else {
        mIsRunning = false;
      }
    }

  }

  private boolean isRunning() {
    return mIsRunning;
  }
}
