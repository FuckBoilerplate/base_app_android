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

import app.data.foundation.UIUtils;
import app.presentation.sections.Wireframe;

public abstract class PresenterFragment {
    protected final Wireframe wireframe;
    protected final UIUtils uiUtils;

    protected PresenterFragment(Wireframe wireframe, UIUtils uiUtils) {
        this.wireframe = wireframe;
        this.uiUtils = uiUtils;
    }

    public void back() {
        wireframe.popCurrentScreen();
    }
}
