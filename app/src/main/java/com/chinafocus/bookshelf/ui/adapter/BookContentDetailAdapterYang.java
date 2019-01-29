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
import com.chinafocus.bookshelf.bean.BookContentRawBean.BookContentResultBean;
import com.chinafocus.bookshelf.global.BookShelfConstant;
import com.chinafocus.bookshelf.ui.widgets.MyNoTouchWebView;
import com.chinafocus.bookshelf.utils.LogUtil;
import com.chinafocus.bookshelf.utils.SpUtil;

import java.util.List;

/**
 * 图书内容章节适配器
 *
 * @author HUANG_JIN
 * @version 1.0
 * create at 2018/11/27 14:47
 */
public class BookContentDetailAdapterYang extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "ContentDetail";
    private Context mContext;

    private List<BookContentResultBean> mBookContents;

    //内容字号 --->默认250 zoom
    private int mFontOption;

    private static final int CONTENT_VIEW = 0;
    private static final int FOOT_VIEW_LOAD_MORE = 1;

    public BookContentDetailAdapterYang(Context context, List<BookContentResultBean> result) {
        this.mContext = context;
        this.mBookContents = result;
        mFontOption = SpUtil.getInt(mContext, BookShelfConstant.BOOKSHELF_FONT_SETTING, BookShelfConstant.NORMAL);
        Log.d(TAG, " fontOption >>> " + mFontOption);
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1) {
            return FOOT_VIEW_LOAD_MORE;
        }
        return CONTENT_VIEW;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == FOOT_VIEW_LOAD_MORE) {
            View foot = LayoutInflater.from(mContext).inflate(R.layout.bookshelf_recycle_item_book_content_foot, parent, false);

            return new BookFootViewHolder(foot);
        }

        View contentView = LayoutInflater.from(mContext).inflate(
                R.layout.bookshelf_recycle_item_book_content, parent, false);
        return new BookContentHolder(contentView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (getItemViewType(position) == FOOT_VIEW_LOAD_MORE) {
            if (position >= getItemCount() - 1) {
                if (mLoadMore != null) {
                    mLoadMore.onLoadMore();
                }
                return;
            }
        }

        if (getItemViewType(position) == CONTENT_VIEW) {
            initBindBookContent(holder, position);
        }

    }


    private ILoadMore mLoadMore;

    public void addOnLoadMoreFromFootView(ILoadMore loadMore) {
        mLoadMore = loadMore;
    }

    @FunctionalInterface
    public interface ILoadMore {
        void onLoadMore();
    }

    private void initBindBookContent(@NonNull RecyclerView.ViewHolder holder, int position) {
        BookContentResultBean bookContentResultBean = mBookContents.get(position);
        if (bookContentResultBean != null) {
            String current = bookContentResultBean.getCurrent();


//            https://book-common.oss-cn-beijing.aliyuncs.com/css/common.css
//            String css = "https://book-common.oss-cn-beijing.aliyuncs.com/css/common.css";
            if (!TextUtils.isEmpty(current)) {


                if (current.contains("\uE003")) {
                    current = current.replace("\uE003", " ");
                }
                if (current.contains("\uE10B")) {
                    current = current.replace("\uE10B", " ");
                }
                if (current.contains("\uE011")) {
                    current = current.replace("\uE011", " ");
                }

                LogUtil.veryLongForI("BookContentDetailAdapterYang", current);

                //加载本地文件
                String linkCss = "<link rel=\"stylesheet\" href=\"" + bookContentResultBean.getCss() + "\" type=\"text/css\">";
                String linkRemote = "<link rel=\"stylesheet\" href=\"https://book-common.oss-cn-beijing.aliyuncs.com/css/common.css\" type=\"text/css\">";
//                String linkRemote = "<link rel=\"stylesheet\" href=\"file:///android_asset/bookshelfcontentstylesheet.css\" type=\"text/css\">";
//                String body = "<html><header>" + linkCss + linkRemote + "</header>" + current + "</body></html>";
                String body = "<html><header>" + linkRemote + "</header>" + current + "</body></html>";

                WebSettings webSettings = ((BookContentHolder) holder).mWebContent.getSettings(); //声明WebSettings子类

                webSettings.setDomStorageEnabled(true);  //设置适应Html5的一些方法
                //设置自适应屏幕，两者合用
                webSettings.setUseWideViewPort(true); //将图片调整到适合WebView的大小
                webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
                //缩放操作
                webSettings.setSupportZoom(false); //支持缩放，默认为true。是下面那个的前提。
                webSettings.setBuiltInZoomControls(false); //设置内置的缩放控件。若为false，则该WebView不可缩放
                webSettings.setDisplayZoomControls(false); //隐藏原生的缩放控件
                webSettings.setTextZoom(mFontOption);
                ((BookContentHolder) holder).mWebContent.setWebViewClient(new WebViewClient() {
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

                ((BookContentHolder) holder).mWebContent.loadDataWithBaseURL(linkRemote, body, "text/html", "UTF-8", null);
//                holder.mWebContent.loadDataWithBaseURL(linkCss, body, "text/html", "UTF-8", null);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mBookContents != null ? mBookContents.size() + 1 : 1;
    }

    /**
     * 添加content实体数据bean到数据链表中
     */
    public void addContentEntity(BookContentResultBean contentEntity) {
//        mFontOption = SpUtil.getInt(mContext, BookShelfConstant.BOOKSHELF_FONT_SETTING, BookShelfConstant.NORMAL);
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

    class BookContentHolder extends RecyclerView.ViewHolder {
        private MyNoTouchWebView mWebContent;

        BookContentHolder(View itemView) {
            super(itemView);
            mWebContent = itemView.findViewById(R.id.wv_book_content_detail);
        }
    }

    class BookFootViewHolder extends RecyclerView.ViewHolder {

        BookFootViewHolder(View itemView) {
            super(itemView);
        }
    }
}
