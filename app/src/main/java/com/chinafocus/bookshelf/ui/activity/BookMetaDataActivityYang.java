package com.chinafocus.bookshelf.ui.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chinafocus.bookshelf.R;
import com.chinafocus.bookshelf.bean.BookMetadataRawBean.BookMetadataResultBean;
import com.chinafocus.bookshelf.bean.BookMetadataRawBean.BookMetadataResultBean.TocBean;
import com.chinafocus.bookshelf.model.base.activity.BaseActivity;
import com.chinafocus.bookshelf.presenter.shelves.BookMetaDataPresenter;
import com.chinafocus.bookshelf.presenter.shelves.IShelvesMvpContract;
import com.chinafocus.bookshelf.presenter.statistics.StatisticsPresenter;
import com.chinafocus.bookshelf.ui.adapter.BookNodeAdapter;
import com.chinafocus.bookshelf.ui.dialog.BookCoverDialog;
import com.chinafocus.bookshelf.utils.UIHelper;
import com.jakewharton.rxbinding2.view.RxView;
import com.zhy.android.percent.support.PercentLinearLayout;
import com.zhy.android.percent.support.PercentRelativeLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * 图书目录页面
 *
 * @author HUANG_JIN
 * @version 1.0
 * create at 2018/11/27 13:06
 */
public class BookMetaDataActivityYang extends BaseActivity<BookMetadataResultBean> implements View.OnClickListener {
    private static final String TAG = "BookMeta";
    private PercentRelativeLayout mRlAppBar;
    private BookMetaDataPresenter mPresenter;
    //错误视图
    private PercentLinearLayout mLlErrorLayout;
    //目录列表容器
    private RecyclerView mRvMetaData;
    private BookNodeAdapter mBookNodeAdapter;
    //滑动控件
    private NestedScrollView mNestedScrollView;
    //悬浮控件
    private ImageView mIvBackTop;

    private String mCoverUrl;
    private BookCoverDialog mBookCoverDialog;
    //图书名
    private String mBookName;
    //当前图书书柜id
    private int mShelfId;
    //当前图书id
    private int mBookId;
    //当前图书的分类
    private int mCategoryId;
    //分类标签名
    private String mCategoryTagName;
    private TextView mTv_expand_content_view;
    private TextView mTv_expand_title;
    private TextView mTv_expand_control;
    private ValueAnimator mAnimator;
    private LinearLayout.LayoutParams mTv_expand_layoutParams;
    private Drawable mBookshelf_arrow_up;
    private Drawable mBookshelf_arrow_down;
    private Disposable mViewHeaderWrapperClicks;
    //图书封面
    private View mViewHeaderWrapper;
    private ImageView mIvBookCover;
    private Disposable mTvExpandControlClicks;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void initView() {
        getExtraFromIntent();
        setContentView(R.layout.bookshelf_activity_book_meta_data_yang);
        initNavMenu();
        //无数据视图
        mLlErrorLayout = findViewById(R.id.ll_bookshelf_reconnect_net);
        mLlErrorLayout.setOnClickListener(this);
        //封面点击事件
        initViewHeaderWrapper();
        //初始化图书信息HeaderExpand标题及内容
        initExpandText();
        //初始化多级目录信息
        initRvMetaData();
        //返回顶部
        iniRollbackTop();
        //加载数据
        loadBookMeta();

    }

    private void iniRollbackTop() {
        mIvBackTop = findViewById(R.id.iv_book_meta_back_top);
        mIvBackTop.setOnClickListener(this);
        //底部拖动View
        mNestedScrollView = findViewById(R.id.nsv_book_meta_data);

        mNestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView nestedScrollView, int i, int i1, int i2, int i3) {
                //判断当前NestedScrollView是否滑动到顶部
                if (mNestedScrollView.getScrollY() == 0) {
//                    setFloatingButtonState(true);
                    mIvBackTop.setVisibility(View.GONE);
                } else {
//                    setFloatingButtonState(false);
                    mIvBackTop.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initViewHeaderWrapper() {

        mIvBookCover = findViewById(R.id.iv_book_meta_data_cover_yang);

        mViewHeaderWrapper = findViewById(R.id.view_header_wrapper);

        mViewHeaderWrapperClicks = RxView.clicks(mViewHeaderWrapper).throttleFirst(1000, TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        if (!TextUtils.isEmpty(mCoverUrl)) {
                            if (mBookCoverDialog == null) {
                                mBookCoverDialog = new BookCoverDialog(BookMetaDataActivityYang.this, mRlAppBar);
                                mBookCoverDialog.setImageUrl(mCoverUrl);
                            }
                            if (!mBookCoverDialog.isShowing()) {
                                mBookCoverDialog.show();
                            }
                        }
                    }
                });

    }


