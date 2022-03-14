package kkafara.server.connection;

import kkafara.server.chat.*;
import kkafara.server.data.model.Message;
import kkafara.server.data.model.ServerResponses;
import kkafara.server.data.model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UserConnectionHandler implements Runnable {
  private static final int DEFAULT_INPUT_BUFFER_SIZE = 1024;
  private static final int DEFAULT_OUTPUT_BUFFER_SIZE = 1024;

  private final Socket mClientSocket;
  private final Logger mLogger;

  private DatagramSocket mDatagramSocket;

  private MessageSender mMessageSender;
  private MessageSender mUdpMessageSender;
  private MessageReceiver mMessageReceiver;
  private MessageReceiver mUdpMessageReceiver;

  private final ChatManager mChatManager;

  private final ExecutorService mExecutor;
  private final ExecutorService mUdpListener;

  private User mUser;

  private boolean mIsRunning;

  private final List<UserActivityListener> mUserActivityListeners;

  public UserConnectionHandler(Socket clientSocket, ChatManager chatManager) {
    if (clientSocket == null) {
      throw new IllegalArgumentException("Client socket must not be null");
    }
    mClientSocket = clientSocket;
    mLogger = LogManager.getLogger(UserConnectionHandler.class);
    mChatManager = chatManager;
    mExecutor = Executors.newSingleThreadExecutor();
    mUdpListener = Executors.newSingleThreadExecutor();
    mIsRunning = true;
    mUserActivityListeners = List.of(chatManager);
  }

  public void setUdpMessageSender(MessageSender messageSender) {
    mUdpMessageSender = messageSender;
  }

  @Override
  public void run() {
    mLogger.info("Running connection handler");

    int bytesRead;
    Message message;

    try {
      mMessageReceiver = new TCPMessageReceiver(DEFAULT_INPUT_BUFFER_SIZE, mClientSocket.getInputStream());
      mMessageSender = new TCPMessageSender(mClientSocket.getOutputStream());
//      mDatagramSocket = new DatagramSocket(mClientSocket.getPort());
//      mUdpMessageReceiver = new UDPMessageReceiver(DEFAULT_INPUT_BUFFER_SIZE, mDatagramSocket);
//      mUdpMessageSender = new UDPMessageSender(mDatagramSocket);
    } catch (IOException e) {
      if (e.getMessage() != null) {
        mLogger.error(e.getMessage());
      }
      e.printStackTrace();
      return;
    }

    // client registration
    message = mMessageReceiver.receive();

    mLogger.debug("After message parsing");

    assert message != null;

    mUser = message.getUser();
    mUser.setPort(mClientSocket.getPort());

    if (mChatManager.notifyOnNewUserMessage(message.getUser(), mMessageSender, mUdpMessageSender)) {
      mMessageSender.send(new Message(mChatManager.getRootUser(), Integer.toString(mClientSocket.getLocalPort())));
    } else {
      mLogger.error("Client failed to register properly");
      mMessageSender.send(new Message(mChatManager.getRootUser(), ServerResponses.ERROR));
      try {
        mClientSocket.close();
//        mDatagramSocket.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
      return;
    }

    try {
//      mUdpListener.submit(new UDPListener(mUdpMessageReceiver, mChatManager));

      while (isRunning()) {
        // get client message
        message = mMessageReceiver.receive();
        if (message != null) {
          message.setType(Message.Type.TCP);
          mChatManager.notifyOnMessageAsync(message);
        } else {
          mIsRunning = false;
          mChatManager.notifyOnUserDisconnectedMessageAsync(mUser);
        }
      }
    } finally {
      try {
        mClientSocket.close();
//        mDatagramSocket.close();
//        mUdpListener.shutdownNow();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    mLogger.info("Connection handler closed by exiting run method");
  }

  private boolean isRunning() {
    return mIsRunning && !mClientSocket.isClosed();
  }
}
