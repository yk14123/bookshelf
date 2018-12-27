package com.chinafocus.bookshelf.ui.activity;

import android.content.DialogInterface;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.chinafocus.bookshelf.R;
import com.chinafocus.bookshelf.bean.ShelvesResultBean;
import com.chinafocus.bookshelf.global.BookShelfConstant;
import com.chinafocus.bookshelf.model.base.activity.BaseActivity;
import com.chinafocus.bookshelf.presenter.shelves.IShelvesMvpContract;
import com.chinafocus.bookshelf.presenter.shelves.ShelvesPresenter;
import com.chinafocus.bookshelf.utils.ManifestUtils;
import com.chinafocus.bookshelf.utils.SpUtil;
import com.chinafocus.bookshelf.utils.UIHelper;
import com.jakewharton.rxbinding2.view.RxView;
import com.zhy.android.percent.support.PercentFrameLayout;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;


public class ShelvesActivity extends BaseActivity<ShelvesResultBean> {

    private IShelvesMvpContract.IPresenter mPresenter;
    private String mOriginId;

    private boolean mIsNetWorkErro = true;
    /**
     * 网络错误的处理
     */
    private PercentFrameLayout mLlErrorLayout;
    private Disposable mErrorRetry_clicks;
    private TextView mTvErrorExit;

    private AlertDialog mExitDialog;
    private View mLocation_exit_view;
    private EditText mExit_id;
    private String mExitPass;

    @Override
    protected void initView() {

        setContentView(R.layout.bookshelf_activity_shelves_home);

        mOriginId = SpUtil.getString(getApplicationContext(), BookShelfConstant.BOOK_INIT_LOCATION_ID);

        initExitDialogView();
        //网络错误重试
        initErrorRetry();

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (mIsNetWorkErro) {

                    //初始化Presenter请求数据
                    loadShelfId();
                    SystemClock.sleep(3000);
                }
            }
        }).start();

    }

    private void loadShelfId() {
        //初始化控制器
        if (mPresenter == null) {
            mPresenter = new ShelvesPresenter(this);
        }

//        mPresenter.refresh(IShelvesMvpContract.REFRESH_SHELVES, new String[]{"expressreader"});
        mPresenter.refresh(IShelvesMvpContract.REFRESH_SHELVES, new String[]{mOriginId});
    }


    @Override
    public void onRefreshFinished(String refreshType, List<ShelvesResultBean> resultBean) {
        if (resultBean != null) {
            showRefreshLayout(false);
            int shelfId = resultBean.get(0).getShelfId();

            UIHelper.startShelfDetailActivity(this, shelfId);
            finish();
        } else {
            showRefreshLayout(true);
        }

    }


    @Override
    public void showTips(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        showRefreshLayout(true);
    }

    private void showRefreshLayout(boolean showRefresh) {
        mIsNetWorkErro = showRefresh;
        mLlErrorLayout.setVisibility(showRefresh ? View.VISIBLE : View.GONE);
//        mRlShelfRoot.setVisibility(showRefresh ? View.GONE : View.VISIBLE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mErrorRetry_clicks != null && mErrorRetry_clicks.isDisposed()) {
            mErrorRetry_clicks.dispose();
        }
        mPresenter.destroy();
    }

    private void initErrorRetry() {
        mTvErrorExit = findViewById(R.id.tv_shelf_detail_version_no_net);
        String appVersion = ManifestUtils.getVersionName(this);
        mTvErrorExit.setText(appVersion);

        mTvErrorExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onErrorExit();
            }
        });
        //无数据视图
        mLlErrorLayout = findViewById(R.id.rl_shelf_detail_error_bg);

        mErrorRetry_clicks = RxView.clicks(mLlErrorLayout).throttleFirst(1, TimeUnit.SECONDS).subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object o) throws Exception {
                //mLlErrorLayout.setVisibility(View.GONE);
                loadShelfId();
            }
        });
    }

    private void initExitDialogView() {
        mLocation_exit_view = View.inflate(getApplicationContext(), R.layout.bookshelf_dialog_init_location_exit, null);

        TextView location_id = mLocation_exit_view.findViewById(R.id.tv_shelf_detail_exit_location);
        mExit_id = mLocation_exit_view.findViewById(R.id.et_shelf_detail_exit);

        location_id.setText("您的代码是：" + mOriginId);

    }

    private void onErrorExit() {

        mExit_id.setText("");

        mExitPass = mOriginId + "logout";

        if (mExitDialog == null) {

            mExitDialog = new AlertDialog.Builder(ShelvesActivity.this)
                    .setView(mLocation_exit_view)
                    .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            String s = mExit_id.getText().toString();

                            if (s.equals(mExitPass)) {

                                System.exit(0);
                            } else {

                                Toast.makeText(getApplicationContext(), "请再重新输入", Toast.LENGTH_SHORT).show();

                            }
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mExitDialog.dismiss();
                        }
                    })
                    .create();
            //設置点击外部不可以消失
            mExitDialog.setCanceledOnTouchOutside(false);
            //设置不可以点击消失
            mExitDialog.setCancelable(false);

        }

        if (!mExitDialog.isShowing()) {
            mExitDialog.show();
        }
    }
}
