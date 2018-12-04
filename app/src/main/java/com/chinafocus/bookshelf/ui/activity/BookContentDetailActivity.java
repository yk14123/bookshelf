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

import com.chinafocus.bookshelf.R;
import com.chinafocus.bookshelf.base.BaseActivity;
import com.chinafocus.bookshelf.global.BookShelfConstant;
import com.chinafocus.bookshelf.model.bean.BookContentRawBean;
import com.chinafocus.bookshelf.presenter.shelves.BookContentDetailPresenter;
import com.chinafocus.bookshelf.presenter.shelves.IShelvesMvpContract;
import com.chinafocus.bookshelf.ui.adapter.BookContentDetailAdapter;
import com.chinafocus.bookshelf.ui.dialog.FontSettingsDialog;

import java.util.List;

/**
 * 图书章节内容頁面
 *
 * @author HUANG_JIN
 * @version 1.0
 * create at 2018/11/27 15:34
 */

public class BookContentDetailActivity extends BaseActivity<BookContentRawBean.BookContentResultBean> {
    private static final String TAG = "BookContentDetail";
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
    //字体调节对话框
    private FontSettingsDialog mFontDialog;


    @Override
    protected void initView() {
        getExtraFromIntent();
        setContentView(R.layout.bookshelf_activity_book_content_detail);
        initNavMenu();
        //初始化WebView
        mRvBookContent = findViewById(R.id.rv_book_content_detail);
        LinearLayoutManager manager = new LinearLayoutManager(
                this, LinearLayoutManager.VERTICAL, false);
        mRvBookContent.setLayoutManager(manager);
        //添加下拉底部加载下一步数据
        mRvBookContent.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                //判断当前RecyclerView是否滑到底部。加载更多的逻辑 >>>避免当前只有一条的情况
//                int childCount = recyclerView.getChildCount();
                if (!mRvBookContent.canScrollVertically(1)
                        && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    Log.d(TAG, "onScrollStateChanged: 当前滑动到底部 >>>> loadMore >>> ");
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
                mFontDialog = new FontSettingsDialog(this);
                mFontDialog.setOnFontListener(fontOption -> {
                    Log.d(TAG, "initNavMenu: fontOption >>> " + fontOption);
                    setFontSize(fontOption);
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
        //初始化Presenter
        IShelvesMvpContract.IPresenter mPresenter = new BookContentDetailPresenter(this);
        mPresenter.refresh(IShelvesMvpContract.REFRESH_BOOK_CONTENT_DETAIL,
                new String[]{String.valueOf(mShelfId),
                        String.valueOf(mCategoryId),
                        String.valueOf(mBookId), pageId});
    }


    @Override
    public void onRefreshFinished(String refreshType, List<BookContentRawBean.BookContentResultBean> result) {
        BookContentRawBean.BookContentResultBean bookContentResultBean = result.get(0);
        if (bookContentResultBean != null) {
            mNextPage = bookContentResultBean.getNext();
            if (mBookContentAdapter == null) {
                //首次加載
                mBookContentAdapter = new BookContentDetailAdapter(this, result);
                mRvBookContent.setAdapter(mBookContentAdapter);
            } else {
                //加載更多
                mBookContentAdapter.addContentEntity(bookContentResultBean);
            }
        }
    }

    /**
     * 刷新当前WebView内部的字体大小
     *
     * @param fontOption {@link FontSettingsDialog}類常量值定义
     */
    private void setFontSize(int fontOption) {
        switch (fontOption) {
            case FontSettingsDialog.FONT_SMALLER:
                mBookContentAdapter.setFontSize(BookShelfConstant.SMALLER);
                break;
            case FontSettingsDialog.FONT_STANDARD:
                mBookContentAdapter.setFontSize(BookShelfConstant.SMALLER);
                break;
            case FontSettingsDialog.FONT_LARGE:
                mBookContentAdapter.setFontSize(BookShelfConstant.LARGE);
                break;
            case FontSettingsDialog.FONT_LARGER:
                mBookContentAdapter.setFontSize(BookShelfConstant.LARGER);
                break;
            case FontSettingsDialog.FONT_LARGEST:
                mBookContentAdapter.setFontSize(BookShelfConstant.LARGEST);
                break;
            default:
                mBookContentAdapter.setFontSize(BookShelfConstant.SMALLER);
                break;
        }
    }

    @Override
    public void showTips(String message) {

    }

    @Override
    protected void onDestroy() {
        //防止WebView导致的内存泄露
        super.onDestroy();
    }
}
