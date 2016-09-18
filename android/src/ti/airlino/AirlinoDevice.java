package ti.airlino;

import java.net.InetAddress;
import android.net.nsd.NsdServiceInfo;

public class AirlinoDevice {
	private String endpoint;

	/**
	 * @return the endpoint
	 */
	public String getEndpoint() {
		return endpoint;
	}

	/**
	 * @param endpoint
	 *            the endpoint to set
	 */
	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}

	private String host;

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	private String name;
	private int port;

	public static AirlinoDevice parseNsdServiceInfo(NsdServiceInfo so) {
		String endpoint = "http://";
		AirlinoDevice device = new AirlinoDevice();
		InetAddress address = so.getHost();
		if (address != null) {
			device.setHost(address.getHostAddress());
			endpoint += address.getHostAddress();
		}
		device.setPort(so.getPort());
		endpoint += (":" + so.getPort() + "/api/v15/radio.action");
		device.setName(so.getServiceName());
		device.setEndpoint(endpoint);
		return device;
	}

}
