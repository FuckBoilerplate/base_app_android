/*
 * Copyright 2015 RefineriaWeb
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

package app.sections.user_demo;

import android.support.test.espresso.contrib.RecyclerViewActions;

import org.base_app_android.R;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import app.common.BaseTest;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UsersTest extends BaseTest {
    private static final int INDEX_LIST = 11, ID_USER_REFINERIA = 8580307;
    private static final String USERNAME_REFINERIA = "RefineriaWeb";

    @Test public void _1_Get_Users() {
        mediumWait();
        onView(withId(android.R.id.list)).perform(RecyclerViewActions.scrollToPosition(INDEX_LIST));
    }

    @Test public void _2_Search_User() {
        mediumWait();
        onView(withId(android.R.id.list)).perform(RecyclerViewActions.scrollToPosition(INDEX_LIST));

        mediumWait();
        onView(withId(android.R.id.list)).perform(RecyclerViewActions.actionOnItemAtPosition(INDEX_LIST, click()));

        onView(withId(R.id.bt_go_to_search_user)).perform(click());

        onView(withId(R.id.et_name)).perform(click(), replaceText(USERNAME_REFINERIA), closeSoftKeyboard());
        onView(withId(R.id.bt_find_user)).perform(click());
        mediumWait();

        onView(withId(R.id.tv_name)).check(matches(withText(ID_USER_REFINERIA + ":" + USERNAME_REFINERIA)));
    }

}
