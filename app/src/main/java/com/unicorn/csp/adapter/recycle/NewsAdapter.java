package com.unicorn.csp.adapter.recycle;

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
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;


public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

    // todo delete
    public String[] JPGS = {
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


    private List<News> newsList = new ArrayList<>();


    public class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.tv_title)
        TextView tvTitle;

        @Bind(R.id.tv_time)
        TextView tvTime;

        @Bind(R.id.tv_comment_count)
        TextView tvCommentCount;

        @Bind(R.id.niv_picture)
        NetworkImageView nivPicture;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    Intent intent = new Intent(itemView.getContext(), WebViewActivity.class);
                    intent.putExtra("title", tvTitle.getText().toString().trim());
                    intent.putExtra("data", newsList.get(getAdapterPosition()).getData());
                    itemView.getContext().startActivity(intent);
                }
            });
        }
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_news, viewGroup, false));
    }

    @Override
    public int getItemCount() {

        return newsList.size();
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        News news = newsList.get(position);
        viewHolder.tvTitle.setText(news.getTitle());

        // todo tvTime tvCommentCount nivPicture
        viewHolder.tvTime.setText(DateUtils.getFormatDateString(news.getTime(), new SimpleDateFormat("MM-dd HH:mm", Locale.CHINA)));
        viewHolder.tvCommentCount.setText("评论 " + news.getCommentCount() + "");
        viewHolder.nivPicture.setDefaultImageResId(R.drawable.news);
        viewHolder.nivPicture.setImageUrl(JPGS[position % 10], MyVolley.getImageLoader());
    }

    public List<News> getNewsList() {

        return newsList;
    }

    public void setNewsList(List<News> newsList) {

        this.newsList = newsList;
    }

}