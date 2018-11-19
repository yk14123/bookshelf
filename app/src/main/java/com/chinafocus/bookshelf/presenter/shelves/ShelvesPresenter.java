package com.chinafocus.bookshelf.presenter.shelves;


public class ShelvesPresenter extends AbstractShelvesPresenter {


    public ShelvesPresenter(IShelvesMvpContract.IView view) {
        super(view);
    }

    @Override
    protected void startData() {

    }

//    private void updateNewsBeans(@IShelvesMvpContract.RefreshType int refreshType, NewsEntity newsEntity) {
//        List<NewsBean> filter = new ArrayList<>();
//        for (NewsResultEntity resultEntity : newsEntity.getResults()) { //对资讯进行去重，需要重写NewsBean的对应方法。
//            NewsBean newsBean = entityToBean(resultEntity);
//            if (!mShelvesRawBeans.contains(newsBean)) {
//                filter.add(newsBean);
//            }
//        }
//        if (refreshType == IShelvesMvpContract.REFRESH_CACHE && mShelvesRawBeans.size() == 0) { //只有当前没有数据时，才使用缓存。
//            mShelvesRawBeans = filter;
//        } else if (refreshType == IShelvesMvpContract.REFRESH_AUTO) { //自动刷新的数据放在头部。
//            mShelvesRawBeans.addAll(0, filter);
//            mLastNetUpdateTime = System.currentTimeMillis();
//        }
//    }

//    private NewsBean entityToBean(NewsResultEntity resultEntity) {
//        String title = resultEntity.getDesc();
//        NewsBean bean = new NewsBean();
//        bean.setTitle(title);
//        return bean;
//    }


}
