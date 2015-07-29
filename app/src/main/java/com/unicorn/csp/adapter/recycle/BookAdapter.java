package com.unicorn.csp.adapter.recycle;

import android.app.Activity;
import android.os.Environment;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import org.geometerplus.android.fbreader.FBReader;
import org.geometerplus.fbreader.book.Book;

import java.io.File;
import java.io.FileOutputStream;
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

//
//            String epubPath = Environment.getExternalStorageDirectory() + "/test.epub";
//            File epub = new File(epubPath);
//
//            if (!epub.exists()){
//                try {
//                    InputStream is = activity.getAssets().open("test.epub");
//                    FileOutputStream fos = new FileOutputStream(epub);
//                    byte[] buffer = new byte[1024];
//                    int byteCount = 0;
//                    while ((byteCount = is.read(buffer)) != -1) {//循环从输入流读取 buffer字节
//                        fos.write(buffer, 0, byteCount);//将读取的输入流写入到输出流
//                    }
//                    fos.flush();//刷新缓冲区
//                    is.close();
//                    fos.close();
//                } catch (Exception e) {
//                    //
//                }
//            }
//
//
//            Book book = new Book(101,epubPath,"hehe",null,null);
//            FBReader.openBookActivity(activity, book, null);


            final MaterialDialog dialog = new MaterialDialog.Builder(activity)
                    .title("下载书籍中")
                    .content("单位: MB")
                    .progress(false, 100, true)
                    .cancelable(false)
                    .show();

            String fileUrl = "http://192.168.1.101:3000/withub/1.epub";
            AsyncHttpClient client = new AsyncHttpClient();
            client.get(fileUrl, new FileAsyncHttpResponseHandler(MyApplication.getInstance()) {
                @Override
                public void onSuccess(int statusCode, Header[] headers, File response) {
                    dialog.setTitle("下载完成");
                    dialog.dismiss();


                    String epubPath = Environment.getExternalStorageDirectory() + "/test2.epub";
                    copyfile(response, new File(epubPath), true);
//                    ToastUtils.show(response.getAbsolutePath());
//                    Log.e("result",response.getAbsolutePath() );


                    Book book = new Book(101, epubPath, "hehe", null, null);
                    FBReader.openBookActivity(activity, book, null);
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


        public void copyfile(File fromFile, File toFile, Boolean rewrite)

        {

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
