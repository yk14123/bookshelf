package com.chinafocus.bookshelf.ui.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.chinafocus.bookshelf.R;
import com.chinafocus.bookshelf.base.BaseActivity;
import com.chinafocus.bookshelf.model.bean.ShelvesResultBean;
import com.chinafocus.bookshelf.presenter.shelves.IShelvesMvpContract;
import com.chinafocus.bookshelf.presenter.shelves.ShelvesPresenter;

import java.util.List;

//import com.chinafocus.bookshelf.model.bean.ShelvesCategoryResultBean.ShelvesCategoriesFinalBean;


//public class ShelvesActivity extends BaseActivity<ShelvesCategoriesFinalBean> {
public class ShelvesActivity extends BaseActivity<ShelvesResultBean> {

    private IShelvesMvpContract.IPresenter mPresenter;

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.bookshelf_activity_main);

    }

    @Override
    protected void initView() {
        mPresenter = new ShelvesPresenter(this);
//        mPresenter = new ShelvesDetailPresenter(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        dispatchRefresh();
    }

    private void dispatchRefresh() {
        mPresenter.refresh(IShelvesMvpContract.REFRESH_SHELVES, null);
//        mPresenter.refresh(IShelvesMvpContract.REFRESH_SHELVES_DETAIL, new String[]{"2"});

    }


//    @Override
//    public void onRefreshFinished(String refreshType, List<ShelvesCategoriesFinalBean> resultBean) {
//        if (resultBean != null) {
//            Log.i("MyLog", "ShelvesActivity  --  refreshType  -->" + refreshType);
//            Toast.makeText(this, "成功了 --> " + resultBean.get(0).getName() + "--  getCategoryId  -- " + resultBean.get(0).getCategoryId(), Toast.LENGTH_SHORT).show();
//        }
//
//    }

    @Override
    public void onRefreshFinished(String refreshType, List<ShelvesResultBean> resultBean) {
        if (resultBean != null) {
            Log.i("MyLog", "ShelvesActivity  --  refreshType  -->" + refreshType);
            Toast.makeText(this, "成功了 --> " + resultBean.get(0).getName(), Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    public void showTips(String message) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.destroy();
    }
}
