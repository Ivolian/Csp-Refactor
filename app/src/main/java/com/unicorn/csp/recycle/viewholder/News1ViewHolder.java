package com.unicorn.csp.recycle.viewholder;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.unicorn.csp.R;
import com.unicorn.csp.activity.WebViewActivity;
import com.unicorn.csp.recycle.item.News1;
import com.unicorn.csp.utils.DateUtils;

import java.text.SimpleDateFormat;
import java.util.Locale;

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

    public News1ViewHolder(final View itemView) {

        super(itemView);
        ButterKnife.bind(this, itemView);
        tvTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(itemView.getContext(), WebViewActivity.class);
                intent.putExtra("title",tvTitle.getText().toString().trim());
                itemView.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public void onBindView(News1 news1) {

        tvTitle.setText(news1.getTitle());
        tvTime.setText(DateUtils.getFormatDateString(news1.getTime(),new SimpleDateFormat("MM-dd HH:mm", Locale.CHINA)));
        tvCommentCount.setText("评论 " + news1.getCommentCount()+"");
    }

}
