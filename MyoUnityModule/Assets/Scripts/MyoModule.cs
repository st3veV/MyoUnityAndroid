using UnityEngine;
using System.Collections;

public class MyoModule : MonoBehaviour
{
    private GameObject _listener;
    
    public IMyoConnector InitConnector(IMyoNativeConnectorListener connectorListener, bool debug = false)
    {
        MyoConnector myoConnector = Listener.AddComponent<MyoConnector>();
        myoConnector.InitConnector(connectorListener, debug);
        return myoConnector;
    }

    public IMyoHandler InitHandler(IMyoNativeHandlerListener handlerListener, bool debug = false)
    {
        MyoHandler myoHandler = Listener.AddComponent<MyoHandler>();
        myoHandler.InitHandler(handlerListener, debug);
        return myoHandler;
    }

    private GameObject Listener
    {
        get
        {
            if (_listener == null)
            {
                _listener = new GameObject("MyoAndroidListener");
                _listener.transform.SetParent(gameObject.transform);
            }
            return _listener;
        }
    }
}

public class MyoConnector:MonoBehaviour,IMyoConnector
{
    private IMyoNativeConnectorListener _myoConnectorListener;

    public void InitConnector(IMyoNativeConnectorListener connectorListener, bool debug)
    {
        Debug.Log("initializing");
        _myoConnectorListener = connectorListener;
        Connector.Call("init", gameObject.name, debug);
    }

    public void ConnectDirectly()
    {
        Connector.Call("connectDirectly");
    }

    public void ShowScanActivity()
    {
        Connector.Call("showScanActivity");
    }

    public void MyoSelected()
    {
        _myoConnectorListener.MyoSelected();
    }

    private AndroidJavaObject _connectorJavaObject;
    private AndroidJavaObject Connector
    {
        get
        {
            if (_connectorJavaObject == null)
            {
                AndroidJavaClass jc = new AndroidJavaClass("eu.stepanvyterna.games.utils.myo.myolibrary.MyoNativeConnector");
                _connectorJavaObject = jc.CallStatic<AndroidJavaObject>("instance");
                Debug.Log("got instance");
            }
            return _connectorJavaObject;
        }
    }
}

public class MyoHandler:MonoBehaviour,IMyoHandler
{
    private IMyoNativeHandlerListener _myoHandlerListener;

    public void InitHandler(IMyoNativeHandlerListener handlerListener, bool debug)
    {
        Debug.Log("initializing");
        _myoHandlerListener = handlerListener;
        Handler.Call("init", gameObject.name, debug);
    }


    #region Listener for android stuff
    
    public void MyoOrientation(string param)
    {
        string[] arr = param.Split(' ');
        /*
        MyoQaternion myoQuat = new MyoQaternion(
            float.Parse(arr[1]),
            float.Parse(arr[2]),
            float.Parse(arr[3]),
            float.Parse(arr[0])
        );
        thalmicMyo.SetQuaternion(myoQuat);
        */
        _myoHandlerListener.MyoOrientation();
    }

    public void MyoPose(string param)
    {
        /*
        Debug.Log("pose: " + param);
        curPose = param;
        if (param != null)
        {
            switch (param)
            {
                case "Rest":
                    thalmicMyo.pose = Pose.Rest;
                    break;
                case "Tap":
                    thalmicMyo.pose = Pose.DoubleTap;
                    break;
                case "Fist":
                    thalmicMyo.pose = Pose.Fist;
                    break;
                case "Wave_In":
                    thalmicMyo.pose = Pose.WaveIn;
                    break;
                case "Wave_Out":
                    thalmicMyo.pose = Pose.WaveOut;
                    break;
                case "Spread":
                    thalmicMyo.pose = Pose.FingersSpread;
                    break;
            }
        }
        */
        _myoHandlerListener.MyoPose();
    }

    public void MyoArm(string armName)
    {
        Debug.Log("received arm: " + armName);
        /*
        switch (armName)
        {
            case "LEFT":
                thalmicMyo.SetArm(Arm.Left);
                break;
            case "RIGHT":
                thalmicMyo.SetArm(Arm.Right);
                break;
            default:
                thalmicMyo.SetArm(Arm.Unknown);
                break;
        }
        */
        _myoHandlerListener.MyoArm();
    }

    public void MyoDirection(string directionName)
    {
        Debug.Log("received direction: " + directionName);
        /*
        switch (directionName)
        {
            case "TOWARD_ELBOW":
                thalmicMyo.SetDirection(XDirection.TowardElbow);
                break;
            case "TOWARD_WRIST":
                thalmicMyo.SetDirection(XDirection.TowardWrist);
                break;
            default:
                thalmicMyo.SetDirection(XDirection.Unknown);
                break;
        }
        */
        _myoHandlerListener.MyoDirection();
    }

    public void MyoDisconnected()
    {
        _myoHandlerListener.MyoDisconnected();
    }

    /*
    public string GetMyoPose() { return curPose; }
    */
    #endregion
    
    #region Implementation of IMyoHandler
    
    public void Unlock(/*UnlockType type*/)
    {
#if UNITY_ANDROID
        android.Call<int>("myoUnlock", type.ToString());
#endif
    }

    public void Lock()
    {
#if UNITY_ANDROID
        android.Call<int>("myoLock");
#endif
    }

    public void NotifyUserAction()
    {
#if UNITY_ANDROID
        android.Call<int>("myoNotifyUserAction");
#endif 
    }

    public void Vibrate(/*VibrationType type*/)
    {
#if UNITY_ANDROID
        android.Call<int>("myoVibrate", type.ToString());
#endif
    }

    #endregion

    private AndroidJavaObject _handlerJavaObject;
    private AndroidJavaObject Handler
    {
        get
        {
            if (_handlerJavaObject == null)
            {
                AndroidJavaClass jc = new AndroidJavaClass("eu.stepanvyterna.games.utils.myo.myolibrary.MyoNativeHandler");
                _handlerJavaObject = jc.CallStatic<AndroidJavaObject>("instance");
                Debug.Log("got instance");
            }
            return _handlerJavaObject;
        }
    }
}

public interface IMyoConnector
{
    void ConnectDirectly();
    void ShowScanActivity();
}

public interface IMyoHandler
{
    
}