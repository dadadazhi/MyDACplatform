package com.stx.xhb.DCAPlatform.utils;

/**

 * 接口地址工具类
 */
public class API {
    //登陆请求
    public static final String Login_URL = "http://120.79.231.209:8080/DACplatform/LoginServlet";
    //注册请求
    public static final String Registered_URL = "http://120.79.231.209:8080/DACplatform/RegisteredServlet";
    //创建赛事请求
    public static final String CreateEvent_URL = "http://120.79.231.209:8080/DACplatform/CreateEventServlet";
    //获取所有赛事请求
    public static final String AllEvent_URL = "http://120.79.231.209:8080/DACplatform/GetEventServlet";
    //获取赛事详细信息
    public static final String EventDetail_URL = "http://120.79.231.209:8080/DACplatform/EventDetailServlet";
    //管理员获得各阶段赛事
    public static final String AdminEvent_URL = "http://120.79.231.209:8080/DACplatform/AdminEventServlet";
    //报名请求
    public static final String SignUpEvent_URL = "http://120.79.231.209:8080/DACplatform/SignUpEventServlet";
    //删除请求
    public static final String DeleteEvent_URL = "http://120.79.231.209:8080/DACplatform/DeleteEventServlet";
    //改变赛事状态请求
    public static final String  ChangeEventState_URL = "http://120.79.231.209:8080/DACplatform/ChangeEventStateServlet";
    //拉取所有评委请求
    public static final String  Judges_URL = "http://120.79.231.209:8080/DACplatform/JudgesServlet";
    //设置赛事评委请求
    public static final String  SetJudges_URL = "http://120.79.231.209:8080/DACplatform/SetJudgesServlet";
    //参赛者获取参加的赛事请求
    public static final String  UserEvent_URL = "http://120.79.231.209:8080/DACplatform/UserEventServlet";
    //参赛者查询赛事状态请求
    public static final String  QueryEvent_URL = "http://120.79.231.209:8080/DACplatform/QueryEventServlet";
    //参赛者查询已结束赛事请求
    public static final String  UserEnd_URL = "http://120.79.231.209:8080/DACplatform/UserEndServlet";
    //上传文件请求
    public static final String  Uploadfile_URL = "http://120.79.231.209:8080/DACplatform/UploadfileServlet";
    //上传用户提交信息至数据库
    public static final String  UserUpload_URL = "http://120.79.231.209:8080/DACplatform/UserUploadServlet";
    //审核者获取需审核的信息
    public static final String  JudgesEvent_URL = "http://120.79.231.209:8080/DACplatform/JudgesEventServlet";
    //上传分数
    public static final String  SetScore_URL = "http://120.79.231.209:8080/DACplatform/SetScoreServlet";
    //查询排名
    public static final String  GetRanking_URL = "http://120.79.231.209:8080/DACplatform/GetRankingServlet";
    //查询所有赛事名称
    public static final String  AllEventName_URL = "http://120.79.231.209:8080/DACplatform/AllEventServlet";
    //发布公告
    public static final String  NewMessage_URL = "http://120.79.231.209:8080/DACplatform/NewMessageServlet";
    //获取消息
    public static final String  GetMessage_URL = "http://120.79.231.209:8080/DACplatform/GetMessageServlet";
    //改变消息状态
    public static final String  ChangeMessage_URL = "http://120.79.231.209:8080/DACplatform/ChangeMessageServlet";
    //修改昵称
    public static final String  ChangeUserName_URL = "http://120.79.231.209:8080/DACplatform/ChangeUserNameServlet";
    //修改头像
    public static final String  ChangeHead_URL = "http://120.79.231.209:8080/DACplatform/ChangeHeadServlet";
    //修改赛事信息
    public static final String  ChangeEvent_URL = "http://120.79.231.209:8080/DACplatform/ChangeEventServlet";
    //修改头像
    public static final String  ChangePassword_URL = "http://120.79.231.209:8080/DACplatform/ChangePasswordServlet";
    //删除消息请求
    public static final String DeleteMessage_URL = "http://120.79.231.209:8080/DACplatform/DelMessageServlet";
}
