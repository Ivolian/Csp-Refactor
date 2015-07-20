package com.unicorn.csp.activity;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.malinskiy.materialicons.IconDrawable;
import com.malinskiy.materialicons.Iconify;
import com.quinny898.library.persistentsearch.SearchBox;
import com.quinny898.library.persistentsearch.SearchResult;
import com.unicorn.csp.MyApplication;
import com.unicorn.csp.R;
import com.unicorn.csp.activity.base.ButterKnifeActivity;
import com.unicorn.csp.adapter.recycle.NewsAdapter;
import com.unicorn.csp.greendao.SearchHistory;
import com.unicorn.csp.model.News;
import com.unicorn.csp.other.greenmatter.ColorOverrider;
import com.unicorn.csp.utils.ConfigUtils;
import com.unicorn.csp.utils.JSONUtils;
import com.unicorn.csp.utils.RecycleViewUtils;
import com.unicorn.csp.utils.ToastUtils;
import com.unicorn.csp.volley.MyVolley;
import com.unicorn.csp.volley.toolbox.VolleyErrorHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import butterknife.Bind;


public class SearchActivity extends ButterKnifeActivity {


    // ==================== views ====================

    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;


    // ==================== newsAdapter ====================

    NewsAdapter newsAdapter;


    // ==================== page data ====================

    final Integer PAGE_SIZE = 5;

    Integer pageNo;

    boolean loadingMore;

    boolean lastPage;


    // ==================== onCreateView ====================

private String title = "";



    private void initSwipeRefreshLayout() {

        swipeRefreshLayout.setColorSchemeColors(ColorOverrider.getInstance(this).getColorAccent());
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                reload();
            }
        });
    }

    private void initRecyclerView() {

        recyclerView.setHasFixedSize(true);
        final LinearLayoutManager linearLayoutManager = RecycleViewUtils.getLinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(newsAdapter = new NewsAdapter());
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

                if (lastPage || loadingMore) {
                    return;
                }
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    int lastVisibleItem = linearLayoutManager.findLastCompletelyVisibleItemPosition();
                    int totalItemCount = linearLayoutManager.getItemCount();
                    if (totalItemCount != 0 && totalItemCount == (lastVisibleItem + 1)) {
                        loadMore();
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            }
        });
    }

    private void reload() {


        clearPageData();
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
            }
        });
        MyVolley.addRequest(new JsonObjectRequest(getUrl(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        swipeRefreshLayout.setRefreshing(false);
                        newsAdapter.setNewsList(parseNewsList(response));
                        newsAdapter.notifyDataSetChanged();
                        checkLastPage(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        swipeRefreshLayout.setRefreshing(false);
                        ToastUtils.show(VolleyErrorHelper.getErrorMessage(volleyError));
                    }
                }));
    }

    private void loadMore() {

        pageNo++;
        loadingMore = true;

        MyVolley.addRequest(new JsonObjectRequest(getUrl(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        loadingMore = false;
                        newsAdapter.getNewsList().addAll(parseNewsList(response));
                        newsAdapter.notifyDataSetChanged();
                        checkLastPage(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        loadingMore = false;
                        ToastUtils.show(VolleyErrorHelper.getErrorMessage(volleyError));
                    }
                }));
    }


    // ========================== 基础方法 ==========================

    private void clearPageData() {

        pageNo = 1;
        lastPage = false;
    }

    private String getUrl() {

        Uri.Builder builder = Uri.parse(ConfigUtils.getBaseUrl()+"/api/v1/news?").buildUpon();
        builder.appendQueryParameter("pageNo", pageNo.toString());
        builder.appendQueryParameter("pageSize", PAGE_SIZE.toString());
        builder.appendQueryParameter("title", title);


//        Menu menu = (Menu)getArguments().getSerializable("menu");
//        builder.appendQueryParameter("regionId",menu.getId());

        return builder.toString();
    }


    private List<News> parseNewsList(JSONObject response) {

        JSONArray contents = JSONUtils.getJSONArray(response, "content", null);
        List<News> newsList = new ArrayList<>();
        for (int i = 0; i != contents.length(); i++) {
            JSONObject content = JSONUtils.getJSONObject(contents, i);
            String title = JSONUtils.getString(content, "title", "");
            JSONObject contentData = JSONUtils.getJSONObject(content, "contentData", null);
            String data = JSONUtils.getString(contentData, "data", "");
            String picture = JSONUtils.getString(content,"picture","");
            newsList.add(new News(title, new Date(), data, 11,picture));
        }

        return newsList;
    }

    private boolean isLastPage(JSONObject response) {

        return JSONUtils.getBoolean(response, "lastPage", false);
    }

    private void checkLastPage(JSONObject response) {

        if (lastPage = isLastPage(response)) {
            ToastUtils.show("已加载全部数据");
        }
    }

    @Bind(R.id.searchbox)
    SearchBox searchBox;

    Queue<String> queue = new LinkedList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        initViews();
    }

//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if ( requestCode == SearchBox.VOICE_RECOGNITION_CODE && resultCode == RESULT_OK) {
//            ArrayList<String> matches = data
//                    .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
//            searchBox.populateEditText(matches);
//        }
//        super.onActivityResult(requestCode, resultCode, data);
//    }


    @Override
    protected void onDestroy() {


        super.onDestroy();

        MyApplication.getSearchHistoryDao().deleteAll();
        List<String> titles = (List<String>)queue;
        for (int i=0;i!=titles.size();i++){
            String title = titles.get(i);
            SearchHistory searchHistory = new SearchHistory();
            searchHistory.setTitle(title);
            MyApplication.getSearchHistoryDao().insert(searchHistory);
        }

    }

    private void initViews(){

        initSwipeRefreshLayout();
        initRecyclerView();
        reload();




        searchBox.enableVoiceRecognition(this);
        searchBox.setLogoText("请输入查询内容");

        Drawable drawable= new IconDrawable(SearchActivity.this, Iconify.IconValue.zmdi_time)
                .colorRes(android.R.color.darker_gray)
                .actionBarSize();
        List<SearchHistory> searchHistoryList = MyApplication.getSearchHistoryDao().loadAll();
        for (SearchHistory searchHistory:searchHistoryList){
            searchBox.addSearchable(new SearchResult(searchHistory.getTitle(), drawable));
            queue.add(searchHistory.getTitle());
        }



        searchBox.setSearchListener(new SearchBox.SearchListener(){

            @Override
            public void onSearchOpened() {
                //Use this to tint the screen
            }

            @Override
            public void onSearchClosed() {
                //Use this to un-tint the screen
            }

            @Override
            public void onSearchTermChanged() {
                //React to the search term changing
                //Called after it has updated results
            }

            @Override
            public void onSearch(String searchTerm) {
                if (queue.contains(searchTerm)){
                    queue.remove(searchTerm);
                }
                queue.add(searchTerm);
                if (queue.size() == 6){
                    queue.remove();
                }

                searchBox.clearSearchable();
                Drawable drawable= new IconDrawable(SearchActivity.this, Iconify.IconValue.zmdi_time)
                    .colorRes(android.R.color.darker_gray)
                    .actionBarSize();


                List<String> titles = (List<String>)queue;


                for (int i=0;i!=titles.size();i++){
                    String title = titles.get(titles.size()-1-i);
                    searchBox.addSearchable(new SearchResult(title,drawable));
                }


                title = searchTerm;

                //





                reload();


            }

            @Override
            public void onSearchCleared() {
                //Called when the clear button is clicked

            }

        });
    }

}
