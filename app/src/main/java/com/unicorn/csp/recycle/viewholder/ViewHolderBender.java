package com.unicorn.csp.recycle.viewholder;

import android.view.View;
import android.widget.TextView;

import com.unicorn.csp.R;
import com.unicorn.csp.recycle.items.ItemBender;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.alexrs.recyclerviewrenderers.viewholder.RenderViewHolder;


public class ViewHolderBender extends RenderViewHolder<ItemBender> {

    @Bind(R.id.tv_title)
    TextView tvTitle;

    public ViewHolderBender(View itemView) {

        super(itemView);
        ButterKnife.bind(this,itemView);
    }

    @Override
    public void onBindView(ItemBender itemBender) {

        tvTitle.setText(itemBender.getTitle());
    }
}
