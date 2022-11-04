package com.ooooo.netty.heartbeat;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;
import lombok.SneakyThrows;

/**
 * simple heartbeat server
 *
 * @author ooooo
 * @date 2021/08/03 20:58
 */
public class HeartbeatServer {

	public static final int port = 8002;

	@SneakyThrows
	public static void main(String[] args) {
		EventLoopGroup bossEventLoopGroup = new NioEventLoopGroup(1);
		EventLoopGroup workEventLoopGroup = new NioEventLoopGroup();

		try {
			ServerBootstrap serverBootstrap = new ServerBootstrap();
			serverBootstrap.group(bossEventLoopGroup, workEventLoopGroup)
							.channel(NioServerSocketChannel.class)
							.option(ChannelOption.SO_BACKLOG, 1024)
							.childHandler(new ChannelInitializer<Channel>() {
								@Override
								protected void initChannel(Channel ch) throws Exception {
									ChannelPipeline pipeline = ch.pipeline();
									pipeline.addLast(new IdleStateHandler(0, 0, HeartbeatClient.heartbeat_time * 3, TimeUnit.SECONDS));
									pipeline.addLast(new StringDecoder());
									pipeline.addLast(new HeartbeatCheckHandler());
								}
							});

			ChannelFuture channelFuture = serverBootstrap.bind(new InetSocketAddress(port)).sync();
			channelFuture.channel().closeFuture().sync();
		} finally {
			bossEventLoopGroup.shutdownGracefully();
			workEventLoopGroup.shutdownGracefully();
		}
	}


	private static class HeartbeatCheckHandler extends ChannelDuplexHandler {


		@Override
		public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
			System.out.println("server receive from client[" + ctx.channel().remoteAddress() + "], data: '" + msg + "'");
		}

		@Override
		public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
			if (evt instanceof IdleStateEvent) {
				System.out.println("close channel ");
				ctx.channel().close();
			}
		}
	}
}
