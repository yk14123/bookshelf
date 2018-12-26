package com.chinafocus.bookshelf.presenter.bookinitportrait;

import android.content.Intent;
import android.widget.Toast;

import com.chinafocus.bookshelf.global.BookShelfConstant;
import com.chinafocus.bookshelf.model.bookinitportrait.BookInitPortraitModule;
import com.chinafocus.bookshelf.ui.activity.BookInitPortraitActivity;
import com.chinafocus.bookshelf.ui.activity.BookShelfSplashActivity;
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

        String locationId = activity.getEt_init_location().getText().toString();
        String locationIdConfirm = activity.getEt_init_location_confirm().getText().toString();

        if (locationId.equals(locationIdConfirm) && locationId.length() > 3) {
            allowEnter(activity, locationId);
        } else {
            Toast.makeText(activity, "两次输入编号不一致或者长度小于4位", Toast.LENGTH_SHORT).show();
        }

    }

    private void allowEnter(BookInitPortraitActivity activity, String locationId) {
        SpUtil.setString(activity, BookShelfConstant.BOOK_INIT_LOCATION_ID, locationId);
        activity.startActivity(new Intent(activity, BookShelfSplashActivity.class));
        activity.finish();
    }

    public void ClearActivity() {
        if (mViewWeakReference != null) {
            mViewWeakReference.clear();
            mViewWeakReference = null;
        }
    }

}
