package nio.udp;

import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;

public class UDPServer extends Thread {
	public void run() {
		Selector selector = null;
		try {
			DatagramChannel channel = DatagramChannel.open();
			DatagramSocket socket = channel.socket();
			channel.configureBlocking(false);
			socket.bind(new InetSocketAddress(55555));

			selector = Selector.open();
			channel.register(selector, SelectionKey.OP_READ);
		} catch (Exception e) {
			e.printStackTrace();
		}

		ByteBuffer byteBuffer = ByteBuffer.allocate(65536);
		while (true) {
			try {
				int eventsCount = selector.select();
				if (eventsCount > 0) {
					Set selectedKeys = selector.selectedKeys();
					Iterator iterator = selectedKeys.iterator();
					while (iterator.hasNext()) {
						SelectionKey sk = (SelectionKey) iterator.next();
						iterator.remove();
						if (sk.isReadable()) {
							DatagramChannel datagramChannel = (DatagramChannel) sk
									.channel();
							SocketAddress sa = datagramChannel
									.receive(byteBuffer);
							byteBuffer.flip();

							// 测试：通过将收到的ByteBuffer首先通过缺省的编码解码成CharBuffer 再输出
							CharBuffer charBuffer = Charset.defaultCharset()
									.decode(byteBuffer);
							System.out.println("receive message:"
									+ charBuffer.toString());
							byteBuffer.clear();

							String echo = "This is the reply message from 服务器。";
							ByteBuffer buffer = Charset.defaultCharset()
									.encode(echo);
							datagramChannel.write(buffer);
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	public static void main(String[] args) {
		new UDPServer().start();
	}
}