package com.ooooo.netty.heartbeat;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;
import lombok.SneakyThrows;

/**
 * simple heartbeat client
 * @author ooooo
 * @date 2021/08/03 20:56
 */
public class HeartbeatClient {

	public static final long heartbeat_time = 2;

	@SneakyThrows
	public static void main(String[] args) {
		EventLoopGroup eventLoopGroup = new NioEventLoopGroup(1);

		try {
			Bootstrap bootstrap = new Bootstrap();
			bootstrap.group(eventLoopGroup)
							.channel(NioSocketChannel.class)
							.handler(new ChannelInitializer<Channel>() {
								@Override
								protected void initChannel(Channel ch) throws Exception {
									ChannelPipeline pipeline = ch.pipeline();
									pipeline.addLast(new IdleStateHandler(heartbeat_time, 0, 0, TimeUnit.SECONDS));
									pipeline.addLast(new StringDecoder());
									pipeline.addLast(new ClientHandler());
									pipeline.addLast(new StringEncoder());
								}
							});

			ChannelFuture channelFuture = bootstrap.connect(new InetSocketAddress(HeartbeatServer.port)).sync();
			Channel channel = channelFuture.channel();
			channel.closeFuture().sync();
		} finally {
			eventLoopGroup.shutdownGracefully();
		}
	}

	private static class ClientHandler extends ChannelDuplexHandler {

		@Override
		public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
			if (evt instanceof IdleStateEvent) {
				System.out.println("preparing send heartbeat");
				ctx.channel().writeAndFlush("heartbeat!!!");
			}
		}

		@Override
		public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
			System.out.println("receiver server data: '" + msg + "'");
		}
	}
}
