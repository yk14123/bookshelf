package com.chinafocus.bookshelf.ui.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.NestedScrollView;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chinafocus.bookshelf.R;
import com.chinafocus.bookshelf.base.BaseActivity;
import com.chinafocus.bookshelf.global.BookShelfConstant;
import com.chinafocus.bookshelf.model.bean.BookMetadataRawBean;
import com.chinafocus.bookshelf.presenter.shelves.BookMetaDataPresenter;
import com.chinafocus.bookshelf.presenter.shelves.IShelvesMvpContract;
import com.chinafocus.bookshelf.ui.adapter.BookMetaParentAdapter;
import com.chinafocus.bookshelf.ui.widgets.ExpandableTextView;
import com.chinafocus.bookshelf.utils.ScreenUtils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

/**
 * 图书目录页面
 *
 * @author HUANG_JIN
 * @version 1.0
 * create at 2018/11/27 13:06
 */
public class BookMetaDataActivity extends BaseActivity<BookMetadataRawBean.BookMetadataResultBean> {
    private static final String TAG = "BookMetaData";

    //显示控件
    private ExpandableListView mExpandListView;
    //滑动控件
    private NestedScrollView mNestedScrollView;
    //悬浮控件
    private FloatingActionButton mFabBackTop;
    //点击查看更多文字
    private ExpandableTextView expandableTextView;
    //图书封面
    private ImageView mIvBookCover;
    //图书名称
    private TextView mTvBookTitle;
    //推荐语标签
    private TextView mTvBookComment;
    //Behavior
    private BottomSheetBehavior<NestedScrollView> sheetBehavior;

    //当前图书书柜id
    private int mShelfId;
    //当前图书id
    private int mBookId;
    //当前图书的分类
    private int mCategoryId;
    //当前图书的名称
    private String mBookName;
    //分类标签名
    private String mCategoryName;

