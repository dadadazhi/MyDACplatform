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
import com.stx.xhb.DCAPlatform.base.BaseApplication;
import com.stx.xhb.DCAPlatform.utils.API;
import com.stx.xhb.DCAPlatform.utils.SystemBarTintManager;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import sun.misc.BASE64Decoder;

/**
 * 赛事详情界面
 */
public class EventDetailActivity extends AppCompatActivity implements View.OnClickListener {



    private Toolbar toolbar;
    private Button button;//报名按钮
    private String  EventId,Type,UserId;
    private ImageView imageView;
    private TextView eventName,eventType,sponsor,startDate,organizer,website,introduction,state;
    private BaseApplication baseApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent=getIntent();
        EventId=intent.getStringExtra("EventId");
        setContentView(R.layout.activity_game_detail);
        initWindow();
        initView();
        Request(EventId);
        Request1();
        //设置监听
        setListener();

    }

    //初始化控件，获取控件
    private void initView() {
        //获取控件
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        button=(Button)findViewById(R.id.roundButton);
        //2.替代
        setSupportActionBar(toolbar);
        //加载背景颜色
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorBackground)));
        //设置显示返回上一级的图标
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //设置标题
        getSupportActionBar().setTitle("赛事详情");
        //设置标题栏字体颜色
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha));
        imageView=(ImageView)findViewById(R.id.event_image);
        eventName=(TextView)findViewById(R.id.event_name);
        eventType=(TextView)findViewById(R.id.event_type);
        sponsor=(TextView)findViewById(R.id.sponsor);
        startDate=(TextView)findViewById(R.id.start_date);
        organizer=(TextView)findViewById(R.id.organizer);
        website=(TextView)findViewById(R.id.event_url);
        introduction=(TextView)findViewById(R.id.introduction);
        state=(TextView)findViewById(R.id.state);
        baseApplication=(BaseApplication)getApplication();
        Type=baseApplication.getType();
        if(Type.equals("3")){
            button.setText("修改信息");
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(EventDetailActivity.this, ChangeEventActivity.class);
                    intent.putExtra("EventId",EventId);
                    startActivity(intent);
                }
            });
        }else if (Type.equals("2")){
            button.setVisibility(View.INVISIBLE);
        }else{
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(EventDetailActivity.this, SignUpActivity.class);
                    intent.putExtra("EventId",EventId);
                    startActivity(intent);
                }
            });
        }
        UserId=baseApplication.getUserId();
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

    //初始化数据
    public void Request(final String EventId) {
        //请求地址
        String url = API.EventDetail_URL;    //注①
        String tag = "EventDetail ";    //注②

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
                            JSONObject EventDetail = jsonObject.getJSONObject("EventDetail"); //注③
                            imageView.setImageBitmap(stringImage(EventDetail.getString("Image")));
                            eventName.setText(EventDetail.getString("EventName"));
                            eventType.setText(EventDetail.getString("EventType"));
                            sponsor.setText(EventDetail.getString("Sponsor"));
                            startDate.setText(EventDetail.getString("StartDate"));
                            organizer.setText(EventDetail.getString("Organizer"));
                            website.setText(EventDetail.getString("Website"));
                            introduction.setText(EventDetail.getString("Introduction"));
                            String State=EventDetail.getString("State");
                            if(State.equals("1")){
                                state.setText("尚未开始");
                                button.setText("尚未开始");
                                button.setEnabled(false);
                            }else if(State.equals("2")){
                                state.setText("报名中");
                            }else {
                                state.setText("报名已结束");
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
                return params;
            }
        };

        //设置Tag标签
        request.setTag(tag);

        //将请求添加到队列中
        requestQueue.add(request);
    }
    public void Request1() {
        //请求地址
        String url = API.QueryEvent_URL;    //注①
        String tag = "QueryEvent";    //注②

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
                            JSONObject events = jsonObject.getJSONObject("Event"); //注③
                            if(events.getString("EventState").equals("2")&&events.getString("TeamState").equals("1")){
                                button.setText("已报名");
                                button.setEnabled(false);
                            }else if(events.getString("EventState").equals("3")&&events.getString("TeamState").equals("1")){
                                button.setText("上传材料");
                                button.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent=new Intent(EventDetailActivity.this, SubmitActivity.class);
                                        intent.putExtra("EventId",EventId);
                                        startActivity(intent);
                                    }
                                });
                            }else if(events.getString("EventState").equals("3")&&events.getString("TeamState").equals("2")){
                                button.setText("重新上传");
                                button.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent=new Intent(EventDetailActivity.this, SubmitActivity.class);
                                        intent.putExtra("EventId",EventId);
                                        startActivity(intent);
                                    }
                                });
                            }else if(events.getString("EventState").equals("5")&&events.getString("TeamState").equals("2")){
                                button.setText("查看排名");
                                button.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent=new Intent(EventDetailActivity.this, RankingActivity.class);
                                        intent.putExtra("EventId",EventId);
                                        startActivity(intent);
                                    }
                                });
                            }else {
                                button.setVisibility(View.INVISIBLE);
                            }

                        } catch (JSONException e) {

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
                params.put("UserId", UserId);
                params.put("EventId", EventId);  //注⑥
                return params;
            }
        };

        //设置Tag标签
        request.setTag(tag);

        //将请求添加到队列中
        requestQueue.add(request);
    }
    //设置监听
    private void setListener() {
        //设置toolbar返回上一级的事件监听
        toolbar.setNavigationOnClickListener(this);
    }

    //toolbar的事件监听
    @Override
    public void onClick(View v) {
        //返回上一级
        finish();
    }
    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onResume(this);       //统计时长
    }

    @Override
    protected void onResume() {
        super.onResume();
        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    Thread.sleep(500);
                    Request(EventId);
                    Request1();

                } catch (Exception e) {
                    e.printStackTrace(System.err);
                }

            }
        }).start();

        MobclickAgent.onPause(this);
    }
    //String 转为Bitmap
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
