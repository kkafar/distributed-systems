package com.kkafara.server;

import com.kkafara.server.config.ChatServerConfig;
import com.kkafara.server.grpc.CounterServiceImpl;
import com.kkafara.server.grpc.IssueTrackerImpl;
import com.kkafara.server.grpc.TimeServiceImpl;
import io.grpc.Server;
import io.grpc.netty.shaded.io.grpc.netty.NettyServerBuilder;
import io.grpc.protobuf.services.ProtoReflectionService;
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
        .addService(new TimeServiceImpl())
        .addService(new CounterServiceImpl())
        .addService(new IssueTrackerImpl())
        .addService(ProtoReflectionService.newInstance())
        .build();
  }

  public void start() throws IOException, InterruptedException {
    server.start();
    if (server != null) {
      server.awaitTermination();
    }
  }
}
