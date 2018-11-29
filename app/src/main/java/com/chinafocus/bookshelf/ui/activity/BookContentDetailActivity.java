package com.chinafocus.bookshelf.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.chinafocus.bookshelf.R;
import com.chinafocus.bookshelf.base.BaseActivity;
import com.chinafocus.bookshelf.global.BookShelfConstant;
import com.chinafocus.bookshelf.model.bean.BookContentRawBean;
import com.chinafocus.bookshelf.presenter.shelves.BookContentDetailPresenter;
import com.chinafocus.bookshelf.presenter.shelves.IShelvesMvpContract;
import com.chinafocus.bookshelf.ui.fragment.FontSettingsDialog;
import com.chinafocus.bookshelf.utils.SpUtil;

import java.util.List;

/**
 * 图书章节内容頁面
 *
 * @author HUANG_JIN
 * @version 1.0
 * create at 2018/11/27 15:34
 */

public class BookContentDetailActivity extends BaseActivity<BookContentRawBean.BookContentResultBean> {
    private static final String TAG = "BookContentDetail";
    //内容展示控件
    private WebView mWvBookContent;
    //当前图书书柜id
    private int mShelfId;
    //当前图书id
    private int mBookId;
    //当前图书的分类
    private int mCategoryId;
    //当前图书的名称
    private String mBookName;
    //page
    private String mPageId;
    private FontSettingsDialog mFontDialog;

    @Override
    protected void initView() {
        getExtraFromIntent();
        setContentView(R.layout.bookshelf_activity_book_content_detail);
        initNavMenu();
        //初始化WebView
        mWvBookContent = findViewById(R.id.wv_book_content_detail);
        WebSettings webSettings = mWvBookContent.getSettings(); //声明WebSettings子类

        webSettings.setDomStorageEnabled(true);  //设置适应Html5的一些方法
        //设置自适应屏幕，两者合用
        webSettings.setUseWideViewPort(true); //将图片调整到适合WebView的大小
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
        //缩放操作
        webSettings.setSupportZoom(false); //支持缩放，默认为true。是下面那个的前提。
        webSettings.setBuiltInZoomControls(false); //设置内置的缩放控件。若为false，则该WebView不可缩放
        webSettings.setDisplayZoomControls(false); //隐藏原生的缩放控件
        int fontOption = SpUtil.getInt(this, BookShelfConstant.BOOKSHELF_FONT_SETTING);
        setFontSize(fontOption);
        mWvBookContent.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
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
        //初始化Presenter
        //控制器
        IShelvesMvpContract.IPresenter mPresenter = new BookContentDetailPresenter(this);
        mPresenter.refresh(IShelvesMvpContract.REFRESH_BOOK_CONTENT_DETAIL,
                new String[]{String.valueOf(mShelfId),
                        String.valueOf(mCategoryId),
                        String.valueOf(mBookId), mPageId});
    }

    /**
     * 初始化通用的ActionBar布局
     */
    private void initNavMenu() {
        //Back键
        ImageView mIvBack = findViewById(R.id.iv_bookshelf_left_menu);
        mIvBack.setOnClickListener(v -> finish());
        //初始化标题
        TextView mTvCategoryName = findViewById(R.id.tv_bookshelf_title);
        if (!TextUtils.isEmpty(mBookName)) {
            mTvCategoryName.setText(mBookName);
        }
        //右側menu
        ImageView mIvRightMenu = findViewById(R.id.iv_bookshelf_right_menu);
        mIvRightMenu.setVisibility(View.VISIBLE);
        mIvRightMenu.setOnClickListener(v -> {
            //调整字体大小
            if (mFontDialog == null) {
                mFontDialog = new FontSettingsDialog(this);
                mFontDialog.setOnFontListener(fontOption -> {
                    Log.d(TAG, "initNavMenu: fontOption >>> " + fontOption);
                    setFontSize(fontOption);
                });
            }
            if (!mFontDialog.isShowing()) {
                mFontDialog.show();
            }
        });
    }

    /**
     * 設置字體顔色
     *
     * @param fontOption {@link FontSettingsDialog}類常量值定义
     */
    private void setFontSize(int fontOption) {
        switch (fontOption) {
            case FontSettingsDialog.FONT_SMALLER:
                mWvBookContent.getSettings().setTextZoom(200);
                break;
            case FontSettingsDialog.FONT_STANDARD:
                mWvBookContent.getSettings().setTextZoom(250);
                break;
            case FontSettingsDialog.FONT_LARGE:
                mWvBookContent.getSettings().setTextZoom(300);
                break;
            case FontSettingsDialog.FONT_LARGER:
                mWvBookContent.getSettings().setTextZoom(350);
                break;
            case FontSettingsDialog.FONT_LARGEST:
                mWvBookContent.getSettings().setTextZoom(400);
                break;
            default:
                mWvBookContent.getSettings().setTextZoom(250);
                break;
        }
    }

    /**
     * 获取intent跳转的参数
     */
    private void getExtraFromIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            mShelfId = intent.getIntExtra(BookShelfConstant.SHELF_ID, 1);
            mBookId = intent.getIntExtra(BookShelfConstant.BOOK_ID, 1);
            mCategoryId = intent.getIntExtra(BookShelfConstant.CATEGORY_ID, 1);
            mBookName = intent.getStringExtra(BookShelfConstant.BOOK_NAME);
            mPageId = intent.getStringExtra(BookShelfConstant.PAGE);
            Log.d(TAG, "getExtraFromIntent: mShelfId >>>" + mShelfId
                    + " mBookId >>> " + mBookId + " mCategoryId >>> "
                    + mCategoryId + " mBookName >>> " + mBookName
                    + " mPageId >>> " + mPageId);
        }
    }


    @Override
    public void onRefreshFinished(String refreshType, List<BookContentRawBean.BookContentResultBean> result) {
        BookContentRawBean.BookContentResultBean bookContentResultBean = result.get(0);
        if (bookContentResultBean != null) {
            String current = bookContentResultBean.getCurrent();
            String css = bookContentResultBean.getCss();
            if (!TextUtils.isEmpty(current)) {
                //加载本地文件
                String linkCss = "<link rel=\"stylesheet\" href=\"" + css + "\" type=\"text/img\">";
                Log.d(TAG, "onRefreshFinished: linkCss >>>" + linkCss);
                String body = "<html><header>" + linkCss + "</header>" + current + "</body></html>";
                mWvBookContent.loadDataWithBaseURL(linkCss, body, "text/html", "UTF-8", null);
            }
        }
    }

    @Override
    public void showTips(String message) {

    }

    @Override
    protected void onDestroy() {
        //防止WebView导致的内存泄露
        if (mWvBookContent != null) {
            // 如果先调用destroy()方法，则会命中
            // if (isDestroyed()) return;这一行代码，需要先onDetachedFromWindow()，再destroy()
            ViewParent parent = mWvBookContent.getParent();
            if (parent != null) {
                ((ViewGroup) parent).removeView(mWvBookContent);
            }
            mWvBookContent.stopLoading();
            // 退出时调用此方法，移除绑定的服务，否则某些特定系统会报错
            mWvBookContent.getSettings().setJavaScriptEnabled(false);
            mWvBookContent.clearHistory();
//            mWvBookContent.clearView();
            mWvBookContent.removeAllViews();
            mWvBookContent.destroy();
        }
        super.onDestroy();
    }
}
