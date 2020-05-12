package com.stx.xhb.DCAPlatform.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.stx.xhb.DCAPlatform.MainActivity;
import com.stx.xhb.DCAPlatform.R;
import com.stx.xhb.DCAPlatform.utils.API;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {


    private TextView textView;
    private Button button1,button2;
    private EditText id,pwd;
    private TextView re;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();


    }


    //获取控件
    private void initView() {
        button1=(Button)findViewById(R.id.btn_login1);
        button2=(Button)findViewById(R.id.btn_login2);
        id=(EditText)findViewById(R.id.et_acount);
        pwd=(EditText)findViewById(R.id.et_pwd);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RegisterRequest(id.getText().toString(),pwd.getText().toString(),"1");
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RegisterRequest(id.getText().toString(),pwd.getText().toString(),"2");
            }
        });
    }
    //网络请求
    public void RegisterRequest(final String accountNumber, final String password,final String Type) {
        //请求地址
        String url = API.Registered_URL;  //注①
        String tag = "Register";    //注②

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
                                Intent intent=new Intent();
                                intent.setClass(RegisterActivity.this, MainActivity.class);
                                Toast.makeText(getApplicationContext(), "注册成功",
                                        Toast.LENGTH_SHORT).show();
                                finish();

                            } else {
                                Toast.makeText(getApplicationContext(), "注册失败",
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
                params.put("Image", Bitmap2StrByBase64(BitmapFactory.decodeResource(getResources(), R.drawable.logo)));
                params.put("Type", Type);
                return params;
            }
        };
        //设置Tag标签
        request.setTag(tag);

        //将请求添加到队列中
        requestQueue.add(request);
    }
    //压缩图片
    public String Bitmap2StrByBase64(Bitmap bit){
        ByteArrayOutputStream bos=new ByteArrayOutputStream();
        bit.compress(Bitmap.CompressFormat.JPEG, 40, bos);//参数100表示不压缩
        byte[] bytes=bos.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }


}

