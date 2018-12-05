package com.chinafocus.bookshelf.ui.activity;

import android.content.Intent;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chinafocus.bookshelf.R;
import com.chinafocus.bookshelf.base.BaseActivity;
import com.chinafocus.bookshelf.global.BookShelfConstant;
import com.chinafocus.bookshelf.model.bean.BookMetadataRawBean;
import com.chinafocus.bookshelf.model.bean.BookMetadataRawBean.BookMetadataResultBean.TocBean;
import com.chinafocus.bookshelf.presenter.shelves.BookMetaDataPresenter;
import com.chinafocus.bookshelf.presenter.shelves.IShelvesMvpContract;
import com.chinafocus.bookshelf.ui.adapter.BookNodeAdapter;
import com.chinafocus.bookshelf.ui.dialog.BookCoverDialog;
import com.chinafocus.bookshelf.ui.widgets.BottomViewDragBehavior;
import com.chinafocus.bookshelf.ui.widgets.ExpandableTextView;
import com.chinafocus.bookshelf.ui.widgets.TagTextView;
import com.chinafocus.bookshelf.utils.ScreenUtils;
import com.chinafocus.bookshelf.utils.UIHelper;
import com.zhy.android.percent.support.PercentLinearLayout;
import com.zhy.android.percent.support.PercentRelativeLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * 图书目录页面
 *
 * @author HUANG_JIN
 * @version 1.0
 * create at 2018/11/27 13:06
 */
public class BookMetaDataActivity extends BaseActivity<BookMetadataRawBean.BookMetadataResultBean> {
    private static final String TAG = "BookMetaData";
    private PercentRelativeLayout mRlAppBar;
    private BookMetaDataPresenter mPresenter;
    //目录列表容器
    private PercentLinearLayout mLlBookMetaWrapper;
    private RecyclerView mRvMetaData;
    private BookNodeAdapter mBookNodeAdapter;
    //滑动控件
    private NestedScrollView mNestedScrollView;
    //悬浮控件
    private ImageView mIvBackTop;
    //卡片容器
    private CardView mCvBookHeaderWrapper;
    //点击查看更多文字
    private ExpandableTextView mExpandText;
    //图书封面
    private ImageView mIvBookCover;
    private String mCoverUrl;
    private BookCoverDialog mBookCoverDialog;
    //当前图书的名称
    private TextView mTvBookNavTitle;
    private TagTextView mTvBookTitle;
    private String mBookTitle;
    //Behavior
    private BottomViewDragBehavior<NestedScrollView> mViewDragBehavior;
    //当前图书书柜id
    private int mShelfId;
    //当前图书id
    private int mBookId;
    //当前图书的分类
    private int mCategoryId;
    //分类标签名
    private String mCategoryTagName;


