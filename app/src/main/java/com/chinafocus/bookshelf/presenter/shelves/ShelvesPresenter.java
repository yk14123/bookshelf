package com.chinafocus.bookshelf.presenter.shelves;


import com.chinafocus.bookshelf.model.bean.ShelvesRawBean;
import com.chinafocus.bookshelf.model.bean.ShelvesResultBean;

import java.util.List;

public class ShelvesPresenter extends AbstractShelvesPresenter<List<ShelvesResultBean>> {


    public ShelvesPresenter(IShelvesMvpContract.IView view) {
        super(view);
    }

    @Override
    protected List<ShelvesResultBean> rawToResultFromGson(String s) {
        ShelvesRawBean shelvesRawBean = mGson.fromJson(s, ShelvesRawBean.class);

        return shelvesRawBean.getData();
    }

}
