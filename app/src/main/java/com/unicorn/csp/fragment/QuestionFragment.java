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


public class QuestionFragment extends ButterKnifeFragment {

    // todo 5dp
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


    //

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;

    QuestionAdapter questionAdapter;


    private void initViews() {

        questionAdapter = new QuestionAdapter(getActivity(), getQuestionList(), R.id.itv_expand, 500);
        recyclerView.setAdapter(questionAdapter);
        recyclerView.setLayoutManager(RecycleViewUtils.getLinearLayoutManager(getActivity()));
//        questionAdapter.setParentAndIconExpandOnClick(true);

    }


    private List<ParentObject> getQuestionList() {

        Question question = new Question();
        question.setContent("能不能建一个我的收藏，方便大家把自己需啊哟的法律法规收藏在自己的文件夹里，便于查阅。");

        List<Object> answerList = new ArrayList<>();
        answerList.add(new Answer("说的不错"));
        answerList.add(new Answer("可也。"));
        question.setChildObjectList(answerList);

        Question question2 = new Question();
        question2.setContent("我问个啥问题吧我个啥问题吧我问个啥问题吧");


        List<Object> answerList2 = new ArrayList<>();
        answerList2.add(new Answer("hehe3"));
        answerList2.add(new Answer("hehe4"));
        question2.setChildObjectList(answerList2);


        Question question3 = new Question();
        question3.setContent("我问个啥问题吧我");


        List<ParentObject> questionList = new ArrayList<>();
        questionList.add(question);
        questionList.add(question2);
        questionList.add(question3);
        return questionList;
    }

}
