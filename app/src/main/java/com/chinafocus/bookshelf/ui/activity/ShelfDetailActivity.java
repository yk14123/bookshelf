package com.chinafocus.bookshelf.ui.activity;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.RequestOptions;
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
import java.util.concurrent.ExecutionException;

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
    private boolean isHideRvCategory = true;
    private ObjectAnimator mObjectAnimator_RvCategory_hide,mObjectAnimator_RvCategory_show;
    private ObjectAnimator mObjectAnimator_IvCopyRight_hide,mObjectAnimator_IvCopyRight_show;
    private Drawable mDrawable;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler(){



        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            mDrawable = (Drawable) msg.obj;
            mRlShelfRoot.setBackground(mDrawable);

            int mDrawableIntrinsicHeight = mDrawable.getIntrinsicHeight();
            int mDrawableIntrinsicWidth = mDrawable.getIntrinsicWidth();

            Log.i("MyLog", "mDrawableIntrinsicWidth -->" + mDrawableIntrinsicWidth);
            Log.i("MyLog", "mDrawableIntrinsicHeight -->" + mDrawableIntrinsicHeight);

        }
    };

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

//        mWvIntro = findViewById(R.id.wv_shelf_detail_intro);
//        //123
//        //初始化WebView
//        mWvIntro.setVerticalScrollBarEnabled(true);//不能垂直滑动
//        mWvIntro.setHorizontalScrollBarEnabled(true);//不能水平滑动
//        //設置WebView默認的白色背景為透明色
//        mWvIntro.setBackgroundColor(0);
//
//        WebSettings webSettings = mWvIntro.getSettings(); //声明WebSettings子类
//
//        webSettings.setDomStorageEnabled(true);  //设置适应Html5的一些方法
//        //设置自适应屏幕，两者合用
//        webSettings.setUseWideViewPort(true); //将图片调整到适合WebView的大小
//        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
//        //缩放操作
//        webSettings.setSupportZoom(true); //支持缩放，默认为true。是下面那个的前提。
//        webSettings.setBuiltInZoomControls(false); //设置内置的缩放控件。若为false，则该WebView不可缩放
//        webSettings.setDisplayZoomControls(false); //隐藏原生的缩放控件
//        webSettings.setTextZoom(180);   //设置显示的字体大小
//
//
////        WebSettings settings = mWvIntro.getSettings();
////        settings.setJavaScriptEnabled(true);//启用js功能
////        settings.setSupportZoom(true);//2跟手指头可以自由缩放WebView
////        settings.setBuiltInZoomControls(true);//启用放大缩小按键，已经适配的网页会无法显示这个按键
////        settings.setUseWideViewPort(true);//启用双击缩放，已经适配的网页会无法显示这个按键
//
//        //加载本地文件
//        mWvIntro.loadUrl("file:///android_asset/bookcaseIntro.html");
//
//        mWvIntro.setWebViewClient(new WebViewClient() {
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                view.loadUrl(url);
//                return true;
//            }
//
//            @Override
//            public void onPageFinished(WebView view, String url) {
//                Log.d(TAG, "onPageFinished: url >>>" + url);
//                super.onPageFinished(view, url);
//            }
//
//            @Override
//            public void onPageStarted(WebView view, String url, Bitmap favicon) {
//                Log.d(TAG, "onPageStarted: url >>>" + url);
//                super.onPageStarted(view, url, favicon);
//            }
//
//            @Override
//            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
//                handler.proceed();  // 接受所有网站的证书
//                super.onReceivedSslError(view, handler, error);
//            }
//        });

        mIvCopyRight = findViewById(R.id.iv_shelf_detail_copyright);
        mIvCopyRight.setOnClickListener(this);
        //初始化Presenter
        mPresenter = new ShelvesDetailPresenter(this);
        //请求数据


        //这里的属性随便写什么都可以。但是需要我们手动更新绘制
        mObjectAnimator_RvCategory_hide = ObjectAnimator.ofFloat(mRvCategory, "scaleX", 1.0f, 0.0f);
//1.设置动画更新监听
        mObjectAnimator_RvCategory_hide.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
//2.从监听器里面获取动画值
                float animatedValue = (float) animation.getAnimatedValue();
                mRvCategory.setScaleX(animatedValue);//3.手动更新绘制
                mRvCategory.setScaleY(animatedValue);
            }
        });
        mObjectAnimator_RvCategory_hide.setDuration(1000);

        //这里的属性随便写什么都可以。但是需要我们手动更新绘制
        mObjectAnimator_RvCategory_show = ObjectAnimator.ofFloat(mRvCategory, "scaleX", 0.0f, 1.0f);
//1.设置动画更新监听
        mObjectAnimator_RvCategory_show.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
