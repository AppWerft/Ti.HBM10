package ti.airlino;

import java.io.*;
import java.net.*;

public class MSearchSDDPRequest {

	/*
	 * multicast SSDP M-SEARCH
	 */

	public static void mSearch(String host, int port) throws IOException {
		/* create byte arrays to hold our send and response data */
		byte[] sendData = new byte[1024];
		byte[] receiveData = new byte[1024];
		/* our M-SEARCH data as a byte array */
		String MSEARCH = "M-SEARCH * HTTP/1.1\nHost: " + host + ":" + port
				+ "\nMan: \"ssdp:discover\"\nST: roku:ecp\n";
		sendData = MSEARCH.getBytes();
		/* create a packet from our data destined for 239.255.255.250:1900 */
		DatagramPacket sendPacket = null;
		try {
			sendPacket = new DatagramPacket(sendData, sendData.length,
					InetAddress.getByName(host), port);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/* send packet to the socket we're creating */
		DatagramSocket clientSocket = null;
		try {
			clientSocket = new DatagramSocket();
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		clientSocket.send(sendPacket);
		/* recieve response and store in our receivePacket */
		DatagramPacket receivePacket = new DatagramPacket(receiveData,
				receiveData.length);
		clientSocket.receive(receivePacket);
		/* get the response as a string */
		String response = new String(receivePacket.getData());
		/* print the response */
		System.out.println(response);
		/* close the socket */
		clientSocket.close();
	}
}
