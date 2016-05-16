package com.example.eagle.lalala;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.example.eagle.lalala.Activity.MainActivity;
import com.example.eagle.lalala.NetWork.HttpCallbackListener;
import com.example.eagle.lalala.NetWork.HttpUtil;
import com.example.eagle.lalala.PictureWork.HandlePicture;
import com.example.eagle.lalala.PictureWork.TakePicture;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * Created by NeilHY on 2016/4/19.
 */
public class Edit_marks_aty extends AppCompatActivity implements View.OnClickListener {

    private static final String serviceUrl="http://119.29.198.149:8080/createMark";
    private static final long userId= MainActivity.userId;//用户的id
    public static final int PICK_PHOTO=2;
    private static final int AUTHORITY_PUBLIC=0;
    private static final int AUTHORITY_PRIVATE=1;
    private static final int AUTHORITY_SOME=2;
    private static int AUTHORITY_TYPE=AUTHORITY_PUBLIC;

    private String imageCamera;
    private ImageButton addPhotoBtn;
    private EditText editMarkContent;
    private EditText editMarkName;
    private EditText editMarkAddress;
    private TextView textViewForCoordinate;
    private RadioGroup radioGroup;
    private RadioButton radio_public;
    private RadioButton radio_private;
    private RadioButton radio_some;
    private LinearLayout layoutForRadio_some;
    private ProgressDialog progressDialog;
    private AMapLocationClient mapLocationClient;

