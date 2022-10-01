package com.youjingjiaoyu.upload.utils;

import static com.youjingjiaoyu.upload.utils.Constants.*;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public abstract class DownloadReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        String tag = bundle.getString(RECEIVER_TYPE_TAG);
        if (RECEIVER_TAG_ERROR.equals(tag)) {
            String err = bundle.getString(RECEIVER_TAG_ERROR);
            downloadFail(err);
        } else if (RECEIVER_TAG_DOING.equals(tag)) {
            int progress = bundle.getInt(RECEIVER_TAG_DOING_PROGRESS);
            downloading(progress);
            if (progress == RECEIVER_TAG_DOING_PROGRESS_MAX) {
                downloadComplete();
            }
        }
    }

    /**
     * 下载完成调用
     */
    protected abstract void downloadComplete();

    /**
     * 下载中调用
     *
     * @param progress 进度
     */
    protected abstract void downloading(int progress);

    /**
     * 下载失败调用
     *
     * @param e 异常信息
     */
    protected abstract void downloadFail(String e);
}
