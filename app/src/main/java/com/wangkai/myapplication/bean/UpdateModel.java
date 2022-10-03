package com.wangkai.myapplication.bean;

import androidx.annotation.NonNull;

import com.youjingjiaoyu.upload.model.LibraryUpdateEntity;

/**
 * 这个model是调用接口之后返回的数据
 *
 * @author wangkai
 */
public class UpdateModel implements LibraryUpdateEntity {

    private int code;
    private String msg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }


    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    /**
     * 线上的版本号
     *
     * @return 版本号
     */
    @Override
    public int getAppVersionCode() {
        if (!data.getCv().isEmpty()) {
            return Integer.parseInt(data.getCv());
        }
        return 0;
    }

    /**
     * 版本名称
     *
     * @return 版本名称
     */
    @NonNull
    @Override
    public String getAppVersionName() {
        return data.getVersion();
    }

    /**
     * app下载的url
     *
     * @return url
     */
    @NonNull
    @Override
    public String getAppApkUrls() {
        return data.getDown_url();
    }

    /**
     * 更新日志
     *
     * @return 更新日志
     */
    @NonNull
    @Override
    public String getAppUpdateLog() {
        return data.getContent();
    }

    /**
     * app大小，用来判断是否已经下载过
     *
     * @return app大小
     */
    @NonNull
    @Override
    public String getAppApkSize() {
        return data.getFilesize();
    }

    /**
     * 是否强制更新
     *
     * @return 强制更新
     */
    @Override
    public int forceAppUpdateFlag() {
        return data.getIs_mustup();
    }

    /**
     * 需要强制更新的版本号
     *
     * @return 强制更新的版本号
     */
    @NonNull
    @Override
    public String getAppHasAffectCodes() {
        return "";
    }

    /**
     * app的MD5
     *
     * @return MD5
     */
    @NonNull
    @Override
    public String getFileMd5Check() {
        return "";
    }

    @NonNull
    @Override
    public String getChannel() {
        return data.getChannel();
    }
}
