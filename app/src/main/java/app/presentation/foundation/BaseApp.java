/*
 * Copyright 2016 FuckBoilerplate
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package app.presentation.foundation;

import android.app.Application;
import android.support.annotation.Nullable;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

import org.base_app_android.BuildConfig;
import org.base_app_android.R;

import app.data.foundation.gcm.GcmMessageReceiver;
import app.data.foundation.gcm.GcmTokenReceiver;
import app.presentation.foundation.dagger.DaggerPresentationComponent;
import app.presentation.foundation.dagger.PresentationComponent;
import app.presentation.foundation.dagger.PresentationModule;
import app.presentation.foundation.gcm.GcmReceiverBackground;
import app.presentation.foundation.views.BaseActivity;
import rx_gcm.internal.RxGcm;

/**
 * Custom Application
 */
public class BaseApp extends Application {
    private PresentationComponent presentationComponent;
    private GoogleAnalytics analytics;
    private Tracker tracker;

    @Override public void onCreate() {
        super.onCreate();
        initInject();
        AppCare.YesSir.takeCareOn(this);
        initGcm();
        initGoogleAnalytics();
    }

    private void initInject() {
        presentationComponent = DaggerPresentationComponent.builder()
                .presentationModule(new PresentationModule(this))
                .build();
    }

    private void initGcm() {
        RxGcm.Notifications.register(this, GcmMessageReceiver.class, GcmReceiverBackground.class)
                .subscribe(token -> {}, error -> {});

        RxGcm.Notifications.onRefreshToken(GcmTokenReceiver.class);
    }

    private void initGoogleAnalytics() {
        analytics = GoogleAnalytics.getInstance(this);

        if(BuildConfig.DEBUG) {
            // true = for log output, it does not sent data to Google Analytics
            analytics.setDryRun(true);
            // To enable debug logging on a device run:
            // adb shell setprop log.tag.GAv4 DEBUG
            // adb logcat -s GAv4
            // true = disable google analytics on the app
//        analytics.setAppOptOut(true);
        }

        tracker = analytics.newTracker(getString(R.string.ga_trackingId));
        tracker.enableExceptionReporting(true);
        tracker.enableAdvertisingIdCollection(true);
        tracker.enableAutoActivityTracking(false);
    }

    public Tracker getTracker() {
        return tracker;
    }

    public PresentationComponent getPresentationComponent() {
        return presentationComponent;
    }

    @Nullable public BaseActivity getLiveActivity(){
        return (BaseActivity) AppCare.YesSir.getLiveActivityOrNull();
    }
}
