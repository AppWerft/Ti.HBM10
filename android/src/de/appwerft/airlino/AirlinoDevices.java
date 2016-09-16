package de.appwerft.airlino;

import java.util.ArrayList;

public class AirlinoDevices extends ArrayList<AirlinoDevice> {

	private static final long serialVersionUID = 1L;

	public AirlinoDevices() {

	}

	public void addDevice(AirlinoDevice device) {
		removeDevice(device);
		this.add(device);
	}

	public void removeDevice(AirlinoDevice olddevice) {
		for (AirlinoDevice device : this) {
			if (device.getHost().equals(olddevice.getHost()))
				this.remove(device);
		}

	}

	public AirlinoDevices listDevices() {
		return this;
	}

	public boolean isInArray(String host) {
		boolean found = false;
		for (AirlinoDevice device : this) {
			if (device.getHost().equals(host))
				found = true;
		}
		return found;
	}
}
