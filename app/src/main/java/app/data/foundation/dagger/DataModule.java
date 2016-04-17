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

package app.data.foundation.dagger;

import java.io.File;

import javax.inject.Singleton;

import app.data.foundation.UIUtils;
import app.data.foundation.cache.RxProviders;
import app.data.foundation.net.RestApi;
import app.data.foundation.net.mock.RestApiMock;
import app.presentation.foundation.BaseApp;
import dagger.Module;
import dagger.Provides;
import io.rx_cache.internal.RxCache;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Dagger module for data layer.
 */
@Module
public class DataModule {

    @Singleton @Provides public RestApi provideRestApi() {
        boolean mockMode = false;//BuildConfig.DEBUG;
        if (mockMode) return new RestApiMock();

        return new Retrofit.Builder()
                .baseUrl(RestApi.URL_BASE)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(RestApi.class);
    }

    @Singleton @Provides public RxProviders provideRxProviders(UIUtils uiUtils) {
        return new RxCache.Builder()
                .persistence(uiUtils.getFilesDir())
                .using(RxProviders.class);
    }

    @Singleton @Provides UIUtils provideUiUtils(BaseApp baseApp) {
        return new UIUtils() {
            @Override public String getLang() {
                return baseApp.getResources().getConfiguration().locale.getLanguage();
            }

            @Override public String getString(int idResource) {
                return baseApp.getString(idResource);
            }

            @Override public File getFilesDir() {
                return baseApp.getFilesDir();
            }
        };
    }
}
