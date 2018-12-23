package com.chinafocus.bookshelf.presenter.shelves;

import android.text.TextUtils;

import com.chinafocus.bookshelf.bean.BookContentRawBean;
import com.chinafocus.bookshelf.utils.JMDESUtil;

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

            String currentRaw = data.getCurrent();
            try {
                String currentResult = JMDESUtil.decode(currentRaw);
                data.setCurrent(currentResult);
                result.add(data);
                return result;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 格式化转译字符串，需要将当前的/字符进行两次转译；
     * 为了防止参数内的#字符不被重复编译，先将/字符替换；
     * retrofit内部默认对字符串进行了转译，此处不在使用UrlEncoder.encode()进行重复转译
     */
    public String formatUrl(String url) {
        if (!TextUtils.isEmpty(url) && url.contains("/")) {
            return url.replace("/", "%2F");
        }
        return url;
    }

}
