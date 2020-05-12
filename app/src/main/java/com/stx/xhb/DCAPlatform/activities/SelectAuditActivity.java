package com.stx.xhb.DCAPlatform.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.stx.xhb.DCAPlatform.R;
import com.stx.xhb.DCAPlatform.utils.API;
import com.stx.xhb.DCAPlatform.utils.SystemBarTintManager;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import butterknife.ButterKnife;
import butterknife.OnClick;
import sun.misc.BASE64Decoder;

public class SelectAuditActivity extends AppCompatActivity {

    Toolbar settingToolbar;
    private Toast mToast;
    private Button button;
    private LinearLayout linearLayout;
    private String EventId,Type,EventName;
    private int index=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_audit);
        Intent intent=getIntent();
        EventId=intent.getStringExtra("EventId");
        Type=intent.getStringExtra("Type");
        EventName=intent.getStringExtra("EventName");
        ButterKnife.bind(this);
        initWindow();
        initView();
        setListener();
        Request();//拉取评委名单
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

    //初始化控件
    private void initView() {
        settingToolbar = (Toolbar) findViewById(R.id.setting_toolbar);
        button = (Button) findViewById(R.id.create_button);
        setSupportActionBar(settingToolbar);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorBackground)));
        //设置显示返回上一级的图标
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //设置标题
        getSupportActionBar().setTitle("选择审核");
        //设置标题栏字体颜色
        settingToolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));
        settingToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha));
        linearLayout=(LinearLayout)findViewById(R.id.linear);
    }

    //点击事件
    @OnClick({R.id.create_button})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.create_button:
                button.setEnabled(false);
                Request1(printData());
                break;
        }
    }
    //设置事件监听
    private void setListener() {
        //toolbar的点击事件
        settingToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onResume(this);       //统计时长

    }
    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPause(this);
    }
    @Override
    protected void onStop() {
        super.onStop();
    }

    private void showTip(final String str) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mToast.setText(str);
                mToast.show();
            }
        });
    }
    //动态添加控件
    private void addContent(String auditName, final Bitmap image,String auditId) {
        View hotelEvaluateView = View.inflate(this, R.layout.item_audit, null);
        final TextView textView=hotelEvaluateView.findViewById(R.id.audit_name);
        final TextView textView1=hotelEvaluateView.findViewById(R.id.audit_Id);
        final ImageView imageView=hotelEvaluateView.findViewById(R.id.image);
        final ImageView imageView1=hotelEvaluateView.findViewById(R.id.select);
        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(imageView1.getTag().equals("yes")){
                    imageView1.setImageResource(R.drawable.select_1);
                    imageView1.setTag("no");
                    index--;
                }else{
                    imageView1.setImageResource(R.drawable.select_2);
                    imageView1.setTag("yes");
                    index++;
                }

            }
        });
        textView.setText(auditName);
        textView1.setText(auditId);
        imageView.setImageBitmap(image);
        linearLayout.addView(hotelEvaluateView);

    }
    //获取动态添加的控件中的内容
    private String printData(){
        String audit="";
        for (int i = 0; i < linearLayout.getChildCount(); i++) {
            View childAt = linearLayout.getChildAt(i);
            ImageView imageView = (ImageView) childAt.findViewById(R.id.select);
            TextView  textView  =(TextView)childAt.findViewById(R.id.audit_Id);
            if(imageView.getTag().equals("yes")){
                audit=audit+textView.getText().toString()+",";
            }
        }
        return  audit;
    }
    //下载网络数据
    public void Request() {
        //请求地址
        String url = API.Judges_URL;  //注①
        String tag = "Judges";    //注②

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
                            JSONObject jsonObject = (JSONObject) new JSONObject(response).get("params");
                            JSONArray users = jsonObject.getJSONArray("Users"); //注③
                            for (int i = 0; i < users.length(); i++) {
                                JSONObject jsonObject1 = users.getJSONObject(i);
                                addContent(jsonObject1.getString("UserName"),stringImage(jsonObject1.getString("Heortrait")),jsonObject1.getString("UserId"));
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
                params.put("userId", "ok");  //注⑥
                return params;
            }
        };

        //设置Tag标签
        request.setTag(tag);

        //将请求添加到队列中
        requestQueue.add(request);
    }
    //审核人员上传
    public void Request1(final String audit) {
        //请求地址
        String url =API.SetJudges_URL;  //注①
        String tag = "SetJudgesServlet";    //注②

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
                                Request2(EventId,Integer.parseInt(Type)+1+"");
                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(), "设置失败",
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
                params.put("EventId", EventId);  //注⑥
                params.put("Judges", audit);
                params.put("Index", index+"");
                return params;
            }
        };
        //设置Tag标签
        request.setTag(tag);

        //将请求添加到队列中
        requestQueue.add(request);
    }
    //更改赛事状态请求
    public void Request2(final String EventId,final String State) {
        //请求地址
        String url = API.ChangeEventState_URL;    //注①
        String tag = "ChangeEventState";    //注②

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
                            JSONObject jsonObject = (JSONObject) new JSONObject(response).get("params");
                            String result = jsonObject.getString("Result");  //注④
                            if (result.equals("success")) {  //注⑤
                                Request3("1",EventId,EventName);
                                Toast.makeText(getApplicationContext(), "操作成功",
                                        Toast.LENGTH_SHORT).show();


                            } else {
                                Toast.makeText(getApplicationContext(), "操作失败",
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
                params.put("EventId", EventId);  //注⑥
                params.put("State", State);
                return params;
            }
        };

        //设置Tag标签
        request.setTag(tag);

        //将请求添加到队列中
        requestQueue.add(request);
    }
    //更改状态发送提示消息
    public void Request3(final String Recipient,final String Eventid,final String EventName) {
        //请求地址
        String url = API.NewMessage_URL;    //注①
        String tag = "NewMessage";    //注②

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

                            } else {

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
                params.put("Id", Eventid);
                params.put("EventName", EventName);
                params.put("Content", "你已经被选为"+EventName+"的审核人员，请尽快前往审核");
                params.put("Recipient", Recipient);
                params.put("Time",getTime());
                return params;
            }
        };

        //设置Tag标签
        request.setTag(tag);

        //将请求添加到队列中
        requestQueue.add(request);
    }
    //将Bitmap转化为图片
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
    private String getTime(){
        SimpleDateFormat formatter = new SimpleDateFormat("YYYY-MM-dd HH:mm"); //设置时间格式
        formatter.setTimeZone(TimeZone.getTimeZone("GMT+08")); //设置时区
        Date curDate = new Date(System.currentTimeMillis()); //获取当前时间
        String createDate = formatter.format(curDate);   //格式转换
        return createDate;
    }
}
