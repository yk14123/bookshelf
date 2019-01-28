package com.chinafocus.bookshelf.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chinafocus.bookshelf.R;
import com.chinafocus.bookshelf.bean.BookContentRawBean;
import com.chinafocus.bookshelf.global.BookShelfConstant;
import com.chinafocus.bookshelf.model.base.activity.BaseActivity;
import com.chinafocus.bookshelf.presenter.shelves.BookContentDetailPresenter;
import com.chinafocus.bookshelf.presenter.shelves.IShelvesMvpContract;
import com.chinafocus.bookshelf.ui.adapter.BookContentDetailAdapterYang;
import com.chinafocus.bookshelf.ui.dialog.FontSettingDialog;
import com.chinafocus.bookshelf.utils.SpUtil;
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
    private BookContentDetailPresenter mPresenter;
    //错误视图
    private PercentLinearLayout mLlErrorLayout;
    //内容展示控件
    private RecyclerView mRvBookContent;
    //适配器
//    private BookContentDetailAdapter mBookContentAdapter;
    private BookContentDetailAdapterYang mBookContentAdapter;
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

    private String mOriginId;

    private ImageView mIvBackTop;

    private long[] mHits = new long[2];//数组的次数就是击打的次数

    @Override
    protected void initView() {
        //初始化Presenter
        if (mPresenter == null)
            mPresenter = new BookContentDetailPresenter(this);

        mOriginId = SpUtil.getString(getApplicationContext(), BookShelfConstant.BOOK_INIT_LOCATION_ID);

        getExtraFromIntent();
        setContentView(R.layout.bookshelf_activity_book_content_detail);
        initNavMenu();
        mLlErrorLayout = findViewById(R.id.ll_bookshelf_reconnect_net);
        mLlErrorLayout.setOnClickListener(v -> loadBookContent(mPageId));

        //返回顶部
        iniRollbackTop();

        //初始化RecyclerView
        initRvContent();

        loadBookContent(mPageId);
    }

    private void initRvContent() {
        mRvBookContent = findViewById(R.id.rv_book_content_detail);
        LinearLayoutManager manager = new LinearLayoutManager(
                this, LinearLayoutManager.VERTICAL, false);
        mRvBookContent.setLayoutManager(manager);

//        mRvBookContent.addItemDecoration(new RecyclerView.ItemDecoration() {
//            @Override
//            public void getItemOffsets(@NonNull Rect outRect,
//                                       @NonNull View view,
//                                       @NonNull RecyclerView parent,
//                                       @NonNull RecyclerView.State state) {
//                outRect.top = 30;
//            }
//        });

        //取消RecyclerView的自动滑动效果 ---> 避免调整字体之后自动滑动到顶部
        mRvBookContent.setFocusableInTouchMode(false);
        //添加下拉底部加载下一步数据

        mRvBookContent.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int position = layoutManager.findFirstVisibleItemPosition();
                View firstVisibleChildView = layoutManager.findViewByPosition(position);
                int itemHeight = firstVisibleChildView.getHeight();

                int top = firstVisibleChildView.getTop();

                int i = (position) * itemHeight - top;

                Log.i("firstVisibleChildView", "firstVisibleChildView -- getTop -- >" + firstVisibleChildView.getTop());
                Log.i("firstVisibleChildView", "firstVisibleChildView -- getHeight -- >" + firstVisibleChildView.getHeight());

                if (i > 0 && top < 0) {
                    mIvBackTop.setVisibility(View.VISIBLE);
                } else {
                    mIvBackTop.setVisibility(View.GONE);
                }

            }
        });

    }

    private void iniRollbackTop() {
        mIvBackTop = findViewById(R.id.iv_book_content_back_top);
        mIvBackTop.setOnClickListener(v -> {
//            mRvBookContent.smoothScrollToPosition(0);
            mRvBookContent.scrollToPosition(0);
            mIvBackTop.post(() -> mIvBackTop.setVisibility(View.GONE));

        });
    }

    /**
     * 初始化通用的ActionBar布局
     */
    @SuppressLint("CheckResult")
    private void initNavMenu() {
        //Back键
        ImageView mIvBack = findViewById(R.id.iv_bookshelf_left_menu);
        mIvBack.setOnClickListener(v -> finish());
        //初始化标题
        TextView mTvCategoryName = findViewById(R.id.tv_bookshelf_title);
        if (!TextUtils.isEmpty(mBookName)) {
            mTvCategoryName.setText(mBookName);
        }

        mTvCategoryName.setOnClickListener(v -> {

            System.arraycopy(mHits, 1, mHits, 0, mHits.length - 1);
            mHits[mHits.length - 1] = SystemClock.uptimeMillis();
            if (mHits[mHits.length - 1] - mHits[0] < 500) {
                //双击后做处理
//                mRvBookContent.smoothScrollToPosition(0);
                mRvBookContent.scrollToPosition(0);
                mIvBackTop.post(() -> mIvBackTop.setVisibility(View.GONE));
            }


        });


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
            mPageId = mPresenter.formatUrl(mPageId);

            Log.i("BookContent", "mPageId-!!0000000!!!->" + mPageId);
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
        //初始化Presenter
//        mPresenter.refresh(IShelvesMvpContract.REFRESH_BOOK_CONTENT_DETAIL,
//                new String[]{String.valueOf(mShelfId),
//                        String.valueOf(mCategoryId),
//                        String.valueOf(mBookId), pageId});

        mPresenter.refresh(IShelvesMvpContract.REFRESH_BOOK_CONTENT_AES_DETAIL,
                new String[]{String.valueOf(mShelfId),
                        String.valueOf(mCategoryId),
                        String.valueOf(mBookId), pageId, mOriginId});

    }


    @Override
    public void onRefreshFinished(String refreshType, List<BookContentRawBean.BookContentResultBean> result) {
        BookContentRawBean.BookContentResultBean bookContentResultBean = result.get(0);
        if (bookContentResultBean != null) {
            showRefreshLayout(false);
            mNextPage = bookContentResultBean.getNext();
            mNextPage = mPresenter.formatUrl(mNextPage);
            if (mBookContentAdapter == null) {
                //首次加載
//                mBookContentAdapter = new BookContentDetailAdapter(this, result);
                mBookContentAdapter = new BookContentDetailAdapterYang(this, result);
//
                mBookContentAdapter.addOnLoadMoreFromFootView(() -> {

                    Log.i("MyBug", "loadBookContent");

                    isLoadMore = true;
                    loadBookContent(mNextPage);
                });

                mRvBookContent.setAdapter(mBookContentAdapter);
            } else {
                //加載更多
                mBookContentAdapter.addContentEntity(bookContentResultBean);
            }
        } else {
            showRefreshLayout(true);
        }
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
//        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        //当前在加载更多操作过程中，无需显示网络刷新视图，只需提示用户网络异常情况
        if (!isLoadMore) {
            showRefreshLayout(true);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.destroy();
    }
}
