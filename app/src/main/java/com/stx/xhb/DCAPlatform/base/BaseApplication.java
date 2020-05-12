package com.stx.xhb.DCAPlatform.base;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.multidex.MultiDex;

import com.umeng.socialize.PlatformConfig;

import org.xutils.x;

import cn.jpush.android.api.JPushInterface;

/**
 * 程序主入口，当程序启动的时候，会调用这个方法
 */
public class BaseApplication extends Application {
    private String UserId;
    private String Type;
    private String UserName;
    private Bitmap Image;

    @Override
    public void onCreate() {
        super.onCreate();
        //初始化xutils的操作
        x.Ext.init(this);
        //设置是否输出日志
        x.Ext.setDebug(true);
        //微信 appid appsecret
        PlatformConfig.setWeixin("wx152334f54a39c3b0", "24949aef9a179c253fdd55f12a576632");
        // QQ和Qzone appid appkey
        PlatformConfig.setQQZone("100424468", "c7394704798a158208a74ab60104f0ba");
        //极光推送初始化
        JPushInterface.init(this);
        JPushInterface.setDebugMode(true);//设置是否开启log日志，正式打包发布时建议关闭使用
    }
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String UserName) {
        this.UserName = UserName;
    }

    public Bitmap getImage(){return Image ;}

    public void  setImage(Bitmap image){this.Image=image;}
}
