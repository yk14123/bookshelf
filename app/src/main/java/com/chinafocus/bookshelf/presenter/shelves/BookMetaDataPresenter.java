package com.chinafocus.bookshelf.presenter.shelves;

import android.util.Log;

import com.chinafocus.bookshelf.bean.BookMetadataRawBean;
import com.chinafocus.bookshelf.bean.BookMetadataRawBean.BookMetadataResultBean;
import com.chinafocus.bookshelf.bean.BookMetadataRawBean.BookMetadataResultBean.TocBean;

import java.util.ArrayList;
import java.util.List;

public class BookMetaDataPresenter extends AbstractShelvesPresenter<List<BookMetadataResultBean>> {

    public BookMetaDataPresenter(IShelvesMvpContract.IView view) {
        super(view);
    }

    @Override
    protected ArrayList<BookMetadataResultBean> rawToResultFromGson(String s) {
        BookMetadataRawBean shelvesCategoryRawBean = mGson.fromJson(s, BookMetadataRawBean.class);
        ArrayList<BookMetadataResultBean> result = new ArrayList<>(1);
        if (shelvesCategoryRawBean != null) {
            BookMetadataResultBean data = shelvesCategoryRawBean.getData();
            result.add(data);
        }
        return result;
    }

    private ArrayList<TocBean> tocBeans = new ArrayList<>();

    /**
     * 解析条目数据转化,为每一层tocBean添加所在的节点(level)属性,便于设置其缩进关系
     * @param _level 当前嵌套列表的初始节点位置
     * @param tocBeanList 原始数据列表
     * @return beanList
     */
    public ArrayList<TocBean> getTocList(List<TocBean> tocBeanList, int _level) {
        if (tocBeanList != null && tocBeanList.size() != 0) {
            _level++;
            int level = _level;
            //获取顶层元素，去除需要隐藏的数据条目
            for (int i = 0; i < tocBeanList.size(); i++) {
                TocBean bean = tocBeanList.get(i);
                if (bean != null && bean.getVisible() == 1) {
                    bean.setLevel(level);
                    Log.d("getTocList", "title >>> " + bean.getTitle() + " _level >>> " + level);
                    tocBeans.add(bean);
                    //递归完成数据筛选
                    List<TocBean> children = bean.getChildren();
                    if (children != null && children.size() != 0) {
                        getTocList(children, level);
                    }
                }
            }
        }
        return tocBeans;
    }
}
