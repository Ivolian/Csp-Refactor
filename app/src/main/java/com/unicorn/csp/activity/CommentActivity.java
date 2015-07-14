package com.unicorn.csp.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.melnykov.fab.FloatingActionButton;
import com.unicorn.csp.R;
import com.unicorn.csp.activity.base.ToolbarActivity;
import com.unicorn.csp.recycle.factory.Factory;
import com.unicorn.csp.recycle.item.Comment;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import me.alexrs.recyclerviewrenderers.adapter.RendererAdapter;
import me.alexrs.recyclerviewrenderers.builder.RendererBuilder;
import me.alexrs.recyclerviewrenderers.interfaces.Renderable;

public class CommentActivity extends ToolbarActivity {

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;

    @Bind(R.id.fab)
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        initToolbar("评论", true);
        initRecyclerView();
    }


    private void initRecyclerView() {

        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(getLinearLayoutManager());
        RendererAdapter adapter = new RendererAdapter(getItems(), new RendererBuilder(new Factory()));
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this).build());
        fab.attachToRecyclerView(recyclerView);
    }

    @OnClick(R.id.fab)
    public void onFabClick() {

    }

    private List<Renderable> getItems() {

        List<Renderable> items = new ArrayList<>();
        for (int i = 0; i != 10; i++) {
            items.add(new Comment("钱卫星",new Date(),"看了此文后大吃一惊，为压压惊，立刻决定：1)自现在起，停止收看电视，反正想看的盒子上都有"));
        }
        return items;
    }

    private LinearLayoutManager getLinearLayoutManager() {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        return linearLayoutManager;
    }

}
