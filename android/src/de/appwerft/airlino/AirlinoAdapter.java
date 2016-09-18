package de.appwerft.airlino;

// http://stackoverflow.com/questions/18146361/how-to-create-jar-file-with-package-structure

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
	HashMap<String, String> map;

	public AirlinoAdapter(Context ctx, String endpoint,
			onResultHandler resultHandler) {
		this.ctx = ctx;
		this.resultHandler = resultHandler;
		this.ENDPOINT = endpoint;
		map = new HashMap<String, String>();
	}

	public interface onResultHandler {
		public void onResult(JSONObject result);
	}

	public void playRadio(String url, String name)
			throws UnsupportedEncodingException, JSONException {
		map.put("url", url);
		map.put("action", "play");
		map.put("name", name);
		doRequest("radio.action", map);
	}

	public void stopRadio() throws UnsupportedEncodingException, JSONException {
		map.put("action", "stop");
		doRequest("radio.action", map);
	}

	public void queryRadio() throws UnsupportedEncodingException, JSONException {
		map.put("action", "query");
		doRequest("radio.action", map);
	}

	public void getPlayList() throws UnsupportedEncodingException,
			JSONException {
		map.put("action", "getplaylist");
		doRequest("radio.action", map);
	}

	public void getFavoriteStation() throws UnsupportedEncodingException,
			JSONException {
		map.put("action", "getfavouritestation");
		doRequest("radio.action", map);
	}

	public void setFavoriteStation(String url, String name)
			throws UnsupportedEncodingException, JSONException {
		map.put("url", url);
		map.put("action", "setfavouritestation");
		map.put("name", name);
		doRequest("radio.action", map);
	}

	public void setLEDConfiguration(int brightness)
			throws UnsupportedEncodingException, JSONException {
		map.put("brightness", "" + brightness);
		map.put("action", "set");
		doRequest("leds.action", map);
	}

	public void getLEDConfiguration() throws UnsupportedEncodingException,
			JSONException {
		map.put("action", "get");
		doRequest("leds.action", map);
	}

	public void playPlayList(int listId) throws UnsupportedEncodingException,
			JSONException {
		map.put("action", "playplaylist");
		map.put("listid", "" + listId);
		doRequest("radio.action", map);
	}

	public void deviceStatus() throws JSONException,
			UnsupportedEncodingException {
		map.put("action", "query");
		doRequest("configure.action", map);
	}

	private void doRequest(String action, HashMap<String, String> map)
			throws JSONException, UnsupportedEncodingException {
		AsyncHttpClient client = new AsyncHttpClient();
		JSONObject jsonParams = new JSONObject();
		for (String key : map.keySet()) {
			jsonParams.put(key, map.get(key));
		}
		StringEntity entity = new StringEntity(jsonParams.toString());
		client.post(ctx, ENDPOINT, entity, "application/json",
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
