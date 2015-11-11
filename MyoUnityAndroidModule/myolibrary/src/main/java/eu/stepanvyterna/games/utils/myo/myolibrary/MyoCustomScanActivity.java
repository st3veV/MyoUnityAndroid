package eu.stepanvyterna.games.utils.myo.myolibrary;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.thalmic.myo.Hub;
import com.thalmic.myo.scanner.ScanActivity;

public class MyoCustomScanActivity extends ScanActivity {

    private static String TAG = "MyoCustomScanActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Hub hub = Hub.getInstance();
        if (!hub.init(UnityUtils.getUnityContext())) {
            Log.e(TAG, "Could not initialize the Hub.");
            return;
        }

        super.onCreate(savedInstanceState);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(Menu.NONE, 1337, 1, "Return");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == 1337) {
            MyoNativeConnector.instance().myoSelected();
            finish();
            return true;
        }
        return false;
    }
}
