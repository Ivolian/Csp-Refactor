package com.unicorn.csp.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.unicorn.csp.R;
import com.unicorn.csp.adapter.ItemViewAdapter;
import com.unicorn.csp.fragment.base.ButterKnifeFragment;
import com.unicorn.csp.other.greenmatter.ColorOverrider;
import com.unicorn.csp.recycle.item.News;
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


public class NewsFragment extends ButterKnifeFragment {

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_news;
    }


    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;


    ItemViewAdapter itemViewAdapter;
    // ==================== pager data ====================

    final Integer pagerSize = 5;

    Integer currentPager;

    Boolean noData;

    Boolean allDataLoaded;

    boolean loadingMore;


    private void initPagerData() {

        currentPager = 1;
        noData = false;
        allDataLoaded = false;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        initViews();
        return rootView;
    }

    private void reload() {


        initPagerData();



//        String url = "http://192.168.1.101:3002/withub/api/v1/news?pageNo=1&pageSize=10";
        Uri.Builder builder = Uri.parse("http://192.168.1.101:3002/withub/api/v1/news?").buildUpon();
        builder.appendQueryParameter("pageNo", currentPager.toString());
        builder.appendQueryParameter("pageSize", "5");


        MyVolley.getRequestQueue().add(new JsonObjectRequest(builder.toString(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        swipeRefreshLayout.setRefreshing(false);
                        JSONArray contents = JSONUtils.getJSONArray(response, "content", null);
                        List<News> newsList = new ArrayList<>();
                        for (int i = 0; i != contents.length(); i++) {
                            JSONObject content = JSONUtils.getJSONObject(contents, i);
                            String title = JSONUtils.getString(content, "title", "");
                            JSONObject contentData = JSONUtils.getJSONObject(content, "contentData", null);
                            String data = JSONUtils.getString(contentData, "data", "");

                            // todo img
                            Date time = new Date();
                            int commentCount = 11;

                            newsList.add(new News(title, time, data, commentCount));
                        }
                        itemViewAdapter.setNewsList(newsList);

                        itemViewAdapter.notifyDataSetChanged();
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

    private void initViews() {


        initRecyclerView();
        initSwipeRefreshLayout();
        initPagerData();
        reload();
    }

    private void loadMore() {

       loadingMore = true;
        Uri.Builder builder = Uri.parse("http://192.168.1.101:3002/withub/api/v1/news?").buildUpon();
        builder.appendQueryParameter("pageNo", (currentPager+1)+"");
        builder.appendQueryParameter("pageSize", "5");

        MyVolley.getRequestQueue().add(new JsonObjectRequest(builder.toString(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        loadingMore = false;


                        JSONArray contents = JSONUtils.getJSONArray(response, "content", null);
                        List<News> newsList = new ArrayList<>();
                        for (int i = 0; i != contents.length(); i++) {
                            JSONObject content = JSONUtils.getJSONObject(contents, i);
                            String title = JSONUtils.getString(content, "title", "");
                            JSONObject contentData = JSONUtils.getJSONObject(content, "contentData", null);
                            String data = JSONUtils.getString(contentData, "data", "");

                            // todo img
                            Date time = new Date();
                            int commentCount = 11;

                            newsList.add(new News(title, time, data, commentCount));
                        }

                        itemViewAdapter.getNewsList().addAll(newsList);
                        itemViewAdapter.notifyDataSetChanged();
                        currentPager++;

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


    private void initRecyclerView() {

        recyclerView.setHasFixedSize(true);
        final LinearLayoutManager linearLayoutManager = getLinearLayoutManager();
        recyclerView.setLayoutManager(linearLayoutManager);
    itemViewAdapter = new ItemViewAdapter(getActivity(),new ArrayList<News>());
        recyclerView.setAdapter(itemViewAdapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

                if (noData || allDataLoaded || loadingMore) {
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

    private void initSwipeRefreshLayout() {

        swipeRefreshLayout.setColorSchemeColors(ColorOverrider.getInstance(getActivity()).getColorAccent());
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                reload();
            }
        });
    }

    private void onRefresh() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(false);
            }
        }, 2000);
    }







    // ========================== 基础方法 ==========================

    private LinearLayoutManager getLinearLayoutManager() {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        return linearLayoutManager;
    }

    // ========================== 下拉刷新提示信息 ==========================

//    @Bind(R.id.container)
//    FrameLayout container;
//
//    private void showMsgAfterRefresh() {
//
//        SnackbarManager.show(Snackbar.with(getActivity())
//                .position(Snackbar.SnackbarPosition.TOP)
//                .color(0xff4caf50)
//                .margin(25, 25)
//                .text("有10条资讯热点更新！"), container);
//    }

}
