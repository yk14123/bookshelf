package com.chinafocus.bookshelf.ui.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.chinafocus.bookshelf.R;
import com.chinafocus.bookshelf.base.BaseActivity;
import com.chinafocus.bookshelf.global.BookShelfConstant;
import com.chinafocus.bookshelf.model.bean.ShelvesCategoryResultBean;
import com.chinafocus.bookshelf.presenter.shelves.IShelvesMvpContract;
import com.chinafocus.bookshelf.presenter.shelves.ShelvesDetailPresenter;
import com.chinafocus.bookshelf.ui.adapter.ShelfCategoryAdapter;
import com.chinafocus.bookshelf.ui.adapter.ShelfIntroAdapter;
import com.chinafocus.bookshelf.ui.dialog.ShelfCopyrightDialog;
import com.chinafocus.bookshelf.utils.ManifestUtils;
import com.jakewharton.rxbinding2.view.RxView;
import com.zhy.android.percent.support.PercentRelativeLayout;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * 书柜九宮格页面-->习近平的书柜
 *
 * @author HUANG_JIN
 * @version 1.0
 * create at 2018/11/26
 */
public class ShelfDetailActivity extends BaseActivity<ShelvesCategoryResultBean> {
    private static final String TAG = "ShelfDetailActivity";
    //連接器
    private IShelvesMvpContract.IPresenter mPresenter;
    //root
    private PercentRelativeLayout mRlShelfRoot;
    //logo
    private ImageView mIvLogo;
    //分类列表
    private RecyclerView mRvCategory;
    private RecyclerView mRvShelfIntro;
    //适配器
    private ShelfCategoryAdapter mShelfCategoryAdapter;
    //copyright
    private ImageView mIvCopyRight;
    //    private ShelfCopyrightDialog mCopyrightDialog;
    private AlertDialog mCopyrightDialog;
    private boolean isHideRvCategory = true;
    private Animation mCategory_Scale_out;
    private Animation mCategory_Scale_in;
    private Animation mShelfIntro_Translate_in;
    private Animation mShelfIntro_Translate_out;
    private Disposable mIvLogo_clicks, mIvCopyRight_clicks;
    private AlertDialog mAlertDialog;

    @SuppressLint("CheckResult")
    @Override
    protected void initView() {
        setContentView(R.layout.bookshelf_activity_detail);
        //设置当前的版本号
        TextView mTvVersionInfo = findViewById(R.id.tv_shelf_detail_version);
        String appVersion = ManifestUtils.getVersionName(this);
        mTvVersionInfo.setText(appVersion);
        //root
        mRlShelfRoot = findViewById(R.id.rl_shelf_detail_root);

        //习近平的书柜
        initLogo();
        //书柜分类九宫格
        initRvCategory();
        //书柜简介
        initRvShelfIntro();
        //易阅通
        initCopyRight();

        //初始化动画
        initAnim();

        //初始化Presenter
        mPresenter = new ShelvesDetailPresenter(this);
        //请求数据
        requestShelfDetail();

        String cpuSerial = getCPUSerial();

        Log.i("MyLog", "cpuSerial  -->" + cpuSerial);

        String SerialNumber = android.os.Build.SERIAL;

        Log.i("MyLog", "SerialNumber  -->" + SerialNumber);

    }

