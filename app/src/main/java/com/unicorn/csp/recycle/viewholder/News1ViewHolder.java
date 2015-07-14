package com.unicorn.csp.recycle.viewholder;

import android.view.View;
import android.widget.TextView;

import com.unicorn.csp.R;
import com.unicorn.csp.recycle.item.News1;
import com.unicorn.csp.utils.DateUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.alexrs.recyclerviewrenderers.viewholder.RenderViewHolder;


public class News1ViewHolder extends RenderViewHolder<News1> {

    @Bind(R.id.tv_title)
    TextView tvTitle;

    @Bind(R.id.tv_time)
    TextView tvTime;

    @Bind(R.id.tv_comment_count)
    TextView tvCommentCount;

    public News1ViewHolder(View itemView) {

        super(itemView);
        ButterKnife.bind(this,itemView);
    }

    @Override
    public void onBindView(News1 news1) {

        tvTitle.setText(news1.getTitle());
        tvTime.setText(DateUtils.getFormatDateString(news1.getTime()));
        tvCommentCount.setText("评论 " + news1.getCommentCount()+"");
    }

}
