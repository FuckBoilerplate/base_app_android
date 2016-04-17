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

import java.util.ArrayList;
import java.util.List;

import app.data.foundation.Repository;
import app.domain.user_demo.User;

public class Seeder {

    public User getUserByName(String name) {
        User user = new User(1);
        user.setLogin(name);
        user.setAvatar_url("https://assets-cdn.github.com/images/modules/logos_page/GitHub-Mark.png");

        return user;
    }

    public List<User> getUsers() {
        List<User> users = new ArrayList<>();

        for (int i = 1; i < Repository.PER_PAGE; i++) {
            User user = new User(i);
            user.setLogin("Name " + i);
            user.setAvatar_url("https://assets-cdn.github.com/images/modules/logos_page/GitHub-Mark.png");
            users.add(user);
        }

        return users;
    }
}
