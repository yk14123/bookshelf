package com.chinafocus.bookshelf.ui.adapter;

import android.content.Context;
import android.net.http.SslError;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.chinafocus.bookshelf.R;
import com.chinafocus.bookshelf.utils.ManifestUtils;

/**
 * 书柜简介数据适配器
 *
 * @author HUANG_JIN
 * @version 1.0
 * create at 2018/11/26 14:47
 */
public class ShelfIntroAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "ShelfIntroAdapter";
    //ITEM类型
    private static final int WEB = 0;
    private static final int IMG = 1;

    private Context mContext;

    private int[] mShelfImages;

    public ShelfIntroAdapter(Context context) {
        this.mContext = context;
        this.mShelfImages = new int[]{
                R.drawable.bookshelf_case_intro1,
                R.drawable.bookshelf_case_intro2,
                R.drawable.bookshelf_case_intro3,
                R.drawable.bookshelf_case_intro4};
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return WEB;
        }
        return IMG;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == WEB) {
            View webContent = LayoutInflater.from(mContext).inflate(
                    R.layout.bookshelf_recycle_item_shelf_intro_web, parent, false);
            return new WebViewHolder(webContent);
        }
        View imageContent = LayoutInflater.from(mContext).inflate(
                R.layout.bookshelf_recycle_item_shelf_intro_image, parent, false);
        return new ImageHolder(imageContent);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof WebViewHolder) {
            //適配WebView
            WebViewHolder webViewHolder = (WebViewHolder) holder;
            webViewHolder.wvShelfWeb.setVerticalScrollBarEnabled(false);//不能垂直滑动
            webViewHolder.wvShelfWeb.setHorizontalScrollBarEnabled(false);//不能水平滑动
            //設置WebView默認的白色背景為透明色
            webViewHolder.wvShelfWeb.setBackgroundColor(0);
            webViewHolder.wvShelfWeb.setWebViewClient(new WebViewClient() {
                @Override
                public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                    handler.proceed();
                    super.onReceivedSslError(view, handler, error);
                }
            });
            WebSettings webSettings = webViewHolder.wvShelfWeb.getSettings(); //声明WebSettings子类
            webSettings.setDomStorageEnabled(true);  //设置适应Html5的一些方法
            //设置自适应屏幕，两者合用
            webSettings.setUseWideViewPort(true); //将图片调整到适合WebView的大小
            webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
            //缩放操作
            webSettings.setSupportZoom(false); //支持缩放，默认为true。是下面那个的前提。
            webSettings.setBuiltInZoomControls(false); //设置内置的缩放控件。若为false，则该WebView不可缩放
            webSettings.setDisplayZoomControls(false); //隐藏原生的缩放控件
            webSettings.setTextZoom(150);   //设置显示的字体大小
            //加载本地文件
            webViewHolder.wvShelfWeb.loadUrl("file:///android_asset/bookcaseIntro.html");

//            //防止WebView导致的内存泄露
//            if (mWvIntro != null) {
//                // 如果先调用destroy()方法，则会命中
//                // if (isDestroyed()) return;这一行代码，需要先onDetachedFromWindow()，再destroy()
//                ViewParent parent = mWvIntro.getParent();
//                if (parent != null) {
//                    ((ViewGroup) parent).removeView(mWvIntro);
//                }
//                mWvIntro.stopLoading();
//                // 退出时调用此方法，移除绑定的服务，否则某些特定系统会报错
//                mWvIntro.getSettings().setJavaScriptEnabled(false);
//                mWvIntro.clearHistory();
////            mWvIntro.clearView();
//                mWvIntro.removeAllViews();
//                mWvIntro.destroy();
//            }

        } else if (holder instanceof ImageHolder) {
            ImageHolder imageHolder = (ImageHolder) holder;
            Log.d(TAG, "onBindViewHolder: current position >>> " + position);
            imageHolder.ivShelfImage.setBackgroundResource(mShelfImages[position - 1]);
        }
    }

    /**
     * 获取drawable资源
     */
    private int getDrawableRes(String resName) {
        return mContext.getResources().getIdentifier(resName, "drawable", ManifestUtils.getPackageName(mContext));
    }

    @Override
    public int getItemCount() {
        return 5;
    }

    /**
     * 书柜Image圖片holder
     */
    static class ImageHolder extends RecyclerView.ViewHolder {
        private ImageView ivShelfImage;

        ImageHolder(@NonNull View itemView) {
            super(itemView);
            ivShelfImage = itemView.findViewById(R.id.iv_book_shelf_image);
        }
    }

    /**
     * 书柜WEB页面holder
     */
    static class WebViewHolder extends RecyclerView.ViewHolder {
        private WebView wvShelfWeb;

        WebViewHolder(View itemView) {
            super(itemView);
            wvShelfWeb = itemView.findViewById(R.id.wv_book_shelf_web);
        }
    }
}
