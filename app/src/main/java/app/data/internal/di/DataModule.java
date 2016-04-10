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

package app.data.internal.di;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import app.data.cache.RxProviders;
import app.data.net.RestApi;
import io.rx_cache.internal.RxCache;
import app.presentation.foundation.BaseApp;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Dagger module for data layer.
 */
@Module
public class DataModule {

    @Singleton @Provides RestApi provideRestApi() {
        return new Retrofit.Builder()
                .baseUrl(RestApi.URL_BASE)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(RestApi.class);
    }

    @Singleton @Provides RxProviders provideRxProviders(BaseApp baseApp) {
        return new RxCache.Builder()
                .persistence(baseApp.getFilesDir())
                .using(RxProviders.class);
    }
}