    private Handler handler = new Handler(){
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    progressDialog.dismiss();
                    Toast.makeText(Edit_marks_aty.this,"上传成功！",Toast.LENGTH_SHORT).show();
                    finish();
                    break;
                case -1:
                    progressDialog.dismiss();
                    Toast.makeText(Edit_marks_aty.this,"上传失败！",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    private ArrayList<CheckBox> checkBoxArrayList;

    public static void actionStart(Context context,String imagePath,long userId){
        Intent intent = new Intent(context, Edit_marks_aty.class);
        intent.putExtra("imagePath", imagePath);
        intent.putExtra("userId", userId);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_marks);

        initView();//初始化所有控件
        compressPhoto(getIntent());//压缩按钮上的图片


    }

    private void initView() {
        addPhotoBtn = (ImageButton) findViewById(R.id.imageBtn_editmark);
        editMarkContent= (EditText) findViewById(R.id.edittext_editmark);
        editMarkName = (EditText) findViewById(R.id.edit_marks_name);
        editMarkAddress = (EditText) findViewById(R.id.edit_marks_addr);
        textViewForCoordinate= (TextView) findViewById(R.id.tv_position);
        radioGroup= (RadioGroup) findViewById(R.id.radiogroup_editmark);
        radio_public= (RadioButton) findViewById(R.id.radiobtn_public);
        radio_private= (RadioButton) findViewById(R.id.radiobtn_private);
        radio_some= (RadioButton) findViewById(R.id.radiobtn_some);
        layoutForRadio_some= (LinearLayout) findViewById(R.id.radio_layout_some);
        radioGroup.check(radio_public.getId());//设置默认选中公开单选

        getSomeList();//获得部分好友的列表信息【可以让线程去执行耗时的获取列表操作】

        addPhotoBtn.setOnClickListener(this);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (group.getCheckedRadioButtonId()) {
                    case R.id.radiobtn_public:
                        layoutForRadio_some.setVisibility(View.GONE);
                        AUTHORITY_TYPE=AUTHORITY_PUBLIC;

                        break;
                    case R.id.radiobtn_private:
                        layoutForRadio_some.setVisibility(View.GONE);
                        AUTHORITY_TYPE=AUTHORITY_PRIVATE;

                        break;
                    case R.id.radiobtn_some:
                        layoutForRadio_some.setVisibility(View.VISIBLE);
                        AUTHORITY_TYPE=AUTHORITY_SOME;

                        break;
                }
            }
        });
        textViewForCoordinate.setText(getLocation());
    }

    private void compressPhoto(Intent intent){
        imageCamera = intent.getStringExtra("imagePath");
        if (imageCamera != null) {
            Bitmap bitmap2 = null;
//            try {
//                bitmap1 = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
            bitmap2 = HandlePicture.decodeSampleBitmapFromPath(imageCamera, 120,240);

            addPhotoBtn.setImageBitmap(bitmap2);
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            }

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PICK_PHOTO:
                if (resultCode == RESULT_OK) {
                    //判断手机系统版本号
                    if (Build.VERSION.SDK_INT >= 19) {
                        //4.4及以上系统使用这个方法处理图片
                        imageCamera=TakePicture.handleImageOnKitKat(Edit_marks_aty.this, data);
                    }else{
                        //4.4以下系统使用这个方法处理图片
                        imageCamera = TakePicture.handleImageBeforeKitKat(Edit_marks_aty.this, data);
                    }
                    if (imageCamera != null) {
                        Bitmap bitmap = HandlePicture.decodeSampleBitmapFromPath(imageCamera, 120,240);
                        addPhotoBtn.setImageBitmap(bitmap);
                    }else{
                        Toast.makeText(this, "failed to get image", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_mark_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(userId != 0) {
            if ( !editMarkName.getText().toString().equals("") && !editMarkAddress.getText().toString().equals("") ) {
//                imageCamera != null &&
                JSONObject object = new JSONObject();
                try {
                    object.put("userID", userId);           //用户的id
                    try {
                        object.put("content", new String(editMarkContent.getText().toString().getBytes(),"UTF-8"));
                        object.put("name", new String(editMarkName.getText().toString().getBytes(),"UTF-8"));
                        object.put("address", new String(editMarkAddress.getText().toString().getBytes(),"UTF-8"));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    if (imageCamera != null) {
                        object.put("photo", HandlePicture.bitmapToString(imageCamera));
                    }
                    object.put("location", getLocation());
                    object.put("authority", AUTHORITY_TYPE);
                    if (AUTHORITY_TYPE == AUTHORITY_SOME) {
                        JSONArray array = new JSONArray();
                        for (CheckBox checkBox : checkBoxArrayList) {
                            if (checkBox.isChecked()) {
                                JSONObject jsonObject = new JSONObject();
                                jsonObject.put("friendID", checkBox.getText().toString());
                                jsonObject.put("status", 0);
                                array.put(jsonObject);
                            }
                        }
                        object.put("checkedItemList", array);
                    }
                    object.put("checkedItemList", null);
                    Log.i("Edit_json:::", object.toString());////////////测试输出
                    Log.i("Edit_json:::object:::", object.get("address").toString());
                    Log.i("Edit_json:::edit:::", editMarkAddress.getText().toString());
                    Log.i("Edit_json:GBK:", HttpUtil.toGBK_UTFString(object.get("address").toString()));
                    Log.i("Edit_json:::utf:::", HttpUtil.toUTFString(object.get("address").toString()));
                    Log.i("Edit_json:gb2312:", HttpUtil.toGb2312_UTFString(object.get("address").toString()));
                    Log.i("Edit_json:::name:::", object.get("name").toString());
                    Log.i("Edit_json:::authority:", object.get("authority").toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                new UploadMark().execute(object);//用异步处理上传操作
            } else {
                Toast.makeText(Edit_marks_aty.this, "请选择要上传的图片或者填写完整信息…", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(Edit_marks_aty.this, "请登录...", Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    private String getLocation(){
//        private String location;  //坐标，支持点数据 , 规则：经度,纬度，经纬度支持到小数点后6位 格式示例：104.394729,31.125698
        mapLocationClient = new AMapLocationClient(this);
        AMapLocation aMapLocation = mapLocationClient.getLastKnownLocation();
        String latitude=aMapLocation.getLatitude()+"";
        String longtitude=aMapLocation.getLongitude()+",";
//        String longtitude=23.1231232+",";
//        String latitude=67.123123+"";
        return longtitude+latitude;
//        return "113.403294,23.051701";
    }
    @Override
    protected void onDestroy() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageBtn_editmark:
               // Toast.makeText(Edit_marks_aty.this,"onOptionsItemSelected",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent("android.intent.action.GET_CONTENT");
                intent.setType("image/*");
                startActivityForResult(intent,PICK_PHOTO);//打开相册
                break;
        }
    }

    private void getSomeList(){//获得部分好友的标签
        //向数据库中查找标签表
        checkBoxArrayList=new ArrayList<>();
        CheckBox box1 = new CheckBox(this);
        box1.setText("10");
        checkBoxArrayList.add(box1);
        CheckBox box2 = new CheckBox(this);
        box2.setText("11");
        checkBoxArrayList.add(box2);
        CheckBox box3 = new CheckBox(this);
        box3.setText("12");
        checkBoxArrayList.add(box3);
        CheckBox box4 = new CheckBox(this);
        box4.setText("7");
        checkBoxArrayList.add(box4);
        CheckBox box5 = new CheckBox(this);
        box5.setText("8");
        checkBoxArrayList.add(box5);

        //展示出来
        int i=0;
        LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        for(CheckBox checkBox:checkBoxArrayList){
            layoutForRadio_some.addView(checkBox,i,params);
            i++;
        }
        layoutForRadio_some.setVisibility(View.GONE);
    }

    //上传的异步类
    private class UploadMark extends AsyncTask<JSONObject,Void,String> {
        private String status;
        private String info;
        @Override
        protected String doInBackground(JSONObject... params) {

            HttpUtil.getJsonArrayByHttp(serviceUrl, params[0], new HttpCallbackListener() {
                @Override
                public void onFinishGetJson(JSONObject jsonObject) {
                    if (jsonObject != null) {
                        try {
                            status =jsonObject.getString("status");
                            info = jsonObject.getString("info");
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
                    Log.e("EditMarks", e.getMessage());
                    status="0";
                }
            });
            return null;
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(Edit_marks_aty.this);
            progressDialog.setMessage("正在提交,请稍候...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {

        }
    }
}
