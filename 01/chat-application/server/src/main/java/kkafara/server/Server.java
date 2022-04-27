package kkafara.server;

import kkafara.server.chat.ChatManager;
import kkafara.server.chat.UDPListener;
import kkafara.server.chat.UDPMessageReceiver;
import kkafara.server.chat.UDPMessageSender;
import kkafara.server.connection.UserConnectionHandler;
import kkafara.server.data.model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.*;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
  public static final int DEFAULT_PORT = 8080;
  public static final String DEFAULT_ADDRESS = "127.0.0.1";

  private static final int WORKER_THREADS = 4;

  private final String mAddress;
  private ServerSocket mServerSocket;
  private DatagramSocket mDatagramSocket;
  private final Logger mLogger;
  private final Set<Integer> mPossiblePorts;
  private final ChatManager mChatManager;

  private UDPMessageSender mUdpMessageSender;
  private UDPMessageReceiver mUdpMessageReceiver;
  private UDPListener mUdpListener;


  private final ExecutorService mExecutors;
  private final ExecutorService mUdpExecutor;

  public Server() {
    this(DEFAULT_ADDRESS, DEFAULT_PORT);
  }

  public Server(String address, int port) {
    this(address, Set.of(port));
  }

  public Server(String address, Set<Integer> possiblePorts) {
    mLogger = LogManager.getLogger(Server.class);

    mAddress = address;
    mPossiblePorts = possiblePorts;

    mExecutors = Executors.newCachedThreadPool();
    mUdpExecutor = Executors.newSingleThreadExecutor();

    mChatManager = new ChatManager();
  }

  public void run() {
    allocateServerSocketOnFirstFreePortFromSet(mPossiblePorts).ifPresentOrElse(socket -> {
      mServerSocket = socket;
      mLogger.info("Server socket created! Address: " + mServerSocket.getInetAddress() + " port: " + mServerSocket.getLocalPort());
    }, () -> {
      String errorMessage = "Failed to allocate socket on proved port set";
      mLogger.error(errorMessage);
      throw new NoSuchElementException(errorMessage);
    });

    allocateDatagramSocketOnFirstFreePortFromSet(mPossiblePorts).ifPresentOrElse(socket -> {
      mDatagramSocket = socket;
      mLogger.info("Server socket created! Address: " + mDatagramSocket.getInetAddress() + " port: " + mDatagramSocket.getLocalPort());
    }, () -> {
      String errorMessage = "Failed to allocate socket on proved port set";
      mLogger.error(errorMessage);
      throw new NoSuchElementException(errorMessage);
    });

    try {
      mUdpMessageSender = new UDPMessageSender(mDatagramSocket, DEFAULT_ADDRESS);
      mUdpMessageReceiver = new UDPMessageReceiver(1024, mDatagramSocket);
      mUdpListener = new UDPListener(mUdpMessageReceiver, mChatManager);

      mUdpExecutor.submit(mUdpListener);

      while (true) {
        try {
          Socket incomingConnectionSocket = mServerSocket.accept();
          dispatchConnectionToHandler(incomingConnectionSocket);
        } catch (IOException exception) {
          mLogger.warn("Failed to handle incoming connection");
          mLogger.warn(exception.getMessage());
          exception.printStackTrace();
        }
      }
    } catch (UnknownHostException e) {
      e.printStackTrace();
    } finally {
      try {
        mLogger.info("Closing server socket");
        mServerSocket.close();
        mDatagramSocket.close();
        mUdpExecutor.shutdownNow();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  private void dispatchConnectionToHandler(Socket incomingConnectionSocket) throws SocketException {
    incomingConnectionSocket.setKeepAlive(true);
    mLogger.info("Incoming connection on local port: " + incomingConnectionSocket.getLocalPort() + " remote port: " + incomingConnectionSocket.getPort());
    UserConnectionHandler handler = new UserConnectionHandler(incomingConnectionSocket, mChatManager);
    handler.setUdpMessageSender(mUdpMessageSender);
    mExecutors.submit(handler);
  }

  private Optional<ServerSocket> allocateServerSocketOnFirstFreePortFromSet(Set<Integer> ports) {
    ServerSocket socket = null;
    for (int port : ports) {
      try {
        socket = new ServerSocket(port);
        return Optional.of(socket);
      } catch (IOException ignore) {}
    }
    return Optional.empty();
  }

  private Optional<DatagramSocket> allocateDatagramSocketOnFirstFreePortFromSet(Set<Integer> ports) {
    DatagramSocket socket = null;
    for (int port : ports) {
      try {
        socket = new DatagramSocket(port);
        return Optional.of(socket);
      } catch (SocketException e) {
        e.printStackTrace();
      }
    }
    return Optional.empty();
  }
}
