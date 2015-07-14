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

import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.unicorn.csp.R;
import com.unicorn.csp.fragment.base.ButterKnifeFragment;
import com.unicorn.csp.other.greenmatter.ColorOverrider;
import com.unicorn.csp.recycle.factory.Factory;
import com.unicorn.csp.recycle.item.News1;
import com.unicorn.csp.recycle.item.News2;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import me.alexrs.recyclerviewrenderers.adapter.RendererAdapter;
import me.alexrs.recyclerviewrenderers.builder.RendererBuilder;
import me.alexrs.recyclerviewrenderers.interfaces.Renderable;


public class NewsFragment extends ButterKnifeFragment {

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;

    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    @Bind(R.id.container)
    FrameLayout container;

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_news;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        initViews();
        return rootView;
    }

    private void initViews() {

        initRecyclerView();
        initSwipeRefreshLayout();
    }

    private void initRecyclerView() {

        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(getLinearLayoutManager());
        RendererAdapter adapter = new RendererAdapter(getItems(), new RendererBuilder(new Factory()));
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


    private LinearLayoutManager getLinearLayoutManager() {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        return linearLayoutManager;
    }

    private void showMsgAfterRefresh() {

        SnackbarManager.show(Snackbar.with(getActivity())
                .position(Snackbar.SnackbarPosition.TOP)
                .color(0xff4caf50)
                .margin(25, 25)
                .text("有10条资讯热点更新！"), container);
    }

    private List<Renderable> getItems() {

        List<Renderable> items = new ArrayList<>();
        for (int i = 0; i != 5; i++) {
            items.add(new News1("最高人民法院出台服务保障“一带一路”意见",new Date(),11));
            items.add(new News2("最高人民法院出台服务保障“一带一路”意见",new Date(),33));
        }
        return items;
    }

}