    private void initCopyRight() {

        mIvCopyRight = findViewById(R.id.iv_shelf_detail_copyright);
        mIvCopyRight_clicks = RxView.clicks(mIvCopyRight).throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
//                        if (mAlertDialog == null) {
//                            View mContentView = LayoutInflater.from(ShelfDetailActivity.this).inflate(R.layout.bookshelf_dialog_shelf_copyright, null);
//                            mAlertDialog = new AlertDialog.Builder(ShelfDetailActivity.this)
//                                    .setView(mContentView)
//                                    .create();
//                            //設置点击外部可以消失
//                            mAlertDialog.setCanceledOnTouchOutside(true);
//                            //设置可以点击消失
//                            mAlertDialog.setCancelable(true);
//                        }
//                        //点击copyRight弹出版权信息对话框
//                        if (!mAlertDialog.isShowing()) {
//                            mAlertDialog.show();
//                            WindowManager windowManager = getWindowManager();
//                            Display display = windowManager.getDefaultDisplay();
//                            Window window = mAlertDialog.getWindow();
//                            if (window != null) {
//                                window.getDecorView().setPadding(0, 0, 0, 0);
//                                WindowManager.LayoutParams lp = window.getAttributes();
//                                int screenWidth = ScreenUtils.getScreenWidth(ShelfDetailActivity.this);
//                                lp.width = (int) (screenWidth * 0.7); //设置宽度
//                                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
////                                lp.dimAmount = 0f;
//                                mAlertDialog.getWindow().setAttributes(lp);
//                                //设置dialog的背景颜色为透明色,就可以显示圆角了!!
//                                window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
////                                window.setBackgroundDrawableResource(android.R.color.transparent);
//                            }
//                        }

                        if (mCopyrightDialog == null) {
                            View mContentView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.bookshelf_dialog_shelf_copyright, null);
                            mCopyrightDialog = new ShelfCopyrightDialog.Builder(ShelfDetailActivity.this)
                                    .setView(mContentView).create();

                            //設置点击外部可以消失
                            mCopyrightDialog.setCanceledOnTouchOutside(true);
                            //设置可以点击消失
                            mCopyrightDialog.setCancelable(true);


                            Window window = mCopyrightDialog.getWindow();
                            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        }
                        //点击copyRight弹出版权信息对话框
                        if (!mCopyrightDialog.isShowing()) {
                            mCopyrightDialog.show();

//                            WindowManager windowManager = getWindowManager();
//                            Display display = windowManager.getDefaultDisplay();
//                            Window window = mCopyrightDialog.getWindow();
//                            if (window != null) {
//                                window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                                window.getDecorView().setPadding(0, 0, 0, 0);
//                                WindowManager.LayoutParams lp = window.getAttributes();
////                                Log.i("MyLog","display.getWidth() -->" +display.getWidth());
//                                int screenWidth = ScreenUtils.getScreenWidth(ShelfDetailActivity.this);
//                                Log.i("MyLog", "screenWidth -->" + screenWidth);
//                                lp.width = (int) (screenWidth * 0.7); //设置宽度
//                                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
////                                lp.dimAmount = 0f;
//                                mCopyrightDialog.getWindow().setAttributes(lp);
//                                //设置dialog的背景颜色为透明色,就可以显示圆角了!!
//                                window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                                window.setBackgroundDrawableResource(android.R.color.transparent);
//                                window.setBackgroundDrawableResource(android.R.color.transparent);
//                            }
                        }
                    }
                });
    }


