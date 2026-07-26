package com.connectgold.ads.sdk;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ConnectGoldSDK {
    private static final String TAG = "ConnectGoldSDK";
    private static final String ENDPOINT_REGISTER = "https://old-term-8a97.trustconnect713.workers.dev/v1/register";
    private static final String ENDPOINT_MEDIATE = "https://old-term-8a97.trustconnect713.workers.dev/v1/mediate";

    private static String publisherId = null;
    private static volatile boolean isInitialized = false;
    private static final ExecutorService executor = Executors.newCachedThreadPool();
    private static final Handler mainHandler = new Handler(Looper.getMainLooper());

    public interface WaterfallCallback {
        void onSuccess(JSONArray waterfall);
        void onError(String error);
    }

    public static void initialize(Context context, String appId) {
        if (isInitialized) return;

        synchronized (ConnectGoldSDK.class) {
            if (isInitialized) return;
            isInitialized = true;
        }

        executor.execute(() -> {
            try {
                // Auto-register publisher with zero dashboard signups
                URL url = new URL(ENDPOINT_REGISTER);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setDoOutput(true);

                JSONObject payload = new JSONObject();
                payload.put("packageName", context.getPackageName());
                payload.put("appId", appId);

                try (OutputStream os = conn.getOutputStream()) {
                    os.write(payload.toString().getBytes(StandardCharsets.UTF_8));
                }

                if (conn.getResponseCode() == 200) {
                    String response = new String(conn.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
                    JSONObject json = new JSONObject(response);

                    publisherId = json.getString("publisher_id");
                    String profileUrl = json.getString("profile_url");

                    Log.i(TAG, "ConnectGold Auto-Registered!");
                    Log.i(TAG, "Assigned Publisher ID: " + publisherId);
                    Log.i(TAG, "View Profile directly at URL: " + profileUrl);
                }
            } catch (Exception e) {
                Log.e(TAG, "Auto-registration error: " + e.getMessage());
            }
        });
    }

    public static String getPublisherId() { return publisherId; }

    public static void fetchWaterfall(Context context, String userId, String placementId, String format, JSONObject credentials, WaterfallCallback callback) {
        executor.execute(() -> {
            try {
                URL url = new URL(ENDPOINT_MEDIATE);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setDoOutput(true);

                JSONObject payload = new JSONObject();
                payload.put("userId", userId != null ? userId : "anonymous");
                payload.put("publisherId", publisherId != null ? publisherId : "anonymous_publisher");
                payload.put("placementId", placementId);
                payload.put("adFormat", format.toUpperCase());
                if (credentials != null) {
                    payload.put("networkCredentials", credentials);
                }

                try (OutputStream os = conn.getOutputStream()) {
                    os.write(payload.toString().getBytes(StandardCharsets.UTF_8));
                }

                if (conn.getResponseCode() == 200) {
                    String jsonStr = new String(conn.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
                    JSONObject response = new JSONObject(jsonStr);
                    JSONArray waterfall = response.getJSONArray("waterfall_sequence");
                    mainHandler.post(() -> callback.onSuccess(waterfall));
                } else {
                    final int responseCode = conn.getResponseCode();
                    mainHandler.post(() -> callback.onError("Server Error: " + responseCode));
                }
            } catch (Exception e) {
                final String errorMessage = e.getMessage();
                mainHandler.post(() -> callback.onError(errorMessage));
            }
        });
    }
}
