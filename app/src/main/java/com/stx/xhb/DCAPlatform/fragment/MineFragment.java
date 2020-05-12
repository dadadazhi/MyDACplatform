package com.stx.xhb.DCAPlatform.fragment;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
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
import com.stx.xhb.DCAPlatform.activities.AboutActivity;
import com.stx.xhb.DCAPlatform.activities.ChangeActivity;
import com.stx.xhb.DCAPlatform.activities.LoginActivity;
import com.stx.xhb.DCAPlatform.activities.MyEventsActivity;
import com.stx.xhb.DCAPlatform.base.BaseApplication;
import com.stx.xhb.DCAPlatform.utils.API;
import com.stx.xhb.DCAPlatform.utils.DataCleanManager;
import com.stx.xhb.DCAPlatform.utils.ToastUtil;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;

public class MineFragment extends Fragment implements View.OnTouchListener{

    private TextView tv_title;

    TextView mTvCache;
    private Toast mToast;
    private View view;
    private ImageView imageView;
    private TextView textView1,textView2;
    private BaseApplication baseApplication;
    private Dialog mDialog;
    private Bitmap bitmap;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_mine, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }


    //初始化控件
    private void initView() {
        mTvCache=(TextView)view.findViewById(R.id.tv_cache);
        tv_title = (TextView) view.findViewById(R.id.title);
        tv_title.setText("我的");
        mTvCache.setText(DataCleanManager.getTotalCacheSize(getActivity()));
        mToast = Toast.makeText(getActivity(), "", Toast.LENGTH_SHORT);
        baseApplication=(BaseApplication)getActivity().getApplication();
        imageView=(ImageView)view.findViewById(R.id.head_portrait);
        textView1=(TextView)view.findViewById(R.id.username);
        textView2=(TextView)view.findViewById(R.id.user_id);
        imageView.setImageBitmap(baseApplication.getImage());
        textView1.setText(baseApplication.getUserName());
        textView2.setText(baseApplication.getUserId());
    }

    //点击事件
    @OnClick({R.id.setting_iv_ch,R.id.setting_iv_clearCache,R.id.setting_iv_out, R.id.setting_iv_about,R.id.ac_registered,R.id.ac_end,R.id.ac_underway,R.id.head_portrait,R.id.username})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ac_registered:
                Intent intent1 = new Intent(getActivity(), MyEventsActivity.class);
                intent1.putExtra("Type","1");
                startActivity(intent1);
                break;
            case R.id.ac_underway:
                Intent intent2 = new Intent(getActivity(), MyEventsActivity.class);
                intent2.putExtra("Type","2");
                startActivity(intent2);
                break;
            case R.id.ac_end:
                Intent intent3 = new Intent(getActivity(),MyEventsActivity.class);
                intent3.putExtra("Type","3");
                startActivity(intent3);
                break;
            case R.id.setting_iv_clearCache://清理缓存
                if ("0 KB".equals(DataCleanManager.getTotalCacheSize(getActivity()))) {
                    ToastUtil.showAtCenter(getActivity(), "暂无缓存");
                } else {
                    DataCleanManager.clearAllCache(getActivity());
                    mTvCache.setText(DataCleanManager.getTotalCacheSize(getActivity()));
                    ToastUtil.showAtCenter(getActivity(), "缓存清理成功");
                }
                break;
            case R.id.setting_iv_about://关于
                Intent about = new Intent(getActivity(), AboutActivity.class);
                startActivity(about);
                break;
            case R.id.head_portrait:
            case R.id.username:
                showDialog();
                break;
            case R.id.setting_iv_ch:
                Intent intent =new Intent(getActivity(), ChangeActivity.class);
                intent.putExtra("Type","1");
                startActivity(intent);
                break;
            case R.id.setting_iv_out:
                Intent out = new Intent(getActivity(), LoginActivity.class);
                startActivity(out);
                getActivity().finish();
                break;

        }
    }
    /**
     * dialog 初始化
     */
    private void showDialog() {
        mDialog = new Dialog(getContext(), R.style.dialogStyle);
        mDialog.setCanceledOnTouchOutside(true);
        mDialog.setCancelable(true);            //点击框外，框退出
        Window window = mDialog.getWindow();
        window.setGravity(Gravity.BOTTOM);      //位于底部
        window.setWindowAnimations(R.style.dialog_share);    //弹出动画
        View inflate = View.inflate(getContext(), R.layout.personal_dialog, null);
        inflate.findViewById(R.id.dialog_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDialog != null && mDialog.isShowing()){

                    mDialog.dismiss();      //消失，退出
                }
            }
        });
        inflate.findViewById(R.id.change_name).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDialog != null && mDialog.isShowing()){
                    Intent intent =new Intent(getActivity(), ChangeActivity.class);
                    intent.putExtra("Type","0");
                    startActivity(intent);
                    mDialog.dismiss();      //消失，退出
                }
            }
        });
        inflate.findViewById(R.id.change_head).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDialog != null && mDialog.isShowing()){
                    selectPicture();
                    mDialog.dismiss();      //消失，退出

                }
            }
        });
        window.setContentView(inflate);
        //横向充满
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        mDialog.show();
    }


    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onResume(getActivity());       //统计时长

    }

    @Override
    public void onResume() {
        super.onResume();
        textView1.setText(baseApplication.getUserName());
        MobclickAgent.onPause(getActivity());
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_UP){
            onClick(view);
        }
        return false;
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
                Cursor cursor =getActivity().getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);//从系统表中查询指定Uri对应的照片
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);  //获取照片路径
                cursor.close();

                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
                }
                bitmap= BitmapFactory.decodeFile(picturePath);
                baseApplication.setImage(bitmap);
                imageView.setImageBitmap(bitmap);
                Request();
            } catch (Exception e) {
                // TODO Auto-generatedcatch block
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    //上传头像
    public void Request() {
        //请求地址
        String url = API.ChangeHead_URL;    //注①
        String tag = "ChangeHead";    //注②

        //取得请求队列
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

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

                            } else {
                                Toast.makeText(getContext(), "修改失败",
                                        Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(getContext(), "无连接",
                                    Toast.LENGTH_SHORT).show();
                            Log.e("TAG", e.getMessage(), e);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //做自己的响应错误操作，如Toast提示（“请稍后重试”等）

                Toast.makeText(getContext(), "稍后重试",
                        Toast.LENGTH_SHORT).show();
                Log.e("TAG", error.getMessage(), error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("UserId", baseApplication.getUserId());  //注⑥
                params.put("Head",Bitmap2StrByBase64(bitmap));
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

