package com.thalmic.myo.unity;

import android.app.Activity;
import android.content.Context;

import com.unity3d.player.UnityPlayer;

/**
 * Created by St3veV on 10.11.2015.
 */
public class UnityUtils {

    public static Activity getUnityActivity()
    {
        return UnityPlayer.currentActivity;
    }

    public static Context getUnityContext()
    {
        return UnityPlayer.currentActivity.getApplicationContext();
    }

    public static void sendUnityMessage(String listener, String method, String payload)
    {
        UnityPlayer.UnitySendMessage(listener, method, payload);
    }
}
