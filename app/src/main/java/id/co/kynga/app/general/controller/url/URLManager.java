package id.co.kynga.app.general.controller.url;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class URLManager {
	public interface URLCallback {
		void didURLSucceeded(final int status_code, final String response);

		void didURLFailed();
	}

	public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
	public static int TIMEOUT = 30;

	public static OkHttpClient getClient() {
		final OkHttpClient client = new OkHttpClient();
		OkHttpClient client_timeout = client.newBuilder()
				.connectTimeout(TIMEOUT, TimeUnit.SECONDS)
				.writeTimeout(TIMEOUT, TimeUnit.SECONDS)
				.readTimeout(TIMEOUT, TimeUnit.SECONDS)
				.build();
		return client_timeout;
	}

	public static Request.Builder getBuilder(final Map map) {
		Request.Builder builder = new Request.Builder();
		if (map != null) {
			Iterator it = map.entrySet().iterator();
			while (it.hasNext()) {
				final Map.Entry pair = (Map.Entry) it.next();
				builder.addHeader(pair.getKey().toString(), pair.getValue().toString());
				it.remove();
			}
		}
		return builder;
	}

	public static RequestBody getRequestBody(final Map map) {
		MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
		if (map != null) {
			Iterator it = map.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry pair = (Map.Entry) it.next();
				Headers headers = Headers.of("Content-Disposition", "form-data; name=\"" + pair.getKey() + "\"");
				builder.addPart(headers, RequestBody.create(null, pair.getValue().toString()));
				it.remove();
			}
		}
		return builder.build();
	}

	public static String requestGet(
			final String url,
			final Map map_header) throws IOException {
		final OkHttpClient client = getClient();
		Request.Builder builder = getBuilder(map_header);
		Request request = builder.url(url)
				.build();
		Response response = client.newCall(request).execute();
		if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
		return response.body().string();
	}

	public static void requestGetAsync(
			final String url,
			final Map map_header,
			final URLCallback url_callback) {
		final OkHttpClient client = getClient();
		Request.Builder builder = getBuilder(map_header);
		Request request = builder.url(url)
				.build();
		client.newCall(request).enqueue(new Callback() {
			@Override
			public void onFailure(Call call, IOException e) {
				if (url_callback != null) {
					url_callback.didURLFailed();
				}
			}

			@Override
			public void onResponse(Call call, Response response) throws IOException {
				if (url_callback != null) {
					url_callback.didURLSucceeded(response.code(), response.body().string());
				}
			}
		});
	}

	public static String requestPost(
			final String url,
			final Map map_parameters,
			final Map map_header) throws IOException {
		final OkHttpClient client = getClient();
		RequestBody body = getRequestBody(map_parameters);
		Request.Builder builder = getBuilder(map_header);
		Request request = builder.url(url)
				.post(body)
				.build();
		Response response = client.newCall(request).execute();
		if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
		return response.body().string();
	}

	public static void requestPostAsync(
			final String url,
			final Map map_parameters,
			final Map map_header,
			final URLCallback url_callback) {
		final OkHttpClient client = getClient();
		RequestBody body = getRequestBody(map_parameters);
		Request.Builder builder = getBuilder(map_header);
		Request request = builder.url(url)
				.post(body)
				.build();
		client.newCall(request).enqueue(new Callback() {
			@Override
			public void onFailure(Call call, IOException e) {
				if (url_callback != null) {
					url_callback.didURLFailed();
				}
			}

			@Override
			public void onResponse(Call call, Response response) throws IOException {
				if (url_callback != null) {
					url_callback.didURLSucceeded(response.code(), response.body().string());
				}
			}
		});
	}

	public static String requestJSON(
			final String url,
			final Map map_header,
			final String json) throws IOException {
		final OkHttpClient client = getClient();
		RequestBody body = RequestBody.create(URLManager.JSON, json);
		Request.Builder builder = getBuilder(map_header);
		Request request = builder.url(url)
				.post(body)
				.build();
		Response response = client.newCall(request).execute();
		if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
		return response.body().string();
	}

	public static void requestJSONAsync(
			final String url,
			final Map map_header,
			final String json,
			final URLCallback url_callback) {
		final OkHttpClient client = getClient();
		RequestBody body = RequestBody.create(URLManager.JSON, json);
		Request.Builder builder = getBuilder(map_header);
		Request request = builder.url(url)
				.post(body)
				.build();
		client.newCall(request).enqueue(new Callback() {
			@Override
			public void onFailure(Call call, IOException e) {
				if (url_callback != null) {
					url_callback.didURLFailed();
				}
			}

			@Override
			public void onResponse(Call call, Response response) throws IOException {
				if (url_callback != null) {
					url_callback.didURLSucceeded(response.code(), response.body().string());
				}
			}
		});
	}
}