package com.chinafocus.bookshelf.presenter.shelves;

import com.chinafocus.bookshelf.bean.ShelvesCategoryRawBean;

import java.util.ArrayList;
import java.util.List;

public class ShelvesDetailPresenter extends AbstractShelvesPresenter<List<ShelvesCategoryRawBean.ShelvesCategoryResultBean>> {

    public ShelvesDetailPresenter(IShelvesMvpContract.IView view) {
        super(view);
    }

    @Override
    protected ArrayList<ShelvesCategoryRawBean.ShelvesCategoryResultBean> rawToResultFromGson(String s) {
        ShelvesCategoryRawBean shelvesCategoryRawBean = mGson.fromJson(s, ShelvesCategoryRawBean.class);
//        ArrayList<ShelvesCategoryRawBean.ShelvesCategoryResultBean> result = new ArrayList<>(1);
//        result.add(shelvesCategoryRawBean.getData());
        return shelvesCategoryRawBean.getData();
    }
}
