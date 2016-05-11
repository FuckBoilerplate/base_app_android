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

import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.$Gson$Types;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import rx_gcm.Message;

@Data public class GcmNotification<T> {
    private final T data;
    private final String title, body;

    public static GcmNotification getMessageFromGcmNotification(Message message) {
        return getMessageFromGcmNotification(Object.class, message);
    }

    public static <T> GcmNotification<T> getMessageFromGcmNotification(Class<T> classData, Message message) {
        String payload = message.payload().getString("payload");
        String title = message.payload().getString("title");
        String body = message.payload().getString("body");

        Type type = $Gson$Types.newParameterizedTypeWithOwner(null, classData);
        T data = new Gson().fromJson(payload, type);

        return new GcmNotification(data, title, body);
    }

    public static <T> GcmNotification<List<T>> getMessageArrayListFromGcmNotification(Class<T> classData, Message message) {
        String payload = message.payload().getString("payload");
        String title = message.payload().getString("title");
        String body = message.payload().getString("body");

        Type typeCollection = $Gson$Types.newParameterizedTypeWithOwner(null, ArrayList.class, classData);

        List<T> data = new GsonBuilder().create().fromJson(payload, typeCollection);
        return new GcmNotification(data, title, body);
    }

    public static <T> T getDataFromGcmNotification(@Nullable Class<T> classData, Message message) {
        return getMessageFromGcmNotification(classData, message).getData();
    }

    public static <T> List<T> getDataArrayListFromGcmNotification(@Nullable Class<T> classData, Message message) {
        return getMessageArrayListFromGcmNotification(classData, message).getData();
    }
}
