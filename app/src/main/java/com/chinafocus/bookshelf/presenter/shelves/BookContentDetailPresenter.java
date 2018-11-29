package com.chinafocus.bookshelf.presenter.shelves;

import com.chinafocus.bookshelf.model.bean.BookContentRawBean;
import com.chinafocus.bookshelf.model.bean.BookMetadataRawBean;

import java.util.ArrayList;
import java.util.List;

public class BookContentDetailPresenter extends AbstractShelvesPresenter<List<BookContentRawBean.BookContentResultBean>> {

    public BookContentDetailPresenter(IShelvesMvpContract.IView view) {
        super(view);
    }

    @Override
    protected ArrayList<BookContentRawBean.BookContentResultBean> rawToResultFromGson(String s) {
        BookContentRawBean bookContentRawBean = mGson.fromJson(s, BookContentRawBean.class);
        ArrayList<BookContentRawBean.BookContentResultBean> result = new ArrayList<>(1);
        if (bookContentRawBean != null) {
            BookContentRawBean.BookContentResultBean data = bookContentRawBean.getData();
            result.add(data);
        }
        return result;
    }
}