//    /* 渠道标志为：
//            * 1，andriod（a）
//            *
//            * 识别符来源标志：
//            * 1， wifi mac地址（wifi）；
//            * 2， IMEI（imei）；
//            * 3， 序列号（sn）；
//            * 4， id：随机码。若前面的都取不到时，则随机生成一个随机码，需要缓存。
//            *
//            * @param context
//  * @return
//          */
//    public static String getDeviceId(Context context) {
//        StringBuilder deviceId = new StringBuilder();
//        // 渠道标志
//        deviceId.append("a");
//        try {
//            //wifi mac地址
//            WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
//            WifiInfo info = wifi.getConnectionInfo();
//            String wifiMac = info.getMacAddress();
//            if(!isEmpty(wifiMac)){
//                deviceId.append("wifi");
//                deviceId.append(wifiMac);
//                PALog.e("getDeviceId : ", deviceId.toString());
//                return deviceId.toString();
//            }
//            //IMEI（imei）
//            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
//            String imei = tm.getDeviceId();
//            if(!isEmpty(imei)){
//                deviceId.append("imei");
//                deviceId.append(imei);
//                PALog.e("getDeviceId : ", deviceId.toString());
//                return deviceId.toString();
//            }
//            //序列号（sn）
//            String sn = tm.getSimSerialNumber();
//            if(!isEmpty(sn)){
//                deviceId.append("sn");
//                deviceId.append(sn);
//                PALog.e("getDeviceId : ", deviceId.toString());
//                return deviceId.toString();
//            }
//            //如果上面都没有， 则生成一个id：随机码
//            String uuid = getUUID(context);
//            if(!isEmpty(uuid)){
//                deviceId.append("id");
//                deviceId.append(uuid);
//                PALog.e("getDeviceId : ", deviceId.toString());
//                return deviceId.toString();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            deviceId.append("id").append(getUUID(context));
//        }
//        PALog.e("getDeviceId : ", deviceId.toString());
//        return deviceId.toString();
//    }
//    /**
//     * 得到全局唯一UUID
//     */
//    public static String getUUID(Context context){
//        SharedPreferences mShare = getSysShare(context, "sysCacheMap");
//        if(mShare != null){
//            uuid = mShare.getString("uuid", "");
//        }
//        if(isEmpty(uuid)){
//            uuid = UUID.randomUUID().toString();
//            saveSysMap(context, "sysCacheMap", "uuid", uuid);
//        }
//        PALog.e(tag, "getUUID : " + uuid);
//        return uuid;
//    }

    /**
     * 获取CPU序列号
     *
     * @return CPU序列号(16位) 读取失败为"0000000000000000"
     */
    public static String getCPUSerial() {
        String str = "", strCPU = "", cpuAddress = "0000000000000000";
        try {
            // 读取CPU信息
            Process pp = Runtime.getRuntime().exec("cat /proc/cpuinfo");
            InputStreamReader ir = new InputStreamReader(pp.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);
            // 查找CPU序列号
            for (int i = 1; i < 100; i++) {
                str = input.readLine();
                if (str != null) {
                    // 查找到序列号所在行
                    if (str.indexOf("Serial") > -1) {
                        // 提取序列号
                        strCPU = str.substring(str.indexOf(":") + 1, str.length());
                        // 去空格
                        cpuAddress = strCPU.trim();
                        break;
                    }
                } else {
                    // 文件结尾
                    break;
                }
            }
        } catch (IOException ex) {
            // 赋予默认值
            ex.printStackTrace();
        }
        return cpuAddress;
    }

    private void initLogo() {
        mIvLogo = findViewById(R.id.iv_shelf_detail_logo);
        //点击logo弹出书柜简介
        mIvLogo_clicks = RxView.clicks(mIvLogo).throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        startViewAnimation();
                    }
                });
    }

    private void initAnim() {
        mCategory_Scale_out = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bookshelf_view_scale_out);
        mCategory_Scale_in = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bookshelf_view_scale_in);
        mShelfIntro_Translate_in = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bookshelf_view_translate_in);
        mShelfIntro_Translate_out = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bookshelf_view_translate_out);
    }

    private void initRvCategory() {
        mRvCategory = findViewById(R.id.rv_shelf_detail_category);
        GridLayoutManager manager = new GridLayoutManager(getApplicationContext(), 3);
        mRvCategory.setLayoutManager(manager);
        mRvCategory.setHasFixedSize(true);
    }

    private void initRvShelfIntro() {
        mRvShelfIntro = findViewById(R.id.rv_shelf_detail_intro);
        LinearLayoutManager linearManager = new LinearLayoutManager(
                this, LinearLayoutManager.VERTICAL, false);
        mRvShelfIntro.setHasFixedSize(true);
        mRvShelfIntro.setLayoutManager(linearManager);
        ShelfIntroAdapter introAdapter = new ShelfIntroAdapter(this);
        mRvShelfIntro.setAdapter(introAdapter);

    }

    /**
     * 请求九宫格分类API接口数据
     */
    private void requestShelfDetail() {
        mPresenter.refresh(IShelvesMvpContract.REFRESH_SHELVES_DETAIL, new String[]{"2"});
    }


    @Override
    public void onRefreshFinished(String refreshType, List<ShelvesCategoryResultBean> result) {
        ShelvesCategoryResultBean resultBean = result.get(0);
        if (resultBean != null) {
            if (mShelfCategoryAdapter == null) {
                mShelfCategoryAdapter = new ShelfCategoryAdapter(ShelfDetailActivity.this, resultBean.getCategories());
                mShelfCategoryAdapter.setShelfCategoryListener((shelfId, categoryId, categoryName) -> {
                    //跳转书架页面
                    Intent intent = new Intent(ShelfDetailActivity.this, BookCategoryDetailActivity.class);
                    intent.putExtra(BookShelfConstant.SHELF_ID, shelfId);
                    intent.putExtra(BookShelfConstant.CATEGORY_ID, categoryId);
                    intent.putExtra(BookShelfConstant.CATEGORY_NAME, categoryName);
                    startActivity(intent);
                });
                mRvCategory.setAdapter(mShelfCategoryAdapter);
            } else {
                mShelfCategoryAdapter.setCategoryEntity(resultBean.getCategories());
            }
        }
    }

    @Override
    public void showTips(String message) {

    }

    /**
     * 设置View的进场动画
     */
    private void startViewAnimation() {
        if (isHideRvCategory) {
            mRvShelfIntro.scrollToPosition(0);
            Log.d(TAG, "startViewAnimation: RecyclerView is visible >>> ");
            //隐藏九宫格,显示书柜简介
            mRvCategory.startAnimation(mCategory_Scale_out);
            mCategory_Scale_out.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    mRvCategory.setVisibility(View.GONE);
                    mRvShelfIntro.startAnimation(mShelfIntro_Translate_in);
                    mRvShelfIntro.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });

            isHideRvCategory = false;

        } else {
            Log.d(TAG, "startViewAnimation: RecyclerView is gone >>> ");
            //隱藏WebView,显示九宫格
            mRvShelfIntro.startAnimation(mShelfIntro_Translate_out);
            mShelfIntro_Translate_out.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    mRvShelfIntro.setVisibility(View.GONE);
                    mRvCategory.setVisibility(View.VISIBLE);
                    mRvCategory.startAnimation(mCategory_Scale_in);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
            isHideRvCategory = true;
        }
    }

    @Override
    protected void onDestroy() {
        //去除View动画
        mRvCategory.clearAnimation();
        mRvShelfIntro.clearAnimation();
        if (mIvLogo_clicks != null && mIvLogo_clicks.isDisposed()) {
            mIvLogo_clicks.dispose();
        }
        if (mIvCopyRight_clicks != null && mIvCopyRight_clicks.isDisposed()) {
            mIvCopyRight_clicks.dispose();
        }
        super.onDestroy();
    }
}
