package com.chinafocus.bookshelf.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chinafocus.bookshelf.R;
import com.chinafocus.bookshelf.bean.BookMetadataRawBean.BookMetadataResultBean;
import com.chinafocus.bookshelf.bean.BookMetadataRawBean.BookMetadataResultBean.TocBean;
import com.chinafocus.bookshelf.global.BookShelfConstant;
import com.chinafocus.bookshelf.model.base.activity.BaseActivity;
import com.chinafocus.bookshelf.presenter.shelves.BookMetaDataPresenter;
import com.chinafocus.bookshelf.presenter.shelves.IShelvesMvpContract;
import com.chinafocus.bookshelf.presenter.statistics.StatisticsPresenter;
import com.chinafocus.bookshelf.ui.adapter.BookNodeAdapterYangRV;
import com.chinafocus.bookshelf.ui.widgets.MyOverByRecyclerView;
import com.chinafocus.bookshelf.utils.SpUtil;
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
public class BookMetaDataActivityYangRV extends BaseActivity<BookMetadataResultBean> implements View.OnClickListener {
    private static final String TAG = "YangRV";
    private PercentRelativeLayout mRlAppBar;
    private BookMetaDataPresenter mPresenter;
    //错误视图
    private PercentLinearLayout mLlErrorLayout;
    //目录列表容器
//    private RecyclerView mRvMetaData;
    private MyOverByRecyclerView mRvMetaData;

    private BookNodeAdapterYangRV mBookNodeAdapter;
    //滑动控件
    private NestedScrollView mNestedScrollView;
    //悬浮控件
    private ImageView mIvBackTop;

    private String mCoverUrl;
    //    private BookCoverDialog mBookCoverDialog;
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
    //图书封面
    private ImageView mIvBookCover;

//    private String[] mHeaderContent = new String[4];

    private ArrayList<String> mHeaderContent = new ArrayList<>();

    private String mOriginId;
    private boolean mIsSmoothMoveTop;
    private ImageView mBookCoverBg;

    private Animation mBookCoverBg_Scale_out, mBookCoverBg_Scale_in;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void initView() {

        getExtraFromIntent();
        setContentView(R.layout.bookshelf_activity_book_meta_data_yang_rv);

//        SpUtil.setString(getApplicationContext(), BookShelfConstant.BOOK_INIT_LOCATION_ID, "expressreader");

        mOriginId = SpUtil.getString(getApplicationContext(), BookShelfConstant.BOOK_INIT_LOCATION_ID);

        //初始化BookCoverBg展示动画
        initAnimBg();

        initNavMenu();
        //无数据视图
        mLlErrorLayout = findViewById(R.id.ll_bookshelf_reconnect_net);
        mLlErrorLayout.setOnClickListener(this);

        //点击展示整个图书封面
        initBookCoverBg();

        //封面点击事件
        initViewHeaderWrapper();
        //初始化图书信息HeaderExpand标题及内容
//        initExpandText();

        //返回顶部
        iniRollbackTop();

        //初始化多级目录信息
        initRvMetaData();


        //加载数据
        loadBookMeta();

    }

