package com.bluepixel.android.sgpool.ui.parse;

import android.app.Activity;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import com.bluepixel.android.sgpool.common.Log;
import com.parse.ParsePushBroadcastReceiver;

/**
 * Created by thomas_win on 11/12/2014.
 */
public class CustomBroadcastReceiver extends ParsePushBroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        Log.i("TAG", "onReceive : " + intent.getExtras().toString());
    }

    @Override
    protected void onPushReceive(Context context, Intent intent) {
        // super.onPushReceive(context, intent);

        Log.i("TAG", "onPushReceive : " + intent.getExtras().toString());
    }

    @Override
    protected void onPushDismiss(Context context, Intent intent) {
        super.onPushDismiss(context, intent);
        Log.i("TAG", "onPushDismiss : " + intent.toString());

    }

    @Override
    protected void onPushOpen(Context context, Intent intent) {
        super.onPushOpen(context, intent);
        Log.i("TAG", "onPushOpen : " + intent.toString());

    }

    @Override
    protected Class<? extends Activity> getActivity(Context context, Intent intent) {
        return super.getActivity(context, intent);
    }

    @Override
    protected int getSmallIconId(Context context, Intent intent) {
        return super.getSmallIconId(context, intent);
    }

    @Override
    protected Bitmap getLargeIcon(Context context, Intent intent) {
        return super.getLargeIcon(context, intent);
    }

    @Override
    protected Notification getNotification(Context context, Intent intent) {
        return super.getNotification(context, intent);
    }
}
