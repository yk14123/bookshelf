package com.chinafocus.bookshelf.model.base.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.chinafocus.bookshelf.presenter.shelves.IShelvesMvpContract;
import com.chinafocus.bookshelf.utils.ProgressBarUtils;

/**
 * Created by 馒头大人 on 2017/11/3.
 */

public abstract class BaseActivity<T> extends AppCompatActivity implements IShelvesMvpContract.IView<T> {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
    }

    /**
     * 初始化View
     */
    protected abstract void initView();


    /**
     * 显示loading视图
     */
    public void showLoading() {
        ProgressBarUtils.showProgressDialog(this, "");
    }

    /**
     * 关闭loading视图
     */
    public void dismissLoading() {
        ProgressBarUtils.dismiss();
    }

    @Override
    protected void onDestroy() {
        dismissLoading();
        super.onDestroy();
    }

}
