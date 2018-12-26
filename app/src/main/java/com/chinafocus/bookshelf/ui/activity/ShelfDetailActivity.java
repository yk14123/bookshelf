package com.chinafocus.bookshelf.ui.activity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.chinafocus.bookshelf.R;
import com.chinafocus.bookshelf.bean.ShelvesCategoryResultBean;
import com.chinafocus.bookshelf.global.BookShelfConstant;
import com.chinafocus.bookshelf.model.base.activity.BaseActivity;
import com.chinafocus.bookshelf.presenter.shelves.IShelvesMvpContract;
import com.chinafocus.bookshelf.presenter.shelves.ShelvesDetailPresenter;
import com.chinafocus.bookshelf.presenter.statistics.StatisticsPresenter;
import com.chinafocus.bookshelf.ui.adapter.ShelfCategoryAdapter;
import com.chinafocus.bookshelf.ui.adapter.ShelfIntroAdapter;
import com.chinafocus.bookshelf.utils.ManifestUtils;
import com.chinafocus.bookshelf.utils.SpUtil;
import com.chinafocus.bookshelf.utils.UIHelper;
import com.jakewharton.rxbinding2.view.RxView;
import com.zhy.android.percent.support.PercentFrameLayout;
import com.zhy.android.percent.support.PercentRelativeLayout;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * 书柜九宮格页面-->习近平的书柜
 *
 * @author HUANG_JIN
 * @version 1.0
 * create at 2018/11/26
 */
public class ShelfDetailActivity extends BaseActivity<ShelvesCategoryResultBean> {
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

    private boolean mIsNetWorkErro;
    private String mAppVersion;

    @SuppressLint("CheckResult")
    @Override
    protected void initView() {
        setContentView(R.layout.bookshelf_activity_detail);
        //设置当前的版本号
        initControlExitLogo();
        //root
        mRlShelfRoot = findViewById(R.id.rl_shelf_detail_root);

        //网络错误重试
        initErrorRetry();

        //习近平的书柜
        initLogo();
        //书柜分类九宫格
        initRvCategory();
        //书柜简介
        initRvShelfIntro();
        //易阅通
        initCopyright();

        //初始化动画
        initAnim();

        //初始化Presenter请求数据
        loadShelfDetail();

    }

    private void initErrorRetry() {
        mTvErrorExit = findViewById(R.id.tv_shelf_detail_version_no_net);

        mTvErrorExit.setText(mAppVersion);

        mTvErrorExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mExit_id.setText("");
                String string = SpUtil.getString(getApplicationContext(), BookShelfConstant.BOOK_INIT_LOCATION_ID);
                if (!TextUtils.isEmpty(string))

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

                                        if (s.equals("123")) {

                                            SpUtil.setString(getApplicationContext(), BookShelfConstant.BOOK_INIT_LOCATION_ID, "");

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
        });
        //无数据视图
        mLlErrorLayout = findViewById(R.id.rl_shelf_detail_error_bg);

        mErrorRetry_clicks = RxView.clicks(mLlErrorLayout).throttleFirst(1, TimeUnit.SECONDS).subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object o) throws Exception {
                //mLlErrorLayout.setVisibility(View.GONE);
                loadShelfDetail();
            }
        });
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
        mPresenter.refresh(IShelvesMvpContract.REFRESH_SHELVES_DETAIL, new String[]{"2"});
    }


    private int mCountShowExit;

    private void initControlExitLogo() {
        mTvVersionInfo = findViewById(R.id.tv_shelf_detail_version);
        mAppVersion = ManifestUtils.getVersionName(this);
        mTvVersionInfo.setText(mAppVersion);
        mTvVersionInfo.setVisibility(View.VISIBLE);

        //初始化退出dialog自定义View
        initExitDialogView();

        mTvVersionInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mCountShowExit < 3) {
                    mExit_id.setText("");
                    String string = SpUtil.getString(getApplicationContext(), BookShelfConstant.BOOK_INIT_LOCATION_ID);
                    if (!TextUtils.isEmpty(string))

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

                                            if (s.equals("123")) {

                                                SpUtil.setString(getApplicationContext(), BookShelfConstant.BOOK_INIT_LOCATION_ID, "");

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
            }
        });
    }

    private void initExitDialogView() {
        mLocation_exit_view = View.inflate(getApplicationContext(), R.layout.bookshelf_dialog_init_location_exit, null);

        TextView location_id = mLocation_exit_view.findViewById(R.id.tv_shelf_detail_exit_location);
        mExit_id = mLocation_exit_view.findViewById(R.id.et_shelf_detail_exit);

        String Id = SpUtil.getString(getApplicationContext(), BookShelfConstant.BOOK_INIT_LOCATION_ID);
        location_id.setText("您的代码是：" + Id);


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
                        }
                    }
                });
    }

    private void initCopyrightDialog() {
        if (mCopyrightDialog == null) {
            View mContentView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.bookshelf_dialog_shelf_copyright, null);

            mSvCopyrightContent = mContentView.findViewById(R.id.sv_shelf_detail_copyright);
            mTvCopyrightContent = mContentView.findViewById(R.id.tv_shelf_detail_copyright_content);

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
            if (window != null)
                window.setBackgroundDrawableResource(android.R.color.transparent);
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
        mTvCopyrightContent.setText(spannableString);
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
        mRvCategory.setHasFixedSize(true);
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
    public void onRefreshFinished(String refreshType, List<ShelvesCategoryResultBean> result) {
        ShelvesCategoryResultBean resultBean = result.get(0);
        if (resultBean != null) {
            showRefreshLayout(false);
            /**
             * 设置ShelfDetailActivityBg来源
             * 竖屏设备：九宫格背景图片，来自本地
             * 手机设备：如九宫格背景图片需要来自网络，则启用（已实现）
             */
            String bg = resultBean.getBg();
//            RequestOptions requestOptions = new RequestOptions();
//            requestOptions.centerCrop().diskCacheStrategy(DiskCacheStrategy.RESOURCE);
//            Glide.with(ShelfDetailActivity.this).load(bg).apply(requestOptions).into(new SimpleTarget<Drawable>(1080,1920) {
//                @Override
//                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
//                    mRlShelfRoot.setBackground(resource);
//                }
//            });

            if (mShelfCategoryAdapter == null) {
                mShelfCategoryAdapter = new ShelfCategoryAdapter(ShelfDetailActivity.this, resultBean.getCategories());
                mShelfCategoryAdapter.setShelfCategoryListener((shelfId, categoryId, categoryName) -> {

//                    StatisticsType.postStatisticsNow(getApplicationContext(), "2", String.valueOf(categoryId));
                    StatisticsPresenter.getInstance().startStatistics(getApplicationContext(), "2", String.valueOf(categoryId));
                    //跳转书架页面
                    UIHelper.startBookCategoryDetailActivity(ShelfDetailActivity.this, shelfId, categoryId, categoryName);

                });
                mRvCategory.setAdapter(mShelfCategoryAdapter);
            } else {
                mShelfCategoryAdapter.setCategoryEntity(resultBean.getCategories());
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

        mPresenter.destroy();
        super.onDestroy();
    }
}
