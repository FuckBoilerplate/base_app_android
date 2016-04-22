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

package app.presentation.sections;

import android.content.Intent;
import android.os.Bundle;

import org.base_app_android.R;

import javax.inject.Inject;

import app.data.sections.WireframeRepository;
import app.presentation.foundation.BaseApp;
import app.presentation.foundation.views.BaseActivity;
import app.presentation.foundation.views.SingleActivity;
import app.presentation.sections.dashboard.DashBoardActivity;
import app.presentation.sections.user_demo.detail.UserFragment;
import app.presentation.sections.user_demo.search.SearchUserFragment;
import rx.Observable;

/**
 * Provides the routing for the application screens.
 */
public class Wireframe {
    private final BaseApp baseApp;
    private final WireframeRepository wireframeRepository;

    @Inject public Wireframe(BaseApp baseApp, WireframeRepository wireframeRepository) {
        this.baseApp = baseApp;
        this.wireframeRepository = wireframeRepository;
    }

    public void dashboard() {
        baseApp.getLiveActivity().startActivity(new Intent(baseApp, DashBoardActivity.class));
    }

    public void userScreen() {
        Bundle bundle = new Bundle();
        bundle.putString(BaseActivity.Behaviour.TITLE_KEY, baseApp.getString(R.string.user));
        bundle.putSerializable(BaseActivity.Behaviour.FRAGMENT_CLASS_KEY, UserFragment.class);

        Intent intent = new Intent(baseApp, SingleActivity.class);
        intent.putExtras(bundle);
        baseApp.getLiveActivity().startActivity(intent);
    }

    public void searchUserScreen() {
        Bundle bundleFragment = new Bundle();
        bundleFragment.putString(SearchUserFragment.HELLO_FROM_BUNDLE_WIREFRAME_KEY, "Hi from wireframe bundle");

        Bundle bundle = new Bundle();
        bundle.putString(BaseActivity.Behaviour.TITLE_KEY, baseApp.getString(R.string.find_user));
        bundle.putSerializable(BaseActivity.Behaviour.FRAGMENT_CLASS_KEY, SearchUserFragment.class);
        bundle.putBundle(BaseActivity.Behaviour.BUNDLE_FOR_FRAGMENT, bundleFragment);

        Intent intent = new Intent(baseApp, SingleActivity.class);
        intent.putExtras(bundle);
        baseApp.getLiveActivity().startActivity(intent);
    }

    public <T> Observable<T> getWireframeCurrentObject() {
        return wireframeRepository.getWireframeCurrentObject();
    }

    public Observable<Void> setWireframeCurrentObject(Object oObject) {
        return wireframeRepository.setWireframeCurrentObject(oObject);
    }

    public void popCurrentScreen() {
        baseApp.getLiveActivity().finish();
    }
}
