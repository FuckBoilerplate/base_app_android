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

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import org.base_app_android.R;

import app.domain.dashboard.ItemMenu;
import butterknife.Bind;
import butterknife.ButterKnife;
import library.recycler_view.OkRecyclerViewAdapter;

public class ItemMenuViewGroup extends FrameLayout implements OkRecyclerViewAdapter.Binder<ItemMenu> {
    @Bind(R.id.iv_icon) protected ImageView iv_icon;
    @Bind(R.id.tv_title) protected TextView tv_title;

    public ItemMenuViewGroup(Context context) {
        super(context);
        init();
    }

    public ItemMenuViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.item_menu_view_group, this, true);
        ButterKnife.bind(this, view);
    }

    @Override public void bind(ItemMenu itemMenu, int position) {
        iv_icon.setImageResource(itemMenu.getIdDrawableResource());
        tv_title.setText(itemMenu.getTitle());
    }
}
