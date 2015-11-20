package eu.stepanvyterna.games.utils.myo.myolibrary;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.thalmic.myo.Hub;

public class MyoCustomScanActivity extends Activity {

    private static String TAG = "MyoCustomScanActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        String appId = getIntent().getExtras().getString("appId");

        Hub hub = Hub.getInstance();
        boolean initSuccess = hub.init(UnityUtils.getUnityContext(), appId);

        if (!initSuccess) {
            Log.e(TAG, "Could not initialize the Hub.");
            finish();
            return;
        }

        int widthId = this.getResources().getIdentifier("myosdk__fragment_scan_window_width", "dimen", getPackageName());
        int width = this.getResources().getDimensionPixelSize(widthId);
        int height = this.getResources().getDimensionPixelSize(this.getResources().getIdentifier("myosdk__fragment_scan_window_height", "dimen", getPackageName()));
        if (width > 0 && height > 0) {
            this.getWindow().setLayout(width, height);
        }

        super.onCreate(savedInstanceState);
        int identifier = this.getResources().getIdentifier("myosdk__activity_scan", "layout", getPackageName());
        FrameLayout layout = (FrameLayout) LayoutInflater.from(this).inflate(identifier, null);
        this.setContentView(layout);
        this.getActionBar().setDisplayOptions(0, ActionBar.DISPLAY_SHOW_HOME);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(TAG, "onCreateOptionsMenu");
        menu.add(Menu.NONE, 1337, 1, "Return");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected");
        if (item.getItemId() == 1337) {
            MyoNativeConnector.instance().myoSelected();
            finish();
            return true;
        }
        return false;
    }
}
