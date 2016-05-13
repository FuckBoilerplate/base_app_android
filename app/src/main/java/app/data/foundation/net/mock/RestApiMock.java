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

package app.data.foundation.net.mock;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import app.data.foundation.net.RestApi;
import app.domain.user_demo.User;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okio.Buffer;
import retrofit2.Response;
import retrofit2.adapter.rxjava.HttpException;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;
import rx.exceptions.Exceptions;

public class RestApiMock implements RestApi {
    private final Seeder seeder;
    private final Validator validator;

    public RestApiMock() {
        this.seeder = new Seeder();
        validator = new Validator();
    }

    @Override public Observable<Response<User>> getUserByName(@Path("username") String username) {
        return Observable.defer(() -> {
            Object[] values = {username};
            if (validator.notNullEmpty(values)) return responseSuccess(seeder.getUserByName(username));
            else return responseError();
        });
    }

    @Override public Observable<Response<List<User>>> getUsers(@Query("since") int lastIdQueried, @Query("per_page") int perPage) {
        return Observable.defer(() -> {
            Object[] values = {perPage};
            if (validator.notNullEmpty(values)) return responseSuccess(seeder.getUsers());
            else return responseError();
        });
    }

    private final static int SECONDS_DELAY = 2;

    private <T> Observable<Response<T>> responseSuccess(T object) {
        return Observable.just(Response.success(object)).delay(SECONDS_DELAY, TimeUnit.SECONDS);
    }

    private <T> Observable<Response<T>> responseError() {
        Response<T> response = Response.error(404, ResponseBody.create(MediaType.parse("application/json"), "responseError"));
        return Observable.just(response).delay(SECONDS_DELAY, TimeUnit.SECONDS);
    }

    private <T> Observable<T> success(T object) {
        return Observable.just(object).delay(SECONDS_DELAY, TimeUnit.SECONDS);
    }

    private <T> Observable<T> error() {
        Response<T> response = Response.error(404, ResponseBody.create(MediaType.parse("application/json"), "responseError"));
        return Observable.<T>create(subscriber -> subscriber.onError(new HttpException(response)))
                .delay(SECONDS_DELAY, TimeUnit.SECONDS);
    }

    private String bodyToString(final RequestBody request) {
        try {
            final RequestBody copy = request;
            final Buffer buffer = new Buffer();
            if (copy != null) copy.writeTo(buffer);
            else return "";
            return buffer.readUtf8();
        } catch (final IOException e) {
            throw Exceptions.propagate(e);
        }
    }
}
