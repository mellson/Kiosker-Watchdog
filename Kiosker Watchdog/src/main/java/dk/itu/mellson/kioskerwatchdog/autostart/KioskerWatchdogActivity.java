package dk.itu.mellson.kioskerwatchdog.autostart;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import dk.itu.mellson.kioskerwatchdog.R;

public class KioskerWatchdogActivity extends Activity {

    static final String KIOSKER_WATCHDOG_RUN_ENABLED = "kiosker_watchdog_run_enabled";
    private TextView statusTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        statusTextView = (TextView) findViewById(R.id.statusTextView);
    }

    public void startWatchdog(View view) {
        SharedPreferences.Editor editor = getSharedPreferences(KIOSKER_WATCHDOG_RUN_ENABLED, Context.MODE_PRIVATE).edit();
        editor.putBoolean(KIOSKER_WATCHDOG_RUN_ENABLED, true);
        editor.apply();
        updateStatus();
        Intent intent = new Intent(this, KioskerWatchdogService.class);
        startService(intent);
    }

    public void stopWatchdog(View view) {
        SharedPreferences.Editor editor = getSharedPreferences(KIOSKER_WATCHDOG_RUN_ENABLED, Context.MODE_PRIVATE).edit();
        editor.putBoolean(KIOSKER_WATCHDOG_RUN_ENABLED, false);
        editor.apply();
        updateStatus();
    }

    public Boolean getWatchdogRunning(Activity activity) {
        SharedPreferences prefs = activity.getSharedPreferences(KIOSKER_WATCHDOG_RUN_ENABLED, Context.MODE_PRIVATE);
        return prefs.getBoolean(KIOSKER_WATCHDOG_RUN_ENABLED, false);
    }

    @Override
    public void onResume() {
        updateStatus();
        super.onResume();
    }

    private void updateStatus() {
        String statusString = "Watchdog is " + (getWatchdogRunning(this) ? "running" : "not running");
        statusTextView.setText(statusString);
    }
}
