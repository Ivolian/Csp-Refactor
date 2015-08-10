package com.unicorn.csp.activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bartoszlipinski.recyclerviewheader.RecyclerViewHeader;
import com.f2prateek.dart.InjectExtra;
import com.unicorn.csp.R;
import com.unicorn.csp.activity.base.ToolbarActivity;
import com.unicorn.csp.adapter.recyclerView.AnswerAdapter;
import com.unicorn.csp.model.Answer;
import com.unicorn.csp.model.Question;
import com.unicorn.csp.utils.ConfigUtils;
import com.unicorn.csp.utils.JSONUtils;
import com.unicorn.csp.utils.RecycleViewUtils;
import com.unicorn.csp.utils.ToastUtils;
import com.unicorn.csp.volley.MyVolley;
import com.unicorn.csp.volley.toolbox.VolleyErrorHelper;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;


public class QuestionDetailActivity extends ToolbarActivity{


    @InjectExtra("question")
    Question question;

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_detail);
        initToolbar("问答详情", true);
        initViews();
    }


    // ==================== answerAdapter ====================

    public AnswerAdapter answerAdapter;


    // ==================== page data ====================

    protected final Integer PAGE_SIZE = 5;

    Integer pageNo;

    boolean loadingMore;

    boolean lastPage;


    // ==================== initViews ====================

    private void initViews() {

        initRecyclerView();
        reload();
    }

    private void initRecyclerView() {

        // 如果不使用多类型 item 的话，可以加上这句提高效率
        recyclerView.setHasFixedSize(true);
        final LinearLayoutManager linearLayoutManager = RecycleViewUtils.getLinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(answerAdapter = new AnswerAdapter());
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
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this).build());
        attachHeader();
    }

    private void attachHeader(){

        RecyclerViewHeader header = RecyclerViewHeader.fromXml(this,R.layout.question_detail_header);
        TextView tvQuestion = (TextView)header.findViewById(R.id.tv_content);
        tvQuestion.setText(question.getContent());
        header.attachTo(recyclerView);
    }

    public void reload() {

        clearPageData();
        MyVolley.addRequest(new JsonObjectRequest(getUrl(pageNo),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        answerAdapter.setAnswerList(parseAnswerList(response));
                        answerAdapter.notifyDataSetChanged();
                        checkLastPage(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
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
                        answerAdapter.getAnswerList().addAll(parseAnswerList(response));
                        answerAdapter.notifyDataSetChanged();
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


    // ========================== 通用分页方法 ==========================

    protected String getUrl(Integer pageNo) {

        Uri.Builder builder = Uri.parse(ConfigUtils.getBaseUrl() + "/api/v1/answer/list?").buildUpon();
        builder.appendQueryParameter("pageNo", pageNo.toString());
        builder.appendQueryParameter("pageSize", PAGE_SIZE.toString());
        builder.appendQueryParameter("questionId", question.getId());
        return builder.toString();
    }

    private List<Answer> parseAnswerList(JSONObject response) {

        JSONArray contents = JSONUtils.getJSONArray(response, "content", null);
        List<Answer> answerList = new ArrayList<>();
        for (int i = 0; i != contents.length(); i++) {
            JSONObject answerJSONObject = JSONUtils.getJSONObject(contents, i);
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

}
