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

import app.presentation.foundation.views.BaseActivity;
import app.presentation.foundation.views.BaseFragment;
import app.presentation.foundation.views.LayoutResFragment;
import app.presentation.sections.user_demo.UserViewGroup;
import butterknife.Bind;
import butterknife.OnClick;
import rx.Observable;

@LayoutResFragment(R.layout.user_search_fragment)
public class SearchUserFragment extends BaseFragment<SearchUserPresenter> implements BaseActivity.BackButtonListener {
    @Bind(R.id.user_view_group) protected UserViewGroup user_view_group;
    @Bind(R.id.et_name) protected EditText et_name;

    @Override protected void injectDagger() {
        getApplicationComponent().inject(this);
    }

    @Override protected void initViews() {
        super.initViews();
        showToast(presenter.helloFromBundle());
    }

    @Override public void showLoading() {
        super.showLoading();
        user_view_group.setVisibility(View.GONE);
    }

    @OnClick(R.id.bt_find_user)
    protected void bt_find_user() {
        String userName = et_name.getText().toString();

        presenter.getUserByUserName(userName).subscribe(user -> {
                user_view_group.setVisibility(View.VISIBLE);
                user_view_group.bind(user);
        });
    }

    @Override public boolean onBackPressed() {
        String message = "Closed on back press from fragment";
        showToast(Observable.just(message));

        getActivity().finish();
        return false;
    }

}
