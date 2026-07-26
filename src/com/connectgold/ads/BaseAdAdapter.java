package com.connectgold.ads;

import android.content.Context;
import android.view.ViewGroup;

public interface BaseAdAdapter {
    interface AdapterListener {
        void onSuccess();
        void onError(String reason);
    }

    void loadAd(Context context, String format, ViewGroup container, AdapterListener listener);
}
