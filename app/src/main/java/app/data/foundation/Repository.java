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

import com.google.gson.Gson;
import com.google.gson.JsonParseException;

import java.io.IOException;

import app.data.foundation.cache.RxProviders;
import app.data.foundation.net.RestApi;
import lombok.Data;
import retrofit2.Response;
import rx.Observable;


public abstract class Repository {
    public static final int PER_PAGE = 50, FIRST_ID_QUERIED = 0;
    protected final RestApi restApi;
    protected final RxProviders rxProviders;
    protected final UIUtils uiUtils;

    public Repository(RestApi restApi, RxProviders rxProviders, UIUtils uiUtils) {
        this.restApi = restApi;
        this.rxProviders = rxProviders;
        this.uiUtils = uiUtils;
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
}
