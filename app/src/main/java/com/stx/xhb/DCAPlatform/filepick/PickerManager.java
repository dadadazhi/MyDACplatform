package com.stx.xhb.DCAPlatform.filepick;

import com.stx.xhb.DCAPlatform.R;

import java.util.ArrayList;

/**
 * 作者：chs on 2017-08-24 14:34
 * 邮箱：657083984@qq.com
 */

public class PickerManager {
    public static PickerManager getInstance() {
        return SingletonHolder.INSTANCE;
    }
    private static class SingletonHolder{
        private static final PickerManager INSTANCE = new PickerManager();
    }
    /**
     * 最多能选的文件的个数
     */
    public int maxCount = 1;
    /**
     * 保存结果
     */
    public ArrayList<FileEntity> files;
    /**
     * 筛选条件 类型
     */
    public ArrayList<FileType> mFileTypes;
    /**
     * 文件夹筛选
     * 这里包括 微信和QQ中的下载的文件和图片
     */
    public String[] mFilterFolder = new String[]{"MicroMsg/Download","WeiXin","QQfile_recv","MobileQQ/photo"};
    private PickerManager() {
        files = new ArrayList<>();
        mFileTypes = new ArrayList<>();
        addDocTypes();
    }
    public void addDocTypes()
    {

        String[] docs = {"doc","docx", "dot","dotx"};
        mFileTypes.add(new FileType("DOC",docs,R.mipmap.file_picker_word));


    }

    public ArrayList<FileType> getFileTypes() {
        return mFileTypes;
    }


    public PickerManager setMaxCount(int maxCount) {
        this.maxCount = maxCount;
        return this;
    }
}
