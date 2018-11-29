package com.zhy.android.percent.supportnew;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;

import com.chinafocus.bookshelf.model.lru.LruMap;

import java.lang.reflect.Method;

public class NavigationBarManager {

    private ViewTreeObserver.OnGlobalLayoutListener mOnGlobalLayoutListener;
    private View mViewObserved;
    @SuppressWarnings("unused")
    private int mUsableHeightPrevious;
    @SuppressWarnings("unused")
    private ViewGroup.LayoutParams mFrameLayoutParams;
    private boolean mIsNavigationBarShow;

    private NavigationBarManager() {

    }

    public static NavigationBarManager getInstance() {
        LruMap lruMap = LruMap.getInstance();
        String name = NavigationBarManager.class.getName();
        NavigationBarManager navigationBarManager = (NavigationBarManager) lruMap.get(name);
        if (navigationBarManager == null) {
            navigationBarManager = new NavigationBarManager();
            lruMap.put(name, navigationBarManager);
        }
        return navigationBarManager;
    }

    /**
     * 关联要监听的视图
     *
     * @param viewObserving
     * @param isNavigationBarShow
     */
    @SuppressWarnings("JavaDoc")
    public void add(View viewObserving, boolean isNavigationBarShow, OnNavigationBarChangeListener onNavigationBarChangeListener) {
        mIsNavigationBarShow = isNavigationBarShow;

        if (mViewObserved != null) {
            remove();
        }
        mViewObserved = viewObserving;

        mFrameLayoutParams = mViewObserved.getLayoutParams();

        // 给View添加全局的布局监听器
        mOnGlobalLayoutListener = () -> resetLayoutByUsableHeight(onNavigationBarChangeListener);
        mViewObserved.getViewTreeObserver().addOnGlobalLayoutListener(mOnGlobalLayoutListener);
    }

    @SuppressWarnings("WeakerAccess")
    public interface OnNavigationBarChangeListener {
        void onNavigationBarChange(boolean isNavigationBarShow);
    }

    public void remove() {
        if (mViewObserved == null) {
            return;
        }
        ViewUtil.removeOnGlobalLayoutListener(mViewObserved, mOnGlobalLayoutListener);
        mViewObserved = null;
        mFrameLayoutParams = null;
        mOnGlobalLayoutListener = null;
        mIsNavigationBarShow = false;
    }

    @SuppressWarnings("deprecation")
    private void resetLayoutByUsableHeight(OnNavigationBarChangeListener onNavigationBarChangeListener) {
        // 比较布局变化前后的View的可用高度
//        LogUtil.showScreenInfo((Activity) mViewObserved.getContext());
//        isNavigationBarShow((Activity) mViewObserved.getContext(), null);
//        int screenHeightReal = ScreenUtils.getScreenHeightReal((Activity) mViewObserved.getContext());
//        int usableHeightNow = mViewObserved.getHeight();
//        View childAt = ScreenUtils.getContentView((Activity) mViewObserved.getContext()).getChildAt(0);
//        LogUtil.i("(childAt==mViewObserved) -> " + (childAt == mViewObserved));
//        LogUtil.i("mUsableHeightPrevious -> " + mUsableHeightPrevious);
//        LogUtil.i("usableHeightNow -> " + usableHeightNow);

//        if (usableHeightNow > 0 && usableHeightNow != mUsableHeightPrevious) {
//            // 如果两次高度不一致
//            // 将当前的View的可用高度设置成View的实际高度
////            if (usableHeightNow != screenHeightReal) {
//            mFrameLayoutParams.height = usableHeightNow;
//            mUsableHeightPrevious = usableHeightNow;
//            mViewObserved.setLayoutParams(mFrameLayoutParams);
////            mViewObserved.requestLayout();
//            mViewObserved.invalidate();
////            ScreenUtils.getContentView((Activity) mViewObserved.getContext()).requestLayout();
////            ScreenUtils.getContentView((Activity) mViewObserved.getContext()).invalidate();
////            }
//        }
        if (mViewObserved.getHeight() < 0) {
            return;
        }
        Activity activity;
        boolean isNavigationBarShow;
        activity = (Activity) mViewObserved.getContext();
        isNavigationBarShow = isNavigationBarShow(activity, null);
        if (mIsNavigationBarShow == isNavigationBarShow) {
            return;
        }
        mIsNavigationBarShow = isNavigationBarShow;

//        int navHeight = getNavigationHeightFromResource(activity);
//        if (isNavigationBarShow) {
////            activity.setForNavigationBarBottom(activity);
//
////            FrameLayout content = ((FrameLayout) activity.findViewById(android.R.id.content));
//////            ViewGroup content = (ViewGroup) mViewObserved;
////            int childCount = content.getChildCount();
////            FrameLayout.LayoutParams layoutParams;
////            for (int i = 0; i < childCount; i++) {
////                View childView = content.getChildAt(i);
////
//////                childView.setPadding(0, 0, 0, navHeight);
////                layoutParams = (FrameLayout.LayoutParams) childView.getLayoutParams();
////                layoutParams.topMargin = -navHeight;
//////                childView.setLayoutParams(layoutParams);
////            }
//            View view1 = mViewObserved.findViewById(R.id.vgJJIcon);
//            View view2 = mViewObserved.findViewById(R.id.vgToSpeedUpTime);
//            View view3 = mViewObserved.findViewById(R.id.vgToSpeedUp);
//            ViewGroup.MarginLayoutParams layoutParams1 = (ViewGroup.MarginLayoutParams) view1.getLayoutParams();
//            ViewGroup.MarginLayoutParams layoutParams2 = (ViewGroup.MarginLayoutParams) view2.getLayoutParams();
//            ViewGroup.MarginLayoutParams layoutParams3 = (ViewGroup.MarginLayoutParams) view3.getLayoutParams();
//            layoutParams1.topMargin -= navHeight;
//            layoutParams2.topMargin -= navHeight;
//            layoutParams3.topMargin -= navHeight;
////            view3.setLayoutParams(layoutParams3);
////            view2.setLayoutParams(layoutParams2);
////            view1.setLayoutParams(layoutParams1);
////            mViewObserved.requestLayout();
////            mViewObserved.invalidate();
//            mViewObserved.setLayoutParams(mViewObserved.getLayoutParams());
//
////            FrameLayout.LayoutParams navBarLayoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
////                    navHeight, Gravity.BOTTOM);
////            View navBar = new View(activity);
////            navBar.setBackgroundColor(activity.getResources().getColor(R.color.title_bg));
////            content.addView(navBar, navBarLayoutParams);
////            content.requestLayout();
////            content.invalidate();
//        } else {
//            View view1 = mViewObserved.findViewById(R.id.vgJJIcon);
//            View view2 = mViewObserved.findViewById(R.id.vgToSpeedUpTime);
//            View view3 = mViewObserved.findViewById(R.id.vgToSpeedUp);
//            ViewGroup.MarginLayoutParams layoutParams1 = (ViewGroup.MarginLayoutParams) view1.getLayoutParams();
//            ViewGroup.MarginLayoutParams layoutParams2 = (ViewGroup.MarginLayoutParams) view2.getLayoutParams();
//            ViewGroup.MarginLayoutParams layoutParams3 = (ViewGroup.MarginLayoutParams) view3.getLayoutParams();
//            layoutParams1.topMargin += navHeight;
//            layoutParams2.topMargin += navHeight;
//            layoutParams3.topMargin += navHeight;
////            view3.setLayoutParams(layoutParams3);
////            view2.setLayoutParams(layoutParams2);
////            view1.setLayoutParams(layoutParams1);
////            mViewObserved.requestLayout();
////            mViewObserved.invalidate();
//            mViewObserved.setLayoutParams(mViewObserved.getLayoutParams());
//        }

//        LogUtil.i("切换布局");

//        if (!isNavigationBarShow) {
//            activity.setContentView(R.layout.fragment_main_no_navigation);
//        } else {
//            activity.setContentView(R.layout.fragment_main_navigation);
//        }
//        activity.onCreate(null);
//        ((JJBoostMainActivity) activity).doOnResume();

//        activity.mIsSwitchLayout = true;
//        activity.onBackPressed();

//        activity.switchFragment();
        if (onNavigationBarChangeListener != null) {
            onNavigationBarChangeListener.onNavigationBarChange(isNavigationBarShow);
        }
    }

//    /**
//     * 计算视图可视高度
//     *
//     * @return
//     */
//    @SuppressWarnings("JavaDoc")
//    private int computeUsableHeight() {
//        Rect r = new Rect();
//        mViewObserved.getWindowVisibleDisplayFrame(r);
//        return (r.bottom - r.top);
//    }