    private void initArrowImage() {
        mBookshelf_arrow_up = getResources().getDrawable(
                R.drawable.bookshelf_arrow_up);
        mBookshelf_arrow_down = getResources().getDrawable(
                R.drawable.bookshelf_arrow_down);
    }

    private boolean isHeaderContentLoadMore = true;

    private void toggle() {
        int shortHeight = getShortHeight();
        int longHeight = getLongHeight();

        if (!isHeaderContentLoadMore) {
            //打开更多
            //不需要更多，所以执行，关闭-->显示：更多
            isHeaderContentLoadMore = true;
            if (longHeight > shortHeight)
                mAnimator = ValueAnimator.ofInt(longHeight, shortHeight);
        } else {
            //关闭收入
            //是需要更多，所以执行，打开-->显示：收入
            isHeaderContentLoadMore = false;
            if (longHeight > shortHeight)
                mAnimator = ValueAnimator.ofInt(shortHeight, longHeight);
        }

        if (mAnimator != null && !mAnimator.isRunning()) {
            mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    mTv_expand_layoutParams.height = (Integer) animation.getAnimatedValue();
                    mTv_expand_content_view.setLayoutParams(mTv_expand_layoutParams);
                }
            });

            mAnimator.setDuration(500);
            mAnimator.start();

            mAnimator.addListener(new AnimatorListenerAdapter() {

                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);

                    if (isHeaderContentLoadMore) {
                        mTv_expand_control.setText("更多");
                        mTv_expand_control.setCompoundDrawablesWithIntrinsicBounds(null, null, mBookshelf_arrow_down, null);
                    } else {
                        mTv_expand_control.setText("收起");
                        mTv_expand_control.setCompoundDrawablesWithIntrinsicBounds(null, null, mBookshelf_arrow_up, null);
                    }

                }
            });
        }
    }

    private void initExpandText() {
        mTv_expand_title = findViewById(R.id.tv_expand_title);

        mTv_expand_content_view = findViewById(R.id.tv_expand_content_view);

        mTv_expand_layoutParams = (LinearLayout.LayoutParams) mTv_expand_content_view.getLayoutParams();
        mTv_expand_layoutParams.height = getShortHeight();
        mTv_expand_content_view.setLayoutParams(mTv_expand_layoutParams);
        mTv_expand_content_view.setText(Html.fromHtml(mDataTest));

        mTv_expand_control = findViewById(R.id.tv_expand_control);
        mTvExpandControlClicks = RxView.clicks(mTv_expand_control).throttleFirst(1000, TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        toggle();
                    }
                });
        initArrowImage();
    }

    private int getShortHeight() {
        int measuredWidth = mTv_expand_content_view.getMeasuredWidth();

        TextView textView = new TextView(this);
        textView.setText(Html.fromHtml(mDataTest));
        //竖屏设备sp：25
//        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);//要和xml配置一致
//        textView.setLineSpacing(30, 1);//要和xml配置一致

        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);//要和xml配置一致
        textView.setLineSpacing(30, 1);//要和xml配置一致，始终是固定30。因为xml里面写的是dp。竖屏和手机都是10dp

        textView.setMaxLines(4);

        int widthSpec = View.MeasureSpec.makeMeasureSpec(measuredWidth, View.MeasureSpec.EXACTLY);
        int heightSpec = View.MeasureSpec.makeMeasureSpec(10000, View.MeasureSpec.AT_MOST);

        textView.measure(widthSpec, heightSpec);

        return textView.getMeasuredHeight();
    }

    private String mDataTest = "人是科技创新最关键的因素。创新的事业呼唤创新的人才。尊重人才，是中华民族的悠久传统。“思皇多士，生此王国。王国克生，维周之桢；济济多士，文王以宁。”这是《诗经•大雅•文王》中的话，说的是周文王尊贤礼士，贤才济济，所以国势强盛。千秋基业，人才为先。实现中华民族伟大复兴，人才越多越好，本事越大越好。<br><br><i>——摘自“加快从要素驱动、投资规模驱动发展为主向以创新驱动发展为主的转变（2014年6月9日）”，《习近平谈治国理政 第一卷》</i>";

    private int getLongHeight() {
        int measuredWidth = mTv_expand_content_view.getMeasuredWidth();

        TextView textView = new TextView(this);
        textView.setText(Html.fromHtml(mDataTest));
        //竖屏设备sp：25
//        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);//要和xml配置一致
//        textView.setLineSpacing(30, 1);//要和xml配置一致

        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);//要和xml配置一致
        textView.setLineSpacing(30, 1);//要和xml配置一致

        int widthSpec = View.MeasureSpec.makeMeasureSpec(measuredWidth, View.MeasureSpec.EXACTLY);
        int heightSpec = View.MeasureSpec.makeMeasureSpec(10000, View.MeasureSpec.AT_MOST);

        textView.measure(widthSpec, heightSpec);

        return textView.getMeasuredHeight();
    }

    private void initRvMetaData() {
        mRvMetaData = findViewById(R.id.rv_book_meta_data);
        LinearLayoutManager manager = new LinearLayoutManager(
                this, LinearLayoutManager.VERTICAL, false);
        manager.setSmoothScrollbarEnabled(true);
//        manager.setAutoMeasureEnabled(true);默认是true

        mRvMetaData.setLayoutManager(manager);
        mRvMetaData.setHasFixedSize(true);//当前条目固定的情况下，设置此属性，提高RecyclerView的性能
        mRvMetaData.setFocusable(false);//取消RecyclerView获取焦点事件，避免NestedScrollView无法滑动到顶部的问题
        mRvMetaData.setNestedScrollingEnabled(false);//禁止RecyclerView的滑动事件，交给NestedScrollView
    }

    /**
     * 获取intent跳转的参数
     */
    private void getExtraFromIntent() {
        Intent intent = getIntent();
        if (intent != null) {
//            mShelfId = intent.getIntExtra(BookShelfConstant.SHELF_ID, 1);
//            mBookId = intent.getIntExtra(BookShelfConstant.BOOK_ID, 1);
//            mCategoryId = intent.getIntExtra(BookShelfConstant.CATEGORY_ID, 1);
//            mCategoryTagName = intent.getStringExtra(BookShelfConstant.CATEGORY_NAME);
//            mBookName = intent.getStringExtra(BookShelfConstant.BOOK_NAME);
            Log.d(TAG, "getExtraFromIntent: mShelfId >>>" + mShelfId
                    + " mBookId >>> " + mBookId + " mCategoryId >>> "
                    + mCategoryId + " mCategoryTagName >>> " + mCategoryTagName);

            mShelfId = 2;
            mBookId = 185;
            mCategoryId = 16;
            mCategoryTagName = "习近平著作";
            mBookName = "习近平著作标题";
//            mShelfId >>>2 mBookId >>> 185 mCategoryId >>> 16 mCategoryTagName >>> 习近平著作
//            mShelfId >>>2 mBookId >>> 189 mCategoryId >>> 13 mCategoryTagName >>> 经史典集
//            mShelfId = 2;
//            mBookId = 189;
//            mCategoryId = 13;
//            mCategoryTagName = "经史典集";
//            mBookName = "经史典集标题";
        }
    }

    /**
     * 加载目录页面数据
     */
    private void loadBookMeta() {
        showLoading();
        //初始化控制器
        if (mPresenter == null) {
            mPresenter = new BookMetaDataPresenter(this);
        }
        mPresenter.refresh(IShelvesMvpContract.REFRESH_BOOK_METADATA,
                new String[]{String.valueOf(mShelfId),
                        String.valueOf(mCategoryId),
                        String.valueOf(mBookId)});
    }

    /**
     * 初始化通用的ActionBar布局
     */
    private void initNavMenu() {
        mRlAppBar = findViewById(R.id.tb_book_category_bar);
        //Back键
        ImageView mIvBack = findViewById(R.id.iv_bookshelf_left_menu);
        mIvBack.setOnClickListener(v -> {
            if (mBookCoverDialog != null && mBookCoverDialog.isShowing()) {
                mBookCoverDialog.dismiss();
            } else finish();
        });
        //初始化标题
        //当前图书的名称
        TextView mTvBookNavTitle = findViewById(R.id.tv_bookshelf_title);
        if (!TextUtils.isEmpty(mBookName))
            mTvBookNavTitle.setText(mBookName);
        //右側menu
        ImageView mIvRightMenu = findViewById(R.id.iv_bookshelf_right_menu);
        mIvRightMenu.setVisibility(View.INVISIBLE);
    }

