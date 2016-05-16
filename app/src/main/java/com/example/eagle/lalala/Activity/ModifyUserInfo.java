package com.example.eagle.lalala.Activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eagle.lalala.NetWork.HttpCallbackListener;
import com.example.eagle.lalala.NetWork.HttpUtil;
import com.example.eagle.lalala.PictureWork.HandlePicture;
import com.example.eagle.lalala.PictureWork.TakePicture;
import com.example.eagle.lalala.R;
import com.example.eagle.lalala.SQL.WeMarkDatabaseHelper;
import com.example.eagle.lalala.Service.WorkWithDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;

/**
 * Created by NeilHY on 2016/4/26.
 */
public class ModifyUserInfo extends AppCompatActivity implements View.OnClickListener,DialogEditUserString.EditInputListener {

    private static final String serviceUrl="http://119.29.198.149:8080/updateUser";

    private LinearLayout iconLayout;
    private LinearLayout nickNameLayout;
    private LinearLayout emailLayout;
    private LinearLayout signatureLayout;
    private ImageView backgroundIv;
    private ImageView iconIv;
    private TextView nickNameTv;
    private TextView emailTv;
    private TextView signatureTv;
    private static final long userId=MainActivity.userId;

    private WorkWithDatabase.AccessDatabaseBinder accessDatabaseBinder;//对后台的绑定
    private ServiceConnection connection;
    private ProgressDialog progressDialog;
    private HashMap<String,Object> userInfo=new HashMap<>();
    private String password;

    private Handler handler = new Handler(){
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    upDateUserInfo(userInfo);
//                    try{
//                        Thread.sleep(1000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
                    progressDialog.dismiss();
                    Toast.makeText(ModifyUserInfo.this,"上传成功！",Toast.LENGTH_SHORT).show();
//                    unbindService(connection);
                    ModifyUserInfo.this.finish();
                    break;
                case -1:
                    progressDialog.dismiss();
                    Toast.makeText(ModifyUserInfo.this,"上传失败！",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    public static final int BackgroundTAKE_PHOTO=1;
    public static final int BackgroundPICK_PHOTO=2;
    public static final int BackgroundCROP_PHOTO=3;
    public static final int IconTAKE_PHOTO=4;
    public static final int IconPICK_PHOTO=5;
    public static final int IconCROP_PHOTO=6;

    private String imageCamera;
    private File iconFile;
    private File backgroundFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifyuserinfo);
        initView();
        getUserInfo();
    }

