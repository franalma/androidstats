package com.diusframi.android.stats;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

public class WiifStats {
    private static WiifStats instance;
    private NetworkConnectionChangeReceiver networkReceiver;
    private NetworkInfo info;
    private WiifStats(){}
    private Log Log = com.diusframi.android.stats.Log.getInstance();
    
    public static WiifStats getInstance(){
        if (instance == null){
            instance = new WiifStats();
        }
        return instance; 
    }

    public void init(Context context){
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        info = cm.getActiveNetworkInfo();
        boolean isConnected = info != null && info.isConnectedOrConnecting();
        Log.d("isConnectedToWifi: "+ isConnected);

        if (networkReceiver != null){
            context.unregisterReceiver(networkReceiver);
        }
        networkReceiver = new NetworkConnectionChangeReceiver();
        IntentFilter wifiStatus = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        context.registerReceiver(networkReceiver, wifiStatus);

    }

    public int getWifiSignalStrength(Context context){
        if (context != null){
            WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            int numberOfLevels = 5;
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            int level = WifiManager.calculateSignalLevel(wifiInfo.getRssi(), numberOfLevels);
            return  level;
        }
        return -1;
    }

    public int getWifiIp(Context context){
        if (context != null){
            WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            int numberOfLevels = 5;
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            int ip = wifiInfo.getIpAddress();
            return ip;
        }
        return -1;
    }

    public String getWifiSsid(Context context){
        if (context != null){
            WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService (Context.WIFI_SERVICE);
            WifiInfo info = wifiManager.getConnectionInfo ();
            String ssid  = info.getSSID().replace("\"", "");
            return ssid;
        }
        return null;
    }


    class NetworkConnectionChangeReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            boolean wifi = info.getType() == ConnectivityManager.TYPE_WIFI;
            boolean mobile = info.getType() == ConnectivityManager.TYPE_MOBILE;
            Log.d("connected to wifi: "+ wifi);
            Log.d("connected to mobile: "+ mobile);
            if (wifi) {
                Log.d("wifi strength: "+getWifiSignalStrength(context));
                Log.d("wifi name: "+getWifiSsid(context).replace("\"",""));
                Log.d("wifi ip: "+getWifiIp(context));
            }
        }
    }
}
