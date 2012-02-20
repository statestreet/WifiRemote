package nio.readpage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

public class ProxyReader {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("proxy.statestr.com", 80));
		URL url = new URL("http://www.yahoo.com");
		HttpURLConnection uc = (HttpURLConnection)url.openConnection(proxy);
		uc.connect();
		BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream()));
		 FileOutputStream fout = new FileOutputStream(new File("c:/yahoo.html"));  
	        // 获取输入输出通道  
	        FileChannel fcout = fout.getChannel();  
	        // 创建缓冲区  
	        ByteBuffer byteBuffer = ByteBuffer.allocate(1024000);  
	        CharBuffer charBuffer = Charset.forName("UTF-8")
					.decode(byteBuffer);
	        while (true) {  
	            // clear方法重设缓冲区，使它可以接受读入的数据  
	        	byteBuffer.clear();  
	            // 从输入通道中将数据读到缓冲区  
	            int r = in.read(charBuffer);  
	            // read方法返回读取的字节数，可能为零，如果该通道已到达流的末尾，则返回-1  
	            if (r == -1) {  
	                break;  
	            }  
	            // flip方法让缓冲区可以将新读入的数据写入另一个通道  
	            byteBuffer.flip();  
	            // 从输出通道中将数据写入缓冲区  
	            fcout.write(byteBuffer);  
	        }  

	}

}
