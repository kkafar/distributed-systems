package kkafara.client.chat;

import kkafara.client.Client;
import kkafara.data.model.Message;

public class TCPListener implements Runnable {
  private final MessageReceiver mMessageReceiver;
  private final Client mClient;

  private boolean mIsRunning;

  public TCPListener(MessageReceiver TCPMessageReceiver, Client client) {
    mMessageReceiver = TCPMessageReceiver;
    mClient = client;
    mIsRunning = true;
  }

  @Override
  public void run() {
    Message message;
    while (isRunning()) {
      message = mMessageReceiver.receive();
      if (message != null) {
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
