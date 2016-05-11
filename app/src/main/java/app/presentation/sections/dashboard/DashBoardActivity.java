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
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import org.base_app_android.R;

import javax.inject.Inject;

import app.data.foundation.analytics.GoogleAnalyticsSender;
import app.presentation.foundation.views.BaseActivity;
import app.presentation.foundation.views.LayoutResActivity;
import app.presentation.sections.user_demo.detail.UserFragment;
import app.presentation.sections.user_demo.list.UsersFragment;
import app.presentation.sections.user_demo.search.SearchUserFragment;
import butterknife.Bind;
import butterknife.BindString;

@LayoutResActivity(R.layout.dashboard_activity)
public class DashBoardActivity extends BaseActivity {
    @Bind(R.id.drawer_layout) protected DrawerLayout drawer_layout;
    @Bind(R.id.navigation_view) protected NavigationView navigation_view;
    @Inject GoogleAnalyticsSender googleAnalytics;
    private ActionBarDrawerToggle drawerToggle;

    @Override protected void injectDagger() {
        getApplicationComponent().inject(this);
    }

    @Override protected void initViews() {
        super.initViews();
        setUpDrawerToggle();
        setUpNavigationView();
        googleAnalytics.send(this.getClass().getSimpleName());
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

    private void setUpNavigationView() {
        navigation_view.setNavigationItemSelectedListener(menuItem -> {
            if (!menuItem.isChecked()) menuItem.setChecked(true);
            drawer_layout.closeDrawers();

            switch (menuItem.getItemId()) {
                case R.id.drawer_users:
                    showUsers();
                    return true;
                case R.id.drawer_user:
                    showUser();
                    return true;
                case R.id.drawer_find_user:
                    showUserSearch();
                    return true;
                default:
                    return true;
            }
        });

        navigation_view.setCheckedItem(R.id.drawer_users);
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
        else if (item.getItemId() == R.id.action_logout) askForLogout();
        return super.onOptionsItemSelected(item);
    }

    @Override public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_dashboard, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void askForLogout() {
        new AlertDialog.Builder(DashBoardActivity.this)
                .setTitle(R.string.app_name)
                .setMessage(R.string.ask_logout)
                .setCancelable(false)
                .setPositiveButton(android.R.string.yes, (dialog, id) -> finish())
                .setNegativeButton(android.R.string.cancel, (dialog, id) -> dialog.cancel())
                .show();
    }

    public void showUsersCounter(int usersLoaded) {
        TextView tvOrdersNotSent = (TextView) navigation_view.getMenu().findItem(R.id.drawer_users).getActionView().findViewById(R.id.tv_counter);
        tvOrdersNotSent.setText(String.valueOf(usersLoaded));
        tvOrdersNotSent.setVisibility(usersLoaded > 0 ? View.VISIBLE : View.GONE);
    }
}
