package com.unicorn.csp.recycle.viewholder;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.unicorn.csp.R;
import com.unicorn.csp.activity.WebViewActivity;
import com.unicorn.csp.recycle.item.News;
import com.unicorn.csp.utils.DateUtils;

import java.text.SimpleDateFormat;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.alexrs.recyclerviewrenderers.viewholder.RenderViewHolder;


public class NewsViewHolder extends RenderViewHolder<News> {

    @Bind(R.id.tv_title)
    TextView tvTitle;

    @Bind(R.id.tv_time)
    TextView tvTime;

    @Bind(R.id.tv_comment_count)
    TextView tvCommentCount;

    News news;

    public NewsViewHolder(final View itemView) {

        super(itemView);
        ButterKnife.bind(this, itemView);

        tvTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(itemView.getContext(), WebViewActivity.class);
                intent.putExtra("title",tvTitle.getText().toString().trim());
                intent.putExtra("data", news.getData());
                itemView.getContext().startActivity(intent);

            }
        });
    }

    @Override
    public void onBindView(News news) {

        this.news = news;
        tvTitle.setText(news.getTitle());
        tvTime.setText(DateUtils.getFormatDateString(news.getTime(),new SimpleDateFormat("MM-dd HH:mm", Locale.CHINA)));
        tvCommentCount.setText("评论 " + news.getCommentCount()+"");
    }

}
