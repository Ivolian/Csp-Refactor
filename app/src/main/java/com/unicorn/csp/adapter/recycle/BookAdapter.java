package com.unicorn.csp.adapter.recycle;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.toolbox.NetworkImageView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.unicorn.csp.MyApplication;
import com.unicorn.csp.R;
import com.unicorn.csp.utils.ConfigUtils;
import com.unicorn.csp.utils.ToastUtils;
import com.unicorn.csp.volley.MyVolley;

import org.apache.http.Header;
import org.geometerplus.android.fbreader.OrientationUtil;
import org.geometerplus.android.fbreader.api.FBReaderIntents;
import org.geometerplus.android.fbreader.library.BookInfoActivity;
import org.geometerplus.fbreader.book.Book;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


public class BookAdapter extends RecyclerView.Adapter<BookAdapter.ViewHolder> {

    Activity activity;

    public BookAdapter(Activity activity) {
        this.activity = activity;
    }

    private List<com.unicorn.csp.model.Book> bookList = new ArrayList<>();

    public class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.cardview)
        CardView cardView;

        @Bind(R.id.tv_name)
        TextView tvName;

        @Bind(R.id.niv_picture)
        NetworkImageView nivPicture;


        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_book, viewGroup, false));
    }

    @Override
    public int getItemCount() {

        return bookList.size();
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        final com.unicorn.csp.model.Book book = bookList.get(position);
        viewHolder.tvName.setText(book.getName());
        // todo change book_default
        viewHolder.nivPicture.setDefaultImageResId(R.drawable.book_default);
        viewHolder.nivPicture.setImageUrl(ConfigUtils.getBaseUrl() + book.getPicture(), MyVolley.getImageLoader());

        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isBookExist(book)) {
                    openBook(book);
                } else {
                    showConfirmDownloadDialog(book);
                }
            }
        });
        viewHolder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (isBookExist(book)) {
                    showConfirmDeleteDialog(book);
                }
                return true;
            }
        });
    }

    private MaterialDialog showConfirmDeleteDialog(final com.unicorn.csp.model.Book book) {

        return new MaterialDialog.Builder(activity)
                .title("确认要删除该书籍？")
                .positiveText("确认")
                .negativeText("取消")
                .cancelable(false)
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        dialog.dismiss();
                        File file = new File(getBookPath(book));
                        boolean result = file.delete();
                        showResultDialog(result ? "删除成功" : "删除失败");
                    }
                })
                .show();
    }

    private MaterialDialog showResultDialog(String result) {

        return new MaterialDialog.Builder(activity)
                .title(result)
                .positiveText("确认")
                .cancelable(false)
                .show();
    }

    private MaterialDialog showConfirmDownloadDialog(final com.unicorn.csp.model.Book book) {

        return new MaterialDialog.Builder(activity)
                .title("该书籍未缓存，是否需要下载？")
                .positiveText("确认")
                .negativeText("取消")
                .cancelable(false)
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        dialog.dismiss();
                        download(book);
                    }
                })
                .show();
    }

    private boolean isBookExist(com.unicorn.csp.model.Book book) {

        return new File(getBookPath(book)).exists();
    }

    private String getBookPath(com.unicorn.csp.model.Book book) {

        return ConfigUtils.getDownloadDirPath() + "/" + book.getEbookFilename();
    }

    private void openBook(com.unicorn.csp.model.Book book) {

        // todo 研究 Fbreader book 的使用
        // todo 目前只是暂时用 BookDetailActivity 解决
        // todo 貌似 bookId 和 bookPath 都不能重复
        Book bookzz = new Book(book.getId(), getBookPath(book), book.getName(), null, null);
        Intent intent = new Intent(activity, BookInfoActivity.class);
        FBReaderIntents.putBookExtra(intent, bookzz);
        OrientationUtil.startActivity(activity, intent);
    }

    private void download(final com.unicorn.csp.model.Book book) {

        final MaterialDialog downloadDialog = showDownloadDialog(book);
        String url = ConfigUtils.getBaseUrl() + book.getEbook();
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, new FileAsyncHttpResponseHandler(MyApplication.getInstance()) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, File response) {

                copyfile(response, new File(getBookPath(book)), true);
                downloadDialog.setTitle("下载完成");
                downloadDialog.setActionButton(DialogAction.POSITIVE, "开始阅读");
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, File file) {

                ToastUtils.show("下载失败");
                downloadDialog.dismiss();
            }

            @Override
            public void onProgress(long bytesWritten, long totalSize) {

                downloadDialog.setMaxProgress((int) totalSize / 1024);
                downloadDialog.setProgress((int) bytesWritten / 1024);
            }
        });
    }

    private MaterialDialog showDownloadDialog(final com.unicorn.csp.model.Book book) {

        return new MaterialDialog.Builder(activity)
                .title("下载书籍中")
                .progress(false, 100)
                .cancelable(false)
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        dialog.dismiss();
                        openBook(book);
                    }
                })
                .show();
    }

    public void copyfile(File fromFile, File toFile, Boolean rewrite) {

        if (!fromFile.exists()) {

            return;

        }

        if (!fromFile.isFile()) {

            return;

        }

        if (!fromFile.canRead()) {

            return;

        }

        if (!toFile.getParentFile().exists()) {

            toFile.getParentFile().mkdirs();

        }

        if (toFile.exists() && rewrite) {

            toFile.delete();

        }


        // if (!toFile.canWrite()) {

        // MessageDialog.openError(new Shell(),"错误信息","不能够写将要复制的目标文件" + toFile.getPath());

        // Toast.makeText(this,"不能够写将要复制的目标文件", Toast.LENGTH_SHORT);

        // return ;

        // }

        try {

            java.io.FileInputStream fosfrom = new java.io.FileInputStream(fromFile);

            java.io.FileOutputStream fosto = new FileOutputStream(toFile);

            byte bt[] = new byte[1024];

            int c;

            while ((c = fosfrom.read(bt)) > 0) {

                fosto.write(bt, 0, c); //将内容写到新文件当中

            }

            fosfrom.close();

            fosto.close();

        } catch (Exception ex) {

            Log.e("readfile", ex.getMessage());

        }

    }

    public List<com.unicorn.csp.model.Book> getBookList() {

        return bookList;
    }

    public void setBookList(List<com.unicorn.csp.model.Book> bookList) {

        this.bookList = bookList;
    }

}
