package com.chinafocus.bookshelf.presenter.shelves;

import com.chinafocus.bookshelf.bean.BookCategoryDetailRawBean;

import java.util.ArrayList;
import java.util.List;

public class BookCategoryDetailPresenter extends AbstractShelvesPresenter<List<BookCategoryDetailRawBean.BookCategoryDetailResultBean>> {

    public BookCategoryDetailPresenter(IShelvesMvpContract.IView view) {
        super(view);
    }

    @Override
    protected ArrayList<BookCategoryDetailRawBean.BookCategoryDetailResultBean> rawToResultFromGson(String s) {
        BookCategoryDetailRawBean shelvesCategoryRawBean = mGson.fromJson(s, BookCategoryDetailRawBean.class);
        ArrayList<BookCategoryDetailRawBean.BookCategoryDetailResultBean> result = new ArrayList<>(1);
        if (shelvesCategoryRawBean != null) {
            BookCategoryDetailRawBean.BookCategoryDetailResultBean data = shelvesCategoryRawBean.getData();
            result.add(data);
        }
        return result;
    }
}
