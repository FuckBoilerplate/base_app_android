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

package app.presentation.internal.di;

import javax.inject.Singleton;

import app.data.foundation.GcmMessageReceiver;
import app.data.foundation.GcmTokenReceiver;
import app.presentation.sections.dashboard.DashBoardActivity;
import app.presentation.sections.launch.LaunchActivity;
import app.presentation.sections.user_demo.detail.UserFragment;
import app.presentation.sections.user_demo.list.UsersFragment;
import app.presentation.sections.user_demo.search.SearchUserFragment;
import dagger.Component;

/**
 * Every fragment or activity which needs to be part of the dependency system provided by Dagger needs
 * to be declared in this component in order to be injected later
 */
@Singleton @Component(modules = {PresentationModule.class})
public interface PresentationComponent {
    void inject(LaunchActivity launchActivity);

    void inject(DashBoardActivity dashBoardActivity);
    void inject(UserFragment userFragment);
    void inject(UsersFragment usersFragment);
    void inject(SearchUserFragment searchUserFragment);

    void inject(GcmTokenReceiver gcmTokenReceiver);
    void inject(GcmMessageReceiver gcmMessageReceiver);
}
