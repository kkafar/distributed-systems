package kkafara.client;

import kkafara.client.chat.*;
import kkafara.data.model.Message;
import kkafara.data.model.ServerResponses;
import kkafara.data.model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

public class Client {
  private static final int SERVER_PORT = 8080;
  private static final String PROMPT = "> ";
  private static final int DEFAULT_INPUT_BUFFER_SIZE = 1024;
  private final Logger mLogger;
  private final User mUser;

  private BufferedReader mStdInReader;
  private final ChatPrinter mChatPrinter;

  private final LinkedList<Message> mChatHistory;

  private MessageSender mTCPMessageSender;
  private MessageReceiver mTCPMessageReceiver;
  private MessageReceiver mUDPMessageReceiver;

  private final ExecutorService mListenerExecutor;
  private final ExecutorService mExecutor;

  private final ReentrantLock mLock;

  public Client(String name) {
    assertNameIsValid(name);
    mLogger = LogManager.getLogger(Client.class);
    mUser = new User(0, name);
    mChatPrinter = new ChatPrinter();
    mChatHistory = new LinkedList<>();
    mListenerExecutor = Executors.newCachedThreadPool();
    mExecutor = Executors.newSingleThreadExecutor();
    mLock = new ReentrantLock(true);
  }

  private void assertNameIsValid(String name) {
  }

  public void run(String serverAddress, int port) {
    DatagramSocket datagramSocket = null;

    try (
        Socket socket = new Socket(serverAddress, port);
    ) {
      mTCPMessageSender = new TCPMessageSender(socket.getOutputStream());
      mTCPMessageReceiver = new TCPMessageReceiver(DEFAULT_INPUT_BUFFER_SIZE, socket.getInputStream());

      // attempt to register user
      mTCPMessageSender.send(new Message(mUser, ""));

      // await server response
      Message message = mTCPMessageReceiver.receive();

      if (message == null) return;

      mLogger.info("Server response: " + message.getContent());

      if (!message.getContent().equals(ServerResponses.ERROR)) {
//        int udpPort = Integer.parseInt(message.getContent());
        int udpPort = socket.getLocalPort();
        System.out.println("PORT: " + udpPort);
        datagramSocket = new DatagramSocket(udpPort);
        mUDPMessageReceiver = new UDPMessageReceiver(DEFAULT_INPUT_BUFFER_SIZE, datagramSocket);

        mListenerExecutor.submit(new TCPListener(mTCPMessageReceiver, this));
        mListenerExecutor.submit(new UDPListener(mUDPMessageReceiver, this));

        mStdInReader = new BufferedReader(new InputStreamReader(System.in));

        while (true) {
          String input = getUserInput();
          clearScreen();
          System.out.println("User input:");
          System.out.println(input);

          message = new Message(mUser, input);


          switch (UserInputParser.resolveMessageConfiguration(input)) {
            case TCP -> {
              handleTCPMessage(message);
            }
            case UDP -> {
              System.out.println("UDP message");
              input = input.substring(1);
              message = new Message(mUser, input);
              handleUDPMessage(message, datagramSocket);
            }
            case MULTICAST -> handleMulticastMessage(message);
          }

          if (mChatHistory.size() > 10) {
            mChatHistory.removeFirst();
          }
          mChatHistory.addLast(message);
          printChat();
        }
      } else {
        mLogger.info("Failed to register. Try to provide different nickname next time.");
      }
    } catch (IOException exception) {
      if (exception.getMessage() != null) {
        mLogger.warn(exception.getMessage());
      } else {
        mLogger.warn("Exception thrown in run method");
      }
      exception.printStackTrace();
    } finally {
      if (datagramSocket != null && !datagramSocket.isClosed()) {
        datagramSocket.close();
      }
    }
    mListenerExecutor.shutdownNow();
  }


  private String getUserInput() throws IOException {
    System.out.print(PROMPT);
//    StringBuilder builder = new StringBuilder();
//    mStdInReader.lines().takeWhile(line -> {
//      try {
//        return mStdInReader.ready();
//      } catch (IOException e) {
//        e.printStackTrace();
//      }
//      return false;
//    }).forEach(builder::append);
//    return builder.toString();
    return mStdInReader.readLine();
  }

  private void handleMulticastMessage(Message message) {
    mLogger.info("Handling multicast message");
  }

  private void handleUDPMessage(Message message, DatagramSocket socket) {
    mLogger.info("Handling UDP message");
    try {
      InetAddress address = InetAddress.getByName("127.0.0.1");

      byte[] bytesToSend = message.encodeToString().getBytes(StandardCharsets.UTF_8);
      DatagramPacket sendPacket = new DatagramPacket(bytesToSend, bytesToSend.length, address, SERVER_PORT);

      socket.send(sendPacket);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void handleTCPMessage(Message message) {
    mLogger.info("Handling TCP message");
    mTCPMessageSender.send(message);
  }

  private void printChat() {
    clearScreen();
    System.out.println("--".repeat(20));
    for (Message message : mChatHistory) {
      System.out.println(message.getUser().getName() + " says: ");
      System.out.println(message.getContent());
    }
    System.out.println("--".repeat(20));
  }

  private static void clearScreen() {
    System.out.print("\033[H\033[2J");
    System.out.flush();
    System.out.println('\r');
  }

  public void notifyOnMessageReceivedAsync(Message message) {
    mExecutor.submit(() -> {
//      mLogger.info("Received message");
//      System.out.println(message);
      mLock.lock();
      try {
        mChatHistory.add(message);
        clearScreen();
        printChat();
      } finally {
        mLock.unlock();
      }
    });
  }
}
