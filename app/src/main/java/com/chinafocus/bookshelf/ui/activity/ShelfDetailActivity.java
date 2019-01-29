package com.chinafocus.bookshelf.ui.activity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.chinafocus.bookshelf.R;
import com.chinafocus.bookshelf.bean.ShelvesCategoryRawBean;
import com.chinafocus.bookshelf.bean.ShelvesRawBean;
import com.chinafocus.bookshelf.global.BookShelfConstant;
import com.chinafocus.bookshelf.model.base.activity.BaseActivity;
import com.chinafocus.bookshelf.model.base.lru.LruMap;
import com.chinafocus.bookshelf.model.base.network.ApiManager;
import com.chinafocus.bookshelf.model.base.statusbar.StatusBarCompat;
import com.chinafocus.bookshelf.presenter.shelves.IShelvesMvpContract;
import com.chinafocus.bookshelf.presenter.shelves.ShelvesDetailPresenter;
import com.chinafocus.bookshelf.presenter.statistics.StatisticsPresenter;
import com.chinafocus.bookshelf.ui.adapter.ShelfCategoryAdapter;
import com.chinafocus.bookshelf.ui.adapter.ShelfIntroAdapter;
import com.chinafocus.bookshelf.utils.ManifestUtils;
import com.chinafocus.bookshelf.utils.ScreenUtils;
import com.chinafocus.bookshelf.utils.SpUtil;
import com.chinafocus.bookshelf.utils.UIHelper;
import com.google.gson.Gson;
import com.jakewharton.rxbinding2.view.RxView;
import com.zhy.android.percent.support.PercentFrameLayout;
import com.zhy.android.percent.support.PercentRelativeLayout;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * 书柜九宮格页面-->习近平的书柜
 *
 * @author HUANG_JIN
 * @version 1.0
 * create at 2018/11/26
 */
public class ShelfDetailActivity extends BaseActivity<ShelvesCategoryRawBean.ShelvesCategoryResultBean> {
    private static final String TAG = "ShelfDetailActivity";
    //連接器
    private IShelvesMvpContract.IPresenter mPresenter;
    //root
    private PercentRelativeLayout mRlShelfRoot;
    //logo
    private ImageView mIvLogo;
    //分类列表
    private RecyclerView mRvCategory;
    private RecyclerView mRvShelfIntro;
    //适配器
    private ShelfCategoryAdapter mShelfCategoryAdapter;
    //copyright
    private ImageView mIvCopyright;
    private AlertDialog mCopyrightDialog;
    private boolean isHideRvCategory = true;
    private Animation mCategory_Scale_out;
    private Animation mCategory_Scale_in;
    private Animation mShelfIntro_Translate_in;
    private Animation mShelfIntro_Translate_out;
    private Disposable mIvLogo_clicks, mIvCopyright_clicks;
    private ScrollView mSvCopyrightContent;
    private TextView mTvCopyrightContent;

    private TextView mTvVersionInfo;
    private AlertDialog mExitDialog;
    private View mLocation_exit_view;
    private EditText mExit_id;

    private PercentFrameLayout mLlErrorLayout;
    private Disposable mErrorRetry_clicks;
    private TextView mTvErrorExit;

    private boolean mIsNetWorkErro = true;
    private String mAppVersion;
    private String mExitPass;
    private String mOriginId;

    //书柜id
    private int mShelfId;
    private ImageView mCloseIcon;
    private RelativeLayout mRlCopyright_close;
    private ImageView mCopyrightHeader;

    @SuppressLint("CheckResult")
    @Override
    protected void initView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.setStatusBarColor(Color.TRANSPARENT);
        }

        getExtraFromIntent();

        setContentView(R.layout.bookshelf_activity_detail);

