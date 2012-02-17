package nio.readpage;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.net.InetSocketAddress;
import java.util.Properties;
import java.io.IOException;

public class BaiduReader {
	private Charset charset = Charset.forName("GBK");// 创建GBK字符集
	private SocketChannel channel;

	public void readHTMLContent() {
		try {
//			String proxyHost = "localhost";     
//			String proxyPort = "9999";     
//			System.getProperties().put("socksProxySet","true");     
//			System.getProperties().put("socksProxyHost",proxyHost);     
//			System.getProperties().put("socksProxyPort",proxyPort); 

			InetSocketAddress socketAddress = new InetSocketAddress(
					"localhost", 9999);
			// step1:打开连接
			channel = SocketChannel.open(socketAddress);
			channel.configureBlocking (false); // nonblocking 
			println(channel.isBlocking());
			// step2:发送请求，使用GBK编码
			channel.write(charset.encode("GET " + "/ HTTP/1.1" + "\r\n\r\n"));
			// step3:读取数据
			ByteBuffer buffer = ByteBuffer.allocate(1024);// 创建1024字节的缓冲
			while (channel.read(buffer) != -1) {
				buffer.flip();// flip方法在读缓冲区字节操作之前调用。
				System.out.println(charset.decode(buffer));
				// 使用Charset.decode方法将字节转换为字符串
				buffer.clear();// 清空缓冲
			}
		} catch (IOException e) {
			System.err.println(e.toString());
		} finally {
			if (channel != null) {
				try {
					channel.close();
				} catch (IOException e) {
				}
			}
		}
	}

	public static void println(Object s){
		System.out.println(s);
	}
	public static void main(String[] args) {
		new BaiduReader().readHTMLContent();
	}
}