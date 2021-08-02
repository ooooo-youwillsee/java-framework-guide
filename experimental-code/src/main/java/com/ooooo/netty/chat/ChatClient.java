package com.ooooo.netty.chat;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;

/**
 * Simple Chat Client
 *
 * @author ooooo
 * @date 2021/08/02 21:53
 */
public class ChatClient {


	@SneakyThrows
	public static void main(String[] args) {

		EventLoopGroup eventLoopGroup = new NioEventLoopGroup(1);

		try {
			Bootstrap bootstrap = new Bootstrap();
			bootstrap.channel(NioSocketChannel.class)
							.group(eventLoopGroup)
							.handler(new ChannelInitializer<Channel>() {

								@Override
								protected void initChannel(Channel ch) throws Exception {
									ChannelPipeline pipeline = ch.pipeline();
									pipeline.addLast(new StringDecoder());
									pipeline.addLast(new StringEncoder());
									pipeline.addLast(new ClientHandler());
								}
							});

			ChannelFuture channelFuture = bootstrap.connect(new InetSocketAddress(ChatServer.port)).sync();
			Channel channel = channelFuture.channel();

			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
			while (true) {
				String line = bufferedReader.readLine();
				if ("quit".equals(line)) {
					break;
				}
				channel.writeAndFlush(line);
			}
		} finally {
			eventLoopGroup.shutdownGracefully();
		}
	}


	private static class ClientHandler extends ChannelDuplexHandler {

		@Override
		public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
			System.out.println("client receive message: {" + msg + "}");
		}
	}
}
