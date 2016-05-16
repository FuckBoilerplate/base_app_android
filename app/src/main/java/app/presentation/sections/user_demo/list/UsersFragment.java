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

package app.presentation.sections.user_demo.list;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.malinskiy.superrecyclerview.SuperRecyclerView;

import org.base_app_android.R;

import java.util.List;

import app.domain.user_demo.User;
import app.presentation.foundation.views.BaseFragment;
import app.presentation.foundation.views.LayoutResFragment;
import app.presentation.sections.dashboard.DashBoardActivity;
import app.presentation.sections.user_demo.UserViewGroup;
import butterknife.Bind;
import library.recycler_view.OkRecyclerViewAdapter;
import rx.Observable;


@LayoutResFragment(R.layout.users_fragment)
public class UsersFragment extends BaseFragment<UsersPresenter>  {
    @Bind(R.id.rv_users) protected SuperRecyclerView rv_users;
    private OkRecyclerViewAdapter<User, UserViewGroup> adapter;
    private SearchView searchView;
    private String query;

    @Override protected void injectDagger() {
        getApplicationComponent().inject(this);
    }

    @Nullable @Override protected String getScreenNameForGoogleAnalytics() {
        return this.getClass().getSimpleName();
    }

    @Override protected void initViews() {
        super.initViews();
        setUpRecyclerView();
        setHasOptionsMenu(true);
    }

    private void setUpRecyclerView() {
        adapter = new OkRecyclerViewAdapter<User, UserViewGroup>() {
            @Override protected UserViewGroup onCreateItemView(ViewGroup parent, int viewType) {
                return new UserViewGroup(getActivity());
            }
        };

        adapter.setOnItemClickListener((user, userViewGroup, position) ->
            presenter.dataForNextScreen(user)
                    .compose(safelyReport())
                    .subscribe(_I -> wireframe.userScreen())
        );

        adapter.setRxPager(R.layout.srv_progress,
                lastUser -> presenter.nextPage(lastUser, query)
                            .compose(safelyReport())
                            .doOnCompleted(this::populateCounterUsers));

        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        adapter.configureGridLayoutManagerForPagination(layoutManager);

        rv_users.setLayoutManager(layoutManager);
        rv_users.setAdapter(adapter);
        rv_users.setRefreshListener(() -> {
            Observable<List<User>> oUsers = presenter.refreshList(query).compose(safelyReport()).doOnCompleted(this::populateCounterUsers);
            adapter.resetPager(oUsers);
        });
    }

    @Override protected void onSyncScreen() {
        super.onSyncScreen();
    }


    @Override public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_filter, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.action_search:
                searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
                setupSearchView(menuItem);
                break;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    private void setupSearchView(MenuItem menuItem) {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override public boolean onQueryTextSubmit(String query) {
                hideKeyboard(searchView);
                UsersFragment.this.query = query;
                Observable<List<User>> oUsers = presenter.refreshList(query).compose(safelyReport()).doOnCompleted(() -> populateCounterUsers());
                adapter.resetPager(oUsers);
                return true;
            }

            @Override public boolean onQueryTextChange(String query) {
                return true;
            }
        });

        // For Android API 14+
        MenuItemCompat.setOnActionExpandListener(menuItem, new MenuItemCompat.OnActionExpandListener() {
            @Override public boolean onMenuItemActionCollapse(MenuItem item) {
                UsersFragment.this.query = null;
                Observable<List<User>> oUsers = presenter.refreshList(query).compose(safelyReport()).doOnCompleted(() -> populateCounterUsers());
                adapter.resetPager(oUsers);
                return true;
            }

            @Override public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }
        });
    }

    private void populateCounterUsers() {
        rv_users.postDelayed(() -> {
            if (getActivity() instanceof DashBoardActivity)
                ((DashBoardActivity) getActivity()).showUsersCounter(adapter.getAll().size());
        }, 500);
    }

    private void hideKeyboard(final View view) {
        view.clearFocus();
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