    private void initAnimBg() {
        mBookCoverBg_Scale_out = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bookshelf_view_scale_out);
        mBookCoverBg_Scale_in = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bookshelf_view_scale_in);
    }

    private void initBookCoverBg() {
        mBookCoverBg = findViewById(R.id.iv_book_meta_data_show);

        mBookCoverBg.setOnClickListener(v -> handleBookCoverBgAnimOut(mBookCoverBg, mBookCoverBg_Scale_out, 200));

        handleBookCoverBgAnimOut(mBookCoverBg, mBookCoverBg_Scale_out, 0);

    }

    private void handleBookCoverBgAnimOut(ImageView view, Animation animation, long time) {
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.clearAnimation();
                view.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        //等会这里设置0试试
        animation.setDuration(time);

        view.startAnimation(animation);
    }

    private void iniRollbackTop() {
        mIvBackTop = findViewById(R.id.iv_book_meta_back_top_rv_yang);
        mIvBackTop.setOnClickListener(this);
    }

    private void initViewHeaderWrapper() {
        mIvBookCover = findViewById(R.id.iv_book_meta_data_cover_yang_rv);
    }


    @SuppressLint("ClickableViewAccessibility")
    private void initRvMetaData() {
        mRvMetaData = findViewById(R.id.rv_book_meta_data_yang_rv);
        LinearLayoutManager manager = new LinearLayoutManager(
                this, LinearLayoutManager.VERTICAL, false);
        manager.setSmoothScrollbarEnabled(true);

        mRvMetaData.setLayoutManager(manager);

        mRvMetaData.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if (layoutManager != null) {
                    int position = layoutManager.findFirstVisibleItemPosition();

                    mIsSmoothMoveTop = position < 30;

                    View firstVisibleChildView = layoutManager.findViewByPosition(position);
                    if (firstVisibleChildView != null) {
                        int itemHeight = firstVisibleChildView.getHeight();
                        int i = (position) * itemHeight - firstVisibleChildView.getTop();
                        if (i > 0) {
                            mIvBackTop.setVisibility(View.VISIBLE);
                        } else {
                            mIvBackTop.setVisibility(View.GONE);
                        }
                    }

                }
            }
        });


//        mRvMetaData.setOnTouchListener(new View.OnTouchListener() {
//
//            private static final int MOVE_VERTIFY = 10;
//            //可以延伸到屏幕的四分之一
//            private static final int DEFAULT_DEVIDE = 3;
//
//            //recyclerView_thumbnail的padding
//            private int paddingTop;
//            private int paddingBottom;
//            private int paddingLeft;
//            private int paddingRight;
//
//            float downTouch;
//
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//
//                switch (event.getAction()) {
//                    case MotionEvent.ACTION_DOWN:
////                downY = event.getY();
//////                    Log.d("zbv","downY="+downY);
//                        break;
//                    case MotionEvent.ACTION_MOVE:
//                        //这样写是因为无法监听到down事件所以第一次move事件的坐标作为down
//
//                        if (mIsCanOverTouch) {
//                            //消除第一次downX和moveX不一致
//                            downTouch = event.getY();
//                            mIsCanOverTouch = false;
//                            return false;
//                        }
//                        float moveTouch = 0;
//                        moveTouch = event.getY();
//                        if (!mRvMetaData.canScrollVertically(-1)) {
//                            if ((moveTouch - downTouch) >= MOVE_VERTIFY) {
//                                int deltY = (int) (moveTouch - downTouch) / DEFAULT_DEVIDE;
//                                mRvMetaData.setPadding(mRvMetaData.getPaddingLeft(), mRvMetaData.getPaddingTop() + deltY, mRvMetaData.getPaddingRight(),
//                                        mRvMetaData.getPaddingBottom());
//                            } else if ((moveTouch - downTouch) <= -MOVE_VERTIFY) {
//                                mRvMetaData.setPadding(mRvMetaData.getPaddingLeft(), paddingTop, mRvMetaData.getPaddingRight(), paddingBottom);
//                            }
//                        } else if (!mRvMetaData.canScrollVertically(1)) {
//                            if ((downTouch - moveTouch) >= MOVE_VERTIFY) {
//                                int deltY = (int) (downTouch - moveTouch) / DEFAULT_DEVIDE;
//                                mRvMetaData.setPadding(mRvMetaData.getPaddingLeft(), mRvMetaData.getPaddingTop(), mRvMetaData.getPaddingRight(),
//                                        mRvMetaData.getPaddingBottom() + deltY);
//                            } else if ((downTouch - moveTouch) <= -MOVE_VERTIFY) {
//                                mRvMetaData.setPadding(mRvMetaData.getPaddingLeft(), paddingTop, mRvMetaData.getPaddingRight(), paddingBottom);
//                            }
//                        } else {
//                            mRvMetaData.setPadding(mRvMetaData.getPaddingLeft(), paddingTop, mRvMetaData.getPaddingRight(), paddingBottom);
//                        }
//
//                        //防止在既不是顶部也不是底部时的闪烁
//                        downTouch = moveTouch;
//                        break;
//                    case MotionEvent.ACTION_CANCEL:
//                    case MotionEvent.ACTION_UP:
//                        mRvMetaData.setPadding(mRvMetaData.getPaddingLeft(), paddingTop, mRvMetaData.getPaddingRight(), paddingBottom);
//                        mIsCanOverTouch = true;
//                        break;
//                }
//                return false;
//            }
//        });

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
//            mBookId = 102;
//            mCategoryId = 17;
//            mCategoryTagName = "习近平著作";
//            mBookName = "习近平著作标题";
//            mShelfId >>>2 mBookId >>> 185 mCategoryId >>> 16 mCategoryTagName >>> 习近平著作
//            mShelfId >>>2 mBookId >>> 189 mCategoryId >>> 13 mCategoryTagName >>> 经史典集

