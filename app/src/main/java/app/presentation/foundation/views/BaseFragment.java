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

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.trello.rxlifecycle.RxLifecycle;
import com.trello.rxlifecycle.components.support.RxFragment;

import org.base_app_android.BuildConfig;
import org.base_app_android.R;

import javax.inject.Inject;

import app.data.foundation.analytics.GoogleAnalyticsSender;
import app.data.foundation.net.BadResponseException;
import app.domain.foundation.gcm.GcmNotification;
import app.presentation.foundation.PresenterFragment;
import app.presentation.foundation.SyncScreens;
import app.presentation.foundation.dagger.PresentationComponent;
import app.presentation.sections.Wireframe;
import butterknife.ButterKnife;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.exceptions.CompositeException;
import rx.schedulers.Schedulers;
import rx_gcm.GcmReceiverUIForeground;
import rx_gcm.Message;

public abstract class BaseFragment<P extends PresenterFragment> extends RxFragment implements GcmReceiverUIForeground, SyncScreens.Matcher {
    @Inject protected P presenter;
    @Inject protected SyncScreens syncScreens;
    @Inject protected Wireframe wireframe;
    @Inject protected GoogleAnalyticsSender googleAnalytics;

    @Nullable @Override public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(layoutRes(), container, false);
        ButterKnife.bind(this, view);

        injectDagger();
        return view;
    }

    private Integer layoutRes() {
        LayoutResFragment layoutRes = this.getClass().getAnnotation(LayoutResFragment.class);
        return layoutRes != null ? layoutRes.value() : null;
    }

    @Override public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initViews();
    }

    @Override public void onResume() {
        super.onResume();

        boolean needToSync = syncScreens.needToSync(this);
        if (needToSync) onSyncScreen();
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    protected abstract void injectDagger();
    @Nullable protected abstract String getScreenNameForGoogleAnalytics();

    protected void initViews() {
        String screenName = getScreenNameForGoogleAnalytics();
        if (screenName != null) googleAnalytics.send(screenName);
    }

    /**
     * Override this method and do not call super to add functionality when sync screen is called
     */
    protected void onSyncScreen() {
        throw new RuntimeException(getString(R.string.sync_screen_error, getClass()));
    }

    protected PresentationComponent getApplicationComponent() {
        return ((BaseActivity)getActivity()).getApplicationComponent();
    }

    protected void replaceFragment(int id, Fragment fragment) {
        FragmentManager fragmentManager = getChildFragmentManager();
        fragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        fragmentManager.beginTransaction()
                .replace(id, fragment, fragment.getClass().getSimpleName())
                .commit();
    }

    public void showToast(Observable<String> oTitle) {
        ((BaseActivity)getActivity()).showToast(oTitle);
    }

    public void showSnackBar(Observable<String> oTitle) {
        ((BaseActivity)getActivity()).showSnackBar(oTitle);
    }

    public void showLoading() {
        ((BaseActivity)getActivity()).showLoading();
    }

    public void hideLoading() {
        ((BaseActivity)getActivity()).hideLoading();
    }

    @Override public void onTargetNotification(Observable<Message> ignore) {
        onSyncScreen();
    }

    @Override public void onMismatchTargetNotification(Observable<Message> oMessage) {
        Observable<String> oGcmNotification = oMessage
                .doOnNext(message -> syncScreens.addScreen(message.target()))
                .map(GcmNotification::getMessageFromGcmNotification)
                .map(gcmMessageNotification -> gcmMessageNotification.getTitle() + System.getProperty("line.separator") + gcmMessageNotification.getBody());

        showToast(oGcmNotification);
    }

    /**
     * Override this method if the fragment requires to be notified, whether by a gcm notification, or due to some other internal event
     * handled by screensSync instance.
     */
    @Override public boolean matchesTarget(String key) {
        return false;
    }

    protected void setTitle(String title){
        BaseActivity baseFragmentActivity = (BaseActivity) getActivity();
        baseFragmentActivity.setTitle(title);
    }

    protected void back() {
        wireframe.popCurrentScreen();
    }

    protected <T> Observable.Transformer<T, T> safely() {
        return observable -> observable.compose(applySchedulers())
                .compose(RxLifecycle.bindFragment(lifecycle()));
    }

    protected <T> Observable.Transformer<T, T> safelyLoading() {
        return observable -> observable.compose(safely()).compose(applyLoading());
    }

    protected <T> Observable.Transformer<T, T> safelyReport() {
        return observable -> observable.compose(safely())
                .doOnError(throwable -> {
                    if (BuildConfig.DEBUG) showToast(parseException(throwable));
                    else showSnackBar(parseException(throwable));
                })
                .onErrorResumeNext(throwable -> Observable.empty());
    }

    protected <T> Observable.Transformer<T, T> safelyReportLoading() {
        return observable -> observable.compose(safelyReport()).compose(applyLoading());
    }

    protected <T> Observable.Transformer<T, T> applySchedulers() {
        return observable -> observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private  <T> Observable.Transformer<T, T> applyLoading() {
        return observable -> observable.doOnSubscribe(() -> showLoading())
                .doOnCompleted(() -> hideLoading());
    }

    public Observable<String> parseException(Throwable throwable) {
        if (!BuildConfig.DEBUG && !(throwable instanceof BadResponseException))
            return Observable.just(getString(R.string.errors_happen));

        String message = throwable.getMessage();

        if(throwable.getCause() != null) message += System.getProperty("line.separator") + throwable.getCause().getMessage();

        if (throwable instanceof CompositeException) {
            message += System.getProperty("line.separator");
            CompositeException compositeException = (CompositeException) throwable;

            for (Throwable exception : compositeException.getExceptions()) {
                String exceptionName = exception.getClass().getSimpleName();
                String exceptionMessage = exception.getMessage() != null ? exception.getMessage() : "";
                message += exceptionName + " -> " + exceptionMessage + System.getProperty("line.separator");
            }
        }

        return Observable.just(message);
    }

}
