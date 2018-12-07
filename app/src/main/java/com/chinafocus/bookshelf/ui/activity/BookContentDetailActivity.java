package com.chinafocus.bookshelf.ui.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chinafocus.bookshelf.R;
import com.chinafocus.bookshelf.base.BaseActivity;
import com.chinafocus.bookshelf.global.BookShelfConstant;
import com.chinafocus.bookshelf.model.bean.BookContentRawBean;
import com.chinafocus.bookshelf.presenter.shelves.BookContentDetailPresenter;
import com.chinafocus.bookshelf.presenter.shelves.IShelvesMvpContract;
import com.chinafocus.bookshelf.ui.adapter.BookContentDetailAdapter;
import com.chinafocus.bookshelf.ui.dialog.FontSettingDialog;
import com.zhy.android.percent.support.PercentLinearLayout;

import java.util.List;

/**
 * 图书章节内容頁面
 *
 * @author HUANG_JIN
 * @version 1.0
 * create at 2018/11/27 15:34
 */

public class BookContentDetailActivity extends BaseActivity<BookContentRawBean.BookContentResultBean> {
    private static final String TAG = "BookContent";
    private IShelvesMvpContract.IPresenter mPresenter;
    //错误视图
    private PercentLinearLayout mLlErrorLayout;
    //内容展示控件
    private RecyclerView mRvBookContent;
    //适配器
    private BookContentDetailAdapter mBookContentAdapter;
    //当前图书书柜id
    private int mShelfId;
    //当前图书id
    private int mBookId;
    //当前图书的分类
    private int mCategoryId;
    //当前图书的名称
    private String mBookName;
    //上一頁的full參數
    private String mPageId;
    //当前内容页的下一个pageId参数
    private String mNextPage;
    //是否是加载更多
    private boolean isLoadMore = false;
    //字体调节对话框
    private FontSettingDialog mFontDialog;


    @Override
    protected void initView() {
        getExtraFromIntent();
        setContentView(R.layout.bookshelf_activity_book_content_detail);
        initNavMenu();
        mLlErrorLayout = findViewById(R.id.ll_bookshelf_reconnect_net);
        mLlErrorLayout.setOnClickListener(v -> loadBookContent(mPageId));
        //初始化RecyclerView
        mRvBookContent = findViewById(R.id.rv_book_content_detail);
        LinearLayoutManager manager = new LinearLayoutManager(
                this, LinearLayoutManager.VERTICAL, false);
        mRvBookContent.setLayoutManager(manager);
        //取消RecyclerView的自动滑动效果 ---> 避免调整字体之后自动滑动到顶部
        mRvBookContent.setFocusableInTouchMode(false);
        //添加下拉底部加载下一步数据
        mRvBookContent.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                //判断当前RecyclerView是否滑到底部。加载更多的逻辑 >>>避免当前只有一条的情况
//                int childCount = recyclerView.getChildCount();
                if (!mRvBookContent.canScrollVertically(1)
                        && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    Log.d(TAG, "onScrollStateChanged: 当前滑动到底部 >>>> loadMore >>> ");
                    isLoadMore = true;
                    loadBookContent(mNextPage);
                }
            }
        });

        loadBookContent(mPageId);
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
        if (!TextUtils.isEmpty(mBookName)) {
            mTvCategoryName.setText(mBookName);
        }
        //右側menu
        ImageView mIvRightMenu = findViewById(R.id.iv_bookshelf_right_menu);
        mIvRightMenu.setVisibility(View.VISIBLE);
        mIvRightMenu.setOnClickListener(v -> {
            //调整字体大小
            if (mFontDialog == null) {
                mFontDialog = new FontSettingDialog(this);
                mFontDialog.setOnFontOptionListener(fontZoomSize -> {
                    Log.d(TAG, "initNavMenu: fontOption >>> " + fontZoomSize);
                    if (mBookContentAdapter != null)
                        mBookContentAdapter.setFontSize(fontZoomSize);
                });
            }
            if (!mFontDialog.isShowing()) {
                mFontDialog.show();
            }
        });
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
            mBookName = intent.getStringExtra(BookShelfConstant.BOOK_NAME);
            mPageId = intent.getStringExtra(BookShelfConstant.PAGE);
            Log.d(TAG, "getExtraFromIntent: mShelfId >>>" + mShelfId
                    + " mBookId >>> " + mBookId + " mCategoryId >>> "
                    + mCategoryId + " mBookName >>> " + mBookName
                    + " mPageId >>> " + mPageId);
        }
    }

    /**
     * 加载指定pageId的章节内容数据
     */
    private void loadBookContent(String pageId) {
        showRefreshLayout(false);
        showLoading();
        //初始化Presenter
        if (mPresenter == null)
            mPresenter = new BookContentDetailPresenter(this);
//        mPresenter.refresh(IShelvesMvpContract.REFRESH_BOOK_CONTENT_DETAIL,
//                new String[]{String.valueOf(mShelfId),
//                        String.valueOf(mCategoryId),
//                        String.valueOf(mBookId), pageId});

        mPresenter.refresh(IShelvesMvpContract.REFRESH_BOOK_CONTENT_AES_DETAIL,
                new String[]{String.valueOf(mShelfId),
                        String.valueOf(mCategoryId),
                        String.valueOf(mBookId), pageId});
    }


    @Override
    public void onRefreshFinished(String refreshType, List<BookContentRawBean.BookContentResultBean> result) {
        BookContentRawBean.BookContentResultBean bookContentResultBean = result.get(0);
        if (bookContentResultBean != null) {
            showRefreshLayout(false);
            mNextPage = bookContentResultBean.getNext();
            if (mBookContentAdapter == null) {
                //首次加載
                mBookContentAdapter = new BookContentDetailAdapter(this, result);
                mRvBookContent.setAdapter(mBookContentAdapter);
            } else {
                //加載更多
                mBookContentAdapter.addContentEntity(bookContentResultBean);
            }
        } else {
            showRefreshLayout(true);
        }
        dismissLoading();
    }

    /**
     * 显示刷新视图
     *
     * @param showRefresh 是否刷新视图，false将显示内容视图
     */
    private void showRefreshLayout(boolean showRefresh) {
        mLlErrorLayout.setVisibility(showRefresh ? View.VISIBLE : View.GONE);
        mRvBookContent.setVisibility(showRefresh ? View.GONE : View.VISIBLE);
    }

    @Override
    public void showTips(String message) {
        dismissLoading();
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        //当前在加载更多操作过程中，无需显示网络刷新视图，只需提示用户网络异常情况
        if (!isLoadMore) {
            showRefreshLayout(true);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
