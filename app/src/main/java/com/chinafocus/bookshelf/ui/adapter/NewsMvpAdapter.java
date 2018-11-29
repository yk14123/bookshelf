package com.chinafocus.bookshelf.ui.adapter;

//public class NewsMvpAdapter extends RecyclerView.Adapter<NewsMvpAdapter.BookCategoryHolder> {
//
//
//    private List<NewsBean> mNewsEntity;
//
//    @NonNull
//    @Override
//    public BookCategoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        return new BookCategoryHolder(parent, R.layout.bookshelf_recycle_item_news);
//    }
//
//    @Override
//    public void onBindViewHolder(BookCategoryHolder holder, int position) {
//        if (mNewsEntity != null) {
//            NewsBean newsBean = mNewsEntity.get(position);
//            if (newsBean != null) {
//                TextView titleView = holder.getView(R.id.tv_title);
//                titleView.setText(newsBean.getTitle());
//            }
//        }
//    }
//
//    @Override
//    public int getItemCount() {
//        return mNewsEntity != null ? mNewsEntity.size() : 0;
//    }
//
//    public void setCategoryEntity(List<NewsBean> newsEntity) {
//        mNewsEntity = newsEntity;
//    }
//
//    class BookCategoryHolder extends RecyclerView.ViewHolder {
//
//        BookCategoryHolder(ViewGroup holder, @LayoutRes int layoutId) {
//            super(LayoutInflater.from(holder.getContext()).inflate(layoutId, holder, false));
//        }
//
//        <T extends IView> T getView(@IdRes int resId) {
//            Object childView = itemView.getTag(resId);
//            if (childView == null) {
//                childView = itemView.findViewById(resId);
//                itemView.setTag(resId, childView);
//            }
//            return (T) childView;
//        }
//    }
//}
