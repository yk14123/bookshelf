package com.chinafocus.bookshelf.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.chinafocus.bookshelf.R;
import com.chinafocus.bookshelf.presenter.bookinitportrait.BookInitPortraitPresenter;

/**
 * @author
 * @date 2018/12/21
 * description：
 */
public class BookInitPortraitActivity extends AppCompatActivity {

    private EditText mEt_init_location;
    private EditText mEt_init_location_confirm;
    private Button mBt_init_ok_confirm;
    private BookInitPortraitPresenter mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.bookshelf_activity_book_init);

        initView();
        mPresenter = new BookInitPortraitPresenter();
        mPresenter.initData(this);
    }

    public void initData() {

        mBt_init_ok_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.runClick();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.ClearActivity();
    }

    private void initView() {
        mEt_init_location = findViewById(R.id.et_init_location);
        mEt_init_location_confirm = findViewById(R.id.et_init_location_confirm);
        mBt_init_ok_confirm = findViewById(R.id.bt_init_ok_confirm);
    }

    public EditText getEt_init_location() {
        return mEt_init_location;
    }

    public EditText getEt_init_location_confirm() {
        return mEt_init_location_confirm;
    }
}
