using UnityEngine;
using System.Collections;

public interface IMyoNativeHandlerListener
{
    void MyoOrientation();
    void MyoPose();
    void MyoArm();
    void MyoDirection();
    void MyoDisconnected();
}

public interface IMyoNativeConnectorListener
{
    void MyoSelected();
}