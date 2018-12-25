package com.chinafocus.bookshelf.presenter.bookinitportrait;

import android.content.Intent;
import android.widget.Toast;

import com.chinafocus.bookshelf.global.BookShelfConstant;
import com.chinafocus.bookshelf.model.bookinitportrait.BookInitPortraitModule;
import com.chinafocus.bookshelf.ui.activity.BookInitPortraitActivity;
import com.chinafocus.bookshelf.ui.activity.ShelfDetailActivity;
import com.chinafocus.bookshelf.utils.SpUtil;

import java.lang.ref.WeakReference;

public class BookInitPortraitPresenter {


    private WeakReference mViewWeakReference;

    public void initData(BookInitPortraitActivity activity) {

        mViewWeakReference = new WeakReference<>(activity);

        new BookInitPortraitModule().initData(activity);
    }

    public void runClick() {

        BookInitPortraitActivity activity = (BookInitPortraitActivity) mViewWeakReference.get();

        String preId = SpUtil.getString(activity, BookShelfConstant.BOOK_INIT_LOCATION_ID_TEMP);

        String locationId = activity.getEt_init_location().getText().toString();
        String locationIdConfirm = activity.getEt_init_location_confirm().getText().toString();

        //首次进入
        if (preId == null) {

            if (locationId.equals(locationIdConfirm) && locationId.length() > 3) {
                allowEnter(activity, locationId);
            } else {
                Toast.makeText(activity, "两次输入编号不一致或者长度小于4位", Toast.LENGTH_SHORT).show();
            }

        } else {//调整后，二次进入

            if (locationId.equals(locationIdConfirm) && locationId.length() > 3 && locationId.equals(preId)) {
                allowEnter(activity, locationId);
            } else {
                Toast.makeText(activity, "此次输入的代码必须和旧的代码一致！", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void allowEnter(BookInitPortraitActivity activity, String locationId) {
        SpUtil.setString(activity, BookShelfConstant.BOOK_INIT_LOCATION_ID, locationId);
        SpUtil.setString(activity, BookShelfConstant.BOOK_INIT_LOCATION_ID_TEMP, locationId);
        activity.startActivity(new Intent(activity, ShelfDetailActivity.class));
        activity.finish();
    }

    public void ClearActivity() {
        if (mViewWeakReference != null) {
            mViewWeakReference.clear();
            mViewWeakReference = null;
        }
    }

//    public static class BookInitPortraitRunnable implements Runnable {
//        private BookInitPortraitActivity mActivity;
//
//        public BookInitPortraitRunnable(BookInitPortraitActivity activity) {
//            mActivity = activity;
//        }
//
//        @Override
//        public void run() {
//            String locationId = mActivity.getEt_init_location().getText().toString();
//            String locationIdConfirm = mActivity.getEt_init_location_confirm().getText().toString();
//
//            if (locationId.equals(locationIdConfirm) && locationId.length() > 5) {
//                SpUtil.setString(mActivity, BookShelfConstant.BOOK_INIT_LOCATION_ID, locationId);
//                SpUtil.setString(mActivity, BookShelfConstant.BOOK_INIT_LOCATION_ID_TEMP, locationId);
//                mActivity.startActivity(new Intent(mActivity, ShelfDetailActivity.class));
//
//                mActivity.finish();
//            } else {
//                Toast.makeText(mActivity, "两次输入编号不一致或者长度要大于6", Toast.LENGTH_SHORT).show();
//            }
//            mActivity = null;
//        }
//    }

}
