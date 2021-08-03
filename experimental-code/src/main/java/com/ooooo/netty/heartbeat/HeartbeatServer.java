package com.ooooo.netty.heartbeat;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.SneakyThrows;

import java.net.InetSocketAddress;
import java.util.concurrent.atomic.AtomicInteger;

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
									pipeline.addLast(new IdleStateHandler(2, 0, 0));
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

		private AtomicInteger timeoutCnt = new AtomicInteger(0);


		@Override
		public void channelActive(ChannelHandlerContext ctx) throws Exception {
			timeoutCnt.set(0);
		}

		@Override
		public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
			System.out.println("client " + ctx.channel().remoteAddress() + " active");
		}

		@Override
		public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
			if (evt instanceof IdleStateEvent) {
				int cnt = timeoutCnt.incrementAndGet();
				if (cnt >= 3) {
					ctx.channel().close();
				}
			}
		}
	}
}
