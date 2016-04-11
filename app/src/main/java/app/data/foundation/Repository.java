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

package app.data.foundation;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;

import java.io.IOException;
import java.lang.reflect.Type;

import app.data.cache.RxProviders;
import app.data.net.RestApi;
import app.domain.gcm_notifications.GcmNotification;
import lombok.Data;
import retrofit2.Response;
import rx.Observable;
import rx_gcm.Message;


public abstract class Repository {
    protected final RestApi restApi;
    protected final RxProviders rxProviders;

    public Repository(RestApi restApi, RxProviders rxProviders) {
        this.restApi = restApi;
        this.rxProviders = rxProviders;
    }

    protected void handleError(Response response) {
        if (response.isSuccess()) return;

        try {
            ResponseError responseError = new Gson().fromJson(response.errorBody().string(), ResponseError.class);
            throw new RuntimeException(responseError.getMessage());
        } catch (JsonParseException |IOException exception) {
            throw new RuntimeException();
        }
    }

    @Data private static class ResponseError {
        private final String message;
    }

    protected Observable buildObservableError(String message) {
        return Observable.create(subscriber -> subscriber.onError(new RuntimeException(message)));
    }

    public <T> Observable<GcmNotification<T>> getMessageFromGcmNotification(Message message) {
        String payload = message.payload().toString();
        Type type = new TypeToken<GcmNotification<T>>(){}.getType();
        GcmNotification<T> gcmNotification = new GsonBuilder().create().fromJson(payload, type);
        return Observable.just(gcmNotification);
    }

    public <T> Observable<T> getDataFromGcmNotification(Message message) {
        return getMessageFromGcmNotification(message)
                .map(gcmNotification -> (T) gcmNotification.getData());
    }
}
