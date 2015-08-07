package com.unicorn.csp.adapter.recycle;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.Adapter.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.Model.ParentObject;
import com.bignerdranch.expandablerecyclerview.ViewHolder.ChildViewHolder;
import com.bignerdranch.expandablerecyclerview.ViewHolder.ParentViewHolder;
import com.unicorn.csp.R;
import com.unicorn.csp.model.Answer;
import com.unicorn.csp.model.Question;

import java.util.List;


public class QuestionAdapter extends ExpandableRecyclerAdapter<QuestionAdapter.QuestionViewHolder, QuestionAdapter.AnswerViewHolder> {

    private LayoutInflater mInflater;



    public QuestionAdapter(Context context, List<ParentObject> parentItemList) {
        super(context, parentItemList);
        mInflater = LayoutInflater.from(context);
    }


    /**
     * Public secondary constructor. This constructor adds the ability to add a custom triggering
     * view when the adapter is created without having to set it later. This is here for demo
     * purposes.
     *
     * @param context for inflating views
     * @param parentItemList the list of parent items to be displayed in the RecyclerView
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
     * @param context for inflating views
     * @param parentItemList the list of parent items to be displayed in the RecyclerView
     * @param customClickableViewId the id of the view that triggers the expansion
     * @param animationDuration the duration (in ms) of the rotation animation
     */
    public QuestionAdapter(Context context, List<ParentObject> parentItemList,
                           int customClickableViewId, long animationDuration) {
        super(context, parentItemList, customClickableViewId, animationDuration);
        mInflater = LayoutInflater.from(context);
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
    }

    @Override
    public void onBindChildViewHolder(AnswerViewHolder childViewHolder, int position, Object childObject) {
        Answer answer = (Answer) childObject;
        childViewHolder.tvContent.setText(answer.getContent());
    }


    public class QuestionViewHolder extends ParentViewHolder {

        public TextView tvContent;

        /**
         * Public constructor for the CustomViewHolder.
         *
         * @param itemView the view of the parent item. Find/modify views using this.
         */
        public QuestionViewHolder(View itemView) {
            super(itemView);

            tvContent = (TextView) itemView.findViewById(R.id.tv_content);
        }
    }


    public class AnswerViewHolder extends ChildViewHolder {

        public TextView tvContent;

        public AnswerViewHolder(View itemView) {
            super(itemView);

            tvContent = (TextView) itemView.findViewById(R.id.tv_content);
        }

    }

}