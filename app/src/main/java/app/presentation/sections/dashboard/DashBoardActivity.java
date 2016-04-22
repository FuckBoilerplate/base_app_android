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

package app.presentation.sections.dashboard;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import org.base_app_android.R;

import java.util.Arrays;

import app.domain.dashboard.ItemMenu;
import app.presentation.foundation.views.BaseActivity;
import app.presentation.foundation.views.LayoutResActivity;
import app.presentation.sections.user_demo.detail.UserFragment;
import app.presentation.sections.user_demo.list.UsersFragment;
import app.presentation.sections.user_demo.search.SearchUserFragment;
import butterknife.Bind;
import butterknife.BindString;
import library.recycler_view.OkRecyclerViewAdapter;

@LayoutResActivity(R.layout.dashboard_activity)
public class DashBoardActivity extends BaseActivity {
    private final static int ID_USERS = 1, ID_USER = 2, ID_SEARCH = 3;

    @Bind(R.id.rv_menu_items) protected RecyclerView rv_menu_items;
    @Bind(R.id.drawer_layout) protected DrawerLayout drawer_layout;
    private OkRecyclerViewAdapter<ItemMenu, ItemMenuViewGroup> adapter;
    private ActionBarDrawerToggle drawerToggle;

    @Override protected void injectDagger() {
        getApplicationComponent().inject(this);
    }

    @Override protected void initViews() {
        super.initViews();
        setUpDrawerToggle();
        setUpRecyclerView();
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        drawer_layout.removeDrawerListener(drawerToggle);
    }

    private void setUpDrawerToggle() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        drawerToggle = new ActionBarDrawerToggle(this, drawer_layout, R.string.app_name, R.string.app_name) {
            @Override public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
            }

            @Override public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };

        drawer_layout.addDrawerListener(drawerToggle);
    }

    private void setUpRecyclerView() {
        rv_menu_items.setHasFixedSize(true);
        rv_menu_items.setLayoutManager(new LinearLayoutManager(this));

        adapter = new OkRecyclerViewAdapter<ItemMenu, ItemMenuViewGroup>() {
            @Override protected ItemMenuViewGroup onCreateItemView(ViewGroup viewGroup, int i) {
                return new ItemMenuViewGroup(DashBoardActivity.this);
            }
        };

        adapter.setOnItemClickListener((itemMenu, itemMenuViewGroup) -> {
            drawer_layout.closeDrawer(rv_menu_items);

            if (itemMenu.getId() == ID_USERS) showUsers();
            else if (itemMenu.getId() == ID_USER) showUser();
            else showUserSearch();
        });

        adapter.setAll(Arrays.asList(
                new ItemMenu(ID_USERS, getString(R.string.users), R.drawable.ic_users),
                new ItemMenu(ID_USER, getString(R.string.user), R.drawable.ic_user),
                new ItemMenu(ID_SEARCH, getString(R.string.find_user), R.drawable.ic_search)
        ));

        rv_menu_items.setAdapter(adapter);

        showUsers();
    }

    @BindString(R.string.users) protected String users;
    public void showUsers() {
        replaceFragmentIfItIsNotCurrentDisplayed(UsersFragment.class);
        setTitle(users);
    }

    @BindString(R.string.user) protected String user;
    public void showUser() {
        replaceFragmentIfItIsNotCurrentDisplayed(UserFragment.class);
        setTitle(user);
    }

    @BindString(R.string.find_user) protected String find_user;
    public void showUserSearch() {
        replaceFragmentIfItIsNotCurrentDisplayed(SearchUserFragment.class);
        setTitle(find_user);
    }

    @Override protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) return true;
        else if (item.getItemId() == android.R.id.home) onBackPressed();
        return super.onOptionsItemSelected(item);
    }
}
