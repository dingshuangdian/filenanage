package com.lsq.easton.filemanage.base;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

import com.lsq.easton.filemanage.R;
import com.lsq.easton.filemanage.bean.EventCenter;
import com.lsq.easton.filemanage.utils.StatusBarUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;


/**
 * Created by CWJ on 2017/3/17.
 */

public abstract class baseActivity extends AppCompatActivity {
    private PermissionHandler mHandler;
  @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 无标题
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        // 设置竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(getLayoutId());
        StatusBarUtil.setTranslucentStatus(this, R.color.color_48baf3);
        if (this.isBindEventBusHere()) {
            EventBus.getDefault().register(this);
        }
        //ButterKnife.bind(this);
        this.initViewAndEvent();
    }

    public abstract void onEventComming(EventCenter var1);

    public abstract boolean isBindEventBusHere();

    //初始化view
    public abstract void initViewAndEvent();

    //获取布局文件
    public abstract int getLayoutId();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (this.isBindEventBusHere()) {
            EventBus.getDefault().unregister(this);
        }
       // ButterKnife.unbind(this);
    }

    @Subscribe
    public void onEventMainThread(EventCenter eventCenter) {
        if (null != eventCenter) {
            this.onEventComming(eventCenter);
        }

    }

    /**
     * 权限回调接口
     */
    public abstract class PermissionHandler {
        /**
         * 权限通过
         */
        public abstract void onGranted();

        /**
         * 权限拒绝
         */
        public void onDenied() {
            finish();
        }
    }

    //-----------------------------------------------------------

    /**
     * 请求读写SD卡权限
     *
     * @param permissionHandler
     */



}