//2.从监听器里面获取动画值
                float animatedValue = (float) animation.getAnimatedValue();
                mRvCategory.setScaleX(animatedValue);//3.手动更新绘制
                mRvCategory.setScaleY(animatedValue);
            }
        });
        mObjectAnimator_RvCategory_show.setDuration(1000);


        //这里的属性随便写什么都可以。但是需要我们手动更新绘制
        mObjectAnimator_IvCopyRight_hide = ObjectAnimator.ofFloat(mIvCopyRight, "scaleX", 1.0f, 0.0f);
//1.设置动画更新监听
        mObjectAnimator_IvCopyRight_hide.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
//2.从监听器里面获取动画值
                float animatedValue = (float) animation.getAnimatedValue();
                mIvCopyRight.setScaleX(animatedValue);//3.手动更新绘制
                mIvCopyRight.setScaleY(animatedValue);
            }
        });
        mObjectAnimator_IvCopyRight_hide.setDuration(1000);

        //这里的属性随便写什么都可以。但是需要我们手动更新绘制
        mObjectAnimator_IvCopyRight_show = ObjectAnimator.ofFloat(mIvCopyRight, "scaleX", 0.0f, 1.0f);
//1.设置动画更新监听
        mObjectAnimator_IvCopyRight_show.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
//2.从监听器里面获取动画值
                float animatedValue = (float) animation.getAnimatedValue();
                mIvCopyRight.setScaleX(animatedValue);//3.手动更新绘制
                mIvCopyRight.setScaleY(animatedValue);
            }
        });
        mObjectAnimator_IvCopyRight_show.setDuration(1000);
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

        int intrinsicWidth = mIvLogo.getDrawable().getIntrinsicWidth();
        int intrinsicHeight = mIvLogo.getDrawable().getIntrinsicHeight();

        Log.i("MyLog", "intrinsicWidth -->" + intrinsicWidth);
        Log.i("MyLog", "intrinsicHeight -->" + intrinsicHeight);


        float xdpi = getResources().getDisplayMetrics().xdpi;
        float ydpi = getResources().getDisplayMetrics().ydpi;

        Log.i("MyLog", "xdpi -->" + xdpi);
        Log.i("MyLog", "ydpi -->" + ydpi);

        int mIvCopyRightIntrinsicWidth = mIvCopyRight.getDrawable().getIntrinsicWidth();
        int mIvCopyRightIntrinsicHeight = mIvCopyRight.getDrawable().getIntrinsicHeight();

        Log.i("MyLog", "mIvCopyRightIntrinsicWidth -->" + mIvCopyRightIntrinsicWidth);
        Log.i("MyLog", "mIvCopyRightIntrinsicHeight -->" + mIvCopyRightIntrinsicHeight);


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

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        RequestOptions requestOptions = new RequestOptions();
                        FutureTarget<Drawable> into = Glide.with(ShelfDetailActivity.this).load(bg).apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.RESOURCE)).into(1080, 1920);

                        Message message = Message.obtain();

                        try {
                            message.obj = into.get();
                            mHandler.sendMessage(message);
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

//                        .into(new SimpleTarget<Drawable>() {
//                    @Override
//                    public void onResourceReady(@NonNull Drawable resource,
//                                                @Nullable Transition<? super Drawable> transition) {
//                        mRlShelfRoot.setBackground(resource);
//                    }
//                });

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
//        if (mRvCategory.getVisibility() == View.VISIBLE) {
        if (isHideRvCategory) {
            mObjectAnimator_RvCategory_hide.start();
            mObjectAnimator_IvCopyRight_hide.start();
//            //1.设置每个属性动画效果
//            PropertyValuesHolder scaleX = PropertyValuesHolder.ofFloat("scaleX", 1, 0);
//            PropertyValuesHolder scaleY = PropertyValuesHolder.ofFloat("scaleY", 1, 0);
////2.在ofPropertyValuesHolder()方法里面把动画添加进去即可
//            ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(mRvCategory, scaleX, scaleY);
//            objectAnimator.setInterpolator(new LinearInterpolator());
//            objectAnimator.setDuration(1000);
//            objectAnimator.start();


//            mObjectAnimator_IvCopyRight.start();



//            mRvCategory.animate().scaleX(0.0f).scaleY(0.0f).setDuration(1000).setListener(new AnimatorListenerAdapter() {
//                @Override
//                public void onAnimationEnd(Animator animation) {
//                    super.onAnimationEnd(animation);
//                    mRvCategory.setVisibility(View.GONE);
//                }
//            });

//            mIvCopyRight.animate().scaleX(0.0f).scaleY(0.0f).setDuration(1000).setListener(new AnimatorListenerAdapter() {
//                @Override
//                public void onAnimationEnd(Animator animation) {
//                    super.onAnimationEnd(animation);
//                    mIvCopyRight.setVisibility(View.GONE);
//                }
//            });

            //隐藏九宫格,显示书柜简介
//            Animation scaleOut = AnimationUtils.loadAnimation(mContext, R.anim.bookshelf_view_scale_out);
//            scaleOut.setFillAfter(true);
//            mRvCategory.startAnimation(scaleOut);
            isHideRvCategory = false;
//            scaleOut.setAnimationListener(new Animation.AnimationListener() {
//                @Override
//                public void onAnimationStart(Animation animation) {
//                }
//
//                @Override
//                public void onAnimationEnd(Animation animation) {
//                    mRvCategory.setVisibility(View.GONE);
////                    mScrollViewIntro.setVisibility(View.VISIBLE);
////                    mWvIntro.setVisibility(View.VISIBLE);
////
////                    Animation translateIn = AnimationUtils.loadAnimation(mContext, R.anim.bookshelf_view_translate_in);
//////                    mScrollViewIntro.startAnimation(translateIn);
////                    mWvIntro.startAnimation(translateIn);
////                    translateIn.setAnimationListener(new Animation.AnimationListener() {
////                        @Override
////                        public void onAnimationStart(Animation animation) {
////
////                        }
////
////                        @Override
////                        public void onAnimationEnd(Animation animation) {
////
////                        }
////
////                        @Override
////                        public void onAnimationRepeat(Animation animation) {
////
////                        }
////                    });
//                }
//
//                @Override
//                public void onAnimationRepeat(Animation animation) {
//                }
//            });
        } else {

            mObjectAnimator_RvCategory_show.start();
            mObjectAnimator_IvCopyRight_show.start();
//            //1.设置每个属性动画效果
//            PropertyValuesHolder scaleX = PropertyValuesHolder.ofFloat("scaleX", 0, 1);
//            PropertyValuesHolder scaleY = PropertyValuesHolder.ofFloat("scaleY", 0, 1);
////2.在ofPropertyValuesHolder()方法里面把动画添加进去即可
//            ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(mRvCategory, scaleX, scaleY);
//            objectAnimator.setInterpolator(new LinearInterpolator());
//            objectAnimator.setDuration(1000);
//            objectAnimator.start();

//            mRvCategory.animate().scaleX(1.0f).scaleY(1.0f).setDuration(1000).setListener(new AnimatorListenerAdapter() {
//                @Override
//                public void onAnimationStart(Animator animation) {
//                    super.onAnimationStart(animation);
//                    mRvCategory.setVisibility(View.VISIBLE);
//                }
//            });

//            mIvCopyRight.animate().scaleX(1.0f).scaleY(1.0f).setDuration(1000).setListener(new AnimatorListenerAdapter() {
//                @Override
//                public void onAnimationStart(Animator animation) {
//                    super.onAnimationStart(animation);
//                    mIvCopyRight.setVisibility(View.VISIBLE);
//                }
//            });
//            Animation scaleIn = AnimationUtils.loadAnimation(mContext, R.anim.bookshelf_view_scale_in);
//            scaleIn.setFillAfter(true);
//            mRvCategory.startAnimation(scaleIn);
            isHideRvCategory = true;
//            mRvCategory.setLayoutAnimationListener(new Animation.AnimationListener() {
//                @Override
//                public void onAnimationStart(Animation animation) {
//
//                }
//
//                @Override
//                public void onAnimationEnd(Animation animation) {
//                    mRvCategory.setVisibility(View.VISIBLE);
//                }
//
//                @Override
//                public void onAnimationRepeat(Animation animation) {
//
//                }
//            });
//            //隱藏WebView,显示九宫格
//            Animation translateOut = AnimationUtils.loadAnimation(mContext, R.anim.bookshelf_view_translate_out);
////            mScrollViewIntro.startAnimation(translateOut);
//            mWvIntro.startAnimation(translateOut);
//            translateOut.setAnimationListener(new Animation.AnimationListener() {
//                @Override
//                public void onAnimationStart(Animation animation) {
//                }
//
//                @Override
//                public void onAnimationEnd(Animation animation) {
////                    mScrollViewIntro.setVisibility(View.GONE);
//                    mWvIntro.setVisibility(View.GONE);
//                    mRvCategory.setVisibility(View.VISIBLE);
//                    Animation scaleIn = AnimationUtils.loadAnimation(mContext, R.anim.bookshelf_view_scale_in);
//                    mRvCategory.startAnimation(scaleIn);
//                    scaleIn.setAnimationListener(new Animation.AnimationListener() {
//                        @Override
//                        public void onAnimationStart(Animation animation) {
//                        }
//
//                        @Override
//                        public void onAnimationEnd(Animation animation) {
//
//                        }
//
//                        @Override
//                        public void onAnimationRepeat(Animation animation) {
//                        }
//                    });
//
//                }
//
//                @Override
//                public void onAnimationRepeat(Animation animation) {
//
//                }
//            });

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
//        mRvCategory.clearAnimation();
//        mScrollViewIntro.clearAnimation();
//        mWvIntro.clearAnimation();
        super.onDestroy();
    }
}
