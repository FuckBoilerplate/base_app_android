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

package app.presentation.sections.user_demo.detail;

import org.base_app_android.R;

import app.presentation.foundation.views.BaseFragment;
import app.presentation.foundation.views.LayoutResFragment;
import app.presentation.sections.user_demo.UserViewGroup;
import butterknife.Bind;
import butterknife.OnClick;

@LayoutResFragment(R.layout.user_fragment)
public class UserFragment extends BaseFragment<UserPresenter> {
    @Bind(R.id.user_view_group) protected UserViewGroup user_view_group;

    @Override protected void injectDagger() {
        getApplicationComponent().inject(this);
    }

    @Override protected void initViews() {
        super.initViews();
        presenter.getCurrentUser().subscribe(user -> user_view_group.bind(user));
    }

    @OnClick(R.id.bt_go_to_search_user)
    protected void bt_go_to_search_user() {
        presenter.goToSearchScreen();
    }
}
