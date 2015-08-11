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
import com.unicorn.csp.adapter.recyclerView.question.QuestionFragmentAdapter;
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


// clear
public class QuestionFragment extends LazyLoadFragment {


    @Override
    public int getLayoutResId() {
        return R.layout.fragment_question;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return super.onCreateView(inflater, container, savedInstanceState);
    }


    // ==================== views ====================

    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;


    // ==================== 因为 ExpandableAdapter 没有提供改变内部值的方法，暂时在 fragment 这边维护 data  ====================

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
        recyclerView.setAdapter(new QuestionFragmentAdapter(getActivity(), new ArrayList<ParentObject>(), R.id.itv_expand, 500));
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
                        notifyDataSetChanged();
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
                        notifyDataSetChanged();
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

    private void notifyDataSetChanged() {

        QuestionFragmentAdapter questionFragmentAdapter = new QuestionFragmentAdapter(getActivity(), questionList, R.id.itv_expand, 500);
        recyclerView.setAdapter(questionFragmentAdapter);
        questionFragmentAdapter.notifyDataSetChanged();
    }


    // ========================== 分页的常见方法 ==========================

    private String getUrl(Integer pageNo) {

        Uri.Builder builder = Uri.parse(ConfigUtils.getBaseUrl() + "/api/v1/question/list").buildUpon();
        builder.appendQueryParameter("pageNo", pageNo.toString());
        builder.appendQueryParameter("pageSize", PAGE_SIZE.toString());
        return builder.toString();
    }

    private List<ParentObject> parseQuestionList(JSONObject response) {

        JSONArray questionJSONArray = JSONUtils.getJSONArray(response, "content", null);
        List<ParentObject> questionList = new ArrayList<>();
        for (int i = 0; i != questionJSONArray.length(); i++) {
            JSONObject questionJSONObject = JSONUtils.getJSONObject(questionJSONArray, i);
            String id = JSONUtils.getString(questionJSONObject, "id", "");
            String content = JSONUtils.getString(questionJSONObject, "content", "");
            String username = JSONUtils.getString(questionJSONObject, "username", "");
            Date eventTime = new Date(JSONUtils.getLong(questionJSONObject, "eventTime", 0));
            Question question = new Question(id, content, username, eventTime);
            question.setChildObjectList(parseAnswerList(questionJSONObject));
            questionList.add(question);
        }
        return questionList;
    }

    private List<Object> parseAnswerList(JSONObject questionJSONObject) {

        List<Object> answerList = new ArrayList<>();
        JSONArray answerJSONArray = JSONUtils.getJSONArray(questionJSONObject, "answerList", new JSONArray());
        for (int i = 0; i != answerJSONArray.length(); i++) {
            JSONObject answerJSONObject = JSONUtils.getJSONObject(answerJSONArray, i);
            String id = JSONUtils.getString(answerJSONObject, "id", "");
            String content = JSONUtils.getString(answerJSONObject, "content", "");
            String username = JSONUtils.getString(answerJSONObject, "username", "");
            Date eventTime = new Date(JSONUtils.getLong(answerJSONObject, "eventTime", 0));
            answerList.add(new Answer(id, content, username, eventTime));
        }
        return answerList;
    }

    private void clearPageData() {

        pageNo = 1;
        lastPage = false;
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
