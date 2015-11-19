package eu.stepanvyterna.games.utils.myo.myolibrary;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.thalmic.myo.Hub;

/**
 * Created by Steve on 10.11.2015.
 */
public class MyoNativeConnector {

    private static String TAG = "MyoNativeConnector";

    private static MyoNativeConnector _instance;
    private String unityListener;
    private boolean debug;
    private String appId;

    public static MyoNativeConnector instance() {
        if (_instance == null) {
            _instance = new MyoNativeConnector();
            Log.d(TAG, "creating instance");
        }
        return _instance;
    }

    public void init(String listener, String appId, boolean debug) {
        this.unityListener = listener;
        this.debug = debug;
        this.appId = appId;
    }

    public void connectDirectly() {
        Hub hub = Hub.getInstance();
        if (!hub.init(UnityUtils.getUnityContext(), appId)) {
            Log.e(TAG, "Could not initialize the Hub.");
        } else {
            hub.attachToAdjacentMyo();
            myoSelected();
        }
    }

    public void showScanActivity() {
        if (debug) {
            Log.d(TAG, "Showing scan activity");
        }
        Intent intent = new Intent(UnityUtils.getUnityContext(), MyoCustomScanActivity.class);
        Bundle extras = new Bundle();
        extras.putString("appId", appId);
        intent.putExtras(extras);
        UnityUtils.getUnityActivity().startActivity(intent);
    }

    public void myoSelected() {
        UnityUtils.sendUnityMessage(unityListener, UnityListenerMethods.NativeConnector.MYO_SELECTED, "");
    }
}
