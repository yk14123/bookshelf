package com.chinafocus.bookshelf.ui.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chinafocus.bookshelf.R;
import com.chinafocus.bookshelf.bean.BookCategoryDetailRawBean.BookCategoryDetailResultBean;
import com.chinafocus.bookshelf.global.BookShelfConstant;
import com.chinafocus.bookshelf.model.base.activity.BaseActivity;
import com.chinafocus.bookshelf.presenter.shelves.BookCategoryDetailPresenter;
import com.chinafocus.bookshelf.presenter.shelves.IShelvesMvpContract;
import com.chinafocus.bookshelf.presenter.statistics.StatisticsPresenter;
import com.chinafocus.bookshelf.ui.adapter.BookCategoryAdapter;
import com.chinafocus.bookshelf.ui.widgets.LinearItemDecoration;
import com.chinafocus.bookshelf.utils.UIHelper;
import com.zhy.android.percent.support.PercentLinearLayout;

import java.util.List;

/**
 * 图书分类列表页
 *
 * @author HUANG_JIN
 * @version 1.0
 * create at 2018/11/27 10:36
 */
public class BookCategoryDetailActivity extends BaseActivity<BookCategoryDetailResultBean> {
    private static final String TAG = "BookCategoryDetail";
    //控制器
    private IShelvesMvpContract.IPresenter mPresenter;
    //数据列表
    private RecyclerView mRvBookCategory;
    //适配器
    private BookCategoryAdapter mBookCategoryAdapter;
    //书柜id
    private int mShelfId;
    //书柜名称
    private String mCategoryName;
    //类别id
    private int mCategoryId;
    //错误视图
    private PercentLinearLayout mLlErrorLayout;

    @Override
    protected void initView() {
        getExtraFromIntent();
        setContentView(R.layout.bookshelf_activity_book_category_detail);
        //初始化actionBar
        initNavMenu();

        //无数据视图
        mLlErrorLayout = findViewById(R.id.ll_bookshelf_reconnect_net);
        mLlErrorLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLlErrorLayout.setVisibility(View.GONE);
                loadBookCategory();
            }
        });

        //初始化RecyclerView
        initRvBookCategoryList();

        Log.i("MyLog", "BookCategoryDetailActivity  onCreate");
        //初始化presenter控制器
        loadBookCategory();
    }

    /**
     * 加载分类页面数据
     */
    private void loadBookCategory() {
        showLoading();
        //初始化控制器
        if (mPresenter == null) {
            mPresenter = new BookCategoryDetailPresenter(this);
        }
        mPresenter.refresh(IShelvesMvpContract.REFRESH_BOOK_CATEGORY_DETAIL,
                new String[]{String.valueOf(mShelfId), String.valueOf(mCategoryId)});
    }

    private void initRvBookCategoryList() {
        mRvBookCategory = findViewById(R.id.rv_book_category_list);
        LinearLayoutManager manager = new LinearLayoutManager(
                this, LinearLayoutManager.VERTICAL, false);
        mRvBookCategory.setLayoutManager(manager);
        mRvBookCategory.setHasFixedSize(true);
        mRvBookCategory.addItemDecoration(new LinearItemDecoration());
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
        if (!TextUtils.isEmpty(mCategoryName)) {
            mTvCategoryName.setText(mCategoryName);
        }
        //右側menu
        ImageView mIvRightMenu = findViewById(R.id.iv_bookshelf_right_menu);
        mIvRightMenu.setVisibility(View.INVISIBLE);
    }

    /**
     * 获取Intent中的参数数据
     */
    private void getExtraFromIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            mShelfId = intent.getIntExtra(BookShelfConstant.SHELF_ID, -1);
            mCategoryId = intent.getIntExtra(BookShelfConstant.CATEGORY_ID, -1);
            mCategoryName = intent.getStringExtra(BookShelfConstant.CATEGORY_NAME);
            Log.d(TAG, "getExtraFromIntent: mShelfId >>> " + mShelfId + "  mCategoryId >>> " + mCategoryId);
        }
    }

    @Override
    public void onRefreshFinished(String refreshType, List<BookCategoryDetailResultBean> result) {
        Log.d(TAG, "onRefreshFinished: resultBean.size >>> " + result.size());
        BookCategoryDetailResultBean bookCategoryDetailResultBean = result.get(0);
        if (bookCategoryDetailResultBean != null) {
            showRefreshLayout(false);
            List<BookCategoryDetailResultBean.BooksCategoryDetailFinalBean> books
                    = bookCategoryDetailResultBean.getBooks();
            if (mBookCategoryAdapter == null) {
                mBookCategoryAdapter = new BookCategoryAdapter(this, books);
                mRvBookCategory.setAdapter(mBookCategoryAdapter);
                mBookCategoryAdapter.setBookItemListener((bookId, bookName) -> {
                    Log.d(TAG, "onRefreshFinished: bookId >>> " + bookId + " bookName >>> " + bookName);
//                    StatisticsType.postStatisticsNow(getApplicationContext(), "3", String.valueOf(bookId));
                    StatisticsPresenter.getInstance().startStatistics(getApplicationContext(), "3", String.valueOf(bookId));
                    //跳轉圖書詳情頁
                    UIHelper.startBookMetaActivity(this, mShelfId, mCategoryId, bookId, bookName, mCategoryName);
                });
            } else {
                mBookCategoryAdapter.setCategoryEntity(books);
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
        mLlErrorLayout.setVisibility(showRefresh ? View.VISIBLE : View.GONE);
        mRvBookCategory.setVisibility(showRefresh ? View.GONE : View.VISIBLE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.destroy();
    }
}
