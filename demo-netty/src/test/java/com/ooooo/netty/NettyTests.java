package com.ooooo.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.FixedLengthFrameDecoder;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LineBasedFrameDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @since 2021/4/5 21:41
 */
public class NettyTests {
	
	
	/**
	 * 测试 LineBasedFrameDecoder (以分隔符'\n'或者'\r\n')
	 */
	@Test
	public void testLineBasedFrameDecoder() {
		EmbeddedChannel channel = new EmbeddedChannel();
		channel.pipeline()
		       .addLast(new LineBasedFrameDecoder(Integer.MAX_VALUE))
		       .addLast(new ByteToMessageDecoder() {
			       @Override
			       protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
				       int readableBytes = in.readableBytes();
				       byte[] buf = new byte[readableBytes];
				       in.readBytes(buf);
				       String msg = new String(buf, StandardCharsets.UTF_8);
				       //System.out.println(msg);
				       out.add(msg);
			       }
		       });
		
		int n = 10;
		ByteBuf buffer = Unpooled.buffer();
		for (int i = 0; i < n; i++) {
			buffer.writeBytes(String.format("第%d条消息\n", i).getBytes(StandardCharsets.UTF_8));
		}
		channel.writeInbound(buffer);
		channel.finish();
		
		while (true) {
			Object o = channel.readInbound();
			if (o == null) {
				break;
			}
			System.out.println(o);
		}
		
	}
	
	
	/**
	 * 测试 FixedLengthFrameDecoder (以固定长度)
	 */
	@Test
	public void testFixedLengthFrameDecoder() {
		EmbeddedChannel channel = new EmbeddedChannel();
		channel.pipeline()
		       .addLast(new FixedLengthFrameDecoder(6))
		       .addLast(new ByteToMessageDecoder() {
			       @Override
			       protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
				       int readableBytes = in.readableBytes();
				       byte[] buf = new byte[readableBytes];
				       in.readBytes(buf);
				       String msg = new String(buf, StandardCharsets.UTF_8);
				       //System.out.println(msg);
				       out.add(msg);
			       }
		       });
		
		int n = 26;
		ByteBuf buffer = Unpooled.buffer();
		for (int i = 0; i < n; i++) {
			// java 中采用unicode字符，char 占用两个字节
			buffer.writeChar('A' + i);
		}
		channel.writeInbound(buffer);
		channel.finish();
		
		while (true) {
			Object o = channel.readInbound();
			if (o == null) {
				break;
			}
			System.out.println(o);
		}
		
	}
	
	
	/**
	 * 测试 LengthFieldBasedFrameDecoder (变长)
	 *  这个解码器，用的地方不多
	 * @note lengthAdjustment
	 * <br>
	 * lengthFieldLength中的大小 + lengthAdjustment 等于最终读的字节数大小
	 * <br>
	 * lengthAdjustment 如果是负数，也就是少读几个字节，如果是正数，也就是多读几个字节。
	 */
	@Test
	public void testLengthFieldBasedFrameDecoder() {
		byte[] header = "header msg".getBytes(StandardCharsets.UTF_8); // 放在最前面，都是固定长度
		byte[] body = "body msg".getBytes(StandardCharsets.UTF_8);
		ByteBuf buffer = Unpooled.buffer();
		buffer.writeBytes(header);
		buffer.writeShort(body.length);
		buffer.writeBytes(body);
		
		EmbeddedChannel channel = new EmbeddedChannel();
		channel.pipeline()
		       //.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, header.length, 2, 0, header.length + 2)) // header.length + 2 等价于 header.length， 不会管长度域的长度
		       .addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, header.length, 2, 0, header.length)) // 读取到的字节是 header + body
		       .addLast(new ByteToMessageDecoder() {
			       @Override
			       protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
				       int readableBytes = in.readableBytes();
				       byte[] buf = new byte[readableBytes];
				       in.readBytes(buf);
				       String msg = new String(buf, StandardCharsets.UTF_8);
				       //System.out.println(msg);
				       out.add(msg);
			       }
		       });
		
		channel.writeInbound(buffer);
		channel.finish();
		
		while (true) {
			Object o = channel.readInbound();
			if (o == null) {
				break;
			}
			System.out.println(o);
		}
	}
	
	
}
