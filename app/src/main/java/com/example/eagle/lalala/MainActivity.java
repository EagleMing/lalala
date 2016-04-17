package com.example.eagle.lalala;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MainActivity extends SingleFragmentActivity {


    @Bind(R.id.textView_titel_map)
    TextView mTextViewTitelMap;
    @Bind(R.id.textView_titel_list)
    TextView mTextViewTitelList;
    @Bind(R.id.btn_info_in_MainActivity)
    ImageButton mBtnInfoInMainActivity;
    @Bind(R.id.btn_search_in_MainActivity)
    ImageButton mBtnSearchInMainActivity;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.single_frag_container)
    FrameLayout mSingleFragContainer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frag0_single_with_titelbar);
        ButterKnife.bind(this);
        init();
        setSupportActionBar(mToolbar);
    }


    private void init() {


    }

    @Override
    protected Fragment creatFragment() {
        return new MapFragment();
    }

    @OnClick({R.id.textView_titel_map, R.id.textView_titel_list, R.id.btn_info_in_MainActivity, R.id.btn_search_in_MainActivity})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.textView_titel_map:
                mTextViewTitelMap.setTextColor(0xffffffff);
                mTextViewTitelList.setTextColor(0xb0ffffff);
                changeFrag(new MapFragment());
                break;
            case R.id.textView_titel_list:
                mTextViewTitelList.setTextColor(0xffffffff);
                mTextViewTitelMap.setTextColor(0xb0ffffff);
                changeFrag(new SharedFragment());
                break;
            case R.id.btn_info_in_MainActivity:
                startActivity(new Intent(MainActivity.this, InfoActivity.class));
                break;
            case R.id.btn_search_in_MainActivity:
                break;
        }
    }


}
