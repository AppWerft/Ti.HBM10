package de.appwerft.airlino;

import java.net.InetAddress;

import org.appcelerator.kroll.KrollDict;
import org.appcelerator.kroll.common.Log;

import ti.airlino.AirlinoModule;
import android.net.nsd.NsdServiceInfo;

public class AirlinoDevice {
	public static String LCAT = AirlinoModule.LCAT;
	private String host;
	private String serviceName;
	private String name;
	private int port;

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

	public static AirlinoDevice parseNsdServiceInfo(NsdServiceInfo serviceInfo) {
		AirlinoDevice airlinoDevice = new AirlinoDevice();
		Log.d(LCAT, serviceInfo.toString());
		airlinoDevice.serviceName = serviceInfo.getServiceName();
		InetAddress address = serviceInfo.getHost();
		if (address != null) {
			airlinoDevice.setHost(address.getHostAddress());
		}
		airlinoDevice.setPort(serviceInfo.getPort());
		airlinoDevice.setName(serviceInfo.getServiceName());
		return airlinoDevice;
	}
}
