package com.unicorn.csp.activity.main;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.malinskiy.materialicons.IconDrawable;
import com.malinskiy.materialicons.Iconify;
import com.mikepenz.iconics.typeface.FontAwesome;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.unicorn.csp.MyApplication;
import com.unicorn.csp.R;
import com.unicorn.csp.activity.base.ToolbarActivity;
import com.unicorn.csp.activity.news.FavoriteNewsActivity;
import com.unicorn.csp.activity.question.AddQuestionActivity;
import com.unicorn.csp.activity.search.BookSearchActivity;
import com.unicorn.csp.activity.search.NewsSearchActivity;
import com.unicorn.csp.adapter.viewpager.ViewPagerAdapter;
import com.unicorn.csp.greendao.Menu;
import com.unicorn.csp.greendao.MenuDao;
import com.unicorn.csp.other.greenmatter.ColorOverrider;
import com.unicorn.csp.other.greenmatter.SelectColorActivity;
import com.unicorn.csp.utils.AppUtils;
import com.unicorn.csp.utils.ConfigUtils;
import com.unicorn.csp.utils.JSONUtils;
import com.unicorn.csp.utils.ToastUtils;
import com.unicorn.csp.volley.MyVolley;

import org.apache.commons.io.FileUtils;
import org.apache.http.Header;
import org.json.JSONObject;

import java.io.File;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;


// clear
public class MainActivity extends ToolbarActivity {


