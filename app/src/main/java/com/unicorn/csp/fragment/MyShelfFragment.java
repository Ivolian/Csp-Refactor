package com.unicorn.csp.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.nispok.snackbar.listeners.ActionClickListener;
import com.unicorn.csp.R;
import com.unicorn.csp.adapter.recyclerView.MyShelfAdapter;
import com.unicorn.csp.fragment.base.LazyLoadFragment;
import com.unicorn.csp.model.Book;
import com.unicorn.csp.other.SwipeableRecyclerViewTouchListener;
import com.unicorn.csp.other.greenmatter.ColorOverrider;
import com.unicorn.csp.utils.ConfigUtils;
import com.unicorn.csp.utils.JSONUtils;
import com.unicorn.csp.utils.RecycleViewUtils;
import com.unicorn.csp.utils.ToastUtils;
import com.unicorn.csp.volley.MyVolley;
import com.unicorn.csp.volley.toolbox.VolleyErrorHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;


public class MyShelfFragment extends LazyLoadFragment {

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_my_shelf;
    }


    // ==================== views ====================

    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;


    // ==================== myShelfAdapter ====================

    MyShelfAdapter myShelfAdapter;


    // ==================== page data ====================

    final Integer PAGE_SIZE = 10;

    Integer pageNo;

    boolean loadingMore;

    boolean lastPage;


    // ==================== onCreateView ====================

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onFirstUserVisible() {

        initViews();
    }

    private void initViews() {

        initSwipeRefreshLayout();
        initRecyclerView();
        reload();
    }

    private void initSwipeRefreshLayout() {

        swipeRefreshLayout.setColorSchemeColors(ColorOverrider.getInstance(getActivity()).getColorAccent());
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                reload();
            }
        });
    }

    private void initRecyclerView() {

        recyclerView.setHasFixedSize(true);
        final LinearLayoutManager linearLayoutManager = RecycleViewUtils.getLinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(myShelfAdapter = new MyShelfAdapter(getActivity()));
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

                if (lastPage || loadingMore) {
                    return;
                }
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    int lastVisibleItem = linearLayoutManager.findLastCompletelyVisibleItemPosition();
                    int totalItemCount = linearLayoutManager.getItemCount();
                    if (totalItemCount != 0 && totalItemCount == (lastVisibleItem + 1)) {
                        loadMore();
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            }
        });
        addSwipeListenerForRecycleView();
    }

    public void reload() {

        clearPageData();
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                startRefreshing();
            }
        });
        MyVolley.addRequest(new JsonObjectRequest(getUrl(pageNo),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        stopRefreshing();
                        myShelfAdapter.setBookList(parseBookList(response));
                        myShelfAdapter.notifyDataSetChanged();
                        checkLastPage(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        stopRefreshing();
                        ToastUtils.show(VolleyErrorHelper.getErrorMessage(volleyError));
                    }
                }));
    }

    private void loadMore() {

        loadingMore = true;
        MyVolley.addRequest(new JsonObjectRequest(getUrl(pageNo + 1),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        loadingMore = false;
                        pageNo++;
                        myShelfAdapter.getBookList().addAll(parseBookList(response));
                        myShelfAdapter.notifyDataSetChanged();
                        checkLastPage(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        loadingMore = false;
                        ToastUtils.show(VolleyErrorHelper.getErrorMessage(volleyError));
                    }
                }));
    }

    private void addSwipeListenerForRecycleView() {

        SwipeableRecyclerViewTouchListener swipeTouchListener =
                new SwipeableRecyclerViewTouchListener(recyclerView,
                        new SwipeableRecyclerViewTouchListener.SwipeListener() {
                            @Override
                            public boolean canSwipe(int position) {
                                return true;
                            }

                            @Override
                            public void onDismissedBySwipeLeft(RecyclerView recyclerView, int[] reverseSortedPositions) {
                                for (int position : reverseSortedPositions) {
                                    String bookId = myShelfAdapter.getBookList().get(position).getId();
                                    showConfirmBar(bookId);
                                    myShelfAdapter.getBookList().remove(position);
                                    myShelfAdapter.notifyItemRemoved(position);
                                }
                                myShelfAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onDismissedBySwipeRight(RecyclerView recyclerView, int[] reverseSortedPositions) {
                                for (int position : reverseSortedPositions) {
                                    String bookId = myShelfAdapter.getBookList().get(position).getId();
                                    showConfirmBar(bookId);
                                    myShelfAdapter.getBookList().remove(position);
                                    myShelfAdapter.notifyItemRemoved(position);
                                }
                                myShelfAdapter.notifyDataSetChanged();
                            }
                        });
        recyclerView.addOnItemTouchListener(swipeTouchListener);
    }

    private void showConfirmBar(final String bookId) {

        SnackbarManager.show(
                Snackbar.with(getActivity())
                        .text("确认从我的书架中移除？")
                        .actionLabel("确认")
                        .actionColor(ColorOverrider.getInstance(getActivity()).getColorAccent())
                        .actionListener(new ActionClickListener() {
                            @Override
                            public void onActionClicked(Snackbar snackbar) {
                                removeFavoriteBook(bookId);
                            }
                        })
                , getActivity());
    }

    private void removeFavoriteBook(String bookId) {

        MyVolley.addRequest(new StringRequest(getRemoveFavoriteBookUrl(bookId),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        boolean result = response.equals(Boolean.TRUE.toString());
                        ToastUtils.show(result ? "移除成功" : "移除失败");
                    }
                },
                MyVolley.getDefaultErrorListener()));
    }

    private String getRemoveFavoriteBookUrl(String bookId) {

        Uri.Builder builder = Uri.parse(ConfigUtils.getBaseUrl() + "/api/v1/favoritebook/delete?").buildUpon();
        builder.appendQueryParameter("bookId", bookId);
        builder.appendQueryParameter("userId", ConfigUtils.getUserId());
        return builder.toString();
    }


    // ========================== 基础方法 ==========================

    private void clearPageData() {

        pageNo = 1;
        lastPage = false;
    }

    private String getUrl(Integer pageNo) {

        Uri.Builder builder = Uri.parse(ConfigUtils.getBaseUrl() + "/api/v1/favoritebook?").buildUpon();
        builder.appendQueryParameter("pageNo", pageNo.toString());
        builder.appendQueryParameter("pageSize", PAGE_SIZE.toString());
        builder.appendQueryParameter("userId", ConfigUtils.getUserId());
        return builder.toString();
    }

    private List<Book> parseBookList(JSONObject response) {

        JSONArray bookJSONArray = JSONUtils.getJSONArray(response, "content", null);
        List<Book> bookList = new ArrayList<>();
        for (int i = 0; i != bookJSONArray.length(); i++) {
            JSONObject bookJSONObject = JSONUtils.getJSONObject(bookJSONArray, i);
            Integer id2 = JSONUtils.getInt(bookJSONObject, "orderNo", 0);
            String name = JSONUtils.getString(bookJSONObject, "name", "");
            String picture = JSONUtils.getString(bookJSONObject, "picture", "");
            String ebook = JSONUtils.getString(bookJSONObject, "ebook", "");
            String ebookFilename = JSONUtils.getString(bookJSONObject, "ebookFilename", "");
            String summary = JSONUtils.getString(bookJSONObject, "summary", "");
            String id = JSONUtils.getString(bookJSONObject, "id", "");
            bookList.add(new Book(id2, name, picture, ebook, ebookFilename, summary, id));
        }
        return bookList;
    }

    private void checkLastPage(JSONObject response) {

        if (lastPage = isLastPage(response)) {
            ToastUtils.show(noData(response) ? "暂无数据" : "已加载全部数据");
        }
    }

    private boolean isLastPage(JSONObject response) {

        return JSONUtils.getBoolean(response, "lastPage", false);
    }

    private boolean noData(JSONObject response) {

        return JSONUtils.getInt(response, "totalPages", 0) == 0;
    }

    private void stopRefreshing() {

        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    private void startRefreshing() {

        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setRefreshing(true);
        }
    }

}
