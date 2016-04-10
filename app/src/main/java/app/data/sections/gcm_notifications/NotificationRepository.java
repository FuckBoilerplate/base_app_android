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

package app.data.sections.gcm_notifications;

import com.google.gson.GsonBuilder;

import javax.inject.Inject;

import app.data.cache.RxProviders;
import app.data.net.RestApi;
import app.data.sections.foundation.Repository;
import app.domain.gcm_notifications.GcmNotification;
import rx.Observable;
import rx_gcm.Message;
import rx_gcm.TokenUpdate;

public class NotificationRepository extends Repository {

    @Inject public NotificationRepository(RestApi restApi, RxProviders rxProviders) {
        super(restApi, rxProviders);
    }

    public Observable<GcmNotification> getMessageFromGcmNotification(Message message) {
        String payload = message.payload().toString();
        GcmNotification gcmNotification = new GsonBuilder().create().fromJson(payload, GcmNotification.class);
        return Observable.just(gcmNotification);
    }

    public Observable<Void> onTokenRefresh(TokenUpdate tokenUpdate) {
        //send request to server
        return Observable.just(null);
    }

    public Observable<Message> onNotificationReceived(Message message) {
        //Update base_app.data models
        return Observable.just(null);
    }
}
