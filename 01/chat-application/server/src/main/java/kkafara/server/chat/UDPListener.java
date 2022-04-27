package kkafara.server.chat;


import kkafara.server.data.model.Message;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UDPListener implements Runnable {
  private final MessageReceiver mMessageReceiver;
  private final ChatManager mChatManager;
  private boolean mIsRunning;
  private final Logger mLogger;

  public UDPListener(MessageReceiver udpMessageReceiver, ChatManager chatManager) {
    mMessageReceiver = udpMessageReceiver;
    mChatManager = chatManager;
    mIsRunning = true;
    mLogger = LogManager.getLogger(UDPListener.class);
  }

  @Override
  public void run() {
    Message message;
    while (isRunning()) {
      message = mMessageReceiver.receive();
      if (message != null) {
        mLogger.info("Received non null UDP message");
        message.setType(Message.Type.UDP);
        mChatManager.notifyOnMessageAsync(message);
      } else {
        mIsRunning = false;
      }
    }

  }

  private boolean isRunning() {
    return mIsRunning;
  }
}
