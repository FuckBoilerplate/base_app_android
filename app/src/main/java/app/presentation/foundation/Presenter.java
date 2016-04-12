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

package app.presentation.foundation;

import android.content.Context;
import android.support.annotation.StringRes;

import com.trello.rxlifecycle.RxLifecycle;

import app.domain.gcm_notifications.GcmNotification;
import app.presentation.sections.Wireframe;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.exceptions.CompositeException;
import rx.schedulers.Schedulers;
import rx_gcm.Message;

public abstract class Presenter<V extends BaseView> {
    protected V view;
    protected final Wireframe wireframe;

    protected Presenter(Wireframe wireframe) {
        this.wireframe = wireframe;
    }

    public void attachView(V view) {
        this.view = view;
    }

    public void onCreatedView() {}

    public void onResume() {}

    public void onPause() {}

    public void onDestroy() {}

    public void back() {
        wireframe.popCurrentScreen();
    }

    protected <T> Observable.Transformer<T, T> safely() {
        return observable -> observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(bindToLifeCycle());
    }

    protected <T> Observable.Transformer<T, T> safelyReport() {
        return observable -> observable.compose(safely())
                .doOnError(throwable -> view.showToast(parseException(throwable)))
                .onErrorResumeNext(throwable -> Observable.empty());
    }

    protected <T> Observable.Transformer<T, T> safelyReportSnackbar() {
        return observable -> observable.compose(safely())
                .doOnError(throwable -> view.showSnackBar(parseException(throwable)))
                .onErrorResumeNext(throwable -> Observable.empty());
    }

    protected <T> Observable.Transformer<T, T> applyLoading() {
        return observable -> observable.doOnSubscribe(() -> view.showLoading())
                .doOnCompleted(() -> view.hideLoading());
    }

    public Observable<String> parseException(Throwable throwable) {
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

    protected Observable.Transformer bindToLifeCycle() {
        if (view instanceof BaseActivity) return RxLifecycle.bindActivity(((BaseActivity) view).lifecycle());
        if (view instanceof BaseFragment) return RxLifecycle.bindFragment(((BaseFragment) view).lifecycle());

        throw new RuntimeException("The associated view must be a BaseActivity or a BaseFragment");
    }

    public void onTargetNotification(Observable<Message> ignore) {}

    public void onMismatchTargetNotification(Observable<Message> oMessage) {
        Observable<String> oGcmNotification = oMessage
                .flatMap(GcmNotification::getMessageFromGcmNotification)
                .map(gcmNotification -> gcmNotification.getTitle() + System.lineSeparator() + gcmNotification.getBody());

        view.showToast(oGcmNotification);
    }

    public String target() {
        return "";
    }

    public String getString(@StringRes int resId) {
        Context context = null;

        if (view instanceof BaseActivity) context = ((BaseActivity) view);
        if (view instanceof BaseFragment) context = ((BaseFragment) view).getActivity();
        if (context == null) throw new RuntimeException("The associated view must be a BaseActivity or a BaseFragment");

        return context.getString(resId);
    }
}