    private void initView() {
        iconLayout = (LinearLayout) findViewById(R.id.modify_icon_layout);
        nickNameLayout = (LinearLayout) findViewById(R.id.modify_nickname_layout);
        signatureLayout = (LinearLayout) findViewById(R.id.modify_signature_layout);
        backgroundIv = (ImageView) findViewById(R.id.modify_background_iv);
        iconIv = (ImageView) findViewById(R.id.modify_icon_iv);
        nickNameTv = (TextView) findViewById(R.id.modify_nickname_tv);
        emailTv = (TextView) findViewById(R.id.modify_email_tv);
        signatureTv = (TextView) findViewById(R.id.modify_signature_tv);

        backgroundIv.setOnClickListener(this);
        iconLayout.setOnClickListener(this);
        nickNameLayout.setOnClickListener(this);
        signatureLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.modify_background_iv:
                BackgroundChoosePhotoDialog();
                break;
            case R.id.modify_icon_layout:
                IconChoosePhotoDialog();
                break;
            case R.id.modify_nickname_layout:
                showEditStringDialog("昵称",1);
                break;
            case R.id.modify_signature_layout:
                showEditStringDialog("签名",2);

                break;
        }
    }

    public void showEditStringDialog(String title,int i)
    {
        Bundle bundle=new Bundle();
        bundle.putString("title",title);
        bundle.putInt("flag",i);
        DialogEditUserString dialog = new DialogEditUserString();
        dialog.setArguments(bundle);
        dialog.show(getFragmentManager(),title);

    }

    @Override
    public void onEditInputComplete(String info,int i) {
        if (info != null) {

            if (i == 1) {
                nickNameTv.setText(info);
            }else{
                signatureTv.setText(info);
            }
        } else {
            Toast.makeText(ModifyUserInfo.this, "请输入内容…", Toast.LENGTH_SHORT).show();
        }
    }

    private void BackgroundChoosePhotoDialog() {
        AlertDialog Builder=new AlertDialog.Builder(this).setTitle("请选择：")
                .setSingleChoiceItems(
                        new String[] { "相 册", "拍 照" }, -1,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:
                                        Intent intentPick = new Intent("android.intent.action.GET_CONTENT");
                                        intentPick.setType("image/*");
                                        startActivityForResult(intentPick,BackgroundPICK_PHOTO);//打开相册
                                        break;
                                    case 1:
                                        backgroundFile=HandlePicture.createFileForBackground();
                                        Intent intentCamara=new Intent("android.media.action.IMAGE_CAPTURE");
                                        intentCamara.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(backgroundFile));
                                        startActivityForResult(intentCamara,BackgroundTAKE_PHOTO);//启动相机程序
                                        break;
                                }
                                dialog.dismiss();
                            }
                        }).setNegativeButton("取消", null).show();
    }

    private void IconChoosePhotoDialog() {
        AlertDialog Builder=new AlertDialog.Builder(this).setTitle("请选择：")
                .setSingleChoiceItems(
                        new String[] { "相 册", "拍 照" }, -1,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:
                                        Intent intentPick = new Intent("android.intent.action.GET_CONTENT");
                                        intentPick.setType("image/*");
                                        startActivityForResult(intentPick,IconPICK_PHOTO);//打开相册
                                        break;
                                    case 1:
                                        iconFile=HandlePicture.createFileForIcon();
                                        Intent intentCamara=new Intent("android.media.action.IMAGE_CAPTURE");
                                        intentCamara.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(iconFile));
                                        startActivityForResult(intentCamara,IconTAKE_PHOTO);//启动相机程序
                                        break;
                                }
                                dialog.dismiss();
                            }
                        }).setNegativeButton("取消", null).show();
    }

    public void getUserInfo() {
        WeMarkDatabaseHelper databaseHelper = new WeMarkDatabaseHelper(ModifyUserInfo.this, "WeMark.db", null, 1);
        final SQLiteDatabase db=databaseHelper.getReadableDatabase();
        Cursor cursor = db.query(WeMarkDatabaseHelper.USER_TABLE, null, "userId=" + MainActivity.userId, null, null, null, null);
        Log.i("Modify::getinfo:", "cursor started");
        if (cursor.moveToFirst()) {
            do {
                if (cursor.getString(cursor.getColumnIndex("email")) != null) {
                    emailTv.setText(cursor.getString(cursor.getColumnIndex("email")));
                    Log.i("Modify::getinfo:", cursor.getString(cursor.getColumnIndex("email")));
                }else{
                    Log.i("Modify::getinfo:", "email null");
                }
                if (cursor.getString(cursor.getColumnIndex("userName")) != null) {
                    nickNameTv.setText(cursor.getString(cursor.getColumnIndex("userName")));
                    Log.i("Modify::getinfo:", cursor.getString(cursor.getColumnIndex("userName")));
                }else{
                    Log.i("Modify::getinfo:", "userName null");
                }
                if (cursor.getString(cursor.getColumnIndex("signature")) != null) {
                    signatureTv.setText(cursor.getString(cursor.getColumnIndex("signature")));
                }
                if (cursor.getString(cursor.getColumnIndex("password")) != null) {
                    password=cursor.getString(cursor.getColumnIndex("password"));
                    Log.i("Modify::getinfo:", cursor.getString(cursor.getColumnIndex("password")));
                }else{
                    Log.i("Modify::getinfo:", "password null");
                }
                if (cursor.getString(cursor.getColumnIndex("icon")) != null&& !cursor.getString(cursor.getColumnIndex("icon")).equals("")) {
                    Bitmap bitmap1 = HandlePicture.decodeSampleBitmapFromPath(cursor.getString(cursor.getColumnIndex("icon")), 60, 120);
                    iconIv.setImageBitmap(bitmap1);
                }
                if (cursor.getString(cursor.getColumnIndex("background")) != null&& !cursor.getString(cursor.getColumnIndex("background")).equals("")) {
                    Bitmap bitmap2 = HandlePicture.decodeSampleBitmapFromPath(cursor.getString(cursor.getColumnIndex("background")), 120, 240);
                    backgroundIv.setImageBitmap(bitmap2);
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
    }

//    private void upDateUserInfo() {
//        connection=new ServiceConnection() {
//            @Override
//            public void onServiceConnected(ComponentName name, IBinder service) {
//                accessDatabaseBinder= (WorkWithDatabase.AccessDatabaseBinder) service;
//                accessDatabaseBinder.upDateUserInfo(ModifyUserInfo.this,userInfo);
//            }
//
//            @Override
//            public void onServiceDisconnected(ComponentName name) {
//
//            }
//        };
//        Intent bindIntent = new Intent(ModifyUserInfo.this, WorkWithDatabase.class);
//        bindService(bindIntent, connection, BIND_AUTO_CREATE);//绑定服务
//    }

    public void upDateUserInfo(final HashMap<String, Object> userInfo) {
        WeMarkDatabaseHelper databaseHelper = new WeMarkDatabaseHelper(ModifyUserInfo.this, "WeMark.db", null, 1);
        final SQLiteDatabase db = databaseHelper.getWritableDatabase();

                db.beginTransaction();
                try {
                    ContentValues values = new ContentValues();
                    boolean flag = false;
                    if ((long) userInfo.get("userId") != 0) {
                        values.put(WeMarkDatabaseHelper.USER_ID, (long) userInfo.get("userId"));
                        flag = true;//标志有userId，不为空才可以插入
                        Log.i("Database::", "userId "+(long)userInfo.get("userId"));
                    }
                    if (userInfo.get("email") != null && !userInfo.get("email").toString().equals("")) {
                        values.put(WeMarkDatabaseHelper.EMAIL, userInfo.get("email").toString());
                        Log.i("Database::", "email "+userInfo.get("email").toString());

                    }
                    if (userInfo.get("userName") != null && !userInfo.get("userName").toString().equals("")) {
                        values.put(WeMarkDatabaseHelper.USER_NAME, userInfo.get("userName").toString());
                        Log.i("Database::", "userName "+userInfo.get("userName").toString());

                    }
                    if (userInfo.get("password") != null && !userInfo.get("password").toString().equals("")) {
                        values.put(WeMarkDatabaseHelper.PASSWORD, userInfo.get("password").toString());
                    }
                    if (userInfo.get("icon") != null && !userInfo.get("icon").toString().equals("")) {
//                            Bitmap bitmap = HandlePicture.StringToBitmap(userInfo.get("icon").toString());
//                            values.put(WeMarkDatabaseHelper.ICON, HandlePicture.bitmapTobyte(bitmap));
                        values.put(WeMarkDatabaseHelper.ICON, userInfo.get("icon").toString());
                    }
                    if (userInfo.get("background") != null && !userInfo.get("background").toString().equals("")) {
//                            Bitmap bitmap = HandlePicture.StringToBitmap(userInfo.get("background").toString());
//                            values.put(WeMarkDatabaseHelper.BACKGROUND, HandlePicture.bitmapTobyte(bitmap));
                        values.put(WeMarkDatabaseHelper.BACKGROUND, userInfo.get("background").toString());
                    }
                    if (userInfo.get("signature") != null) {
                        values.put(WeMarkDatabaseHelper.SIGNATURE, userInfo.get("signature").toString());
                    }
                    if (flag) {
                        db.update(WeMarkDatabaseHelper.USER_TABLE, values, "userId=" + userInfo.get("userId"), null);
                    }
                    db.setTransactionSuccessful();
                    Log.i("Database::", "transaction success ");
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    db.endTransaction();
                }

        db.close();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode != RESULT_OK)
            return;
        switch (requestCode) {
            case BackgroundTAKE_PHOTO:
                Intent backgroundintent = new Intent("com.android.camera.action.CROP");
                backgroundintent.setDataAndType(Uri.fromFile(backgroundFile), "image/*");
                backgroundintent.putExtra("scale", true);
                backgroundintent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(backgroundFile));
                startActivityForResult(backgroundintent,BackgroundCROP_PHOTO);
                break;
            case BackgroundPICK_PHOTO:
                    //判断手机系统版本号
                    if (Build.VERSION.SDK_INT >= 19) {
                        //4.4及以上系统使用这个方法处理图片
                        imageCamera= TakePicture.handleImageOnKitKat(ModifyUserInfo.this, data);
                        backgroundFile = new File(imageCamera);
                        Bitmap bitmap1 = HandlePicture.decodeSampleBitmapFromPath(imageCamera, 120, 240);
                        backgroundIv.setImageBitmap(bitmap1);
                    }else{
                        //4.4以下系统使用这个方法处理图片
                        imageCamera = TakePicture.handleImageBeforeKitKat(ModifyUserInfo.this, data);
                        backgroundFile = new File(imageCamera);
                        Bitmap bitmap1 = HandlePicture.decodeSampleBitmapFromPath(imageCamera, 120, 240);
                        backgroundIv.setImageBitmap(bitmap1);
                    }
                break;
            case BackgroundCROP_PHOTO:
                Bitmap bitmap3 = HandlePicture.decodeSampleBitmapFromPath(backgroundFile.getAbsolutePath(), 120, 240);
                backgroundIv.setImageBitmap(bitmap3);
                break;
            case IconTAKE_PHOTO:
                Intent intent = new Intent("com.android.camera.action.CROP");
                intent.setDataAndType(Uri.fromFile(iconFile), "image/*");
                intent.putExtra("scale", true);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(iconFile));
                startActivityForResult(intent,IconCROP_PHOTO);
                break;
            case IconPICK_PHOTO:
                //判断手机系统版本号
                if (Build.VERSION.SDK_INT >= 19) {
                    //4.4及以上系统使用这个方法处理图片
                    imageCamera= TakePicture.handleImageOnKitKat(ModifyUserInfo.this, data);
                    iconFile = new File(imageCamera);
                    Bitmap bitmap2 = HandlePicture.decodeSampleBitmapFromPath(imageCamera, 100, 200);
                    iconIv.setImageBitmap(bitmap2);
                }else{
                    //4.4以下系统使用这个方法处理图片
                    imageCamera = TakePicture.handleImageBeforeKitKat(ModifyUserInfo.this, data);
                    iconFile = new File(imageCamera);
                    Bitmap bitmap2 = HandlePicture.decodeSampleBitmapFromPath(imageCamera, 100, 200);
                    iconIv.setImageBitmap(bitmap2);
                }
                break;
            case IconCROP_PHOTO:
                Bitmap bitmap4 = HandlePicture.decodeSampleBitmapFromPath(iconFile.getAbsolutePath(), 100, 200);
                iconIv.setImageBitmap(bitmap4);
                break;
        }
    }

    @Override
    protected void onDestroy() {
//        unbindService(connection);
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_mark_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(MainActivity.userId != 0) {
            if ( emailTv != null ) {

                JSONObject object = new JSONObject();
                try {
                    String signature = null;
                    String nickName=null;
                    try {
                        signature=new String(signatureTv.getText().toString().getBytes(),"UTF-8");
                        nickName=new String(nickNameTv.getText().toString().getBytes(),"UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    object.put("userID", userId);
                    object.put("emailAddr", emailTv.getText().toString());
//                    object.put("userName", HttpUtil.toUTFString(nickNameTv.getText().toString()));
                    object.put("password", password);
                    if (((BitmapDrawable) iconIv.getDrawable()).getBitmap() != null) {
                        String icon = HandlePicture.bitmapToString(((BitmapDrawable) iconIv.getDrawable()).getBitmap());
                        object.put("icon", icon);
                        Log.i("Modify_info:::","ModifyInSended:::"+ icon);
                    }
                    if (((BitmapDrawable) backgroundIv.getDrawable()).getBitmap() != null) {
                        String background = HandlePicture.bitmapToString(((BitmapDrawable) backgroundIv.getDrawable()).getBitmap());
                        object.put("background",background);
                    }
//                    object.put("signature", HttpUtil.toUTFString(signatureTv.getText().toString()));
                        object.put("signature", signature);
                        object.put("userName", nickName);


                    userInfo.put("userId", userId);
                    userInfo.put("email", emailTv.getText().toString());

                        userInfo.put("userName", nickName);
                        userInfo.put("signature", signature);
                    Log.i("Modify::userInfo:", nickName +"----"+ signature);

                    if (iconFile != null) {
                        userInfo.put("icon", iconFile.getAbsolutePath());
                    }
                    if (backgroundFile != null) {
                        userInfo.put("background",backgroundFile.getAbsolutePath());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.i("Modify::object:", object.toString());
                new ModifyInfo().execute(object);//用异步处理上传操作
            } else {
                Toast.makeText(ModifyUserInfo.this, "邮箱为空，请重新登陆…", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(ModifyUserInfo.this, "请登录...", Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    private class ModifyInfo extends AsyncTask<JSONObject, Void, String> {
        private String status;
        private String info;
        @Override
        protected String doInBackground(JSONObject... params) {
            HttpUtil.getJsonArrayByHttp(serviceUrl, params[0], new HttpCallbackListener() {
                @Override
                public void onFinishGetJson(JSONObject jsonObject) {
                    if (jsonObject == null) {
                        Log.i("status", "json:null" );
                    } else if(jsonObject != null){
                        try {
                            Log.i("status", "json:"+jsonObject.toString() );
                            status =jsonObject.getString("status");
                            info = jsonObject.getString("info");
                            Log.i("status1", "status:" + status + " info:" + info);
                            Log.i("Modify_info:::","ModifyInReceived:::"+ jsonObject.get("icon").toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    Message message=new Message();
                    if (status.equals("1") && info.equals("OK")) {
                        message.what=1;
                    }else{
                        message.what=-1;
                    }
                    handler.sendMessage(message);
                }

                @Override
                public void onFinishGetString(String response) {

                }

                @Override
                public void onError(Exception e) {
//                    Log.e("LoginFrag", e.getMessage());
                    status="0";
                }
            });
            return null;
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(ModifyUserInfo.this);
            progressDialog.setMessage("正在上传,请稍候...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {

        }
    }



}
