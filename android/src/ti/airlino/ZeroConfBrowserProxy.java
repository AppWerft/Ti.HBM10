package ti.airlino;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.appcelerator.kroll.KrollDict;
import org.appcelerator.kroll.KrollFunction;
import org.appcelerator.kroll.KrollProxy;
import org.appcelerator.kroll.annotations.Kroll;
import org.appcelerator.kroll.common.Log;
import org.appcelerator.titanium.TiApplication;

import android.app.Activity;
import android.content.Context;

import com.youview.tinydnssd.DiscoverResolver;
import com.youview.tinydnssd.MDNSDiscover;
import com.youview.tinydnssd.MDNSDiscover.Result;

@Kroll.proxy(creatableInModule = AirlinoModule.class)
public class ZeroConfBrowserProxy extends KrollProxy {

	private final class DiscoverResolverHandler implements
			DiscoverResolver.Listener {
		@Override
		public void onServicesChanged(Map<String, Result> services) {
			Log.d(LCAT, "onServicesChanged ======");
			KrollDict res = new KrollDict();
			List<KrollDict> list = new ArrayList<KrollDict>();
			for (MDNSDiscover.Result result : services.values()) {
				KrollDict kd = new KrollDict();
				KrollDict txt = new KrollDict();
				if (result.txt.dict != null) {
					for (Map.Entry<String, String> entry : result.txt.dict
							.entrySet()) {
						if (!entry.getKey().equals(""))
							txt.put(entry.getKey(), entry.getValue());
					}
				}
				kd.put("txt", txt);
				kd.put("ip", result.a.ipaddr);
				kd.put("port", result.srv.port);
				kd.put("target", result.srv.target);
				kd.put("fqdn", result.srv.fqdn);

				String[] parts = result.srv.fqdn.split("\\.");
				if (parts != null) {
					kd.put("name", parts[0]);
				} else
					Log.w(LCAT,
							"parts is not an array after splitting by '.' of "
									+ result.srv.fqdn);

				// ;

				kd.put("ttl", result.a.ttl);
				kd.put("dnstype", dnstype);

				list.add(kd);
				Log.d(LCAT, kd.toString());

			}
			res.put("devices", list.toArray());

			if (onChangeCallback != null)
				onChangeCallback.call(getKrollObject(), res);
		}
	}

	public static final String LCAT = AirlinoModule.LCAT;
	private Context ctx;

	private KrollFunction onChangeCallback;
	private KrollFunction onErrorCallback;
	String dnstype = "dockset";
	DiscoverResolver resolver;

	public ZeroConfBrowserProxy() {
		super();
		ctx = TiApplication.getInstance().getBaseContext();
		Log.d(LCAT, "BrowserProxy started");
	}

	@Override
	public void onPause(Activity a) {

		super.onPause(a);

	}

	@Override
	public void onResume(Activity a) {
		super.onResume(a);
		Log.d(LCAT, "BrowserProxy onResuke");

	}

	public void onDestroy(Activity a) {
		Log.d(LCAT, "BrowserProxy onDestroy");

		super.onDestroy(a);
	}

	@Kroll.method
	public void handleCreationDict(KrollDict opt) {

		if (opt.containsKeyAndNotNull("onchange")) {
			Object o = opt.get("onchange");
			if (o instanceof KrollFunction) {
				onChangeCallback = (KrollFunction) o;
			} else
				Log.w(LCAT, "onSuccessFn missed");
		} else
			Log.w(LCAT, "onSuccess missed");
		if (opt.containsKeyAndNotNull("onError")) {
			Object o = opt.get("onFound");
			if (o instanceof KrollFunction) {
				onErrorCallback = (KrollFunction) o;
			}
		}
		if (opt.containsKeyAndNotNull("dnstype")) {
			dnstype = opt.getString("dnstype");
		}

		resolver = new DiscoverResolver(ctx, "_" + dnstype + "._tcp.",
				new DiscoverResolverHandler());

	}

	@Kroll.method
	public void start() {
		if (resolver != null) {
			resolver.start();
		}
	}

	@Kroll.method
	public void stop() {
		if (resolver != null) {
			resolver.stop();
		}
	}
}
