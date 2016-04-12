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

package app.domain.foundation.gcm;

import com.google.common.reflect.TypeToken;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Type;

import app.domain.user_demo.User;
import lombok.Data;
import rx.Observable;
import rx_gcm.Message;


@Data public class GcmNotification<T> {
    private final String key, group; //in order to retrieve cache
    private final T data;
    private final String title, body;

    public static <T> Observable<GcmNotification<T>> getMessageFromGcmNotification(Message message) {
        String payload = message.payload().toString();
        Type type = new TypeToken<GcmNotification<T>>(){}.getType();
        GcmNotification<T> gcmNotification = new GsonBuilder().create().fromJson(payload, type);
        return Observable.just(gcmNotification);
    }

    public static <T> Observable<T> getDataFromGcmNotification(Message message) {
        return getMessageFromGcmNotification(message)
                .map(gcmNotification -> (T) gcmNotification.getData());
    }

    public boolean isWallType() {
        return data instanceof User;
    }

}
