package de.appwerft.airlino;

import java.util.ArrayList;

@SuppressWarnings({ "serial" })
public class AirlinoDevices extends ArrayList<AirlinoDevice> {

	public AirlinoDevices() {
	}

	@Override
	public boolean add(AirlinoDevice device) {
		this.remove(device);
		return super.add(device);
	}

	public Object[] getAllDevices() {
		return this.toArray();
	}

}
