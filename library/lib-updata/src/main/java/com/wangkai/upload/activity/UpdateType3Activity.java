package com.wangkai.upload.activity;

import android.content.Context;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.wangkai.upload.R;
import com.wangkai.upload.interfaces.AppDownloadListener;
import com.wangkai.upload.interfaces.AppUpdateInfoListener;
import com.wangkai.upload.interfaces.MD5CheckListener;
import com.wangkai.upload.model.DownloadInfo;
import com.wangkai.upload.utils.LogUtils;
import com.wangkai.upload.utils.ResUtils;
import com.wangkai.upload.utils.RootActivity;

/**
 * 默认提供的样式
 *
 * @author wangk
 */
public class UpdateType3Activity extends RootActivity {

    private TextView tvMsg;
    private TextView tvBtn2;
    private ImageView ivClose;
    private TextView tvVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_type3);

        findView();

        setDataAndListener();
    }

    private void setDataAndListener() {
        tvMsg.setText(downloadInfo.getUpdateLog());
        tvMsg.setMovementMethod(ScrollingMovementMethod.getInstance());
        tvVersion.setText("v" + downloadInfo.getProdVersionName());

        if (downloadInfo.isForceUpdateFlag()) {
            ivClose.setVisibility(View.GONE);
        } else {
            ivClose.setVisibility(View.VISIBLE);
        }

        ivClose.setOnClickListener(v -> finish());

        tvBtn2.setOnClickListener(v -> {
            //右边的按钮
            download();
        });
    }

    private void findView() {
        tvMsg = findViewById(R.id.tv_content);
        tvBtn2 = findViewById(R.id.tv_update);
        ivClose = findViewById(R.id.iv_close);
        tvVersion = findViewById(R.id.tv_version);
    }

    @Override
    public AppDownloadListener obtainDownloadListener() {
        return new AppDownloadListener() {
            @Override
            public void downloading(int progress) {
                tvBtn2.setText(ResUtils.getString(R.string.downloading) + progress + "%");
            }

            @Override
            public void downloadFail(String msg) {
                tvBtn2.setText(ResUtils.getString(R.string.btn_update_now));
                Toast.makeText(UpdateType3Activity.this, ResUtils.getString(R.string.apk_file_download_fail), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void downloadComplete(String path) {
                tvBtn2.setText(ResUtils.getString(R.string.btn_update_now));
            }

            @Override
            public void downloadStart() {
                tvBtn2.setText(ResUtils.getString(R.string.downloading));
            }

            @Override
            public void reDownload() {
                LogUtils.log("下载失败后点击重试");
            }

            @Override
            public void pause() {

            }
        };
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
        launchActivity(context, info, UpdateType3Activity.class);
    }

}
