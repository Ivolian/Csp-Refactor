package com.unicorn.csp.activity;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import com.unicorn.csp.other.greenmatter.ColorOverrider;
import com.unicorn.csp.other.greenmatter.SelectColorActivity;
import com.unicorn.csp.utils.ToastUtils;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

public class MainActivity extends ToolbarActivity {


    // ========================== 保存选中项 ==========================

    final String SELECTED = "selected";

    int selected = -1;


    @Override
    protected void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);
        outState.putInt(SELECTED, selected);
    }


    // ========================== onCreate ==========================

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initToolbar("", false);
        initViews();

        if (savedInstanceState == null) {
            selectBottomTab(0, true);
        } else {
            selectBottomTab(savedInstanceState.getInt(SELECTED), false);
        }
    }

    private void initViews() {

        initDrawer();
        initBottomTabList();
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
                                startSelectColorActivity();
                                break;
                        }
                        return false;
                    }
                }).build();
    }


    // ========================== 再按一次退出 ==========================

    long exitTime = 0;

    @Override
    public void onBackPressed() {

        if (drawer.isDrawerOpen()) {
            drawer.closeDrawer();
        } else {
            if (System.currentTimeMillis() - exitTime > 2000) {
                ToastUtils.show(this, "再按一次退出");
                exitTime = System.currentTimeMillis();
            } else {
                super.onBackPressed();
            }
        }
    }


    // ========================== 主题色彩 ==========================

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == SelectColorActivity.SELECT_COLOR_SUCCESS) {
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    recreate();
                }
            });
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void startSelectColorActivity() {

        Intent intent = new Intent(this, SelectColorActivity.class);
        startActivityForResult(intent, 2333);
    }


    // ========================== 底部栏 ==========================

    @Bind({R.id.llHotSpot, R.id.llStudyField, R.id.llBookCity, R.id.llMyStudy, R.id.llSocialRegion})
    List<LinearLayout> bottomTabList;

    @SuppressWarnings("SuspiciousMethodCalls")
    @OnClick({R.id.llHotSpot, R.id.llStudyField, R.id.llBookCity, R.id.llMyStudy, R.id.llSocialRegion})
    public void onBottomTabClick(LinearLayout v) {
        selectBottomTab(bottomTabList.indexOf(v), true);
    }

    private void initBottomTabList() {

        for (LinearLayout bottomTab : bottomTabList) {
            changeBottomTabColor(bottomTab, getResources().getColor(R.color.tab_unselected));
        }
    }

    private void selectBottomTab(int index, boolean replaceFragment) {

        // 如果已经选中
        if (index == selected) {
            return;
        }
        changeBottomTabColor(bottomTabList.get(index), ColorOverrider.getInstance(this).getColorAccent());
        if (selected != -1) {
            changeBottomTabColor(bottomTabList.get(selected), getResources().getColor(R.color.tab_unselected));
        }
        changeToolbarTitle(index);
        if (replaceFragment) replaceFragment(index);
        selected = index;
    }

    private void changeBottomTabColor(LinearLayout bottomTab, int color) {

        ((ImageView) bottomTab.getChildAt(0)).setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        ((TextView) bottomTab.getChildAt(1)).setTextColor(color);
    }

    private void changeToolbarTitle(int index) {

        switch (index) {
            case 0:
                setToolbarTitle(R.string.hot_spot);
                break;
            case 1:
                setToolbarTitle(R.string.study_field);
                break;
            case 2:
                setToolbarTitle(R.string.book_city);
                break;
            case 3:
                setToolbarTitle(R.string.my_study);
                break;
            case 4:
                setToolbarTitle(R.string.social_region);
                break;
        }
    }

    private void replaceFragment(int index) {

        // todo
    }

    private void replaceFragment_(Fragment fragment) {

        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
    }

}
