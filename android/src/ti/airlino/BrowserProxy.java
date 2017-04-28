package ti.airlino;

import java.util.Map;

import org.appcelerator.kroll.KrollDict;
import org.appcelerator.kroll.KrollFunction;
import org.appcelerator.kroll.KrollProxy;
import org.appcelerator.kroll.annotations.Kroll;
import org.appcelerator.kroll.common.Log;
import org.appcelerator.titanium.TiApplication;

import com.youview.tinydnssd.DiscoverResolver;
import com.youview.tinydnssd.MDNSDiscover;
import com.youview.tinydnssd.MDNSDiscover.Result;

import de.appwerft.airlino.*;
import android.app.Activity;
import android.content.Context;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;

@Kroll.proxy(creatableInModule = AirlinoModule.class)
public class BrowserProxy extends KrollProxy {

	private final class NsdManagerDiscoveryHandler implements
			NsdManager.DiscoveryListener {
		@Override
		public void onDiscoveryStarted(String regType) {
			Log.d(LCAT, "Service discovery started " + regType);

		}

		/* a new device is found */
		@Override
		public void onServiceFound(NsdServiceInfo service) {
			Log.d(LCAT, "onServiceFound");
			nsdManager.resolveService(service, new NsdManagerResolveHandler());
		}

		/* a device is lost */
		@Override
		public void onServiceLost(NsdServiceInfo service) {
		}

		@Override
		public void onDiscoveryStopped(String serviceType) {
			Log.i(LCAT, "Discovery stopped: " + serviceType);
		}

		@Override
		public void onStartDiscoveryFailed(String serviceType, int errorCode) {
			Log.e(LCAT, "Discovery failed: Error code:" + errorCode);
			nsdManager.stopServiceDiscovery(this);
		}

		@Override
		public void onStopDiscoveryFailed(String serviceType, int errorCode) {
			Log.e(LCAT, "Discovery failed: Error code:" + errorCode);
			nsdManager.stopServiceDiscovery(this);
		}
	}

	private final class NsdManagerResolveHandler implements
			NsdManager.ResolveListener {
		@Override
		public void onResolveFailed(NsdServiceInfo serviceInfo, int errorCode) {
			Log.e(LCAT, "Resolve failed" + errorCode);
		}

		@Override
		public void onServiceResolved(NsdServiceInfo serviceInfo) {
			Log.d(LCAT, "onServiceResolved " + serviceInfo.toString());
			KrollDict result = new KrollDict();
			if (isPingActive) {
				wasPingSuccessful = true;
				result.put("result", true);
				isPingActive = false;
				stopDiscoveryListener();
				Log.d(LCAT, "stopDiscoveryListener()");
				if (onResultCallback != null)
					onResultCallback.call(getKrollObject(), result);
			} else { // standard scan
				airlinoDevices.add(AirlinoDevice
						.parseNsdServiceInfo(serviceInfo));
				if (onSuccessCallback != null) {
					result.put("devices", airlinoDevices.getAllDevices());
					onSuccessCallback.call(getKrollObject(), result);
				}
			}
		}
	}

	public static final String LCAT = AirlinoModule.LCAT;
	private Context ctx;
	private NsdManager nsdManager;
	private KrollFunction onSuccessCallback = null;
	private KrollFunction onErrorCallback = null;
	private KrollFunction onResultCallback = null;
	private boolean isPingActive = false;
	private boolean wasPingSuccessful = false;
	final String DNSTYPE = "_dockset._tcp.";
	int scanTimeout = 2000;
	int pingTimeout = 1000;
	AirlinoDevices airlinoDevices = new AirlinoDevices();
	@Kroll.constant
	public final int PLAY = 0;
	@Kroll.constant
	public final int STOP = 1;

	public NsdManager.DiscoveryListener discoveryListener;

	public BrowserProxy() {
		super();
		ctx = TiApplication.getInstance().getBaseContext();
		nsdManager = (NsdManager) ctx.getSystemService(Context.NSD_SERVICE);

		Log.d(LCAT, "BrowserProxy started");
	}

	@Override
	public void onPause(Activity a) {
		Log.d(LCAT, "BrowserProxy onPause");
		tearDown();
		super.onPause(a);

	}

	@Override
	public void onResume(Activity a) {
		super.onResume(a);
		Log.d(LCAT, "BrowserProxy onResuke");
		initializeDiscoveryListener();
	}

	private void tearDown() {
		if (discoveryListener != null)
			nsdManager.stopServiceDiscovery(discoveryListener);
	}

	public void onDestroy(Activity a) {
		Log.d(LCAT, "BrowserProxy onDestroy");
		tearDown();
		super.onDestroy(a);
	}

	@Kroll.method
	public void isAvailable(KrollDict opt) {
		Object callback;
		wasPingSuccessful = false;
		if (opt.containsKeyAndNotNull("onResult")) {
			callback = opt.get("onResult");
			if (callback instanceof KrollFunction) {
				onResultCallback = (KrollFunction) callback;
			} else
				Log.w(LCAT, "onSuccessFn missed");
		} else
			Log.w(LCAT, "onSuccess missed");
		if (opt.containsKeyAndNotNull("timeout")) {
			scanTimeout = opt.getInt("timeout");
		}
		isPingActive = true;
		this.initializeDiscoveryListener();
	}

	@Kroll.method
	public void startScan(KrollDict opt) {
		Object callback;
		if (opt.containsKeyAndNotNull("onSuccess")) {
			callback = opt.get("onSuccess");
			if (callback instanceof KrollFunction) {
				onSuccessCallback = (KrollFunction) callback;
			} else
				Log.w(LCAT, "onSuccessFn missed");
		} else
			Log.w(LCAT, "onSuccess missed");
		if (opt.containsKeyAndNotNull("onError")) {
			callback = opt.get("onFound");
			if (callback instanceof KrollFunction) {
				onErrorCallback = (KrollFunction) callback;
			}
		}
		if (opt.containsKeyAndNotNull("timeout")) {
			scanTimeout = opt.getInt("timeout");
		}
		Log.d(LCAT,
				"startScan has opts imported => initializeDiscoveryListener");
		initializeDiscoveryListener();
		DiscoverResolver resolver = new DiscoverResolver(ctx, "_dockset._tcp.",
				new DiscoverResolver.Listener() {
					@Override
					public void onServicesChanged(Map<String, Result> services) {
						Log.d(LCAT, "onServicesChanged ======");
						for (MDNSDiscover.Result result : services.values()) {
							Log.d(LCAT, result.txt.dict.toString());
							Log.d(LCAT, result.srv.fqdn);
							Log.d(LCAT, result.srv.target);
							Log.d(LCAT, "P=" + result.srv.port);

						}

					}

				});
		resolver.start();
	}

	private void stopDiscoveryListener() {
		try {
			nsdManager.stopServiceDiscovery(discoveryListener);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void initializeDiscoveryListener() {
		discoveryListener = new NsdManagerDiscoveryHandler();
		nsdManager.discoverServices("_dockset._tcp.",
				NsdManager.PROTOCOL_DNS_SD, discoveryListener);

		new android.os.Handler().postDelayed(new Runnable() {
			public void run() {
				if (isPingActive && wasPingSuccessful == false) {
					stopDiscoveryListener();
					if (onResultCallback != null) {
						KrollDict result = new KrollDict();
						result.put("result", false);
						onResultCallback.call(getKrollObject(), result);
					}
					isPingActive = false;
				}
			}
		}, (isPingActive) ? pingTimeout : scanTimeout);
	}
}
