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

package app.sections.dashboard;

import android.support.test.espresso.matcher.ViewMatchers;

import org.base_app_android.R;
import org.hamcrest.Matchers;
import org.junit.Test;

import app.common.BaseTest;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static app.common.ViewActions.actionCloseDrawer;
import static app.common.ViewActions.actionOpenDrawer;

public class DashboardTest extends BaseTest {

    @Test public void Open_And_Close_Users() {
        onView(withId(R.id.drawer_layout)).perform(actionOpenDrawer());
        mediumWait();

        onView(Matchers.allOf(ViewMatchers.withId(R.id.navigation_view), ViewMatchers.hasDescendant(withText(R.string.users)))).perform(click());

        onView(withId(R.id.drawer_layout)).perform(actionCloseDrawer());
    }

    @Test public void Open_And_Close_User() {
        onView(withId(R.id.drawer_layout)).perform(actionOpenDrawer());
        mediumWait();

        onView(Matchers.allOf(ViewMatchers.withId(R.id.navigation_view), ViewMatchers.hasDescendant(withText(R.string.user)))).perform(click());

        onView(withId(R.id.drawer_layout)).perform(actionCloseDrawer());
    }

    @Test public void Open_And_Close_Search_User() {
        onView(withId(R.id.drawer_layout)).perform(actionOpenDrawer());
        mediumWait();

        onView(Matchers.allOf(ViewMatchers.withId(R.id.navigation_view), ViewMatchers.hasDescendant(withText(R.string.find_user)))).perform(click());

        onView(withId(R.id.drawer_layout)).perform(actionCloseDrawer());
    }

}
