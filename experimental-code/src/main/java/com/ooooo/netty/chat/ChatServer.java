package com.ooooo.netty.chat;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.SneakyThrows;

import java.net.InetSocketAddress;

/**
 * Simple Chat Server
 *
 * @author ooooo
 * @date 2021/08/02 21:28
 */
public class ChatServer {

	public static final int port = 9991;

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
									pipeline.addLast(new StringDecoder());
									pipeline.addLast(new StringEncoder());
									pipeline.addLast(new ServerHandler());
								}
							});

			ChannelFuture channelFuture = serverBootstrap.bind(new InetSocketAddress(port)).sync();
			channelFuture.channel().closeFuture().sync();
		} finally {
			bossEventLoopGroup.shutdownGracefully();
			workEventLoopGroup.shutdownGracefully();
		}
	}

	@Sharable
	private static class ServerHandler extends ChannelDuplexHandler {

		private static final ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

		@Override
		public void channelInactive(ChannelHandlerContext ctx) throws Exception {
			Channel channel = ctx.channel();
			System.out.println("client " + channel.remoteAddress() + " has exited");
			channels.removeIf(c -> c == channel);
		}


		@Override
		public void channelActive(ChannelHandlerContext ctx) throws Exception {
			Channel channel = ctx.channel();
			System.out.println("client " + channel.remoteAddress() + " has joined");
			channels.add(channel);
		}

		@Override
		public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
			Channel curChannel = ctx.channel();
			for (Channel channel : channels) {
				if (curChannel != channel) {
					channel.writeAndFlush(msg);
				}
			}
		}


	}


}
