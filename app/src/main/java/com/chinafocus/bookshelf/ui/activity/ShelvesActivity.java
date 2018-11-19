package com.chinafocus.bookshelf.ui.activity;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.chinafocus.bookshelf.R;
import com.chinafocus.bookshelf.model.bean.ShelvesRawBean;
import com.chinafocus.bookshelf.presenter.shelves.IShelvesMvpContract;
import com.chinafocus.bookshelf.presenter.shelves.ShelvesPresenter;

import java.util.List;

public class ShelvesActivity extends AppCompatActivity implements IShelvesMvpContract.IView {

    private CoordinatorLayout mRootLayout;
    private RecyclerView mRecyclerView;
//    private NewsMvpAdapter mRecyclerAdapter;
//    private List<NewsBean> mNewsBeans = new ArrayList<>();
    private IShelvesMvpContract.IPresenter mPresenter;
    private LinearLayoutManager mLayoutMgr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bookshelf_activity_mvp);
        initView();
//        dispatchRefresh(IShelvesMvpContract.REFRESH_CACHE);
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
        dispatchRefresh(IShelvesMvpContract.REFRESH_AUTO);
    }

    private void dispatchRefresh(@IShelvesMvpContract.RefreshType int refreshType) {
        mPresenter.refresh(refreshType);
    }

    @Override
    public void onRefreshFinished(@IShelvesMvpContract.RefreshType int refreshType, List<ShelvesRawBean> newsBeans) {
//        mNewsBeans.clear();
//        mNewsBeans.addAll(newsBeans);
//        mRecyclerAdapter.notifyDataSetChanged();
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