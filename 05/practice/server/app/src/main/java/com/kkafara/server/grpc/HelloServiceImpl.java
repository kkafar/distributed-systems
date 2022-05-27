package com.kkafara.server.grpc;

import io.grpc.stub.StreamObserver;

public class HelloServiceImpl extends HelloServiceGrpc.HelloServiceImplBase {
  @Override
  public void hello(HelloRequest request, StreamObserver<HelloResponse> responseObserver) {
    String responseMessage = "Hello " + request.getFirstName() + " " + request.getLastName() + "!";

    HelloResponse response = HelloResponse.newBuilder().setGreeting(responseMessage).build();

    responseObserver.onNext(response);
    responseObserver.onCompleted();
  }
}
