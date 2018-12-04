package com.chinafocus.bookshelf.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.chinafocus.bookshelf.R;
import com.chinafocus.bookshelf.global.BookShelfConstant;
import com.chinafocus.bookshelf.model.bean.BookContentRawBean;

import java.util.List;

/**
 * 图书内容章节适配器
 *
 * @author HUANG_JIN
 * @version 1.0
 * create at 2018/11/27 14:47
 */
public class BookContentDetailAdapter extends RecyclerView.Adapter<BookContentDetailAdapter.BookCategoryHolder> {
    private static final String TAG = "ShelfCategoryAdapter";
    private Context mContext;

    private List<BookContentRawBean.BookContentResultBean> mBookContents;

    //内容字号 --->默认250 zoom
    private int mFontOption = BookShelfConstant.NORMAL;

    public BookContentDetailAdapter(Context context, List<BookContentRawBean.BookContentResultBean> result) {
        this.mContext = context;
        this.mBookContents = result;
    }

    @NonNull
    @Override
    public BookCategoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View contentView = LayoutInflater.from(mContext).inflate(
                R.layout.bookshelf_recycle_item_book_content, parent, false);
        return new BookCategoryHolder(contentView);
    }

    @Override
    public void onBindViewHolder(@NonNull BookCategoryHolder holder, int position) {
        BookContentRawBean.BookContentResultBean bookContentResultBean = mBookContents.get(position);
        if (bookContentResultBean != null) {
            String current = bookContentResultBean.getCurrent();
            String css = bookContentResultBean.getCss();
            if (!TextUtils.isEmpty(current)) {
                //加载本地文件
                String linkCss = "<link rel=\"stylesheet\" href=\"" + css + "\" type=\"text/img\">";
                Log.d(TAG, "onRefreshFinished: linkCss >>>" + linkCss);
                String body = "<html><header>" + linkCss + "</header>" + current + "</body></html>";
                WebSettings webSettings = holder.mWebContent.getSettings(); //声明WebSettings子类

                webSettings.setDomStorageEnabled(true);  //设置适应Html5的一些方法
                //设置自适应屏幕，两者合用
                webSettings.setUseWideViewPort(true); //将图片调整到适合WebView的大小
                webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
                //缩放操作
                webSettings.setSupportZoom(false); //支持缩放，默认为true。是下面那个的前提。
                webSettings.setBuiltInZoomControls(false); //设置内置的缩放控件。若为false，则该WebView不可缩放
                webSettings.setDisplayZoomControls(false); //隐藏原生的缩放控件
                webSettings.setTextZoom(mFontOption);
                holder.mWebContent.setWebViewClient(new WebViewClient() {
                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        view.loadUrl(url);
                        //禁止当前页面内部链接地址的跳转
                        return false;
                    }

                    @Override
                    public void onPageFinished(WebView view, String url) {
                        Log.d(TAG, "onPageFinished: url >>>" + url);
                        super.onPageFinished(view, url);
                    }

                    @Override
                    public void onPageStarted(WebView view, String url, Bitmap favicon) {
                        Log.d(TAG, "onPageStarted: url >>>" + url);
                        super.onPageStarted(view, url, favicon);
                    }

                    @Override
                    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                        handler.proceed();
                    }
                });
                holder.mWebContent.loadDataWithBaseURL(css, body, "text/html", "UTF-8", null);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mBookContents != null ? mBookContents.size() : 0;
    }

    /**
     * 添加content实体数据bean到数据链表中
     */
    public void addContentEntity(BookContentRawBean.BookContentResultBean contentEntity) {
        mBookContents.add(contentEntity);
        notifyItemInserted(getItemCount());
    }

    /**
     * 刷新當前WebView内部列表的item的字体大小
     *
     * @param fontOption 字號大小
     */
    public void setFontSize(int fontOption) {
        this.mFontOption = fontOption;
        notifyDataSetChanged();
    }

    class BookCategoryHolder extends RecyclerView.ViewHolder {
        private WebView mWebContent;

        BookCategoryHolder(View itemView) {
            super(itemView);
            mWebContent = itemView.findViewById(R.id.wv_book_content_detail);
        }
    }
}