    // todo 用更酷的方法来完成
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
        checkUpdate();
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
                .withCloseOnClick(false)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName("我的关注").withIcon(FontAwesome.Icon.faw_star).withIdentifier(1).withCheckable(false),
                        new PrimaryDrawerItem().withName("我要提问").withIcon(FontAwesome.Icon.faw_question_circle).withIdentifier(2).withCheckable(false),
                        new PrimaryDrawerItem().withName("主题色彩").withIcon(FontAwesome.Icon.faw_paint_brush).withIdentifier(4).withCheckable(false),
                        new PrimaryDrawerItem().withName("用户登出").withIcon(FontAwesome.Icon.faw_sign_out).withIdentifier(5).withCheckable(false),
                        new PrimaryDrawerItem().withName("更多设置").withIcon(FontAwesome.Icon.faw_cog).withIdentifier(6).withCheckable(false)
//                        new SwitchDrawerItem().withName("隐藏标题栏").withChecked(false).withOnCheckedChangeListener(new OnCheckedChangeListener() {
//                            @Override
//                            public void onCheckedChanged(IDrawerItem iDrawerItem, CompoundButton compoundButton, boolean b) {
//                                toggleToolbar();
//                            }
//                        })
                )
                .withSelectedItem(-1)
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(AdapterView<?> parent, View view, int position, long id, IDrawerItem drawerItem) {
                        switch (drawerItem.getIdentifier()) {
                            case 1:
                                startActivity(FavoriteNewsActivity.class);
//                                startActivity(DbInspectorActivity.class);
                                break;
                            case 2:
                                startActivity(AddQuestionActivity.class);
                                break;
                            case 4:
                                startSelectColorActivity();
                                break;
                            case 5:
                                showSignOutDialog();
                                break;
                            case 6:
                                startActivity(SettingActivity.class);
                        }
                        return false;
                    }
                }).build();
    }


    // ========================== 用户登出 ==========================

    private MaterialDialog showSignOutDialog() {

        return new MaterialDialog.Builder(this)
                .title("确认登出？")
                .positiveText("确认")
                .negativeText("取消")
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        startActivityAndFinish(LoginActivity.class);
                    }
                })
                .show();
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

        Intent intent = new Intent(this, SelectColorActivity.class);
        startActivityForResult(intent, 2333);
    }


    // ========================== 底部栏 ==========================

    @Bind({R.id.llHotSpot, R.id.llStudyField, R.id.llBookCity, R.id.llMyStudy, R.id.llSocialRegion})
    List<LinearLayout> bottomTabList;

    private void initBottomTabList() {

        for (LinearLayout bottomTab : bottomTabList) {
            changeBottomTabColor(bottomTab, getResources().getColor(R.color.tab_unselected));
        }
    }

    @OnClick({R.id.llHotSpot, R.id.llStudyField, R.id.llBookCity, R.id.llMyStudy, R.id.llSocialRegion})
    public void onBottomTabClick(LinearLayout bottomTab) {
        selectBottomTab(bottomTabList.indexOf(bottomTab), true);
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
        if (replaceFragment) {
            replaceFragment(index);
        }
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
        Fragment fragment = ViewPagerAdapter.getFragmentByMenu(menu, true);
        replaceFragment_(fragment);
    }

    private Menu findMenuByName(String name) {

        List<Menu> menuList = MyApplication.getMenuDao().queryBuilder()
                .where(MenuDao.Properties.Name.eq(name))
                .list();
        return menuList.get(0);
    }

    private void replaceFragment_(Fragment fragment) {

        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
    }


    // ========================== 查询 ==========================

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {

        getMenuInflater().inflate(R.menu.main, menu);
        menu.findItem(R.id.search).setIcon(getActionDrawable());
        return super.onCreateOptionsMenu(menu);
    }

    private Drawable getActionDrawable() {

        return new IconDrawable(this, Iconify.IconValue.zmdi_search)
                .colorRes(android.R.color.white)
                .actionBarSize();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.search_news:
                startActivity(NewsSearchActivity.class);
                return true;
            case R.id.search_book:
                startActivity(BookSearchActivity.class);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    // ========================== 检查更新 ==========================

    private void checkUpdate() {

        MyVolley.addRequest(new JsonObjectRequest(getCheckUpdateUrl(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        boolean needUpdate = JSONUtils.getBoolean(response, "needUpdate", false);
                        if (needUpdate) {
                            String apk = JSONUtils.getString(response, "apk", "");
                            showConfirmUpdateDialog(apk);
                        }
                    }
                },
                MyVolley.getDefaultErrorListener()));
    }

    private String getCheckUpdateUrl() {

        Uri.Builder builder = Uri.parse(ConfigUtils.getBaseUrl() + "/api/v1/app/checkUpdate?").buildUpon();
        builder.appendQueryParameter("versionName", AppUtils.getVersionName());
        return builder.toString();
    }

    private MaterialDialog showConfirmUpdateDialog(final String apk) {

        // todo 添加更新细节
        return new MaterialDialog.Builder(this)
                .title("检测到新版本，是否立即更新？")
                .cancelable(false)
                .positiveText("立即更新")
                .negativeText("下次再说")
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        downloadApk(apk);
                    }
                })
                .show();
    }

    private void downloadApk(String apk) {

        final MaterialDialog downloadDialog = showDownloadApkDialog();
        String url = ConfigUtils.getBaseUrl() + apk;
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, new FileAsyncHttpResponseHandler(MyApplication.getInstance()) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, File response) {

                downloadDialog.dismiss();
                installApk(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, File file) {

                downloadDialog.dismiss();
                ToastUtils.show("下载失败");
            }

            @Override
            public void onProgress(long bytesWritten, long totalSize) {

                downloadDialog.setMaxProgress((int) totalSize);
                downloadDialog.setProgress((int) bytesWritten);
            }
        });
    }

    private MaterialDialog showDownloadApkDialog() {

        return new MaterialDialog.Builder(this)
                .title("下载APK中")
                .progress(false, 100)
                .cancelable(false)
                .show();
    }

    private void installApk(File response) {

        String apkPath = ConfigUtils.getDownloadDirPath() + "/csp.apk";
        File apk = new File(apkPath);
        if (apk.exists()) {
            apk.delete();
        }
        try {
            FileUtils.copyFile(response, apk);
        } catch (Exception e) {
            //
        }

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(apk), "application/vnd.android.package-archive");
        startActivity(intent);
    }
}
