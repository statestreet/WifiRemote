package nio.readpage;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Socket;

public class SocksSender {
	
	public static void main(String[] args) throws IOException{
		Socket socket=new Socket(new Proxy(Proxy.Type.SOCKS, new InetSocketAddress("10.248.65.126", 9999)));
		StringBuffer sb = new StringBuffer("GET\" HTTP/1.1");
		sb.append("\r\n");
		sb.append("Host: www.baidu.com");
		sb.append("\r\n");
		sb
		.append("User-Agent: Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:1.9.1.8) Gecko/20100202 Firefox/3.5.8 GTB6");
		sb.append("\r\n");
		sb
		.append("Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		sb.append("\r\n");
		sb.append("Accept-Language: zh,en;q=0.8,zh-cn;q=0.5,en-us;q=0.3");
		sb.append("\r\n");
		sb.append("Accept-Encoding: gzip,deflate");
		sb.append("\r\n");
		sb.append("Accept-Charset: ISO-8859-1,utf-8;q=0.7,*;q=0.7");
		sb.append("\r\n");
		sb.append("\r\n");
		sb.append("\r\n");
		
		OutputStream socketOut = socket.getOutputStream(); //debug运行到此时说socket没连接
		socketOut.write(sb.toString().getBytes());
		socket.shutdownOutput();
	}
	
}