    @Override
    protected void initView() {
        getExtraFromIntent();
        setContentView(R.layout.bookshelf_activity_book_meta_data);
        initNavMenu();
        //封面点击事件
        mIvBookCover = findViewById(R.id.iv_book_meta_data_cover);
        mIvBookCover.setOnClickListener(v -> {
            if (!TextUtils.isEmpty(mCoverUrl)) {
                if (mBookCoverDialog == null) {
                    mBookCoverDialog = new BookCoverDialog(BookMetaDataActivity.this, mRlAppBar);
                    mBookCoverDialog.setImageUrl(mCoverUrl);
                }
                if (!mBookCoverDialog.isShowing()) {
                    mBookCoverDialog.show();
                }
            }
        });
        //图书信息
        mTvBookTitle = findViewById(R.id.tv_book_meta_data_title);
        //目录信息
        mRvMetaData = findViewById(R.id.rv_book_meta_data);
        LinearLayoutManager manager = new LinearLayoutManager(
                this, LinearLayoutManager.VERTICAL, false);
        mRvMetaData.setLayoutManager(manager);
        mRvMetaData.setHasFixedSize(true);//当前条目固定的情况下，设置此属性，提高RecyclerView的性能
        mRvMetaData.setFocusable(false);//取消RecyclerView获取焦点事件，避免NestedScrollView无法滑动到顶部的问题
        mRvMetaData.setNestedScrollingEnabled(false);//禁止RecyclerView的滑动事件，交给NestedScrollView
        //返回顶部
        mIvBackTop = findViewById(R.id.iv_book_meta_back_top);
        mIvBackTop.setOnClickListener(v -> {
            mViewDragBehavior.setState(BottomViewDragBehavior.STATE_COLLAPSED);
            mNestedScrollView.fullScroll(View.FOCUS_UP);
        });
        //更多&收起
        mCvBookHeaderWrapper = findViewById(R.id.cv_book_meta_header);  //卡片布局
        mLlBookMetaWrapper = findViewById(R.id.ll_book_meta_root);//padding值
        mExpandText = findViewById(R.id.expand_text_view);
        mExpandText.setOnExpandStateChangeListener((textView, isExpanded) -> {
            Log.d(TAG, "expandState changed >>> isExpanded >>> " + isExpanded);
            requestNestedLayout();
        });
        //底部拖动View
        mNestedScrollView = findViewById(R.id.nsv_book_meta_data);
        mViewDragBehavior = BottomViewDragBehavior.from(mNestedScrollView);
        int screenHeight = ScreenUtils.getScreenHeight(this);
        mViewDragBehavior.setPeekHeight(screenHeight / 2);
        mNestedScrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener)
                (nestedScrollView, i, i1, i2, i3) -> {
                    //判断当前NestedScrollView是否滑动到顶部
                    if (mNestedScrollView.getScrollY() == 0) {
                        setFloatingButtonState(true);
                    } else {
                        setFloatingButtonState(false);
                    }
                });

        //初始化控制器
        mPresenter = new BookMetaDataPresenter(this);
        mPresenter.refresh(IShelvesMvpContract.REFRESH_BOOK_METADATA,
                new String[]{String.valueOf(mShelfId),
                        String.valueOf(mCategoryId),
                        String.valueOf(mBookId)});
    }

    /**
     * 动态设定目录信息容器的paddingTop值
     */
    private void requestNestedLayout() {
        int height = mCvBookHeaderWrapper.getHeight();
        Log.d(TAG, "requestNestedLayout: the height >>> " + height);
        mLlBookMetaWrapper.setPadding(0, height, 0, 0);
        mNestedScrollView.requestLayout();
    }

    /**
     * 設置悬浮按钮的显示和隐藏状态
     *
     * @param reachTop 当前NestedScrollView是否滑动到顶部
     */
    private void setFloatingButtonState(boolean reachTop) {
        if (reachTop) {
            if (mIvBackTop.getVisibility() == View.VISIBLE) {
                mIvBackTop.setVisibility(View.GONE);
            }
        } else {
            if (mIvBackTop.getVisibility() == View.GONE) {
                mIvBackTop.setVisibility(View.VISIBLE);
            }
        }
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
            Log.d(TAG, "getExtraFromIntent: mShelfId >>>" + mShelfId
                    + " mBookId >>> " + mBookId + " mCategoryId >>> "
                    + mCategoryId + " mCategoryTagName >>> " + mCategoryTagName);
        }
    }

    /**
     * 初始化通用的ActionBar布局
     */
    private void initNavMenu() {
        mRlAppBar = findViewById(R.id.tb_book_category_bar);
        //Back键
        ImageView mIvBack = findViewById(R.id.iv_bookshelf_left_menu);
        mIvBack.setOnClickListener(v -> finish());
        //初始化标题
        mTvBookNavTitle = findViewById(R.id.tv_bookshelf_title);
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
            mBookTitle = dataBean.getTitle();
            if (!TextUtils.isEmpty(mBookTitle)) {
                mTvBookNavTitle.setText(mBookTitle);
                if (!TextUtils.isEmpty(mCategoryTagName)) {
                    mTvBookTitle.setContentAndTag(mBookTitle, mCategoryTagName);
                }
            }
            //图书封面
            mCoverUrl = dataBean.getCover();
            Glide.with(this)
                    .load(mCoverUrl)
                    .apply(new RequestOptions()
                            .placeholder(R.drawable.bookshelf_default_cover_land)
                            .error(R.drawable.bookshelf_default_cover_land))
                    .into(mIvBookCover);
            //图书推荐语
            String comment = dataBean.getComment();
            if (!TextUtils.isEmpty(comment)) {
                Log.i(TAG, "comment >>> " + comment);
                mExpandText.setTextWithTag(Html.fromHtml(comment),
                        getString(R.string.bookshelf_book_comment));
            } else {
                String description = dataBean.getDescription();
                if (!TextUtils.isEmpty(description)) {
                    mExpandText.setTextWithTag(Html.fromHtml(description),
                            getString(R.string.bookshelf_book_intro));
                }
            }
            //此处在设置完成数据之后，设置延迟500ms之后执行paddingTop的自适应刷新操作
            mLlBookMetaWrapper.postDelayed(this::requestNestedLayout, 500);

            List<TocBean> toc = dataBean.getToc();
            //获取数据
            ArrayList<TocBean> baseNodes = mPresenter.getTocList(toc, -1);
            if (mBookNodeAdapter == null) {
                mBookNodeAdapter = new BookNodeAdapter(this, baseNodes);
            }
            mBookNodeAdapter.setBookNodeClickListener(pageId ->
                    UIHelper.startContentDetailActivity(BookMetaDataActivity.this,
                            mShelfId, mCategoryId, mBookId, mBookTitle, pageId));
            mRvMetaData.setAdapter(mBookNodeAdapter);
        }
    }

    @Override
    public void showTips(String message) {

    }

    /**
     * 复写finish方法，检测PopupWindow是否显示，显示dismiss掉
     */
    @Override
    public void finish() {
        if (mBookCoverDialog != null && mBookCoverDialog.isShowing()) {
            mBookCoverDialog.dismiss();
            Log.d(TAG, "finish: dialog is showing...");
        } else {
            Log.d(TAG, "finish: dialog is dismiss...");
            super.finish();
        }
    }
}
