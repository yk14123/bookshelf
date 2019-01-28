package com.chinafocus.bookshelf.presenter.shelves;


import com.chinafocus.bookshelf.bean.ShelvesRawBean;

import java.util.List;

public class ShelvesPresenter extends AbstractShelvesPresenter<List<ShelvesRawBean.ShelvesResultBean>> {


    public ShelvesPresenter(IShelvesMvpContract.IView view) {
        super(view);
    }

    @Override
    protected List<ShelvesRawBean.ShelvesResultBean> rawToResultFromGson(String s) {
        ShelvesRawBean shelvesRawBean = mGson.fromJson(s, ShelvesRawBean.class);

        return shelvesRawBean.getData();
    }

}
