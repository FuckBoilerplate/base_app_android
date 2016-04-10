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

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.trello.rxlifecycle.components.support.RxFragment;

import javax.inject.Inject;

import butterknife.ButterKnife;
import app.presentation.internal.di.PresentationComponent;
import rx.Observable;
import rx_gcm.GcmReceiverUIForeground;
import rx_gcm.Message;

public abstract class BaseFragment<P extends Presenter> extends RxFragment implements BaseView, GcmReceiverUIForeground {
    @Inject protected P presenter;

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
        presenter.onCreatedView();
    }

    @Override public void onPause() {
        super.onPause();
        presenter.onPause();
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        presenter.onDestroy();
    }

    @Override public void onResume() {
        super.onResume();
        presenter.onResume();
    }

    protected abstract void injectDagger();
    protected void initViews() {}

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
        if (getActivity() instanceof BaseActivity)
            ((BaseActivity)getActivity()).showToast(oTitle);
    }

    @Override public void showSnackBar(Observable<String> oTitle) {
        if (getActivity() instanceof BaseActivity)
            ((BaseActivity)getActivity()).showSnackBar(oTitle);
    }

    @Override public void showLoading() {
        if (getActivity() instanceof BaseActivity)
            ((BaseActivity)getActivity()).showLoading();
    }

    @Override public void hideLoading() {
        if (getActivity() instanceof BaseActivity)
            ((BaseActivity)getActivity()).hideLoading();
    }

    @Override public void onTargetNotification(Observable<Message> ignore) {
        presenter.onTargetNotification(ignore);
    }

    @Override public void onMismatchTargetNotification(Observable<Message> oMessage) {
        presenter.onMismatchTargetNotification(oMessage);
    }

    @Override public String target() {
        return presenter.target();
    }
}
