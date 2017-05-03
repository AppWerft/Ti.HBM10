package ti.airlino;

import java.io.*;
import java.net.*;

import org.appcelerator.kroll.KrollProxy;
import org.appcelerator.kroll.annotations.Kroll;
import org.appcelerator.kroll.common.Log;

import android.os.AsyncTask;

@Kroll.proxy(creatableInModule = AirlinoModule.class)
public class MSearchProxy extends KrollProxy {
	final int SSDP_PORT = 1900;
	final int SSDP_SEARCH_PORT = 1901;
	// Broadcast address for finding routers.
	final String SSDP_IP = "239.255.255.250";
	// Time out of the connection.
	int TIMEOUT = 5000;
	final static String SEP = "\n\n";

	/*
	 * multicast SSDP M-SEARCH
	 */
	// http://192.168.1.151:49152/upmpd/icon.png
	@Kroll.method
	public void start() {
		(new doMSearchRequest()).execute();

	}

	private class doMSearchRequest extends AsyncTask<Void, Void, String> {
		@Override
		protected String doInBackground(Void... params) {
			try {
				InetAddress localhost = InetAddress.getLocalHost();
			} catch (UnknownHostException e1) {
				e1.printStackTrace();
			}
			/* create byte arrays to hold our send and response data */
			byte[] sendData = new byte[1024];
			byte[] receiveData = new byte[1024];
			/* our M-SEARCH data as a byte array */
			String MSEARCH = "M-SEARCH * HTTP/1.1" + SEP //
					+ "HOST:" + SSDP_IP + ":" + SSDP_PORT + SEP //
					+ "Man: \"ssdp:discover\"" + SEP//
					+ "ST: urn:schemas-upnp-org:device:ZonePlayer:1" + SEP;
			sendData = MSEARCH.getBytes();
			/*
			 * create a packet from our data destined for 239.255.255.250:1900
			 */
			DatagramPacket sendPacket = null;
			try {
				sendPacket = new DatagramPacket(sendData, sendData.length,
						InetAddress.getByName(SSDP_IP), SSDP_PORT);
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
			/* send packet to the socket we're creating */
			DatagramSocket clientSocket = null;
			try {
				clientSocket = new DatagramSocket();
			} catch (SocketException e) {
				e.printStackTrace();
			}
			try {
				clientSocket.send(sendPacket);
			} catch (IOException e) {
				e.printStackTrace();
			}
			/* recieve response and store in our receivePacket */
			DatagramPacket receivePacket = new DatagramPacket(receiveData,
					receiveData.length);
			try {
				clientSocket.receive(receivePacket);
			} catch (IOException e) {
				e.printStackTrace();
			}
			/* get the response as a string */
			String response = new String(receivePacket.getData());
			/* print the response */
			/* close the socket */
			clientSocket.close();
			return response;

		}

		@Override
		protected void onPostExecute(String res) {
			Log.d("MSearch", res);
		}
	}
}
