package com.youjingjiaoyu.upload.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.WindowManager;

import com.youjingjiaoyu.upload.interfaces.AppDownloadListener;
import com.youjingjiaoyu.upload.interfaces.AppUpdateInfoListener;
import com.youjingjiaoyu.upload.interfaces.MD5CheckListener;
import com.youjingjiaoyu.upload.model.DownloadInfo;
import com.youjingjiaoyu.upload.utils.RootActivity;

/**
 * 后台更新
 * @author wangk
 */
public class UpdateBackgroundActivity extends RootActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setGravity(Gravity.START | Gravity.TOP);
        WindowManager.LayoutParams attributes = getWindow().getAttributes();
        attributes.x = 0;
        attributes.y = 0;
        attributes.height = 1;
        attributes.width = 1;

        download();
        finish();
    }

    @Override
    public AppDownloadListener obtainDownloadListener() {
        return null;
    }

    @Override
    public MD5CheckListener obtainMd5CheckListener() {
        return null;
    }

    @Override
    public AppUpdateInfoListener obtainAppUpdateInfoListener() {
        return null;
    }

    /**
     * 启动Activity
     *
     * @param context
     * @param info
     */
    public static void launch(Context context, DownloadInfo info) {
        launchActivity(context, info, UpdateBackgroundActivity.class);
    }

}
