package com.stx.xhb.DCAPlatform.activities;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
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
import com.stx.xhb.DCAPlatform.filepick.FileEntity;
import com.stx.xhb.DCAPlatform.filepick.FilePickerActivity;
import com.stx.xhb.DCAPlatform.filepick.PickerManager;
import com.stx.xhb.DCAPlatform.utils.API;
import com.stx.xhb.DCAPlatform.utils.HttpAssist;
import com.stx.xhb.DCAPlatform.utils.SystemBarTintManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class SubmitActivity extends AppCompatActivity {
    private String EventId,UserId;
    private EditText VideoUrl;
    private TextView mTvName;
    String result = "";
    private ImageView mIvType;
    private BaseApplication baseApplication;
    Toolbar settingToolbar;
    private static int REQ_CODE = 0X01;
    private FileEntity fileEntity;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent=getIntent();
        EventId=intent.getStringExtra("EventId");
        setContentView(R.layout.activity_submit);
        initWindow();
        ButterKnife.bind(this);
        initView();
        setListener();


    }
    //初始化控件
    private void initView() {
        settingToolbar = (Toolbar) findViewById(R.id.setting_toolbar);
        setSupportActionBar(settingToolbar);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorBackground)));
        //设置显示返回上一级的图标
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //设置标题
        getSupportActionBar().setTitle("上传作品");
        //设置标题栏字体颜色
        settingToolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));
        settingToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha));
        VideoUrl=(EditText)findViewById(R.id.video_url);
        mTvName=(TextView) findViewById(R.id.tv_name);
        mIvType=(ImageView) findViewById(R.id.iv_type);
        baseApplication=(BaseApplication)getApplication();
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
    //点击事件
    @OnClick({R.id.choose_word,R.id.roundButton,R.id.check_word})
    public void onClick(View view)  {
        switch (view.getId()) {
            case R.id.choose_word:
                Intent intent=new Intent(SubmitActivity.this, FilePickerActivity.class);
                startActivityForResult(intent,REQ_CODE);//获取返回的文件
                break;
            case R.id.check_word:
                if(mTvName.getText().toString().equals("")){

                }else {
                    Intent intent1 = getWordFileIntent(new File(fileEntity.getPath()).getPath());
                    try {
                        startActivity(intent1);
                    }catch (Exception e) {
                        Toast.makeText(this,"找不到可以打开该文件的程序", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.roundButton:
                if (TextUtils.isEmpty(VideoUrl.getText().toString().trim())){
                    Toast.makeText(this, "请输入演示视频网址", Toast.LENGTH_SHORT).show();
                }else {
                    new Thread(new Runnable(){
                        @Override
                        public void run() {
                            result =HttpAssist.uploadFile(new File(fileEntity.getPath()),EventId+"-"+UserId);
                            if (result.equals("0")){
                                Toast.makeText(getApplicationContext(), "文档上传失败",
                                        Toast.LENGTH_SHORT).show();
                            }else {
                                Request();
                            }
                        }
                    }).start();
                }
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
    //返回此界面获得文件
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQ_CODE&&resultCode!=0&&PickerManager.getInstance().files.toArray().length!=0){
            fileEntity= PickerManager.getInstance().files.get(0);
            if (fileEntity.getFileType() != null) {
                String title = fileEntity.getFileType().getTitle();
                mTvName.setText(fileEntity.getName());
                if (title.equals("IMG")) {
                } else {
                   mIvType.setImageResource(fileEntity.getFileType().getIconStyle());
                }
            } else {
                    mIvType.setImageResource(R.mipmap.file_picker_def);
            }
        }
    }
    //上传用户提交信息至数据库
    public void Request() {
        //请求地址
        String url = API.UserUpload_URL;  //注①
        String tag = "UserUpload";    //注②

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
                                Toast.makeText(getApplicationContext(), "提交成功",
                                        Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(), "提交失败",
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
                params.put("UserId", UserId);
                params.put("Video", VideoUrl.getText().toString());
                params.put("Word", EventId+"-"+UserId+new File(fileEntity.getPath()).getPath().substring(new File(fileEntity.getPath()).getPath()
                        .lastIndexOf(".")));
                return params;
            }
        };
        //设置Tag标签
        request.setTag(tag);

        //将请求添加到队列中
        requestQueue.add(request);
    }
    //android获取一个用于打开Word文件的intent
    public static Intent getWordFileIntent(String param ) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(param ));
        intent.setDataAndType(uri, "application/msword");
        return intent;
    }

}