//        mOriginId = SpUtil.getString(getApplicationContext(), BookShelfConstant.BOOK_INIT_LOCATION_ID);


        //设置当前的版本号
        initControlExitLogo();
        //root
        mRlShelfRoot = findViewById(R.id.rl_shelf_detail_root);

        //网络错误重试
        initErrorRetry();

        initBack();

        //习近平的书柜
        initLogo();
        //书柜分类九宫格
        initRvCategory();
        //书柜简介
        initRvShelfIntro();
        //易阅通
        initCopyright();
        //替换易阅通关闭图标
        initCloseTag();

        //初始化动画
        initAnim();


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
        mTvCategoryName.setVisibility(View.INVISIBLE);
        //右側menu
        ImageView mIvRightMenu = findViewById(R.id.iv_bookshelf_right_menu);
        mIvRightMenu.setVisibility(View.INVISIBLE);
    }

    private void initBack() {
        ImageView imageView = findViewById(R.id.iv_shelf_detail_back);
        imageView.setOnClickListener(v -> finish());
    }

    private void initCloseTag() {
        mCloseIcon = findViewById(R.id.iv_shelf_detail_copyright_close);

        mCloseIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startViewAnimation();
            }
        });

    }

    private void getExtraFromIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            mOriginId = intent.getStringExtra(BookShelfConstant.BOOK_INIT_LOCATION_ID);
            String client_uuid = intent.getStringExtra(BookShelfConstant.BOOK_CLIENT_UUID);

            SpUtil.setString(getApplicationContext(), BookShelfConstant.BOOK_INIT_LOCATION_ID, mOriginId);
            SpUtil.setString(getApplicationContext(), BookShelfConstant.BOOK_CLIENT_UUID, client_uuid);

            loadMoreShelfDetail();
        }
    }

    private void loadMoreShelfDetail() {
        ApiManager.getInstance().getNewService().getShelves(mOriginId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String s) {
                        Gson gson = new Gson();
                        ShelvesRawBean shelvesRawBean = gson.fromJson(s, ShelvesRawBean.class);
                        mShelfId = shelvesRawBean.getData().get(0).getShelfId();

                        LruMap.getInstance().put(BookShelfConstant.SHELF_ID_TEMP, mShelfId);

                        StatisticsPresenter.getInstance().startStatistics(getApplicationContext(), "1", String.valueOf(mShelfId));
                        loadShelfDetail();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Object tempId = LruMap.getInstance().get(BookShelfConstant.SHELF_ID_TEMP);
                        if (tempId == null) {
                            Toast.makeText(ShelfDetailActivity.this, "客户代码错误或者网络错误", Toast.LENGTH_SHORT).show();
                            showRefreshLayout(true);
                        } else {
                            loadShelfDetail();
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    private void initErrorRetry() {
        mTvErrorExit = findViewById(R.id.tv_shelf_detail_version_no_net);

        mTvErrorExit.setText(mAppVersion);

        mTvErrorExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onErrorExit();
            }
        });
        //无数据视图
        mLlErrorLayout = findViewById(R.id.rl_shelf_detail_error_bg);

        mErrorRetry_clicks = RxView.clicks(mLlErrorLayout).throttleFirst(1, TimeUnit.SECONDS).subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object o) throws Exception {
                //mLlErrorLayout.setVisibility(View.GONE);
//                loadShelfDetail();
                loadMoreShelfDetail();
            }
        });
    }

    private void onErrorExit() {

        mExit_id.setText("");

        mExitPass = mOriginId + "logout";

        if (mExitDialog == null) {
            mExitDialog = new AlertDialog.Builder(ShelfDetailActivity.this)
                    .setView(mLocation_exit_view)
                    .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (!mIsNetWorkErro) {
                                mCountShowExit++;
                            }
                            String s = mExit_id.getText().toString();

                            if (s.equals(mExitPass)) {

                                System.exit(0);
                            } else {

                                Toast.makeText(getApplicationContext(), "请再重新输入", Toast.LENGTH_SHORT).show();

                            }
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mExitDialog.dismiss();
                        }
                    })
                    .create();
            //設置点击外部不可以消失
            mExitDialog.setCanceledOnTouchOutside(false);
            //设置不可以点击消失
            mExitDialog.setCancelable(false);

        }

        if (!mExitDialog.isShowing()) {
            mExitDialog.show();
        }
    }

    /**
     * 请求九宫格分类API接口数据
     */
    private void loadShelfDetail() {
        showLoading();
        //初始化控制器
        if (mPresenter == null) {
            mPresenter = new ShelvesDetailPresenter(this);

        }
        mShelfId = (int) LruMap.getInstance().get(BookShelfConstant.SHELF_ID_TEMP);

        Log.i("ShelfDetailActivity", "loadShelfDetail -- >mShelfId-->" + mShelfId + "mOriginId-->" + mOriginId);
        mPresenter.refresh(IShelvesMvpContract.REFRESH_SHELVES_DETAIL, new String[]{String.valueOf(mShelfId), mOriginId});

    }


    private int mCountShowExit;

    private void initControlExitLogo() {
        mTvVersionInfo = findViewById(R.id.tv_shelf_detail_version);
        mAppVersion = ManifestUtils.getVersionName(this);
        mTvVersionInfo.setText(mAppVersion);
//        mTvVersionInfo.setVisibility(View.VISIBLE);

        //初始化退出dialog自定义View
        initExitDialogView();

        mTvVersionInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mCountShowExit < 3) {
                    onErrorExit();

                }
            }
        });
    }

    private void initExitDialogView() {
        mLocation_exit_view = View.inflate(getApplicationContext(), R.layout.bookshelf_dialog_init_location_exit, null);

        TextView location_id = mLocation_exit_view.findViewById(R.id.tv_shelf_detail_exit_location);
        mExit_id = mLocation_exit_view.findViewById(R.id.et_shelf_detail_exit);

        location_id.setText("您的代码是：" + mOriginId);

    }

    private void initCopyright() {
        //初始化CopyrightDialog
        initCopyrightDialog();
        mIvCopyright = findViewById(R.id.iv_shelf_detail_copyright);

        mIvCopyright_clicks = RxView.clicks(mIvCopyright).throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        mSvCopyrightContent.post(new Runnable() {
                            @Override
                            public void run() {
                                mSvCopyrightContent.scrollTo(0, 0);
                            }
                        });
                        //点击copyRight弹出版权信息对话框
                        if (!mCopyrightDialog.isShowing()) {
                            mCopyrightDialog.show();

                            WindowManager.LayoutParams params = mCopyrightDialog.getWindow().getAttributes();
                            params.width = (int) (ShelfDetailActivity.this.getWindowManager().getDefaultDisplay().getWidth() * 0.695);
//                            params.height = (int) (ShelfDetailActivity.this.getWindowManager().getDefaultDisplay().getWidth() * 0.695 * 1.512);
                            mCopyrightDialog.getWindow().setAttributes(params);
                        }
                    }
                });
    }


    private void initCopyrightDialog() {
        if (mCopyrightDialog == null) {
            View mContentView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.bookshelf_dialog_shelf_copyright, null);

            mSvCopyrightContent = mContentView.findViewById(R.id.sv_shelf_detail_copyright);
            mTvCopyrightContent = mContentView.findViewById(R.id.tv_shelf_detail_copyright_content);

            mCopyrightHeader = mContentView.findViewById(R.id.iv_book_shelf_detail_copyright_header);

            mRlCopyright_close = mContentView.findViewById(R.id.rl_shelf_dialog_copyright_close);
            mRlCopyright_close.setOnClickListener(v -> mCopyrightDialog.dismiss());

            String string = getString(R.string.bookshelf_dialog_copyright_info);
            setCopyrightDialogContent(string);

            mCopyrightDialog = new AlertDialog.Builder(ShelfDetailActivity.this)
                    .setView(mContentView)
                    .create();

            //設置点击外部可以消失
            mCopyrightDialog.setCanceledOnTouchOutside(true);
            //设置可以点击消失
            mCopyrightDialog.setCancelable(true);

            Window window = mCopyrightDialog.getWindow();
            if (window != null) {
                window.setBackgroundDrawableResource(android.R.color.transparent);
                WindowManager.LayoutParams lp = window.getAttributes();
                window.setGravity(Gravity.CENTER);

//                lp.y = (int) (ScreenUtils.getScreenHeight(getApplicationContext()) * 0.104);
//
//                window.setAttributes(lp);
            }
        }
    }

    /**
     * 设置CopyrightDialogContent
     * 竖屏设备：网址变色，且不能点击
     * 手机设备：网址变色，能够点击（未实现）
     *
     * @param string
     */
    private void setCopyrightDialogContent(String string) {
        SpannableString spannableString = new SpannableString(string);
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#3481C7"));
        spannableString.setSpan(colorSpan, 4, 19, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        //新增点击跳转
        mTvCopyrightContent.setMovementMethod(LinkMovementMethod.getInstance());
        spannableString.setSpan(new MyClickableSpan(), 4, 19, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        mTvCopyrightContent.setText(spannableString);
    }

    private class MyClickableSpan extends ClickableSpan {

        @Override
        public void onClick(View widget) {
            startActivity(new Intent(getApplicationContext(), BookShelfCopyrightActivity.class));

            //由第三方游览器接管跳转url
//            Uri uri = Uri.parse("http://www.cnpereading.com/");
//            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//            startActivity(intent);
        }

        @Override
        public void updateDrawState(TextPaint ds) {
//            super.updateDrawState(ds);
//            ds.setColor(Color.parseColor("#3481C7"));
            ds.setUnderlineText(false);//去除超链接的下划线
        }
    }

    private void initLogo() {
        mIvLogo = findViewById(R.id.iv_shelf_detail_logo);
        //点击logo弹出书柜简介
        mIvLogo_clicks = RxView.clicks(mIvLogo).throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        startViewAnimation();
                    }
                });

    }

    private void initAnim() {
        mCategory_Scale_out = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bookshelf_view_scale_out);
        mCategory_Scale_in = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bookshelf_view_scale_in);
        mShelfIntro_Translate_in = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bookshelf_view_translate_in);
        mShelfIntro_Translate_out = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bookshelf_view_translate_out);
    }

    private void initRvCategory() {
        mRvCategory = findViewById(R.id.rv_shelf_detail_category);
        GridLayoutManager manager = new GridLayoutManager(getApplicationContext(), 3);
        mRvCategory.setLayoutManager(manager);

//        mRvCategory.setPadding(0, 0, 0, getNavigationBarHeight());
    }


    private void initRvShelfIntro() {
        mRvShelfIntro = findViewById(R.id.rv_shelf_detail_intro);
        LinearLayoutManager linearManager = new LinearLayoutManager(
                this, LinearLayoutManager.VERTICAL, false);
        mRvShelfIntro.setHasFixedSize(true);
        mRvShelfIntro.setLayoutManager(linearManager);
        ShelfIntroAdapter introAdapter = new ShelfIntroAdapter(this);
        mRvShelfIntro.setAdapter(introAdapter);

    }


    @SuppressLint("CheckResult")
    @Override
    public void onRefreshFinished(String refreshType, List<ShelvesCategoryRawBean.ShelvesCategoryResultBean> result) {
        if (result != null) {
            showRefreshLayout(false);

            SpUtil.setBoolean(getApplicationContext(), BookShelfConstant.HAS_SHELF_DETAIL_CACHE, true);

            /**
             * 设置ShelfDetailActivityBg来源
             * 竖屏设备：九宫格背景图片，来自本地
             * 手机设备：如九宫格背景图片需要来自网络，则启用（已实现）
             */
            ShelvesCategoryRawBean.ShelvesCategoryResultBean resultBean = result.get(0);
            String bg = resultBean.getBg();
            String logoUrl = resultBean.getLogo();

            try {
                Glide.with(ShelfDetailActivity.this)
                        .load(logoUrl)
                        .into(mIvLogo);

                Glide.with(ShelfDetailActivity.this)
                        .load(R.drawable.bookshelf_detail_copyright)
                        .into(mIvCopyright);

                RequestOptions requestOptions = new RequestOptions();
                requestOptions.centerCrop().diskCacheStrategy(DiskCacheStrategy.RESOURCE);
                Glide.with(ShelfDetailActivity.this).load(bg).apply(requestOptions)
                        .into(new SimpleTarget<Drawable>(ScreenUtils.getScreenWidth(ShelfDetailActivity.this), ScreenUtils.getScreenHeight(ShelfDetailActivity.this)) {
                            @Override
                            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                mRlShelfRoot.setBackground(resource);
                            }
                        });
            } catch (Exception e) {

            }


            Log.i("ShelfDetailActivity", "ShelfDetailActivity success!");

            if (mShelfCategoryAdapter == null) {
                mShelfCategoryAdapter = new ShelfCategoryAdapter(ShelfDetailActivity.this, result, mShelfId);
                mShelfCategoryAdapter.setShelfCategoryListener((shelfId, categoryId, categoryName) -> {

//                    StatisticsType.postStatisticsNow(getApplicationContext(), "2", String.valueOf(categoryId));
                    StatisticsPresenter.getInstance().startStatistics(getApplicationContext(), "2", String.valueOf(categoryId));
                    //跳转书架页面
                    UIHelper.startBookCategoryDetailActivity(ShelfDetailActivity.this, shelfId, categoryId, categoryName);

                });
                mRvCategory.setAdapter(mShelfCategoryAdapter);
            } else {
                mShelfCategoryAdapter.setCategoryEntity(result);
            }
        } else {
            showRefreshLayout(true);
        }
        dismissLoading();
    }

    @Override
    public void showTips(String message) {
        dismissLoading();
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        showRefreshLayout(true);
    }

    private void showRefreshLayout(boolean showRefresh) {
        mIsNetWorkErro = showRefresh;
        mLlErrorLayout.setVisibility(showRefresh ? View.VISIBLE : View.GONE);
        mRlShelfRoot.setVisibility(showRefresh ? View.GONE : View.VISIBLE);

        if (mLlErrorLayout.getVisibility()==View.VISIBLE){
            StatusBarCompat.setStatusBarColor(this,Color.TRANSPARENT,true);
            initNavMenu();
        }else {
            StatusBarCompat.setStatusBarColor(this,Color.TRANSPARENT,false);
        }
    }

    /**
     * 设置View的进场动画
     */
    private void startViewAnimation() {
        if (isHideRvCategory) {
            mRvShelfIntro.scrollToPosition(0);
            Log.d(TAG, "startViewAnimation: RecyclerView is visible >>> ");
            //隐藏九宫格,显示书柜简介
            mRvCategory.startAnimation(mCategory_Scale_out);
            mCategory_Scale_out.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    mRvCategory.setVisibility(View.GONE);
                    mRvShelfIntro.startAnimation(mShelfIntro_Translate_in);

                    mCloseIcon.setVisibility(View.VISIBLE);
                    mIvCopyright.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
            mShelfIntro_Translate_in.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    mRvShelfIntro.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationEnd(Animation animation) {

                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });

            isHideRvCategory = false;

        } else {
            Log.d(TAG, "startViewAnimation: RecyclerView is gone >>> ");
            //隱藏WebView,显示九宫格
            mRvShelfIntro.startAnimation(mShelfIntro_Translate_out);
            mShelfIntro_Translate_out.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    mRvShelfIntro.setVisibility(View.GONE);
                    mRvCategory.startAnimation(mCategory_Scale_in);

                    mCloseIcon.setVisibility(View.GONE);
                    mIvCopyright.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
            mCategory_Scale_in.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    mRvCategory.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationEnd(Animation animation) {

                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            isHideRvCategory = true;
        }
    }

    @Override
    protected void onDestroy() {
        //去除View动画
        mRvCategory.clearAnimation();
        mRvShelfIntro.clearAnimation();
        if (mIvLogo_clicks != null && mIvLogo_clicks.isDisposed()) {
            mIvLogo_clicks.dispose();
        }
        if (mIvCopyright_clicks != null && mIvCopyright_clicks.isDisposed()) {
            mIvCopyright_clicks.dispose();
        }
        if (mErrorRetry_clicks != null && mErrorRetry_clicks.isDisposed()) {
            mErrorRetry_clicks.dispose();
        }

        if (mPresenter != null) {
            mPresenter.destroy();
        }
        super.onDestroy();
    }
}
