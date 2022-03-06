package kkafara.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
  public static final int DEFAULT_PORT = 8080;

  private static final int WORKER_THREADS = 4;

  private final String mAddress;
  private ServerSocket mServerSocket;
  private final Logger mLogger;
  private final Set<Integer> mPossiblePorts;

  private final ExecutorService mExecutors;

  public Server() {
    this("127.0.0.1", DEFAULT_PORT);
  }

  public Server(String address, int port) {
    this(address, Set.of(port));
  }

  public Server(String address, Set<Integer> possiblePorts) {
    mLogger = LogManager.getLogger(Server.class);

    mAddress = address;
    mPossiblePorts = possiblePorts;

    mExecutors = Executors.newCachedThreadPool();
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

    try {
      while (true) {
        try {
          Socket incomingConnectionSocket = mServerSocket.accept();
          dispatchConnectionToHandler(incomingConnectionSocket);
        } catch (IOException exception) {
          mLogger.warn("Failed to handle incoming connection");
        }
      }
    } finally {
      try {
        mLogger.info("Closing server socket");
        mServerSocket.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  private void dispatchConnectionToHandler(Socket incomingConnectionSocket) {
  }

  public int getPort() {
    return mServerSocket.getLocalPort();
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
}
