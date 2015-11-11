package eu.stepanvyterna.games.utils.myo.myolibrary;

import android.content.Intent;
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

    public static MyoNativeConnector instance()
    {
        if(_instance == null)
        {
            _instance = new MyoNativeConnector();
            Log.d(TAG, "creating instance");
        }
        return _instance;
    }

    public void init(String listener, boolean debug)
    {
        this.unityListener = listener;
        this.debug = debug;
    }

    public void connectDirectly()
    {
        Hub hub = Hub.getInstance();
        if (!hub.init(UnityUtils.getUnityContext())) {
            Log.e(TAG, "Could not initialize the Hub.");
        }
        else
        {
            hub.attachToAdjacentMyo();
            myoSelected();
        }
    }

    public void showScanActivity()
    {
        if(debug){
            Log.d(TAG,"Showing scan activity");
        }
        Intent intent = new Intent(UnityUtils.getUnityContext(), MyoCustomScanActivity.class);
        UnityUtils.getUnityContext().startActivity(intent);
    }

    public void myoSelected() {
        UnityUtils.sendUnityMessage(unityListener, UnityListenerMethods.NativeConnector.MYO_SELECTED, null);
    }
}
