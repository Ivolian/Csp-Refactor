package com.unicorn.csp.fragment;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bignerdranch.expandablerecyclerview.Model.ParentObject;
import com.unicorn.csp.R;
import com.unicorn.csp.adapter.recycle.QuestionAdapter;
import com.unicorn.csp.fragment.base.ButterKnifeFragment;
import com.unicorn.csp.model.Answer;
import com.unicorn.csp.model.Question;
import com.unicorn.csp.utils.RecycleViewUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by Administrator on 2015/8/1.
 */
public class QuestionFragment extends ButterKnifeFragment{

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;

    QuestionAdapter questionAdapter;

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_refresh_recycleview;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        initViews();
        return rootView;
    }

    private void initViews(){

        questionAdapter = new QuestionAdapter(getActivity(),getQuestionList(),R.id.recycler_item_arrow_parent,500);
        recyclerView.setAdapter(questionAdapter);
        recyclerView.setLayoutManager(RecycleViewUtils.getLinearLayoutManager(getActivity()));
        questionAdapter.setParentAndIconExpandOnClick(true);
    }


    private List<ParentObject> getQuestionList(){

        Question question = new Question();
        question.setContent("你是SB吗？");

        Answer answer = new Answer();
        answer.setContent("不是");

        List<Object> answerList = new ArrayList<>();
        answerList.add(answer);
        answerList.add(answer);
        answerList.add(answer);
        answerList.add(answer);

        question.setChildObjectList(answerList);

        List<ParentObject> questionList = new ArrayList<>();
        questionList.add(question);
        return  questionList;
    }
}
