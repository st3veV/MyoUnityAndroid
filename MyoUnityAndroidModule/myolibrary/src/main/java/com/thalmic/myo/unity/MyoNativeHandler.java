package com.thalmic.myo.unity;

import android.util.Log;
import android.widget.Toast;

import com.thalmic.myo.AbstractDeviceListener;
import com.thalmic.myo.DeviceListener;
import com.thalmic.myo.Hub;
import com.thalmic.myo.Myo;
import com.thalmic.myo.Pose;
import com.thalmic.myo.Quaternion;

/**
 * Created by St3veV on 10.11.2015.
 */
public class MyoNativeHandler {

    private static String TAG = "MyoNativeHandler";

    private static MyoNativeHandler _instance;
    public Myo connectedMyo;

    private String unityListener;
    private boolean debug = false;

    public static MyoNativeHandler instance()
    {
        if(_instance == null)
        {
            _instance = new MyoNativeHandler();
            _instance.log("creating instance");
        }
        return _instance;
    }

    public void init(String unityListener, boolean debug)
    {
        this.unityListener = unityListener;
        this.debug = debug;
        log("init");

        Hub hub = Hub.getInstance();

        DeviceListener mListener = new AbstractDeviceListener() {

            @Override
            public void onOrientationData(Myo myo, long timestamp, Quaternion rotation) {
                sendMessage(UnityListenerMethods.NativeHandler.MYO_ORIENTATION_DATA, String.format("%f %f %f %f", rotation.w(), rotation.x(), rotation.y(), rotation.z()));
            }

            @Override
            public void onPose(Myo myo, long timestamp, Pose pose) {
                String poseText = null;
                log("myo pose performed: " + pose.toString());
                switch (pose) {
                    case UNKNOWN:
                        poseText = "Unknown";
                        break;
                    case REST:
                        poseText = "Rest";
                        break;
                    case DOUBLE_TAP:
                        poseText = "Tap";
                        break;
                    case FIST:
                        poseText = "Fist";
                        break;
                    case WAVE_IN:
                        poseText = "Wave_In";
                        break;
                    case WAVE_OUT:
                        poseText = "Wave_Out";
                        break;
                    case FINGERS_SPREAD:
                        poseText = "Spread";
                        break;
                }
                sendMessage(UnityListenerMethods.NativeHandler.MYO_POSE, poseText);
            }

            @Override
            public void onConnect(Myo myo, long timestamp) {
                Toast.makeText(UnityUtils.getUnityContext(), "Myo Connected!", Toast.LENGTH_SHORT).show();

                Toast.makeText(UnityUtils.getUnityContext(), "Myo settings: Arm=" + myo.getArm().name().toLowerCase() + ", Direction=" + myo.getXDirection().name().toLowerCase(), Toast.LENGTH_LONG).show();

                sendMessage(UnityListenerMethods.NativeHandler.MYO_ARM, myo.getArm().name());
                sendMessage(UnityListenerMethods.NativeHandler.MYO_X_DIRECTION, myo.getXDirection().name());

                MyoNativeHandler.instance().connectedMyo = myo;
            }

            @Override
            public void onDisconnect(Myo myo, long timestamp) {
                Toast.makeText(UnityUtils.getUnityContext(), "Myo Disconnected!", Toast.LENGTH_SHORT).show();
                sendMessage(UnityListenerMethods.NativeHandler.MYO_DISCONNECTED, "");
            }
        };

        hub.addListener(mListener);

        hub.setLockingPolicy(Hub.LockingPolicy.NONE);
    }

    public void myoNotifyUserAction()
    {
        log("myo notify user action");

        if(connectedMyo != null)
        {
            connectedMyo.notifyUserAction();
        }
    }

    public void myoVibrate(String typeName)
    {
        log("vibrate myo: " + typeName);
        if(connectedMyo != null)
        {
            Myo.VibrationType type;
            switch (typeName)
            {
                case "Long":
                    type = Myo.VibrationType.LONG;
                    break;
                case "Medium":
                    type = Myo.VibrationType.MEDIUM;
                    break;
                case "Short":
                default:
                    type = Myo.VibrationType.SHORT;
            }
            connectedMyo.vibrate(type);
        }
    }

    public void myoLock()
    {
        log("myo lock");

        if(connectedMyo != null)
        {
            connectedMyo.lock();
        }
    }

    public void myoUnlock(String unlockTypeName)
    {
        log("myo unlock: " + unlockTypeName);

        if(connectedMyo != null)
        {
            Myo.UnlockType type;
            switch (unlockTypeName)
            {
                case "Hold":
                    type = Myo.UnlockType.HOLD;
                    break;
                case "Timed":
                default:
                    type = Myo.UnlockType.TIMED;
                    break;
            }
            connectedMyo.unlock(type);
        }
    }

    private void log(String message)
    {
        if(debug)
        {
            Log.d(TAG, message);
        }
    }

    private void sendMessage(String method, String payload)
    {
        UnityUtils.sendUnityMessage(unityListener, method, payload);
    }
}
