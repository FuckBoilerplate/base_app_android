package app.data.foundation.analytics;

import com.google.android.gms.analytics.HitBuilders;

import javax.inject.Inject;

import app.presentation.foundation.BaseApp;

public class GoogleAnalyticsSender {
    private BaseApp baseApp;

    @Inject public GoogleAnalyticsSender(BaseApp baseApp) {
        this.baseApp = baseApp;
    }

    public void send(String screenName) {
        baseApp.getTracker().setScreenName(screenName);
        baseApp.getTracker().send(new HitBuilders.ScreenViewBuilder().build());
    }

    public void send(String screenName, String category, String action, String label) {
        baseApp.getTracker().setScreenName(screenName);
        baseApp.getTracker().send(new HitBuilders.EventBuilder()
                .setCategory(category)
                .setAction(action)
                .setLabel(label)
                .setValue(1)
                .build());
    }

    public void sendTiming(String screenName, String category, long msTimestamp, String timingName, String label) {
        baseApp.getTracker().setScreenName(screenName);
        baseApp.getTracker().send(new HitBuilders.TimingBuilder()
                .setCategory(category)
                .setValue(msTimestamp)
                .setVariable(timingName)
                .setLabel(label)
                .setValue(1)
                .build());
    }
}