    @SuppressWarnings("StatementWithEmptyBody")
    public boolean isNavigationBarShow(Activity activity, Display display) {
        boolean result;
        if (Build.VERSION.SDK_INT >= 17) {
            if (display == null) {
                display = activity.getWindowManager().getDefaultDisplay();
            }
            Point size = new Point();
            Point realSize = new Point();
            display.getSize(size);
            display.getRealSize(realSize);
//            LogUtil.i("size -> " + size);
//            LogUtil.i("realSize -> " + realSize);
            result = realSize.y != size.y;
        } else {
            result = !(ViewConfiguration.get(activity).hasPermanentMenuKey() || KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK));
        }
        if (result) {
//            LogUtil.i("导航栏显示");
        } else {
//            LogUtil.i("导航栏隐藏");
        }
        return result;
    }


    public int getNavigationHeightFromResource(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        int navigationBarHeight = 0;
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("config_showNavigationBar",
                "bool", "android");
        if (resourceId > 0) {
            boolean hasNav = resources.getBoolean(resourceId);
            if (hasNav) {
                resourceId = resources.getIdentifier("navigation_bar_height",
                        "dimen", "android");
                if (resourceId > 0) {
                    navigationBarHeight = resources
                            .getDimensionPixelSize(resourceId);
                }
            }
        }

        if (navigationBarHeight <= 0) {
            DisplayMetrics dMetrics = new DisplayMetrics();
            display.getMetrics(dMetrics);
            int screenHeight = Math.max(dMetrics.widthPixels, dMetrics.heightPixels);
            int realHeight = 0;
            try {
                Method mt = display.getClass().getMethod("getRealSize", Point.class);
                Point size = new Point();
                mt.invoke(display, size);
                realHeight = Math.max(size.x, size.y);
            } catch (NoSuchMethodException e) {
                Method mt = null;
                try {
                    mt = display.getClass().getMethod("getRawHeight");
                } catch (NoSuchMethodException e2) {
                    try {
                        mt = display.getClass().getMethod("getRealHeight");
                    } catch (NoSuchMethodException ignored) {

                    }
                }
                if (mt != null) {
                    try {
                        realHeight = (int) mt.invoke(display);
                    } catch (Exception ignored) {

                    }
                }
            } catch (Exception ignored) {

            }
            // 如果是橫屏,这种计算方式是不是会有问题.
            navigationBarHeight = realHeight - screenHeight;
        }

        return navigationBarHeight;
    }

}
