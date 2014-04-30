package dk.itu.mellson.kioskerwatchdog.autostart;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Toast;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;

public class KioskerWatchdogService extends Service {
    private static final String TAG = "KioskerWatchdog Service";
    private Subscriber<Long> watchDogSubscriber;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onDestroy() {
        Toast.makeText(this, "KioskerWatchdog Service Stopped", Toast.LENGTH_LONG).show();
        Log.d(TAG, "onDestroy");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "Sending you back into Kiosker", Toast.LENGTH_LONG).show();
        Observable.timer(10, TimeUnit.MINUTES).repeat().subscribe(getWatchDogSubscriber());
        backToKiosker(KioskerWatchdogService.this);
        return START_STICKY;
    }

    private Subscriber<? super Long> getWatchDogSubscriber() {
        if (watchDogSubscriber != null && !watchDogSubscriber.isUnsubscribed())
            watchDogSubscriber.unsubscribe();
        watchDogSubscriber = new Subscriber<Long>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Long aLong) {
                boolean runEnabled = getSharedPreferences(KioskerWatchdogActivity.KIOSKER_WATCHDOG_RUN_ENABLED, Context.MODE_PRIVATE).getBoolean(KioskerWatchdogActivity.KIOSKER_WATCHDOG_RUN_ENABLED, true);
                if (!runEnabled) {
                    unsubscribe();
                    return;
                }
                if (((PowerManager) getSystemService(Context.POWER_SERVICE)).isScreenOn())
                    backToKiosker(KioskerWatchdogService.this);
            }
        };
        return watchDogSubscriber;
    }

    private void backToKiosker(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(100);
        String className = taskInfo.get(0).topActivity.getClassName();
        if (className != null && !className.isEmpty()) {
            if (!isClassNameLegal(className)) {
                startKiosker();
            }

        }
    }

    private boolean isClassNameLegal(String className) {
        return className.contains("dk.itu.kiosker.activities.KioskerActivity") ||
                className.contains("dk.itu.kiosker.activities.LogActivity") ||
                className.contains("dk.itu.kiosker.activities.SettingsActivity");
    }

    private void startKiosker() {
        Log.d(TAG, "Starting Kiosker.");
        Intent kioskerIntent = getPackageManager().getLaunchIntentForPackage("dk.itu.kiosker");
        startActivity(kioskerIntent);
    }
}