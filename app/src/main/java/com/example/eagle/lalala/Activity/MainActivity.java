package com.example.eagle.lalala.Activity;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eagle.lalala.Edit_marks_aty;
import com.example.eagle.lalala.Fragment.MapFragment;
import com.example.eagle.lalala.NetWork.HttpUtil;
import com.example.eagle.lalala.PacelForConvey.ConveyJson;
import com.example.eagle.lalala.PictureWork.HandlePicture;
import com.example.eagle.lalala.PictureWork.TakePicture;
import com.example.eagle.lalala.R;
import com.example.eagle.lalala.Fragment.SharedFragment;
import com.example.eagle.lalala.SQL.WeMarkDatabaseHelper;
import com.example.eagle.lalala.Service.WorkWithDatabase;
import com.example.eagle.lalala.utils.CommonUtils;
import com.example.eagle.lalala.utils.DatasUtil;
import com.example.neilhy.floatingbutton_library.FloatingActionButton;
import com.example.neilhy.floatingbutton_library.FloatingActionMenu;

import org.json.JSONException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int TAKE_PHOTO=1;
    public static long userId=0;//用户的id
    public static String userName="";//用户的昵称

    @Bind(R.id.textView_title_map)
    TextView mTextViewTitleMap;
    @Bind(R.id.textView_title_list)
    TextView mTextViewTitleList;
    TextView mUserName;
    ImageView mUserIcon;
    @Bind(R.id.btn_contacts_in_MainActivity)
    ImageButton mBtnSearchInMainActivity;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.single_frag_container)
    FrameLayout mSingleFragContainer;
    @Bind(R.id.fab_add)
    FloatingActionButton fab_add;
    @Bind(R.id.fab_camera)
    FloatingActionButton fab_camera;
    @Bind(R.id.menu_button)
    FloatingActionMenu menuButton;
    @Bind(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @Bind(R.id.navigation_view)
    NavigationView mNavigationView;

    private Handler mUiHandler=new Handler();
    private File imageFile;

    private Fragment mMapFrgment;
    private Fragment mRecommendedFragment;
    private Fragment mFocusedFragment;
    private Fragment mPostedFragment;
    private Fragment mFavoriteFragment;

    private WorkWithDatabase.AccessDatabaseBinder accessDatabaseBinder;//对后台的绑定
    private ServiceConnection connection;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        init();
        saveUserInfo();//启用后台获取数据库中用户的数据
    }
    @Override
    protected void onStart() {
        super.onStart();
        takeUserIconAndName();
    }
    private void init() {

        mMapFrgment = new MapFragment();
        mRecommendedFragment = new SharedFragment();
        mFocusedFragment = new SharedFragment();
        mPostedFragment = new SharedFragment();
        mFavoriteFragment = new SharedFragment();

        getSupportFragmentManager().beginTransaction()
               // .add(R.id.single_frag_container,mRecommendedFragment,"recommended_frag")
                .add(R.id.single_frag_container,mMapFrgment,"map_frag")
//                .add(R.id.single_frag_container,mFocusedFragment,"focused_frag")
//                .add(R.id.single_frag_container,mFavoriteFragment,"favorite_frag")
//                .add(R.id.single_frag_container,mPostedFragment,"posted_frag")
//                .hide(mFavoriteFragment)
//                .hide(mPostedFragment)
//                .hide(mRecommendedFragment)
//                .hide(mFocusedFragment)
                .commit();

        RelativeLayout drawerHeaderLayout= (RelativeLayout) mNavigationView.getHeaderView(0);
        mUserIcon= (ImageView) drawerHeaderLayout.getChildAt(0);
        mUserName = (TextView) drawerHeaderLayout.getChildAt(1);
        mUserIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,ModifyUserInfo.class));
            }
        });
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                mDrawerLayout.closeDrawer(GravityCompat.START);

                switch (item.getItemId()) {
//                    case R.id.menu_message:
//                        Toast.makeText(MainActivity.this,"message",Toast.LENGTH_SHORT).show();
//                        break;
//                   // case R.id.menu_favorite:
//                      //  CommonUtils.changeFrag(MainActivity.this,"favorite_frag");
//                  //      break;
//                    case R.id.menu_setting:
//
//                        break;
                    default:

                        break;
                }


                return true;
            }
        });
        setSupportActionBar(mToolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        fab_add.setOnClickListener(this);
        fab_camera.setOnClickListener(this);

        menuButton.setClosedOnTouchOutside(true);//点击外部可以关闭选项
        menuButton.hideMenuButton(false);

        mUiHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                menuButton.showMenuButton(true);
            }
        },500);//让这个按钮500毫秒之后显示出来

        TakePicture.createCustomAnimation(menuButton);//设置点击按钮后的动画，星星变叉
    }



    private void takeUserIconAndName() {
        WeMarkDatabaseHelper helper = new WeMarkDatabaseHelper(MainActivity.this, "WeMark.db", null, 1);
        SQLiteDatabase db=helper.getReadableDatabase();
        db.beginTransaction();
        try {
            Cursor cursor = db.query(WeMarkDatabaseHelper.USER_TABLE, null, "userId=" + userId, null, null, null, null);
            if (cursor.moveToFirst()) {
                do {
                    if (cursor.getString(cursor.getColumnIndex("userName")) != null && !cursor.getString(cursor.getColumnIndex("userName")).equals("")) {
                        userName=cursor.getString(cursor.getColumnIndex("userName"));
                        mUserName.setText(cursor.getString(cursor.getColumnIndex("userName")));
                    }
                    if (cursor.getString(cursor.getColumnIndex("icon")) != null && !cursor.getString(cursor.getColumnIndex("icon")).equals("")) {

//                        mUserIcon.setImageBitmap(BitmapFactory.decodeFile( cursor.getString(cursor.getColumnIndex("icon")) ));
                        mUserIcon.setImageBitmap(HandlePicture.decodeSampleBitmapFromPath(cursor.getString(cursor.getColumnIndex("icon")),60,120));
                    }
                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            db.endTransaction();
        }
        db.close();
    }



    private void takePhoto(){
        imageFile= HandlePicture.createFileForPhoto();//创建图片路径
        Intent intent=new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imageFile));
        startActivityForResult(intent,TAKE_PHOTO);//启动相机程序
    }

    @OnClick({R.id.textView_title_map, R.id.textView_title_list, R.id.btn_contacts_in_MainActivity})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.textView_title_map:
                CommonUtils.changeFrag(MainActivity.this,"map_frag");
                break;
            case R.id.textView_title_list:
                CommonUtils.changeFrag(MainActivity.this,"focused_frag");
                break;
            case R.id.btn_contacts_in_MainActivity:
                startActivity(new Intent(MainActivity.this,ContactActivity.class));
                break;
            case R.id.fab_add:
                Intent intent = new Intent(MainActivity.this, Edit_marks_aty.class);
                startActivity(intent);
                break;
            case R.id.fab_camera:
                takePhoto();
                break;
        }
        menuButton.close(true);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode != RESULT_OK)
            return;
        switch (requestCode) {
            case TAKE_PHOTO:
                    Edit_marks_aty.actionStart(MainActivity.this,imageFile.getAbsolutePath(),userId);
                    break;
        }
    }

    @Override
    protected void onDestroy() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout != null && mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void saveUserInfo(){
        ConveyJson userJson=getIntent().getParcelableExtra("userInfo");
        final HashMap<String,Object> userInfo=new HashMap<>();
        try {
            userName=userJson.object.getString("userName");

            String icon= userJson.object.getString("icon");
            String background = userJson.object.getString("background");

            userId = userJson.object.getLong("userID");
            Log.i("MainActivity:::userId:", userId + "");
            Log.i("MainActivity:::object:", userJson.object.toString());

            userInfo.put("userId",userId);
            userInfo.put("email", userJson.object.getString("emailAddr"));
            userInfo.put("userName", userName);
            userInfo.put("password", userJson.object.getString("password"));
            if (icon != null && !icon.equals("")) {
                userInfo.put("icon", icon);
            }
            if (background != null && !background.equals("")) {
                userInfo.put("background", background);
            }
            userInfo.put("signature", userJson.object.getString("signature"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
//        connection=new ServiceConnection() {
//            @Override
//            public void onServiceConnected(ComponentName name, IBinder service) {
//                accessDatabaseBinder= (WorkWithDatabase.AccessDatabaseBinder) service;
//                accessDatabaseBinder.saveUserInfo(MainActivity.this,userInfo);
////                accessDatabaseBinder.saveUserInfo(MainActivity.this);
//
//            }
//
//            @Override
//            public void onServiceDisconnected(ComponentName name) {
//
//            }
//        };
        new saveUserInfos().execute(userInfo);
    }

    private class saveUserInfos extends AsyncTask<HashMap<String,Object>, Void, String> {
        @Override
        protected String doInBackground(HashMap<String,Object>... params) {
            saveUserInfo(params[0]);

//            Intent bindIntent = new Intent(MainActivity.this, WorkWithDatabase.class);
//            bindService(bindIntent, params[0], BIND_AUTO_CREATE);//绑定服务
//            try {
//                Thread.sleep(2000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("正在初始化,请稍候...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(String str) {
            progressDialog.dismiss();
//            unbindService(connection);
 //           Toast.makeText(MainActivity.this, "初始化成功", Toast.LENGTH_LONG).show();
        }
    }

    public void saveUserInfo(final HashMap<String, Object> userInfo){
        WeMarkDatabaseHelper databaseHelper = new WeMarkDatabaseHelper(MainActivity.this, "WeMark.db", null, 1);
        final SQLiteDatabase db=databaseHelper.getWritableDatabase();
                db.beginTransaction();
                try {
                    int i=db.delete(WeMarkDatabaseHelper.USER_TABLE, null, null);//删除表的数据
                    Log.i("Database::", "delete "+i);
                    ContentValues values = new ContentValues();
                    boolean flag = false;
                    Log.i("Database::userId:",userId+" "+ userInfo.get("userId"));
                    if ((long) userInfo.get("userId") != 0) {
                        values.put(WeMarkDatabaseHelper.USER_ID, (long) userInfo.get("userId"));
                        flag = true;//标志有userId，不为空才可以插入
                        Log.i("Database:save:", "userId "+(long)userInfo.get("userId"));

                    }
                    if (userInfo.get("email") != null && !userInfo.get("email").toString().equals("")) {
                        values.put(WeMarkDatabaseHelper.EMAIL, userInfo.get("email").toString());
                        Log.i("Database:save:", "email "+userInfo.get("email").toString());

                    }
                    if (userInfo.get("userName") != null && !userInfo.get("userName").toString().equals("")) {
                        values.put(WeMarkDatabaseHelper.USER_NAME, userInfo.get("userName").toString());
                        Log.i("Database:save:", "userName "+userInfo.get("userName").toString());
                    }
                    if (userInfo.get("password") != null && !userInfo.get("password").toString().equals("")) {
                        values.put(WeMarkDatabaseHelper.PASSWORD, userInfo.get("password").toString());
                    }
                    if (userInfo.get("icon") != null && !userInfo.get("icon").toString().equals("")) {
//                                Bitmap bitmap = HandlePicture.StringToBitmap(userInfo.get("icon").toString());
//                                values.put(WeMarkDatabaseHelper.ICON, HandlePicture.bitmapTobyte(bitmap));
                        values.put(WeMarkDatabaseHelper.ICON,userInfo.get("icon").toString());
                    }
                    if (userInfo.get("background") != null && !userInfo.get("background").toString().equals("")) {
//                                Bitmap bitmap = HandlePicture.StringToBitmap(userInfo.get("background").toString());
//                                values.put(WeMarkDatabaseHelper.BACKGROUND, HandlePicture.bitmapTobyte(bitmap));
                        values.put(WeMarkDatabaseHelper.BACKGROUND,userInfo.get("background").toString());
                    }
                    if (userInfo.get("signature") != null) {
                        values.put(WeMarkDatabaseHelper.SIGNATURE, userInfo.get("signature").toString());
                    }
                    if (flag) {
                        db.insert(WeMarkDatabaseHelper.USER_TABLE, null, values);
                    }
                    db.setTransactionSuccessful();
                    Log.i("Database:save:", "transaction success");
                } catch (Exception e) {
                    e.printStackTrace();
                }finally {
                    db.endTransaction();
                }

        db.close();
    }
}
