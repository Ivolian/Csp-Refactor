package com.unicorn.csp.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.malinskiy.materialicons.IconDrawable;
import com.malinskiy.materialicons.Iconify;
import com.quinny898.library.persistentsearch.SearchBox;
import com.quinny898.library.persistentsearch.SearchResult;
import com.unicorn.csp.R;
import com.unicorn.csp.activity.base.ButterKnifeActivity;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import butterknife.Bind;


public class SearchActivity extends ButterKnifeActivity {

    @Bind(R.id.searchbox)
    SearchBox searchBox;

    Queue<String> queue = new LinkedList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initViews();
    }

    private void initViews(){
        searchBox.setLogoText("请输入查询内容");



//        for(int x = 0; x < 20; x++){
//            SearchResult option = new SearchResult("Result " + Integer.toString(x),  new IconDrawable(this, Iconify.IconValue.zmdi_time)
//                    .colorRes(android.R.color.darker_gray)
//                    .actionBarSize());
//            searchBox.addSearchable(option);
//        }

        searchBox.setSearchListener(new SearchBox.SearchListener(){

            @Override
            public void onSearchOpened() {
                //Use this to tint the screen
            }

            @Override
            public void onSearchClosed() {
                //Use this to un-tint the screen
            }

            @Override
            public void onSearchTermChanged() {
                //React to the search term changing
                //Called after it has updated results
            }

            @Override
            public void onSearch(String searchTerm) {


                queue.add(searchTerm);
                if (queue.size() == 6){
                    queue.remove();
                }

//                searchBox.clearResults();
                searchBox.clearSearchable();

                Drawable drawable= new IconDrawable(SearchActivity.this, Iconify.IconValue.zmdi_time)
                    .colorRes(android.R.color.darker_gray)
                    .actionBarSize();


                List<String> titles = (List<String>)queue;
                for (int i=0;i!=titles.size();i++){
                    String title = titles.get(titles.size()-1-i);
                    searchBox.addSearchable(new SearchResult(title,drawable));
                }
            }

            @Override
            public void onSearchCleared() {
                //Called when the clear button is clicked

            }

        });
    }

}
