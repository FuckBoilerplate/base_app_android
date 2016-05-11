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

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Store those fragments names which require to be called onSyncScreen() when they come from a background state to a foreground state.
 */
@Singleton public final class SyncScreens {
    private final List<String> pendingScreens;

    @Inject public SyncScreens() {
        this.pendingScreens = new ArrayList<>();
    }

    public void addScreen(String screen) {
        if (!pendingScreens.contains(screen)) pendingScreens.add(screen);
    }

    public boolean needToSync(Matcher matcher) {
        boolean needToSync = false;

        int index = 0;

        for (String screen : pendingScreens) {
            if (matcher.matchesTarget(screen)) {
                needToSync = true;
                break;
            }

            index++;
        }

        if (needToSync) pendingScreens.remove(index);
        return needToSync;
    }

    public interface Matcher {
        boolean matchesTarget(String key);
    }
}
