package com.ooooo.netty.heartbeat;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import lombok.SneakyThrows;

import java.net.InetSocketAddress;
import java.util.Random;

/**
 * simple heartbeat client
 * @author ooooo
 * @date 2021/08/03 20:56
 */
public class HeartbeatClient {


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
									pipeline.addLast(new StringEncoder());
								}
							});

			ChannelFuture channelFuture = bootstrap.connect(new InetSocketAddress(HeartbeatServer.port)).sync();
			Channel channel = channelFuture.channel();

			while (channel.isActive()) {
				Random random = new Random();
				Thread.sleep(random.nextInt(10) * 1000);
				channel.writeAndFlush("heartbeat");
			}
		} finally {
			eventLoopGroup.shutdownGracefully();
		}
	}
}
