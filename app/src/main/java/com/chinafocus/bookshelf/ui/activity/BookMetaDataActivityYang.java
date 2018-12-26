package com.chinafocus.bookshelf.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chinafocus.bookshelf.R;
import com.chinafocus.bookshelf.bean.BookMetadataRawBean.BookMetadataResultBean;
import com.chinafocus.bookshelf.bean.BookMetadataRawBean.BookMetadataResultBean.TocBean;
import com.chinafocus.bookshelf.bean.TocResultBean;
import com.chinafocus.bookshelf.global.BookShelfConstant;
import com.chinafocus.bookshelf.model.base.activity.BaseActivity;
import com.chinafocus.bookshelf.presenter.shelves.BookMetaDataPresenter;
import com.chinafocus.bookshelf.presenter.shelves.IShelvesMvpContract;
import com.chinafocus.bookshelf.presenter.statistics.StatisticsPresenter;
import com.chinafocus.bookshelf.ui.adapter.BookNodeAdapterYang;
import com.chinafocus.bookshelf.ui.dialog.BookCoverDialog;
import com.chinafocus.bookshelf.ui.widgets.ExpandableTextView;
import com.chinafocus.bookshelf.utils.SpUtil;
import com.chinafocus.bookshelf.utils.SplitListUtil;
import com.chinafocus.bookshelf.utils.TocRawToResult;
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
    private BookNodeAdapterYang mBookNodeAdapter;
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
    private TextView mTv_expand_title;
    private Disposable mViewHeaderWrapperClicks;
    //图书封面
    private View mViewHeaderWrapper;
    private ImageView mIvBookCover;
    private TextView mTvCategoryTag;
    private ExpandableTextView mExpandText;
    private List<List<TocResultBean>> mTocBeanTotalLists;
    private String mOriginId;
    private int mTempCount = 1;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void initView() {
        getExtraFromIntent();
        setContentView(R.layout.bookshelf_activity_book_meta_data_yang);
        initNavMenu();

        mOriginId = SpUtil.getString(getApplicationContext(), BookShelfConstant.BOOK_INIT_LOCATION_ID);

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
        mNestedScrollView = findViewById(R.id.nsv_book_meta_data_yang);

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

                if (i1 == (nestedScrollView.getChildAt(0).getMeasuredHeight() - nestedScrollView.getMeasuredHeight())) {
                    // 底部
                    Log.i("onLoadMore", "onLoadMore");
                    //size 5
                    //加载4次 1- 2- 3- 4
                    //mTempCount
                    if (mTocBeanTotalLists != null) {
                        if (mTempCount < mTocBeanTotalLists.size()) {
                            mBookNodeAdapter.addListAndNotify(mTocBeanTotalLists.get(mTempCount));
                            mTempCount++;
                        } else {
                            Toast.makeText(getApplicationContext(), "本书已经全部加载完毕！", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "本书已经全部加载完毕！", Toast.LENGTH_LONG).show();
                    }
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

    private void initExpandText() {

        mTv_expand_title = findViewById(R.id.tv_expand_title);

        mExpandText = findViewById(R.id.expand_text_view);
        mExpandText.setOnExpandStateChangeListener((textView, isExpanded) -> {
            Log.d(TAG, "expandState changed >>> isExpanded >>> " + isExpanded);
//            requestNestedLayout();
        });


        mTvCategoryTag = findViewById(R.id.tv_book_meta_data_category_tag);


    }


    private void initRvMetaData() {
        mRvMetaData = findViewById(R.id.rv_book_meta_data);
        LinearLayoutManager manager = new LinearLayoutManager(
                this, LinearLayoutManager.VERTICAL, false);
        manager.setSmoothScrollbarEnabled(true);

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
            mShelfId = intent.getIntExtra(BookShelfConstant.SHELF_ID, 1);
            mBookId = intent.getIntExtra(BookShelfConstant.BOOK_ID, 1);
            mCategoryId = intent.getIntExtra(BookShelfConstant.CATEGORY_ID, 1);
            mCategoryTagName = intent.getStringExtra(BookShelfConstant.CATEGORY_NAME);
            mBookName = intent.getStringExtra(BookShelfConstant.BOOK_NAME);
            Log.d(TAG, "getExtraFromIntent: mShelfId >>>" + mShelfId
                    + " mBookId >>> " + mBookId + " mCategoryId >>> "
                    + mCategoryId + " mCategoryTagName >>> " + mCategoryTagName);

//            mShelfId = 2;
//            mBookId = 185;
//            mCategoryId = 16;
//            mCategoryTagName = "习近平著作";
//            mBookName = "习近平著作标题";
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
                        String.valueOf(mBookId),
                        mOriginId});
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
                    mTv_expand_title.setText(mBookTitle);
                    mTvCategoryTag.setText(mCategoryTagName);
                }
            }

            //图书封面
            mCoverUrl = dataBean.getCover();
            initBgView();


            //图书推荐语
            String comment = dataBean.getComment();
            if (!TextUtils.isEmpty(comment)) {
                Log.i(TAG, "comment >>> " + comment);
                mExpandText.setTextWithTag(Html.fromHtml(comment),
                        getString(R.string.bookshelf_book_comment));
            } else {
                String description = dataBean.getDescription();
                if (!TextUtils.isEmpty(description)) {
                    Log.i(TAG, "description >>> " + description);
                    mExpandText.setTextWithTag(Html.fromHtml(description),
                            getString(R.string.bookshelf_book_intro));
                }
            }


            /**
             *  clean一下Toc数据
             */
            List<TocBean> toc = dataBean.getToc();

            ArrayList<TocResultBean> tocResultList = new ArrayList<>();

            TocRawToResult.cleanData(toc, tocResultList, 1);

            Log.i("TocResultBean", "size-->" + tocResultList.size());

            //获取数据
//            ArrayList<TocBean> baseNodes = mPresenter.getTocList(toc, -1);

            /**
             * 客户端要做分页优化！！！
             */
            if (tocResultList.size() > 50) {

                mTocBeanTotalLists = SplitListUtil.splitList(tocResultList, 50);

                tocResultList = new ArrayList<>(mTocBeanTotalLists.get(0));

                Log.i("SplitListUtil", "size-->" + mTocBeanTotalLists.size());

            }

            if (mBookNodeAdapter == null) {
                mBookNodeAdapter = new BookNodeAdapterYang(this, tocResultList);
            }


            mBookNodeAdapter.setBookNodeClickListener(new BookNodeAdapterYang.OnBookNodeClickListener() {
                @Override
                public void onNodeClick(String label, String pageId) {

                    String str = mBookId + "!" + label;

                    Log.i("StatisticsType", "StatisticsType  last -->" + str);
//                    StatisticsType.postStatisticsNow(getApplicationContext(), "4", str);
                    StatisticsPresenter.getInstance().startStatistics(getApplicationContext(), "4", str);
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

            mNestedScrollView.postDelayed(new Runnable() {
                @Override
                public void run() {

                    if (mTocBeanTotalLists != null) {
                        mTempCount = 1;
                        mBookNodeAdapter.addReSetListAndNotify(mTocBeanTotalLists.get(0));
                    }
                }
            }, 200);

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

        mPresenter.destroy();
    }
}
