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

import com.trello.rxlifecycle.FragmentEvent;
import com.trello.rxlifecycle.components.support.RxFragment;

import org.base_app_android.R;

import javax.inject.Inject;

import app.domain.foundation.gcm.GcmNotification;
import app.presentation.foundation.PresenterFragment;
import app.presentation.foundation.SyncScreens;
import app.presentation.foundation.dagger.PresentationComponent;
import butterknife.ButterKnife;
import rx.Observable;
import rx_gcm.GcmReceiverUIForeground;
import rx_gcm.Message;

public abstract class BaseFragment<P extends PresenterFragment> extends RxFragment implements BaseViewFragment, GcmReceiverUIForeground {
    @Inject protected P presenter;
    @Inject protected SyncScreens syncScreens;

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

        presenter.attachView(this);
        initViews();
    }

    @Override public void onResume() {
        super.onResume();

        boolean needToSync = syncScreens.needToSync(target());
        if (needToSync) onSyncScreen();
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    protected abstract void injectDagger();
    protected void initViews() {}

    /**
     * Override this method to add functionality on sync screen call
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

    @Override public void showToast(Observable<String> oTitle) {
        ((BaseActivity)getActivity()).showToast(oTitle);
    }

    @Override public void showSnackBar(Observable<String> oTitle) {
        ((BaseActivity)getActivity()).showSnackBar(oTitle);
    }

    @Override public void showLoading() {
        ((BaseActivity)getActivity()).showLoading();
    }

    @Override public void hideLoading() {
        ((BaseActivity)getActivity()).hideLoading();
    }

    @Override public Observable<FragmentEvent> lifeCycle() {
        return lifecycle();
    }

    @Override public void onTargetNotification(Observable<Message> ignore) {}

    @Override public void onMismatchTargetNotification(Observable<Message> oMessage) {
        Observable<String> oGcmNotification = oMessage
                .doOnNext(message -> syncScreens.addScreen(message.target()))
                .map(GcmNotification::getMessageFromGcmNotification)
                .map(gcmMessageNotification -> gcmMessageNotification.getTitle() + "\n" + gcmMessageNotification.getBody());
        showToast(oGcmNotification);
    }

    /**
     * Override this method if the fragment requires to be notified, whether by a gcm notification, or due to some other internal event
     * handled by screensSync instance.
     */
    @Override public String target() {
        return "";
    }

    protected void setTittle(String tittle){
        BaseActivity baseFragmentActivity = (BaseActivity) getActivity();
        baseFragmentActivity.setTitle(tittle);
    }
}
