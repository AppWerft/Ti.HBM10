package ti.airlino;

import org.appcelerator.kroll.KrollDict;
import org.appcelerator.kroll.KrollFunction;
import org.appcelerator.kroll.KrollProxy;
import org.appcelerator.kroll.annotations.Kroll;
import org.appcelerator.kroll.common.Log;
import org.appcelerator.titanium.TiApplication;

import android.app.Activity;
import android.content.Context;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;

@Kroll.proxy(creatableInModule = AirlinoModule.class)
public class AirlinobrowserProxy extends KrollProxy {
	private static final String LCAT = "AirLino 😈";
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
	AirlinoDevices aDevices = new AirlinoDevices();
	@Kroll.constant
	public final int PLAY = 0;
	@Kroll.constant
	public final int STOP = 1;
	public NsdManager.ResolveListener resolveListener;
	public NsdManager.DiscoveryListener discListener = null;

	public AirlinobrowserProxy() {
		super();
		ctx = TiApplication.getInstance();
		resolveListener = new NsdManager.ResolveListener() {
			@Override
			public void onResolveFailed(NsdServiceInfo serviceInfo,
					int errorCode) {
				// Called when the resolve fails. Use the error
				// code to debug.
				Log.e(LCAT, "Resolve failed" + errorCode);
			}

			@Override
			public void onServiceResolved(NsdServiceInfo serviceInfo) {
				Log.d(LCAT, "Resolve succeeded. " + serviceInfo);

				if (isPingActive) {
					wasPingSuccessful = true;
					KrollDict result = new KrollDict();
					result.put("result", true);
					isPingActive = false;
					stopDiscoveryListener();
					Log.d(LCAT, "stopDiscoveryListener()");
					if (onResultCallback != null)
						onResultCallback.call(getKrollObject(), result);
				} else {
					aDevices.add(AirlinoDevice.parseNsdServiceInfo(serviceInfo));
					if (onSuccessCallback != null) {
						onSuccessCallback.call(getKrollObject(),
								aDevices.getAllDevices());
					}
				}
			}
		};

	}

	@Override
	public void onPause(Activity a) {
		tearDown();
		super.onPause(a);

	}

	@Override
	public void onResume(Activity a) {
		super.onResume(a);
		Log.d(LCAT, "onResume");
		this.initializeDiscoveryListener();
	}

	private void tearDown() {
		if (discListener != null)
			nsdManager.stopServiceDiscovery(discListener);
	}

	public void onDestroy(Activity a) {
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
		this.initializeDiscoveryListener();

	}

	private void stopDiscoveryListener() {
		try {
			nsdManager.stopServiceDiscovery(discListener);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void initializeDiscoveryListener() {
		ctx = TiApplication.getInstance().getApplicationContext();
		nsdManager = (NsdManager) ctx.getSystemService(Context.NSD_SERVICE);
		discListener = new NsdManager.DiscoveryListener() {
			@Override
			public void onDiscoveryStarted(String regType) {
				Log.d(LCAT, "Service discovery started");

			}

			/* a new device is found */
			@Override
			public void onServiceFound(NsdServiceInfo service) {
				nsdManager.resolveService(service, resolveListener);
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
		};

		nsdManager.discoverServices(DNSTYPE, NsdManager.PROTOCOL_DNS_SD,
				discListener);

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
