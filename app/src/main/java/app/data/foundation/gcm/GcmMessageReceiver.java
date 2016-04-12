package app.data.foundation.gcm;

import app.presentation.foundation.BaseApp;
import rx.Observable;
import rx_gcm.GcmReceiverData;
import rx_gcm.Message;

/**
 * Created by victor on 12/04/16.
 */
public final class GcmMessageReceiver implements GcmReceiverData {

    @Override public Observable<Message> onNotification(Observable<Message> oMessage) {
        return oMessage.flatMap(message -> {
            BaseApp baseApp = (BaseApp) message.application();
            baseApp.getPresentationComponent().inject(this);

            //use repositories to update models

            return Observable.just(message);
        });
    }

}
