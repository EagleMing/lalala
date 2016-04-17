package com.example.eagle.lalala;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by eagle on 2016/4/9.
 */
public abstract class SingleFragmentActivity extends AppCompatActivity {

    protected abstract Fragment creatFragment();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frag0_single);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.single_frag_container);

        if(fragment == null){
            fragment = creatFragment();
            fm.beginTransaction().add(R.id.single_frag_container,fragment)
                                 .commit();
        }
    }

    protected void changeFrag(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.single_frag_container, fragment).commit();
    }

}
