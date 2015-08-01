package com.unicorn.csp.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.unicorn.csp.R;
import com.unicorn.csp.adapter.recycle.BookAdapter;
import com.unicorn.csp.fragment.base.LazyLoadFragment;
import com.unicorn.csp.greendao.Menu;
import com.unicorn.csp.model.News;
import com.unicorn.csp.other.greenmatter.ColorOverrider;
import com.unicorn.csp.utils.ConfigUtils;
import com.unicorn.csp.utils.JSONUtils;
import com.unicorn.csp.utils.ToastUtils;
import com.unicorn.csp.volley.MyVolley;
import com.unicorn.csp.volley.toolbox.VolleyErrorHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;


public class BookFragment extends LazyLoadFragment {

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_news;
    }


    // ==================== views ====================

    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;


    // ==================== bookAdapter ====================

    BookAdapter bookAdapter;


    // ==================== page data ====================

    final Integer PAGE_SIZE = 5;

    Integer pageNo;

    boolean loadingMore;

    boolean lastPage;


    // ==================== onCreateView ====================

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onFirstUserVisible() {

        initViews();
    }

    private void initViews() {

        initSwipeRefreshLayout();
        initRecyclerView();
        reload();
    }

    private void initSwipeRefreshLayout() {

        swipeRefreshLayout.setColorSchemeColors(ColorOverrider.getInstance(getActivity()).getColorAccent());
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                reload();
            }
        });
    }

    private void initRecyclerView() {

        recyclerView.setHasFixedSize(true);
        final GridLayoutManager gridLayoutManager = getGridLayoutManager();
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(bookAdapter = new BookAdapter(getActivity()));
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

                if (lastPage || loadingMore) {
                    return;
                }
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    int lastVisibleItem = gridLayoutManager.findLastCompletelyVisibleItemPosition();
                    int totalItemCount = gridLayoutManager.getItemCount();
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

    public void reload() {

        clearPageData();
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
            }
        });
        MyVolley.addRequest(new JsonObjectRequest(getUrl(pageNo),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        stopRefreshing();
                        bookAdapter.setNewsList(getNewsList(response));
                        bookAdapter.notifyDataSetChanged();
                        checkLastPage(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        stopRefreshing();
                        ToastUtils.show(VolleyErrorHelper.getErrorMessage(volleyError));
                    }
                }));
    }

    private void loadMore() {

        loadingMore = true;
        MyVolley.addRequest(new JsonObjectRequest(getUrl(pageNo + 1),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        loadingMore = false;
                        pageNo++;
                        bookAdapter.getNewsList().addAll(getNewsList(response));
                        bookAdapter.notifyDataSetChanged();
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

    private String getUrl(int pageNo) {

        Uri.Builder builder = Uri.parse(ConfigUtils.getBaseUrl() + "/api/v1/news?").buildUpon();
        builder.appendQueryParameter("pageNo", pageNo + "");
        builder.appendQueryParameter("pageSize", PAGE_SIZE.toString());
        Menu menu = (Menu) getArguments().getSerializable("menu");
        builder.appendQueryParameter("regionId", menu == null ? "" : menu.getId());
        String title = getArguments().getString("title");
        builder.appendQueryParameter("title", title == null ? "" : title);
        return builder.toString();
    }

    private List<News> parseNewsList(JSONObject response) {

        JSONArray contents = JSONUtils.getJSONArray(response, "content", null);
        List<News> newsList = new ArrayList<>();
        for (int i = 0; i != contents.length(); i++) {
            JSONObject content = JSONUtils.getJSONObject(contents, i);
            String id = JSONUtils.getString(content, "id", "");
            String title = JSONUtils.getString(content, "title", "");
            JSONObject contentData = JSONUtils.getJSONObject(content, "contentData", null);
            String data = JSONUtils.getString(contentData, "data", "");
            String picture = JSONUtils.getString(content, "picture", "");
            int commentCount = JSONUtils.getInt(content, "commentCount", 0);
            newsList.add(new News(id, title, new Date(), commentCount, picture));
        }
        return newsList;
    }

    private List<News> getNewsList(JSONObject response) {

        List<News> newsList = new ArrayList<>();
        for (int i = 0; i != 10; i++) {
            News news = new News("", "", new Date(), 0, "");
            newsList.add(news);
        }
        return newsList;
    }


    // 检查是否所有数据加载完毕
    private void checkLastPage(JSONObject response) {

        if (lastPage = isLastPage(response)) {
            ToastUtils.show(noData(response) ? "暂无数据" : "已加载全部数据");
        }
    }

    private boolean isLastPage(JSONObject response) {

        return JSONUtils.getBoolean(response, "lastPage", false);
    }

    private boolean noData(JSONObject response) {

        return JSONUtils.getInt(response, "totalPages", 0) == 0;
    }

    private void stopRefreshing() {

        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    private GridLayoutManager getGridLayoutManager() {

        return new GridLayoutManager(getActivity(), 2);
    }

}
