package multicast;

import java.io.*;
import java.net.*;
import java.util.*;

public class MulticastClient {
	
	private static final String MCAST_IP = "230.0.0.111";
	private static final int CLIENT_SOCKET_PORT = 1111;

	public static void main(String[] args) throws IOException {
		
		// create a multicast socket
		MulticastSocket socket = new MulticastSocket(CLIENT_SOCKET_PORT);
		InetAddress address = InetAddress.getByName(MCAST_IP);
		
		// join group
		socket.joinGroup(address);
		
		DatagramPacket packet;
		
		//get stocks
		for (int i = 0; i < 100; i++) {
			
			byte[] buf = new byte[256];
			packet = new DatagramPacket(buf, buf.length);
			socket.receive(packet);
			
			String received = new String(packet.getData(), 0, packet.getLength());
			System.out.println("Stock: " + received);
		}
		
		//leave group
		socket.leaveGroup(address);
		socket.close();
	}

}
