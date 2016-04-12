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

package app.data.foundation.net;

import java.util.ArrayList;
import java.util.List;

import app.domain.user_demo.User;
import retrofit2.Response;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

public class RestApiMock implements RestApi {

    @Override public Observable<Response<User>> getUserByName(@Path("username") String username) {
        User user = new User(1);
        user.setLogin("Mock name");
        user.setAvatar_url("https://assets-cdn.github.com/images/modules/logos_page/GitHub-Mark.png");
        return Observable.just(Response.success(user));
    }

    @Override public Observable<Response<List<User>>> getUsers(@Query("since") int lastIdQueried, @Query("per_page") int perPage) {
        List<User> users = new ArrayList<>();

        for (int i = 0; i < 50; i++) {
            users.add(createUser(i));
        }

        return Observable.just(Response.success(users));
    }

    private User createUser(int id) {
        User user = new User(id);
        user.setLogin("Mock name");
        user.setAvatar_url("https://assets-cdn.github.com/images/modules/logos_page/GitHub-Mark.png");
        return user;
    }
}
