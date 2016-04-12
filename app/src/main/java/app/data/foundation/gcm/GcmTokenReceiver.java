package app.data.foundation.gcm;

import javax.inject.Inject;

import app.presentation.foundation.BaseApp;
import rx.Observable;
import rx_gcm.GcmRefreshTokenReceiver;
import rx_gcm.TokenUpdate;

/**
 * Created by victor on 12/04/16.
 */
public final class GcmTokenReceiver implements GcmRefreshTokenReceiver {

    @Override public void onTokenReceive(Observable<TokenUpdate> oTokenUpdate) {
        oTokenUpdate.subscribe(tokenUpdate -> {
            BaseApp baseApp = (BaseApp) tokenUpdate.getApplication();
            baseApp.getPresentationComponent().inject(this);

            //use repository to update token
        });
    }
}
