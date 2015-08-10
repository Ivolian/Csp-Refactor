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
import com.unicorn.csp.activity.QuestionDetailActivity;
import com.unicorn.csp.model.Answer;
import com.unicorn.csp.model.Question;
import com.unicorn.csp.utils.DateUtils;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;


public class QuestionAdapter extends ExpandableRecyclerAdapter<QuestionAdapter.QuestionViewHolder, QuestionAdapter.AnswerViewHolder> {

    private LayoutInflater mInflater;

    private Activity activity;


    public QuestionAdapter(Context context, List<ParentObject> parentItemList) {
        super(context, parentItemList);
        mInflater = LayoutInflater.from(context);
    }


    /**
     * Public secondary constructor. This constructor adds the ability to add a custom triggering
     * view when the adapter is created without having to set it later. This is here for demo
     * purposes.
     *
     * @param context               for inflating views
     * @param parentItemList        the list of parent items to be displayed in the RecyclerView
     * @param customClickableViewId the id of the view that triggers the expansion
     */
    public QuestionAdapter(Context context, List<ParentObject> parentItemList,
                           int customClickableViewId) {
        super(context, parentItemList, customClickableViewId);
        mInflater = LayoutInflater.from(context);
    }

    /**
     * Public secondary constructor. This constructor adds the ability to add a custom triggering
     * view and a custom animation duration when the adapter is created without having to set them
     * later. This is here for demo purposes.
     *
     * @param context               for inflating views
     * @param parentItemList        the list of parent items to be displayed in the RecyclerView
     * @param customClickableViewId the id of the view that triggers the expansion
     * @param animationDuration     the duration (in ms) of the rotation animation
     */
    public QuestionAdapter(Context context, List<ParentObject> parentItemList,
                           int customClickableViewId, long animationDuration) {
        super(context, parentItemList, customClickableViewId, animationDuration);
        mInflater = LayoutInflater.from(context);
        this.activity = (Activity) context;
    }

    @Override
    public QuestionViewHolder onCreateParentViewHolder(ViewGroup parent) {
        View view = mInflater.inflate(R.layout.item_quesion, parent, false);
        return new QuestionViewHolder(view);
    }

    @Override
    public AnswerViewHolder onCreateChildViewHolder(ViewGroup parent) {
        View view = mInflater.inflate(R.layout.item_answer, parent, false);
        return new AnswerViewHolder(view);
    }

    @Override
    public void onBindParentViewHolder(QuestionViewHolder parentViewHolder, int position, Object parentObject) {
        Question question = (Question) parentObject;
        parentViewHolder.tvContent.setText(question.getContent());
        if (question.getChildObjectList().size() == 0) {
            parentViewHolder.itvExpand.setVisibility(View.GONE);
            parentViewHolder.line.setVisibility(View.GONE);
        }
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

        public TextView tvContent;

        public IconTextView itvExpand;

        public View line;

        public TextView tvTime;

        public TextView tvUsername;

        /**
         * Public constructor for the CustomViewHolder.
         *
         * @param itemView the view of the parent item. Find/modify views using this.
         */
        public QuestionViewHolder(View itemView) {
            super(itemView);

            tvContent = (TextView) itemView.findViewById(R.id.tv_content);
            itvExpand = (IconTextView) itemView.findViewById(R.id.itv_expand);
            line = itemView.findViewById(R.id.line);
            tvTime = (TextView) itemView.findViewById(R.id.tv_time);
            tvUsername = (TextView) itemView.findViewById(R.id.tv_username);
            tvContent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(activity, QuestionDetailActivity.class);
                    Question question = (Question) mParentItemList.get(getAdapterPosition());
                    intent.putExtra("question", question);
                    activity.startActivity(intent);

                }
            });
        }
    }


    public class AnswerViewHolder extends ChildViewHolder {

        public TextView tvContent;

        public TextView tvTime;

        public TextView tvUsername;

        public AnswerViewHolder(View itemView) {
            super(itemView);

            tvContent = (TextView) itemView.findViewById(R.id.tv_content);
            tvTime = (TextView) itemView.findViewById(R.id.tv_time);
            tvUsername = (TextView) itemView.findViewById(R.id.tv_username);
        }

    }

}