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

package app.presentation.sections.user_demo.search;

import android.view.View;
import android.widget.EditText;

import org.base_app_android.R;

import butterknife.Bind;
import butterknife.OnClick;
import app.domain.user_demo.User;
import app.presentation.foundation.views.BaseActivity;
import app.presentation.foundation.views.BaseFragment;
import app.presentation.foundation.views.LayoutResFragment;
import app.presentation.sections.user_demo.UserViewGroup;
import rx.Observable;

@LayoutResFragment(R.layout.user_search_fragment)
public class SearchUserFragment extends BaseFragment<SearchUserPresenter> implements BaseActivity.BackButtonListener {
    @Bind(R.id.user_view_group) protected UserViewGroup user_view_group;
    @Bind(R.id.et_name) protected EditText et_name;

    @Override protected void injectDagger() {
        getApplicationComponent().inject(this);
    }

    public void showUser(Observable<User> oUser) {
        oUser.subscribe(user_view_group::bind);
    }

    @Override public void showLoading() {
        super.showLoading();
        user_view_group.setVisibility(View.GONE);
    }

    @Override public void hideLoading() {
        super.hideLoading();
        user_view_group.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.bt_find_user)
    protected void bt_find_user() {
        presenter.getUserByUserName(et_name.getText().toString());
    }

    @Override public boolean onBackPressed() {
        String message = "Closed on back press from fragment";
        showToast(Observable.just(message));

        getActivity().finish();
        return false;
    }

}
