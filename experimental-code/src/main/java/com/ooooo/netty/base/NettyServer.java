package com.ooooo.netty.base;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import lombok.SneakyThrows;

import java.net.InetSocketAddress;

/**
 * simple Netty Server
 *
 * @author ooooo
 * @date 2021/08/01 21:10
 */
public class NettyServer {

	public static final int port = 9999;


	@SneakyThrows
	public static void main(String[] args) {
		EventLoopGroup bossEventLoopGroup = new NioEventLoopGroup(1);
		EventLoopGroup workEventLoopGroup = new NioEventLoopGroup(1);

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
									pipeline.addLast(new StringServerHandler());
								}
							});

			ChannelFuture channelFuture = serverBootstrap.bind(new InetSocketAddress(port)).sync();
			channelFuture.channel().closeFuture().sync();
		} finally {
			bossEventLoopGroup.shutdownGracefully();
			workEventLoopGroup.shutdownGracefully();
		}

	}

	private static class StringServerHandler extends SimpleChannelInboundHandler<String> {

		@Override
		protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
			System.out.println("server receive data : {" + msg + "}");
			ctx.writeAndFlush("Hello Client");
		}
	}
}