//            mShelfId = 2;
//            mBookId = 181;
//            mCategoryId = 14;
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

        mRlAppBar.setOnClickListener(v -> Log.i("abc", "abc"));

        //Back键
        ImageView mIvBack = findViewById(R.id.iv_bookshelf_left_menu);
        mIvBack.setOnClickListener(v -> {
            if (mBookCoverBg.getVisibility() == View.VISIBLE) {

                handleBookCoverBgAnimOut(mBookCoverBg, mBookCoverBg_Scale_out, 200);

            } else finish();
        });
        //初始化标题
        //当前图书的名称
        TextView mTvBookNavTitle = findViewById(R.id.tv_bookshelf_title);
        if (!TextUtils.isEmpty(mBookName))
            mTvBookNavTitle.setText(mBookName);

        mTvBookNavTitle.setOnClickListener(v -> Log.i("abc", "abc"));
        //右側menu
        ImageView mIvRightMenu = findViewById(R.id.iv_bookshelf_right_menu);
        mIvRightMenu.setVisibility(View.INVISIBLE);
//        mIvRightMenu.setOnClickListener(v -> Log.i("abc", "abc"));
    }


    @SuppressLint("CheckResult")
    @Override
    public void onRefreshFinished(String refreshType, List<BookMetadataResultBean> result) {
        Log.d(TAG, "showTips: onRefreshFinished >>> ");
        BookMetadataResultBean dataBean = result.get(0);
        if (dataBean != null) {
            showRefreshLayout(false);

            //图书封面
            mCoverUrl = dataBean.getCover();

            Log.i(TAG, "mCoverUrl-->" + mCoverUrl);

            initBgView(mIvBookCover);

            //图书名称
            String mBookTitle = dataBean.getName();
            if (!TextUtils.isEmpty(mBookTitle)) {
                if (!TextUtils.isEmpty(mCategoryTagName)) {
//                    mTvBookTitle.setContentAndTag(mBookTitle, mCategoryTagName);
//                    mTv_expand_title.setText(mBookTitle);
//                    mTvCategoryTag.setText(mCategoryTagName);

                    mHeaderContent.add(mBookTitle);
                    mHeaderContent.add(mCategoryTagName);
//                    mHeaderContent[0] = mBookTitle;
//                    mHeaderContent[1] = mCategoryTagName;
                }
            }


            //图书推荐语
            String comment = dataBean.getComment();
            if (!TextUtils.isEmpty(comment)) {
                Log.i(TAG, "comment >>> " + comment);
//                mExpandText.setTextWithTag(Html.fromHtml(comment), getString(R.string.bookshelf_book_comment));

                mHeaderContent.add("推荐语");
                mHeaderContent.add(comment);
//                mHeaderContent[2] = "推荐语";
//                mHeaderContent[3] = comment;
            } else {
                String description = dataBean.getDescription();
                if (!TextUtils.isEmpty(description)) {
                    Log.i(TAG, "description >>> " + description);
//                    mExpandText.setTextWithTag(Html.fromHtml(description), getString(R.string.bookshelf_book_intro));
                    mHeaderContent.add("简介");
                    mHeaderContent.add(description);
//                    mHeaderContent[2] = "简介";
//                    mHeaderContent[3] = description;
                }
            }

            List<TocBean> toc = dataBean.getToc();
            //获取数据
            ArrayList<TocBean> mBaseNodes = mPresenter.getTocList(toc, -1);

//            /**
//             * 客户端要做分页优化！！！
//             */
//            ArrayList<TocBean> finalBaseNode;
//            if (baseNodes.size() > 99) {
//                List<TocBean> subBaseNodes = baseNodes.subList(0, 10);
//                finalBaseNode = new ArrayList<>(subBaseNodes);
//            } else {
//                finalBaseNode = baseNodes;
//            }

            if (mBookNodeAdapter == null) {
                mBookNodeAdapter = new BookNodeAdapterYangRV(this, mBaseNodes, mHeaderContent);
                initBookNodeAdapter();
            }
            mRvMetaData.setAdapter(mBookNodeAdapter);
        } else {
            showRefreshLayout(true);
        }
        dismissLoading();
    }

    @Override
    public void onBackPressed() {


        if (mBookCoverBg.getVisibility() == View.VISIBLE) {
            handleBookCoverBgAnimOut(mBookCoverBg, mBookCoverBg_Scale_out, 200);
            return;
        }

        super.onBackPressed();
    }

    private void initBookNodeAdapter() {
        mBookNodeAdapter.setOnDialogShowListener(new BookNodeAdapterYangRV.OnDialogShowListener() {
            @Override
            public void onDialogShowClick(View v) {
//                if (!TextUtils.isEmpty(mCoverUrl)) {
//                    if (mBookCoverDialog == null) {
//                        mBookCoverDialog = new BookCoverDialog(BookMetaDataActivityYangRV.this, mRlAppBar);
//                        mBookCoverDialog.setImageUrl(mCoverUrl);
//                    }
//                    if (!mBookCoverDialog.isShowing()) {
//                        mBookCoverDialog.show();
//                    }
//                }

                //点击放大背景图
                mBookCoverBg_Scale_in.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        mBookCoverBg.setVisibility(View.VISIBLE);
                        mBookCoverBg.setBackgroundColor(Color.BLACK);
                        initBgView(mBookCoverBg);
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        mBookCoverBg.clearAnimation();
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                //等会这里设置0试试
                mBookCoverBg_Scale_in.setDuration(200);

                mBookCoverBg.startAnimation(mBookCoverBg_Scale_in);


            }
        });

        mBookNodeAdapter.setBookNodeClickListener(new BookNodeAdapterYangRV.OnBookNodeClickListener() {
            @Override
            public void onNodeClick(String label, String pageId, String title) {

                String str = mBookId + "!" + title;

                Log.i("StatisticsType", "StatisticsType  last -->" + str);
//                    StatisticsType.postStatisticsNow(getApplicationContext(), "4", str);
                StatisticsPresenter.getInstance().startStatistics(getApplicationContext(), "4", str);
                UIHelper.startContentDetailActivity(BookMetaDataActivityYangRV.this,
                        mShelfId, mCategoryId, mBookId, label, pageId);
            }
        });

    }

    @SuppressLint("CheckResult")
    private void initBgView(ImageView view) {
        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.bookshelf_default_cover_port)
                .error(R.drawable.bookshelf_default_cover_port);

        Glide.with(this)
                .load(mCoverUrl)
                .apply(requestOptions)
                .into(view);

//        mIvBookCover
    }

    @Override
    public void showTips(String message) {
        dismissLoading();
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        showRefreshLayout(true);
    }

    private void showRefreshLayout(boolean showRefresh) {
        mLlErrorLayout.setVisibility(showRefresh ? View.VISIBLE : View.GONE);
//        mNestedScrollView.setVisibility(showRefresh ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        if (v == mIvBackTop) {
            if (mIsSmoothMoveTop) {
                mRvMetaData.smoothScrollToPosition(0);
            } else {
                mRvMetaData.scrollToPosition(0);
            }
        } else if (v == mLlErrorLayout) {
            mLlErrorLayout.setVisibility(View.GONE);
            loadBookMeta();
        }
    }

    @Override
    protected void onDestroy() {

        if (mBookNodeAdapter != null) {
            mBookNodeAdapter.clear();
        }

        if (mPresenter != null) {
            mPresenter.destroy();
        }

        super.onDestroy();

    }
}
