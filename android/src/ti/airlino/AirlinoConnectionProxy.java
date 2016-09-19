package ti.airlino;

import java.io.UnsupportedEncodingException;

import org.appcelerator.kroll.KrollDict;
import org.appcelerator.kroll.KrollFunction;
import org.appcelerator.kroll.KrollProxy;
import org.appcelerator.kroll.annotations.Kroll;
import org.appcelerator.titanium.TiApplication;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import de.appwerft.airlino.AirlinoAdapter;

// https://github.com/luis1987/PayPalAppcelerator/blob/master/android/src/com/bea/paypal/ModulopaypalModule.java
// example :https://github.com/paypal/PayPal-Android-SDK/blob/master/SampleApp/src/main/java/com/paypal/example/paypalandroidsdkexample/SampleActivity.java

@Kroll.proxy(creatableInModule = AirlinoModule.class)
public class AirlinoConnectionProxy extends KrollProxy {
	// Standard Debugging variables
	private static final String LCAT = "Airlino 👑👑";
	private String host;
	final int DEFAULTPORT = 4949;
	private int port = DEFAULTPORT;

	Context ctx;
	AirlinoAdapter adapter;

	public AirlinoConnectionProxy() {
		super();
		ctx = TiApplication.getInstance();
		adapter = new AirlinoAdapter(ctx, host, port);
	}

	@Override
	public void handleCreationDict(KrollDict options) {
		super.handleCreationDict(options);
		if (options.containsKeyAndNotNull("host")) {
			host = options.getString("host");
		}
		if (options.containsKeyAndNotNull("port")) {
			port = options.getInt("port");
		}
	}

	@Kroll.method
	public void playRadio(KrollDict options)
			throws UnsupportedEncodingException, JSONException {
		String url = "http://";
		String name = "";
		KrollFunction cb = getCallback(options);
		adapter.playRadio(url, name, new resultHandler(cb));
	}

	private final class resultHandler implements AirlinoAdapter.onResultHandler {
		private KrollFunction resultCallback;

		private resultHandler(KrollFunction cb) {
			this.resultCallback = cb;
		}

		@Override
		public void onResult(JSONObject result) {
			if (resultCallback != null) {
				KrollDict dict;
				try {
					dict = new KrollDict(result);
					resultCallback.call(getKrollObject(), dict);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private KrollFunction getCallback(KrollDict options) {
		if (options.containsKeyAndNotNull("onResult")) {
			Object cb;
			cb = options.get("onResult");
			if (cb instanceof KrollFunction) {
				return (KrollFunction) cb;
			}
		}
		return null;
	}
}