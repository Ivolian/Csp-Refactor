package com.unicorn.csp.adapter.recycle;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.unicorn.csp.R;
import com.unicorn.csp.model.Comment;
import com.unicorn.csp.utils.DateUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;


public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    private List<Comment> commentList = new ArrayList<>();


    public class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.tv_username)
        TextView tvUsername;

        @Bind(R.id.tv_eventtime)
        TextView tvEventTime;

        @Bind(R.id.tv_words)
        TextView tvWords;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_comment, viewGroup, false));
    }

    @Override
    public int getItemCount() {

        return commentList.size();
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        Comment comment = commentList.get(position);
        viewHolder.tvUsername.setText(comment.getUsername());
        viewHolder.tvEventTime.setText(DateUtils.getFormatDateString(comment.getEventTime(), new SimpleDateFormat("MM-dd HH:mm", Locale.CHINA)));
        viewHolder.tvWords.setText(comment.getWords());
    }

    public List<Comment> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<Comment> commentList) {
        this.commentList = commentList;
    }

}
