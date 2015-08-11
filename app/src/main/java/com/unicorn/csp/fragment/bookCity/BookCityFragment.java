package com.unicorn.csp.fragment.bookCity;

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

    @Bind(R.id.convenientBanner)
    ConvenientBanner<Integer> convenientBanner;

    private List<Integer> images = Arrays.asList(
            R.drawable.zhiwang1,
            R.drawable.zhiwang2,
            R.drawable.zhiwang3,
            R.drawable.zhiwang4,
            R.drawable.zhiwang5
    );

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        initViews();
        return rootView;
    }

    private void initViews() {

        convenientBanner.setPages(
                new CBViewHolderCreator<ImageHolderView>() {
                    @Override
                    public ImageHolderView createHolder() {
                        return new ImageHolderView(getActivity());
                    }
                }, images)
                .setPageIndicator(new int[]{R.drawable.ic_page_indicator, R.drawable.ic_page_indicator_focused})
                .setPageTransformer(ConvenientBanner.Transformer.DefaultTransformer);
        convenientBanner.startTurning(5000);
    }

}
