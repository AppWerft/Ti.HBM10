package de.appwerft.airlino;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

public class AirlinoAdapter {
	private String ENDPOINT;
	private Context ctx;
	private onResultHandler resultHandler;
	HashMap<String, String> map = new HashMap<String, String>();

	public AirlinoAdapter(Context ctx, String host, int port) {
		this.ctx = ctx;
		this.ENDPOINT = "http://" + host + ":" + port + "/api/v15/";
	}

	public interface onResultHandler {
		public void onResult(JSONObject result);
	}

	public void playRadio(String url, String name, onResultHandler resultHandler)
			throws UnsupportedEncodingException, JSONException {
		map.clear();
		map.put("url", url);
		map.put("action", "play");
		map.put("name", name);
		doRequest("radio.action", map, resultHandler);
	}

	public void stopRadio(onResultHandler resultHandler)
			throws UnsupportedEncodingException, JSONException {
		map.clear();
		map.put("action", "stop");
	}

	public void queryRadio(onResultHandler resultHandler)
			throws UnsupportedEncodingException, JSONException {
		map.clear();
		map.put("action", "query");
		doRequest("radio.action", map, resultHandler);
	}

	public void getPlayList(onResultHandler resultHandler)
			throws UnsupportedEncodingException, JSONException {
		map.clear();
		map.put("action", "getplaylist");
		doRequest("radio.action", map, resultHandler);
	}

	public void getFavoriteStation(onResultHandler resultHandler)
			throws UnsupportedEncodingException, JSONException {
		map.put("action", "getfavouritestation");
		doRequest("radio.action", map, resultHandler);
	}

	public void setFavoriteStation(String url, String name,
			onResultHandler resultHandler) throws UnsupportedEncodingException,
			JSONException {
		map.put("url", url);
		map.put("action", "setfavouritestation");
		map.put("name", name);
		doRequest("radio.action", map, resultHandler);
	}

	public void setLEDConfiguration(int brightness,
			onResultHandler resultHandler) throws UnsupportedEncodingException,
			JSONException {
		map.put("brightness", "" + brightness);
		map.put("action", "set");
		doRequest("leds.action", map, resultHandler);
	}

	public void getLEDConfiguration(onResultHandler resultHandler)
			throws UnsupportedEncodingException, JSONException {
		map.put("action", "get");
		doRequest("leds.action", map, resultHandler);
	}

	public void playPlayList(int listId, onResultHandler resultHandler)
			throws UnsupportedEncodingException, JSONException {
		map.put("action", "playplaylist");
		map.put("listid", "" + listId);
		doRequest("radio.action", map, resultHandler);
	}

	public void deviceStatus(onResultHandler resultHandler)
			throws JSONException, UnsupportedEncodingException {
		map.put("action", "query");
		doRequest("configure.action", map, resultHandler);
	}

	private void doRequest(String action, HashMap<String, String> map,
			onResultHandler resultHandler) throws JSONException,
			UnsupportedEncodingException {
		AsyncHttpClient client = new AsyncHttpClient();
		JSONObject jsonParams = new JSONObject(map);
		StringEntity entity = new StringEntity(jsonParams.toString());
		client.post(ctx, ENDPOINT + action, entity, "application/json",
				new AirlinoResponseHandler());

	}

	private final class AirlinoResponseHandler extends JsonHttpResponseHandler {
		@Override
		public void onSuccess(int statusCode, Header[] headers,
				JSONObject response) {
			if (resultHandler != null)
				resultHandler.onResult(response);
		}

		@Override
		public void onFailure(int statusCode, Header[] headers,
				String responseString, Throwable throwable) {
		}
	}
}
