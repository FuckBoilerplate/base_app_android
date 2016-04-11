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

package app.presentation.foundation.gcm_notifications;

import app.data.foundation.GcmNotificationRepository;
import app.presentation.foundation.BaseApp;
import rx.Observable;
import rx_gcm.GcmRefreshTokenReceiver;
import rx_gcm.TokenUpdate;

public class RefreshTokenReceiver implements GcmRefreshTokenReceiver {
    private GcmNotificationRepository gcmNotificationRepository;

    @Override public void onTokenReceive(Observable<TokenUpdate> oTokenUpdate) {
        oTokenUpdate.subscribe(message -> {
            BaseApp baseApp = (BaseApp) message.getApplication();
            baseApp.getPresentationComponent().inject(this);

            gcmNotificationRepository.onTokenRefresh(message).subscribe();
        });
    }
}
