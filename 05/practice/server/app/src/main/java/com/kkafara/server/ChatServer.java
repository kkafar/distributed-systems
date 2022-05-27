package com.kkafara.server;

import com.kkafara.server.config.ChatServerConfig;
import com.kkafara.server.grpc.HelloServiceImpl;
import io.grpc.Server;
import io.grpc.netty.shaded.io.grpc.netty.NettyServerBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.concurrent.Executors;

public class ChatServer {
  private final Logger logger = LogManager.getLogger(getClass());
  private Server server;

  public ChatServer(ChatServerConfig config) {
    server = NettyServerBuilder
        .forPort(config.port)
        .executor(Executors.newFixedThreadPool(4))
        .addService(new HelloServiceImpl())
        .build();
  }

  public void start() throws IOException, InterruptedException {
    server.start();
    if (server != null) {
      server.awaitTermination();
    }
  }
}
