package multicast;

import java.io.*;
import java.net.*;
import java.util.*;

public class MulticastServerThread extends Thread {
	
	private final long TWO_SECONDS = 2000;
	private final String STOCKS_FILE = "stocks.txt";
	
	private final String MCAST_IP = "230.0.0.111";
	private final int CLIENT_SOCKET_PORT = 1111;
	private final int SERVER_SOCKET_PORT = 11111;
	
	private DatagramSocket socket;
	private BufferedReader in;
	private boolean moreStocks;
	
	public MulticastServerThread() throws IOException {
		socket = null;
		in     = null;
		moreStocks = true;
		
		socket = new DatagramSocket(SERVER_SOCKET_PORT);
		try {
			in = new BufferedReader(new FileReader(STOCKS_FILE));
		} catch (FileNotFoundException e) {
			System.err.println("Could not open file");
		}
	}
	
	private String getNextStock() {
		String returnValue = null;
		try {
			if ((returnValue = in.readLine()) == null) {
				in.close();
				moreStocks = false;
				returnValue = "No more stocks. Goodbye.";
			}
		} catch (IOException e) {
			returnValue = "IOException occured in server.";
		}
		return returnValue;
	}
	
	public void run() {
		System.out.println("Server started...");
		
		while (moreStocks) {
			try {
				byte[] buf = new byte[256];
				
				buf = getNextStock().getBytes();
				
				//send it
				InetAddress mcastIP = InetAddress.getByName(MCAST_IP);
				DatagramPacket packet =
						new DatagramPacket(buf, buf.length,
								mcastIP, CLIENT_SOCKET_PORT);
				socket.send(packet);
				
				//sleep for a while
				try {
					Thread.sleep(TWO_SECONDS);
				} catch (InterruptedException e) { }
				
			} catch (IOException e) {
				e.printStackTrace();
				moreStocks = false;
			}
		}
		socket.close();
	}
}
