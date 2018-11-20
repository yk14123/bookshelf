package com.chinafocus.bookshelf.ui.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.chinafocus.bookshelf.R;
import com.chinafocus.bookshelf.model.bean.ShelvesResultBean;
import com.chinafocus.bookshelf.presenter.shelves.IShelvesMvpContract;
import com.chinafocus.bookshelf.presenter.shelves.ShelvesPresenter;

import java.lang.reflect.Method;
import java.util.List;

public class ShelvesActivity extends AppCompatActivity implements IShelvesMvpContract.IView<ShelvesResultBean> {

    private CoordinatorLayout mRootLayout;
    private RecyclerView mRecyclerView;
    //    private NewsMvpAdapter mRecyclerAdapter;
//    private List<NewsBean> mNewsBeans = new ArrayList<>();
    private IShelvesMvpContract.IPresenter mPresenter;
    private LinearLayoutManager mLayoutMgr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        hideBottomUIMenu();
//        int flag = DISABLE_HOME | DISABLE_RECENT | DISABLE_BACK | DISABLE_EXPAND;
//        setStatusBarDisable(flag);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bookshelf_activity_mvp);
        initView();

        Toast.makeText(this, "哈哈，我成功启动了！", Toast.LENGTH_LONG).show();
    }


    public static final int DISABLE_EXPAND = 0x00010000;//4.2以上的整形标识
    public static final int DISABLE_EXPAND_LOW = 0x00000001;//4.2以下的整形标识
    public static final int DISABLE_NONE = 0x00000000;//取消StatusBar所有disable属性，即还原到最最原始状态

    public static final int DISABLE_HOME = 0x00200000; //二进制的值是0x00200000
    public static final int DISABLE_RECENT = 0x01000000; //二进制的值是 0x01000000
    public static final int DISABLE_BACK = 0x00400000; //二进制的值是 0x00400000

    private void setStatusBarDisable(int disable_status) {
        //调用statusBar的disable方法
        @SuppressLint("WrongConstant")
        Object service = getSystemService("statusbar");
        try {
            Class<?> statusBarManager = Class.forName("android.app.StatusBarManager");
            Method expand = statusBarManager.getMethod("disable", int.class);
            expand.invoke(service, disable_status);
        } catch (Exception e) {
//            unBanStatusBar();
            e.printStackTrace();
        }
    }

//    // this is to start to be visible!
//    @Override
//    public void onWindowFocusChanged(boolean hasFocus)
//    {
//        super.onWindowFocusChanged(hasFocus);
//
//        getWindow().getDecorView().setSystemUiVisibility(
//                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                        | View.SYSTEM_UI_FLAG_FULLSCREEN
//                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
//    }

    /**
     * 隐藏虚拟按键，并且全屏
     */
    protected void hideBottomUIMenu() {

//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
//
//        //隐藏虚拟按键，并且全屏
//        //for new api versions.
//        View decorView = getWindow().getDecorView();
//        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
//        decorView.setSystemUiVisibility(uiOptions);


        final View decorView = getWindow().getDecorView();
        final int uiOption = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;

        decorView.setSystemUiVisibility(uiOption);

        // This code will always hide the navigation bar
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                    decorView.setSystemUiVisibility(uiOption);
                }
            }
        });

    }

    private void initView() {
        mRootLayout = findViewById(R.id.cl_root);
        mRecyclerView = findViewById(R.id.rv_news);
//        mRecyclerAdapter = new NewsMvpAdapter();
//        mRecyclerAdapter.setNewsResult(mNewsBeans);
//        mLayoutMgr = new LinearLayoutManager(this);
//        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
//        mRecyclerView.setLayoutManager(mLayoutMgr);
//        mRecyclerView.setAdapter(mRecyclerAdapter);
        mPresenter = new ShelvesPresenter(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        dispatchRefresh();
    }

    private void dispatchRefresh() {
        mPresenter.refresh(IShelvesMvpContract.REFRESH_SHELVES, null);
    }


    @Override
    public void onRefreshFinished(String refreshType, List<ShelvesResultBean> resultBean) {
        //        mNewsBeans.clear();
//        mNewsBeans.addAll(newsBeans);
//        mRecyclerAdapter.notifyDataSetChanged();
        if (resultBean != null) {
            Toast.makeText(this, "成功了 --> " + resultBean.get(0).getName(), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void showTips(String message) {
        Snackbar.make(mRootLayout, message, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.destroy();
    }
}
