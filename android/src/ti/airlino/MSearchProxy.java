package ti.airlino;

import java.io.*;
import java.net.*;

import org.appcelerator.kroll.KrollProxy;
import org.appcelerator.kroll.annotations.Kroll;
import org.appcelerator.kroll.common.Log;

import android.os.AsyncTask;

@Kroll.proxy(creatableInModule = AirlinoModule.class)
public class MSearchProxy extends KrollProxy {
	final static int DEFAULTPORT = 1900;
	final static String MULTICASTHOST = "239.255.255.250";
	final static String LF = "\n";

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
			/* create byte arrays to hold our send and response data */
			byte[] sendData = new byte[1024];
			byte[] receiveData = new byte[1024];
			/* our M-SEARCH data as a byte array */
			String MSEARCH = "M-SEARCH * HTTP/1.1" + LF //
					+ "HOST:" + MULTICASTHOST + ":" + DEFAULTPORT + LF //
					+ "Man: \"ssdp:discover\"" + LF//
					+ "ST: urn:schemas-upnp-org:device:ZonePlayer:1" + LF;
			sendData = MSEARCH.getBytes();
			/*
			 * create a packet from our data destined for 239.255.255.250:1900
			 */
			DatagramPacket sendPacket = null;
			try {
				sendPacket = new DatagramPacket(sendData, sendData.length,
						InetAddress.getByName(MULTICASTHOST), DEFAULTPORT);
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
