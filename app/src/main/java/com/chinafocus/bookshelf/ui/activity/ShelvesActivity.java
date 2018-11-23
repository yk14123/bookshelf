package com.chinafocus.bookshelf.ui.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.chinafocus.bookshelf.R;
import com.chinafocus.bookshelf.base.BaseActivity;
import com.chinafocus.bookshelf.base.PermissionListener;
import com.chinafocus.bookshelf.model.bean.BookContentRawBean;
import com.chinafocus.bookshelf.model.bean.ShelvesResultBean;
import com.chinafocus.bookshelf.model.network.ApiManager;
import com.chinafocus.bookshelf.presenter.shelves.AbstractShelvesPresenter;
import com.chinafocus.bookshelf.presenter.shelves.INetListener;
import com.chinafocus.bookshelf.presenter.shelves.IShelvesMvpContract;
import com.chinafocus.bookshelf.presenter.shelves.ShelvesDetailPresenter;
import com.chinafocus.bookshelf.presenter.shelves.ShelvesPresenter;
import com.google.gson.Gson;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

//import com.chinafocus.bookshelf.model.bean.ShelvesCategoryResultBean;

public class ShelvesActivity extends BaseActivity<ShelvesResultBean> {

    private CoordinatorLayout mRootLayout;
    private RecyclerView mRecyclerView;
    //    private NewsMvpAdapter mRecyclerAdapter;
//    private List<NewsBean> mNewsBeans = new ArrayList<>();
//    private IShelvesMvpContract.IPresenter mPresenter;
    private LinearLayoutManager mLayoutMgr;
    private WebView mWebView;

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.bookshelf_activity_mvp);

//        requestRuntimePermission();

//        initView();


//        testShelvesCategory();

//        testBookCategoryDetail();

//        testBookMetadata();

//        testBookContentPage();

//        initWebView();
    }

    @SuppressLint("CheckResult")
    private void testBookCategoryDetail() {
        ApiManager.getInstance().getService()
                .getBookCategoryDetail("2", "14")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        i("MyLog", s);
                    }
                });
    }

    @SuppressLint("CheckResult")
    private void testBookMetadata() {
        ApiManager.getInstance().getService()
                .getBookMetadata("2", "14", "175")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        i("MyLog", s);
                    }
                });
    }

    @SuppressLint("CheckResult")
    @NonNull
    private void testShelvesCategory() {
        ApiManager.getInstance().getService()
                .getShelvesDetail("2")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        i("MyLog", s);
                    }
                });
    }

    @SuppressLint("CheckResult")
    private void testBookContentPage() {
        ApiManager.getInstance().getService()
                .getBookContentDetail("2", "13", "189", "chapter001.html#a007")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {

                        i("MyLog", s);

                        BookContentRawBean bookContentRawBean = new Gson().fromJson(s, BookContentRawBean.class);

                        String current = bookContentRawBean.getData().getCurrent();
                        String css = bookContentRawBean.getData().getCss();

                        Log.i("MyLog", "css-->" + css);
                        String htmlDataFromHttpCss = "<link rel=\"stylesheet\" type=\"text/css\" href=\"" + css + "\" />" + current;
                        // lets assume we have /assets/style.css file
                        mWebView.loadDataWithBaseURL(css, htmlDataFromHttpCss, "text/html", "UTF-8", null);
//                        mWebView.loadUrl("file:///android_asset/bookcaseIntro.html");

//                        String htmlDataFromLocal = "<link rel=\"stylesheet\" type=\"text/css\" href=\"" + "StylesheetChange.css" + "\" />" + htmlDataFromHttpCss;
//                        // lets assume we have /assets/style.css file
//                        mWebView.loadDataWithBaseURL("file:///android_asset/StylesheetChange.css", htmlDataFromLocal, "text/html", "UTF-8", null);
                    }
                });
    }

    private void requestRuntimePermission() {
        requestRuntimePermission(new String[]{Manifest.permission.RECEIVE_BOOT_COMPLETED, Manifest.permission.CALL_PHONE, Manifest.permission.WRITE_CONTACTS, Manifest.permission.READ_SMS}, new PermissionListener() {
            @Override
            public void onGranted() {
                //在这个里面写权限申请完毕后要做的事情。。。
                Toast.makeText(ShelvesActivity.this, "权限全部搞定了！！", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDenied(List<String> deniedPermission) {

                for (String s : deniedPermission) {
                    Toast.makeText(ShelvesActivity.this, "权限：" + s + "被拒绝了", Toast.LENGTH_SHORT).show();
                    finish();
                }
//                Toast.makeText(MainActivity.this, "权限：被拒绝了", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initWebView() {
        mWebView = findViewById(R.id.wv_test);
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);//启用js功能
        settings.setSupportZoom(true);//2跟手指头可以自由缩放WebView
        settings.setBuiltInZoomControls(true);//启用放大缩小按键，已经适配的网页会无法显示这个按键
        settings.setUseWideViewPort(true);//启用双击缩放，已经适配的网页会无法显示这个按键
        //设置自适应屏幕  缩小宽度以适合屏幕的内容
        settings.setLoadWithOverviewMode(true);
        //设置布局算法,将所有内容移动到视图宽度的一列中。
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);


        settings.setTextSize(WebSettings.TextSize.NORMAL);//设置字体默认大小
        settings.setTextZoom(300);//按照具体的设置网页字体大小


        //设置webVie监听
        mWebView.setWebViewClient(new WebViewClient() {
            @Override//开始加载网页的监听
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                Log.i("MyLog", "开始加载");
                super.onPageStarted(view, url, favicon);
            }

            @Override//加载网页完成的监听
            public void onPageFinished(WebView view, String url) {
                Log.i("MyLog", "加载结束");
                super.onPageFinished(view, url);
            }

            //当WebView被点击，或者内部的超链接被点击的时候，会执行该方法。可以拿到被点击的超链接url。根据url来做相对应的逻辑
            @Override//跳转链接的监听
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.i("MyLog", "跳转链接");
                //默认会跳转到其他游览器。这里修改成如下代码，让其不跳转到其他游览器
                mWebView.loadUrl(url);
                return true;
            }
        });

        mWebView.setWebChromeClient(new WebChromeClient() {

            @Override//获取网页标题
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
            }

            @Override//获取进度条发生变化
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
            }
        });

