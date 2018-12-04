package com.chinafocus.bookshelf.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.chinafocus.bookshelf.R;
import com.chinafocus.bookshelf.base.BaseActivity;
import com.chinafocus.bookshelf.global.BookShelfConstant;
import com.chinafocus.bookshelf.model.bean.ShelvesCategoryResultBean;
import com.chinafocus.bookshelf.presenter.shelves.IShelvesMvpContract;
import com.chinafocus.bookshelf.presenter.shelves.ShelvesDetailPresenter;
import com.chinafocus.bookshelf.ui.adapter.ShelfCategoryAdapter;
import com.chinafocus.bookshelf.ui.adapter.ShelfIntroAdapter;
import com.chinafocus.bookshelf.ui.dialog.ShelfCopyrightDialog;
import com.chinafocus.bookshelf.utils.ManifestUtils;
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
    private ImageView mIvCopyRight;
    private ShelfCopyrightDialog mCopyrightDialog;
    private boolean isHideRvCategory = true;
    private Animation mCategory_Scale_out;
    private Animation mCategory_Scale_in;
    private Animation mShelfIntro_Translate_in;
    private Animation mShelfIntro_Translate_out;
    private Disposable mIvLogo_clicks, mIvCopyRight_clicks;

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
        initCopyRight();

        //初始化动画
        initAnim();

        //初始化Presenter
        mPresenter = new ShelvesDetailPresenter(this);
        //请求数据
        requestShelfDetail();
    }

    private void initCopyRight() {
        mIvCopyRight = findViewById(R.id.iv_shelf_detail_copyright);
        mIvCopyRight_clicks = RxView.clicks(mIvCopyRight).throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        if (mCopyrightDialog == null) {
                            mCopyrightDialog = new ShelfCopyrightDialog(ShelfDetailActivity.this);
                        }
                        //点击copyRight弹出版权信息对话框
                        if (!mCopyrightDialog.isShowing()) {
                            mCopyrightDialog.show();
                        }
                    }
                });
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


    @Override
    public void onRefreshFinished(String refreshType, List<ShelvesCategoryResultBean> result) {
        ShelvesCategoryResultBean resultBean = result.get(0);
        if (resultBean != null) {
            if (mShelfCategoryAdapter == null) {
                mShelfCategoryAdapter = new ShelfCategoryAdapter(ShelfDetailActivity.this, resultBean.getCategories());
                mShelfCategoryAdapter.setShelfCategoryListener((shelfId, categoryId, categoryName) -> {
                    //跳转书架页面
                    Intent intent = new Intent(ShelfDetailActivity.this, BookCategoryDetailActivity.class);
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
                    mRvShelfIntro.setVisibility(View.VISIBLE);
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
                    mRvCategory.setVisibility(View.VISIBLE);
                    mRvCategory.startAnimation(mCategory_Scale_in);
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
        if (mIvCopyRight_clicks != null && mIvCopyRight_clicks.isDisposed()) {
            mIvCopyRight_clicks.dispose();
        }
        super.onDestroy();
    }
}
