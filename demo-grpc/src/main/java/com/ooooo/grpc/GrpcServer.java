package com.ooooo.grpc;

import com.ooooo.grpc.helloworld.GreeterGrpc;
import com.ooooo.grpc.helloworld.HelloReply;
import com.ooooo.grpc.helloworld.HelloRequest;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import lombok.SneakyThrows;

/**
 * @author ooooo
 * @date 2021/07/27 08:16
 */
public class GrpcServer {

  public static final int PORT = 12345;

  @SneakyThrows
  public static void main(String[] args) {
    Server server = ServerBuilder.forPort(PORT)
        .addService(new GreeterImpl())
        .build()
        .start();
    server.awaitTermination();
  }

  static class GreeterImpl extends GreeterGrpc.GreeterImplBase {

    @Override
    public void sayHello(HelloRequest request, StreamObserver<HelloReply> responseObserver) {
      System.out.println("receive message: " + request.getName());
      HelloReply helloReply = HelloReply.newBuilder().setMessage(request.getName() + " say Hello").build();
      responseObserver.onNext(helloReply);
      responseObserver.onCompleted();
    }

    @Override
    public void sayHi(HelloRequest request, StreamObserver<HelloReply> responseObserver) {
      System.out.println("receive message: " + request.getName());
      HelloReply helloReply = HelloReply.newBuilder().setMessage(request.getName() + " say Hi").build();
      responseObserver.onNext(helloReply);
      responseObserver.onCompleted();
    }
  }


}
