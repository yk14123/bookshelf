package com.chinafocus.bookshelf.ui.activity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.chinafocus.bookshelf.R;
import com.chinafocus.bookshelf.utils.ProgressBarUtils;

/**
 * @author
 * @date 2019/1/23
 * description：
 */
public class BookShelfCopyrightActivity extends AppCompatActivity {

    private WebView mWebViewCopyright;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.bookshelf_activity_book_detail_copyright);

        initWebView();

        initNavMenu();
    }

    private void initWebView() {
        mWebViewCopyright = findViewById(R.id.wv_book_copyright);
        mWebViewCopyright.requestFocus();
        //設置WebView默認的白色背景為透明色
        mWebViewCopyright.setBackgroundColor(0);
        mWebViewCopyright.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
                super.onReceivedSslError(view, handler, error);
            }

            @Override//跳转监听
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                mWebViewCopyright.loadUrl(url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                ProgressBarUtils.showProgressDialog(BookShelfCopyrightActivity.this, "正在加载中");
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                ProgressBarUtils.dismiss();
            }
        });
        WebSettings webSettings = mWebViewCopyright.getSettings(); //声明WebSettings子类
        webSettings.setDomStorageEnabled(true);  //设置适应Html5的一些方法
        //设置自适应屏幕，两者合用
        webSettings.setUseWideViewPort(true); //将图片调整到适合WebView的大小
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
        //缩放操作
        webSettings.setSupportZoom(true); //支持缩放，默认为true。是下面那个的前提。
        webSettings.setBuiltInZoomControls(true); //设置内置的缩放控件。若为false，则该WebView不可缩放
        webSettings.setDisplayZoomControls(false); //false为隐藏原生的缩放控件
        webSettings.setTextZoom(100);   //设置显示的字体大小

        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);//WebView缓存运用(当加载页面是将页面内容储存在本地，下一次浏览此页面是从本地获取资源）

//        不使用缓存
//        webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_NO_NETWORK);

        //设置WebView属性，能够执行Javascript脚本
        webSettings.setJavaScriptEnabled(true);
        //设置可以访问文件
        webSettings.setAllowFileAccess(true);

        webSettings.setAppCacheEnabled(true);
        webSettings.supportMultipleWindows();
        webSettings.setAllowContentAccess(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSettings.setSavePassword(true);
        webSettings.setSaveFormData(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setLoadsImagesAutomatically(true);

        mWebViewCopyright.setWebChromeClient(new WebChromeClient());//这行最好不要丢掉


        //加载本地文件
        mWebViewCopyright.loadUrl("http://www.cnpereading.com/");
    }

    /**
     * 初始化通用的ActionBar布局
     */
    private void initNavMenu() {
        //Back键
        ImageView mIvBack = findViewById(R.id.iv_bookshelf_left_menu);
        mIvBack.setOnClickListener(v -> {
            if (mWebViewCopyright != null && mWebViewCopyright.canGoBack()) {
                mWebViewCopyright.goBack();
            } else {
                finish();
            }
        });
        //初始化标题
        //当前图书的名称
        TextView mTvBookNavTitle = findViewById(R.id.tv_bookshelf_title);
        mTvBookNavTitle.setText("易阅通");
        //右側menu
        ImageView mIvRightMenu = findViewById(R.id.iv_bookshelf_right_menu);
        mIvRightMenu.setVisibility(View.INVISIBLE);
    }

//    @Override
//    public void onBackPressed() {
//        //如果webView有后退的记录的话，后退
//        if (mWebViewCopyright.canGoBack()) {
//            mWebViewCopyright.goBack();
//        } else if (mWebViewCopyright.canGoForward()) {
//            mWebViewCopyright.goForward(); //如果webView有前进的记录的话，前进
//        } else {
//            finish();
//        }
//        super.onBackPressed();
//    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebViewCopyright.canGoBack()) {
            mWebViewCopyright.goBack();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        if (mWebViewCopyright != null) { // 如果先调用destroy()方法，则会命中if (isDestroyed()) return;这一行代码，
            // 需要先onDetachedFromWindow()，再destory()
            ViewParent parent = mWebViewCopyright.getParent();
            if (parent != null) {
                ((ViewGroup) parent).removeView(mWebViewCopyright);
            }
            mWebViewCopyright.stopLoading();
            // 退出时调用此方法，移除绑定的服务，否则某些特定系统会报错
            mWebViewCopyright.getSettings().setJavaScriptEnabled(false);
            mWebViewCopyright.clearHistory();
            mWebViewCopyright.clearView();//此方法已经废弃，使用webView.loadUrl(“about:blank”)替代
            mWebViewCopyright.removeAllViews();
            mWebViewCopyright.destroy();
        }

        super.onDestroy();
    }
}
