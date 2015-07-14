package com.unicorn.csp.recycle.viewholder;

import android.view.View;
import android.widget.TextView;

import com.unicorn.csp.R;
import com.unicorn.csp.recycle.item.News1Item;
import com.unicorn.csp.utils.DateUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.alexrs.recyclerviewrenderers.viewholder.RenderViewHolder;


public class News1ViewHolder extends RenderViewHolder<News1Item> {

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
    public void onBindView(News1Item news1Item) {

        tvTitle.setText(news1Item.getTitle());
        tvTime.setText(DateUtils.getFormatDateString(news1Item.getTime()));
        tvCommentCount.setText("评论 " +news1Item.getCommentCount()+"");
    }

}