//    /**
//     * 設置悬浮按钮的显示和隐藏状态
//     *
//     * @param reachTop 当前NestedScrollView是否滑动到顶部
//     */
//    private void setFloatingButtonState(boolean reachTop) {
//        if (reachTop) {
//            if (mIvBackTop.getVisibility() == View.VISIBLE) {
//                mIvBackTop.setVisibility(View.GONE);
//            }
//        } else {
//            if (mIvBackTop.getVisibility() == View.GONE) {
//                mIvBackTop.setVisibility(View.VISIBLE);
//            }
//        }
//    }

    @SuppressLint("CheckResult")
    @Override
    public void onRefreshFinished(String refreshType, List<BookMetadataResultBean> result) {
        Log.d(TAG, "showTips: onRefreshFinished >>> ");
        BookMetadataResultBean dataBean = result.get(0);
        if (dataBean != null) {
            showRefreshLayout(false);
            //图书名称
            String mBookTitle = dataBean.getTitle();
            if (!TextUtils.isEmpty(mBookTitle)) {
                if (!TextUtils.isEmpty(mCategoryTagName)) {
//                    mTvBookTitle.setContentAndTag(mBookTitle, mCategoryTagName);
                }
            }

            //图书封面
            mCoverUrl = dataBean.getCover();
            initBgView();

            //图书推荐语
            String comment = dataBean.getComment();
            if (!TextUtils.isEmpty(comment)) {
                Log.i(TAG, "comment >>> " + comment);
//                mExpandText.setTextWithTag(Html.fromHtml(comment),
//                        getString(R.string.bookshelf_book_comment));
            } else {
                String description = dataBean.getDescription();
                if (!TextUtils.isEmpty(description)) {
                    Log.i(TAG, "description >>> " + description);
//                    mExpandText.setTextWithTag(Html.fromHtml(description),
//                            getString(R.string.bookshelf_book_intro));
                }
            }
//            此处在设置完成数据之后，设置延迟100ms之后执行paddingTop的自适应刷新操作
//            mLlBookMetaWrapper.postDelayed(this::requestNestedLayout, 100);

            List<TocBean> toc = dataBean.getToc();
            //获取数据
            ArrayList<TocBean> baseNodes = mPresenter.getTocList(toc, -1);


            /**
             * 客户端要做分页优化！！！
             */
            ArrayList<TocBean> finalBaseNode;
            if (baseNodes.size() > 99) {
                List<TocBean> subBaseNodes = baseNodes.subList(0, 100);
                finalBaseNode = new ArrayList<>(subBaseNodes);
            } else {
                finalBaseNode = baseNodes;
            }

            if (mBookNodeAdapter == null) {
                mBookNodeAdapter = new BookNodeAdapter(this, finalBaseNode);
            }

            mBookNodeAdapter.setBookNodeClickListener(new BookNodeAdapter.OnBookNodeClickListener() {
                @Override
                public void onNodeClick(String label, String pageId, String title) {

                    String str = mBookId + "!" + title;

                    Log.i("StatisticsType", "StatisticsType  last -->" + str);
//                    StatisticsType.postStatisticsNow(getApplicationContext(), "4", str);
                    StatisticsPresenter.getInstance().startStatistics(getApplicationContext(),"4",str);
                    UIHelper.startContentDetailActivity(BookMetaDataActivityYang.this,
                            mShelfId, mCategoryId, mBookId, label, pageId);
                }
            });

            mRvMetaData.setAdapter(mBookNodeAdapter);
        } else {
            showRefreshLayout(true);
        }
        dismissLoading();
    }

    @SuppressLint("CheckResult")
    private void initBgView() {
        RequestOptions requestOptions = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.bookshelf_default_cover_port)
                .error(R.drawable.bookshelf_default_cover_port);

        Glide.with(this)
                .load(mCoverUrl)
                .apply(requestOptions)
                .into(mIvBookCover);
    }

    @Override
    public void showTips(String message) {
        dismissLoading();
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        showRefreshLayout(true);
    }

    private void showRefreshLayout(boolean showRefresh) {
        mLlErrorLayout.setVisibility(showRefresh ? View.VISIBLE : View.GONE);
        mNestedScrollView.setVisibility(showRefresh ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        if (v == mIvBackTop) {
            mNestedScrollView.fullScroll(View.FOCUS_UP);
            if (!isHeaderContentLoadMore)
                toggle();
        } else if (v == mLlErrorLayout) {
            mLlErrorLayout.setVisibility(View.GONE);
            loadBookMeta();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mViewHeaderWrapperClicks != null && mViewHeaderWrapperClicks.isDisposed()) {
            mViewHeaderWrapperClicks.dispose();
        }

        if (mTvExpandControlClicks != null && mTvExpandControlClicks.isDisposed()) {
            mTvExpandControlClicks.dispose();
        }
    }
}
