package nio.udp;
import  java.net.InetSocketAddress; 
import  java.net.SocketAddress; 
import  java.nio.ByteBuffer; 
import  java.nio.channels.DatagramChannel; 
import  java.nio.channels.SelectionKey; 
import  java.nio.channels.Selector; 
import  java.nio.charset.Charset; 
import  java.util.Iterator; 
import  java.util.Set; 

public class  UDPClient  extends  Thread  { 
     public  void  run () { 
         DatagramChannel channel =  null ; 
         Selector selector =  null ; 
         try  { 
             channel = DatagramChannel.open () ; 
             channel.configureBlocking ( false ) ; 
             SocketAddress sa =  new  InetSocketAddress ( "localhost" ,  5057 ) ; 
             channel.connect ( sa ) ; 
         }  catch  ( Exception e ) { 
             e.printStackTrace () ; 
         } 

         try  { 
             selector = Selector.open () ; 
             channel.register ( selector, SelectionKey.OP_READ ) ; 
             channel.write ( Charset.defaultCharset () .encode ( "Tell me your time" )) ; 
         }  catch  ( Exception e ) { 
             e.printStackTrace () ; 
         } 
         
         ByteBuffer byteBuffer = ByteBuffer.allocate ( 100 ) ; 
         while  ( true ) { 
             try  { 
                 int  eventsCount = selector.select () ; 
                 if  ( eventsCount >  0 ) { 
                     Set selectedKeys = selector.selectedKeys () ; 
                     Iterator iterator = selectedKeys.iterator () ; 
                     while  ( iterator.hasNext ()) { 
                         SelectionKey sk =  ( SelectionKey )  iterator.next () ; 
                         iterator.remove () ; 
                         if  ( sk.isReadable ()) { 
                             DatagramChannel datagramChannel =  ( DatagramChannel )  sk 
                                     .channel () ; 
                             datagramChannel.read ( byteBuffer ) ; 
                             byteBuffer.flip () ; 
                             
                             //TODO 将报文转化为RUDP消息并调用RUDP协议处理器来处理 
                             
                             System.out.println ( Charset.defaultCharset () .decode ( 
                                     byteBuffer ) .toString ()) ; 
                             byteBuffer.clear () ; 
                             datagramChannel.write ( Charset.defaultCharset () 
                                     .encode ( "Tell me your time" )) ; 
                         } 
                     } 
                 } 
             }  catch  ( Exception e ) { 
                 e.printStackTrace () ; 
             } 
         } 

     } 
}