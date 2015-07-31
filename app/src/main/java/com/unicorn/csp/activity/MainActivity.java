package com.unicorn.csp.activity;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.malinskiy.materialicons.IconDrawable;
import com.malinskiy.materialicons.Iconify;
import com.mikepenz.iconics.typeface.FontAwesome;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SwitchDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.OnCheckedChangeListener;
import com.unicorn.csp.MyApplication;
import com.unicorn.csp.R;
import com.unicorn.csp.activity.base.ToolbarActivity;
import com.unicorn.csp.fragment.TestFragment;
import com.unicorn.csp.fragment.ViewPagerFragmentL1;
import com.unicorn.csp.greendao.Menu;
import com.unicorn.csp.greendao.MenuDao;
import com.unicorn.csp.other.greenmatter.ColorOverrider;
import com.unicorn.csp.other.greenmatter.SelectColorActivity;
import com.unicorn.csp.utils.ToastUtils;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import im.dino.dbinspector.activities.DbInspectorActivity;


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
                        new PrimaryDrawerItem().withName("我的收藏").withIcon(FontAwesome.Icon.faw_star).withIdentifier(1).withCheckable(false),
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
                            case 1:
                                startActivity(FavoriteActivity.class);
                                break;
                            case 2:
                                startActivity(QuestionActivity.class);
                                break;
                            case 3:
                                startActivity(DbInspectorActivity.class);
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
                ToastUtils.show("再按一次退出");
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

        throw  new RuntimeException("sdf");
//        Intent intent = new Intent(this, SelectColorActivity.class);
//        startActivityForResult(intent, 2333);
    }


    // ========================== 底部栏 ==========================

    @Bind({R.id.llHotSpot, R.id.llStudyField, R.id.llBookCity, R.id.llMyStudy, R.id.llSocialRegion})
    List<LinearLayout> bottomTabList;

    @SuppressWarnings("SuspiciousMethodCalls")
    @OnClick({R.id.llHotSpot, R.id.llStudyField, R.id.llBookCity, R.id.llMyStudy, R.id.llSocialRegion})
    public void onBottomTabClick(LinearLayout bottomTab) {
        selectBottomTab(bottomTabList.indexOf(bottomTab), true);
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

        String[] names = {"资讯热点", "学习园地", "网上书城", "我的学习", "互动专区"};
        Menu menu = findMenuByName(names[index]);
        if (menu.getChildren().size() == 0) {
            replaceFragment_(new TestFragment());
            return;
        }

        // 1级 ViewPagerFragment
        ViewPagerFragmentL1 viewPagerFragmentL1 = new ViewPagerFragmentL1();
        Bundle bundle = new Bundle();
        bundle.putSerializable("menu", menu);
        viewPagerFragmentL1.setArguments(bundle);
        replaceFragment_(viewPagerFragmentL1);
    }

    private void replaceFragment_(Fragment fragment) {

        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
    }


    private Menu findMenuByName(String name) {

        List<Menu> result = MyApplication.getMenuDao().queryBuilder()
                .where(MenuDao.Properties.Name.eq(name))
                .list();

        return result.get(0);
    }


    // ========================== 查询 ==========================

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {

        getMenuInflater().inflate(R.menu.activity_main, menu);
        menu.findItem(R.id.search).setIcon(getSearchDrawable());
        return super.onCreateOptionsMenu(menu);
    }

    private Drawable getSearchDrawable() {

        return new IconDrawable(this, Iconify.IconValue.zmdi_search)
                .colorRes(android.R.color.white)
                .actionBarSize();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.search:
                startActivity(SearchActivity.class);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
