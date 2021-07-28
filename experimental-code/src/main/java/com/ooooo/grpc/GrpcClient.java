package com.ooooo.grpc;

import com.ooooo.grpc.helloworld.GreeterGrpc;
import com.ooooo.grpc.helloworld.GreeterGrpc.GreeterBlockingStub;
import com.ooooo.grpc.helloworld.HelloRequest;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.SneakyThrows;

import java.util.concurrent.TimeUnit;

/**
 * @author ooooo
 * @date 2021/07/27 12:07
 */
public class GrpcClient {

	@SneakyThrows
	public static void main(String[] args) {
		String target = "localhost:12345";
		ManagedChannel channel = ManagedChannelBuilder.forTarget(target)
						// Channels are secure by default (via SSL/TLS). For the example we disable TLS to avoid
						// needing certificates.
						.usePlaintext()
						.build();

		GreeterBlockingStub stub = GreeterGrpc.newBlockingStub(channel);
		System.out.println(stub.sayHello(HelloRequest.newBuilder().setName("tom").build()));
		System.out.println(stub.sayHi(HelloRequest.newBuilder().setName("jerry").build()));


		channel.shutdownNow().awaitTermination(5, TimeUnit.SECONDS);
	}
}
