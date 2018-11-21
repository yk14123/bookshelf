package com.chinafocus.bookshelf.presenter.shelves;

import com.chinafocus.bookshelf.model.bean.ShelvesCategoryRawBean;
import com.chinafocus.bookshelf.model.bean.ShelvesCategoryResultBean;

import java.util.List;

public class ShelvesDetailPresenter extends AbstractShelvesPresenter<List<ShelvesCategoryResultBean.ShelvesCategoriesFinalBean>> {

    public ShelvesDetailPresenter(IShelvesMvpContract.IView view) {
        super(view);
    }

    @Override
    protected List<ShelvesCategoryResultBean.ShelvesCategoriesFinalBean> rawToResultFromGson(String s) {

        ShelvesCategoryRawBean shelvesCategoryRawBean = mGson.fromJson(s, ShelvesCategoryRawBean.class);

        return shelvesCategoryRawBean.getData().getCategories();
    }
}
