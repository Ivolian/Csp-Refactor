package com.unicorn.csp.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bigkoo.convenientbanner.CBViewHolderCreator;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.unicorn.csp.R;
import com.unicorn.csp.fragment.base.ButterKnifeFragment;

import java.util.Arrays;
import java.util.List;

import butterknife.Bind;


public class BookCityFragment extends ButterKnifeFragment {

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_bookcity;
    }


    private List<Integer> localImages = Arrays.asList(
            R.drawable.bookcity1,
            R.drawable.bookcity2,
            R.drawable.bookcity3,
            R.drawable.bookcity4,
            R.drawable.bookcity5
    );


    @Bind(R.id.convenientBanner)
    ConvenientBanner convenientBanner;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        initViews();
        return rootView;
    }

    private void initViews() {

        convenientBanner.setPages(
                new CBViewHolderCreator<LocalImageHolderView>() {
                    @Override
                    public LocalImageHolderView createHolder() {
                        return new LocalImageHolderView(getActivity());
                    }
                }, localImages)
                //设置两个点图片作为翻页指示器，不设置则没有指示器，可以根据自己需求自行配合自己的指示器,不需要圆点指示器可用不设
                .setPageIndicator(new int[]{R.drawable.ic_page_indicator, R.drawable.ic_page_indicator_focused})
                        //设置翻页的效果，不需要翻页效果可用不设
                .setPageTransformer(ConvenientBanner.Transformer.AccordionTransformer);
        convenientBanner.startTurning(3000);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            convenientBanner.startTurning(3000);
        } else {
            convenientBanner.stopTurning();
        }
    }

}
