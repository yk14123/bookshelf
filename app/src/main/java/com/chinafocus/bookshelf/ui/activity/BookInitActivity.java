package com.chinafocus.bookshelf.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.chinafocus.bookshelf.R;
import com.chinafocus.bookshelf.global.BookShelfConstant;
import com.chinafocus.bookshelf.utils.SpUtil;

/**
 * @author
 * @date 2018/12/21
 * description：
 */
public class BookInitActivity extends AppCompatActivity {

    private EditText mEt_init_location;
    private EditText mEt_init_location_confirm;
    private Button mBt_init_ok_confirm;
    private TextView mTv_location_temp_pre;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.bookshelf_activity_book_init);

        initView();

        initData();
    }

    private void initData() {

        String LocationId = SpUtil.getString(getApplicationContext(), BookShelfConstant.BOOK_INIT_LOCATION_ID);

        if (!TextUtils.isEmpty(LocationId)) {
            startActivity(new Intent(BookInitActivity.this, ShelfDetailActivity.class));
            finish();
        }

        String preLocationId = SpUtil.getString(getApplicationContext(), BookShelfConstant.BOOK_INIT_LOCATION_ID_TEMP);

        if (TextUtils.isEmpty(preLocationId)) {
            mTv_location_temp_pre.setText("旧的客户id是：还未输入");
        } else {
            mTv_location_temp_pre.setText("旧的客户id是：" + preLocationId);
        }

        mBt_init_ok_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String locationId = mEt_init_location.getText().toString();
                String locationIdConfirm = mEt_init_location_confirm.getText().toString();

                if (locationId.equals(locationIdConfirm) && locationId.length() > 5) {
                    SpUtil.setString(getApplicationContext(), BookShelfConstant.BOOK_INIT_LOCATION_ID, locationId);
                    SpUtil.setString(getApplicationContext(), BookShelfConstant.BOOK_INIT_LOCATION_ID_TEMP, locationId);
                    startActivity(new Intent(BookInitActivity.this, ShelfDetailActivity.class));

                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "两次输入编号不一致或者长度要大于6", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void initView() {
        mEt_init_location = findViewById(R.id.et_init_location);
        mEt_init_location_confirm = findViewById(R.id.et_init_location_confirm);
        mBt_init_ok_confirm = findViewById(R.id.bt_init_ok_confirm);
        mTv_location_temp_pre = findViewById(R.id.tv_location_temp_pre);
    }
}
