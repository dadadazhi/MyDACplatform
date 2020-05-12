package com.stx.xhb.DCAPlatform.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 创建比赛界面
 */
public class CreateEventActivity extends AppCompatActivity{

    Toolbar settingToolbar;
    private Toast mToast;
    private LinearLayout linearLayout;
    private int index =1;
    private Bitmap bitmap;
    private ImageView imageView;
    private EditText eventName,eventType,sponsor,startDate,organizer,website,introduction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        ButterKnife.bind(this);
        initWindow();
        initView();
        setListener();
        addContent(null);//添加第一个

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
        setSupportActionBar(settingToolbar);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorBackground)));
        //设置显示返回上一级的图标
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //设置标题
        getSupportActionBar().setTitle("建立比赛");
        //设置标题栏字体颜色
        settingToolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));
        settingToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha));
        imageView=(ImageView)findViewById(R.id.image_event);
        eventName=(EditText)findViewById(R.id.tv_event_name);
        eventType=(EditText)findViewById(R.id.tv_event_type);
        sponsor=(EditText)findViewById(R.id.sponsor);
        startDate=(EditText)findViewById(R.id.start_date);
        organizer=(EditText)findViewById(R.id.organizer);
        website=(EditText)findViewById(R.id.website);
        introduction=(EditText)findViewById(R.id.introduction);
        mToast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
        linearLayout=(LinearLayout)findViewById(R.id.linear);
        bitmap=((BitmapDrawable)getResources().getDrawable(R.drawable.gamedefault)).getBitmap();
    }

    //点击事件
    @OnClick({R.id.add_item,R.id.image_event,R.id.create_button})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_item:
                addContent(view);
                break;
            case R.id.image_event:
                selectPicture();
                break;
            case R.id.create_button:
                if (TextUtils.isEmpty(eventName.getText().toString().trim())) {
                    Toast.makeText(this, "请输入赛事名称", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(eventType.getText().toString().trim())) {
                    Toast.makeText(this, "请输入赛事类型", Toast.LENGTH_SHORT).show();
                }else if (TextUtils.isEmpty(sponsor.getText().toString().trim())) {
                    Toast.makeText(this, "请输入主办单位", Toast.LENGTH_SHORT).show();
                }else if (TextUtils.isEmpty(startDate.getText().toString().trim())) {
                    Toast.makeText(this, "请输入开始日期", Toast.LENGTH_SHORT).show();
                }else if (TextUtils.isEmpty(website.getText().toString().trim())) {
                    Toast.makeText(this, "请输入官方网站", Toast.LENGTH_SHORT).show();
                }else if (TextUtils.isEmpty(organizer.getText().toString().trim())) {
                    Toast.makeText(this, "请输入承办单位", Toast.LENGTH_SHORT).show();
                }else if (TextUtils.isEmpty(introduction.getText().toString().trim())) {
                    Toast.makeText(this, "请输入赛事简介", Toast.LENGTH_SHORT).show();
                }else if (TextUtils.isEmpty(printData().trim())){
                    Toast.makeText(this, "请输入审核标准", Toast.LENGTH_SHORT).show();
                }else {
                    if(printData().equals("null")){
                        Toast.makeText(getApplicationContext(), "总分值应为100",
                                Toast.LENGTH_SHORT).show();
                    }else {
                        CreateEventRequest(printData());
                        finish();
                    }

                }


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
    private void addContent(View v) {
        if (linearLayout.getChildCount() == 0) {//如果一个都没有，就添加一个
            View hotelEvaluateView = View.inflate(this, R.layout.item_standard, null);
            final TextView textView=hotelEvaluateView.findViewById(R.id.te_standard);
            textView.setText("标准"+index+++":   ");
            linearLayout.addView(hotelEvaluateView,index-2);
            //sortHotelViewItem();
        } else {//如果有一个以上的Item,点击为添加的Item则添加
            View hotelEvaluateView = View.inflate(this, R.layout.item_standard, null);
            final TextView textView=hotelEvaluateView.findViewById(R.id.te_standard);
            textView.setText("标准"+index+++":   ");
            linearLayout.addView(hotelEvaluateView,index-2);
        }
    }
    //获取动态添加的控件中的内容
    private String printData() {
        View childAt1 = linearLayout.getChildAt(0);
        int x=0;
        EditText editText1 = (EditText) childAt1.findViewById(R.id.tv_standard);
        EditText score1 = (EditText) childAt1.findViewById(R.id.tv_score);
        String standard=editText1.getText().toString()+","+score1.getText().toString();
        if(score1.getText().toString().equals("")){
            x=x+0;
        }else {
            x=Integer.parseInt(score1.getText().toString())+x;
        }
        for (int i = 1; i < linearLayout.getChildCount()-1; i++) {
            View childAt = linearLayout.getChildAt(i);
            EditText editText = (EditText) childAt.findViewById(R.id.tv_standard);
            standard=standard+","+editText.getText().toString();
            EditText score = (EditText) childAt.findViewById(R.id.tv_score);
            standard=standard+","+score.getText().toString();
            if(score.getText().toString().equals("")){
                x=x+0;
            }else {
                x=Integer.parseInt(score.getText().toString())+x;
            }
        }
        if(x!=100){
            return "null";
        }else {
            return  standard;
        }

    }
    //点击选择图片
    private void selectPicture () {
        // TODO Auto-generated method stub
        Intent intent=new Intent();
        intent.setAction(Intent.ACTION_PICK);//Pick an item from the data
        intent.setType("image/*");//从所有图片中进行选择
        startActivityForResult(intent, 1);

    }
    //裁剪选择的图片
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode==RESULT_OK) {//从相册选择照片不裁切
            try {
                Uri selectedImage = data.getData(); //获取系统返回的照片的Uri
                String[] filePathColumn = { MediaStore.Images.Media.DATA };
                Cursor cursor =getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);//从系统表中查询指定Uri对应的照片
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);  //获取照片路径
                cursor.close();

                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
                }
                bitmap= BitmapFactory.decodeFile(picturePath);
                imageView.setImageBitmap(bitmap);
            } catch (Exception e) {
                // TODO Auto-generatedcatch block
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    //网络请求
    public void CreateEventRequest(final String Data) {
        //请求地址
        String url = API.CreateEvent_URL;    //注①
        String tag = "CreateEvent";    //注②

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
                                Toast.makeText(getApplicationContext(), "创建成功",
                                Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(getApplicationContext(), "创建失败",
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
                params.put("EventName", eventName.getText().toString());  //注⑥
                params.put("EventType", eventType.getText().toString());
                params.put("Sponsor", sponsor.getText().toString());
                params.put("StartDate", startDate.getText().toString());
                params.put("Organizer", organizer.getText().toString());
                params.put("Website", website.getText().toString());
                params.put("Introduction", introduction.getText().toString());
                params.put("Standard", Data);
                params.put("Image", Bitmap2StrByBase64(bitmap));
                params.put("State", "1");
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

