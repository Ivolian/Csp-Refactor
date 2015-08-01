package com.unicorn.csp.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.unicorn.csp.R;
import com.unicorn.csp.fragment.base.ButterKnifeFragment;

import butterknife.Bind;

/**
 * Created by Administrator on 2015/8/1.
 */
public class QuestionFragment extends ButterKnifeFragment{

    @Bind(R.id.test)
    TextView textView;

    @Override
    public int getLayoutResId() {
        return R.layout.test;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        textView.setText("有问有答");
        return rootView;
    }

}
