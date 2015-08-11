package com.unicorn.csp.adapter.recyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.Adapter.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.Model.ParentObject;
import com.bignerdranch.expandablerecyclerview.ViewHolder.ChildViewHolder;
import com.bignerdranch.expandablerecyclerview.ViewHolder.ParentViewHolder;
import com.malinskiy.materialicons.widget.IconTextView;
import com.unicorn.csp.R;
import com.unicorn.csp.activity.question.QuestionDetailActivity;
import com.unicorn.csp.model.Answer;
import com.unicorn.csp.model.Question;
import com.unicorn.csp.utils.DateUtils;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


// clear
public class QuestionAdapter extends ExpandableRecyclerAdapter<QuestionAdapter.QuestionViewHolder, QuestionAdapter.AnswerViewHolder> {

    private LayoutInflater mInflater;

    private Activity activity;

    public QuestionAdapter(Context context, List<ParentObject> parentItemList, int customClickableViewId, long animationDuration) {

        super(context, parentItemList, customClickableViewId, animationDuration);
        mInflater = LayoutInflater.from(context);
        activity = (Activity) context;
    }

    @Override
    public QuestionViewHolder onCreateParentViewHolder(ViewGroup parent) {

        View view = mInflater.inflate(R.layout.item_quesion, parent, false);
        return new QuestionViewHolder(view);
    }

    @Override
    public AnswerViewHolder onCreateChildViewHolder(ViewGroup parent) {

        View view = mInflater.inflate(R.layout.item_question_answer, parent, false);
        return new AnswerViewHolder(view);
    }

    @Override
    public void onBindParentViewHolder(QuestionViewHolder parentViewHolder, int position, Object parentObject) {

        Question question = (Question) parentObject;
        parentViewHolder.itvExpand.setVisibility(question.getChildObjectList().size() == 0 ? View.GONE : View.VISIBLE);
        parentViewHolder.line.setVisibility(question.getChildObjectList().size() == 0 ? View.GONE : View.VISIBLE);
        parentViewHolder.tvContent.setText(question.getContent());
        parentViewHolder.tvTime.setText(DateUtils.getFormatDateString(question.getEventTime(), new SimpleDateFormat("MM-dd HH:mm", Locale.CHINA)));
        parentViewHolder.tvUsername.setText(question.getName());
    }

    @Override
    public void onBindChildViewHolder(AnswerViewHolder childViewHolder, int position, Object childObject) {

        Answer answer = (Answer) childObject;
        childViewHolder.tvContent.setText(answer.getContent());
        childViewHolder.tvTime.setText(DateUtils.getFormatDateString(answer.getEventTime(), new SimpleDateFormat("MM-dd HH:mm", Locale.CHINA)));
        childViewHolder.tvUsername.setText(answer.getName());
    }

    public class QuestionViewHolder extends ParentViewHolder {

        @Bind(R.id.itv_expand)
        IconTextView itvExpand;

        @Bind(R.id.line)
        View line;

        @Bind(R.id.tv_content)
        TextView tvContent;

        @Bind(R.id.tv_time)
        TextView tvTime;

        @Bind(R.id.tv_username)
        TextView tvUsername;

        public QuestionViewHolder(View itemView) {

            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.llClickRegion)
        public void startQuestionDetailActivity() {

            Intent intent = new Intent(activity, QuestionDetailActivity.class);
            Question question = (Question) mParentItemList.get(getAdapterPosition());
            intent.putExtra("question", question);
            activity.startActivity(intent);
        }
    }

    public class AnswerViewHolder extends ChildViewHolder {

        @Bind(R.id.tv_content)
        TextView tvContent;

        @Bind(R.id.tv_time)
        TextView tvTime;

        @Bind(R.id.tv_username)
        TextView tvUsername;

        public AnswerViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}