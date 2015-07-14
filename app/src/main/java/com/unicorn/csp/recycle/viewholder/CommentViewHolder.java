package com.unicorn.csp.recycle.viewholder;

import android.view.View;
import android.widget.TextView;

import com.unicorn.csp.R;
import com.unicorn.csp.recycle.item.Comment;
import com.unicorn.csp.utils.DateUtils;

import java.text.SimpleDateFormat;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.alexrs.recyclerviewrenderers.viewholder.RenderViewHolder;


public class CommentViewHolder extends RenderViewHolder<Comment> {

    @Bind(R.id.tv_account)
    TextView tvAccount;

    @Bind(R.id.tv_time)
    TextView tvTime;

    @Bind(R.id.tv_content)
    TextView tvContent;

    public CommentViewHolder(View itemView) {

        super(itemView);
        ButterKnife.bind(this,itemView);
    }

    @Override
    public void onBindView(Comment comment) {

        tvAccount.setText(comment.getAccount());
        tvTime.setText(DateUtils.getFormatDateString(comment.getTime(),new SimpleDateFormat("MM-dd HH:mm", Locale.CHINA)));
        tvContent.setText(comment.getContent());
    }

}
