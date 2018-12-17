package com.chinafocus.bookshelf.ui.activity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.chinafocus.bookshelf.R;
import com.chinafocus.bookshelf.base.BaseActivity;
import com.chinafocus.bookshelf.model.bean.ShelvesCategoryResultBean;
import com.chinafocus.bookshelf.presenter.shelves.IShelvesMvpContract;
import com.chinafocus.bookshelf.presenter.shelves.ShelvesDetailPresenter;
import com.chinafocus.bookshelf.ui.adapter.ShelfCategoryAdapter;
import com.chinafocus.bookshelf.ui.adapter.ShelfIntroAdapter;
import com.chinafocus.bookshelf.utils.ManifestUtils;
import com.chinafocus.bookshelf.utils.UIHelper;
import com.jakewharton.rxbinding2.view.RxView;
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

    @SuppressLint("CheckResult")
    @Override
    protected void initView() {
        setContentView(R.layout.bookshelf_activity_detail);
        //设置当前的版本号
        TextView mTvVersionInfo = findViewById(R.id.tv_shelf_detail_version);
        String appVersion = ManifestUtils.getVersionName(this);
        mTvVersionInfo.setText(appVersion);
        //root
        mRlShelfRoot = findViewById(R.id.rl_shelf_detail_root);

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

        //初始化Presenter
        mPresenter = new ShelvesDetailPresenter(this);
        //请求数据
        requestShelfDetail();

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

    /**
     * 请求九宫格分类API接口数据
     */
    private void requestShelfDetail() {
        mPresenter.refresh(IShelvesMvpContract.REFRESH_SHELVES_DETAIL, new String[]{"2"});
    }

    @SuppressLint("CheckResult")
    @Override
    public void onRefreshFinished(String refreshType, List<ShelvesCategoryResultBean> result) {
        ShelvesCategoryResultBean resultBean = result.get(0);
        if (resultBean != null) {
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
                    //跳转书架页面
                    UIHelper.startBookCategoryDetailActivity(ShelfDetailActivity.this,shelfId,categoryId,categoryName);

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
        super.onDestroy();
    }
}
