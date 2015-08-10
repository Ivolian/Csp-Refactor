package com.unicorn.csp.activity.search;

import com.quinny898.library.persistentsearch.SearchBox;


// clear
public class NewsSearchActivity extends SearchActivity implements SearchBox.SearchListener {

    @Override
    String getType() {
        return "news";
    }

}
