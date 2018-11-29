package com.chinafocus.bookshelf.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.http.SslError;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.chinafocus.bookshelf.R;
import com.chinafocus.bookshelf.base.BaseActivity;
import com.chinafocus.bookshelf.global.BookShelfConstant;
import com.chinafocus.bookshelf.model.bean.ShelvesCategoryResultBean;
import com.chinafocus.bookshelf.presenter.shelves.IShelvesMvpContract;
import com.chinafocus.bookshelf.presenter.shelves.ShelvesDetailPresenter;
import com.chinafocus.bookshelf.ui.adapter.ShelfCategoryAdapter;
import com.chinafocus.bookshelf.ui.fragment.BookShelfCopyrightDialog;
import com.chinafocus.bookshelf.utils.LogUtil;
import com.chinafocus.bookshelf.utils.ManifestUtils;
import com.zhy.android.percent.support.PercentRelativeLayout;

import java.util.List;

/**
 * 书柜九宮格页面-->习近平的书柜
 *
 * @author HUANG_JIN
 * @version 1.0
 * create at 2018/11/26
 */
public class ShelfDetailActivity extends BaseActivity<ShelvesCategoryResultBean> implements View.OnClickListener {
    private static final String TAG = "ShelfDetailActivity";
    private Context mContext;
    //連接器
    private IShelvesMvpContract.IPresenter mPresenter;
    //root
    private PercentRelativeLayout mRlShelfRoot;
    //logo
    private ImageView mIvLogo;
    //分类列表
    private RecyclerView mRvCategory;
    //滑动简介
    private ScrollView mScrollViewIntro;
    //内容content
    private WebView mWvIntro;
    //适配器
    private ShelfCategoryAdapter mShelfCategoryAdapter;
    //copyright
    private ImageView mIvCopyRight;
    private BookShelfCopyrightDialog mCopyrightDialog;

    @Override
    protected void initView() {
        setContentView(R.layout.bookshelf_activity_detail);
        mContext = this;
        //设置当前的版本号
        //versionInfo
        TextView mTvVersionInfo = findViewById(R.id.tv_shelf_detail_version);
        String appVersion = ManifestUtils.getVersionName(this);
        mTvVersionInfo.setText(appVersion);
        //root的点击事件
        mRlShelfRoot = findViewById(R.id.rl_shelf_detail_root);

        mIvLogo = findViewById(R.id.iv_shelf_detail_logo);
        mIvLogo.setOnClickListener(this);
        mRvCategory = findViewById(R.id.rv_shelf_detail_category);
        //初始化RecyclerView
        GridLayoutManager manager = new GridLayoutManager(mContext, 3);
        mRvCategory.setLayoutManager(manager);
        mRvCategory.setHasFixedSize(true);
        mRvCategory.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect,
                                       @NonNull View view,
                                       @NonNull RecyclerView parent,
                                       @NonNull RecyclerView.State state) {
                //不是第一个的格子都设一个左边和底部的间距
                outRect.left = 10;
                outRect.bottom = 10; //由于每行都只有3个，所以第一个都是3的倍数，把左边距设为0
                if (parent.getChildLayoutPosition(view) % 3 == 0) {
                    outRect.left = 0;
                }
            }
        });
        mScrollViewIntro = findViewById(R.id.nev_shelf_detail_intro);
        mWvIntro = findViewById(R.id.wv_shelf_detail_intro);
        //初始化WebView
        mWvIntro.setVerticalScrollBarEnabled(false);//不能垂直滑动
        mWvIntro.setHorizontalScrollBarEnabled(false);//不能水平滑动
        //設置WebView默認的白色背景為透明色
        mWvIntro.setBackgroundColor(0);

        WebSettings webSettings = mWvIntro.getSettings(); //声明WebSettings子类

        webSettings.setDomStorageEnabled(true);  //设置适应Html5的一些方法
        //设置自适应屏幕，两者合用
        webSettings.setUseWideViewPort(true); //将图片调整到适合WebView的大小
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
        //缩放操作
        webSettings.setSupportZoom(false); //支持缩放，默认为true。是下面那个的前提。
        webSettings.setBuiltInZoomControls(false); //设置内置的缩放控件。若为false，则该WebView不可缩放
        webSettings.setDisplayZoomControls(false); //隐藏原生的缩放控件
        webSettings.setTextZoom(180);   //设置显示的字体大小
        //加载本地文件
        mWvIntro.loadUrl("file:///android_asset/bookcaseIntro.html");
        mWvIntro.setWebViewClient(new WebViewClient() {
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
                handler.proceed();  // 接受所有网站的证书
                super.onReceivedSslError(view, handler, error);
            }
        });

        mIvCopyRight = findViewById(R.id.iv_shelf_detail_copyright);
        mIvCopyRight.setOnClickListener(this);
        //初始化Presenter
        mPresenter = new ShelvesDetailPresenter(this);
        //请求数据

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        //1.通过WindowManager拿屏幕宽高
        WindowManager mWM = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
