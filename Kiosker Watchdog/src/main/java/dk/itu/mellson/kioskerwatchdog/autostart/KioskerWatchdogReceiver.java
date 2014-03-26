package dk.itu.mellson.kioskerwatchdog.autostart;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class KioskerWatchdogReceiver extends BroadcastReceiver
{
    public void onReceive(Context arg0, Intent arg1)
    {
        Intent intent = new Intent(arg0,KioskerWatchdogService.class);
        arg0.startService(intent);
    }
}
