package com.ooooo.nio;

import lombok.SneakyThrows;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

/**
 * NIO client
 *
 * @author ooooo
 * @date 2021/07/31 18:46
 */
public class NIOClient {


	@SneakyThrows
	public static void main(String[] args) {
		SocketChannel socketChannel = SocketChannel.open();
		socketChannel.configureBlocking(false);
		socketChannel.connect(new InetSocketAddress(NIOServer.port));

		Selector selector = Selector.open();
		socketChannel.register(selector, SelectionKey.OP_CONNECT);

		while (true) {
			selector.select();
			Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();
			while (keyIterator.hasNext()) {
				SelectionKey key = keyIterator.next();
				keyIterator.remove();
				handle(key);
			}
		}
	}

	@SneakyThrows
	private static void handle(SelectionKey key) {
		if (key.isConnectable()) {
			System.out.println("server receive [connect] event");
			SocketChannel socketChannel = (SocketChannel) key.channel();
			if (socketChannel.isConnectionPending()) {
				socketChannel.finishConnect();
			}
			ByteBuffer byteBufferToWrite = ByteBuffer.wrap("Hello Server".getBytes(StandardCharsets.UTF_8));
			socketChannel.write(byteBufferToWrite);
			socketChannel.register(key.selector(), SelectionKey.OP_READ);
		} else if (key.isReadable()) {
			System.out.println("server receive [read] event");
			SocketChannel channel = (SocketChannel) key.channel();
			ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
			int read = channel.read(byteBuffer);
			if (read != -1) {
				System.out.println("client read data: {" + new String(byteBuffer.array(), 0, read) + "}");
			}
		}
	}
}
