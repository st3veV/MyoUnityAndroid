package eu.stepanvyterna.games.utils.myo.myolibrary;

import android.app.Activity;
import android.content.Context;

import com.unity3d.player.UnityPlayer;

/**
 * Created by Steve on 10.11.2015.
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
