package com.chyke.online;

import android.app.Activity;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.connectgold.ads.MediationSdk;

public class MainActivity extends Activity {

    private MediationSdk mediationSdk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Build layout programmatically without XML
        FrameLayout adContainerView = new FrameLayout(this);
        setContentView(adContainerView);

        mediationSdk = new MediationSdk();

        // Request ad from Cloudflare worker engine
        mediationSdk.requestAd(
            this,
            "user_12345",          // userId
            "main_banner_slot",    // placementId
            "BANNER",              // adFormat
            adContainerView,       // view container
            new MediationSdk.AdLoadCallback() {
                @Override
                public void onAdLoaded(String networkName) {
                    Toast.makeText(MainActivity.this, "Ad Loaded via " + networkName, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onAdFailed(String errorReason) {
                    Toast.makeText(MainActivity.this, "Mediation Failed: " + errorReason, Toast.LENGTH_LONG).show();
                }
            }
        );
    }
}
