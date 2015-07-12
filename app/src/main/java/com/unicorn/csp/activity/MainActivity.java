package com.unicorn.csp.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;

import com.mikepenz.iconics.typeface.FontAwesome;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SwitchDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.OnCheckedChangeListener;
import com.unicorn.csp.R;
import com.unicorn.csp.activity.base.ToolbarActivity;

public class MainActivity extends ToolbarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initToolbar("",false);
        initViews();
    }

    private void initViews(){

        initDrawer();
    }

    // ========================== 侧滑菜单 ==========================

    Drawer drawer;

    private void initDrawer() {

        drawer = new DrawerBuilder()
                .withActivity(this)
                .withTranslucentStatusBar(false)
                .withToolbar(getToolbar())
                .withActionBarDrawerToggleAnimated(true)
                .withHeader(R.layout.drawer_header)
                .withHeaderDivider(true)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName("我的收藏").withIcon(FontAwesome.Icon.faw_star).withIdentifier(1).withCheckable(false).withBadge("12"),
                        new PrimaryDrawerItem().withName("我要提问").withIcon(FontAwesome.Icon.faw_question_circle).withIdentifier(2).withCheckable(false),
                        new DividerDrawerItem(),
                        new PrimaryDrawerItem().withName("修改密码").withIcon(FontAwesome.Icon.faw_lock).withIdentifier(3).withCheckable(false),
                        new PrimaryDrawerItem().withName("主题色彩").withIcon(FontAwesome.Icon.faw_paint_brush).withIdentifier(4).withCheckable(false),
                        new PrimaryDrawerItem().withName("更多设置").withIcon(FontAwesome.Icon.faw_cog).withIdentifier(5).withCheckable(false),
                        new DividerDrawerItem(),
                        new SwitchDrawerItem().withName("隐藏标题栏").withChecked(false).withOnCheckedChangeListener(new OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(IDrawerItem iDrawerItem, CompoundButton compoundButton, boolean b) {
                                toggleToolbar();
                            }
                        })
                )
                .withSelectedItem(-1)
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(AdapterView<?> parent, View view, int position, long id, IDrawerItem drawerItem) {
                        switch (drawerItem.getIdentifier()) {
                            case 2:
                                break;
                            case 4:
                                break;
                        }
                        return false;
                    }
                }).build();
    }

}
