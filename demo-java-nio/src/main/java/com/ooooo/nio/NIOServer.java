package com.ooooo.nio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import lombok.SneakyThrows;

/**
 * NIO Server
 *
 * @author ooooo
 * @date 2021/07/31 18:58
 */
public class NIOServer {

	public static final int port = 9009;

	@SneakyThrows
	public static void main(String[] args) {
		ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
		serverSocketChannel.socket().bind(new InetSocketAddress(port));
		serverSocketChannel.configureBlocking(false);

		Selector selector = Selector.open();
		serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

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
		if (key.isAcceptable()) {
			System.out.println("server receive [accept] event");
			// because of serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
			ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
			// accept client socketChannel
			SocketChannel socketChannel = serverSocketChannel.accept();
			socketChannel.configureBlocking(false);
			socketChannel.register(key.selector(), SelectionKey.OP_READ);
		} else if (key.isReadable()) {
			System.out.println("server receive [read] event");
			SocketChannel socketChannel = (SocketChannel) key.channel();
			ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
			int read = socketChannel.read(byteBuffer);
			if (read != -1) {
				System.out.println("server read data: {" + new String(byteBuffer.array(), 0, read) + "}");
			}
			ByteBuffer byteBufferToWrite = ByteBuffer.wrap("Hello Client !!!".getBytes(StandardCharsets.UTF_8));
			socketChannel.write(byteBufferToWrite);
			socketChannel.register(key.selector(), SelectionKey.OP_WRITE | SelectionKey.OP_READ);
		} else if (key.isWritable()) {
			SocketChannel socketChannel = (SocketChannel) key.channel();
			System.out.println("server receive [write] event");
			socketChannel.register(key.selector(), SelectionKey.OP_READ);
		}
	}
}
