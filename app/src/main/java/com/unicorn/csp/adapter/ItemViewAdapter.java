package com.unicorn.csp.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.unicorn.csp.R;
import com.unicorn.csp.activity.WebViewActivity;
import com.unicorn.csp.recycle.item.News;
import com.unicorn.csp.utils.DateUtils;
import com.unicorn.csp.volley.MyVolley;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * Created by Administrator on 2015/7/16.
 */
public class ItemViewAdapter extends RecyclerView.Adapter<ItemViewAdapter.ViewHolder> {

    private Activity activity;

    private List<News> NewsList;

    /**
     * 1. 持有 itemsView 中 views 的引用。
     * 2. 为 itemView 中 views 添加事件。
     */
    public class ViewHolder extends RecyclerView.ViewHolder {

//        @Bind(R.id.tv_title)
        TextView tvTitle;

//        @Bind(R.id.tv_time)
        TextView tvTime;

//        @Bind(R.id.tv_comment_count)
        TextView tvCommentCount;

//        @Bind(R.id.picture)
        NetworkImageView nivPicture;

        public ViewHolder(View v) {
            super(v);
//            ButterKnife.bind(this, v);


            tvTitle = (TextView)v.findViewById(R.id.tv_title);
            tvTime = (TextView)v.findViewById(R.id.tv_time);
            tvCommentCount = (TextView)v.findViewById(R.id.tv_comment_count);
            nivPicture = (NetworkImageView)v.findViewById(R.id.picture);


            // 2 添加事件
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    Intent intent = new Intent(itemView.getContext(), WebViewActivity.class);
                    intent.putExtra("title",tvTitle.getText().toString().trim());
                    intent.putExtra("data", NewsList.get(getAdapterPosition()).getData());
                    itemView.getContext().startActivity(intent);
                }
            });
        }
    }

    public ItemViewAdapter(Activity activity, List<News> NewsList) {

        this.activity = activity;
        this.NewsList = NewsList;
    }

    /**
     * 创建 itemView 时调用，每个 itemView 由一个 ViewHolder 持有引用。
     * 如果一屏幕能展示 13 itemView，那么调用该函数至多 17 次。
     * 如果一屏幕能展示 6 itemView，那么调用该函数至多 10 次。
     * 即上下各预留两个。
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_news, viewGroup, false);

        return new ViewHolder(v);
    }


    public String[] jpgs = {
            "http://pic1.nipic.com/2008-09-08/200898163242920_2.jpg",
            "http://pic.nipic.com/2007-11-09/2007119122519868_2.jpg",
            "http://pic25.nipic.com/20121209/9252150_194258033000_2.jpg",
            "http://pic2.ooopic.com/01/26/61/83bOOOPIC72.jpg",
            "http://pic1.nipic.com/2008-08-12/200881211331729_2.jpg",
            "http://pica.nipic.com/2007-11-09/2007119124413448_2.jpg",
            "http://pic1.nipic.com/2008-09-08/200898163242920_2.jpg",
            "http://pic.nipic.com/2007-11-09/2007119122519868_2.jpg",
            "http://pic25.nipic.com/20121209/9252150_194258033000_2.jpg",
            "http://pic2.ooopic.com/01/26/61/83bOOOPIC72.jpg",
            "http://pic1.nipic.com/2008-08-12/200881211331729_2.jpg"
    };
    /**
     * 设置(viewHolder创建时) 或替换(viewHolder复用时) itemView 中 views 的值。
     */
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {


        News news = NewsList.get(position);
        viewHolder.tvTitle.setText(news.getTitle());
        viewHolder.tvTime.setText(DateUtils.getFormatDateString(news.getTime(), new SimpleDateFormat("MM-dd HH:mm", Locale.CHINA)));
        viewHolder.tvCommentCount.setText("评论 " + news.getCommentCount() + "");
        viewHolder.nivPicture.setDefaultImageResId(R.drawable.news);
        viewHolder.nivPicture.setImageUrl(jpgs[position], MyVolley.getImageLoader());

    }

    @Override
    public int getItemCount() {
        return NewsList.size();
    }

    /**
     * ViewHolder 被复用。
     */
    @Override
    public void onViewRecycled(ViewHolder viewHolder) {

        super.onViewRecycled(viewHolder);
    }

    public List<News> getNewsList() {

        return NewsList;
    }

    public void setNewsList(List<News> NewsList) {

        this.NewsList = NewsList;
    }
}
