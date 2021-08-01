package com.ooooo.netty.base;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import lombok.SneakyThrows;

import java.net.InetSocketAddress;

/**
 * simple Netty Client
 *
 * @author ooooo
 * @date 2021/08/01 13:59
 */
public class NettyClient {

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
									pipeline.addLast(new StringDecoder());
									pipeline.addLast(new StringClientHandler());
								}
							});


			ChannelFuture channelFuture = bootstrap.connect(new InetSocketAddress(NettyServer.port)).sync();
			channelFuture.channel().closeFuture().sync();
		} finally {
			eventLoopGroup.shutdownGracefully();
		}

	}

	private static class StringClientHandler extends SimpleChannelInboundHandler<String> {

		@Override
		public void channelActive(ChannelHandlerContext ctx) throws Exception {
			ctx.writeAndFlush("Hello Server");
		}

		@Override
		protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
			System.out.println("client receive data : {" + msg + "}");
		}
	}
}
