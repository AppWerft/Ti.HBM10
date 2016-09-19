package de.appwerft.airlino;

import java.net.InetAddress;
import android.net.nsd.NsdServiceInfo;

public class AirlinoDevice {
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
		AirlinoDevice device = new AirlinoDevice();
		InetAddress address = so.getHost();
		if (address != null) {
			device.setHost(address.getHostAddress());
		}
		device.setPort(so.getPort());
		device.setName(so.getServiceName());
		return device;
	}

}
