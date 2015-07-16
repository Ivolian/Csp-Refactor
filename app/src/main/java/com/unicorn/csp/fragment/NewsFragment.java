package com.unicorn.csp.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.unicorn.csp.R;
import com.unicorn.csp.fragment.base.ButterKnifeFragment;
import com.unicorn.csp.other.greenmatter.ColorOverrider;
import com.unicorn.csp.recycle.factory.Factory;
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
import me.alexrs.recyclerviewrenderers.adapter.RendererAdapter;
import me.alexrs.recyclerviewrenderers.builder.RendererBuilder;
import me.alexrs.recyclerviewrenderers.interfaces.Renderable;


public class NewsFragment extends ButterKnifeFragment {

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_news;
    }

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    RendererAdapter adapter;

    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        initViews();
        return rootView;
    }

    private void askForData() {

        String url = "http://192.168.1.101:3002/withub/api/v1/news?pageNo=1&pageSize=10";
        MyVolley.getRequestQueue().add(new JsonObjectRequest(url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONArray contents = JSONUtils.getJSONArray(response, "content", null);
                      List<Renderable> newsList = new ArrayList<>();
                        for (int i=0;i!=contents.length();i++){
                          JSONObject content = JSONUtils.getJSONObject(contents,i);
                          String title = JSONUtils.getString(content, "title", "");
                            JSONObject contentData = JSONUtils.getJSONObject(content, "contentData", null);
                            String data = JSONUtils.getString(contentData,"data","");

                                    // todo img
                          Date time = new Date();
                            int commentCount = 11;

                            newsList.add(new News(title,time,data,commentCount));
                      }
        adapter.update(newsList);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        ToastUtils.show(VolleyErrorHelper.getErrorMessage(volleyError));
                    }
                }));
    }

    private void initViews() {

        initRecyclerView();
        initSwipeRefreshLayout();
        askForData();
    }

    private void initRecyclerView() {

        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(getLinearLayoutManager());
         adapter = new RendererAdapter(new ArrayList<Renderable>(), new RendererBuilder(new Factory()));
        recyclerView.setAdapter(adapter);
    }

    private void initSwipeRefreshLayout() {

        swipeRefreshLayout.setColorSchemeColors(ColorOverrider.getInstance(getActivity()).getColorAccent());
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                NewsFragment.this.onRefresh();
            }
        });
    }

    private void onRefresh() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                swipeRefreshLayout.setRefreshing(false);
                showMsgAfterRefresh();
            }
        }, 2000);
    }

    private List<Renderable> getItems() {

        List<Renderable> items = new ArrayList<>();
        for (int i = 0; i != 10; i++) {
            items.add(new News("最高人民法院出台服务保障“一带一路”意见", new Date(), "asdfasf", 11));
        }
        return items;
    }


    // ========================== 下拉刷新提示信息 ==========================

    @Bind(R.id.container)
    FrameLayout container;

    private void showMsgAfterRefresh() {

        SnackbarManager.show(Snackbar.with(getActivity())
                .position(Snackbar.SnackbarPosition.TOP)
                .color(0xff4caf50)
                .margin(25, 25)
                .text("有10条资讯热点更新！"), container);
    }


    // ========================== 基础方法 ==========================

    private LinearLayoutManager getLinearLayoutManager() {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        return linearLayoutManager;
    }


}
