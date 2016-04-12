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

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.List;

import app.data.foundation.dagger.DataModule;
import app.data.foundation.net.RestApi;
import app.domain.user_demo.User;
import retrofit2.Response;
import rx.observers.TestSubscriber;

import static junit.framework.Assert.assertNull;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RestApiTest {
    private static final String VALID_USERNAME = "RefineriaWeb", INVALID_USERNAME = "";
    private RestApi restApiUT;

    @Before public void setUp() {
        restApiUT = new DataModule().provideRestApi();
    }

    @Test public void _1_When_Get_User_With_Valid_User_Name_Then_Get_UserDemo() {
        TestSubscriber<Response<User>> subscriber = new TestSubscriber<>();
        restApiUT.getUserByName(VALID_USERNAME).subscribe(subscriber);

        subscriber.awaitTerminalEvent();
        Response<User> userDemo = subscriber.getOnNextEvents().get(0);
        assertThat(userDemo.body().getId(), is(not(0)));
        assertThat(subscriber.getOnNextEvents().size(), is(1));
    }

    @Test public void _2_When_Get_User_With_Invalid_User_Name_Then_Throw_An_ExceptionOnSubscriber() {
        TestSubscriber<Response<User>> subscriber = new TestSubscriber<>();
        restApiUT.getUserByName(INVALID_USERNAME).subscribe(subscriber);

        subscriber.awaitTerminalEvent();
        Response<User> userDemo = subscriber.getOnNextEvents().get(0);
        assertNull(userDemo.body());
    }

    @Test public void _3_When_Get_Users_Then_Get_Users() {
        TestSubscriber<Response<List<User>>> subscriber = new TestSubscriber<>();
        restApiUT.getUsers(0, 25).subscribe(subscriber);

        subscriber.awaitTerminalEvent();
        assertThat(subscriber.getOnNextEvents().size(), is(not(0)));
    }

}
