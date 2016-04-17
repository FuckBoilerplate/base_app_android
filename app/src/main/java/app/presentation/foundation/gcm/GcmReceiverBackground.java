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

package app.presentation.foundation.gcm;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.support.v4.app.NotificationCompat;

import org.base_app_android.R;

import app.domain.foundation.gcm.GcmNotification;
import app.presentation.foundation.BaseApp;
import rx.Observable;
import rx_gcm.GcmReceiverUIBackground;
import rx_gcm.Message;

public class GcmReceiverBackground implements GcmReceiverUIBackground {

    @Override public void onNotification(Observable<Message> oMessage) {
        oMessage.subscribe(message -> {
            BaseApp baseApp = (BaseApp) message.application();

            GcmNotification gcmNotification = GcmNotification.getMessageFromGcmNotification(message);
            showNotification(message, gcmNotification.getTitle(), gcmNotification.getBody(), baseApp);
        });
    }

    private void showNotification(Message message, String title, String body, Application application) {
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(application)
                .setContentTitle(title)
                .setContentText(body)
                .setDefaults(Notification.DEFAULT_ALL)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)
                .setContentIntent(getPendingIntentForNotification(message));

        NotificationManager notificationManager = (NotificationManager) application.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, notificationBuilder.build());
    }

    private PendingIntent getPendingIntentForNotification(Message message) {
        /*        Application application = message.application();
        String target = message.target();

        Bundle bundle = new Bundle();
        bundle.putBoolean(BaseActivity.Behaviour.SHOW_TOOLBAR, false);
        bundle.putString(Notifications.class.getName(), target);

        Intent intent = new Intent(application, DashboardActivity.class);
        intent.putExtras(bundle);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(application);
        stackBuilder.addParentStack(DashboardActivity.class);
        stackBuilder.addNextIntent(intent);

        return stackBuilder.getPendingIntent(0, PendingIntent.FLAG_CANCEL_CURRENT);*/
        return null;
    }

}

