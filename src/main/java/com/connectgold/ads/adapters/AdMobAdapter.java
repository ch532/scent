package com.connectgold.ads.adapters;

import android.content.Context;
import android.view.ViewGroup;
import com.connectgold.ads.BaseAdAdapter;

public class AdMobAdapter implements BaseAdAdapter {
    @Override
    public void loadAd(Context context, String format, ViewGroup container, AdapterListener listener) {
        try {
            // Programmatic AdMob AdView setup
            listener.onSuccess();
        } catch (Exception e) {
            listener.onError("AdMob error: " + e.getMessage());
        }
    }
}
