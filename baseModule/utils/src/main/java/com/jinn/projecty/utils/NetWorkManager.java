package com.jinn.projecty.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.LinkProperties;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.NetworkRequest;


public class NetWorkManager {

    private static final String TAG ="NetWorkManager";
    private ConnectivityManager mConnectivityManager;
    private static NetWorkManager sInstance;

    private NetWorkManager(Context context){
         mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    public static NetWorkManager getInstance(Context context){
        if(null ==sInstance){
             synchronized (NetWorkManager.class){
                 if(null==sInstance){
                     sInstance = new NetWorkManager(context);
                 }
             }
        }
        return sInstance;
    }

    /**
     * 实时查询网络类型
     * @return
     */
    public int getConnectionType(){
        return mConnectivityManager.getActiveNetworkInfo().getType();
    }

    /**
     * 当前连接的是wifi
     * @return
     */
    public boolean isInWifiState(){
        return getConnectionType() ==ConnectivityManager.TYPE_WIFI;
    }

    /**
     * 当前是否是移动网络
     * @return
     */
    public boolean isInMobileState(){
        return getConnectionType() == ConnectivityManager.TYPE_MOBILE;
    }

    /**
     * 实时查询网络是否已连接
     * @return
     */
    public boolean isNetWorkConnected(){
        if(mConnectivityManager.getActiveNetworkInfo()==null || !mConnectivityManager.getActiveNetworkInfo().isAvailable()){
            return false;
        }else{
            return true;
        }
    }

    public void registerNetListener(){
        NetworkRequest request =new NetworkRequest.Builder().build();
        mConnectivityManager.registerNetworkCallback(request,mCallBack);
    }

    public void unRegisterNetListener(){
        mConnectivityManager.unregisterNetworkCallback(mCallBack);
    }

    private ConnectivityManager.NetworkCallback mCallBack = new ConnectivityManager.NetworkCallback(){
        @Override
        public void onAvailable(Network network) {
            LogUtils.d(TAG,"onAvailable");
            super.onAvailable(network);
        }

        @Override
        public void onLosing(Network network, int maxMsToLive) {
            LogUtils.d(TAG,"onLosing");
            super.onLosing(network, maxMsToLive);
        }

        @Override
        public void onLost(Network network) {
            LogUtils.d(TAG,"onLost");
            super.onLost(network);
        }

        @Override
        public void onUnavailable() {
            LogUtils.d(TAG,"onUnavailable");
            super.onUnavailable();
        }

        @Override
        public void onCapabilitiesChanged(Network network, NetworkCapabilities networkCapabilities) {
            LogUtils.d(TAG,"onCapabilitiesChanged");
            super.onCapabilitiesChanged(network, networkCapabilities);
        }

        @Override
        public void onLinkPropertiesChanged(Network network, LinkProperties linkProperties) {
            LogUtils.d(TAG,"onLinkPropertiesChanged");
            super.onLinkPropertiesChanged(network, linkProperties);
        }

        @Override
        public void onBlockedStatusChanged(Network network, boolean blocked) {
            LogUtils.d(TAG,"onBlockedStatusChanged");
            super.onBlockedStatusChanged(network, blocked);
        }
    };



}
