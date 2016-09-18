package de.appwerft.airlino;

import org.appcelerator.kroll.KrollDict;
import org.appcelerator.kroll.KrollProxy;
import org.appcelerator.kroll.annotations.Kroll;

// https://github.com/luis1987/PayPalAppcelerator/blob/master/android/src/com/bea/paypal/ModulopaypalModule.java
// example :https://github.com/paypal/PayPal-Android-SDK/blob/master/SampleApp/src/main/java/com/paypal/example/paypalandroidsdkexample/SampleActivity.java

@Kroll.proxy(creatableInModule = AirlinoModule.class)
public class AirlinoConnectionProxy extends KrollProxy {
	// Standard Debugging variables
	private static final String LCAT = "Airlino 💰💰";
	private String endpoint;

	public AirlinoConnectionProxy() {
		super();

	}

	@SuppressWarnings("unchecked")
	@Override
	public void handleCreationDict(KrollDict options) {
		super.handleCreationDict(options);
		if (options.containsKeyAndNotNull("endpoint")) {
			endpoint = options.getString("endpoint");
		}
	}

	@Kroll.method
	public void playRadio() {

	}

}