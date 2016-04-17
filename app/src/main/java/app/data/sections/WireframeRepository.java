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

package app.data.sections;

import javax.inject.Inject;

import app.data.foundation.Repository;
import app.data.foundation.UIUtils;
import app.data.foundation.cache.RxProviders;
import app.data.foundation.net.RestApi;
import io.rx_cache.EvictProvider;
import rx.Observable;

public class WireframeRepository extends Repository {

    @Inject public WireframeRepository(RestApi restApi, RxProviders rxProviders, UIUtils uiUtils) {
        super(restApi, rxProviders, uiUtils);
    }

    public <T> Observable<T> getWireframeCurrentObject() {
        return rxProviders
                .<T>getWireframeCurrentObject(Observable.just(null), new EvictProvider(false))
                .doOnError(throwable -> {
                    throw new WireframeException();
                });
    }

    public Observable<Void> setWireframeCurrentObject(Object object) {
        return rxProviders.getWireframeCurrentObject(Observable.just(object), new EvictProvider(true))
                .map(_I -> null);
    }

    public class WireframeException extends RuntimeException {

        public WireframeException() {
            super("There is not cached object in the wireframe");
        }

    }
}
