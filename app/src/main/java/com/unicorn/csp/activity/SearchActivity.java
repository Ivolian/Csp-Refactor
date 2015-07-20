package com.unicorn.csp.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.speech.RecognizerIntent;

import com.malinskiy.materialicons.IconDrawable;
import com.malinskiy.materialicons.Iconify;
import com.quinny898.library.persistentsearch.SearchBox;
import com.quinny898.library.persistentsearch.SearchResult;
import com.unicorn.csp.MyApplication;
import com.unicorn.csp.R;
import com.unicorn.csp.activity.base.ButterKnifeActivity;
import com.unicorn.csp.fragment.NewsFragment;
import com.unicorn.csp.greendao.SearchHistory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import butterknife.Bind;


public class SearchActivity extends ButterKnifeActivity implements SearchBox.SearchListener {

    @Bind(R.id.searchbox)
    SearchBox searchBox;

    // 队列，先进后出，用于维护用户查询历史
    Queue<String> titleQueue = new LinkedList<>();

    NewsFragment newsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initViews();
    }

    private void initViews() {

        initSearchBoxAndQueue();
        initNewsFragment();
    }

    private void initSearchBoxAndQueue() {

        searchBox.enableVoiceRecognition(this);
        searchBox.setLogoText("请输入查询内容");
        searchBox.setSearchListener(this);
        List<SearchHistory> searchHistoryList = MyApplication.getSearchHistoryDao().loadAll();
        for (SearchHistory searchHistory : searchHistoryList) {
            titleQueue.add(searchHistory.getTitle());
        }
        refreshSearchBox();
    }

    private void initNewsFragment() {

        newsFragment = new NewsFragment();
        // 不知道为什么，这里 newsFragment 不会调用 setUserVisibleHint 方法，所以需要手动调用 initPrepare 方法
        newsFragment.initPrepare();
        Bundle bundle = new Bundle();
        newsFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.container, newsFragment).commit();
    }

    // ================================ 语音功能 ================================

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == SearchBox.VOICE_RECOGNITION_CODE && resultCode == RESULT_OK) {
            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            matches.set(0, matches.get(0).replaceAll("\\p{P}", ""));
            searchBox.populateEditText(matches);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    // ================================ 查询按钮被点击 ================================

    private void onSearchBtnClick(String title) {

        search(title);
        addTitleToQueue(title);
        refreshSearchBox();
    }

    private void addTitleToQueue(String title) {

        if (titleQueue.contains(title)) {
            titleQueue.remove(title);
        }
        titleQueue.add(title);
        if (titleQueue.size() == 6) {
            titleQueue.remove();
        }
    }

    // ================================ persistSearchHistory ================================

    @Override
    protected void onDestroy() {

        persistSearchHistory();
        super.onDestroy();
    }

    private void persistSearchHistory() {

        MyApplication.getSearchHistoryDao().deleteAll();
        List<SearchHistory> searchHistoryList = new ArrayList<>();
        for (String title : titleQueue) {
            SearchHistory searchHistory = new SearchHistory();
            searchHistory.setTitle(title);
            searchHistoryList.add(searchHistory);
        }
        MyApplication.getSearchHistoryDao().insertInTx(searchHistoryList);
    }


    // ================================ 底层方法 ================================

    private void refreshSearchBox() {

        ArrayList<SearchResult> searchResultList = new ArrayList<>();
        for (String title : titleQueue) {
            searchResultList.add(new SearchResult(title, getHistoryDrawable()));
        }
        Collections.reverse(searchResultList);
        searchBox.setSearchables(searchResultList);
    }

    private void search(String title) {

        newsFragment.getArguments().putString("title", title);
        newsFragment.reload();
    }

    private Drawable getHistoryDrawable() {

        return new IconDrawable(SearchActivity.this, Iconify.IconValue.zmdi_time)
                .colorRes(android.R.color.darker_gray)
                .actionBarSize();
    }


    // ================================ SearchBox.SearchListener ================================

    @Override
    public void onSearchOpened() {

    }

    @Override
    public void onSearchCleared() {

    }

    @Override
    public void onSearchClosed() {

    }

    @Override
    public void onSearchTermChanged() {

    }

    @Override
    public void onSearch(String title) {

        onSearchBtnClick(title);
    }

}
