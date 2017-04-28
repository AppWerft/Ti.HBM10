package de.appwerft.airlino;

import java.util.ArrayList;
import java.util.List;

import org.appcelerator.kroll.KrollDict;

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
		List<KrollDict> list = new ArrayList<KrollDict>();
		for (AirlinoDevice d : this) {
			KrollDict kd = new KrollDict();
			kd.put("port", d.getPort());
			kd.put("name", d.getName());
			kd.put("host", d.getHost());
			list.add(kd);
		}
		return list.toArray();
	}

}
