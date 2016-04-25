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

package app.presentation.foundation.views;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

import org.base_app_android.R;

import java.io.Serializable;

import javax.inject.Inject;

import app.presentation.foundation.BaseApp;
import app.presentation.foundation.dagger.PresentationComponent;
import app.presentation.sections.Wireframe;
import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;

public abstract class BaseActivity extends AppCompatActivity {
    @Nullable @Bind(R.id.app_bar) protected AppBarLayout app_bar;
    @Nullable @Bind(R.id.toolbar) protected Toolbar toolbar;
    @Inject protected Wireframe wireframe;

    protected String app_name;
    private MaterialDialog materialDialog;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app_name = getString(R.string.app_name);

        if (layoutRes() != null) setContentView(layoutRes());

        ButterKnife.bind(this);
        injectDagger();

        configureToolbar(toolbar, app_bar);

        initViews();
        configureFragment();
    }

    protected void initViews() {}

    private Integer layoutRes() {
        LayoutResActivity layoutRes = this.getClass().getAnnotation(LayoutResActivity.class);
        return layoutRes != null ? layoutRes.value() : null;
    }

    protected abstract void injectDagger();

    public BaseApp getBaseApp() {
        return ((BaseApp)getApplication());
    }

    public PresentationComponent getApplicationComponent() {
        return getBaseApp().getPresentationComponent();
    }

    protected <T extends BaseFragment> BaseFragment replaceFragmentIfItIsNotCurrentDisplayed(Class<T> clazz) {
        BaseFragment current = getCurrentPresenterFragment();
        if (current != null && current.getClass() == clazz) return current;
        return replaceFragment(clazz);
    }

    protected <T extends BaseFragment> BaseFragment replaceFragment(Class<T> clazz) {
        try {
            BaseFragment fragment = clazz.newInstance();
            getSupportFragmentManager().beginTransaction().replace(R.id.fl_fragment, fragment).commit();
            return fragment;
        } catch (InstantiationException e) {
            throw new IllegalStateException(e.getMessage());
        } catch (IllegalAccessException e) {
            throw new IllegalStateException(e.getMessage());
        }
    }

    public interface Behaviour {
        String FRAGMENT_CLASS_KEY = "fragment_class_key";
        String BUNDLE_FOR_FRAGMENT = "bundle_for_fragment";
        boolean SHOW_BACK_AS_DEFAULT = true;
        String SHOW_BACK_KEY = "show_back_key";
        String TITLE_KEY = "title_key";
        boolean SHOW_TOOLBAR_AS_DEFAULT = true;
        String SHOW_TOOLBAR = "show_toolbar";
    }

    public BaseFragment getCurrentPresenterFragment() {
        return (BaseFragment) getSupportFragmentManager().findFragmentById(R.id.fl_fragment);
    }

    private void configureFragment() {
        Bundle bundle = getIntent().getExtras();
        if (bundle == null || bundle.getSerializable(Behaviour.FRAGMENT_CLASS_KEY) == null) {
            Log.w(BaseActivity.class.getSimpleName(), "When using " + BaseActivity.class.getSimpleName() + " you could supply" +
                    " a fragment which extends from " + BaseFragment.class.getSimpleName() + " by extra argument in the intent" +
                    " as value and " + Behaviour.FRAGMENT_CLASS_KEY + " as key, but a <FrameLayout android:id=\"@id/fl_fragment\" .../>" +
                    " will be mandatory in your activity layout.");
            return;
        }

        Serializable serializable = bundle.getSerializable(Behaviour.FRAGMENT_CLASS_KEY);
        Class<BaseFragment> clazz = (Class<BaseFragment>) serializable;

        BaseFragment baseFragment = replaceFragment(clazz);
        Bundle bundleFragment = bundle.getBundle(Behaviour.BUNDLE_FOR_FRAGMENT);
        baseFragment.setArguments(bundleFragment);
    }

    private void configureToolbar(Toolbar toolbar, @Nullable AppBarLayout appBarLayout) {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        boolean showToolbar = Behaviour.SHOW_TOOLBAR_AS_DEFAULT;

        if (actionBar != null) {
            Bundle bundle = getIntent().getExtras();

            if (bundle != null) {
                boolean showBackKey = bundle.getBoolean(Behaviour.SHOW_BACK_KEY, Behaviour.SHOW_BACK_AS_DEFAULT);
                showToolbar = bundle.getBoolean(Behaviour.SHOW_TOOLBAR, showToolbar);
                actionBar.setDisplayHomeAsUpEnabled(showBackKey);
                String title = bundle.getString(Behaviour.TITLE_KEY);
                actionBar.setTitle(title);
            } else {
                actionBar.setDisplayHomeAsUpEnabled(Behaviour.SHOW_BACK_AS_DEFAULT);
                actionBar.setTitle(app_name);
            }
        }

        setStatusBarColor();

        if (appBarLayout != null)
            appBarLayout.setVisibility(showToolbar ? View.VISIBLE : View.GONE);
    }

    private void setStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        }
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    @Override public void setTitle(CharSequence title) {
        getSupportActionBar().setTitle(title);
    }

    public void showLoading() {
        materialDialog =  new MaterialDialog.Builder(this)
                .titleColorRes(R.color.colorPrimaryDark)
                .cancelable(false)
                .contentColor(ContextCompat.getColor(this, R.color.colorPrimaryDark))
                .widgetColorRes(R.color.colorPrimaryDark)
                .title(app_name)
                .content(getString(R.string.loading))
                .progress(true, 0)
                .show();
    }

    public void hideLoading() {
        if (materialDialog != null) materialDialog.dismiss();
    }

    @Override public void onBackPressed() {
        BaseFragment fragment = getCurrentPresenterFragment();
        BackButtonListener listener = fragment != null && fragment instanceof BackButtonListener ? (BackButtonListener) fragment : null;

        if (listener == null) {
            super.onBackPressed();
            return;
        }

        if (listener.onBackPressed()) super.onBackPressed();
    }

    public void showToast(Observable<String> oTitle) {
        oTitle.subscribe(title -> Toast.makeText(this, title, Toast.LENGTH_LONG).show());
    }

    public void showSnackBar(Observable<String> oTitle) {
        oTitle.subscribe(title -> Snackbar.make(findViewById(android.R.id.content), title, Snackbar.LENGTH_LONG).show());
    }

    /***
     * BasePresenterFragment can implement this interface to be notified when user performs a back action.
     */
    public interface BackButtonListener {
        /***
         * @return true if activity must handle back action, as removing itself from the stack
         */
        boolean onBackPressed();
    }
}