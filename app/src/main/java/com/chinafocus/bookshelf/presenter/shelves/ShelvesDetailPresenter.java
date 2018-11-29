package com.chinafocus.bookshelf.presenter.shelves;

import com.chinafocus.bookshelf.model.bean.ShelvesCategoryRawBean;
import com.chinafocus.bookshelf.model.bean.ShelvesCategoryResultBean;

import java.util.ArrayList;
import java.util.List;

public class ShelvesDetailPresenter extends AbstractShelvesPresenter<List<ShelvesCategoryResultBean>> {

    public ShelvesDetailPresenter(IShelvesMvpContract.IView view) {
        super(view);
    }

    @Override
    protected ArrayList<ShelvesCategoryResultBean> rawToResultFromGson(String s) {
        ShelvesCategoryRawBean shelvesCategoryRawBean = mGson.fromJson(s, ShelvesCategoryRawBean.class);
        ArrayList<ShelvesCategoryResultBean> result = new ArrayList<>(1);
        result.add(shelvesCategoryRawBean.getData());
        return result;
    }
}
