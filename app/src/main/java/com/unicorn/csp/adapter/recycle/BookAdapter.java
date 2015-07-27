package com.unicorn.csp.adapter.recycle;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.malinskiy.materialicons.widget.IconTextView;
import com.unicorn.csp.R;
import com.unicorn.csp.model.News;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


public class BookAdapter extends RecyclerView.Adapter<BookAdapter.ViewHolder> {

    // todo replace news to book
    private List<News> newsList = new ArrayList<>();


    public class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.more)
        IconTextView more;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_book, viewGroup, false));
    }

    @Override
    public int getItemCount() {

        return newsList.size();
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        News news = newsList.get(position);
    }

    public List<News> getNewsList() {

        return newsList;
    }

    public void setNewsList(List<News> newsList) {

        this.newsList = newsList;
    }

}
