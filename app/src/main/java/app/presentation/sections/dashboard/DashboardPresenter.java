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

package app.presentation.sections.dashboard;

import org.base_app_android.R;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import app.data.sections.gcm_notifications.NotificationRepository;
import app.domain.dashboard.ItemMenu;
import app.presentation.foundation.Presenter;
import app.presentation.sections.Wireframe;
import rx.Observable;

public class DashboardPresenter extends Presenter<DashBoardActivity> {
    private final static int ID_USERS = 1, ID_USER = 2, ID_SEARCH = 3;

    @Inject public DashboardPresenter(Wireframe wireframe, NotificationRepository notificationRepository) {
        super(wireframe, notificationRepository);
    }

    @Override public void onCreatedView() {
        super.onCreatedView();
        view.showItemsMenu(itemsMenu().doOnSubscribe(() -> view.showUsers()));
    }

    private Observable<List<ItemMenu>> itemsMenu() {
        List<ItemMenu> itemMenus = new ArrayList<>();

        itemMenus.add(new ItemMenu(ID_USERS, view.getString(R.string.users), R.drawable.ic_users));
        itemMenus.add(new ItemMenu(ID_USER, view.getString(R.string.user), R.drawable.ic_user));
        itemMenus.add(new ItemMenu(ID_SEARCH, view.getString(R.string.find_user), R.drawable.ic_search));

        return Observable.just(itemMenus).compose(safely());
    }

    public void setSelectedItemMenu(ItemMenu itemMenu) {
        if (itemMenu.getId() == ID_USERS)
            view.showUsers();
        else if (itemMenu.getId() == ID_USER)
            view.showUser();
        else
            view.showUserSearch();
    }
}
