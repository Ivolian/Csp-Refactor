package com.unicorn.csp.adapter.recycle;

import android.app.Activity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.MaterialDialog;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.malinskiy.materialicons.widget.IconTextView;
import com.unicorn.csp.MyApplication;
import com.unicorn.csp.R;
import com.unicorn.csp.model.News;
import com.unicorn.csp.utils.ToastUtils;

import org.apache.http.Header;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class BookAdapter extends RecyclerView.Adapter<BookAdapter.ViewHolder> {

    Activity activity;

    public BookAdapter(Activity activity) {
        this.activity = activity;
    }

    // todo replace news to book
    private List<News> newsList = new ArrayList<>();


    public class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.more)
        IconTextView more;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @OnClick(R.id.more)
        public void onMoreClick(View view) {
            showContextMenu(view);
        }

        private void showContextMenu(View view) {

            PopupMenu popupMenu = new PopupMenu(activity, view);
            popupMenu.inflate(R.menu.menu_more);
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    switch (menuItem.getItemId()) {
                        case R.id.download:
                            download();
                            return true;
                    }
                    return false;
                }
            });
            popupMenu.show();
        }


        private void download() {

            final MaterialDialog dialog = new MaterialDialog.Builder(activity)
                    .title("下载书籍中")
                    .content("单位: MB")
                    .progress(false, 100, true)
                    .cancelable(false)
                    .show();

            String fileUrl = "http://192.168.1.101:3000/withub/3.mp4";
            AsyncHttpClient client = new AsyncHttpClient();
            client.get(fileUrl, new FileAsyncHttpResponseHandler(MyApplication.getInstance()) {
                @Override
                public void onSuccess(int statusCode, Header[] headers, File response) {
                    dialog.setTitle("下载完成");
                    dialog.setCancelable(true);
                }


                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, File file) {
                    ToastUtils.show("sorry");
                }

                @Override
                public void onProgress(long bytesWritten, long totalSize) {
                    super.onProgress(bytesWritten, totalSize);
                    dialog.setMaxProgress((int) totalSize / 1024);

                    int progress = (int) (bytesWritten * 100 / totalSize);
                    dialog.setProgress((int) bytesWritten / 1024);
//                    Log.e("result", totalSize + "");
//                    Log.e("result", bytesWritten + "");
//                    ToastUtils.show(bytesWritten + "");
                }
            });
        }


    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_book, viewGroup, false));
    }

    @Override
    public int getItemCount() {

        return newsList.size();
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        News news = newsList.get(position);
    }

    public List<News> getNewsList() {

        return newsList;
    }

    public void setNewsList(List<News> newsList) {

        this.newsList = newsList;
    }

}
