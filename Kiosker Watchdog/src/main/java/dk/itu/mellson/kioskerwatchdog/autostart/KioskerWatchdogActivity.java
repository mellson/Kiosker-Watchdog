package dk.itu.mellson.kioskerwatchdog.autostart;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import dk.itu.mellson.kioskerwatchdog.R;

public class KioskerWatchdogActivity extends Activity {

    static final String KIOSKER_WATCHDOG_RUN_ENABLED = "kiosker_watchdog_run_enabled";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void startWatchdog(View view) {
        SharedPreferences.Editor editor = getSharedPreferences(KIOSKER_WATCHDOG_RUN_ENABLED, Context.MODE_PRIVATE).edit();
        editor.putBoolean(KIOSKER_WATCHDOG_RUN_ENABLED, true);
        editor.apply();

        Intent intent = new Intent(this, KioskerWatchdogService.class);
        startService(intent);
    }

    public void stopWatchdog(View view) {
        SharedPreferences.Editor editor = getSharedPreferences(KIOSKER_WATCHDOG_RUN_ENABLED, Context.MODE_PRIVATE).edit();
        editor.putBoolean(KIOSKER_WATCHDOG_RUN_ENABLED, false);
        editor.apply();
    }
}
