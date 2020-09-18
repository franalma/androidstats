package com.diusframi.android.stats;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;

public class BatteryStats {
    private static BatteryStats instance;
    private Intent batteryStatus;
    private BatteryReceiver batteryReceiver;
    private Log Log = com.diusframi.android.stats.Log.getInstance();

    private BatteryStats(){}

    public static BatteryStats getInstance(){
        if (instance == null){
            instance = new BatteryStats();
        }
        return instance; 
    }

    public void init(Context context){
        if (context != null){
            IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
            filter.addAction(Intent.ACTION_POWER_CONNECTED);
            filter.addAction(Intent.ACTION_POWER_DISCONNECTED);
            filter.addAction(Intent.ACTION_BATTERY_LOW);
            filter.addAction(Intent.ACTION_BATTERY_OKAY);
            if (batteryReceiver != null){
                try{
                    context.unregisterReceiver(batteryReceiver);
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
            batteryReceiver = new BatteryReceiver();
            batteryStatus = context.registerReceiver(batteryReceiver, filter);

        }
    }

    public void stop(Context context){
        context.unregisterReceiver(batteryReceiver);
    }


    class BatteryReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null){
                int batteryLevel = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
                int batteryScale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
                int batteryCurrentStatus = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
                boolean isCharging = batteryCurrentStatus == BatteryManager.BATTERY_STATUS_CHARGING
                        || batteryCurrentStatus == BatteryManager.BATTERY_STATUS_FULL;
                Log.d("batteryIsCharging: "+isCharging);
                if(isCharging){
                    int chargePlug = batteryStatus.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
                    boolean usbCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_USB;
                    boolean acCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_AC;
                    Log.d("batteryIsCharging with ac: "+ acCharge + " usb: "+usbCharge);
                }
                float batteryPct = batteryLevel * 100 / (float)batteryScale;
                Log.d("batteryLevel: "+batteryPct);
                int batteryHealth = batteryStatus.getIntExtra(BatteryManager.EXTRA_HEALTH, 0);
                Log.d("batteryHealth : "+batteryHealth);



            }


        }
    }


}
