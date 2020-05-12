package com.stx.xhb.DCAPlatform.activities;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.classic.common.MultipleStatusView;
import com.stx.xhb.DCAPlatform.R;
import com.stx.xhb.DCAPlatform.adapter.ListViewAdapter;
import com.stx.xhb.DCAPlatform.entity.ChapterListItem;
import com.stx.xhb.DCAPlatform.utils.API;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import butterknife.ButterKnife;
import sun.misc.BASE64Decoder;

public class
AdminEventActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    MultipleStatusView multiplestatusview;
    private ListView news_lv;
    private ListViewAdapter adapter;
    private List<ChapterListItem> data = new ArrayList<>();
    //Android自带下拉刷新控件
    private Toolbar toolbar;
    private String Type;
    private Dialog mDialog,dialog,sDialog;
    private String eventid,eventname;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        Intent intent=getIntent();
        Type=intent.getStringExtra("Type");
        ButterKnife.bind(this);
        initView();
        setListener();
        Request(Type);


    }
    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }
    //获取控件
    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.ae_toolbar);
        multiplestatusview=(MultipleStatusView)findViewById(R.id.ae_multiplestatusview) ;
        setSupportActionBar(toolbar);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorBackground)));
        //设置显示返回上一级的图标
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //设置标题
        switch (Type){
            case "1":
                getSupportActionBar().setTitle("已创建");
                break;
            case "2":
                getSupportActionBar().setTitle("报名中");
                break;
            case "3":
                getSupportActionBar().setTitle("上传阶段");
                break;
            case "4":
                getSupportActionBar().setTitle("审核中");
                break;
            case "5":
                getSupportActionBar().setTitle("已结束");
                break;

        }

        //设置标题栏字体颜色
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha));
        news_lv = (ListView) findViewById(R.id.ae_content_view);
        adapter = new ListViewAdapter(AdminEventActivity.this, data);

    }
    //设置事件监听
    private void setListener() {
        //toolbar的点击事件
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //listview的条目事件点击
        news_lv.setOnItemClickListener(this);
        news_lv.setOnItemLongClickListener(this);
        //点击重试
        multiplestatusview.setOnRetryClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Request(Type);
            }
        });
    }
    //下载网络数据
    public void Request(final String State) {
        multiplestatusview.showLoading();
        //请求地址
        String url = API.AdminEvent_URL;    //注①
        String tag = "AdminEvent";    //注②

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
                            multiplestatusview.showContent();
                            JSONObject jsonObject = (JSONObject) new JSONObject(response).get("params");
                            JSONArray events = jsonObject.getJSONArray("Events"); //注③
                            data.clear();
                            for (int i = 0; i < events.length(); i++) {
                                JSONObject jsonObject1 = events.getJSONObject(i);
                                ChapterListItem chapterListItems = new ChapterListItem(jsonObject1.getString("EventName"),jsonObject1.getString("StartDate"),stringImage(jsonObject1.getString("Image")),jsonObject1.getString("Id"));
                                data.add(chapterListItems);
                            }
                            news_lv.setAdapter(adapter);
                        } catch (JSONException e) {
                            multiplestatusview.showError();
                            Toast.makeText(getApplicationContext(), "无连接",
                                    Toast.LENGTH_SHORT).show();
                            Log.e("TAG", e.getMessage(), e);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //做自己的响应错误操作，如Toast提示（“请稍后重试”等）
                multiplestatusview.showError();
                Toast.makeText(getApplicationContext(), "稍后重试",
                        Toast.LENGTH_SHORT).show();
                Log.e("TAG", error.getMessage(), error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("State", State);  //注⑥
                return params;
            }
        };

        //设置Tag标签
        request.setTag(tag);

        //将请求添加到队列中
        requestQueue.add(request);
    }
    //删除赛事请求
    public void Request1(final String EventId) {
        multiplestatusview.showLoading();
        //请求地址
        String url = API.DeleteEvent_URL;    //注①
        String tag = "DeleteEvent";    //注②

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
                            multiplestatusview.showContent();
                            JSONObject jsonObject = (JSONObject) new JSONObject(response).get("params");
                            String result = jsonObject.getString("Result");  //注④
                            if (result.equals("success")) {  //注⑤
                                Request(Type);
                                Toast.makeText(getApplicationContext(), "操作成功",
                                        Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(getApplicationContext(), "操作失败",
                                        Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            multiplestatusview.showError();
                            Toast.makeText(getApplicationContext(), "无连接",
                                    Toast.LENGTH_SHORT).show();
                            Log.e("TAG", e.getMessage(), e);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //做自己的响应错误操作，如Toast提示（“请稍后重试”等）
                multiplestatusview.showError();
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
    //更改赛事状态请求
    public void Request2(final String EventId,final String State) {
        multiplestatusview.showLoading();
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
                            multiplestatusview.showContent();
                            JSONObject jsonObject = (JSONObject) new JSONObject(response).get("params");
                            String result = jsonObject.getString("Result");  //注④
                            if (result.equals("success")) {  //注⑤
                                Request(Type);
                                Toast.makeText(getApplicationContext(), "操作成功",
                                        Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(getApplicationContext(), "操作失败",
                                        Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            multiplestatusview.showError();
                            Toast.makeText(getApplicationContext(), "无连接",
                                    Toast.LENGTH_SHORT).show();
                            Log.e("TAG", e.getMessage(), e);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //做自己的响应错误操作，如Toast提示（“请稍后重试”等）
                multiplestatusview.showError();
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
                if(Recipient.equals("2")){
                    params.put("Content", "你参加的"+EventName+"已经可以上传材料了，请尽快前往上传");
                }else {
                    params.put("Content", "你已经被选为"+EventName+"的审核人员，请尽快前往审核");
                }
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
    //设置监听事件
    /**
     * listview的点击事件
     *
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //点击条目跳转到详情界面
        String eventid = data.get(position ).getEventid();//获取到赛事id
        Intent intent = new Intent(AdminEventActivity.this, EventDetailActivity.class);
        intent.putExtra("EventId",eventid);
        startActivity(intent);

    }
    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        eventid = data.get(position).getEventid();//获取赛事Id
        eventname=data.get(position).getEventname();
        initShareDialog(eventid);
        return true;
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
    /**
     * dialog 初始化
     */
    //长按弹出选项框
    private void initShareDialog(final String id) {
        mDialog = new Dialog(this, R.style.dialogStyle);
        mDialog.setCanceledOnTouchOutside(true);
        mDialog.setCancelable(true);            //点击框外，框退出
        Window window = mDialog.getWindow();
        window.setGravity(Gravity.BOTTOM);      //位于底部
        window.setWindowAnimations(R.style.dialog_share);    //弹出动画
        View inflate = View.inflate(this, R.layout.event_dialog, null);
        inflate.findViewById(R.id.dialog_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDialog != null && mDialog.isShowing()){
                    mDialog.dismiss();      //消失，退出
                }
            }
        });
        inflate.findViewById(R.id.delete_event).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDialog != null && mDialog.isShowing()){
                    ConfirmDialog(id,1);
                    mDialog.dismiss();      //消失，退出
                }
            }
        });
        inflate.findViewById(R.id.change_event_state).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDialog != null && mDialog.isShowing()){
                    if(Type.equals("3")){
                        SelectDialog(id);//进入审核阶段需选择评委
                        mDialog.dismiss();
                    }
                    else if (Type.equals("5")){
                        Toast.makeText(getApplicationContext(), "赛事已结束无下一阶段",
                                Toast.LENGTH_SHORT).show();
                        mDialog.dismiss();
                    }
                        else{
                        ConfirmDialog(id,2);
                        mDialog.dismiss();      //消失，退出
                    }

                }
            }
        });
        window.setContentView(inflate);
        //横向充满
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        mDialog.show();
    }
    //确认Dialog
    private void ConfirmDialog(final String id, final int way){
        dialog = new Dialog(this, R.style.dialogStyle);
        dialog.setCanceledOnTouchOutside(true);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER);      //位于中间
        window.setWindowAnimations(R.style.dialog_share);    //弹出动画
        View inflate = View.inflate(this, R.layout.confirm_dialog, null);
        inflate.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog != null && dialog.isShowing()){
                    dialog.dismiss();      //消失，退出
                }
            }
        });
        inflate.findViewById(R.id.tv_sure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog != null && dialog.isShowing()){
                    if(way==1){
                        Request1(id);
                    }else {
                        int index=Integer.parseInt(Type);
                        index++;
                        Request2(id,index+"");
                        if (Type .equals("2")) {
                            Request3("2",eventid,eventname);//给参赛者发送赛事可以上传材料的通知
                        }
                    }
                    dialog.dismiss();      //消失，退出
                }
            }
        });
        window.setContentView(inflate);
        //横向充满
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }
    private void SelectDialog(final String id){
        sDialog = new Dialog(this, R.style.dialogStyle);
        sDialog.setCanceledOnTouchOutside(true);
        Window window = sDialog.getWindow();
        window.setGravity(Gravity.CENTER);      //位于中间
        window.setWindowAnimations(R.style.dialog_share);    //弹出动画
        View inflate = View.inflate(this, R.layout.select_audie_dialog, null);
        inflate.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sDialog != null && sDialog.isShowing()){
                    sDialog.dismiss();      //消失，退出
                }
            }
        });
        inflate.findViewById(R.id.tv_sure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sDialog != null && sDialog.isShowing()){
                    Intent intent = new Intent(AdminEventActivity.this,SelectAuditActivity.class);
                    intent.putExtra("EventId",id);
                    intent.putExtra("Type",Type);
                    intent.putExtra("EventName",eventname);
                    startActivity(intent);
                    sDialog.dismiss();      //消失，退出
                    finish();
                }
            }
        });
        window.setContentView(inflate);
        //横向充满
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        sDialog.show();
    }
    private String getTime(){
        SimpleDateFormat formatter = new SimpleDateFormat("YYYY-MM-dd HH:mm"); //设置时间格式
        formatter.setTimeZone(TimeZone.getTimeZone("GMT+08")); //设置时区
        Date curDate = new Date(System.currentTimeMillis()); //获取当前时间
        String createDate = formatter.format(curDate);   //格式转换
        return createDate;
    }
}

