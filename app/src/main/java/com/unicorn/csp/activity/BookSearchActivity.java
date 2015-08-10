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
import com.unicorn.csp.fragment.BookFragment;
import com.unicorn.csp.greendao.SearchHistory;
import com.unicorn.csp.greendao.SearchHistoryDao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import butterknife.Bind;


public class BookSearchActivity extends ButterKnifeActivity implements SearchBox.SearchListener {

    @Bind(R.id.searchbox)
    SearchBox searchBox;

    // 关键词队列，用于维护用户查询历史
    Queue<String> keywordQueue = new LinkedList<>();

    BookFragment bookFragment;


    // ================================ onCreate ================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initViews();
    }

    private void initViews() {

        initSearchBox();
        initNewsFragment();
    }

    private void initSearchBox() {

        searchBox.enableVoiceRecognition(this);
        searchBox.setSearchListener(this);
        searchBox.setLogoText("请输入查询内容");
        initTitleQueue();
        refreshSearchBox();
    }

    private void initTitleQueue() {

        for (SearchHistory searchHistory : getBookSearchHistory()) {
            keywordQueue.add(searchHistory.getKeyword());
        }
    }

    private void initNewsFragment() {

        bookFragment = new BookFragment();
        // 不知道为什么，这里 newsFragment 没有调用 setUserVisibleHint 方法，需要手动调用 initPrepare 方法
        bookFragment.initPrepare();
        Bundle bundle = new Bundle();
        bookFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.container, bookFragment).commit();
    }


    // ================================ 语音识别 ================================

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == SearchBox.VOICE_RECOGNITION_CODE && resultCode == RESULT_OK) {
            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            // 过滤标点符号，语音识别会自动添加标点符号，太智能了...
            matches.set(0, matches.get(0).replaceAll("\\p{P}", ""));
            searchBox.populateEditText(matches);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    // ================================ 查询按钮被点击 ================================

    private void onSearchBtnClick(String keyword) {

        reloadNews(keyword);
        addKeywordToQueue(keyword);
        refreshSearchBox();
    }

    private void addKeywordToQueue(String keyword) {

        if (keywordQueue.contains(keyword)) {
            keywordQueue.remove(keyword);
        }
        keywordQueue.add(keyword);
        if (keywordQueue.size() == 6) {
            keywordQueue.remove();
        }
    }


    // ================================ persistSearchHistory ================================

    @Override
    protected void onDestroy() {

        persistSearchHistory();
        super.onDestroy();
    }

    private void persistSearchHistory() {

        MyApplication.getSearchHistoryDao().deleteInTx(getBookSearchHistory());
        List<SearchHistory> searchHistoryList = new ArrayList<>();
        for (String keyword : keywordQueue) {
            searchHistoryList.add(new SearchHistory(keyword, "book"));
        }
        MyApplication.getSearchHistoryDao().insertInTx(searchHistoryList);
    }

    private List<SearchHistory> getBookSearchHistory() {

        return MyApplication.getSearchHistoryDao().queryBuilder().where(SearchHistoryDao.Properties.Type.eq("book")).list();
    }


    // ================================ 底层方法 ================================

    private void refreshSearchBox() {

        ArrayList<SearchResult> searchResultList = new ArrayList<>();
        for (String keyword : keywordQueue) {
            searchResultList.add(new SearchResult(keyword, getHistoryDrawable()));
        }
        Collections.reverse(searchResultList);
        searchBox.setSearchables(searchResultList);
    }

    private void reloadNews(String keyword) {

        bookFragment.getArguments().putString("keyword", keyword);
        bookFragment.reload();
    }

    private Drawable getHistoryDrawable() {

        return new IconDrawable(this, Iconify.IconValue.zmdi_time)
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
    public void onSearch(String keyword) {

        onSearchBtnClick(keyword);
    }

}
