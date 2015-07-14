package com.unicorn.csp.recycle.viewholder;

import android.view.View;
import android.widget.TextView;

import com.unicorn.csp.R;
import com.unicorn.csp.recycle.item.News2;
import com.unicorn.csp.utils.DateUtils;

import java.text.SimpleDateFormat;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.alexrs.recyclerviewrenderers.viewholder.RenderViewHolder;


public class News2ViewHolder extends RenderViewHolder<News2> {

    @Bind(R.id.tv_title)
    TextView tvTitle;

    @Bind(R.id.tv_time)
    TextView tvTime;

    @Bind(R.id.tv_comment_count)
    TextView tvCommentCount;

    public News2ViewHolder(View itemView) {

        super(itemView);
        ButterKnife.bind(this,itemView);
    }

    @Override
    public void onBindView(News2 news2) {

        tvTitle.setText(news2.getTitle());
        tvTime.setText(DateUtils.getFormatDateString(news2.getTime(),new SimpleDateFormat("MM-dd HH:mm", Locale.CHINA)));
        tvCommentCount.setText("评论 " + news2.getCommentCount()+"");
    }

}
