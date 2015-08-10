package com.unicorn.csp.fragment.question;

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
import com.bignerdranch.expandablerecyclerview.Model.ParentObject;
import com.unicorn.csp.R;
import com.unicorn.csp.adapter.recyclerView.QuestionAdapter;
import com.unicorn.csp.fragment.base.LazyLoadFragment;
import com.unicorn.csp.model.Answer;
import com.unicorn.csp.model.Question;
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
import java.util.Date;
import java.util.List;

import butterknife.Bind;


public class QuestionFragment extends LazyLoadFragment {

    // todo 5dp
    @Override
    public int getLayoutResId() {
        return R.layout.fragment_question;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        initViews();
        return rootView;
    }


    // ==================== views ====================

    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;


    // ==================== adapter ====================

    QuestionAdapter questionAdapter;

    List<ParentObject> questionList;

    // ==================== page data ====================

    final Integer PAGE_SIZE = 5;

    Integer pageNo;

    boolean loadingMore;

    boolean lastPage;


    // ==================== onFirstUserVisible ====================

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
        questionList = new ArrayList<>();
        questionAdapter = new QuestionAdapter(getActivity(), new ArrayList<ParentObject>(), R.id.itv_expand, 500);
        recyclerView.setAdapter(questionAdapter);
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
                        questionList = parseQuestionList(response);
                        questionAdapter = new QuestionAdapter(getActivity(), questionList, R.id.itv_expand, 500);
                        recyclerView.setAdapter(questionAdapter);
                        questionAdapter.notifyDataSetChanged();
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
                        questionList.addAll(parseQuestionList(response));
                        questionAdapter = new QuestionAdapter(getActivity(), questionList, R.id.itv_expand, 500);
                        recyclerView.setAdapter(questionAdapter);
                        questionAdapter.notifyDataSetChanged();
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

    // ========================== 基础方法 ==========================

    private void clearPageData() {

        pageNo = 1;
        lastPage = false;
    }

    private String getUrl(Integer pageNo) {

        Uri.Builder builder = Uri.parse(ConfigUtils.getBaseUrl() + "/api/v1/question/list").buildUpon();
        builder.appendQueryParameter("pageNo", pageNo.toString());
        builder.appendQueryParameter("pageSize", PAGE_SIZE.toString());
        return builder.toString();
    }

    // todo modify
    private List<ParentObject> parseQuestionList(JSONObject response) {

        JSONArray questionJSONArray = JSONUtils.getJSONArray(response, "content", null);
        List<ParentObject> questionList = new ArrayList<>();
        for (int i = 0; i != questionJSONArray.length(); i++) {
            JSONObject questionJSONObject = JSONUtils.getJSONObject(questionJSONArray, i);
            String content = JSONUtils.getString(questionJSONObject, "content", "");
            String username = JSONUtils.getString(questionJSONObject, "username", "");
            long time = JSONUtils.getLong(questionJSONObject, "eventTime", 0);
            Date eventTime = new Date(time);
            String id = JSONUtils.getString(questionJSONObject, "id", "");
            Question question = new Question(content, username, eventTime, id);

            List<Object> answerList = new ArrayList<>();
            JSONArray answerJSONArray = JSONUtils.getJSONArray(questionJSONObject, "answerList", new JSONArray());
            for (int j = 0; j != answerJSONArray.length(); j++) {
                JSONObject answerJSONObject = JSONUtils.getJSONObject(answerJSONArray, j);
                String answerId = JSONUtils.getString(answerJSONObject, "id", "");
                String answerContent = JSONUtils.getString(answerJSONObject, "content", "");
                String answerUsername = JSONUtils.getString(answerJSONObject,"username","");
                long time2 = JSONUtils.getLong(answerJSONObject, "eventTime", 0);
                Date eventTime2 = new Date(time2);
                answerList.add(new Answer(answerContent, answerUsername, eventTime2, answerId));
            }

            question.setChildObjectList(answerList);
            questionList.add(question);
        }
        return questionList;
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