//        mWebView.loadUrl("http://www.itcast.cn/");
    }


    public static void i(String tag, String msg) {  //信息太长,分段打印
        //因为String的length是字符数量不是字节数量所以为了防止中文字符过多，
        //  把4*1024的MAX字节打印长度改为2001字符数
        int max_str_length = 2001 - tag.length();
        //大于4000时
        while (msg.length() > max_str_length) {
            Log.i(tag, msg.substring(0, max_str_length));
            msg = msg.substring(max_str_length);
        }
        //剩余部分
        Log.i(tag, msg);
    }

    protected void initView() {
        super.initView();
        mRootLayout = findViewById(R.id.cl_root);
//        mRecyclerView = findViewById(R.id.rv_news);
//        mRecyclerAdapter = new NewsMvpAdapter();
//        mRecyclerAdapter.setNewsResult(mNewsBeans);
//        mLayoutMgr = new LinearLayoutManager(this);
//        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
//        mRecyclerView.setLayoutManager(mLayoutMgr);
//        mRecyclerView.setAdapter(mRecyclerAdapter);


//        mPresenter = new ShelvesPresenter(this, new INetListener<ShelvesResultBean>() {
//
//            @Override
//            public void onNext(ShelvesResultBean shelvesResultBean) {
//
//            }
//
//            @Override
//            public void onError(Throwable e) {
//
//            }
//
//            @Override
//            public void onComplete() {
//
//            }
//        });
        mPresenter = new ShelvesDetailPresenter(this);
    }

    @Override
    protected AbstractShelvesPresenter getPresenter() {
        return new ShelvesPresenter(this, new INetListener<ShelvesResultBean>() {

            @Override
            public void onNext(ShelvesResultBean shelvesResultBean) {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        dispatchRefresh();
    }

    private void dispatchRefresh() {
        mPresenter.refresh(IShelvesMvpContract.REFRESH_SHELVES, new String[]{"2"});
    }


    @Override
    public void onRefreshFinished(String refreshType, List<ShelvesResultBean> resultBean) {
        //        mNewsBeans.clear();
//        mNewsBeans.addAll(newsBeans);
//        mRecyclerAdapter.notifyDataSetChanged();
        if (resultBean != null) {
            Toast.makeText(this, "成功了 --> " + resultBean.get(0).getName(), Toast.LENGTH_SHORT).show();
        }

    }

//    @Override
//    public void onRefreshFinished(String refreshType, List<ShelvesCategoriesFinalBean> resultBean) {
//        if (resultBean != null) {
//            ShelvesCategoriesFinalBean shelvesCategoriesDetailBean = resultBean.get(0);
//
//            Log.i("MyLog", "shelvesCategoriesDetailBean.getName() --> " + shelvesCategoriesDetailBean.getName());
//            Log.i("MyLog", "shelvesCategoriesDetailBean.getCategoryId() --> " + shelvesCategoriesDetailBean.getCategoryId());
//            Log.i("MyLog", "shelvesCategoriesDetailBean.getCustomerId() --> " + shelvesCategoriesDetailBean.getCustomerId());
//            Log.i("MyLog", "shelvesCategoriesDetailBean.getShelfId() --> " + shelvesCategoriesDetailBean.getShelfId());
//            Log.i("MyLog", "shelvesCategoriesDetailBean.getLogo() --> " + shelvesCategoriesDetailBean.getLogo());
//        }
//    }

    @Override
    public void showTips(String message) {
        Snackbar.make(mRootLayout, message, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.destroy();
    }
}
