package com.stx.xhb.DCAPlatform.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.zhouwei.library.CustomPopWindow;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.stx.xhb.DCAPlatform.MainActivity;
import com.stx.xhb.DCAPlatform.R;
import com.stx.xhb.DCAPlatform.adapter.LoginAcountAdapter;
import com.stx.xhb.DCAPlatform.base.BaseApplication;
import com.stx.xhb.DCAPlatform.entity.UserData;
import com.stx.xhb.DCAPlatform.utils.API;
import com.stx.xhb.DCAPlatform.utils.SystemBarTintManager;
import com.xw.repo.XEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import sun.misc.BASE64Decoder;

public class LoginActivity extends AppCompatActivity {



    @BindView(R.id.et_acount)
    XEditText et_acount;
    @BindView(R.id.et_pwd)
    XEditText et_pwd;
    @BindView(R.id.btn_login)
    Button btn_login;
    @BindView(R.id.iv_btn)
    ImageView iv_btn;

    List<UserData> arrayList;
    private FileOutputStream fileOutputStream;
    private FileInputStream fileInputStream;
    private BufferedWriter bufferedWriter;
    private Gson gson;
    private BufferedReader bufferedReader;
    private CustomPopWindow mCustomPopWindow;
    private LoginAcountAdapter loginAcountAdapter;
    private String image;
    View pop_loginacount;
    private BaseApplication baseApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //绑定activity
        ButterKnife.bind(this);
        gson = new Gson();
        arrayList = new ArrayList<>();
        baseApplication=(BaseApplication)getApplication();
        isSaveacountAndPwd();
        initWindow();

    }

    /**
     * 是否有以前保存过的账号和密码
     */
    private void isSaveacountAndPwd() {
        String data = readFile();
        if (!TextUtils.isEmpty(data) && !data.equals("[]")) {
            arrayList = gson.fromJson(data, new TypeToken<List<UserData>>() {
            }.getType());
            et_acount.setText(arrayList.get(0).getAcount());
            et_pwd.setText(arrayList.get(0).getPasswd());
            iv_btn.setImageBitmap(stringImage(arrayList.get(0).getImage()));
        }
    }

    /**
     * 读取存储的文件内容
     *
     * @return 账号和密码的json数据
     */
    private String readFile() {
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        try {
            //以防止没有创建时读取错误
            fileOutputStream = openFileOutput("user", MODE_APPEND);
            fileInputStream = openFileInput("user");
            bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            fileOutputStream.close();
            fileInputStream.close();
            bufferedReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    @OnClick({R.id.btn_login, R.id.ivbtn_drop,R.id.tv_register})
    public void onClick(View view) {
        switch (view.getId()) {
            //点击登录按钮
            case R.id.btn_login:
                if (TextUtils.isEmpty(et_acount.getText().toString().trim())) {
                    Toast.makeText(this, "请输入账号", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(et_pwd.getText().toString().trim())) {
                    Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
                } else {
                    LoginRequest(et_acount.getTrimmedString(),et_pwd.getTrimmedString());
                }
                break;

            case R.id.ivbtn_drop:
                //创建并显示popWindow
                if (pop_loginacount==null) {
                    pop_loginacount = LayoutInflater.from(LoginActivity.this).inflate(R.layout.pop_loginacount, null);
                }
                //处理popWindow 显示内容
                handleListView(pop_loginacount);
                mCustomPopWindow = new CustomPopWindow.PopupWindowBuilder(this)
                        .setView(pop_loginacount)
                        .size(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)//显示大小
                        .enableBackgroundDark(false) //弹出popWindow时，背景是否变暗
                        .enableOutsideTouchableDissmiss(true)
                        .create();

                if (Build.VERSION.SDK_INT != 24) {
                    //只有24这个版本有问题，好像是源码的问题
                    mCustomPopWindow.showAsDropDown(et_acount);
                } else {
                    //7.0 showAsDropDown没卵子用 得这么写
                    int[] location = new int[2];
                    et_acount.getLocationOnScreen(location);
                    int x = location[0];
                    int y = location[1];
                    mCustomPopWindow.showAtLocation(et_acount, Gravity.NO_GRAVITY, 0, y +et_acount.getHeight()); }
                break;
            case R.id.tv_register:
                Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
                break;
        }
    }
    //初始化窗体布局
    private void initWindow() {
        SystemBarTintManager tintManager;
        //由于沉浸式状态栏需要在Android4.4.4以上才能使用
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintColor(getResources().getColor(R.color.colorBackground));
            tintManager.setStatusBarTintEnabled(true);
        }
    }
    private void handleListView(View pop_loginacount) {
        ListView listView = (ListView) pop_loginacount.findViewById(R.id.lv_acount);
        loginAcountAdapter = new LoginAcountAdapter(LoginActivity.this, arrayList);
        listView.setAdapter(loginAcountAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                et_acount.setText(arrayList.get(position).getAcount());
                et_pwd.setText(arrayList.get(position).getPasswd());
                iv_btn.setImageBitmap(stringImage(arrayList.get(position).getImage()));
                mCustomPopWindow.dissmiss();
            }
        });
    }

    /**
     * 点击登录按钮,保存账号和密码
     */
    private void saveacountAndPwd() {
        if (save()==-1) {
            try {
                arrayList.add(new UserData(et_acount.getText().toString().trim(), et_pwd.getText().toString().trim(),image.trim()));
                String data = gson.toJson(arrayList);
                //MODE_PRIVATE 在该模式下，写入的内容会覆盖原文件的内容
                fileOutputStream = openFileOutput("user", MODE_PRIVATE);
                bufferedWriter = new BufferedWriter(new OutputStreamWriter(fileOutputStream));
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            try {
                int i=save();
                arrayList.get(i).setAcount(et_acount.getText().toString().trim());
                arrayList.get(i).setPasswd(et_pwd.getText().toString().trim());
                arrayList.get(i).setImage(image);
                String data = gson.toJson(arrayList);
                //MODE_PRIVATE 在该模式下，写入的内容会覆盖原文件的内容
                fileOutputStream = openFileOutput("user", MODE_PRIVATE);
                bufferedWriter = new BufferedWriter(new OutputStreamWriter(fileOutputStream));
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private int save(){
        for (int i=0;i<arrayList.size();i++){
            if(arrayList.get(i).getAcount().equals(et_acount.getText().toString())){
                return i;
            }
        }
        return  -1;
    }
    //网络请求
    public void LoginRequest(final String accountNumber, final String password) {
        //请求地址
        String url =API.Login_URL;  //注①
        String tag = "Login";    //注②

        //取得请求队列
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        //防止重复请求，所以先取消tag标识的请求队列
        requestQueue.cancelAll(tag);

        //创建StringRequest，定义字符串请求的请求方式为POST(省略第一个参数会默认为GET方式)
        final StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = (JSONObject) new JSONObject(response).get("params");  //注③
                            String result = jsonObject.getString("Result");  //注④
                            if (result.equals("success")) {  //注⑤
                                String type=jsonObject.getString("Type");
                                Intent intent1 = new Intent(LoginActivity.this, MainActivity.class);
                                baseApplication.setType(type);//用户类型
                                baseApplication.setUserId(et_acount.getTrimmedString());//用户Id
                                baseApplication.setImage(stringImage(jsonObject.getString("Image")));
                                baseApplication.setUserName(jsonObject.getString("userName"));
                                image=jsonObject.getString("Image");
                                saveacountAndPwd();
                                startActivity(intent1);
                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(), "登录失败",
                                        Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(getApplicationContext(), "无连接",
                                    Toast.LENGTH_SHORT).show();
                            Log.e("TAG", e.getMessage(), e);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //做自己的响应错误操作，如Toast提示（“请稍后重试”等）
                Toast.makeText(getApplicationContext(), "稍后重试",
                        Toast.LENGTH_SHORT).show();
                Log.e("TAG", error.getMessage(), error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("AccountNumber", accountNumber);  //注⑥
                params.put("Password", password);
                return params;
            }
        };
        //设置Tag标签
        request.setTag(tag);

        //将请求添加到队列中
        requestQueue.add(request);
    }

    //将String转化为Bitmap
    public Bitmap stringImage(String imgStr) {
        // 对字节数组字符串进行Base64解码并生成图片
        if (imgStr == null)
            return null;
        try {
            // Base64解码
            byte[] b = new BASE64Decoder().decodeBuffer(imgStr);
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {
                    // 调整异常数据
                    b[i] += 256;
                }
            }
            return BitmapFactory.decodeByteArray(b, 0, b.length, null);
        } catch (Exception e) {
            return null;
        }
    }


}