    @Override
    protected void initView() {
        getExtraFromIntent();
        setContentView(R.layout.bookshelf_activity_book_meta_data);
        initNavMenu();

        mIvBookCover = findViewById(R.id.iv_book_meta_data_cover);
        mTvBookTitle = findViewById(R.id.tv_book_meta_data_title);
        mTvBookComment = findViewById(R.id.tv_book_meta_data_tag);
        mExpandListView = findViewById(R.id.elv_tree_test);
        mExpandListView.setFocusable(false);
        mNestedScrollView = findViewById(R.id.nsv_book_meta_data);
        mFabBackTop = findViewById(R.id.fab_back_top);
        mFabBackTop.setOnClickListener(v -> {
            //返回ExpandableListView的顶部
            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            mNestedScrollView.fullScroll(View.FOCUS_UP);
        });
        //分類名稱
        TextView mTvBookCategory = findViewById(R.id.tv_book_meta_data_category);
        if (!TextUtils.isEmpty(mCategoryName)) {
            mTvBookCategory.setText(mCategoryName);
        }
        expandableTextView = findViewById(R.id.expand_text_view);
        sheetBehavior = BottomSheetBehavior.from(mNestedScrollView);
        int screenHeight = ScreenUtils.getScreenHeight(this);
        sheetBehavior.setPeekHeight(screenHeight / 2);
        sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                Log.i(TAG, "onStateChanged newState>>> " + newState);
                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    Log.i(TAG, "bottomSheet collapsed >>>");
                    mFabBackTop.setVisibility(View.GONE);
                } else if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    Log.i(TAG, "bottomSheet expanded >>>");
                    mFabBackTop.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                Log.i(TAG, "sheetBehavior onSlide slideOffset>>> " + slideOffset);
            }
        });
        //初始化控制器
        IShelvesMvpContract.IPresenter mPresenter = new BookMetaDataPresenter(this);
        mPresenter.refresh(IShelvesMvpContract.REFRESH_BOOK_METADATA,
                new String[]{String.valueOf(mShelfId),
                        String.valueOf(mCategoryId),
                        String.valueOf(mBookId)});
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
            mCategoryName = intent.getStringExtra(BookShelfConstant.CATEGORY_NAME);
            Log.d(TAG, "getExtraFromIntent: mShelfId >>>" + mShelfId
                    + " mBookId >>> " + mBookId + " mCategoryId >>> "
                    + mCategoryId + " mBookName >>> " + mBookName
                    + " mCategoryName >>> " + mCategoryName);
        }
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
        mIvRightMenu.setVisibility(View.GONE);
    }


    @Override
    public void onRefreshFinished(String refreshType, List<BookMetadataRawBean.BookMetadataResultBean> result) {
        Log.d(TAG, "onRefreshFinished: result.size() >>> " + result.size());
        BookMetadataRawBean.BookMetadataResultBean dataBean = result.get(0);
        if (dataBean != null) {
            //图书名称
            String title = dataBean.getTitle();
            if (!TextUtils.isEmpty(title)) {
                mTvBookTitle.setText(title);
            }
            //图书封面
            String cover = dataBean.getCover();
            Glide.with(this)
                    .load(cover)
                    .apply(new RequestOptions()
                            .placeholder(R.drawable.bookshelf_default_cover_port)
                            .error(R.drawable.bookshelf_default_cover_port))
                    .into(mIvBookCover);
            //图书推荐语
            String comment = dataBean.getComment();
            if (!TextUtils.isEmpty(comment)) {
                Log.i(TAG, "comment >>> " + comment);
                mTvBookComment.setText(getString(R.string.bookshelf_book_comment));
                expandableTextView.setText(Html.fromHtml(comment));
            } else {
                mTvBookComment.setText(getString(R.string.bookshelf_book_intro));
                String description = dataBean.getDescription();
                if (!TextUtils.isEmpty(description)) {
                    expandableTextView.setText(Html.fromHtml(description));
                }
            }


            //目录数据
            List<BookMetadataRawBean.BookMetadataResultBean.TocBean> toc = dataBean.getToc();
            Log.d(TAG, "onRefreshFinished: the size >>> " + toc.size());
            if (toc.size() != 0) {
                ArrayList<BookMetadataRawBean.BookMetadataResultBean.TocBean> tocBeans = new ArrayList<>();
                //去除数据完毕
                for (int i = 0; i < toc.size(); i++) {
                    BookMetadataRawBean.BookMetadataResultBean.TocBean tocBean = toc.get(i);
                    Log.d(TAG, "onRefreshFinished: visible >>> " + tocBean.getVisible() + " ...title >>>" + tocBean.getTitle());
                    if (tocBean.getVisible() == 1) {
                        tocBeans.add(tocBean);
                    }
                }
                BookMetaParentAdapter adapter = new BookMetaParentAdapter(this, tocBeans);
                mExpandListView.setAdapter(adapter);
                mExpandListView.setGroupIndicator(null);
                //设置内部item的点击事件
                adapter.setBookMetaListener(new BookMetaParentAdapter.OnBookMetaListener() {
                    @Override
                    public void onChild(int groupPosition, int childPosition,
                                        BookMetadataRawBean.BookMetadataResultBean.TocBean childrenBeanX) {
                        if (childrenBeanX != null) {
                            String full = childrenBeanX.getFull();
                            startIntentForContent(full);
                            Log.i(TAG, "onGroupClick  full >>>" + full);
                        }
                    }

                    @Override
                    public void onItem(BookMetadataRawBean.BookMetadataResultBean.TocBean childrenBean) {
                        if (childrenBean != null) {
                            String full = childrenBean.getFull();
                            startIntentForContent(full);
                            Log.i(TAG, "onGroupClick  full >>>" + full);
                        }
                    }
                });

                //Group的点击监听事件
                mExpandListView.setOnGroupClickListener((parent, v, groupPosition, id) -> {
                    BookMetadataRawBean.BookMetadataResultBean.TocBean tocBean = toc.get(groupPosition);
                    if (tocBean != null) {
                        String href = tocBean.getFull();
                        startIntentForContent(href);
                    }
                    return true;
                });
                //将ExpandableListView内部的group全部显示
                int count = mExpandListView.getCount();
                Log.i(TAG, "getCount >>> " + count);
                for (int i = 0; i < count; i++) {
                    mExpandListView.expandGroup(i);
                }
            } }
    }

    /**
     * 跳转内容详情界面
     */
    public void startIntentForContent(String page) {
        Intent intent = new Intent(this, BookContentDetailActivity.class);
        intent.putExtra(BookShelfConstant.SHELF_ID, mShelfId);
        intent.putExtra(BookShelfConstant.CATEGORY_ID, mCategoryId);
        intent.putExtra(BookShelfConstant.BOOK_ID, mBookId);
        intent.putExtra(BookShelfConstant.BOOK_NAME, mBookName);
        intent.putExtra(BookShelfConstant.PAGE, page);
        startActivity(intent);
    }

    @Override
    public void showTips(String message) {

    }
}
