package com.example.eagle.lalala.Fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eagle.lalala.Activity.MainActivity;
import com.example.eagle.lalala.Activity.SignUpActivity;
import com.example.eagle.lalala.PictureWork.HandlePicture;
import com.example.eagle.lalala.R;
import com.example.eagle.lalala.NetWork.HttpCallbackListener;
import com.example.eagle.lalala.NetWork.HttpUtil;
import com.example.eagle.lalala.PacelForConvey.ConveyJson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by eagle on 2016/4/9.
 */
public class LoginFragment extends Fragment {

    private static final String serviceUrl="http://119.29.166.177:8080/login";
    private ConveyJson userJson;  //为了传递json而创立的类

    @Bind(R.id.input_login_email)
    EditText mInputEmail;
    @Bind(R.id.input_login_password)
    EditText mInputPassword;
    @Bind(R.id.btn_login)
    AppCompatButton mBtnLogin;
    @Bind(R.id.link_signup)
    TextView mLinkSignup;
    private static final String TAG = "LoginFragment";
    private static final int REQUEST_SIGNUP = 1;
    private ProgressDialog progressDialog;
    private JSONObject newJsonObject;

    private Handler handler = new Handler(){
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
//                    Log.i("status4","jsonInHandler:::"+ msg.obj.toString());
//                    onLoginSuccess((JSONObject) msg.obj);
                    JSONObject object = new JSONObject();
                    try {
                        if ( !newJsonObject.getString("icon").equals("")) {
                            object.put("icon",saveUserIcon(newJsonObject.getString("icon")).getAbsolutePath());
                        }else{
                            object.put("icon","");
                        }
                        if ( !newJsonObject.getString("background").equals("")) {
                            object.put("background",saveUserBackground(newJsonObject.getString("background")).getAbsolutePath());
                        }else{
                            object.put("background","");
                        }
                        object.put("userName", newJsonObject.getString("userName"));
                        object.put("userID", newJsonObject.getLong("userID"));
                        object.put("password", newJsonObject.getString("password"));
                        object.put("emailAddr", newJsonObject.getString("emailAddr"));
                        object.put("signature", newJsonObject.getString("signature"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    progressDialog.dismiss();
                    Log.i("Login::object:", object.toString());
                    onLoginSuccess(object);
                    break;
                case -1:
                    progressDialog.dismiss();
                    onLoginFailed();
                    break;
            }
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frag_login, container, false);
        ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onDestroy() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.btn_login, R.id.link_signup})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                login();
                break;
            case R.id.link_signup:
                startActivityForResult(new Intent(getActivity(),SignUpActivity.class),REQUEST_SIGNUP);
                break;
        }
    }

    private File saveUserIcon(String icon) {
        File iconFile = null;
        if (icon != null && !icon.equals("")) {
            iconFile= HandlePicture.createFileForIcon();
            Bitmap bitmap = HandlePicture.StringToBitmap(icon);
            try {
                FileOutputStream out = new FileOutputStream(iconFile);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                out.flush();
                out.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        return iconFile;
    }

    private File saveUserBackground(String background) {
        File backgroundFile=null;
        if (background != null && !background.equals("")) {
            backgroundFile=HandlePicture.createFileForBackground();
            Bitmap bitmap = HandlePicture.StringToBitmap(background);
            try {
                FileOutputStream out = new FileOutputStream(backgroundFile);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                out.flush();
                out.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return backgroundFile;
    }

    public void onLoginSuccess(JSONObject object) {
        mBtnLogin.setEnabled(true);
        userJson = new ConveyJson(object);
        Intent startMainAty = new Intent(getActivity(), MainActivity.class);
        startMainAty.putExtra("userInfo", userJson);
        startActivity(startMainAty);
        getActivity().finish();
    }

    public void onLoginFailed() {
        Toast.makeText(getActivity(), "登陆失败，请输入正确的账号或者密码", Toast.LENGTH_SHORT).show();
        mBtnLogin.setEnabled(true);
    }

    private boolean validate() {
        boolean valid = true;

        String email = mInputEmail.getText().toString();
        String password = mInputPassword.getText().toString();
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mInputEmail.setError("请输入有效邮箱");
            valid = false;
        } else {
            mInputEmail.setError(null);
        }

        if (password.isEmpty() || password.length() < 6 || password.length() > 18) {
            mInputPassword.setError("请输入6到18位长的密码");
            valid = false;
        } else {
            mInputPassword.setError(null);
        }

        return valid;
    }

    private void login() {
        Log.d(TAG, "登陆");

        if (!validate()) {
            onLoginFailed();
            return;
        }
        JSONObject object = new JSONObject();
        try {
            object.put("emailAddr", mInputEmail.getText().toString());
            object.put("password", mInputPassword.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        new LoginIn().execute(object);

//        mBtnLogin.setEnabled(false);
//        startActivity(new Intent(getActivity(),MainActivity.class));

        //此部分后边再补充
//        final ProgressDialog progressDialog = new ProgressDialog(getActivity(),
//                R.style.AppTheme_Dark_Dialog);
//        progressDialog.setIndeterminate(true);
//        progressDialog.setMessage("Authenticating...");
//        progressDialog.show();
//
//        String email = mInputEmail.getText().toString();
//        String password = mInputPassword.getText().toString();
//
//        // TODO: Implement your own authentication logic here.
//
//        new android.os.Handler().postDelayed(
//                new Runnable() {
//                    public void run() {
//                        // On complete call either onLoginSuccess or onLoginFailed
//                        onLoginSuccess();
//                        // onLoginFailed();
//                        progressDialog.dismiss();
//                    }
//                }, 3000);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode != Activity.RESULT_OK) {
            return;//或者需要其他提示信息？
        }

        if(requestCode == REQUEST_SIGNUP){
            Toast.makeText(getActivity(), "请登陆注册邮箱验证完成注册", Toast.LENGTH_SHORT).show();
        }
    }

    private class LoginIn extends AsyncTask<JSONObject,Void,String> {

        private JSONObject object;
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
//                            object=jsonObject;
                            newJsonObject=jsonObject;
                            Log.i("Modify_info:::","login:::"+ jsonObject.get("icon").toString());

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    Message message=new Message();
                    if (status.equals("1") && info.equals("OK")) {
                        message.what=1;
                        message.obj=object;
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
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("正在登陆,请稍候...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {

        }
    }
}
