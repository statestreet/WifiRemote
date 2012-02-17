package nio.readpage;

/**
 * CPSC 441 Computer Communications, Fall 2010
 * Assignment 1: Simple Proxy Server
 * 
 * Student Name: 
 * Student Number:
 *
 */

import java.net.*;
import java.io.*;
import java.util.*;
import java.nio.*;
import java.nio.channels.*;
import java.nio.charset.*;
import java.net.SocketAddress.*;

public class ProxyServer {

	// Port for the proxy
	private int port = 9999;

	// Socket for client connections
	private ServerSocket socket;
	private Selector selector;

	public ProxyServer(int myPort) throws Exception {
		// Initialize the proxy server and all related variables
		selector = Selector.open();
	}

	public void run() throws Exception {
		ServerSocketChannel channel;
		InetSocketAddress socketAddress;

		// Create the server socket channel and register with the selector
		try {
			socketAddress = new InetSocketAddress(port);
			channel = ServerSocketChannel.open();
			// Set channel be non-blocking I/O
			channel.configureBlocking(false);
			socket = channel.socket();
			socket.bind(socketAddress);
			// Interested in ACCEPT operations
			channel.register(selector, SelectionKey.OP_ACCEPT);

		} catch (IOException e) {
			System.out.println("Error creating server channel: " + e);
			System.exit(-1);
		}

		// Wait for something of interest to happen.Block for up to 500
		// milliseconds,
		// more or less, while waiting for a channel to become ready;
		while (selector.select(500) >= 0) {
			// Get a set of ready objects
			Set<SelectionKey> readyKeys = selector.selectedKeys();
			Iterator<SelectionKey> readyIterator = readyKeys.iterator();

			// Walk through set
			while (readyIterator.hasNext()) {
				SelectionKey key = readyIterator.next();
				readyIterator.remove();

				// For the server socket, get channel and accept the new
				// connection
				if (key.isAcceptable()) {
					// Get channel
					ServerSocketChannel keyChannel = (ServerSocketChannel) key
							.channel();

					// Get server socket
					ServerSocket serverSocket = keyChannel.socket();

					// Accept request
					Socket socket = serverSocket.accept();
					System.out.println("Accept conncection from "
							+ socket.toString());

					// Return canned message
					InputStream is = socket.getInputStream();
					FileOutputStream fs = new FileOutputStream("c:/socket.txt");
					int byteread = 0;
					byte[] buffer = new byte[1444];
					while ((byteread = is.read(buffer)) != -1) {
						fs.write(buffer, 0, byteread);
					}
					is.close();
					fs.close();
				} else {
					System.err.println("Ooops");
				}

				// For other sockets, connect and read from the socket

			}
		}
	}

	/*
	 * public void parseRequest(String data) {
	 * 
	 * // Find host header to know which server to contact in case the request
	 * // URL is not complete.
	 * 
	 * 
	 * 
	 * }
	 * 
	 * public void handleRequest(SocketChannel client) {
	 * 
	 * // Read request try {
	 * 
	 * 
	 * 
	 * 
	 * } catch (IOException e) {
	 * System.out.println("Error reading request from client: " + e); return; }
	 * 
	 * // Send request to server try {
	 * 
	 * 
	 * 
	 * 
	 * } catch (UnknownHostException e) { System.out.println("Unknown host: " +
	 * request.getHost()); System.out.println(e); return; } catch (IOException
	 * e) { System.out.println("Error writing request to server: " + e); return;
	 * } }
	 * 
	 * public void handleResponse(SocketChannel server) {
	 * 
	 * // Read response try {
	 * 
	 * 
	 * 
	 * 
	 * } catch (IOException e) {
	 * System.out.println("Error reading response from server: " + e); return; }
	 * 
	 * // Send response to client try {
	 * 
	 * 
	 * 
	 * 
	 * } catch (UnknownHostException e) { System.out.println("Unknown host: " +
	 * client.socket().toString()); System.out.println(e); return; } catch
	 * (IOException e) { System.out.println("Error writing response to client: "
	 * + e); return; } }
	 */

	public static void main(String args[]) throws Exception {
		int myPort = 0;

		// Get the port number from the command line arguments
		try {
			myPort = Integer.parseInt(args[0]);
		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("Need port number as argument");
			System.exit(-1);
		} catch (NumberFormatException e) {
			System.out.println("Please give port number as integer.");
			System.exit(-1);
		}

		ProxyServer pServer = new ProxyServer(myPort);
		pServer.run();
	}
}
