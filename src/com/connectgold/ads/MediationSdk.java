package com.connectgold.ads;

import android.content.Context;
import android.view.ViewGroup;

public class MediationSdk {
    public interface AdLoadCallback {
        void onAdLoaded(String networkName);
        void onAdFailed(String errorReason);
    }

    public void requestAd(Context context, String userId, String placementId, String format, ViewGroup container, AdLoadCallback callback) {
        // Stub implementation
    }
}
