package com.youjingjiaoyu.upload.utils;


import android.content.Context;
import android.content.Intent;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.youjingjiaoyu.upload.R;
import com.youjingjiaoyu.upload.interfaces.AppDownloadListener;
import com.youjingjiaoyu.upload.interfaces.AppUpdateInfoListener;
import com.youjingjiaoyu.upload.interfaces.MD5CheckListener;
import com.youjingjiaoyu.upload.model.DownloadInfo;
import com.youjingjiaoyu.upload.service.UpdateService;

/**
 * 添加一些常用的处理
 *
 * @author wangkai
 */
public abstract class RootActivity extends AppCompatActivity {

    public DownloadInfo downloadInfo;

    private final AppDownloadListener appDownloadListener = obtainDownloadListener();

    private final MD5CheckListener md5CheckListener = obtainMd5CheckListener();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        downloadInfo = getIntent().getParcelableExtra("info");
        AppUpdateUtils.Companion.getInstance().addAppDownloadListener(appDownloadListener);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        downloadInfo = getIntent().getParcelableExtra("info");
        AppUpdateUtils.Companion.getInstance().addAppDownloadListener(appDownloadListener);
    }

    /**
     * 检查下载
     */
    private void checkDownload() {
        // 动态注册广播，8.0 静态注册收不到
        // 开启服务注册，避免直接在Activity中注册广播生命周期随Activity终止而终止
        if (AppUpdateUtils.Companion.getInstance().getUpdateConfig().isShowNotification()) {
            startService(new Intent(this, UpdateService.class));
        }
        //直接下载
        doDownload();
        //区分后台下载
//        if (AppUpdateUtils.getInstance().getUpdateConfig().isAutoDownloadBackground()) {
//            doDownload();
//        } else {
//            if (!NetWorkUtils.getCurrentNetType(this).equals("wifi")) {
//                AppUtils.showDialog(this, ResUtils.getString(R.string.wifi_tips), new OnDialogClickListener() {
//                    @Override
//                    public void onOkClick(DialogInterface dialog) {
//                        dialog.dismiss();
//                        //下载
//                        doDownload();
//                    }
//
//                    @Override
//                    public void onCancelClick(DialogInterface dialog) {
//                        dialog.dismiss();
//                    }
//                }, true, ResUtils.getString(R.string.tips), ResUtils.getString(R.string.cancel), ResUtils.getString(R.string.confirm));
//            } else {
//                doDownload();
//            }
//        }
    }


    /**
     * 暂停任务
     */
    public void pauseTask() {
        AppUpdateUtils.Companion.getInstance().pauseTask();
    }

    /**
     * 取消任务
     */
    public void cancelTask() {
        AppUpdateUtils.Companion.getInstance().cancelTask();
    }

    /**
     * 执行下载
     */
    private void doDownload() {
        AppUpdateUtils.Companion.getInstance().addMd5CheckListener(md5CheckListener).download(downloadInfo);
    }

    /**
     * 是否有更新
     *
     * @return AppDownloadListener
     */
    public abstract AppDownloadListener obtainDownloadListener();

    /**
     * 如果需要监听MD5校验结果 自行重写此方法
     *
     * @return MD5CheckListener
     */
    public abstract MD5CheckListener obtainMd5CheckListener();

    /**
     * 如果需要知道是否新版本更新的信息 自行重写此方法
     *
     * @return AppUpdateInfoListener
     */
    public abstract AppUpdateInfoListener obtainAppUpdateInfoListener();

    /**
     * 获取权限
     */
    public void requestPermission() {
        checkDownload();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.dialog_enter, R.anim.dialog_out);
    }

    @Override
    public void onBackPressed() {
        if (downloadInfo != null) {
            if (!downloadInfo.isForceUpdateFlag()) {
                super.onBackPressed();
            }
        } else {
            super.onBackPressed();
        }
    }

    /**
     * 下载
     */
    public void download() {
        if (!AppUpdateUtils.Companion.getInstance().isDownloading()) {
            requestPermission();
        } else {
            pauseTask();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppUpdateUtils.Companion.getInstance().clearAllListener();
    }

    /**
     * 启动Activity
     *
     * @param context
     * @param info
     */
    public static void launchActivity(Context context, DownloadInfo info, Class<?> cla) {
        Intent intent = new Intent(context, cla);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP + Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra("info", info);
        context.startActivity(intent);
    }
}
