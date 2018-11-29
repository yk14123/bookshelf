package com.zhy.android.percent.supportnew;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import java.lang.reflect.Method;

public class ScreenUtils {

    private ScreenUtils() {
        // cannot be instantiated
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 返回当前屏幕是否为竖屏。
     *
     * @param context
     * @return 当且仅当当前屏幕为竖屏时返回true, 否则返回false。
     */
    @SuppressWarnings({"JavaDoc", "unused"})
    public static boolean isScreenOriatationPortrait(Context context) {
        return context.getResources().getConfiguration().orientation == //
                Configuration.ORIENTATION_PORTRAIT;
    }

    /**
     * 获得屏幕宽度
     *
     * @param context
     * @return
     */
    @SuppressWarnings("JavaDoc")
    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    /**
     * 获得屏幕高度
     *
     * @param context
     * @return
     */
    @SuppressWarnings("JavaDoc")
    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }

    /**
     * 获取不包括状态栏和标题栏的屏幕高度
     *
     * @param activity
     * @return
     */
    @SuppressWarnings("JavaDoc")
    public static int getScreenHeightReal(Activity activity) {
        return getContentView(activity).getHeight();
    }

    /**
     * 获取包括状态栏和导航栏的整个屏幕的完整高度
     *
     * @param activity
     * @return
     */
    @SuppressWarnings({"unused", "JavaDoc"})
    public static int getScreenHeightFull(Activity activity) {
        int result;
        WindowManager wm = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        Display display = wm.getDefaultDisplay();
        if (Build.VERSION.SDK_INT >= 17) {
            display.getRealMetrics(outMetrics);
            return outMetrics.heightPixels;
        } else {
            display.getMetrics(outMetrics);
        }
        int navigationHeight = getNavigationHeightFromResource(activity);
        if (navigationHeight == 0) {
            result = outMetrics.heightPixels;
        } else {
            if (isNavigationBarShow(activity, display)) {
                result = outMetrics.heightPixels + navigationHeight;
            } else {
                result = outMetrics.heightPixels;
            }
        }
        return result;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    private static boolean isNavigationBarShow(Activity activity, Display display) {
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

    private static int getNavigationHeightFromResource(Context context) {
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

//    /**
//     * 获取完整屏幕高度
//     *
//     * @param activity
//     * @return
//     */
//    @SuppressWarnings({"JavaDoc", "unused"})
//    public static int getScreenHeightFull(Activity activity) {
//        WindowManager wm = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
//        DisplayMetrics outMetrics = new DisplayMetrics();
//        Display display = wm.getDefaultDisplay();
//        int heightScreen;
//        if (Build.VERSION.SDK_INT >= 17) {
//            display.getRealMetrics(outMetrics);
//            return outMetrics.heightPixels;
//        } else {
//            display.getMetrics(outMetrics);
//        }
//        NavigationBarManager navigationBarManager = NavigationBarManager.getInstance();
//        int navigationHeight = navigationBarManager.getNavigationHeightFromResource(activity);
//        if (navigationHeight == 0) {
//            heightScreen = outMetrics.heightPixels;
//        } else {
//            if (navigationBarManager.isNavigationBarShow(activity, display)) {
//                heightScreen = outMetrics.heightPixels + navigationHeight;
//            } else {
//                heightScreen = outMetrics.heightPixels;
//            }
//        }
//        return heightScreen;
//    }

    /**
     * 获取标题栏高度
     *
     * @param activity
     * @return
     */
    @SuppressWarnings("JavaDoc")
    public static int getTitleBarHeight(Activity activity) {
        int contentTop = getContentView(activity).getTop();
        if (contentTop == 0) {
            return 0;
        }
        return contentTop - getStatusHeight(activity);
    }

    /**
     * 获取状态栏的高度,getWindowVisibleDisplayFrame()方法拿到包括标题栏但不包括状态栏的区域,必须界面完全显示才可以拿到,
     * 属于动态行为.
     *
     * @param activity
     * @return
     */
    @SuppressWarnings("JavaDoc")
    public static int getStatusHeight(Activity activity) {
        int contentTop = getContentView(activity).getTop();
        if (contentTop == 0) {
            return 0;
        }
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        return frame.top;
    }

    /**
     * 获取状态栏的高度,不管界面是不是有状态栏,还是隐藏了状态栏,都能拿到状态栏的高度,
     * <p>
     * 因为是反射系统dimen资源的内部类的1个静态常量,属于静态行为.
     *
     * @param context
     * @return
     */
    @SuppressWarnings("JavaDoc")
    public static int getStatusHeight2(Context context) {
        int statusHeight = -1;
        try {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            // Object object = clazz.newInstance();
            // Field f = clazz.getField("status_bar_height");
            // Log.i("MyLog", "f.getName() -> " + f.getName());
            // Log.i("MyLog", "f.toString() -> " + f.toString());
            int height = Integer.parseInt(clazz.getField("status_bar_height")
                    .get(null).toString());
            statusHeight = context.getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusHeight;
    }

    @SuppressWarnings("WeakerAccess")
    public static FrameLayout getContentView(Activity activity) {
        return (FrameLayout) activity.getWindow().findViewById(Window.ID_ANDROID_CONTENT);
    }

    /**
     * 获取当前屏幕截图，包含状态栏
     *
     * @param activity
     * @return
     */
    @SuppressWarnings({"JavaDoc", "unused"})
    public static Bitmap snapShotWithStatusBar(Activity activity) {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bmp = view.getDrawingCache();
        int width = getScreenWidth(activity);
        int height = getScreenHeight(activity);
        Bitmap bp;
        bp = Bitmap.createBitmap(bmp, 0, 0, width, height);
        view.destroyDrawingCache();
        return bp;

    }

    /**
     * 获取当前屏幕截图，不包含状态栏
     *
     * @param activity
     * @return
     */
    @SuppressWarnings({"JavaDoc", "unused"})
    public static Bitmap snapShotWithoutStatusBar(Activity activity) {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bmp = view.getDrawingCache();
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;
        int width = getScreenWidth(activity);
        int height = getScreenHeight(activity);
        Bitmap bp;
        bp = Bitmap.createBitmap(bmp, 0, statusBarHeight, width, height
                - statusBarHeight);
        view.destroyDrawingCache();
        return bp;
    }

}
