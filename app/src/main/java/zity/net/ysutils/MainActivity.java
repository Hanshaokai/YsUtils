package zity.net.ysutils;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.LinkProperties;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import zity.net.basecommonmodule.utils.LogUtils;
import zity.net.ysutils.keepalive.LcbAliveService;
import zity.net.ysutils.service.RegisterService;
import zity.net.ysutils.utils.ToastUtils;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int selfPermission = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (selfPermission == PackageManager.PERMISSION_DENIED) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            } else {
                Intent intent = new Intent(this, SportsActivity.class);
                startActivity(intent);
            }
        }
        // android 5.0 以上监听网络变化的方式
        openNetMonitor();
        // 开启开屏锁屏的广播和电量广播监听
        Intent intent = new Intent(this, RegisterService.class);
        startService(intent);
        //启动保活后台服务
        Intent intent2 = new Intent(MainActivity.this, LcbAliveService.class);
        startService(intent2);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(this, SportsActivity.class);
                startActivity(intent);

            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                } else {
                    ToastUtils.showLong("您拒绝了储存权限，一些功能将不可用");
                }
            }
        }

    }

    private void openNetMonitor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivityManager != null) {
                connectivityManager.requestNetwork(new NetworkRequest.Builder().build(), new ConnectivityManager.NetworkCallback() {
                    // 网络可用的回调
                    @Override
                    public void onAvailable(Network network) {
                        super.onAvailable(network);
                        ToastUtils.showLong("网络可用");
                        zity.net.ysutils.utils.LogUtils.writeLog("网络可用");
                    }

                    // 在网络失去连接的时候回调，但是如果是一个生硬的断开，它可能不回调
                    @Override
                    public void onLosing(Network network, int maxMsToLive) {
                        super.onLosing(network, maxMsToLive);
                        ToastUtils.showLong("网络失去连接");
                        zity.net.ysutils.utils.LogUtils.writeLog("网络失去连接");
                    }

                    // 网络丢失的回调
                    @Override
                    public void onLost(Network network) {
                        super.onLost(network);
                        ToastUtils.showLong("网络丢失");
                        zity.net.ysutils.utils.LogUtils.writeLog("网络丢失");
                    }

                    // 如果在超时时间内都没有找到可用的网络时进行回调
                    @Override
                    public void onUnavailable() {
                        super.onUnavailable();
                        ToastUtils.showLong("网络不可用");
                        zity.net.ysutils.utils.LogUtils.writeLog("网络不可用");
                    }

                    // 当网络某个能力发生了变化回调 可能回调多次
                    @Override
                    public void onCapabilitiesChanged(Network network, NetworkCapabilities networkCapabilities) {
                        super.onCapabilitiesChanged(network, networkCapabilities);
                        ToastUtils.showLong("网络发生变化");
                        zity.net.ysutils.utils.LogUtils.writeLog("网络某个能力发生变化");
                    }

                    //当建立网络连接时，回调连接的属性
                    @Override
                    public void onLinkPropertiesChanged(Network network, LinkProperties linkProperties) {
                        super.onLinkPropertiesChanged(network, linkProperties);
                        LogUtils.d(linkProperties.toString());
                        ToastUtils.showLong("网络属性变化");
                        zity.net.ysutils.utils.LogUtils.writeLog("网络属性变化" + linkProperties.toString());
                    }
                });
            } else {
            }
        }
    }
}