//2.获取屏幕宽高
        int mScreenHeight = mWM.getDefaultDisplay().getHeight();
        int mScreenWidth = mWM.getDefaultDisplay().getWidth();

        Log.i("MyLog", "mScreenHeight -->" + mScreenHeight);
        Log.i("MyLog", "mScreenWidth -->" + mScreenWidth);
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestShelfDetail();
    }

    private void requestShelfDetail() {
        mPresenter.refresh(IShelvesMvpContract.REFRESH_SHELVES_DETAIL, new String[]{"2"});
    }

    @Override
    public void onRefreshFinished(String refreshType, List<ShelvesCategoryResultBean> result) {
        ShelvesCategoryResultBean resultBean = result.get(0);
        if (resultBean != null) {
            //背景
            String bg = resultBean.getBg();
            if (!TextUtils.isEmpty(bg)) {
                LogUtil.veryLongForI(TAG, "resultBean bg>>> " + bg);
                Glide.with(this).load(bg).into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource,
                                                @Nullable Transition<? super Drawable> transition) {
                        mRlShelfRoot.setBackground(resource);
                    }
                });
            }
            if (mShelfCategoryAdapter == null) {
                mShelfCategoryAdapter = new ShelfCategoryAdapter(this, resultBean.getCategories());
                mShelfCategoryAdapter.setShelfCategoryListener((shelfId, categoryId, categoryName) -> {
                    //跳转书架页面
                    Intent intent = new Intent(mContext, BookCategoryDetailActivity.class);
                    intent.putExtra(BookShelfConstant.SHELF_ID, shelfId);
                    intent.putExtra(BookShelfConstant.CATEGORY_ID, categoryId);
                    intent.putExtra(BookShelfConstant.CATEGORY_NAME, categoryName);
                    startActivity(intent);
                });
                mRvCategory.setAdapter(mShelfCategoryAdapter);
            } else {
                mShelfCategoryAdapter.setCategoryEntity(resultBean.getCategories());
            }
        }
    }

    @Override
    public void showTips(String message) {

    }

    @Override
    public void onClick(View v) {
        if (v == mIvLogo) {
            //点击logo弹出书柜简介
            startViewAnimation();
        } else if (v == mIvCopyRight) {
            //点击copyRight弹出版权信息对话框
            if (mCopyrightDialog == null) {
                mCopyrightDialog = new BookShelfCopyrightDialog(mContext);
            }
            if (!mCopyrightDialog.isShowing()) {
                mCopyrightDialog.show();
            }
        }
    }

    /**
     * 设置View的进场动画
     */
    private void startViewAnimation() {
        if (mRvCategory.getVisibility() == View.VISIBLE) {
            //隐藏九宫格,显示书柜简介
            Animation scaleOut = AnimationUtils.loadAnimation(mContext, R.anim.bookshelf_view_scale_out);
            mRvCategory.startAnimation(scaleOut);
            scaleOut.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    mRvCategory.setVisibility(View.GONE);
                    mScrollViewIntro.setVisibility(View.VISIBLE);
                    Animation translateIn = AnimationUtils.loadAnimation(mContext, R.anim.bookshelf_view_translate_in);
                    mScrollViewIntro.startAnimation(translateIn);
                    translateIn.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
        } else {
            //隱藏WebView,显示九宫格
            Animation translateOut = AnimationUtils.loadAnimation(mContext, R.anim.bookshelf_view_translate_out);
            mScrollViewIntro.startAnimation(translateOut);
            translateOut.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    mScrollViewIntro.setVisibility(View.GONE);
                    mRvCategory.setVisibility(View.VISIBLE);
                    Animation scaleIn = AnimationUtils.loadAnimation(mContext, R.anim.bookshelf_view_scale_in);
                    mRvCategory.startAnimation(scaleIn);
                    scaleIn.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {
                        }
                    });

                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });

        }
    }


    @Override
    protected void onDestroy() {
        //防止WebView导致的内存泄露
        if (mWvIntro != null) {
            // 如果先调用destroy()方法，则会命中
            // if (isDestroyed()) return;这一行代码，需要先onDetachedFromWindow()，再destroy()
            ViewParent parent = mWvIntro.getParent();
            if (parent != null) {
                ((ViewGroup) parent).removeView(mWvIntro);
            }
            mWvIntro.stopLoading();
            // 退出时调用此方法，移除绑定的服务，否则某些特定系统会报错
            mWvIntro.getSettings().setJavaScriptEnabled(false);
            mWvIntro.clearHistory();
//            mWvIntro.clearView();
            mWvIntro.removeAllViews();
            mWvIntro.destroy();
        }

        //去除View动画
        mRvCategory.clearAnimation();
        mScrollViewIntro.clearAnimation();
        super.onDestroy();
    }
}
