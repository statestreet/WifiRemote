package nio.readpage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

public class FileTest {

	public static int main() throws IOException{
		FileInputStream fi = new FileInputStream(new File("c:/firebug-1.7X.0b4.xpi"));
		FileChannel fcin = fi.getChannel();
		FileLock lock = fcin.lock();
		lock.release();
		return 0;
		
	}
}
