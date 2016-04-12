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

package app.data.sections.user_demo;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import app.data.foundation.cache.RxProviders;
import app.data.foundation.net.RestApi;
import app.data.foundation.Repository;
import app.domain.user_demo.User;
import io.rx_cache.DynamicKey;
import io.rx_cache.EvictProvider;
import rx.Observable;

public class UserRepository extends Repository {
    public static final int USERS_PER_PAGE = 25, MAX_USERS_TO_LOAD = 300;

    @Inject public UserRepository(RestApi restApi, RxProviders rxProviders) {
        super(restApi, rxProviders);
    }

    public Observable<User> searchByUserName(final String  username) {
        return restApi.getUser(username).map(response -> {
            handleError(response);
            final User user = response.body();
            return user;
        });
    }

    public Observable<List<User>> askForUsers(int lastIdQueried, boolean refresh) {
        if (lastIdQueried > MAX_USERS_TO_LOAD) return Observable.just(new ArrayList<>());

        Observable<List<User>> oUsers = restApi.getUsers(lastIdQueried, USERS_PER_PAGE).map(response -> {
            handleError(response);
            return response.body();
        });

        return rxProviders.getUsers(oUsers, new DynamicKey(lastIdQueried), new EvictProvider(refresh)).
                map(reply -> {
                    List<User> users = reply.getData();
                    for (User user : users) {
                        String login = user.getLogin() + System.lineSeparator() + reply.getSource().name();
                        user.setLogin(login);
                    }
                    return users;
                });
    }
}
