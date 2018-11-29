package com.chinafocus.bookshelf.presenter.shelves;

import com.chinafocus.bookshelf.model.bean.BookMetadataRawBean;

import java.util.ArrayList;
import java.util.List;

public class BookMetaDataPresenter extends AbstractShelvesPresenter<List<BookMetadataRawBean.BookMetadataResultBean>> {

    public BookMetaDataPresenter(IShelvesMvpContract.IView view) {
        super(view);
    }

    @Override
    protected ArrayList<BookMetadataRawBean.BookMetadataResultBean> rawToResultFromGson(String s) {
        BookMetadataRawBean shelvesCategoryRawBean = mGson.fromJson(s, BookMetadataRawBean.class);
        ArrayList<BookMetadataRawBean.BookMetadataResultBean> result = new ArrayList<>(1);
        if (shelvesCategoryRawBean != null) {
            BookMetadataRawBean.BookMetadataResultBean data = shelvesCategoryRawBean.getData();
            result.add(data);
        }
        return result;
    }
}
